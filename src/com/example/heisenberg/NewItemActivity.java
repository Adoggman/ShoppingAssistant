package com.example.heisenberg;

import java.util.ArrayList;
import java.util.List;
 
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
 
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NewItemActivity extends Activity {
    // Progress Dialog
    private ProgressDialog pDialog;
 
    JSONParser jsonParser = new JSONParser();
    EditText inputName;
    EditText inputPrice;
    EditText inputDesc;
    EditText inputLocation;
    EditText inputDiscount;
    EditText inputStartDate;
    EditText inputEndDate;
 
    // url to create new item
    private static String url_create_item = Constants.url+"create_item.php";
 
    // JSON Node names
    private static final String TAG_SUCCESS = "success";
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_item);
 
        // Edit Text
        inputName = (EditText) findViewById(R.id.inputName);
        inputPrice = (EditText) findViewById(R.id.inputPrice);
        inputDesc = (EditText) findViewById(R.id.inputDesc);
        inputLocation = (EditText) findViewById(R.id.inputLocation);
        inputDiscount = (EditText) findViewById(R.id.inputDiscount);
        inputStartDate = (EditText) findViewById(R.id.inputStart);
        inputEndDate = (EditText) findViewById(R.id.inputEnd);
 
        // Create button
        Button btnCreateItem = (Button) findViewById(R.id.btnCreateProduct);
 
        // button click event
        btnCreateItem.setOnClickListener(new View.OnClickListener() {
 
            @Override
            public void onClick(View view) {
                // creating new product in background thread
                new CreateNewItem().execute();
            }
        });
    }
 
    /**
     * Background Async Task to Create new product
     * */
    class CreateNewItem extends AsyncTask<String, String, String> {
 
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(NewItemActivity.this);
            pDialog.setMessage("Creating Product..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
 
        /**
         * Creating product
         * */
        protected String doInBackground(String... args) {
        	// get all the values entered for the item
            String name = inputName.getText().toString();
            String cost = inputPrice.getText().toString();
            String description = inputDesc.getText().toString();
            String location = inputLocation.getText().toString();
            String discount = inputDiscount.getText().toString();
            String startDate = inputStartDate.getText().toString();
            String endDate = inputEndDate.getText().toString();
 
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("name", name));
            params.add(new BasicNameValuePair("cost", cost));
            params.add(new BasicNameValuePair("description", description));
            params.add(new BasicNameValuePair("discount", discount));
            params.add(new BasicNameValuePair("location", location));
            params.add(new BasicNameValuePair("startdate", startDate));
            params.add(new BasicNameValuePair("enddate", endDate));
 
            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_create_item,"POST", params);
 
            // check log cat fro response
            Log.d("Create Response", json.toString());
 
            // check for success tag
            try {
                int success = json.getInt(TAG_SUCCESS);
 
                if (success == 1) {
                    // successfully created product
                    Intent i = new Intent(getApplicationContext(), NewItemActivity.class);
                    startActivity(i);
 
                    // closing this screen
                    finish();
                } else {
                    // failed to create product
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
            // dismiss the dialog once done
            pDialog.dismiss();
        }
 
    }
}
