package com.example.veraki.school.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.Toast;

import com.example.veraki.school.R;

/**
 *
 */
public class EventsFragment extends Fragment implements CalendarView.OnDateChangeListener {
    CalendarView calendar;


    public EventsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void initializeCalendar() {
        calendar.setShowWeekNumber(false);
        calendar.setFirstDayOfWeek(2);
       /* calendar.setSelectedWeekBackgroundColor(getResources().getColor(R.color.gray));
        calendar.setWeekSeparatorLineColor(getResources().getColor(R.color.windowBackground));
        calendar.setSelectedDateVerticalBar(R.color.darkgreen);*/
        calendar.setOnDateChangeListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_events_alerts, container, false);
        calendar=(CalendarView) rootView.findViewById(R.id.calendar);
        initializeCalendar();
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {

            Toast.makeText(getActivity(), "Selected Date is\n\n" + dayOfMonth + " / " + month
                            + " / " + year, Toast.LENGTH_LONG).show();
    }
}
