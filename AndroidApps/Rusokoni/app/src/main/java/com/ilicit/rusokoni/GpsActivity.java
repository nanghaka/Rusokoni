package com.ilicit.rusokoni;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Getting Location Updates.
 *
 * Demonstrates how to use the Fused Location Provider API to get updates about a device's
 * location. The Fused Location Provider is part of the Google Play services location APIs.
 *
 * For a simpler example that shows the use of Google Play services to fetch the last known location
 * of a device, see
 * https://github.com/googlesamples/android-play-location/tree/master/BasicLocation.
 *
 * This sample uses Google Play services, but it does not require authentication. For a sample that
 * uses Google Play services for authentication, see
 * https://github.com/googlesamples/android-google-accounts/tree/master/QuickStart.
 */
public class GpsActivity implements
        ConnectionCallbacks, OnConnectionFailedListener, LocationListener{
    protected static final String TAG = "generic app";
    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 300000;
    /**
     * The fastest rate for active location updates. Exact. Updates will never be more frequent
     * than this value.
     */
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;
    // Keys for storing activity state in the Bundle.
    protected final static String REQUESTING_LOCATION_UPDATES_KEY = "requesting-location-updates-key";
    protected final static String LOCATION_KEY = "location-key";
    protected final static String LAST_UPDATED_TIME_STRING_KEY = "last-updated-time-string-key";
    /**
     * Provides the entry point to Google Play services.
     */
    protected GoogleApiClient mGoogleApiClient;
    /**
     * Stores parameters for requests to the FusedLocationProviderApi.
     */
    protected LocationRequest mLocationRequest;
    /**
     * Represents a geographical location.
     */
    protected Location mCurrentLocation;
    // UI Widgets.

    /**
     * Tracks the status of the location updates request. Value changes when the user presses the
     * Start Updates and Stop Updates buttons.
     */
    protected Boolean mRequestingLocationUpdates;
    /**
     * Time when the location was updated represented as a String.
     */
    protected String mLastUpdateTime;

    Context context;

    public GpsActivity(Bundle savedInstanceState, Context context) {

// Locate the UI widgets.

        mRequestingLocationUpdates = true;
        mLastUpdateTime = "";
        this.context =context;
// Update values using data stored in the Bundle.
        updateValuesFromBundle(savedInstanceState);
// Kick off the process of building a GoogleApiClient and requesting the LocationServices
// API.
        buildGoogleApiClient();

    }
    /**
     * Updates fields based on data stored in the bundle.
     *
     * @param savedInstanceState The activity state saved in the Bundle.
     */
    private void updateValuesFromBundle(Bundle savedInstanceState) {
        Log.i(TAG, "Updating values from bundle");
        if (savedInstanceState != null) {
// Update the value of mRequestingLocationUpdates from the Bundle, and make sure that
// the Start Updates and Stop Updates buttons are correctly enabled or disabled.
            if (savedInstanceState.keySet().contains(REQUESTING_LOCATION_UPDATES_KEY)) {
                mRequestingLocationUpdates = savedInstanceState.getBoolean(
                        REQUESTING_LOCATION_UPDATES_KEY);
                setButtonsEnabledState();
            }
// Update the value of mCurrentLocation from the Bundle and update the UI to show the
// correct latitude and longitude.
            if (savedInstanceState.keySet().contains(LOCATION_KEY)) {
// Since LOCATION_KEY was found in the Bundle, we can be sure that mCurrentLocation
// is not null.
                mCurrentLocation = savedInstanceState.getParcelable(LOCATION_KEY);
            }
// Update the value of mLastUpdateTime from the Bundle and update the UI.
            if (savedInstanceState.keySet().contains(LAST_UPDATED_TIME_STRING_KEY)) {
                mLastUpdateTime = savedInstanceState.getString(LAST_UPDATED_TIME_STRING_KEY);
            }
            updateUI();
        }
    }
    /**
     * Builds a GoogleApiClient. Uses the {@code #addApi} method to request the
     * LocationServices API.
     */
    protected synchronized void buildGoogleApiClient() {
        Log.i(TAG, "Building GoogleApiClient");
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        createLocationRequest();

    }
    /**
     * Sets up the location request. Android has two location request settings:
     * {@code ACCESS_COARSE_LOCATION} and {@code ACCESS_FINE_LOCATION}. These settings control
     * the accuracy of the current location. This sample uses ACCESS_FINE_LOCATION, as defined in
     * the AndroidManifest.xml.
     * <p/>
     * When the ACCESS_FINE_LOCATION setting is specified, combined with a fast update
     * interval (5 seconds), the Fused Location Provider API returns location updates that are
     * accurate to within a few feet.
     * <p/>
     * These settings are appropriate for mapping applications that show real-time location
     * updates.
     */
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
// Sets the desired interval for active location updates. This interval is
// inexact. You may not receive updates at all if no location sources are available, or
// you may receive them slower than requested. You may also receive updates faster than
// requested if other applications are requesting location at a faster interval.
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
// Sets the fastest rate for active location updates. This interval is exact, and your
// application will never receive updates faster than this value.
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }
    /**
     * Handles the Start Updates button and requests start of location updates. Does nothing if
     * updates have already been requested.
     */

    /**
     * Requests location updates from the FusedLocationApi.
     */
    protected void startLocationUpdates() {
// The final argument to {@code requestLocationUpdates()} is a LocationListener
// (http://developer.android.com/reference/com/google/android/gms/location/LocationListener.html).
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }
    /**
     * Ensures that only one button is enabled at any time. The Start Updates button is enabled
     * if the user is not requesting location updates. The Stop Updates button is enabled if the
     * user is requesting location updates.
     */
    private void setButtonsEnabledState() {
        if (mRequestingLocationUpdates) {

        } else {

        }
    }
    /**
     * Updates the latitude, the longitude, and the last location time in the UI.
     */
    private void updateUI() {
        if (mCurrentLocation != null) {

            String lat =String.valueOf(mCurrentLocation.getLatitude());
            String log =String.valueOf(mCurrentLocation.getLongitude());

            Utils.save("lat",lat, (android.app.Activity) context);
            Utils.save("long",log, (android.app.Activity) context);

       //     Toast.makeText(context, lat + " " + String.valueOf(mCurrentLocation.getLongitude()), Toast.LENGTH_LONG).show();

        }
    }
    /**
     * Removes location updates from the FusedLocationApi.
     */
    protected void stopLocationUpdates() {
// It is a good practice to remove location requests when the activity is in a paused or
// stopped state. Doing so helps battery performance and is especially
// recommended in applications that request frequent location updates.
// The final argument to {@code requestLocationUpdates()} is a LocationListener
// (http://developer.android.com/reference/com/google/android/gms/location/LocationListener.html).
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    public void onStart() {

        mGoogleApiClient.connect();
    }

    public void onResume() {

// Within {@code onPause()}, we pause location updates, but leave the
// connection to GoogleApiClient intact. Here, we resume receiving
// location updates if the user has requested them.
        if (mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    protected void onPause() {

// Stop location updates to save battery, but don't disconnect the GoogleApiClient object.
        if (mGoogleApiClient.isConnected()) {
            stopLocationUpdates();
        }
    }

    protected void onStop() {

        mGoogleApiClient.disconnect();
    }
    /**
     * Runs when a GoogleApiClient object successfully connects.
     */
    @Override
    public void onConnected(Bundle connectionHint) {
        Log.i(TAG, "Connected to GoogleApiClient");
// If the initial location was never previously requested, we use
// FusedLocationApi.getLastLocation() to get it. If it was previously requested, we store
// its value in the Bundle and check for it in onCreate(). We
// do not request it again unless the user specifically requests location updates by pressing
// the Start Updates button.
//
// Because we cache the value of the initial location in the Bundle, it means that if the
// user launches the activity,
// moves to a new location, and then changes the device orientation, the original location
// is displayed as the activity is re-created.
        if (mCurrentLocation == null) {
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
            updateUI();
        }
// If the user presses the Start Updates button before GoogleApiClient connects, we set
// mRequestingLocationUpdates to true (see startUpdatesButtonHandler()). Here, we check
// the value of mRequestingLocationUpdates and if it is true, we start location updates.
        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }
    /**
     * Callback that fires when the location changes.
     */
    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());




      //  String uid =SPManager.retrive(SPManager.KEY_USERID_LOGIN);
        updateUI();
        mGoogleApiClient.disconnect();




    }
    @Override
    public void onConnectionSuspended(int cause) {
// The connection to Google Play services was lost for some reason. We call connect() to
// attempt to re-establish the connection.
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }
    @Override
    public void onConnectionFailed(ConnectionResult result) {
// Refer to the javadoc for ConnectionResult to see what error codes might be returned in
// onConnectionFailed.
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }
    /**
     * Stores activity data in the Bundle.
     */
    public Bundle  onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean(REQUESTING_LOCATION_UPDATES_KEY, mRequestingLocationUpdates);
        savedInstanceState.putParcelable(LOCATION_KEY, mCurrentLocation);
        savedInstanceState.putString(LAST_UPDATED_TIME_STRING_KEY, mLastUpdateTime);
        return savedInstanceState;
    }






}