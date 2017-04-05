package com.task.vasskob.firebase.ui.fragment;

import com.google.firebase.database.Query;
import com.task.vasskob.firebase.database.FirebaseOperations;
import com.task.vasskob.firebase.ui.DetailActivity;

public class CoordinateListFragment extends ListFragment {
    public CoordinateListFragment() {
    }

    @Override
    public Query getQuery() {
        // TODO: 05/04/17 send this as arguments, don't cast activity
        String uid = ((DetailActivity) getActivity()).getUid();
        String sid = ((DetailActivity) getActivity()).sessionId;
        return FirebaseOperations.getRefForCoordChild(uid, sid).limitToLast(100);
    }
}
