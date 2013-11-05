package com.example.heisenberg;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class BrowseActivity extends ListActivity {
	Intent intent;
	TextView itemId;
	DBController controller = new DBController(this);
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.browse);
        ArrayList<HashMap<String, String>> itemList = controller.getAllItems();
        if (itemList.size() != 0){
        	ListView lv = getListView();
			lv.setOnItemClickListener(new OnItemClickListener() {
				  @Override 
				  public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
					  itemId = (TextView) view.findViewById(R.id.itemId);
					  String valItemId = itemId.getText().toString();					  
					  Intent  i = new Intent(getApplicationContext(), DetailsActivity.class);
					  i.putExtra("itemId", valItemId); 
					  startActivity(i); 
				  }
			}); 
        }
			ListAdapter adapter = new SimpleAdapter( BrowseActivity.this,itemList, R.layout.view_item_entry, new String[] { "itemId","itemName"}, new int[] {R.id.itemId, R.id.itemName}); 
			setListAdapter(adapter);
	}
}
