package com.example.veraki.school.activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
 * Created by veraki on 8/24/2015.
 */
public class AlertFragment extends Fragment {
    private static final String TAG_ALERT_ID = "alert_id";
    private static final String TAG_DATE = "alert_date";
    private static final String TAG_MESSAGE= "alert_message";
    String alertUrl ="http://shuleyetu.net84.net/school/alert.php";
    String info ="";
    JSONArray alert = null;
    String alertJSON;
    ArrayList<HashMap<String, String>> alertList;
    ListView list;
    DatabaseHelper dataHelper;

    public AlertFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataHelper = new DatabaseHelper(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_alert, container, false);
        list = (ListView) rootView.findViewById(R.id.list);
        alertList = new ArrayList<>();
        alertList = dataHelper.getAlerts();

        if(alertList.size() != 0) {
            ListAdapter adapter = new SimpleAdapter(getActivity().getBaseContext(),
                    alertList, R.layout.list_item, new String[]{TAG_DATE, TAG_MESSAGE},
                    new int[]{R.id.first, R.id.second});

            list.setAdapter(adapter);
        }
        getAlertData();
        return rootView;
    }

    protected void showAlerts() {
        try {
            alert = new JSONArray(alertJSON);
            for (int i = 0; i < alert.length(); i++) {
                JSONObject c = alert.getJSONObject(i);
                String alertId = c.getString(TAG_ALERT_ID);
                String date = c.getString(TAG_DATE);
                String message = c.getString(TAG_MESSAGE);

             //   if(!alertId.equals("")|| !date.equals("")||!message.equals("")) {
                    HashMap<String, String> newsl = new HashMap<>();
                    newsl.put(TAG_ALERT_ID, alertId);
                    newsl.put(TAG_DATE, date);
                    newsl.put(TAG_MESSAGE, message);

                    dataHelper.insertAlerts(newsl);
              //  }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getAlertData() {
        class GetDataJSON extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                StringBuffer sb = new StringBuffer("");
                try {
                    URL url = new URL(alertUrl);
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
                    info = sb.toString();
                } catch (Exception e) {
                    Log.e("Buffer Error", "Error converting alert info " + e.toString());
                }

                return info;
            }

            @Override
            protected void onPostExecute(String result) {
                alertJSON = result;
                showAlerts();
            }
        }
        GetDataJSON gJson = new GetDataJSON();
        gJson.execute();
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
