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

    public SessionListAdapter(Query ref, Context context) {
        super(Session.class, R.layout.session_list_item, SessionViewHolder.class, ref);
        this.context = context;
    }


    @Override
    protected void populateViewHolder(SessionViewHolder viewHolder, final Session session, int position) {
        viewHolder.bindToSessions(session, context, viewHolder.getAdapterPosition());
    }


    public static class SessionViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.session_start_time)
        TextView sessionStartTime;
        @Bind(R.id.session_duration)
        TextView sessionDuration;
        @Bind(R.id.session_interval)
        TextView sessionInterval;

        View mView;
        Context mContext;
        Session mSession;
        int mPosition;

        public SessionViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mView = itemView;
            mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO: 11/04/17 do not overload your adapter, send session click event to activity/fragment and handle there
                    // TODO: 11/04/17 about properly on click, check https://youtu.be/imsr8NrIAMs?t=34m52s
                    if (mPosition != RecyclerView.NO_POSITION){
                        Intent intent = new Intent(mContext, DetailActivity.class);
                        intent.putExtra(Constants.SESSION_ID, mSession.id);
                        mContext.startActivity(intent);
                    }
                }
            });
        }

        void bindToSessions(Session session, Context context, int position) {
            sessionStartTime.setText(session.startTime);
            sessionDuration.setText("duration " + session.duration + "s");
            sessionInterval.setText("interval " + session.interval + "s");
            mContext = context;
            mSession = session;
        }
    }
}
