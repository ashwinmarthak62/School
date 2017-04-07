package com.example.veraki.school.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by veraki on 9/8/2015.
 */
public class BroadCaster extends BroadcastReceiver {
    static int noOfTimes = 0;


    @Override
    public void onReceive(final Context context, Intent intent) {
        // TODO Auto-generated method stub
        noOfTimes++;
        Toast.makeText(context, "BC Service Running for " + noOfTimes + " times", Toast.LENGTH_SHORT).show();
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        client.post("http://192.168.2.4:9000/mysqlsqlitesync/getdbrowcount.php",params ,new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }

        /*    @Override
            public void onSuccess(String response) {
                System.out.println(response);
                try {
                    JSONObject obj = new JSONObject(response);
                    System.out.println(obj.get("count"));
                    if(obj.getInt("count") != 0){
                        final Intent intnt = new Intent(context, MyService.class);
                        intnt.putExtra("intntdata", "Unsynced Rows Count "+obj.getInt("count"));
                        context.startService(intnt);
                    }else{
                        Toast.makeText(context, "Sync not needed", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
*/
          /*  @Override
            public void onFailure(int statusCode, Throwable error,
                                  String content) {
                // TODO Auto-generated method stub
                if(statusCode == 404){
                    Toast.makeText(context, "404", Toast.LENGTH_SHORT).show();
                }else if(statusCode == 500){
                    Toast.makeText(context, "500", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context, "Error occured!", Toast.LENGTH_SHORT).show();
                }
            }*/
        });
    }
}
