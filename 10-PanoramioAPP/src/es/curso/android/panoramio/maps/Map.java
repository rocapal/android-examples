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


package es.curso.android.panoramio.maps;

import java.util.List;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

import es.curso.android.panoramio.R;
import es.curso.android.panoramio.parser.PanoramioNode;


public class Map extends MapActivity {
	
    /** Called when the activity is first created. */

    private Location mLoc = null;
    private MapView mapview = null;
    private MapController mapControl = null; 
    
    private static final int DIALOG_ADDRESS = 1;
    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);

        mapview = (MapView) findViewById(R.id.myMapView);
        
        Intent i = getIntent();
        
        PanoramioNode pnode = (PanoramioNode) i.getSerializableExtra("OBJECT");                       

        mLoc = new Location("MyLocation");
        mLoc.setLatitude(pnode.latitude);
        mLoc.setLongitude(pnode.longitude);
        
        mapview.setBuiltInZoomControls(true);
        mapControl = mapview.getController();
                
        refreshMap();
               
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
    	
    }
    
   
    
    
	@Override	
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
}

