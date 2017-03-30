package com.task.vasskob.firebase.fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

public class ResentDataFragment extends ListFragment {
    public  ResentDataFragment(){}
    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        Query recentDataQuery=databaseReference.child("coordinates").limitToFirst(100);
        return recentDataQuery;
    }
}
