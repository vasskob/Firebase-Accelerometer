package com.task.vasskob.firebase.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Coordinates {

    private String uid;
    private String author;
    public String recordTime;
    public String coord;


    public Coordinates() {
        // Default constructor required for calls to DataSnapshot.getValue(Coordinates.class)
    }

    public Coordinates(String uid, String author, String time, String coord) {
        this.uid = uid;
        this.author = author;
        this.recordTime = time;
        this.coord = coord;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("author", author);
        result.put("recordTime", recordTime);
        result.put("coordinates", coord);
        return result;
    }

}

