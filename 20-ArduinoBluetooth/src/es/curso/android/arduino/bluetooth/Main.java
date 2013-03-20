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

import java.util.ArrayList;
import java.util.Timer;

import android.app.ListActivity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class Main extends ListActivity {

	private final int REQUEST_ENABLE_BT = 1;
	private final String TAG = getClass().getName();
	
	private BluetoothAdapter mBluetoothAdapter = null;
	private BroadcastReceiver mReceiver = null;
	private ProgressDialog mProgressDialog = null;
	
	private ArduinoBT mArduinoBT = null;
	
	private ArrayList<BluetoothDevice> mDeviceList = new ArrayList<BluetoothDevice>();
	private MyAdapter mAdapter;
	
	// Wigdets
	private TextView tvStatus;	
	private Button btDisconnect;
	private ImageButton ibLed, ibDoor;
	private TextView tvLog;
	private ScrollView sv;
	
	private BluetoothDevice bDevice; 
	
	private static Timer mTimer = null;
	private static DoorTask mDtask = null;
	
    private NotificationManager mNM;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.main);
		
		mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		mNM.cancel(1);
		
		setupWidgets();				
		
		// Init Bluetooth
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		
		if (mBluetoothAdapter == null) {
		    // Device does not support Bluetooth
			return;
		}
		
		if (!mBluetoothAdapter.isEnabled()) {
		    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
		}
		else		
			startScan();
						
				
	}	
	
	
	/* 
	 * Handler to notify that door is open.
	 * This handler is used by DoorTask
	 */
	private Handler notifyHandler = new Handler(new Handler.Callback() {
		
		@Override
		public boolean handleMessage(Message msg) {
			showNotification();
			return true;
		}
	});
	
	/*
	 * AsyncTask to try to connect with bluetooth device
	 */
	private class ConnectTask extends AsyncTask<Void,Void,Integer>
	{
		ProgressDialog pd;
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			
			pd = ProgressDialog.show(Main.this, "Conectando", "Conectando BT ...");
			
		}

		@Override
		protected Integer doInBackground(Void... params) {
			
			if (mArduinoBT!= null && mArduinoBT.connect())
				return 1;
			else
				return 0;
						
		}
		
		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			if (pd.isShowing())
				pd.dismiss();
			
			if (result == 1)
			{
				tvStatus.setText(getResources().getString(R.string.bt_connected) 
						         + " " + bDevice.getName() );
				screenlog("Connected to " + bDevice.getName() );				
			}
			else
				tvStatus.setText(getResources().getString(R.string.bt_not_connected) 
								 + " " + bDevice.getName() );
			
			
		}
		
	}
	
	
	private void launchTimerTask()
	{
		if (mTimer==null && mDtask==null)
		{
			// Launch TimerTask
			mTimer = new Timer();
								
			mDtask = new DoorTask(mArduinoBT, notifyHandler);						
			
			mTimer.schedule(mDtask, 0, 4000);
						
			
		}
	}
	
	private void cancelTimerTask()
	{
		// Cancelamos tarea 
		if (mDtask != null)
			mDtask.cancel();

		// Desplanificar el timer
		if (mTimer != null)
			mTimer.purge();
		
		mDtask = null;
		mTimer = null;
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		
		cancelTimerTask();
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		
		if (mArduinoBT != null)
			launchTimerTask();
	}
			
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();				
		
		cancelTimerTask();
				
		if (mArduinoBT != null)
			mArduinoBT.disconnect();				
		
		if (mReceiver != null)
			unregisterReceiver(mReceiver);
	}
	
	private void setupWidgets ()
	{
		mAdapter = new MyAdapter(this);
		tvStatus = (TextView) this.findViewById(R.id.tvStatus);
		
		tvLog = (TextView) this.findViewById(R.id.tvLog);
		
		sv = ((ScrollView) findViewById(R.id.sv));
		
		btDisconnect = (Button) this.findViewById(R.id.btDisconnect);
		btDisconnect.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (mArduinoBT != null)
				{
					cancelTimerTask();
					
					mArduinoBT.disconnect();
					mArduinoBT = null;
					
					tvStatus.setText("");
					screenlog("Disconnected!");
				}				
			}
		});
		
		ibLed = (ImageButton) this.findViewById(R.id.btLed);
		ibLed.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View v) 
			{
				sendLedRequest();
			}
		});
		
		ibDoor = (ImageButton) this.findViewById(R.id.btDoor);
		ibDoor.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View v) 
			{
				sendDoorRequest();
			}
		});
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) 
	{
		
		super.onListItemClick(l, v, position, id);
		
		bDevice = mDeviceList.get(position);
		
		if (mArduinoBT == null)
		{
			tvStatus.setText(getResources().getString(R.string.bt_connecting));			
			mArduinoBT = new ArduinoBT(bDevice);
			
			ConnectTask cTask = new ConnectTask();
			cTask.execute(null,null,null);
			
		}
		else
		{
			Toast.makeText(this, getResources().getString(R.string.bt_resource_busy), Toast.LENGTH_SHORT).show();
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		if (requestCode == REQUEST_ENABLE_BT)
			if (resultCode == RESULT_OK)
			{
				// Enabling Bluetooth succeeds
				startScan();
			}
			else if (resultCode == RESULT_CANCELED)
			{
				// Bluetooth was not enabled due to an error (or the user responded "No") 
				Toast.makeText(this, "Bluetooth not enabled", Toast.LENGTH_LONG).show();
			}
				
	}
	
	private void screenlog (String log)
	{
		tvLog.setText(tvLog.getText() + "\n" + log);
		
		// Each time you change the TextView 
		// you must force the FOCUS_DOWN of ScrollView			
		
		sv.post(new Runnable() 
		{
	         public void run() {
	             ((ScrollView) findViewById(R.id.sv)).fullScroll(View.FOCUS_DOWN);
	         }
		});
	}

	private void startScan ()
	{
				
		screenlog("Scanning devices ...");
		
		mProgressDialog = ProgressDialog.show(this, "BT Scan", "Scanning devices ...");
		mBluetoothAdapter.startDiscovery(); 
		
		mReceiver = new BroadcastReceiver(){		
			
			public void onReceive(Context context, Intent intent) {
				String action = intent.getAction();		

				if (BluetoothDevice.ACTION_FOUND.equals(action)) 
				{
					// Get the BluetoothDevice object from the Intent
					BluetoothDevice device = 
							intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
					
					Log.d(TAG, device.getName() + "\n" + device.getAddress());
					
					mDeviceList.add(device);
					
				}
				else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
					Log.d(TAG, "Scan finished!");
					
					screenlog("Scan finished! Found " + String.valueOf(mDeviceList.size()) + " devices" );
					
					mProgressDialog.dismiss();										
					
					setListAdapter(mAdapter);
					
					
				}
			}
		};

		IntentFilter filter = new IntentFilter();
		filter.addAction(BluetoothDevice.ACTION_FOUND);
		filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		registerReceiver(mReceiver, filter);
	}	
	
	private void sendDoorRequest ()
	{

		if (mArduinoBT != null)
		{

			screenlog("Sending DoorSensor Byte: 'D'");

			final Handler handler = new Handler(new Callback() {

				@Override
				public boolean handleMessage(Message msg) {

					if (msg.what == -1)
						screenlog("Receiving DoorSensor: Error - NULL");
					else
						screenlog("Receiving DoorSensor: " +
								(msg.what==0 ? "Open (0)" : "Close (1)") );

					return true;
				}
			});

			Thread th = new Thread(new Runnable() {

				@Override
				public void run() {
					mArduinoBT.send("D");

					byte[] response = new byte[1];
					response = mArduinoBT.receive(1);

					Message msg = new Message();

					if (response == null)
						msg.what = -1;
					else
						msg.what = response[0];

					handler.sendMessage(msg);
				}
			});

			th.start();					
		}
	}
	
	private void sendLedRequest()
	{

		if (mArduinoBT != null)
		{
			screenlog("Sending LedBlink Byte: 'L'");

			final Handler handler = new Handler(new Callback() {

				@Override
				public boolean handleMessage(Message msg) {
					screenlog("Sent LedBlink Byte: 'L'");
					return true;
				}
			});

			Thread th = new Thread(new Runnable() {

				@Override
				public void run() {
					mArduinoBT.send("L");
					handler.sendEmptyMessage(0);
				}
			});

			th.start();	

		}
	}
	
	
	public class MyAdapter extends BaseAdapter
	{
		
		private Context mContext;

		public MyAdapter(Context c) {
			mContext = c;
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mDeviceList.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView (int position, View convertView, ViewGroup parent) {
			

			View view = null;

			if (convertView == null) {
				// Make up a new view
				LayoutInflater inflater = (LayoutInflater) mContext
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

				view = inflater.inflate(R.layout.node, null);
			} else {
				// Use convertView if it is available
				view = convertView;
			}

			
			TextView tTitle = (TextView) view.findViewById(R.id.tvDevice);
			tTitle.setText(mDeviceList.get(position).getName());

			TextView tDescription = (TextView) view.findViewById(R.id.tvAddress);
			tDescription.setText(mDeviceList.get(position).getAddress());

			return view;

		}
		
	}
	
	 private void showNotification ()
     {

		 mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

			
		 // In this sample, we'll use the same text for the ticker and the expanded notification
		 CharSequence text = "Door Opened!!";

		 // Set the icon, scrolling text and timestamp
		 Notification notification = new Notification(R.drawable.yellowred,
				 text,
				 System.currentTimeMillis());

		 //Create the intent to activity
		 Intent iNotification = new Intent(this, new AlarmActivity().getClass() );
		 iNotification.putExtra("DATE", System.currentTimeMillis());
		 
		 PendingIntent contentIntent = PendingIntent.getActivity
				 (this, 0, iNotification, PendingIntent.FLAG_CANCEL_CURRENT);

		 // Set the info for the views that show in the notification panel.        
		 notification.setLatestEventInfo(this, "Alarm!", 
				 						text, contentIntent);

		 // Send the notification.
		 // We use a layout id because it is a unique number.  We use it later to cancel.
		 mNM.notify(1, notification);

     }

	
}
