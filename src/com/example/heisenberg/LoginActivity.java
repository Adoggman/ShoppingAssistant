package com.example.heisenberg;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends Activity {
	
	// Progress Dialog
    private ProgressDialog pDialog;
 
    JSONParser jsonParser = new JSONParser();
    EditText inputEmail;
    EditText inputPassword;
 
    // url to create new product
    private static String url_login = Constants.url+"login.php";
 
    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_USER = "user";
    private static final String TAG_EMAIL = "email";
    private static final String TAG_ADMIN = "admin";
    private static final String TAG_NAME = "name";
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        
        // if the user is logged in, open the main
        if (User.loggedIn(this)) {
        	User.getLoggedInUser(this).logOut(this);
            Intent i = new Intent(getApplicationContext(), HeisenbergMain.class);
            startActivity(i);
        }
        
        TextView registerScreen = (TextView)findViewById(R.id.link_to_register);
        
        // Listening to register new account link
        registerScreen.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//Switch to register screen
				Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
				startActivity(i);
				
			}
		});
        
        inputEmail = (EditText) findViewById(R.id.login_email);
        inputPassword = (EditText) findViewById(R.id.login_password);
        
        Button btnLogin = (Button) findViewById(R.id.btnLogin);
        
        btnLogin.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new Login().execute();
			}
		});
    }
	
	class Login extends AsyncTask<String,String,String> {
		/**
         * Before starting background thread Show Progress Dialog
         * */
		@Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setMessage("Logging in. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
 
        /**
         * getting All items from url
         * */
        protected String doInBackground(String... args) {
            String email = inputEmail.getText().toString();
            String password = inputPassword.getText().toString();
            
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("email", email));
            params.add(new BasicNameValuePair("password", password));
            
            // getting JSON string from URL
            JSONObject json = jsonParser.makeHttpRequest(url_login, "GET", params);
 
            // Check your log cat for JSON reponse
            Log.d("User: ", json.toString());
 
            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    
                	JSONArray c = json.getJSONArray(TAG_USER);
                	JSONObject jsonItem = c.getJSONObject(0);
                	
                	email = jsonItem.getString(TAG_EMAIL);
                	String name = jsonItem.getString(TAG_NAME);
                	boolean admin = jsonItem.getString(TAG_ADMIN).equals("1");
                	
                	// LOG IN CODE
                	SharedPreferences settings = getSharedPreferences(User.PREFS_NAME,0);
            		SharedPreferences.Editor editor = settings.edit();
            		
            		editor.putString("email",email);
            		editor.putString("name", name);
            		editor.putBoolean("admin", admin);
            		editor.putBoolean("loggedIn", true);
            		
            		editor.commit();
            		// END LOG IN CODE
                	
                    Intent i = new Intent(getApplicationContext(), HeisenbergMain.class);
                    startActivity(i);
                } else {

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
            // dismiss the dialog after getting all products
            pDialog.dismiss();
        }
 
	}

}
