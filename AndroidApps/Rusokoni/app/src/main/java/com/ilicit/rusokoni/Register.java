package com.ilicit.rusokoni;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.ilicit.rusokoni.library.UserFunctions;
import com.ilicit.rusokoni.model.UserModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Register extends ActionBarActivity {
	EditText reg_lastname;
	EditText reg_firstname;
	EditText reg_email;
	EditText reg_password;
	Button reg_signup;
    Spinner country;
    ProgressDialog progressDialog;
    String code;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		TextView loginScreen = (TextView) findViewById(R.id.link_to_login);
		reg_firstname = (EditText) findViewById(R.id.reg_firstname);
		reg_lastname = (EditText) findViewById(R.id.reg_lastname);
		reg_email = (EditText) findViewById(R.id.reg_email);
		reg_password = (EditText) findViewById(R.id.reg_password);
        country =(Spinner) findViewById(R.id.usercountry);
		reg_signup = (Button) findViewById(R.id.btnRegister);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("please wait ..");
        progressDialog.setCancelable(false);
        List<String> list = new ArrayList<String>();
        list.add("select country");
        list.add("Uganda");
        list.add("Nigeria");


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item,list);

        dataAdapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);


        country.setAdapter(dataAdapter);

        country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 1){

                    code = "256";

                }else if(position == 2){
                    code ="227";
                }else {
                  code ="0";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

		// Listening to SignUp Button
		reg_signup.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                if(reg_firstname.getText().toString().length() > 0){
                    if(reg_email.getText().length() > 0) {
                        if (Utils.isValidEmail(reg_email.getText())) {

                            if (reg_password.getText().length() > 0) {

                                if (code.equalsIgnoreCase("0")) {

                                    Toast.makeText(Register.this, "please select a country", Toast.LENGTH_LONG).show();

                                } else {

                                    signUp(reg_firstname.getText().toString(), reg_lastname
                                                    .getText().toString(), reg_email.getText().toString(),
                                            reg_password.getText().toString());
                                }

                            } else {
                                reg_password.setError("Please fill in password");
                            }

                        } else {
                            reg_email.setError("Invalid email");

                        }
                    }else{
                        reg_email.setError("please fill in the email");

                    }
                }else{
                    reg_firstname.setError("please fill in the First name");

                }


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

	private void signUp(final String fname, final String lname, String email,
			final String password) {

        final UserModel userModel = new UserModel();
        userModel.setEmail(email);
        userModel.setFirst_name(fname);
        userModel.setLast_name(lname);
        userModel.setPassword(password);
        userModel.setUser_country(code);


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
                    if(jsonResponse != null ) {
                        if (jsonResponse.has("success")) {

                            progressDialog.dismiss();
                            Utils.save("Token",jsonResponse.getString("success"),Register.this);
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));

                        } else if (jsonResponse.getString("success") != null) {

                            progressDialog.dismiss();

                            // showAlert
                            // pass jsonresponse to dialog
                        } else {
                            // showAlert //network fail
                        }
                    }else{

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
