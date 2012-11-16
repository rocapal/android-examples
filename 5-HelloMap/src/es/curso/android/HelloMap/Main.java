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


package es.curso.android.HelloMap;

import java.io.IOException;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;


public class Main extends MapActivity {
    /** Called when the activity is first created. */

    private Location mLoc;
    private MapView mapview = null;
    private MapController mapControl = null; 
    private TextView tvlocation;
    
    private static final int DIALOG_ADDRESS = 1;
    
    private static final int MENU_SETTINGS = Menu.FIRST + 1;
    private static final int MENU_ABOUT = Menu.FIRST + 2;
    private static final int MENU_ADDRESS = Menu.FIRST + 3;
    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mapview = (MapView) findViewById(R.id.myMapView);
        
        tvlocation = (TextView) findViewById(R.id.tvlocation);
        
        mapview.setBuiltInZoomControls(true);
        mapControl = mapview.getController();
                
        getLocation();
        
               
    }    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
    	super.onCreateOptionsMenu(menu);
    	
        menu.add(0, MENU_SETTINGS, Menu.NONE ,"Show Location")
        .setIcon(R.drawable.icon)
        .setAlphabeticShortcut('S');
        
        menu.add(0, MENU_ADDRESS, Menu.NONE ,"Address")
        .setIcon(R.drawable.icon)
        .setAlphabeticShortcut('A');

        menu.add(0, MENU_ABOUT, Menu.NONE, "About")
        .setIcon(R.drawable.icon)
        .setAlphabeticShortcut('B');
        
        return true;
    }
    

    public boolean onOptionsItemSelected (MenuItem item) {
    	
    	switch (item.getItemId()) {
    	
    		case MENU_SETTINGS:
    			refreshMap();
    			break;
    			
    		case MENU_ADDRESS:
    			showDialog(DIALOG_ADDRESS);
    			break;
    			
    		case MENU_ABOUT:
    			Toast.makeText(getBaseContext(),
                        "About pulsado",
                        Toast.LENGTH_LONG).show();
    			break;
    			
    	}
    	
    	return true;
    
    }
    
    private void refreshMap()
    {
    	
    	if (mLoc == null)
    	{
    		Toast.makeText(getBaseContext(),
                    "Location not available!",
                    Toast.LENGTH_LONG).show();
    		
    		return;
    	}
    	
        GeoPoint geoPoint = new GeoPoint ( (int) (mLoc.getLatitude() * 1000000),
				                           (int) (mLoc.getLongitude() * 1000000));

        
        
        mapControl.setZoom(18);
		mapControl.animateTo(geoPoint);
		
		
		
		MapOverlay myMapOver = new MapOverlay();
		myMapOver.setDrawable(getResources().getDrawable(R.drawable.drawingpin));
		myMapOver.setGeoPoint(geoPoint);
		
		final List<Overlay> overlays = mapview.getOverlays();
		overlays.clear();

		overlays.add(myMapOver);

		
		//mapview.setSatellite(true);
		//mapview.setBuiltInZoomControls(true);
		
				
		mapview.setClickable(true);
    	
		
		tvlocation.setText("Your Current Location: \n" + 
				           String.valueOf(mLoc.getLatitude()) + " , " +
				           String.valueOf(mLoc.getLongitude()));
		
    	
    }
    
    @Override
    protected Dialog onCreateDialog(int id) 
    {

    	switch (id) {

        	case DIALOG_ADDRESS:

        		LayoutInflater factory = (LayoutInflater) this.getApplicationContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        		
        		final View textEntryView = factory.inflate(R.layout.manual, null);
        		
        		final EditText edit = (EditText) textEntryView.findViewById (R.id.txtmanual);
                edit.setText("");
                
                return new AlertDialog.Builder(this)
                .setTitle("Insert Address")
                .setView(textEntryView)
                .setPositiveButton("Search", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (!edit.getText().toString().equals(""))
                                searchManual(edit.getText().toString());
                        else
                                Toast.makeText(getBaseContext(),
                                    "Empty Address",
                                    Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        /* User clicked cancel so do some stuff */
                    }
                })
                .create();                        		
        		
    	}
    	return null;
    }
    
    public void searchManual(String address){
    	
        Geocoder gcod = new Geocoder(getApplicationContext());
        
        try {
                List<Address> addressList = gcod.getFromLocationName(address, 1);

                if (addressList.isEmpty()){

                        Toast.makeText(getBaseContext(),
                                        "There's no address",
                                        Toast.LENGTH_LONG).show();
                }
                if (addressList.get(0).hasLatitude() && addressList.get(0).hasLongitude()){

                        Location loc = new Location("MANUAL_PROVIDER");
                        loc.setLatitude(addressList.get(0).getLatitude());
                        loc.setLongitude(addressList.get(0).getLongitude());

                        mLoc = loc;
                        refreshMap();
                        
                        //Send coordinates to the server
                        Toast.makeText(getBaseContext(), 
                        		       "Location updated", 
                        		       Toast.LENGTH_SHORT).show();
                } else
                        Toast.makeText(getBaseContext(),
                                        "There are no coordinates",
                                        Toast.LENGTH_LONG).show();

        } catch (IOException e) {
                e.printStackTrace();
        }
}

    
    private void getLocation ()
    {    
    	LocationManager mLocationManager;
    	LocationListener mLocationListener;
    	
    	mLocationManager = (LocationManager)
    	         getSystemService(Context.LOCATION_SERVICE);
    	
    	mLocationListener = new MyLocationListener();
    	
    	mLocationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER,
    											 5000, 15, mLocationListener); 
    	  	
    }
    
    
    private Location getLocationByCriteria ()
    {
    	LocationManager mLocationManager;
    	mLocationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
    	
    	Criteria criteria = new Criteria();
    	
    	criteria.setAccuracy(Criteria.ACCURACY_FINE);
    	criteria.setAltitudeRequired(false);
    	criteria.setBearingRequired(false);
    	criteria.setPowerRequirement(Criteria.POWER_LOW);
    	
    	String provider = mLocationManager.getBestProvider(criteria, true);

    	if (provider==null)
    		return null;
    	
    	return mLocationManager.getLastKnownLocation(provider);
    	
    }   
    
    private class MyLocationListener implements LocationListener
    {

		@Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			
			mLoc = location;
			Log.d("Location:", String.valueOf(mLoc.getLatitude()) + 
					           " " + 
					           String.valueOf(mLoc.getLongitude()));
			
			refreshMap();
			
		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			
		}

		
    	
    }

	@Override	
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
}

