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


package es.curso.android.panoramio;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import android.app.ListActivity;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import es.curso.android.panoramio.maps.Map;
import es.curso.android.panoramio.parser.JSONParser;
import es.curso.android.panoramio.parser.PanoramioNode;
import es.curso.android.panoramio.service.ILocService;
import es.curso.android.panoramio.service.LocService;

public class Main extends ListActivity {

	private final String TAG = getClass().getSimpleName(); 

	private static ArrayList<PanoramioNode> mList = new ArrayList<PanoramioNode>();
	private MyAdapter mAdapter;
	
	private Integer mPage = 1;
	private Location mLocation;
	
	private ProgressDialog mPd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.main);
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitNetwork().build();
		StrictMode.setThreadPolicy(policy); 
		
		startService(new Intent(this,LocService.class));
		
		Intent i = getIntent();
		
		
		if (i != null)
		{
			// The notification is the entry point
			String isNotify = i.getStringExtra(HelperPanoramio.NEW_PLACES_NAME);
			
			if (isNotify != null && isNotify.equals(HelperPanoramio.NEW_PLACES_VALUE))
			{		
				// Cancel and hidden the notification in Android TopBar
				NotificationManager mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
				mNM.cancel(HelperPanoramio.NOTIFICATION_PLACES_ID);
				
				mList = LocService.getList();
				mLocation = LocService.getLastLocation();
				initList();		
				
				return;
			}			
		}
				
		
			
		// The app runs in normal mode
		// Launch progressDialog waiting GPS info
		mPd = ProgressDialog.show(this,getResources().getString(R.string.progress_dialog_title), 
									   getResources().getString(R.string.progress_dialog_msg),true);
						
		
	}
	
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.d(TAG, "onResume");
		
		LocService.registerListener(new ILocService() {
			
			@Override
			public void updateLocation(Location loc) {
				
				if (mPd != null && mPd.isShowing())
					mPd.dismiss();
				
				mLocation = loc;
				mList.clear();
				
				GetJSONTask task = new GetJSONTask();
				task.execute(HelperPanoramio.getUrlFromLocation(mLocation,1), null, null);
				
			}
		});
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.d(TAG, "onPause");
		
		LocService.unRegisterListener();
	}
			
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		stopService(new Intent(this,LocService.class));
	}
	
	class GetJSONTask extends AsyncTask<String, Void, Void> 
	{
		ProgressDialog pd = null;
		Boolean mShowProgress = true;
	
		protected void onPreExecute()
		{
			if (mShowProgress)
				pd = ProgressDialog.show(Main.this, "Panoramio", "Getting data ...");
		}
		
		public void setProgress (Boolean progress)
		{
			mShowProgress = progress;
		}
		
		@Override
		protected Void doInBackground(String... urls) 
		{
			
			JSONParser jparser = new JSONParser(urls[0]);
			ArrayList<PanoramioNode> list = jparser.parser();
			mList.addAll(list);
			
			Log.d(TAG,"Size: " + String.valueOf(mList.size()));			
			
			return null;
		}
		
		@Override
	    protected void onPostExecute(Void unused)
	    {
			if (mShowProgress)
			{
				pd.dismiss();
			   	initList();
			}
			else
			{
				mAdapter.notifyDataSetChanged();
			}
	    }
		
	}

	private void updateList()
	{
		Log.d(TAG, "updateList");

		if (mList.size() >= mPage*20)
		{
			mPage++;
			GetJSONTask task = new GetJSONTask();
			task.setProgress(false);
			task.execute(HelperPanoramio.getUrlFromLocation(mLocation, mPage),
					null, null);

			Toast.makeText(this, "Getting info", Toast.LENGTH_SHORT).show();
		}
	}

	
	protected void onListItemClick(ListView l, View v, int position, long id) 
	{
		
		Intent i = new Intent(this,Map.class);		
		
		// Send a Serializable Object
		i.putExtra ("OBJECT", mList.get(position));
		
		startActivity(i);
	}
	
	private void initList()
	{
		mAdapter = new MyAdapter(this);
        setListAdapter(mAdapter);	
	}


		
	public static Bitmap getBitmapFromURL(String urlBitmap) {
	    try {
	        URL url = new URL(urlBitmap);
	        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	        connection.setDoInput(true);
	        connection.connect();
	        InputStream input = connection.getInputStream();
	        Bitmap myBitmap = BitmapFactory.decodeStream(input);	        
	        return myBitmap;
	        
	    } catch (IOException e) {
	        e.printStackTrace();
	        return null;
	    }
	}
	
	public static class MyAdapter extends BaseAdapter 
	{
		private Context mContext;

		public MyAdapter(Context c) {
			mContext = c;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mList.size();			
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
		public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
			
			View view = null;

			if (convertView == null) {
				// Make up a new view
				LayoutInflater inflater = (LayoutInflater) mContext
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

				view = inflater.inflate(R.layout.item, null);
			} else {
				// Use convertView if it is available
				view = convertView;
			}
			
			
			TextView tTitle = (TextView) view.findViewById(R.id.title);
			tTitle.setText(mList.get(position).name);
		
			ImageView image = (ImageView) view.findViewById(R.id.image);
			image.setImageBitmap(mList.get(position).getPhotoThumb());						
						
			 if ( position == (mList.size() - 5))
                 ((Main)mContext).updateList();

			return view;
		}
	
	}


}
