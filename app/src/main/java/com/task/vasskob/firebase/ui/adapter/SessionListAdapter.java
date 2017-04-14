package com.task.vasskob.firebase.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;
import com.task.vasskob.firebase.R;
import com.task.vasskob.firebase.listener.OnSessionClickListener;
import com.task.vasskob.firebase.model.Session;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SessionListAdapter extends FirebaseRecyclerAdapter<Session, SessionListAdapter.SessionViewHolder> {

    private final OnSessionClickListener mOnSessionClickListener;
    private final Context context;


    public SessionListAdapter(Context context, Query ref, OnSessionClickListener listener) {
        super(Session.class, R.layout.session_list_item, SessionViewHolder.class, ref);
        this.mOnSessionClickListener = listener;
        this.context = context;
    }

    @Override
    protected void populateViewHolder(SessionViewHolder viewHolder, final Session session, int position) {
        viewHolder.bindToSessions(session);
    }

    @Override
    public SessionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.session_list_item, parent, false);
        return new SessionViewHolder(itemView, mOnSessionClickListener);
    }

    public static class SessionViewHolder extends RecyclerView.ViewHolder {

        private final OnSessionClickListener mOnSessionClickListener;

        @OnClick(R.id.sessions)
        public void onSessionClick(View v) {
            mOnSessionClickListener.onSessionClick(v);
        }

        View mView;
        @Bind(R.id.session_start_time)
        TextView sessionStartTime;
        @Bind(R.id.session_duration)
        TextView sessionDuration;
        @Bind(R.id.session_interval)
        TextView sessionInterval;

        public SessionViewHolder(View itemView, OnSessionClickListener listener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mView = itemView;
            mOnSessionClickListener = listener;
        }

        void bindToSessions(Session session) {
            sessionStartTime.setText(session.startTime);
            sessionDuration.setText("duration " + session.duration + "s");
            sessionInterval.setText("interval " + session.interval + "s");
        }
    }
}
