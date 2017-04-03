package com.task.vasskob.firebase.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.task.vasskob.firebase.DetailActivity;
import com.task.vasskob.firebase.R;
import com.task.vasskob.firebase.model.Coordinates;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;

import static android.graphics.Color.GREEN;


public class ChartFragment extends Fragment {

    @Bind(R.id.chart)
    lecho.lib.hellocharts.view.LineChartView chartView;

    private Map<String, Coordinates> map;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.chart_fragment, parent, false);
        ButterKnife.bind(this, rootView);

        chartView.setViewportCalculationEnabled(true);
        chartView.setZoomEnabled(false);
        loadCoordinates();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

//    https://github.com/lecho/hellocharts-android/issues/135
//    Axis.setMaxLabelChars(int),
//    https://github.com/lecho/hellocharts-android/issues/63

//    https://github.com/PhilJay/MPAndroidChart/issues/789
//    https://firebase.google.com/docs/database/android/read-and-write

    private void setChartView() {

        List<PointValue> valuesX = new ArrayList<>();
        List<PointValue> valuesY = new ArrayList<>();
        List<PointValue> valuesZ = new ArrayList<>();

        for (Map.Entry<String, Coordinates> entry : map.entrySet()) {
            Coordinates value = entry.getValue();

            float time = value.recordTime - map.values().iterator().next().recordTime ;
            valuesX.add(new PointValue(time, value.coordinateX));
            valuesY.add(new PointValue(time, value.coordinateY));
            valuesZ.add(new PointValue(time, value.coordinateZ));
        }
        for (int i = 0; i < valuesX.size(); i++) {

            Log.d("!!!!!!!1", "setChartView valueX= " + valuesY.get(i));
        }


        List<AxisValue> axisValues = new ArrayList<>();
//        for (int i = 0; i < mData.size(); ++i) {
//            DataItem dataItem = mData.getItem(i);
//            DateTime recordedAt = dataItem.getRecordedAt();
//            String formattedRecordedAt = DateTimeFormatter.getFormattedDateTime(recordedAt);
//            int yValue = dataItem.getYValue();
//            yValues.add(new PointValue(recordedAt.toMiliseconds(), yValue)); //use recordedAt timestamp/day of the year number or something like that as x value
//            AxisValue axisValue = new AxisValue(recordedAt.toMiliseconds());
//            axisValue.setLabel(formattedRecordedAt);
//            axisValues.add(axisValue);
//        }
        //In most cased you can call data model methods in builder-pattern-like manner.
        Line lineX = new Line(valuesX).setColor(Color.RED).setCubic(true);
        lineX.setStrokeWidth(3);
        lineX.setPointRadius(4);

        Line lineY = new Line(valuesY).setColor(GREEN).setCubic(true);
        lineY.setStrokeWidth(3);
        lineY.setPointRadius(4);

        Line lineZ = new Line(valuesZ).setColor(Color.BLUE).setCubic(true);
        lineZ.setStrokeWidth(3);
        lineZ.setPointRadius(4);

        List<Line> lines = new ArrayList<>();
        lines.add(lineX);
        lines.add(lineY);
        lines.add(lineZ);

        LineChartData data = new LineChartData();
        data.setLines(lines);

        Axis axisT = new Axis().setHasTiltedLabels(true);
        Axis axisXYZ = new Axis().setHasLines(true).setHasTiltedLabels(true);
        axisT.setName("Axis T");
        axisXYZ.setName("Axis X, Y, Z");

        data.setAxisXBottom(axisT);
        data.setAxisYLeft(axisXYZ);
        chartView.setLineChartData(data);

    }

    private void loadCoordinates() {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("users-sessions-coordinates").child(((DetailActivity) getActivity()).getUid()).child(((DetailActivity) getActivity()).sessionId);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<Map<String, Coordinates>> t = new
                        GenericTypeIndicator<Map<String, Coordinates>>() {};
                map = dataSnapshot.getValue(t);
                setChartView();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }



}
