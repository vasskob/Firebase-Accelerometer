package com.task.vasskob.firebase.ui.fragment;

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
import com.task.vasskob.firebase.listener.OnSessionClickListener;

import butterknife.Bind;
import butterknife.ButterKnife;

public abstract class ListFragment extends Fragment {

    @Bind(R.id.recycle_view)
    RecyclerView recyclerView;

    private FirebaseRecyclerAdapter mAdapter;

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
        init();
    }

    private void init() {

        mAdapter = getAdapter(getQuery(), getListener());
        recyclerView.setAdapter(mAdapter);

//        ((SessionListAdapter)mAdapter).setOnClickListener(new OnSessionClickListener() {
//            @Override
//            public void onSessionClick(int position) {
//
//            }
//        });



    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAdapter != null) {
            mAdapter.cleanup();
        }
    }

    public abstract FirebaseRecyclerAdapter getAdapter(Query query, OnSessionClickListener onSessionClickListener);

    public abstract Query getQuery();

    public abstract OnSessionClickListener getListener();


}
