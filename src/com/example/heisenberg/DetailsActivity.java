package com.example.heisenberg;

import java.util.ArrayList;
import java.util.Date;
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
import android.widget.TextView;
import android.widget.Button;

public class DetailsActivity extends Activity {
	 
    TextView txtName;
    TextView txtCost;
    TextView txtDesc;
    TextView txtLoc;
    TextView txtDisc;
 
    String id;
    String id2;
 
    // Progress Dialog
    private ProgressDialog pDialog;
 
    // JSON parser class
    JSONParser jsonParser = new JSONParser();
 
    // single product url
    private static final String url_item_details = Constants.url+"get_item_details.php";
    
    Item item;
 
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
        setContentView(R.layout.details);
 
        // getting product details from intent
        Intent i = getIntent();
 
        // getting product id (pid) from intent
        id = i.getStringExtra(TAG_ID);
        txtDisc = (TextView) findViewById(R.id.itemDiscount);
        
        // if the user is not logged in, don't display discounts
        if (!User.loggedIn(this)) {
		    txtDisc.setVisibility(View.GONE);
		   ((TextView) findViewById(R.id.txtDiscount)).setVisibility(View.GONE);
        }
        
        // if the user is not an admin, do not display the edit item button
        if (!User.loggedIn(this) || !User.getLoggedInUser(this).isAdmin()) {
        	((Button) findViewById(R.id.btnEdit)).setVisibility(View.GONE);
        }
        // Getting complete product details in background thread
        new GetItemDetails().execute();
 
    }
    
    // redirect to the edit item view
    public void editItem(View v){
    	Intent i = new Intent(getApplicationContext(), EditItemActivity.class);
    	i.putExtra("id", id);
    	startActivityForResult(i, 100);
    }
    
    // redirect to the compare item view
    public void compare(View v) {
    	Intent i = new Intent(getApplicationContext(), BrowseActivity.class);
    	i.putExtra("compareID", id);
    	startActivity(i);
    }
    
    
    
    // Response from Edit Item Activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // if result code 100
        if (resultCode == 100) {
            // if result code 100 is received
            // means user edited/deleted product
            // reload this screen again
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }
 
    }
 
    /**
     * Background Async Task to Get complete product details
     * */
    class GetItemDetails extends AsyncTask<String, String, String> {
 
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(DetailsActivity.this);
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
                    txtName = (TextView) findViewById(R.id.itemName);
                    txtCost = (TextView) findViewById(R.id.itemCost);
                    txtDesc = (TextView) findViewById(R.id.itemDescription);
                    txtLoc = (TextView) findViewById(R.id.itemLocation);
                    txtDisc = (TextView) findViewById(R.id.itemDiscount);
                    
                    item = new Item(jsonItem.getString(TAG_NAME), 
                    		jsonItem.getString(TAG_DESCRIPTION), 
                    		Double.parseDouble(jsonItem.getString(TAG_COST)), 
                    		jsonItem.getString(TAG_LOCATION), 
                    		Integer.parseInt(jsonItem.getString(TAG_DISCOUNT)),
                    		jsonItem.getString(TAG_STARTDATE),
                    		jsonItem.getString(TAG_ENDDATE));
                }
                else{
                            // item not found
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
            txtName = (TextView) findViewById(R.id.itemName);
            txtCost = (TextView) findViewById(R.id.itemCost);
            txtDesc = (TextView) findViewById(R.id.itemDescription);
            txtLoc = (TextView) findViewById(R.id.itemLocation);
            int discount = 0;
            
            Date startDate = getDate(item.getStartDate());
            Date endDate = getDate(item.getEndDate());
            Date currentDate = new Date();
            
            // only display the discount during valid discount time period
            if (currentDate.before(endDate) && currentDate.after(startDate)){
            	discount = item.getDiscount();
            }      

		    // display product data in EditText
		    txtName.setText(item.getName());
		    txtCost.setText("Price: $" + item.getCost().toString());
		    txtDesc.setText(item.getDescription());
		    txtLoc.setText(item.getLocation());
		    txtDisc.setText("This item is currently " + discount + "% off.");
		    
            pDialog.dismiss();
        }
    }
    
    @SuppressWarnings("deprecation")
    // Return a Date object based on String with format YYYY-MM-DD
	private Date getDate(String date)
    {
    	String[] fullDate = date.split("-");
    	// Date attribute year is year + 1900
    	int year = Integer.parseInt(fullDate[0]) - 1900;
    	int month = Integer.parseInt(fullDate[1]) - 1;
    	int day = Integer.parseInt(fullDate[2]);
    	
    	return new Date(year, month, day);
    }
}