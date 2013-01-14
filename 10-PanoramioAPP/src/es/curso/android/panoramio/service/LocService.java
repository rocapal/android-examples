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


package es.curso.android.panoramio.service;

import java.util.ArrayList;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import es.curso.android.panoramio.HelperPanoramio;
import es.curso.android.panoramio.Main;
import es.curso.android.panoramio.R;
import es.curso.android.panoramio.parser.JSONParser;
import es.curso.android.panoramio.parser.PanoramioNode;

public class LocService extends Service {

	private final String TAG = getClass().getSimpleName();
	
	private final String LocProvider = LocationManager.GPS_PROVIDER;
    private static Location mCurrentLocation = null;
    public static Location lastKnownLocation = null;
    private static LocationManager mLocationManager;
    private MyLocListener mLocationListener;
    
    // Location Periodic in seconds
    private final Integer LocationPeriodic = 300;
    // Minimum distance of gps refresh
    private final Integer MinimumDistance = 500;
    
    private static ILocService mILocService;
    private NotificationManager mNM;
    
    private static ArrayList<PanoramioNode> mList = null;
    
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		
		super.onCreate();

		mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

		startService();
		
	}
	
	public static void registerListener (ILocService i)
	{
		mILocService = i;
	}
	
	public static void unRegisterListener ()
	{
		mILocService = null;
	}
	
	public static ArrayList<PanoramioNode> getList ()
	{
		return mList;
	}
	
	
	public static Location getLastLocation ()
	{
		return mCurrentLocation;
	}
	
	
	private void startService() {

		mLocationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		mLocationListener = new MyLocListener();	

		mLocationManager.requestLocationUpdates( LocProvider,LocationPeriodic, MinimumDistance,	mLocationListener);
		
	}
	
	private void notifyLocation ()
	{
		// The app is visible and enable
		if (mILocService != null)			
		{
			mILocService.updateLocation(mCurrentLocation);
		}
		else
		{
			// The app is not foreground and we launch an Android notification
			Runnable downloadTask = new Runnable() {
				public void run() {
					JSONParser jparser = new JSONParser(HelperPanoramio.getUrlFromLocation(mCurrentLocation, 1));
					mList = jparser.parser();
					
					if (mList.size() > 0)					
						showNotification();
					
				}
			};
			
			downloadTask.run();

		}
	}
	


	 private void showNotification ()
     {

		 // In this sample, we'll use the same text for the ticker and the expanded notification
		 CharSequence text = "New places available!";

		 // Set the icon, scrolling text and timestamp
		 Notification notification = new Notification(R.drawable.drawingpin,
				 text,
				 System.currentTimeMillis());

		 //Create the intent to activity
		 Intent iNotification = new Intent(this, new Main().getClass() );
		 	

		 iNotification.putExtra(HelperPanoramio.NEW_PLACES_NAME, 
				 				HelperPanoramio.NEW_PLACES_VALUE);

		 PendingIntent contentIntent = PendingIntent.getActivity
				 (this, 0, iNotification, PendingIntent.FLAG_CANCEL_CURRENT);

		 // Set the info for the views that show in the notification panel.        
		 notification.setLatestEventInfo(this, "Places Notification", text, contentIntent);

		 // Send the notification.
		 // We use a layout id because it is a unique number.  We use it later to cancel.
		 mNM.notify(HelperPanoramio.NOTIFICATION_PLACES_ID, notification);

     }

	
	private class MyLocListener implements LocationListener
    {

        public void onLocationChanged(Location loc) {

                if (loc != null) {

                	// Save the current location
                	mCurrentLocation = loc;
                	Log.d(TAG, String.valueOf(mCurrentLocation.getLatitude()) + " " + String.valueOf(mCurrentLocation.getLongitude()));

                	notifyLocation();
                        
            }
        }

        public void onProviderDisabled(String provider) {
            // TODO Auto-generated method stub
        }


        public void onProviderEnabled(String provider) {
            // TODO Auto-generated method stub
        }


        public void onStatusChanged(String provider, int status,
            Bundle extras) {
            // TODO Auto-generated method stub
        }
    }

}
