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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

public class ArduinoBT {

	private final String TAG = getClass().getName();

	private BluetoothSocket mBSocket = null;
	private BluetoothDevice mDevice = null;
	private OutputStream mmOutputStream = null;
	private InputStream mmInputStream = null;
	
	public ArduinoBT (BluetoothDevice device)
	{
		mDevice = device;
	}
	
	public boolean connect ()
	{
		
		UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb"); //Standard SerialPortService ID		
		try {
			mBSocket = mDevice.createRfcommSocketToServiceRecord(uuid);
			mBSocket.connect();
			mmOutputStream = mBSocket.getOutputStream();
			mmInputStream = mBSocket.getInputStream();

			return true;

		} catch (IOException e) {
			Log.e(TAG, e.getMessage());
			return false;
		}		
	}
	
	public void disconnect()
	{
		try {
			if (mBSocket != null)
				mBSocket.close();
			
		} catch (IOException e) {
			Log.e(TAG, e.getMessage());
		}
	}
	
	public void send (Integer data)
	{
		send(data.toString());
	}
	
	public synchronized void send (String data)
	{
		try {
			
			mmOutputStream.write(data.getBytes());
		} catch (IOException e) {
			Log.e(TAG, e.getMessage());			
		}
				
	}
	
	
	public byte[] receive (int numBytes)
	{
		byte[] b = new byte[numBytes];
		try {
			mmInputStream.read(b,0,numBytes);
			Log.d(TAG, "Recv: " + String.valueOf(b[0]));
			return b;
			
		} catch (IOException e) {
			Log.e(TAG, e.getMessage());		
			return null;
		}
	}
}
