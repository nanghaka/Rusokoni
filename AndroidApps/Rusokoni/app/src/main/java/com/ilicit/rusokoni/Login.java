package com.ilicit.rusokoni;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ilicit.rusokoni.library.UserFunctions;

import org.json.JSONException;
import org.json.JSONObject;

//import com.ewerdimamobile.utils.UserFunctions;
//import com.parse.Parse;
//import com.parse.ParseUser;

public class Login extends Activity implements OnClickListener {
	Button normalLogin;
	Button signUp;
	private MenuItem settings;
	ProgressDialog progressDialog;
	TextView name, gender, location;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_login);
		final EditText username = (EditText) findViewById(R.id.username);
		final EditText password = (EditText) findViewById(R.id.password);
		normalLogin = (Button) findViewById(R.id.normal_loginBTN);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("please wait...");
		signUp = (Button)findViewById(R.id.signup_button);
		signUp.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(getApplicationContext(), Register.class));
			}
		});
		normalLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
                progressDialog.show();
				normalLogin(username.getText().toString(), password.getText()
						.toString());
			}
		});
	}

	protected void normalLogin(final String email, final String password) {
		// TODO Auto-generated method stub

	//	progressDialog.show(this, "", "Logging in...", true);

		final UserFunctions userFunctions = new UserFunctions(this);

		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				JSONObject jsonResponse = null;
				jsonResponse = userFunctions.loginUser(email, password);
				try {
					if (jsonResponse.has("success")) {

                        progressDialog.dismiss();
                        Utils.save("Token",jsonResponse.getString("success"),Login.this);

						showUserDetailsActivity();
					} else if (jsonResponse.getString("success") != null) {

                        progressDialog.dismiss();

						// showAlert
						// pass jsonresponse to dialog
					} else {
						// showAlert //network fail
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
                    progressDialog.dismiss();
					e.printStackTrace();
				}

			}
		}).start();

	}

	private void removeDialog(final Boolean status, final JSONObject jsonResponse) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(status){
					try {
						Toast.makeText(getApplicationContext(), ""+jsonResponse.get("response").toString(), Toast.LENGTH_LONG).show();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				progressDialog.dismiss();
			}
		});
	}

	private void showUserDetailsActivity() {
		// TODO Auto-generated method stub
   startActivity(new Intent(getApplicationContext(), MainActivity.class));
	}

	@Override
	protected void onStart() {
		super.onStart();

	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();

	}

	@Override
	public void onDestroy() {
		super.onDestroy();

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}
}
