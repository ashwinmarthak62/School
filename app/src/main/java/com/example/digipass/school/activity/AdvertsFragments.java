package com.example.veraki.school.activity;

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
 *  The Adverts fragment class displays a list of products on offer
 *  @author <a href="mailto:vonunda@gmail.com">Vera</a>
 *  @version beta
 *  $Author$ Vera Onunda
 *  $Date$ 8/24/2015
 */

public class AdvertsFragments extends Fragment {
    private static final String TAG_PRODUCT_ID ="product_id";
    private static final String TAG_PRODUCT_NAME = "product_name";
    private static final String TAG_PRODUCT_PRICE ="product_price";
    private static final String TAG_DESCRIPTION = "product_description";
    private static final String TAG_CREATED_AT ="created_at";
    private static final String TAG_UPDATED_AT ="updated_at";

    String advertsUrl = "http://shuleyetu.net84.net/school/advert.php";
    String dataResults = "";
    JSONArray adverts = null;
    String advertsJSON = "";
    ArrayList<HashMap<String, String>> advertsList;
    ListView listAdverts;
    DatabaseHelper dbHelp;

    public AdvertsFragments() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelp = new DatabaseHelper(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_adverts, container, false);
        listAdverts = (ListView) rootView.findViewById(R.id.list);
        advertsList = new ArrayList<>();
        advertsList = dbHelp.getProductDetails();

        if(advertsList.size() != 0){
            ListAdapter adapter = new SimpleAdapter(getActivity().getBaseContext(),
                    advertsList, R.layout.list_item, new String[]{TAG_PRODUCT_NAME, TAG_DESCRIPTION},
                    new int[]{R.id.first, R.id.second});
            listAdverts.setAdapter(adapter);

        }
        getAdvertData();
        return rootView;
    }

    protected void updateAdverts() {
        try {
            adverts = new JSONArray(advertsJSON);

            for (int i = 0; i < adverts.length(); i++) {
                JSONObject c = adverts.getJSONObject(i);
                String id = c.getString(TAG_PRODUCT_ID);
                String name = c.getString(TAG_PRODUCT_NAME);
                String price= c.getString(TAG_PRODUCT_PRICE);
                String description = c.getString(TAG_DESCRIPTION);
                String created = c.getString(TAG_CREATED_AT);
                String updated = c.getString(TAG_UPDATED_AT);
                HashMap<String, String> advertMap = new HashMap<>();
                advertMap.put(TAG_PRODUCT_ID, id);
                advertMap.put(TAG_PRODUCT_NAME, name);
                advertMap.put(TAG_PRODUCT_PRICE, price);
                advertMap.put(TAG_DESCRIPTION, description);
                advertMap.put(TAG_CREATED_AT, created);
                advertMap.put(TAG_UPDATED_AT, updated);
                dbHelp.insertProducts(advertMap);
            }

        } catch (JSONException e) {
            Log.e("******JSON Parser", "Error parsing data " + e.getMessage());
        }
    }

    private void getAdvertData() {
        class GetDataJSON extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {

                StringBuffer sb = new StringBuffer("");
                try {
                    URL promo = new URL(advertsUrl);
                    HttpURLConnection conne = (HttpURLConnection) promo.openConnection();
                    conne.setRequestProperty("User-Agent", "");
                    conne.setRequestMethod("GET");
                    conne.setDoInput(true);
                    conne.connect();
                    InputStream inputStream = conne.getInputStream();
                    BufferedReader read = new BufferedReader(new InputStreamReader(inputStream));
                    String row = "";
                    while ((row = read.readLine()) != null) {
                        sb.append(row);
                    }
                    read.close();
                    dataResults = sb.toString();
                } catch (Exception e) {
                    Log.e("Buffer Error", "Error converting dataResults " + e.toString());
                }
                return dataResults;
            }

            @Override
            protected void onPostExecute(String result) {
                advertsJSON = result;
                updateAdverts();
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute();
    }
}
