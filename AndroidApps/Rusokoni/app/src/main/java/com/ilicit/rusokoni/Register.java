package com.ilicit.rusokoni;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ilicit.rusokoni.library.UserFunctions;
import com.ilicit.rusokoni.model.UserModel;

import org.json.JSONException;
import org.json.JSONObject;

public class Register extends ActionBarActivity {
	EditText reg_fullname;
	EditText reg_username;
	EditText reg_email;
	EditText reg_password;
	Button reg_signup;
    ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		TextView loginScreen = (TextView) findViewById(R.id.link_to_login);
		reg_username = (EditText) findViewById(R.id.reg_username);
		reg_fullname = (EditText) findViewById(R.id.reg_fullname);
		reg_email = (EditText) findViewById(R.id.reg_email);
		reg_password = (EditText) findViewById(R.id.reg_password);
		reg_signup = (Button) findViewById(R.id.btnRegister);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("please wait ..");
        progressDialog.setCancelable(false);

		// Listening to SignUp Button
		reg_signup.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				signUp(reg_fullname.getText().toString(), reg_username
						.getText().toString(), reg_email.getText().toString(),
						reg_password.getText().toString());
			}
		});
		// Listening to Login Screen link
		loginScreen.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				// Switching to Login Screen/closing register screen
				finish();
			}
		});
		
	}

	private void signUp(final String fullname, final String username, String email,
			final String password) {

        final UserModel userModel = new UserModel();
        userModel.setEmail(email);
        userModel.setFirst_name("");
        userModel.setLast_name("");
        userModel.setPassword(password);
        userModel.setUser_country("");


		progressDialog.show();
		
		// other fields can be set just like with ParseObject
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				JSONObject jsonResponse = null;
				UserFunctions userFunctions = new UserFunctions(getApplicationContext());
				jsonResponse = userFunctions.registerUser(userModel,Register.this);
				
				try {
					if (jsonResponse.has("success")) {

						progressDialog.dismiss();
						startActivity(new Intent(getApplicationContext(), MainActivity.class));
						
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



}
