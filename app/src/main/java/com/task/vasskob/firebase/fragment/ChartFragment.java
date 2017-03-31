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
import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.ChartData;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.view.LineChartView;

// https://github.com/lecho/hellocharts-android

public class ChartFragment extends Fragment {

    @Bind(R.id.chart)
    lecho.lib.hellocharts.view.LineChartView chartView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.chart_fragment, parent, false);
        ButterKnife.bind(this, rootView);

        chartView.setViewportCalculationEnabled(false);
        chartView.setInteractive(true);
        chartView.setZoomType(ZoomType.HORIZONTAL_AND_VERTICAL);
        chartView.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);

        setChartView();

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    private void setChartView() {

        List<PointValue> values = new ArrayList<>();
        values.add(new PointValue(0, 3));
        values.add(new PointValue(1, 2));
        values.add(new PointValue(2, 3));
        values.add(new PointValue(5, 4));

        List<Float> axis = new ArrayList<>();
        axis.add(0f);
        axis.add(5f);
        axis.add(10f);

        //In most cased you can call data model methods in builder-pattern-like manner.
        Line line = new Line(values).setColor(Color.BLUE).setCubic(true);
        List<Line> lines = new ArrayList<>();
        lines.add(line);
        line.setStrokeWidth(10);

        LineChartData data = new LineChartData();
        data.setLines(lines);
        data.setAxisXBottom(Axis.generateAxisFromCollection(axis));
        data.setAxisYLeft(Axis.generateAxisFromRange(0f,10f,1f));

        chartView = new LineChartView(getContext());
        chartView.setLineChartData(data);
    }
}
