/*
 *
 *  Copyright (C) Roberto Calvo Palomino
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see http://www.gnu.org/licenses/. 
 *
 *  Author : Roberto Calvo Palomino <rocapal at gmail dot com>
 *
 */

package es.curso.android.activities;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

	private String TAG = getClass().getSimpleName();	
	
	
	/*
	 * The entire lifetime of an activity happens between the first call to onCreate(Bundle) 
	 * through to a single final call to onDestroy(). An activity will do all setup of "global" 
	 * state in onCreate(), and release all remaining resources in onDestroy(). For example, 
	 * if it has a thread running in the background to download data from the network, 
	 * it may create that thread in onCreate() and then stop the thread in onDestroy().
	 * */
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
			
		// Know the max memory to alloc in this application
		ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		int memoryClass = am.getMemoryClass();
		Log.v("onCreate", "memoryClass:" + Integer.toString(memoryClass));
				
		Log.d(TAG, "onCreate()");
		
		Button btStart = (Button) this.findViewById(R.id.btStart);
		btStart.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(MainActivity.this, Start.class);
				startActivity(i);
				
			}
		});	
		

			
	}

	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.d(TAG, "onDestroy()");			
	
	}
	
	
	/*
	 * The visible lifetime of an activity happens between a call to onStart() until a 
	 * corresponding call to onStop(). During this time the user can see the activity on-screen, 
	 * though it may not be in the foreground and interacting with the user. 
	 * Between these two methods you can maintain resources that are needed to show the activity 
	 * to the user. For example, you can register a BroadcastReceiver in onStart() to monitor 
	 * for changes that impact your UI, and unregister it in onStop() when the user no longer 
	 * sees what you are displaying. The onStart() and onStop() methods can be called multiple 
	 * times, as the activity becomes visible and hidden to the user.
	 * */
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Log.d(TAG,"onStart()");
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.d(TAG, "onStop()");
	}
				
	/*
	 * The foreground lifetime of an activity happens between a call to onResume() until 
	 * a corresponding call to onPause(). During this time the activity is in front of 
	 * all other activities and interacting with the user. An activity can frequently go 
	 * between the resumed and paused states -- for example when the device goes to sleep,
	 * when an activity result is delivered, when a new intent is delivered -- so the code 
	 * in these methods should be fairly lightweight.
	 * 
	 */
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.d(TAG,"onResume()");
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.d(TAG,"onPause()");
	}
	
	
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		Log.d(TAG, "onRestart()");
	}
	
	
	
	/* This method is always called when the app loses the focus.
	 * This method is called before onPause	 
	 */
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub		
		super.onSaveInstanceState(outState);
		Log.d(TAG, "onSaveInstanceState()");
		
		outState.putLong("param", 5);		
	}
	
	/*
	 * onRestoreInstanceState() is called only when recreating activity 
	 * after it was killed by the OS. This situation happen when:
	 * 
	 *  - orientation of the device changes (your activity is destroyed and recreated) 
	 *  - there is another activity in front of yours and at some point the OS kills your 
	 *     activity in order to free memory (for example). Next time when you start your 
	 *     activity onRestoreInstanceState() will be called.
	 */
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onRestoreInstanceState(savedInstanceState);
		Log.d(TAG, "onRestoreInstanceState()");
		
		Integer i = savedInstanceState.getInt("param");
	}
	
}
