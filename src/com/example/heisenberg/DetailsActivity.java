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

//public class DetailsActivity extends Activity {
//	TextView itemName, itemDescription, itemCost;
//	DBController controller = new DBController(this);
//	// url to get all products list
//    private String url_get_item = "http://192.168.0.24/android_connect/get_item_details.php?id=";
//    
//	@Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.details);
//        itemName = (TextView)findViewById(R.id.itemName);
//        itemDescription = (TextView)findViewById(R.id.itemDescription);
//        itemCost = (TextView)findViewById(R.id.itemCost);
//        Intent objIntent = getIntent();
//        String itemId = objIntent.getStringExtra("id");
//        Item item = controller.getItem(itemId);
//        Log.d("Reading", "Reading all items...");
//    	itemName.setText("Item: " + item.getName());
//    	itemDescription.setText("Description: " + item.getDescription());
//    	itemCost.setText("Cost: " + item.getCost());
//        
//    }
//}
public class DetailsActivity extends Activity {
	 
    TextView txtName;
    TextView txtCost;
    TextView txtDesc;
 
    String id;
 
    // Progress Dialog
    private ProgressDialog pDialog;
 
    // JSON parser class
    JSONParser jsonParser = new JSONParser();
 
    // single product url
    private static final String url_product_details = "http://192.168.0.24/android_connect/get_item_details.php";
 
    // url to update product
    private static final String url_update_product = "http://192.168.0.24/android_connect/update_item.php";
 
    // url to delete product
    private static final String url_delete_product = "http://192.168.0.24/android_connect/delete_item.php";
    
    Item item;
 
    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_ITEM = "item";
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "name";
    private static final String TAG_COST = "cost";
    private static final String TAG_DESCRIPTION = "description";
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details);
 
        // getting product details from intent
        Intent i = getIntent();
 
        // getting product id (pid) from intent
        id = i.getStringExtra(TAG_ID);
 
        // Getting complete product details in background thread
        new GetProductDetails().execute();
 
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
            pDialog = new ProgressDialog(DetailsActivity.this);
            pDialog.setMessage("Loading product details. Please wait...");
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
                params.add(new BasicNameValuePair("id", id));
 
                        // getting product details by making HTTP request
                // Note that product details url will use GET request
                JSONObject json = jsonParser.makeHttpRequest(url_product_details, "GET", params);
 
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
                    txtName = (EditText) findViewById(R.id.itemName);
                    txtCost = (EditText) findViewById(R.id.itemCost);
                    txtDesc = (EditText) findViewById(R.id.itemDescription);
 
                    item = new Item(jsonItem.getString(TAG_NAME), jsonItem.getString(TAG_DESCRIPTION), Double.parseDouble(jsonItem.getString(TAG_COST)));
                        }else{
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
        	// product with this pid found
            txtName = (EditText) findViewById(R.id.itemName);
            txtCost = (EditText) findViewById(R.id.itemCost);
            txtDesc = (EditText) findViewById(R.id.itemDescription);
            
		    // display product data in EditText
		    txtName.setText(item.getName());
		    txtCost.setText(item.getCost().toString());
		    txtDesc.setText(item.getDescription());
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
            pDialog = new ProgressDialog(DetailsActivity.this);
            pDialog.setMessage("Saving product ...");
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
            String price = txtCost.getText().toString();
            String description = txtDesc.getText().toString();
 
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair(TAG_ID, id));
            params.add(new BasicNameValuePair(TAG_NAME, name));
            params.add(new BasicNameValuePair(TAG_COST, price));
            params.add(new BasicNameValuePair(TAG_DESCRIPTION, description));
 
            // sending modified data through http request
            // Notice that update product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_update_product,
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
            pDialog = new ProgressDialog(DetailsActivity.this);
            pDialog.setMessage("Deleting Product...");
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
                        url_delete_product, "POST", params);
 
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
 
        }
 
    }
}