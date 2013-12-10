package com.example.heisenberg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class BrowseActivity extends ListActivity {
	 // Progress Dialog
    private ProgressDialog pDialog;
 
    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();
 
    ArrayList<HashMap<String, String>> itemsList;
 
    // url to get all items list
    private static String url_all_items = Constants.url+"get_all_items.php";
    
    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_ITEMS = "items";
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "name";
 
    // products JSONArray
    JSONArray items = null;
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.browse);
        // Hashmap for ListView
        itemsList = new ArrayList<HashMap<String, String>>();
 
        // Loading products in Background Thread
        new LoadAllProducts().execute();
 
        // Get listview
        ListView lv = getListView();
 
        // launching compare item Screen or Details screen
        lv.setOnItemClickListener(new OnItemClickListener() {
 
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // getting values from selected ListItem
                String itemId = ((TextView) view.findViewById(R.id.id)).getText().toString();
 
                Intent in;
   
                // See if there is an item to compare to
                String compareID = getIntent().getStringExtra("compareID");
                if (compareID != null) {
                	in = new Intent(getApplicationContext(), CompareActivity.class);
                	in.putExtra("compareID", compareID);
                } else {
                	in = new Intent(getApplicationContext(), DetailsActivity.class);
                }
                // sending id to next activity
                in.putExtra(TAG_ID, itemId);
 
                // starting new activity and expecting some response back
                startActivityForResult(in, 100);
            }
        });
        
        // if admin, display add new item button
        if (!User.loggedIn(this) || !User.getLoggedInUser(this).isAdmin()) {
        	((Button)findViewById(R.id.btnAdd)).setVisibility(View.GONE);
        }
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
    
    // Launch add new item view
    public void addItem(View v){
    	Intent i = new Intent(getApplicationContext(), NewItemActivity.class);
    	startActivity(i);
    }
 
    /**
     * Background Async Task to Load all items by making HTTP Request
     * */
    class LoadAllProducts extends AsyncTask<String, String, String> {
 
        
        // Before starting background thread Show Progress Dialog
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(BrowseActivity.this);
            pDialog.setMessage("Loading products. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
 
        // getting All products from url
        protected String doInBackground(String... args) {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            // getting JSON string from URL
            JSONObject json = jParser.makeHttpRequest(url_all_items, "GET", params);
 
            // Check your log cat for JSON reponse
            Log.d("All Items: ", json.toString());
 
            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);
 
                if (success == 1) {
                    // products found
                    // Getting Array of Products
                    items = json.getJSONArray(TAG_ITEMS);
 
                    // looping through All Products
                    for (int i = 0; i < items.length(); i++) {
                        JSONObject c = items.getJSONObject(i);
 
                        // Storing each json item in variable
                        String id = c.getString(TAG_ID);
                        String name = c.getString(TAG_NAME);
 
                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();
 
                        // adding each child node to HashMap key => value
                        map.put(TAG_ID, id);
                        map.put(TAG_NAME, name);
 
                        // adding HashList to ArrayList
                        itemsList.add(map);
                    }
                } else {
                    // no products found
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
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    // Updating parsed JSON data into ListView
                    ListAdapter adapter = new SimpleAdapter(
                            BrowseActivity.this, itemsList,
                            R.layout.view_item_entry, new String[] { TAG_ID, TAG_NAME},
                            new int[] { R.id.id, R.id.name });
                    // updating listview
                    setListAdapter(adapter);
                }
            });
 
        }
 
    }
}

