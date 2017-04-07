package com.example.veraki.school.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.veraki.school.R;
import com.example.veraki.school.model.DatabaseHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;


/**
 *
 */
public class SchoolCalendarFragment extends Fragment {
    private static final String TAG_CALENDAR_EVENT = "calendar_event";
    private static final String TAG_DATE = "calendar_date";
    private static final String TAG_CALENDAR_ID = "id";
    String calendarUrl ="http://shuleyetu.net84.net/school/calendar.php";
    String results ="";
    JSONArray calendar = null;
    String calendarJSON;
    ArrayList<HashMap<String, String>> calendarList;
    ListView list;
    DatabaseHelper dbHelper;

    public SchoolCalendarFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new DatabaseHelper(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_calendar, container, false);

        list = (ListView) rootView.findViewById(R.id.list);
        calendarList = new ArrayList<>();
        calendarList = dbHelper.getSchoolCalendar();
        if(calendarList.size() != 0) {
            ListAdapter adapter = new SimpleAdapter(getActivity().getBaseContext(),
                    calendarList, R.layout.list_item, new String[]{TAG_CALENDAR_EVENT, TAG_DATE},
                    new int[]{R.id.first, R.id.second});

            list.setAdapter(adapter);
        }
        getNewsData();
        return rootView;
    }

    protected void showSchoolCalendar() {
        try {

            calendar = new JSONArray(calendarJSON);
            for (int i = 0; i < calendar.length(); i++) {
                JSONObject c = calendar.getJSONObject(i);
                String calId =c.getString(TAG_CALENDAR_ID);
                String preview = c.getString(TAG_CALENDAR_EVENT);
                String link = c.getString(TAG_DATE);
                HashMap<String, String> calMap = new HashMap<>();
                calMap.put(TAG_CALENDAR_ID, calId);
                calMap.put(TAG_CALENDAR_EVENT, preview);
                calMap.put(TAG_DATE, link);
                dbHelper.insertCalendar(calMap);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getNewsData() {
        class GetDataJSON extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                StringBuffer sb = new StringBuffer("");
                try {
                    URL url = new URL(calendarUrl);
                    HttpURLConnection conne = (HttpURLConnection) url.openConnection();
                    conne.setRequestProperty("User-Agent", "");
                    conne.setRequestMethod("POST");
                    conne.setDoInput(true);
                    conne.connect();

                    InputStream inputStream = conne.getInputStream();

                    BufferedReader read = new BufferedReader(new InputStreamReader(inputStream));
                    String line = "";
                    while ((line = read.readLine()) != null) {
                        sb.append(line);
                    }
                    read.close();
                    results = sb.toString();
                } catch (Exception e) {
                    Log.e("Buffer Error", "Error converting result " + e.toString());
                }

                return results;
            }

            @Override
            protected void onPostExecute(String result) {
                calendarJSON = result;
                showSchoolCalendar();
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute();
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
