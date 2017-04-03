package com.task.vasskob.firebase.fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.task.vasskob.firebase.DetailActivity;

public class ResentDataFragment extends ListFragment {
    public ResentDataFragment() {
    }

    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        return databaseReference.child("users-sessions-coordinates").
                child(((DetailActivity) getActivity()).getUid()).child(((DetailActivity)getActivity()).sessionId).limitToLast(100);
    }
}
