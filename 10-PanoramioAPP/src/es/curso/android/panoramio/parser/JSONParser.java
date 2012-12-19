package es.curso.android.panoramio.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class JSONParser {
	
	private final String TAG = getClass().getSimpleName(); 
	private String mUrl;
		
	
	public JSONParser (String url)
	{
		mUrl = url; 
	}
	
	public ArrayList<PanoramioNode> parser()
	{
				
		String content = doGetPetition();
		
		return parseNodes(content);		
		
	}
	
	private ArrayList<PanoramioNode> parseNodes (String content)
	{
		ArrayList<PanoramioNode> lnodes = new ArrayList<PanoramioNode>();
		
		JSONArray array;
		JSONObject json = null;
		
		try
        {
			json = new JSONObject(content);
	        array = json.getJSONArray("results");
	        
	        for (int i = 0; i < array.length(); i++)
	        {
	        	
	        	JSONObject node = array.getJSONObject(i);
	        	PanoramioNode pnode = parseNode (node);
	        	
	        	lnodes.add(pnode);
	        }	        
	        return lnodes;
        } 
		catch (JSONException e)
        {
			Log.e(TAG, e.getMessage());
			return null;
        }		
	}
	
	private PanoramioNode parseNode (JSONObject jsonData)
	{
		
		PanoramioNode pnode = new PanoramioNode();
		
		try
		{
			if (jsonData.has("name"))
				pnode.name = jsonData.getString("name");
			
			if (jsonData.has("since"))
				pnode.date = jsonData.getString("since");
			
			if (jsonData.has("position"))
			{
				JSONObject jsonPosition = jsonData.getJSONObject("position");
				if (jsonPosition.has("latitude"))
					pnode.latitude = jsonPosition.getDouble("latitude");
				
				if (jsonPosition.has("longitude"))
					pnode.longitude = jsonPosition.getDouble("longitude");
			}
			
			if (jsonData.has("external_info"))
			{
				JSONObject jsonInfo = jsonData.getJSONObject("external_info");
				
				if (jsonInfo.has("info_url"))
					pnode.info_url = jsonInfo.getString("info_url");
				
				if (jsonInfo.has("photo_thumb"))
					pnode.thumb_url = jsonInfo.getString("photo_thumb");
				
				if (jsonInfo.has("photo_url"))
					pnode.photo_url = jsonInfo.getString("photo_url");
				
			}									
			
			return pnode;
		}
		catch (JSONException e)
		{
			Log.e(TAG, e.getMessage());
			return null;
		}	
				
		
	}
	
	
	private String doGetPetition ()
	{
		
		try
		{
			DefaultHttpClient httpclient = new DefaultHttpClient();
			HttpGet httpGet = null;

			httpGet = new HttpGet(mUrl);
			HttpResponse response = httpclient.execute(httpGet);
			HttpEntity entity = response.getEntity();

			String str = convertStreamToString(entity.getContent());

			return str;

		}catch (IOException e) {
			Log.e("doGet",e.getMessage());
			return null;
		}	
	}
	
	
	private String convertStreamToString (InputStream is)
	{

		BufferedReader reader = new BufferedReader(new InputStreamReader(is), 8*1024);
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return sb.toString();

	}


	
}
