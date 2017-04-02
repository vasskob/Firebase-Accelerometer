package com.task.vasskob.firebase.model;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by anonymous on 01.04.17
 */

public class Session {

    public String startTime;
    public int interval;
    public int duration;
    public String userId;

    public Session() {
        // Default constructor required for calls to DataSnapshot.getValue(Coordinates.class)
    }

    public Session(String userId, int interval, int duration, String startTime) {
       this.userId = userId;
        this.interval = interval;
        this.duration = duration;
        this.startTime = startTime;


    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", userId);
        result.put("interval", interval);
        result.put("duration", duration);
        result.put("startTime", startTime);
        return result;
    }



}
