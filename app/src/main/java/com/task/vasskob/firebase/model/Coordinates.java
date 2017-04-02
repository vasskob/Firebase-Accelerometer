package com.task.vasskob.firebase.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Coordinates {

    public String uid;
    public String sessid;
    public String recordTime;
    public int coordinateX;
    public int coordinateY;
    public int coordinateZ;


    public Coordinates() {
        // Default constructor required for calls to DataSnapshot.getValue(Coordinates.class)
    }

    public Coordinates(String uid, String sessid, String time, int coordinateX, int coordinateY, int coordinateZ) {
        this.uid = uid;
        this.sessid = sessid;
        this.recordTime = time;
        this.coordinateX = coordinateX;
        this.coordinateY = coordinateY;
        this.coordinateZ = coordinateZ;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("sessid", sessid);
        result.put("recordTime", recordTime);
        result.put("coordinateX", coordinateX);
        result.put("coordinateY", coordinateY);
        result.put("coordinateZ", coordinateZ);
        return result;
    }

}

