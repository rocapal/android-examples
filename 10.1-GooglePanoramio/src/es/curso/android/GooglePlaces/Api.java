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

package es.curso.android.GooglePlaces;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;




public class Api {
	
	private final String TAG = getClass().getSimpleName();
	
	private final String SEARCH = "http://www.panoramio.com/map/get_panoramas.php?set=public&" +
				"from=%d&to=%d&" +
				"minx=%f&miny=%f&" +
				"maxx=%f&maxy=%f&" + 
				"size=small&mapfilter=true";
	
	public Api ()
	{
		
	}

	public String getUrl (Double latitude, Double longitude, Integer radio, Integer from, Integer to)
	{

		Double minY = latitude - ( radio / ( 69 * 1.609344 ));
		Double maxY = latitude + ( radio / ( 69 * 1.609344 ));
		Double minX = longitude - ( radio / ( 69 * 1.609344 ) * 1 );
		Double maxX = longitude + ( radio / ( 69 * 1.609344 ) * 1 );
		
		Log.d(TAG, String.format(SEARCH,from,to,minX,minY,maxX,maxY,longitude));
		return String.format(SEARCH,from,to,minX,minY,maxX,maxY,longitude);
	}
	
	public ArrayList<Node> searchNodes (Double latitude, Double longitude, Integer radio, Integer from, Integer to)
	{
				
		String strJson = doGetPetition (getUrl(latitude,longitude,radio,from,to));			
		
		return parseJSON(strJson);
	}
	
	public ArrayList<Node> parseJSON (String strJson)
	{
		
		JSONObject json;
		String arrayStr = "";
		try {
			json = new JSONObject(strJson);
			arrayStr = json.getJSONArray("photos").toString();
		} catch (JSONException e) {
			Log.e(TAG, e.getMessage());
		}
				
		Gson gson = new GsonBuilder().create();
		
		Type listOfNode = new TypeToken<ArrayList<Node>>(){}.getType();
		
		ArrayList<Node> array = gson.fromJson(arrayStr, listOfNode);

		Log.d(TAG, "Size: " + String.valueOf(array.size()));
		
		for (int i=0; i<array.size(); i++)
		{
			Log.d(TAG, String.valueOf(array.get(i).photo_file_url));
		}
		
		return array;
		
	}
	
	private String doGetPetition (String url)
	{
		try
		{
			DefaultHttpClient httpclient = new DefaultHttpClient();			
			HttpGet httpGet = null;
			httpGet = new HttpGet(url);
			HttpResponse response = httpclient.execute(httpGet);
			HttpEntity entity = response.getEntity();
			String str = EntityUtils.toString(entity);
			return str;
		}

		catch (IOException e) {
			Log.e(TAG,e.getMessage());
			return null;
		}
	}
	
	public Bitmap doGetImagePetition (String url)
	{

		try
		{
			DefaultHttpClient httpclient = new DefaultHttpClient();
			HttpGet httpGet = null;

			httpGet = new HttpGet(url);
			HttpResponse response = httpclient.execute(httpGet);

            InputStream inputStream;

			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
			{				                                               
				inputStream = response.getEntity().getContent();
				return BitmapFactory.decodeStream(inputStream);
			}
			
			return null;

		}catch (IOException e) {
			Log.e(TAG,e.getMessage());
			return null;
		}
	}
	
	
}
