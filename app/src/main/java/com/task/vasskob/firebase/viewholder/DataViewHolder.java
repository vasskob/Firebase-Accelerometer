package com.task.vasskob.firebase.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.task.vasskob.firebase.R;
import com.task.vasskob.firebase.model.Coordinates;

public class DataViewHolder extends RecyclerView.ViewHolder {

    private TextView recordingTime;
    private TextView accelerometerData;

    public DataViewHolder(View itemView) {
        super(itemView);

        recordingTime = (TextView) itemView.findViewById(R.id.data_time);
        accelerometerData = (TextView) itemView.findViewById(R.id.data_value);

    }

    public void bindToCoordinates(Coordinates coordinates) {
        recordingTime.setText(coordinates.recordTime);
        accelerometerData.setText("(" + coordinates.coordinateX + "," + coordinates.coordinateY + "," + coordinates.coordinateZ + ")");
    }
}
