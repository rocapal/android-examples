package es.curso.android.panoramio;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import es.curso.android.panoramio.maps.Map;
import es.curso.android.panoramio.parser.JSONParser;
import es.curso.android.panoramio.parser.PanoramioNode;

public class Main extends ListActivity {

	private final String TAG = getClass().getSimpleName(); 

	private static ArrayList<PanoramioNode> mList = new ArrayList<PanoramioNode>();
	private MyAdapter mAdapter;
	
	private Integer mPage = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitNetwork().build();
		StrictMode.setThreadPolicy(policy); 
		
		GetJSONTask task = new GetJSONTask();
		task.execute("http://rest.libregeosocial.org/social/layer/560/search/?search=&latitude=40.41687&longitude=-3.70320&radius=1.0&category=0&elems=20&format=JSON",
					  null, null);
					
		
		
		/*
		StrictMode.ThreadPolicy policy = 
				new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy); 
		
		JSONParser jparser = new JSONParser("http://rest.libregeosocial.org/social/layer/560/search/?search=&latitude=40.41687&longitude=-3.70320&radius=1.0&category=0&elems=20&format=JSON");
		ArrayList<PanoramioNode> list = jparser.parser();
		
		Log.d(TAG,"Size: " + list.size());
		 */	
		
		
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
									

			return view;
		}
	
	}


}
