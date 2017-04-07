package com.task.vasskob.firebase.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;
import com.task.vasskob.firebase.R;
import com.task.vasskob.firebase.model.Coordinates;

import java.text.SimpleDateFormat;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CoordinateListAdapter extends FirebaseRecyclerAdapter<Coordinates, CoordinateListAdapter.CoordViewHolder> {

    public CoordinateListAdapter(Query ref) {
        super(Coordinates.class, R.layout.coord_list_item, CoordinateListAdapter.CoordViewHolder.class, ref);

    }

    @Override
    protected void populateViewHolder(CoordViewHolder viewHolder, Coordinates coord, int position) {
        viewHolder.bindToCoordinates(coord);
    }

    static class CoordViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.tv_coord_time)
        TextView recordingTime;

        @Bind(R.id.tv_coord_value)
        TextView accelerometerData;

        public CoordViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void bindToCoordinates(Coordinates coordinates) {
            String formattedTime = new SimpleDateFormat("MM/dd/yyyy_HH:mm:ss", Locale.US).format(coordinates.recordTime);
            recordingTime.setText(formattedTime);
            accelerometerData.setText("(" + coordinates.coordinateX + "," + coordinates.coordinateY + "," + coordinates.coordinateZ + ")");
        }
    }

}
