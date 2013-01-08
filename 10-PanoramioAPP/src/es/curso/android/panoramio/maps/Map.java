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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
	private final String TAG = getClass().getSimpleName();
	
    private Location mLoc = null;
    private MapView mapview = null;
    private MapController mapControl = null; 
    
    private static final int DIALOG_ADDRESS = 1;
    
    PanoramioNode pnode = null;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);

        mapview = (MapView) findViewById(R.id.myMapView);
        
        Intent i = getIntent();
        
        pnode = (PanoramioNode) i.getParcelableExtra("OBJECT");                       

        mLoc = new Location("MyLocation");
        mLoc.setLatitude(pnode.latitude);
        mLoc.setLongitude(pnode.longitude);
        
        mapview.setBuiltInZoomControls(true);
        mapControl = mapview.getController();
                
        refreshMap();
               
    }    
   
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       
    	
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }
    
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        
            case R.id.MenuPhoto:            	
            	String fileName = downloadPhoto();
            	
            	// Show Picture with Android Gallery
            	Intent intent = new Intent();
            	intent.setAction(Intent.ACTION_VIEW);
            	intent.setDataAndType(Uri.parse("file://" + fileName), "image/jpeg");
            	startActivity(intent);
                return true;
                
            case R.id.MenuURL:

            	Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(pnode.info_url));
            	startActivity(browserIntent);
                return true;
                
                
            case R.id.MenuShare:
            	
            	fileName = downloadPhoto();
            	
            	Intent i = new Intent(Intent.ACTION_SEND);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);                              
                i.setType("image/jpeg");
                i.putExtra(Intent.EXTRA_SUBJECT, "[PanoramioJSON-Photo] " + pnode.name);
                i.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(fileName)));
                i.putExtra(Intent.EXTRA_TEXT, pnode.name);
                
                startActivity(i);
            	
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    
    private String downloadPhoto ()
    {
    	final String fileName = Environment.getExternalStorageDirectory() + File.separator + "photo.jpg";
    	Log.d(TAG, fileName);
    	
    	// Download Picture
    	Bitmap bm = pnode.getPhoto();
    	
    	// Convert to bytes
    	ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    	bm.compress(Bitmap.CompressFormat.JPEG, 40, bytes);
    	
    	// Write the BitMap to file
    	File f = new File(fileName);      
    	
    	try {
			f.createNewFile();
			FileOutputStream fo = new FileOutputStream(f);            	
	    	fo.write(bytes.toByteArray());
	    	fo.close();
	    	
		} catch (IOException e) {		
			Log.e(TAG, e.getMessage());
			return null;
		}            	
    	
    	return fileName;
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
		Drawable drawable = new BitmapDrawable(getResources(), pnode.getPhotoThumb());
		myMapOver.setDrawable(drawable);		
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

