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


    double ax, ay, az;
    private SensorManager sensorManager;
    private DatabaseReference mDatabase;
    private String userId;
    private String startTime;

    int interval;
    int duration;
    private String timeStamp;
    private String stringCoord;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("onCreate", "onCreate service START ed");
        mDatabase = FirebaseDatabase.getInstance().getReference();

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);


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
            startTime = extras.getString(Constants.START_TIME_KEY);
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
            String cord = "(" + event.values[0] + "," + event.values[1] + "," + event.values[2] + ")";
            submitData(cord);
            Log.d("onSensorChanged", "ACCELEROMETER COORD = " + cord);
        }
    }

    private void submitData(final String coordinates) {
        timeStamp = new SimpleDateFormat("yyyy/MM/dd_HH:mm:ss", Locale.US).format(Calendar.getInstance().getTime());
        mDatabase.child("users").child(userId).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        if (user != null) {
                            sendDataToFirebase(userId, user.username, timeStamp, coordinates);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );
    }

    private void sendDataToFirebase(String userId, String username, String recordTime, String coord) {

        String key = mDatabase.child("coordinates").push().getKey();
        Coordinates coordinates = new Coordinates(userId, username, recordTime, coord);
        Map<String, Object> coordValues = coordinates.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/coordinates/" + key, coordValues);
        childUpdates.put("/user-coordinates/" + userId + "/" + key, coordValues);
        mDatabase.updateChildren(childUpdates);
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