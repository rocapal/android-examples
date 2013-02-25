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


package es.curso.android.asyncTask;

import android.os.Handler;
import android.os.Message;

public class MyThreadHandler extends Thread {
	
	private final String TAG = getClass().getSimpleName();	
	private final Integer DELAY = 4000;
	
	public Boolean shouldContinue = true;	
	private Handler mHandler = null;
	private Handler mHandlerLoop = new Handler();
	
	private Integer c = 0;
	
	public MyThreadHandler() {};	
	
	public void setHandler (Handler handler)
	{
		mHandler = handler;
	}
	
	@Override
	public void run() {		

		if (!shouldContinue)
			return;
		
		// do something
		
		
		// Interact with the UI using handler	
		Message msg = new Message();
		msg.what = c * (DELAY/1000);
		mHandler.sendMessage(msg);	
		
		c = c + 1;
		
		// Programming the next execution
		mHandlerLoop.postDelayed(this, DELAY);
		
	}

}