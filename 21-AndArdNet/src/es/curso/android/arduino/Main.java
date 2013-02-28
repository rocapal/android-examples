/*
 *    Copyright (C) 2012 - Roberto Calvo Palomino
 * 
 *    This program is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation; either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program; if not, write to the Free Software
 *    Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *    
 *    
 *    Authors: Roberto Calvo <rocapal [at] gmail [dot] com>
 */

package es.curso.android.arduino;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Main extends Activity {

	Button btLed;
	TextView tvStatus;
	Timer mTempTimer;
	
	private final String URL = "http://192.168.1.10/";
	
	private final Integer UPDATED_TIME = 3000;
	
	int statusButton = 0;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		setupWidgets();
		
		launchTimerTask();
		
		statusButton = 0;
		stateButton();
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		
		statusButton = 1;
		stateButton();
		
		if (mTempTimer != null)
			mTempTimer.cancel();
		
	}
	
	private void setupWidgets ()
	{
		
		btLed = (Button) findViewById(R.id.btLed);
		tvStatus = (TextView) findViewById(R.id.tvStatus);
		
		btLed.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				stateButton();
			}
		});
	}
	
	private void stateButton ()
	{
		if (statusButton == 0)
		{
			btLed.setText("LED OFF");
			doPetition(URL + "led/on");
			statusButton = 1;
		}
		else if (statusButton == 1)
		{
			btLed.setText("LED ON");
			doPetition(URL + "led/off");
			
			statusButton = 0;
		}
	}
	
	private String doPetition (String url)
	{
		
			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse response;
			try {
				response = httpclient.execute(new HttpGet(url));
				StatusLine statusLine = response.getStatusLine();
				if(statusLine.getStatusCode() == HttpStatus.SC_OK){
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					response.getEntity().writeTo(out);
					out.close();
					String responseString = out.toString();
					Log.d("Main", responseString);
					return responseString;
					
				} else{
					//Closes the connection.
					response.getEntity().getContent().close();
					return null;
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
			
			
	}
	
	private JSONObject parseJSON (String response)
	{
		try {
			return new JSONObject(response);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();			
		}
		
		return null;
		
	}
	
	private void showData (JSONObject json)
	{
		String msg = "";
			
		msg = "Switch: ";
		if (json.has("switch"))
		{
			try {
				Integer value = json.getInt("switch");
				if (value == 1)
					msg += "ON";
				else
					msg += "OFF";
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		msg += " - Temp: ";
		if (json.has("temp"))
		{
			try {
				Double value = json.getDouble("temp");
				msg += value.toString();
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		final String msgFinal = msg;
		
		Main.this.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {			
				tvStatus.setText(msgFinal);
			}
		});
	}
	
	private void launchTimerTask ()
	{
		
		mTempTimer = new Timer();	
		
		mTempTimer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				String response = doPetition(URL + "status/");
				JSONObject json = parseJSON(response);
				showData (json);
				
			}
		}, 0, UPDATED_TIME);
		
	}

	
	
	
}
