package com.ilicit.rusokoni;




import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ilicit.rusokoni.adapter.MarketAdapter;
        import com.ilicit.rusokoni.entity.AppInfo;
import com.ilicit.rusokoni.helper.JSONParser;
        import com.ilicit.rusokoni.itemanimator.CustomItemAnimator;
import com.ilicit.rusokoni.model.MarketModel;
import com.ilicit.rusokoni.util.UploadHelper;
import com.mikepenz.aboutlibraries.Libs;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.typeface.FontAwesome;

        import org.apache.http.NameValuePair;
        import org.apache.http.message.BasicNameValuePair;
        import org.json.JSONArray;
        import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ActionBarActivity {
    private List<MarketModel> applicationList = new ArrayList<MarketModel>();

    private com.ilicit.rusokoni.adapter.MarketAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ProgressBar mProgressBar;
    JSONArray markets = null;


    private static  String URL_MARKETS;


    JSONParser jsonParser = new JSONParser();
    GpsActivity gpsActivity;
    DrawerLayout mDrawerLayout;

    private static UploadHelper.UploadComponentInfoTask uploadComponentInfoTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        // Handle Toolbar

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Handle DrawerLayout
         mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);

        // Handle ActionBarDrawerToggle
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        actionBarDrawerToggle.syncState();

        // Handle different Drawer States :D
        mDrawerLayout.setDrawerListener(actionBarDrawerToggle);

        // Handle DrawerList
        LinearLayout mDrawerList = (LinearLayout) findViewById(R.id.drawerList);
        gpsActivity = new GpsActivity(savedInstanceState,this);

        // Handle ProgressBar
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);



        mDrawerList.findViewById(R.id.drawer_opensource).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Libs.Builder()
                        .withFields(R.string.class.getFields())
                        .withVersionShown(true)
                        .withLicenseShown(true)
                        .withActivityTitle(getString(R.string.drawer_opensource))
                        .withActivityTheme(R.style.AboutTheme)
                        .start(MainActivity.this);
            }
        });
        ((ImageView) mDrawerList.findViewById(R.id.drawer_opensource_icon)).setImageDrawable(new IconicsDrawable(this, FontAwesome.Icon.faw_github).colorRes(R.color.secondary).actionBarSize());

        // Fab Button


        mRecyclerView = (RecyclerView) findViewById(R.id.list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new CustomItemAnimator());

        //mRecyclerView.setItemAnimator(new ReboundItemAnimator());

        mAdapter = new MarketAdapter(new ArrayList<MarketModel>(), R.layout.row_application, MainActivity.this);
        mRecyclerView.setAdapter(mAdapter);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.theme_accent));
        mSwipeRefreshLayout.setRefreshing(true);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new InitializeApplicationsTask().execute();
            }
        });

        new InitializeApplicationsTask().execute();






        if (savedInstanceState != null) {
            if (uploadComponentInfoTask != null) {
                if (uploadComponentInfoTask.isRunning) {
                    uploadComponentInfoTask.showProgress(this);
                }
            }
        }

        //show progress
        mRecyclerView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    View.OnClickListener fabClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
           uploadComponentInfoTask = UploadHelper.getInstance(MainActivity.this, applicationList).uploadAll();


        }
    };


    public void animateActivity(AppInfo appInfo, View appIcon) {
//        Intent i = new Intent(this, DetailActivity.class);
//        i.putExtra("appInfo", appInfo.getComponentName());
//
//        ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(this, Pair.create((View) mFabButton, "fab"), Pair.create(appIcon, "appIcon"));
//        startActivity(i, transitionActivityOptions.toBundle());
    }


    private class InitializeApplicationsTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            mAdapter.clearApplications();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            applicationList.clear();



            

            List<NameValuePair> paramaters = new ArrayList<NameValuePair>();
            paramaters.add(new BasicNameValuePair(
                    "lat", ""));
            paramaters.add(new BasicNameValuePair(
                    "log", ""));

            if(Utils.getSaved("lat",MainActivity.this).equalsIgnoreCase("")){

                URL_MARKETS ="http://rusokoni.org/index.php/api/rest/markets/format/json";

            }else {
                URL_MARKETS = "http://rusokoni.org/index.php/api/rest/markets/lat/"+Utils.getSaved("lat",MainActivity.this)+"/lng/"+Utils.getSaved("long",MainActivity.this)+"/range/5/format/json";

            }


            // getting JSON string from URL
            String json = jsonParser.makeHttpRequest(URL_MARKETS, "GET",
                    paramaters);

            // Check your log cat for JSON reponse
            Log.e("Markets JSON: ", "> " + json);

            try {

                markets = new JSONArray(json);

                if (markets != null) {
                    ArrayList<MarketModel> m =new Gson().fromJson(json, new TypeToken<List<MarketModel>>() {
                    }.getType());
                    applicationList.addAll(m);



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
            mRecyclerView.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);

            //set data for list
            mAdapter.addApplications(applicationList);
            mSwipeRefreshLayout.setRefreshing(false);

            super.onPostExecute(result);
        }
    }




    @Override
    protected void onResume() {
        super.onResume();
        gpsActivity.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        gpsActivity.onStart();

    }

    @Override
    protected void onPause() {
        super.onPause();
        gpsActivity.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        gpsActivity.onStop();

    }




    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }

    }
}
