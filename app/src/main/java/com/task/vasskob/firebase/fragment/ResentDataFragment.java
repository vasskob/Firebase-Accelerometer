package com.task.vasskob.firebase.fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.task.vasskob.firebase.MainActivity;

public class ResentDataFragment extends ListFragment {
    public ResentDataFragment() {
    }

    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        return databaseReference.child("user-coordinates").
                child(((MainActivity) getActivity()).getUid()).limitToFirst(100);
    }
}
