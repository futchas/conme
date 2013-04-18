/**
 *	Copyright (C) 2013 by Iyad Al-Sahwi
 */
package de.htw.conme;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import de.htw.conme.client.ConnectActivity;
import de.htw.conme.server.ShareActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.util.Log;
import android.view.Menu;
import android.view.View;

/**
 * @author Iyad Al-Sahwi
 *
 */
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
		startActivity(new Intent(this, ShareActivity.class));
	}

	public void connectToAP(View view) {
		startActivity(new Intent(this, ConnectActivity.class));
	}

}
