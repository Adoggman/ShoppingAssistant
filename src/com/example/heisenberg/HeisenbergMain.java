package com.example.heisenberg;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class HeisenbergMain extends Activity {

    private static final String TAG_ID = "id";
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heisenberg_main);
        Button btnLogin = (Button) findViewById(R.id.menuBtnLogin);
        if (User.loggedIn(this)) {
        	btnLogin.setText((CharSequence)"Logout");
        } else {
        	btnLogin.setText((CharSequence)"Login");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.heisenberg_main, menu);
        return true;
    }
    
    // start the QR code scanner
    public void readQRCode(View v){
		IntentIntegrator integrator = new IntentIntegrator(this);
		integrator.initiateScan();
    }
    
    // go to item details on scan
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
		if (scanResult != null) {
            String itemId = scanResult.getContents();
            
            Intent in = new Intent(getApplicationContext(), DetailsActivity.class);
            // sending pid to next activity
            in.putExtra(TAG_ID, itemId);

         // starting new activity and expecting some response back
            startActivityForResult(in, 100);
		}
	}
    
    // navigate to login
    public void login(View v){
    	Intent i = new Intent(getApplicationContext(), LoginActivity.class);
    	startActivity(i);
    }
    
    // navigate to browse
    public void browse(View v){
    	Intent i = new Intent(getApplicationContext(), BrowseActivity.class);
    	startActivity(i);
    }
    
    public void search(View v){
    	EditText txtSearch = (EditText) findViewById(R.id.editText1);
    	Intent i = new Intent(getApplicationContext(), SearchActivity.class);
    	i.putExtra("query", txtSearch.getText().toString());
    	startActivity(i);
    }
}
