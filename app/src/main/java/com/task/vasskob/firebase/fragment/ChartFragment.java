package com.task.vasskob.firebase.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.task.vasskob.firebase.R;
import com.task.vasskob.firebase.model.Coordinates;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;

// https://github.com/lecho/hellocharts-android

public class ChartFragment extends Fragment {
    @Bind(R.id.chart)
    lecho.lib.hellocharts.view.LineChartView chartView;
    private List<Line> lines = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.chart_fragment, parent, false);
        ButterKnife.bind(this, rootView);

        chartView.setViewportCalculationEnabled(true);
        chartView.setZoomEnabled(false);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void onDrawChart(List<Coordinates> coords) {

        Line lineX = new Line(getXPoints(coords)).setColor(Color.GREEN).setCubic(true);
        lineX.setStrokeWidth(4);
        lineX.setPointRadius(4);

        Line lineY = new Line(getYPoints(coords)).setColor(Color.RED).setCubic(true);
        lineY.setStrokeWidth(3);
        lineY.setPointRadius(4);

        Line lineZ = new Line(getZPoints(coords)).setColor(Color.BLUE).setCubic(true);
        lineZ.setStrokeWidth(2);
        lineZ.setPointRadius(4);

        lines.clear(); //clear old lines
        lines.add(lineX);
        lines.add(lineY);
        lines.add(lineZ);

        LineChartData data = new LineChartData();
        data.setLines(lines);

        chartView.setLineChartData(data);
    }

    @NonNull
    private List<PointValue> getXPoints(List<Coordinates> coords) {
        List<PointValue> pointValues = new ArrayList<>();

        for (Coordinates c : coords) {
            pointValues.add(new PointValue(getTime(coords, c), c.coordinateX));
        }
        return pointValues;
    }

    private long getTime(List<Coordinates> coords, Coordinates c) {
        return Math.abs(c.recordTime - coords.get(0).recordTime) / 1000;
    }

    @NonNull
    private List<PointValue> getYPoints(List<Coordinates> coords) {
        List<PointValue> pointValues = new ArrayList<>();

        for (Coordinates c : coords) {
            pointValues.add(new PointValue(getTime(coords, c), c.coordinateY));
        }
        return pointValues;
    }

    @NonNull
    private List<PointValue> getZPoints(List<Coordinates> coords) {
        List<PointValue> pointValues = new ArrayList<>();

        for (Coordinates c : coords) {
            pointValues.add(new PointValue(getTime(coords, c), c.coordinateZ));
        }
        return pointValues;
    }
}
