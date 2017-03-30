package com.task.vasskob.firebase.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;


import com.task.vasskob.firebase.R;
import com.task.vasskob.firebase.model.Coordinates;

public class AccelerometerDataViewHolder extends RecyclerView.ViewHolder {

    public TextView recordingTime;
    public TextView accelerometerData;

//    public ImageView starView;
//    public TextView numStarsView;
//    public TextView bodyView;

    public AccelerometerDataViewHolder(View itemView) {
        super(itemView);

        recordingTime = (TextView) itemView.findViewById(R.id.data_time);
        accelerometerData = (TextView) itemView.findViewById(R.id.data_value);
//        starView = (ImageView) itemView.findViewById(R.id.star);
//        numStarsView = (TextView) itemView.findViewById(R.id.post_num_stars);
//        bodyView = (TextView) itemView.findViewById(R.id.post_body);
    }

    public void bindToPost(Coordinates post, View.OnClickListener starClickListener) {
        recordingTime.setText(post.recordTime);
        accelerometerData.setText(post.coord);

//        numStarsView.setText(String.valueOf(post.starCount));
//        bodyView.setText(post.coordinates);
//
//        starView.setOnClickListener(starClickListener);
    }
}
