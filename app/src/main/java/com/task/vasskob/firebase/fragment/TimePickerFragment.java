package com.task.vasskob.firebase.fragment;


import android.app.AlarmManager;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.TimePicker;

import com.task.vasskob.firebase.Constants;
import com.task.vasskob.firebase.MainActivity;
import com.task.vasskob.firebase.service.AccelerometerService;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //Use the current time as the default values for the time picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        //Create and return a new instance of TimePickerDialog
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    //onTimeSet() callback method
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        ((MainActivity) getActivity()).startTime = hourOfDay + ":" + minute;
        runServiceOnTime(hourOfDay, minute);
    }

    private void runServiceOnTime(int hourOfDay, int minute) {
        Calendar cur_cal = new GregorianCalendar();
        cur_cal.setTimeInMillis(System.currentTimeMillis());

        long selectMinutes = hourOfDay * Constants.HOUR_TO_MIN + minute;
        long currentMinutes = cur_cal.get(Calendar.HOUR_OF_DAY) * Constants.HOUR_TO_MIN + cur_cal.get(Calendar.MINUTE);
        long delayInMinutes;

        if (currentMinutes <= selectMinutes) {
            delayInMinutes = selectMinutes - currentMinutes;

        } else {
            delayInMinutes = selectMinutes + Constants.DAY_TO_MINUTES - currentMinutes;
        }

        Intent intent = new Intent(getActivity(), AccelerometerService.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.USER_ID, ((MainActivity) getActivity()).getUid());
        bundle.putInt(Constants.INTERVAL_KEY, ((MainActivity) getActivity()).interval);
        bundle.putInt(Constants.DURATION_KEY, ((MainActivity) getActivity()).duration);
        intent.putExtra(Constants.OPTIONS_KEY, bundle);
        PendingIntent pendingIntent = PendingIntent.getService(getActivity(), 0, intent, 0);
        AlarmManager alarm = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        alarm.set(AlarmManager.RTC, System.currentTimeMillis() + delayInMinutes * Constants.MIN_TO_MILISEC, pendingIntent);

        Log.d("runServiceOnTime", " DELAY = " + delayInMinutes + "min !!!!!!! selectMinutes = " + selectMinutes + " currentMin = " + currentMinutes);
    }
}