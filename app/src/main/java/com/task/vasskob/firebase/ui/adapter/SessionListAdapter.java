package com.task.vasskob.firebase.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;
import com.task.vasskob.firebase.Constants;
import com.task.vasskob.firebase.R;
import com.task.vasskob.firebase.model.Session;
import com.task.vasskob.firebase.ui.DetailActivity;

import butterknife.Bind;
import butterknife.ButterKnife;


public class SessionListAdapter extends FirebaseRecyclerAdapter<Session, SessionListAdapter.SessionViewHolder> {

    private Context context;


    public SessionListAdapter(Class<Session> modelClass, int modelLayout, Class<SessionViewHolder> viewHolderClass, Query ref) {
        super(modelClass, modelLayout, viewHolderClass, ref);
    }


    @Override
    protected void populateViewHolder(SessionViewHolder viewHolder, final Session session, int position) {
        viewHolder.bindToSessions(session);
            viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra(Constants.SESSION_ID, session.id);
                    context.startActivity(intent);
                }
            });


    }


    class SessionViewHolder extends RecyclerView.ViewHolder {

        View mView;
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

        void bindToSessions(Session session) {
            sessionStartTime.setText(session.startTime);
            sessionDuration.setText("duration " + session.duration + "s");
            sessionInterval.setText("interval " + session.interval + "s");
        }

    }

}
