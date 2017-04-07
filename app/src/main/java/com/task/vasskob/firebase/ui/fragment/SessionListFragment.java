package com.task.vasskob.firebase.ui.fragment;

import android.content.Context;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;
import com.task.vasskob.firebase.database.FirebaseOperations;
import com.task.vasskob.firebase.ui.adapter.SessionListAdapter;

public class SessionListFragment extends ListFragment {

    private static String userId;

    public static SessionListFragment newInstance(String uid) {
        SessionListFragment f = new SessionListFragment();
        userId = uid;
        return f;
    }

    @Override
    public FirebaseRecyclerAdapter getAdapter(Query query, Context context) {
        return new SessionListAdapter(query, context);
    }

    @Override
    public Query getQuery() {
        return FirebaseOperations.getRefForSesChild(userId).limitToLast(100);
    }
}
