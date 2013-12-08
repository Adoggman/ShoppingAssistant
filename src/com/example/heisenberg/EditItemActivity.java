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
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class EditItemActivity extends Activity {
    EditText txtName;
    EditText txtCost;
    EditText txtDesc;
    EditText txtLocation;
    EditText txtDiscount;
    EditText txtStart;
    EditText txtEnd;
    Button btnSave;
    Button btnDelete;
 
    String id;
    Item item;
 
    // Progress Dialog
    private ProgressDialog pDialog;
 
    // JSON parser class
    JSONParser jsonParser = new JSONParser();
 
    // single product url
    private static final String url_item_details = Constants.url+"get_item_details.php";
 
    // url to update product
    private static final String url_update_item = Constants.url+"update_item.php";
 
    // url to delete product
    private static final String url_delete_item = Constants.url+"delete_item.php";
 
    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_ITEM = "item";
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "name";
    private static final String TAG_COST = "cost";
    private static final String TAG_DESCRIPTION = "description";
    private static final String TAG_LOCATION = "location";
    private static final String TAG_DISCOUNT = "discount";
    private static final String TAG_STARTDATE = "startdate";
    private static final String TAG_ENDDATE = "enddate";
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_item);
 
        // save button
        btnSave = (Button) findViewById(R.id.btnSave);
        btnDelete = (Button) findViewById(R.id.btnDelete);
 
        // getting product details from intent
        Intent i = getIntent();
 
        // getting product id (id) from intent
        id = i.getStringExtra(TAG_ID);
 
        // Getting complete product details in background thread
        new GetProductDetails().execute();
 
        // save button click event
        btnSave.setOnClickListener(new View.OnClickListener() {
 
            @Override
            public void onClick(View arg0) {
                // starting background task to update product
                new SaveProductDetails().execute();
            }
        });
 
        // Delete button click event
        btnDelete.setOnClickListener(new View.OnClickListener() {
 
            @Override
            public void onClick(View arg0) {
                // deleting product in background thread
                new DeleteProduct().execute();
            }
        });
 
    }
 
    /**
     * Background Async Task to Get complete product details
     * */
    class GetProductDetails extends AsyncTask<String, String, String> {
 
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EditItemActivity.this);
            pDialog.setMessage("Loading item details. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
 
        /**
         * Getting product details in background thread
         * */
        protected String doInBackground(String... args) {
 
            // Check for success tag
            int success;
            try {
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair(TAG_ID, id));
 
                        // getting product details by making HTTP request
                // Note that product details url will use GET request
                JSONObject json = jsonParser.makeHttpRequest(url_item_details, "GET", params);
 
                        // check your log for json response
                Log.d("Single Product Details", json.toString());
 
                        // json success tag
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    // successfully received product details
                    JSONArray c = json.getJSONArray(TAG_ITEM); // JSON Array
 
                    // get first product object from JSON Array
                    JSONObject jsonItem = c.getJSONObject(0);
 
                    // product with this pid found
                    txtName = (EditText) findViewById(R.id.inputName);
                    txtCost = (EditText) findViewById(R.id.inputPrice);
                    txtDesc = (EditText) findViewById(R.id.inputDesc);
                    txtLocation = (EditText) findViewById(R.id.inputLocation);
                    txtDiscount = (EditText) findViewById(R.id.inputDiscount);
                    txtStart = (EditText) findViewById(R.id.inputStart);
                    txtEnd = (EditText) findViewById(R.id.inputEnd);
                    
                    item = new Item(jsonItem.getString(TAG_NAME), 
                    		jsonItem.getString(TAG_DESCRIPTION), 
                    		Double.parseDouble(jsonItem.getString(TAG_COST)), 
                    		jsonItem.getString(TAG_LOCATION), 
                    		Integer.parseInt(jsonItem.getString(TAG_DISCOUNT)),
                    		jsonItem.getString(TAG_STARTDATE),
                    		jsonItem.getString(TAG_ENDDATE));
                }
                else{
                            // product with pid not found
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
            // dismiss the dialog once got all details
		    // display product data in EditText
		    txtName.setText(item.getName());
		    txtCost.setText(item.getCost().toString());
		    txtDesc.setText(item.getDescription());
		    txtLocation.setText(item.getLocation());
		    txtDiscount.setText( Integer.toString(item.getDiscount()));
		    txtStart.setText(item.getStartDate());
		    txtEnd.setText(item.getEndDate());
		    
            pDialog.dismiss();
        }
    }
 
    /**
     * Background Async Task to  Save product Details
     * */
    class SaveProductDetails extends AsyncTask<String, String, String> {
 
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EditItemActivity.this);
            pDialog.setMessage("Saving item ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
 
        /**
         * Saving product
         * */
        protected String doInBackground(String... args) {
 
            // getting updated data from EditTexts
            String name = txtName.getText().toString();
            String cost = txtCost.getText().toString();
            String description = txtDesc.getText().toString();
            String location = txtLocation.getText().toString();
            String discount = txtDiscount.getText().toString();
            String start = txtStart.getText().toString();
            String end = txtEnd.getText().toString();
 
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair(TAG_ID, id));
            params.add(new BasicNameValuePair(TAG_NAME, name));
            params.add(new BasicNameValuePair(TAG_COST, cost));
            params.add(new BasicNameValuePair(TAG_DESCRIPTION, description));
            params.add(new BasicNameValuePair(TAG_LOCATION, location));
            params.add(new BasicNameValuePair(TAG_DISCOUNT, discount));
            params.add(new BasicNameValuePair(TAG_STARTDATE, start));
            params.add(new BasicNameValuePair(TAG_ENDDATE, end));
 
            // sending modified data through http request
            // Notice that update product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_update_item,
                    "POST", params);
 
            // check json success tag
            try {
                int success = json.getInt(TAG_SUCCESS);
 
                if (success == 1) {
                    // successfully updated
                    Intent i = getIntent();
                    // send result code 100 to notify about product update
                    setResult(100, i);
                    finish();
                } else {
                    // failed to update product
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
            // dismiss the dialog once product uupdated
            pDialog.dismiss();
        }
    }
 
    /*****************************************************************
     * Background Async Task to Delete Product
     * */
    class DeleteProduct extends AsyncTask<String, String, String> {
 
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EditItemActivity.this);
            pDialog.setMessage("Deleting Item...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
 
        /**
         * Deleting product
         * */
        protected String doInBackground(String... args) {
 
            // Check for success tag
            int success;
            try {
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("id", id));
 
                // getting product details by making HTTP request
                JSONObject json = jsonParser.makeHttpRequest(
                        url_delete_item, "POST", params);
 
                // check your log for json response
                Log.d("Delete Product", json.toString());
 
                // json success tag
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    // product successfully deleted
                    // notify previous activity by sending code 100
                    Intent i = getIntent();
                    // send result code 100 to notify about product deletion
                    setResult(100, i);
                    finish();
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
            // dismiss the dialog once product deleted
            pDialog.dismiss();
            Intent i = new Intent(getApplicationContext(), BrowseActivity.class);
        	startActivity(i);
        }
 
    }
}
