package es.curso.android.internet;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	TextView tvConnection = null;
	TextView tvJSON = null;
	ImageView ivPhoto = null;
	
	final String TAG = getClass().getSimpleName();
	final String mUrl = "http://rest.libregeosocial.org/social/layer/560/search/?search=&latitude=40.41687&longitude=-3.70320&radius=1.0&category=0&elems=1&format=JSON";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_main);
		
		tvConnection = (TextView) this.findViewById(R.id.tvConnection);
		tvJSON = (TextView) this.findViewById(R.id.tvJSON);
		ivPhoto = (ImageView) this.findViewById(R.id.ivPhoto);
		
		
		Button btWifiSettings = (Button) this.findViewById(R.id.btWifiSettings);
		btWifiSettings.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
				
			}
		});
		
		Button btMobileSettings = (Button) this.findViewById(R.id.btMobileSettings);
		btMobileSettings.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS));
				
			}
		});
		
		StrictMode.ThreadPolicy policy = new
				StrictMode.ThreadPolicy.Builder().permitNetwork().build();
				StrictMode.setThreadPolicy(policy);
		 
		
		Button btJSON = (Button) this.findViewById(R.id.btDwonloadFile);
		btJSON.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				tvJSON.setText(doGetPetition(mUrl));				
			}
		});					
		
		Button btJSONAsyncTask = (Button) this.findViewById(R.id.btDwonloadFileAsyncTask);
		btJSONAsyncTask.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			
				getJsonAsyncTask task = new getJsonAsyncTask();
				task.execute();
				
			}
		});
		
		
	}
	
	
	public class getJsonAsyncTask extends AsyncTask<Void, Void, Void>
	{
		ProgressDialog mPd;
		String json = null;
		Bitmap bitmap =null;
		Boolean mCancel = false;
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			
			mPd = ProgressDialog.show(MainActivity.this,
					"Downloading", "Downloading JSON", false, true, new OnCancelListener() {
						
						@Override
						public void onCancel(DialogInterface dialog) {
							
							// Todo el código de este método se ejecuta
							// cuando el progressDialog es cancelado (Flecha hacia atrás).
							mCancel = true;
							Toast.makeText(MainActivity.this, "ProgressDialog Cancel", Toast.LENGTH_LONG).show();
						}
					});
		}
		
		@Override
		protected Void doInBackground(Void... params) {
			
			json = doGetPetition(mUrl);
			bitmap = getImage(json);
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (mPd.isShowing())
				mPd.dismiss();
			
			if (mCancel)
				return;
			
			if (json == null)
				Toast.makeText(MainActivity.this, 
						"Connection Error", 
						Toast.LENGTH_LONG).show();
			else
			{
				tvJSON.setText(json);
				ivPhoto.setImageBitmap(bitmap);
				
			}
		}
		
	}
	
	private Bitmap getImage (String jsonStr)
	{
		try {
			JSONObject root = new JSONObject(jsonStr);
			
			JSONArray results = root.getJSONArray("results");
			JSONObject external = results.getJSONObject(0).getJSONObject("external_info");
			String photoUrl = external.getString("photo_thumb");
			String infoUrl = external.getString("info_url");
			
			Log.d(TAG, photoUrl);
			
			return doGetImagePetition(photoUrl);
			
		} catch (JSONException e) {
			Log.e(TAG, e.getMessage());
			return null;
		}
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		
		tvConnection.setText("Connection Type: " + getConnection());

	}
	
	public String getConnection()
	{
		
		final ConnectivityManager conMan = (ConnectivityManager)
				getSystemService(Context.CONNECTIVITY_SERVICE);
		
		final State mobile = conMan.getNetworkInfo(0).getState();
		final State wifi = conMan.getNetworkInfo(1).getState();
		
		if (mobile == NetworkInfo.State.CONNECTED)
			return "mobile";
		else if (wifi == NetworkInfo.State.CONNECTED)
			return"wifi";
		
		return "No connection";
		
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
	private Bitmap doGetImagePetition (String url)
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
