package com.ilicit.rusokoni.library;

import android.content.Context;
import android.location.Criteria;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.Toast;

public class Location extends ActionBarActivity implements LocationListener {
	
	String latitude;
	String longitude;
	LocationManager locationManager;
	String provider;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		//get location manager
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		provider = locationManager.getBestProvider(criteria, false);
		Log.i("GPS", "Initializing GPS");
		android.location.Location location = locationManager.getLastKnownLocation(provider);
	
		//initiallize the location values
		if (location != null ){
			longitude = ""+location.getLatitude(); //or latitude.setText(String.valueOf(location.getLongitude()));
			latitude =""+location.getLongitude();
		} else {
			longitude= "N/A";
			latitude = "N/A";
		}
	}

	@Override
	public void onLocationChanged(android.location.Location location) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		Toast.makeText(getApplicationContext(), "Enabled Provider "+provider, Toast.LENGTH_LONG);
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		Toast.makeText(getApplicationContext(), "Disabled Provider "+provider, Toast.LENGTH_LONG);
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		locationManager.removeUpdates(this);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		locationManager.requestLocationUpdates(provider, 400, 1, this);
	}
	
	public String getLatitude(){
		return latitude;
		
	}
	public String getLongitude(){
		return longitude;
		
	}
}
