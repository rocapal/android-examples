package es.curso.android.mapsv2;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends Activity {
	 
    // Google Map
    private GoogleMap googleMap; 
 
    @Override 
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); 
        setContentView(R.layout.main);
 
        double latitude = 40.3223400;
        double longitude = -3.8649600;
        
        googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        
        CameraPosition cameraPosition = new CameraPosition.Builder().target(
                new LatLng(latitude, longitude)).zoom(12).build();
 
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));              
        
        // create marker
        MarkerOptions marker = new MarkerOptions().position(new LatLng(latitude, longitude)).title("Mostoles");         
        googleMap.addMarker(marker);        
        marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        
    }
     
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
    
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
    	// TODO Auto-generated method stub
    	
    	switch (item.getItemId())
    	{
    	case R.id.map_normal:
    		googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    		return true;
    		
    	case R.id.map_hybrid:
    		googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
    		return true;    
    		
    	case R.id.map_sattelite:
    		googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
    		return true;
    		
    	case R.id.map_terrain:
    		googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
    		return true;
    	}
    	
    	return super.onMenuItemSelected(featureId, item);
    }
 
}
