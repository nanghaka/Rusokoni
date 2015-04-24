package com.ilicit.rusokoni.library;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.ilicit.rusokoni.Utils;
import com.ilicit.rusokoni.model.User;
import com.ilicit.rusokoni.model.UserModel;

public class UserFunctions {

	private JSONParser jsonParser;
	private static String login_tag = "login";
	private static String register_tag = "register";
	private static String postCommodity_tag = "commodity_post";
	private static String dailySales_tag = "daily_sales";
	
	private static String marketIndex = "http://finance.yahoo.com/webservice/v1/symbols/allcurrencies/quote?format=json";
	private static String dailySalesURL = "http://rusokoni.org/m/daily_sales.php";// http://192.168.137.1/ewerdima_mobile/alert.php
	private static String postCommodityURL = "http://rusokoni.org/m/post_commodity.php";// http://192.168.137.1/ewerdima_mobile/alert.php
	private static String loginURL = "http://rusokoni.org/index.php/api/rest/login";
	private static String registerURL = "http://rusokoni.org﻿/index.php/api/rest/signup";
	SharedPreferences prefs;
	SharedPreferences.Editor editor;
	public UserFunctions(Context context) {
		jsonParser = new JSONParser();
		prefs = context.getSharedPreferences("com.ewerdimamobile.app", 0);
	}

	// function to send alert
	public JSONObject postCommodity(String commodity_id, String commodity_buying_price,
			String commodity_selling_price,
			String longitude, String latitude) {
		// Building Parameters
		String user_id = prefs.getString("user_id", "2");
		//$user_id, $latitude, $longitude, $commodity_name, $commodity_buying_price, $commodity_selling_price
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", postCommodity_tag));
		params.add(new BasicNameValuePair("user_id", user_id));
		params.add(new BasicNameValuePair("commodity_id", commodity_id));
		params.add(new BasicNameValuePair("commodity_buying_price", commodity_buying_price));
		params.add(new BasicNameValuePair("commodity_selling_price", commodity_selling_price));
		params.add(new BasicNameValuePair("longitude", longitude));
		params.add(new BasicNameValuePair("latitude", latitude));
		JSONObject json = jsonParser.getJSON(postCommodityURL, params);
		// return json
		// Log.e("JSON", json.toString());
		return json;
	}
	
	public JSONObject dailySales( String commodity_id,
			 String commodity_quantiy,  String commodity_unit_price) {
		// Building Parameters
		String user_id = prefs.getString("user_id", "2");
		//$user_id, $latitude, $longitude, $commodity_name, $commodity_buying_price, $commodity_selling_price
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", dailySales_tag));
		params.add(new BasicNameValuePair("user_id", user_id));
		params.add(new BasicNameValuePair("commodity_id", commodity_id));
		params.add(new BasicNameValuePair("quantity_sold", commodity_quantiy));
		params.add(new BasicNameValuePair("unit_price", commodity_unit_price));
		JSONObject json = jsonParser.getJSON(dailySalesURL, params);
		// return json
		// Log.e("JSON", json.toString());
		return json;
	}

	/**
	 * function make Login Request
	 * 
	 * @param email
	 * @param password
	 * */
	public JSONObject loginUser(String email, String password) {
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", login_tag));
		params.add(new BasicNameValuePair("email", email));
		params.add(new BasicNameValuePair("password", password));
		JSONObject json = jsonParser.getJSON(loginURL, params);
		// return json
		// Log.e("JSON", json.toString());
		return json;
	}
	
	public JSONObject getMarketIndex() {
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		
		JSONObject json = jsonParser.getJSON(marketIndex);
		// return json
		// Log.e("JSON", json.toString());
		return json;
	}


	public JSONObject registerUser(UserModel user,Activity activity) {
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", register_tag));
		params.add(new BasicNameValuePair("first_name", user.getFirst_name()));
		params.add(new BasicNameValuePair("email", user.getEmail()));
		params.add(new BasicNameValuePair("password", user.getPassword()));
        params.add(new BasicNameValuePair("last_name", user.getLast_name()));
        params.add(new BasicNameValuePair("user_country", user.getUser_country()));

		// getting JSON Object
		JSONObject json = jsonParser.getJSON(registerURL, params);
		// return json
		String unique_id;
		try {
			unique_id = json.getString("success").toString();
            Utils.save("Token",unique_id,activity);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return json;
	}

	public boolean isUserLoggedIn() {
		if (prefs.getBoolean("my_first_time", true)) {
			return true;
		}
		else{
		return false;
		}
	}

	public void logoutUser() {
		prefs.edit().clear().commit();
	}
	
	public void saveUserDetails(String name, String unique_id){
		editor = prefs.edit();
		editor.putString("username", name);
		editor.putString("unique_id", unique_id);
		editor.putBoolean("my_first_time", false);
		editor.commit();
	}
	
	

}
