package com.task.vasskob.firebase.ui.fragment;

import android.content.Intent;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;
import com.task.vasskob.firebase.Constants;
import com.task.vasskob.firebase.database.FirebaseOperations;
import com.task.vasskob.firebase.listener.OnSessionClickListener;
import com.task.vasskob.firebase.model.Session;
import com.task.vasskob.firebase.ui.DetailActivity;
import com.task.vasskob.firebase.ui.adapter.SessionListAdapter;

public class SessionListFragment extends ListFragment {

    private static String userId;

    private OnSessionClickListener mOnSessionClickListener = new OnSessionClickListener() {
        @Override
        public void onSessionClick(View view) {

            int position = recyclerView.getChildAdapterPosition(view);
            Session session = (Session) mAdapter.getItem(position);
            Intent intent = new Intent(getActivity(), DetailActivity.class);
            intent.putExtra(Constants.SESSION_ID, session.id);
            startActivity(intent);
        }
    };

    public static SessionListFragment newInstance(String uid) {
        SessionListFragment f = new SessionListFragment();
        userId = uid;
        return f;
    }

    @Override
    public FirebaseRecyclerAdapter getAdapter(Query query, OnSessionClickListener mOnSessionClickListener) {
        return new SessionListAdapter(getActivity(), query, mOnSessionClickListener);
    }

    @Override
    public Query getQuery() {
        return FirebaseOperations.getRefForSesChild(userId).limitToLast(100);
    }

    @Override
    public OnSessionClickListener getListener() {
        return mOnSessionClickListener;
    }
}
