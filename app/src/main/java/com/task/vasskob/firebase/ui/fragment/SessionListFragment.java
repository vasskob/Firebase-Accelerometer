package com.task.vasskob.firebase.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;
import com.task.vasskob.firebase.R;
import com.task.vasskob.firebase.database.FirebaseOperations;
import com.task.vasskob.firebase.model.Session;
import com.task.vasskob.firebase.ui.DetailActivity;
import com.task.vasskob.firebase.ui.MainActivity;
import com.task.vasskob.firebase.ui.viewholder.SessionViewHolder;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SessionListFragment extends Fragment {


    @Bind(R.id.recycle_view)
    RecyclerView recyclerView;

    private FirebaseRecyclerAdapter<Session, SessionViewHolder> mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.list_fragment, parent, false);
        ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        LinearLayoutManager mManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                mManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        // Set up FirebaseRecyclerAdapter with the Query
        Query sessionsQuery = getQuery();

        mAdapter = new FirebaseRecyclerAdapter<Session, SessionViewHolder>
                (Session.class, R.layout.session_list_item, SessionViewHolder.class, sessionsQuery) {
            @Override
            protected void populateViewHolder(SessionViewHolder viewHolder, final Session session, final int position) {
                viewHolder.bindToSessions(session);
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), DetailActivity.class);
                        intent.putExtra("sessionId", session.id);
                        startActivity(intent);
                    }
                });
            }
        };

        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAdapter != null) {
            mAdapter.cleanup();
        }
    }

    public Query getQuery() {
        String uid= ((MainActivity) getActivity()).getUid();
        return FirebaseOperations.getRefForSesChild(uid).limitToLast(100);

    }
}