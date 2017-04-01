package com.task.vasskob.firebase.service;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.task.vasskob.firebase.Constants;
import com.task.vasskob.firebase.model.Coordinates;
import com.task.vasskob.firebase.model.User;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class AccelerometerService extends Service implements SensorEventListener {

    private static final int SEC_TO_MILISEC = 1000;
    private SensorManager sensorManager;
    private DatabaseReference mDatabase;
    private String userId = "gNKfebW2w2Pn6wGX5ht9NIAmJwI2";

    int interval = Constants.DEFAULT_INTERVAL;
    int duration = Constants.DEFAULT_DURATION;
    private String timeStamp;

    private long lastUpdateTime = 0;
    private long startTime;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("onCreate", "onCreate service START ed");
        mDatabase = FirebaseDatabase.getInstance().getReference();

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        startTime = System.currentTimeMillis();
        //  timer = new Timer();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle extras = intent.getBundleExtra(Constants.OPTIONS_KEY);
        initOptions(extras);
        return super.onStartCommand(intent, flags, startId);
    }

    private void initOptions(Bundle extras) {
        if (extras != null) {
            interval = extras.getInt(Constants.INTERVAL_KEY);
            duration = extras.getInt(Constants.DURATION_KEY);
            userId = extras.getString(Constants.USER_ID);
            Log.d("initOptions ", "SERVICE extras = " + interval + " " + startTime + " " + duration);
        } else {
            Log.d("initOptions ", "bundle is NULL !!!!!!!!! ");
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            int ex = (int) Math.floor(event.values[0]);
            int ey = (int) Math.floor(event.values[1]);
            int ez = (int) Math.floor(event.values[2]);

            submitData(ex, ey, ez);
            Log.d("onSensorChanged", "ACCELEROMETER COORD = " + ex + "," + ey + "," + ez);
        }
    }

    private void submitData(final int ex, final int ey, final int ez) {
        timeStamp = new SimpleDateFormat("yyyy/MM/dd_HH:mm:ss", Locale.US).format(Calendar.getInstance().getTime());
        mDatabase.child("users").child(userId).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        if (user != null) {
                            sendDataToFirebase(userId, user.username, timeStamp, ex, ey, ez);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );
    }

    private void sendDataToFirebase(String userId, String username, String recordTime, int ex, int ey, int ez) {
        long currentTime = System.currentTimeMillis();
        if ((currentTime - lastUpdateTime) > interval * SEC_TO_MILISEC) {

            String key = mDatabase.child("coordinates").push().getKey();
            Coordinates coordinates = new Coordinates(userId, username, recordTime, ex, ey, ez);
            Map<String, Object> coordValues = coordinates.toMap();
            Map<String, Object> childUpdates = new HashMap<>();

            //   childUpdates.put("/coordinates/" + key, coordValues);
            childUpdates.put("/user-coordinates/" + userId + "/" + key, coordValues);
            mDatabase.updateChildren(childUpdates);
            lastUpdateTime = currentTime;
        }
        if ((currentTime - startTime) > duration * Constants.SEC_TO_MILISEC) {
            stopSelf();
            Intent i = new Intent("android.intent.action.MAIN");
            this.sendBroadcast(i);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sensorManager.unregisterListener(this);

        Log.d("onDestroy", "onDestroy service STOP ed");
    }

}