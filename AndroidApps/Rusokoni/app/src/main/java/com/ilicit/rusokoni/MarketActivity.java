package com.ilicit.rusokoni;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.ilicit.rusokoni.adapter.*;
import com.ilicit.rusokoni.adapter.MarketAdapter;
import com.ilicit.rusokoni.helper.AlertDialogManager;
import com.ilicit.rusokoni.helper.ConnectionDetector;
import com.ilicit.rusokoni.helper.JSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Dev on 4/8/2015.
 */
public class MarketActivity extends Activity {
    // Connection detector
    ConnectionDetector cd;

    // Alert dialog manager
    AlertDialogManager alert = new AlertDialogManager();

    // Progress Dialog
    private ProgressDialog pDialog;

    // Creating JSON Parser object
    JSONParser jsonParser = new JSONParser();

    ArrayList<HashMap<String, String>> marketsList;


    JSONArray markets = null;

    private static final String URL_MARKETS = "http://rusokoni.org/index.php/api/rest/markets/format/json";

    // ALL JSON node names
    private static final String TAG_ID = "mkt_id";
    private static final String TAG_NAME = "mkt_name";
    RecyclerView lv;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market);

        cd = new ConnectionDetector(getApplicationContext());

        // Check for internet connection
        if (!cd.isConnectingToInternet()) {
            // Internet Connection is not present
            alert.showAlertDialog(this, "Internet Connection Error",
                    "Please connect to working Internet connection", false);
            // stop executing code by return
            return;
        }

        marketsList = new ArrayList<HashMap<String, String>>();

        new LoadMarkets().execute();

        // get listview
        lv = (RecyclerView) findViewById(R.id.list);


        /*lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int arg2,
                                    long arg3) {

                Intent i = new Intent(getApplicationContext(), MarketListActivity.class);

                String mkt_id = ((TextView) view.findViewById(R.id.mkt_id)).getText().toString();
                i.putExtra("mkt_id", mkt_id);

                startActivity(i);
            }
        });*/
    }


    class LoadMarkets extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MarketActivity.this);
            pDialog.setMessage("Listing Markets ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }


        protected String doInBackground(String... args) {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair(
                    "username", ""));
            params.add(new BasicNameValuePair(
                    "password", ""));


            // getting JSON string from URL
            String json = jsonParser.makeHttpRequest(URL_MARKETS, "GET",
                    params,MarketActivity.this);

            // Check your log cat for JSON reponse
            Log.e("Markets JSON: ", "> " + json);

            try {
                markets = new JSONArray(json);

                if (markets != null) {

                    for (int i = 0; i < markets.length(); i++) {
                        JSONObject c = markets.getJSONObject(i);

                        // Storing each json item values in variable
                        String mkt_id = c.getString(TAG_ID);
                        String mkt_name = c.getString(TAG_NAME);

                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        map.put(TAG_ID, mkt_id);
                        map.put(TAG_NAME, mkt_name);


                        // adding HashList to ArrayList
                        marketsList.add(map);
                    }
                }else{
                    Log.d("Markets: ", "null");
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {

            pDialog.dismiss();



        }

    }
}
