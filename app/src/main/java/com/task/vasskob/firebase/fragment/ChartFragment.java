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
import com.google.firebase.database.ValueEventListener;
import com.task.vasskob.firebase.R;
import com.task.vasskob.firebase.model.Coordinates;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;

import static android.graphics.Color.GREEN;

// https://github.com/lecho/hellocharts-android

public class ChartFragment extends Fragment {
    private long timestamp = 1000000;
    private int coordX = 5;
    @Bind(R.id.chart)
    lecho.lib.hellocharts.view.LineChartView chartView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.chart_fragment, parent, false);
        ButterKnife.bind(this, rootView);

        chartView.setViewportCalculationEnabled(true);
        chartView.setZoomEnabled(false);

//        chartView.setOnValueTouchListener(new LineChartOnValueSelectListener() {
//            @Override
//            public void onValueSelected(int lineIndex, int pointIndex, PointValue value) {
//                Toast.makeText(getActivity(), "Selected: " + value, Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onValueDeselected() {
//
//            }
//        });


        setChartView();

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
        valuesY.add(new PointValue(timestamp, coordX));
        valuesY.add(new PointValue(timestamp + 1000, coordX + 2));
        valuesY.add(new PointValue(timestamp + 2000, coordX + 1));
        valuesY.add(new PointValue(timestamp + 3000, coordX - 2));
        valuesY.add(new PointValue(timestamp + 5400, coordX + 3));

        List<PointValue> valuesZ = new ArrayList<>();

        List<Coordinates> coordinates = new ArrayList<>();


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


        Axis axisT = new Axis(axisValues).setHasTiltedLabels(true);
        Axis axisXYZ = new Axis().setHasLines(true).setHasTiltedLabels(true);


        axisT.setName("Axis T");
        axisXYZ.setName("Axis X, Y, Z");

        data.setAxisXBottom(axisT);
        data.setAxisYLeft(axisXYZ);

        chartView.setLineChartData(data);
    }

    private  void loadCoordinates(){

        ValueEventListener eventListener= new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Coordinates coordinates1=dataSnapshot.getValue(Coordinates.class);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("AsseleromService", "loadCoord:onCancelled", databaseError.toException());
            }
        };
    }
}
