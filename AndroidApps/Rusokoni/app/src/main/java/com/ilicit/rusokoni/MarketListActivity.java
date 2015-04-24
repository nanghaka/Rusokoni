package com.ilicit.rusokoni;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ilicit.rusokoni.adapter.ApplicationAdapter;
import com.ilicit.rusokoni.adapter.PricesAdapter;
import com.ilicit.rusokoni.helper.JSONParser;
import com.ilicit.rusokoni.itemanimator.CustomItemAnimator;
import com.ilicit.rusokoni.model.MarketModel;
import com.ilicit.rusokoni.model.PriceModel;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import com.ilicit.rusokoni.adapter.MarketAdapter;
import com.mikepenz.aboutlibraries.Libs;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.typeface.FontAwesome;

/**
 * Created by Dev on 4/6/2015.
 */
public class MarketListActivity extends Fragment{


    JSONParser jsonParser = new JSONParser();
    JSONArray markets = null;
    RecyclerView items;
    ArrayList<PriceModel> prices = new ArrayList<PriceModel>();
    PricesAdapter marketAdapter;
    private ProgressBar mProgressBar;
    TextView no_data;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =inflater.inflate(R.layout.activity_list_market, container, false);


        // Handle ProgressBar
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        items = (RecyclerView) view.findViewById(R.id.listView);
        no_data =(TextView) view.findViewById(R.id.textView);
        items.setLayoutManager(new LinearLayoutManager(getActivity()));
        items.setItemAnimator(new CustomItemAnimator());

        String id = getArguments().getString("id");


        new getMarketPricesAsync().execute(id);

        items.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);


        return view;
    }





    private class getMarketPricesAsync extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {


            List<NameValuePair> paramaters = new ArrayList<NameValuePair>();




              String  URL_MARKETS ="http://rusokoni.org/index.php/api/rest/prices/id/"+params[0]+"/format/json";




            // getting JSON string from URL
            String json = jsonParser.makeHttpRequest(URL_MARKETS, "GET",
                    paramaters);

            // Check your log cat for JSON reponse
            Log.e("Markets JSON: ", "> " + json);

            try {

                markets = new JSONArray(json);

                if (markets != null) {
                    ArrayList<PriceModel> m =new Gson().fromJson(json, new TypeToken<List<PriceModel>>() {
                    }.getType());

                    prices.addAll(m);




                }else{

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            //handle visibility
            items.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);
            if(prices.size() > 0){
                marketAdapter = new PricesAdapter(prices,R.layout.prices_row_layout,getActivity());
                items.setAdapter(marketAdapter);

            }else{
                no_data.setVisibility(View.VISIBLE);

            }


            super.onPostExecute(result);



        }
    }




}
