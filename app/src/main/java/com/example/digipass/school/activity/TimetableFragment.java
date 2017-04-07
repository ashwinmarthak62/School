package com.example.veraki.school.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.veraki.school.R;



public class TimetableFragment extends Fragment {
    TableLayout timetable;
    int numvals = 5;
    public static final int MONDAY= 0;
    public static final int TUESDAY= 1;
    public static final int WENESDAY = 2;
    public static final int THURSDAY = 3;
    public static final int FRIDAY= 4;

    String[] weekDays = new String[5];
    String[] subjectsId = new String[5]; // Get the values from JSON here


    public TimetableFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        if (container == null)
        {
            return null;
        }

        weekDays[MONDAY] = "MONDAY";
        weekDays[TUESDAY] = "TUESDAY";
        weekDays[WENESDAY] = "WENESDAY";
        weekDays[THURSDAY] = "THURSDAY";
        weekDays[FRIDAY] = "FRIDAY ";

        subjectsId[MONDAY] = "Math";
        subjectsId[TUESDAY] = "English";
        subjectsId[WENESDAY] = "Kiswahili";
        subjectsId[THURSDAY] = "GHC";
        subjectsId[FRIDAY] = "Music";
        View rootView = inflater.inflate(R.layout.fragment_timetable, container, false);
        timetable = (TableLayout)rootView.findViewById(R.id.schoolTimetable);

        for (int i = 0; i < numvals; i++)
        {
            // Make TR
            TableRow tr = new TableRow(getActivity());
            tr.setId(100 + i);
            tr.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            TextView weekDaystv = new TextView(getActivity());
            weekDaystv.setId(200 + i);
            weekDaystv.setText(weekDays[i]);
            tr.addView(weekDaystv);


            TextView valstv = new TextView(getActivity());
            valstv.setId(300 + i);
            valstv.setText(subjectsId[i]);
            tr.addView(valstv);

            timetable.addView(tr, new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
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
}
