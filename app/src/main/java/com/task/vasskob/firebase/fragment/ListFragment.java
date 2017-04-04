package com.task.vasskob.firebase.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.task.vasskob.firebase.DetailActivity;
import com.task.vasskob.firebase.R;
import com.task.vasskob.firebase.model.Coordinates;
import com.task.vasskob.firebase.viewholder.CoordViewHolder;

import butterknife.Bind;
import butterknife.ButterKnife;

public abstract class ListFragment extends Fragment {

    //  private static final String TAG = ListFragment.class.getSimpleName();
    @Bind(R.id.recycle_view)
    RecyclerView recyclerView;

    private DatabaseReference mDatabase;
    private FirebaseRecyclerAdapter<Coordinates, CoordViewHolder> mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.list_fragment, parent, false);
        ButterKnife.bind(this, rootView);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        LinearLayoutManager mManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mManager);

        // Set up FirebaseRecyclerAdapter with the Query
        Query coordinatesQuery = getQuery(mDatabase);
        mAdapter = new FirebaseRecyclerAdapter<Coordinates, CoordViewHolder>
                (Coordinates.class, R.layout.coord_list_item, CoordViewHolder.class, coordinatesQuery) {
            @Override
            protected void populateViewHolder(CoordViewHolder viewHolder, final Coordinates coord, int position) {
//                if (getActivity() instanceof DetailActivity) { // save coordinates to acrivity
                ((DetailActivity) getActivity()).addCoordinates(coord);
//                }
                viewHolder.bindToCoordinates(coord);
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

    public abstract Query getQuery(DatabaseReference databaseReference);

}
