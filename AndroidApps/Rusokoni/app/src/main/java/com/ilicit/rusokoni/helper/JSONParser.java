package com.ilicit.rusokoni.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONObject;

import android.app.Activity;
import android.net.http.AndroidHttpClient;
import android.util.Base64;
import android.util.Log;

import com.ilicit.rusokoni.Utils;

public class JSONParser {

	static InputStream is = null;
	static JSONObject jObj = null;
	static String json = "";

	// constructor
	public JSONParser() {

	}

	// function get json from url
	// by making HTTP POST or GET mehtod
	public String makeHttpRequest(String url, String method,
			List<NameValuePair> params,Activity activity) {
        HttpURLConnection myURLConnection = null;
        AndroidHttpClient httpclient = null;

		// Making HTTP request
		try {

			// check for request method
			if (method.equalsIgnoreCase("POST")) {
				// request method is POST
				// defaultHttpClient
				DefaultHttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(url);

				httpPost.setEntity(new UrlEncodedFormEntity(params));

				HttpResponse httpResponse = httpClient.execute(httpPost);
				HttpEntity httpEntity = httpResponse.getEntity();
				is = httpEntity.getContent();

			} else  {
				// request method is GET



                try {
                    httpclient = AndroidHttpClient.newInstance("user agent");

                    URL urlObj = new URL(url);
                    HttpHost host = new HttpHost(urlObj.getHost(), urlObj.getPort(), urlObj.getProtocol());
                    AuthScope scope = new AuthScope(urlObj.getHost(), urlObj.getPort());
                    UsernamePasswordCredentials creds = new UsernamePasswordCredentials(Utils.getSaved("apiUsername", activity), Utils.getSaved("apipassword",activity));

                    CredentialsProvider cp = new BasicCredentialsProvider();
                    cp.setCredentials(scope, creds);
                    HttpContext credContext = new BasicHttpContext();
                    credContext.setAttribute(ClientContext.CREDS_PROVIDER, cp);

                    HttpGet job = new HttpGet(url);
                    HttpResponse response = httpclient.execute(host,job,credContext);
                    StatusLine status = response.getStatusLine();
                    HttpEntity httpEntity = response.getEntity();
                    is = httpEntity.getContent();
                    Log.d("tag", status.toString());

                }
                catch(Exception e){
                    e.printStackTrace();
                }

              /*
				DefaultHttpClient httpClient = new DefaultHttpClient();


				HttpGet httpGet = new HttpGet();
                httpGet.setURI(new URI(url));
                httpGet.addHeader("username", "rusokoni_api");
                httpGet.addHeader("password","zZj0IdM0wC\"2e4M");


				HttpResponse httpResponse = httpClient.execute(myURLConnection);
				HttpEntity httpEntity = httpResponse.getEntity();*/
				//is = myURLConnection.getInputStream();
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

        try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			json = sb.toString();
		} catch (Exception e) {
			Log.e("Buffer Error", "Error converting result " + e.toString());
		}

		// return JSON String
        httpclient.close();
		return json;

	}
}
