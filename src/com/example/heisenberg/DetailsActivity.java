package com.example.heisenberg;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class DetailsActivity extends Activity {
	TextView itemName, itemDescription, itemCost;
	DBController controller = new DBController(this);
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details);
        itemName = (TextView)findViewById(R.id.itemName);
        itemDescription = (TextView)findViewById(R.id.itemDescription);
        itemCost = (TextView)findViewById(R.id.itemCost);
        Intent objIntent = getIntent();
        String itemId = objIntent.getStringExtra("itemId");
        Item item = controller.getItem(itemId);
        Log.d("Reading", "Reading all items...");
    	itemName.setText("Item: " + item.getName());
    	itemDescription.setText("Description: " + item.getDescription());
    	itemCost.setText("Cost: " + item.getCost());
        
    }
	
	public void callHomeActivity(View view) {
		Intent objIntent = new Intent(getApplicationContext(), BrowseActivity.class);
		startActivity(objIntent);
	}

}
