package com.task.vasskob.firebase.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.task.vasskob.firebase.R;
import com.task.vasskob.firebase.model.Session;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SessionViewHolder extends RecyclerView.ViewHolder {

    public View mView;
    @Bind(R.id.session_start_time)
    TextView sessionStartTime;
    @Bind(R.id.session_duration)
    TextView sessionDuration;
    @Bind(R.id.session_interval)
    TextView sessionInterval;

    public SessionViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        mView = itemView;
    }

    public void bindToSessions(Session session) {
        sessionStartTime.setText(session.startTime);
        sessionDuration.setText("duration " + session.duration + "s");
        sessionInterval.setText("interval " + session.interval + "s");
    }

}
