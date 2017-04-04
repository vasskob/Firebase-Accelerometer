package com.task.vasskob.firebase.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.task.vasskob.firebase.Constants;
import com.task.vasskob.firebase.R;
import com.task.vasskob.firebase.database.FirebaseOperations;
import com.task.vasskob.firebase.model.Coordinates;
import com.task.vasskob.firebase.model.Session;
import com.task.vasskob.firebase.model.User;
import com.task.vasskob.firebase.ui.MainActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class AccelerometerService extends Service implements SensorEventListener {

    private String userId;
    private String sessionKey;
    private int interval = Constants.DEFAULT_INTERVAL;
    private int duration = Constants.DEFAULT_DURATION;
    private long lastUpdateTime = 0;
    private long startTime;

    private SensorManager sensorManager;
    private NotificationManagerCompat notificationManager;

    @Override
    public void onCreate() {
        super.onCreate();

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        startTime = System.currentTimeMillis();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle extras = intent.getBundleExtra(Constants.OPTIONS_KEY);
        initOptions(extras);
        initSession();
        return START_REDELIVER_INTENT;
    }

    private void initOptions(Bundle extras) {
        if (extras != null) {
            interval = extras.getInt(Constants.INTERVAL_KEY);
            duration = extras.getInt(Constants.DURATION_KEY);
            userId = extras.getString(Constants.USER_ID);
        } else {
            Log.d("initOptions ", "bundle is NULL !!!!!!!!! ");
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
                submitData(ex, ey, ez);
            }
        }
    }

    private void submitData(final int ex, final int ey, final int ez) {

        FirebaseOperations.getInstanceRef().child(Constants.USERS).child(userId).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        if (user != null) {
                            sendDataToFirebase(userId, System.currentTimeMillis(), ex, ey, ez);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d(" submitData", " onCancelled error " + databaseError);
                    }
                }
        );
    }

    private void sendDataToFirebase(String userKey, long recordTime, int ex, int ey, int ez) {
        long currentTime = System.currentTimeMillis();
        if ((currentTime - lastUpdateTime) > interval * Constants.SEC_TO_MILISEC) {

            String coordinateKey = FirebaseOperations.getChildKey(Constants.COORDINATES);
            Coordinates coordinates = new Coordinates(recordTime, ex, ey, ez);
            FirebaseOperations.sendCoordinatesToDb(userKey, sessionKey, coordinateKey, coordinates);
            lastUpdateTime = currentTime;
            startNotification(0);
        }
        if ((currentTime - startTime) > duration * Constants.SEC_TO_MILISEC) {
            stopSelf();
            notificationManager.cancelAll();
            Intent i = new Intent(Constants.INTENT_ACTION_MAIN);
            this.sendBroadcast(i);
        }
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
                .build();
        notificationManager =
                NotificationManagerCompat.from(context);
        notificationManager.notify(id, notification);
        Log.i("myLog", "NotificationAboutLoading");

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sensorManager.unregisterListener(this);
    }


}