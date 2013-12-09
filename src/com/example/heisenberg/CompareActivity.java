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

public class CompareActivity extends Activity {
	
	TextView txtName1;
    TextView txtCost1;
    TextView txtDesc1;
    TextView txtLoc1;
    TextView txtDisc1;
    
	TextView txtName2;
    TextView txtCost2;
    TextView txtDesc2;
    TextView txtLoc2;
    TextView txtDisc2;
 
    String id;
    String id2;
 
    // Progress Dialog
    private ProgressDialog pDialog;
 
    // JSON parser class
    JSONParser jsonParser = new JSONParser();
 
    // single product url
    private static final String url_item_details = Constants.url+"get_item_details.php";
    
    Item item;
    Item item2;
 
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
        setContentView(R.layout.compare);
 
        // getting product details from intent
        Intent i = getIntent();
 
        // getting product id (pid) from intent
        id = i.getStringExtra(TAG_ID);
        id2 = i.getStringExtra("compareID");
        txtDisc1 = (TextView) findViewById(R.id.item1Discount);
        txtDisc2 = (TextView) findViewById(R.id.item2Discount);
        
        if (!User.loggedIn(this)) {
		    txtDisc1.setVisibility(View.GONE);
		    txtDisc2.setVisibility(View.GONE);
		   ((TextView) findViewById(R.id.txtCompareDiscount)).setVisibility(View.GONE);
        }
        // Getting complete product details in background thread
        new GetProductDetails().execute();
 
    }
    
    
    
 // Response from Edit Product Activity
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
    class GetProductDetails extends AsyncTask<String, String, String> {
 
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(CompareActivity.this);
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
                    txtName1 = (TextView) findViewById(R.id.item1Name);
                    txtCost1 = (TextView) findViewById(R.id.item1Cost);
                    txtDesc1 = (TextView) findViewById(R.id.item1Description);
                    txtLoc1 = (TextView) findViewById(R.id.item1Location);
                    txtDisc1 = (TextView) findViewById(R.id.item1Discount);
                    
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
            
            try {
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair(TAG_ID, id2));
 
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
                    txtName2 = (TextView) findViewById(R.id.item2Name);
                    txtCost2 = (TextView) findViewById(R.id.item2Cost);
                    txtDesc2 = (TextView) findViewById(R.id.item2Description);
                    txtLoc2 = (TextView) findViewById(R.id.item2Location);
                    txtDisc2 = (TextView) findViewById(R.id.item2Discount);
                    
                    item2 = new Item(jsonItem.getString(TAG_NAME), 
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
        	// product with this pid found
            txtName1 = (TextView) findViewById(R.id.item1Name);
            txtCost1 = (TextView) findViewById(R.id.item1Cost);
            txtDesc1 = (TextView) findViewById(R.id.item1Description);
            txtLoc1 = (TextView) findViewById(R.id.item1Location);
            txtDisc1 = (TextView) findViewById(R.id.item1Discount);
            int discount = 0;
            
            Date startDate = getDate(item.getStartDate());
            Date endDate = getDate(item.getEndDate());
            Date currentDate = new Date();
            
            if (currentDate.before(endDate) && currentDate.after(startDate)){
            	discount = item.getDiscount();
            }      

		    // display product data in EditText
		    txtName1.setText(item.getName());
		    txtCost1.setText("Price: $" + item.getCost().toString());
		    txtDesc1.setText(item.getDescription());
		    txtLoc1.setText(item.getLocation());
		    txtDisc1.setText("This item is currently " + discount + "% off.");
		    
            txtName2 = (TextView) findViewById(R.id.item2Name);
            txtCost2 = (TextView) findViewById(R.id.item2Cost);
            txtDesc2 = (TextView) findViewById(R.id.item2Description);
            txtLoc2 = (TextView) findViewById(R.id.item2Location);
            txtDisc2 = (TextView) findViewById(R.id.item2Discount);
            discount = 0;
            
            startDate = getDate(item2.getStartDate());
            endDate = getDate(item2.getEndDate());
            
            if (currentDate.before(endDate) && currentDate.after(startDate)){
            	discount = item2.getDiscount();
            }      

		    // display product data in EditText
		    txtName2.setText(item2.getName());
		    txtCost2.setText("Price: $" + item2.getCost().toString());
		    txtDesc2.setText(item2.getDescription());
		    txtLoc2.setText(item2.getLocation());
		    txtDisc2.setText("This item is currently " + discount + "% off.");
		    
            pDialog.dismiss();
        }
    }
    
    @SuppressWarnings("deprecation")
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
