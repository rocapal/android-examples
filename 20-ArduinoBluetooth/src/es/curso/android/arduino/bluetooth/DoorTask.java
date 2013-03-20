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


package es.curso.android.arduino.bluetooth;

import java.util.TimerTask;

import android.os.Handler;
import android.util.Log;

public class DoorTask extends TimerTask{

	private ArduinoBT mArduinoBT;
	private Handler mHandler;
	
	public DoorTask (ArduinoBT arduinoBT, Handler handler)
	{
		mArduinoBT = arduinoBT;
		mHandler = handler;
	}
	
	@Override
	public void run() {
		
		
		if (mArduinoBT == null)
			return;
		
		mArduinoBT.send("D");
		byte[] response = new byte[1];
		response = mArduinoBT.receive(1);
		
		if (response != null)
		Log.d("DoorTask","Receiving DoorSensor: " + (response[0]==0 ? "Open (0)" : "Close (1)") );
		
		if (response[0]==0 && mHandler != null)
			mHandler.sendEmptyMessage(0);

	}

}
