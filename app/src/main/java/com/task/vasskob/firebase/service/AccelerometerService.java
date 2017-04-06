package com.task.vasskob.firebase.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.task.vasskob.firebase.Constants;
import com.task.vasskob.firebase.R;
import com.task.vasskob.firebase.SessionOptions;
import com.task.vasskob.firebase.database.FirebaseOperations;
import com.task.vasskob.firebase.model.Coordinates;
import com.task.vasskob.firebase.model.Session;
import com.task.vasskob.firebase.ui.MainActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class AccelerometerService extends Service implements SensorEventListener {

    private static final String BROADCAST_SERVICE_STOP = "stop";
    private String userId;
    private String sessionKey;
    private int interval = Constants.DEFAULT_INTERVAL;
    private int duration = Constants.DEFAULT_DURATION;
    private long lastUpdateTime = 0;
    private long startTime;

    private SensorManager sensorManager;
    private NotificationManagerCompat notificationManager;

    final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (action.equals(BROADCAST_SERVICE_STOP)) {
                stopService();
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        startTime = System.currentTimeMillis();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BROADCAST_SERVICE_STOP);
        registerReceiver(broadcastReceiver, intentFilter);

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle b = intent.getBundleExtra(Constants.OPTIONS_KEY);
        SessionOptions sessionOptions = b.getParcelable(Constants.OPTIONS_KEY);
        initOptions(sessionOptions);
        initSession();
        serviceStarted();
        return START_REDELIVER_INTENT;
    }

    private void initOptions(SessionOptions options) {
        if (options != null) {
            interval = options.getInterval();
            duration = options.getDuration();
            userId = options.getUid();
        } else {
            Log.d("initOptions ", "options is NOT SELECTED !!!!!!!! ");
        }
    }

    private void initSession() {
        sessionKey = FirebaseOperations.getChildKey(Constants.SESSIONS);
        Session session = new Session(sessionKey, interval, duration,
                getFormattedCurrentTime());
        FirebaseOperations.sendSessionToDb(userId, sessionKey, session);
    }

    private String getFormattedCurrentTime() {
        return new SimpleDateFormat(Constants.TIME_FORMAT, Locale.US).format(Calendar.getInstance().getTime());
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            int ex = (int) Math.floor(event.values[0]);
            int ey = (int) Math.floor(event.values[1]);
            int ez = (int) Math.floor(event.values[2]);
            if (userId != null) {
                sendDataToFirebase(userId, System.currentTimeMillis(), ex, ey, ez);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }


    private void sendDataToFirebase(String userKey, long recordTime, int ex, int ey, int ez) {
        long currentTime = System.currentTimeMillis();
        if ((currentTime - lastUpdateTime) > interval * Constants.SEC_TO_MILISEC) {

            String coordinateKey = FirebaseOperations.getChildKey(Constants.COORDINATES);
            Coordinates coordinates = new Coordinates(recordTime, ex, ey, ez);
            // TODO: 05/04/17 you don't need coordinateKey, this value depend on internal state of objec
            FirebaseOperations.sendCoordinatesToDb(userKey, sessionKey, coordinateKey, coordinates);
            lastUpdateTime = currentTime;

        }
        if ((currentTime - startTime) > duration * Constants.SEC_TO_MILISEC) {
            stopService();
            // TODO: 05/04/17 better to write own action, that describe the event or use EventBus
        }
    }

    private void stopService() {
        stopSelf();
        notificationManager =
                NotificationManagerCompat.from(getApplicationContext());
        notificationManager.cancelAll();
        Intent i = new Intent(Constants.INTENT_ACTION_MAIN);
        i.putExtra(Constants.SERVISE_IS_RUN, false);
        this.sendBroadcast(i);
    }

    private void serviceStarted() {
        Intent i = new Intent(Constants.INTENT_ACTION_MAIN);
        i.putExtra(Constants.SERVISE_IS_RUN, true);
        this.sendBroadcast(i);
        startNotification(0);
    }

    private void startNotification(int id) {

        Context context = getApplicationContext();
        Resources resources = context.getResources();
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(context, 0, intent, 0);
        Notification notification = new NotificationCompat.Builder(context)
                .setTicker(resources.getString(R.string.app_name))
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(getResources().getString(R.string.notification_title))
                .setContentText(getResources().getString(R.string.notification_text))
                .setContentIntent(pi)
                .addAction(R.drawable.ic_cancel, "STOP", makePendingIntent(BROADCAST_SERVICE_STOP))
                .setAutoCancel(false)
                .setOngoing(true)
                .build();
        notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(id, notification);
        Log.i("myLog", "NotificationAboutLoading");

    }

    private PendingIntent makePendingIntent(String broadcast) {
        Intent intent = new Intent(broadcast);
        return PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sensorManager.unregisterListener(this);
        unregisterReceiver(broadcastReceiver);
    }

}