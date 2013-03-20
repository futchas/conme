package de.htw.conme;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class ConMe extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_con_me);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_con_me, menu);
		return true;
	}
	
	public void shareAP(View view) {
		Intent intent = new Intent(this, ShareActivity.class);
		startActivity(intent);
	}

	public void connectToAP(View view) {
		Intent intent = new Intent(this, ConnectActivity.class);
		startActivity(intent);
	}

}
