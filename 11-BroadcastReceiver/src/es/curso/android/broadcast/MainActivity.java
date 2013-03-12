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

package es.curso.android.broadcast;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

	Button btStart;
	Button btDownload;
		
	private WifiConection wifiEvent = new WifiConection();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		 btDownload = (Button) this.findViewById(R.id.btDownload);
		 btStart = (Button) this.findViewById(R.id.btStart);
				
		 btDownload.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
						
				Intent i=new Intent(MainActivity.this, DownloadService.class);				
				i.setData(Uri.parse("http://host.domain/file.zip"));
				MainActivity.this.startService(i);

			}
		});
		 
		 
		 IntentFilter intentFilter = new IntentFilter();
		 intentFilter.addAction(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION);			
		 registerReceiver(wifiEvent, intentFilter);	
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		IntentFilter f=
				new IntentFilter(DownloadService.ACTION_COMPLETE);
		
		registerReceiver(onEvent, f);

	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		
		unregisterReceiver(onEvent);
		unregisterReceiver(wifiEvent);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	private BroadcastReceiver onEvent=new BroadcastReceiver() {
		public void onReceive(Context ctxt, Intent i) {
			
			btStart.setEnabled(true);
			Toast.makeText(MainActivity.this, "Descarga completada", Toast.LENGTH_SHORT).show();
		}
	};


}
