package com.example.veraki.school.activity;


import android.app.Activity;
import android.app.AlarmManager;
import android.app.ListFragment;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.veraki.school.R;
import com.example.veraki.school.model.DatabaseHelper;
import com.example.veraki.school.model.NewsLetter;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;


public class NewsLettersFragment extends Fragment {
    private static final String TAG_ISSUE = "issue_no";
    private static final String TAG_PREVIEW = "preview";
    private static final String TAG_LINK = "link";
    String newsUrl ="http://shuleyetu.net84.net/school/newsletter.php";
    String results ="";
    JSONArray news = null;

    String newsJSON;
    ArrayList<HashMap<String, String>> newsletterList;
    ListView list;
    DatabaseHelper dbHelper;

    public NewsLettersFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        dbHelper = new DatabaseHelper(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_newsletter, container, false);

        list = (ListView) rootView.findViewById(R.id.list);
        newsletterList = new ArrayList<>();
        newsletterList= dbHelper.getAllNewsLetters();

        if (newsletterList.size() != 0) {
            ListAdapter adapter = new SimpleAdapter(getActivity().getBaseContext(),
                    newsletterList, R.layout.list_item, new String[]{TAG_PREVIEW, TAG_LINK},
                    new int[]{R.id.first, R.id.second});
            list.setAdapter(adapter);
        }
        getNewsData();
        return rootView;
    }

    protected void updateList() {
        try {
            news = new JSONArray(newsJSON);
            for (int i = 0; i < news.length(); i++) {
                JSONObject c = news.getJSONObject(i);
                String issue = c.getString(TAG_ISSUE);
                String preview = c.getString(TAG_PREVIEW);
                String link = c.getString(TAG_LINK);

                    HashMap<String, String> newsMap = new HashMap<>();
                    newsMap.put(TAG_ISSUE, issue);
                    newsMap.put(TAG_PREVIEW, preview);
                    newsMap.put(TAG_LINK, link);
                    dbHelper.insertNews(newsMap);
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
                    URL url = new URL(newsUrl);
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
                newsJSON = result;
                updateList();
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
