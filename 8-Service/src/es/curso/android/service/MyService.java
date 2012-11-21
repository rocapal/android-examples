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


package es.curso.android.service;

import java.util.Calendar;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

public class MyService extends Service 
{
	private final String TAG = getClass().getSimpleName();
	private static IMyService Iservice;
	private Boolean run = false;
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
    public void onCreate() {
		super.onCreate();
		run = true;
		Log.d(TAG, "Init service");
		
		Toast.makeText(Main.context, "Init Service", Toast.LENGTH_SHORT).show();
        initService();
    }		
	
	
	public static void regListener (IMyService iMyService)
	{
		Iservice = iMyService;
	}
	
	private void initService () 
	{
		
		/* The logic of this method is your responsability: 
		    - You can wait for a listener (GPS)
		    - You can wait for a system event/intent
		    - You can download and parse file from internet.
		    
		  In this example, we are going to wait 5 seconds and
		  get the dateTime to show in the main activity
		*/ 
		
		Handler myHandler = new DoSomething();
		myHandler.sendMessageDelayed(new Message(), 3000);			
		
	}
	
	private class DoSomething extends Handler {
		@Override
		public void handleMessage(Message msg) {
			
			String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
			Iservice.updateTime(mydate);
			if (run)
				initService();
		}
	}
	
	@Override
    public void onDestroy() {
		super.onDestroy();
		run = false;
		Log.d(TAG, "Stop service");
		Toast.makeText(Main.context, "Stop Service", Toast.LENGTH_SHORT).show();
		
	}
	

}
