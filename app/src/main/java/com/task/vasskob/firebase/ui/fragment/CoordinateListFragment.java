package com.task.vasskob.firebase.ui.fragment;

import android.content.Context;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;
import com.task.vasskob.firebase.database.FirebaseOperations;
import com.task.vasskob.firebase.ui.adapter.CoordinateListAdapter;

public class CoordinateListFragment extends ListFragment {

    private static String userId;
    private static String sessionId;


    public static CoordinateListFragment newInstance(String uid, String sid) {
        CoordinateListFragment f = new CoordinateListFragment();
        userId = uid;
        sessionId = sid;
        return f;
    }

    @Override
    public FirebaseRecyclerAdapter getAdapter(Query query, Context context) {
        return new CoordinateListAdapter(query);
    }

    @Override
    public Query getQuery() {
        return FirebaseOperations.getRefForCoordChild(userId, sessionId).limitToLast(100);
    }
}
