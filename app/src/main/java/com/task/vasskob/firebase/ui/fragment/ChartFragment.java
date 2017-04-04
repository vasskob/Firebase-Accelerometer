package com.task.vasskob.firebase.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.task.vasskob.firebase.Constants;
import com.task.vasskob.firebase.R;
import com.task.vasskob.firebase.database.FirebaseOperations;
import com.task.vasskob.firebase.model.Coordinates;
import com.task.vasskob.firebase.ui.DetailActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;


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

    private void setChartView() {

        List<PointValue> valuesX = new ArrayList<>();
        List<PointValue> valuesY = new ArrayList<>();
        List<PointValue> valuesZ = new ArrayList<>();

        if (map != null) {
            HashMap<String, Coordinates> copy = new HashMap<>(map);
            List<Coordinates> coordinatesList = sortCoordinatesMap(copy);

            for (int i = 0; i < coordinatesList.size(); i++) {

                float startTimeInSec = (coordinatesList.get(i).recordTime - coordinatesList.get(0).recordTime) / Constants.SEC_TO_MILISEC;
                valuesX.add(new PointValue(startTimeInSec, coordinatesList.get(i).coordinateX));
                valuesY.add(new PointValue(startTimeInSec, coordinatesList.get(i).coordinateY));
                valuesZ.add(new PointValue(startTimeInSec, coordinatesList.get(i).coordinateZ));
                Log.d("ChartFragment", "setChartView valueX= " + valuesX.get(i));
            }
        }

        Line lineX = new Line(valuesX).setColor(Color.RED).setCubic(true).setStrokeWidth(3).setPointRadius(4);
        Line lineY = new Line(valuesY).setColor(Color.GREEN).setCubic(true).setStrokeWidth(3).setPointRadius(4);
        Line lineZ = new Line(valuesZ).setColor(Color.BLUE).setCubic(true).setStrokeWidth(3).setPointRadius(4);

        List<Line> lines = new ArrayList<>(Arrays.asList(lineX, lineY, lineZ));
        LineChartData data = new LineChartData().setLines(lines);

        Axis axisT = new Axis().setHasTiltedLabels(true).setName("Axis T");
        Axis axisXYZ = new Axis().setHasLines(true).setHasTiltedLabels(true).setName("Axis X, Y, Z");

        data.setAxisXBottom(axisT);
        data.setAxisYLeft(axisXYZ);
        chartView.setLineChartData(data);


    }

    private void loadCoordinates() {
        String uid = ((DetailActivity) getActivity()).getUid();
        String sid = ((DetailActivity) getActivity()).sessionId;
        DatabaseReference ref = FirebaseOperations.getRefForCoordChild(uid, sid);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<Map<String, Coordinates>> t = new
                        GenericTypeIndicator<Map<String, Coordinates>>() {
                        };
                map = dataSnapshot.getValue(t);
                setChartView();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("ChartFragment", "loadCoordinates onCancelled  " + databaseError);
            }
        });

    }

    public static List<Coordinates> sortCoordinatesMap(
            HashMap<String, Coordinates> map) {
        if (map != null) {
            List<Coordinates> values = new ArrayList<>();
            values.addAll(map.values());
            Collections.sort(values, new Comparator<Coordinates>() {
                public int compare(Coordinates c1, Coordinates c2) {
                    return Long.compare(c1.recordTime, c2.recordTime);
                }
            });
            return values;
        }
        return null;
    }

}
