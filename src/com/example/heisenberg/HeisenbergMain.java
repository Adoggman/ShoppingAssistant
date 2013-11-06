package com.example.heisenberg;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import com.google.zxing.integration.android.IntentIntegrator;

public class HeisenbergMain extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heisenberg_main);
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
}
