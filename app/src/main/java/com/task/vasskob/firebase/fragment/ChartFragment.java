package com.task.vasskob.firebase.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.task.vasskob.firebase.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;

// https://github.com/lecho/hellocharts-android

public class ChartFragment extends Fragment {

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

    private void setChartView() {

        List<PointValue> valuesX = new ArrayList<>();
        valuesX.add(new PointValue(1, 1));
        valuesX.add(new PointValue(2, 5));
        valuesX.add(new PointValue(3, 3));
        valuesX.add(new PointValue(4, 7));
        valuesX.add(new PointValue(5, -3));
        valuesX.add(new PointValue(6, 0));
        valuesX.add(new PointValue(7, 5));
        valuesX.add(new PointValue(8, 7));


        List<PointValue> valuesY = new ArrayList<>();
        valuesY.add(new PointValue(0, 8));
        valuesY.add(new PointValue(1, 2));
        valuesY.add(new PointValue(2, -4));
        valuesY.add(new PointValue(3, 7));
        valuesY.add(new PointValue(4, 1));
        valuesY.add(new PointValue(5, 5));
        valuesY.add(new PointValue(6, 7));
        valuesY.add(new PointValue(7, 0));


        List<PointValue> valuesZ = new ArrayList<>();
        valuesZ.add(new PointValue(0, 4));
        valuesZ.add(new PointValue(1, 5));
        valuesZ.add(new PointValue(2, 2));
        valuesZ.add(new PointValue(3, 0));
        valuesZ.add(new PointValue(4, 4));
        valuesZ.add(new PointValue(5, 2));
        valuesZ.add(new PointValue(6, 7));
        valuesZ.add(new PointValue(7, 9));


        //In most cased you can call data model methods in builder-pattern-like manner.
        Line lineX = new Line(valuesX).setColor(Color.RED).setCubic(true);
        lineX.setStrokeWidth(3);
        lineX.setPointRadius(4);


        Line lineY = new Line(valuesY).setColor(Color.GREEN).setCubic(true);
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

        Axis axisT = new Axis();
        Axis axisXYZ = new Axis().setHasLines(true);
        axisT.setName("Axis t");
        axisXYZ.setName("Axis x, y, z");
        data.setAxisXBottom(axisT);
        data.setAxisYLeft(axisXYZ);

//        data.setAxisXBottom(Axis.generateAxisFromRange(0, 10, 1));
//        data.setAxisYLeft(Axis.generateAxisFromRange(-8, 16, 1));

        chartView.setLineChartData(data);

    }
}
