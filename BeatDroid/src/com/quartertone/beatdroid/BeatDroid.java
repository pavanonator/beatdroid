package com.quartertone.beatdroid;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.widget.TextView;
import android.widget.Toast;


public class BeatDroid extends Activity implements OnTouchListener, OnLongClickListener {

	public View playground;
	public TextView tap;
	public TextView bpm;
	public TextView coord;
	public float counter = 0;
	public long msec;
	public long firstbeat;
	public boolean donated;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		LayoutInflater inflater = getLayoutInflater();
		playground = inflater.inflate(R.layout.main, (ViewGroup) findViewById(R.id.playground));
		tap = (TextView) playground.findViewById(R.id.Tap);
		bpm = (TextView) playground.findViewById(R.id.BPM);
		coord = (TextView) playground.findViewById(R.id.Coord);
		tap.setOnTouchListener(this);
		tap.setOnLongClickListener(this);
		donated = appExists("com.quartertone.beatdroiddonate");
		if (donated) {
			//toastIt("Thanks for your support!");
		} else {
			toastIt("Please Donate!");
		}
    }

	@Override
	public boolean onTouch(View v, MotionEvent event) {	
		switch(event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			msec = System.currentTimeMillis();
			float avgbeats;
			if (counter == 0) {
				firstbeat = msec;
				bpm.setText("first");
				avgbeats = 0;
			} else {
				avgbeats = 60000 * counter / (msec - firstbeat); // beats per second
			}
			counter++;
			coord.setText("Beats: " + ((int)counter));
			avgbeats = (float) Math.round(avgbeats * 100) / 100;
			bpm.setText("" + avgbeats + " BPM");
			return false;
		case MotionEvent.ACTION_MOVE:
			return false;
		case MotionEvent.ACTION_UP:
			return false;
		}
		return false;
	}

	@Override
	public boolean onLongClick(View v) {
		bpm.setText("");
		coord.setText("");
		counter = 0;
		return true;
	}

public boolean appExists(String app) {
	PackageInfo pinfo;
	try {
		pinfo = getPackageManager().getPackageInfo(app, 0);
		return (pinfo.versionCode > 0);
	} catch (NameNotFoundException e) {
		//e.printStackTrace();
		return false;
	}
}
	

	public void toastIt(CharSequence text) {
		Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		if (donated) {
			menu.setGroupVisible(R.id.g_donate, false);
		} else {
			menu.setGroupVisible(R.id.g_donate, true);
		}
		return true;
	}


	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// super.onPrepareOptionsMenu(menu);
	return true;
	}


	public boolean onOptionsItemSelected(MenuItem item) {
		//	AlertDialog.Builder builder = new AlertDialog.Builder(this);
		//	AlertDialog alert;
			switch (item.getItemId()) {
				case R.id.m_donate:
					Intent donate = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.quartertone.summingdonate"));
					startActivity(donate);
				default:
					return false;

			}
	}




}

