package com.task.vasskob.firebase.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.task.vasskob.firebase.R;
import com.task.vasskob.firebase.model.Session;

public class SessionViewHolder extends RecyclerView.ViewHolder {

    public View mView;
    private TextView sessionStartTime;
    private TextView sessionDuration;
    private TextView sessionInterval;

    public SessionViewHolder(View itemView) {
        super(itemView);
        sessionStartTime = (TextView) itemView.findViewById(R.id.session_start_time);
        sessionDuration = (TextView) itemView.findViewById(R.id.session_duration);
        sessionInterval = (TextView) itemView.findViewById(R.id.session_interval);
        mView = itemView;
    }

    public void bindToSessions(Session session) {
        sessionStartTime.setText(session.startTime);
        sessionDuration.setText("duration " + session.duration + "s");
        sessionInterval.setText("interval " + session.interval + "s");
    }

}
