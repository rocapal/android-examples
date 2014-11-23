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

import java.util.ArrayList;

import org.json.JSONObject;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;


public class MainActivity extends ListActivity {

	private ArrayList<Node> mArray = null;
	private Api mApi = new Api();
	private MyAdapterVolley mAdapter = null;
	
	private Double mLatitude = 40.282987;
	private Double mLongitude = -3.821021;
	private Integer mRadio = 5;
	private Integer mFrom = 0;
	private Integer mTo = 0;
	private Integer mAmount = 20;
	
	private final String TAG = getClass().getSimpleName();
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //MyTask task = new MyTask();
        //task.execute();
        
        mTo = mFrom + mAmount;
        updateList(mLatitude, mLongitude, mRadio, mFrom, mTo);
        
    }
    
    private synchronized void updateList (Double latitude, Double longitude, Integer radio, Integer from, Integer to)
    {
        RequestQueue queue = Volley.newRequestQueue(this);
        
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, mApi.getUrl(latitude, longitude, radio, from, to), null, 
        		new Response.Listener<JSONObject>() 
        		{
        			@Override
        			public void onResponse(JSONObject response) {  
        				if (mArray == null)
        					mArray = mApi.parseJSON(response.toString());
        				else
        					mArray.addAll (mApi.parseJSON(response.toString()));
        				
        				if (mAdapter == null)
        				{
        					mAdapter = new MyAdapterVolley();        				
        					setListAdapter(mAdapter);
        				}
        				else
        					mAdapter.notifyDataSetChanged();
        			}
        		}, 
        		new Response.ErrorListener() 
        		{
        			@Override
        			public void onErrorResponse(VolleyError error) {            
        				Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
        			}
        		}
        );
        
        queue.add(getRequest);
    }

   
    
    
    public class MyAdapterVolley extends BaseAdapter
    {
    	ImageLoader.ImageCache mCache = new BitmapLruCache();
    	ImageLoader mLoader = new ImageLoader(Volley.newRequestQueue(getBaseContext()), mCache);
    	
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mArray.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
		
			View view = convertView	;
			
			if (view == null){
				view = getLayoutInflater().inflate(R.layout.item_volley, null);
			}
						
			if (position == mTo-5 )
			{
				mFrom = mTo +1;
				mTo = mFrom + mAmount;
		        updateList(mLatitude, mLongitude, mRadio, mFrom, mTo);
			}
				
			TextView title = (TextView) view.findViewById(R.id.tvTitleVolley);
			title.setText(mArray.get(position).photo_title);
			
			NetworkImageView image = (NetworkImageView) view.findViewById(R.id.ivPictureVolley);			
			image.setAdjustViewBounds(true);
			image.setDefaultImageResId(R.drawable.defaultimage);
			image.setErrorImageResId(R.drawable.error);			
			image.setImageUrl(mArray.get(position).photo_file_url, mLoader);
			return view;
		}
    	
    }
    
    
    public class MyTask extends AsyncTask<Void, Void, Void>
    {
    	ProgressDialog mPd;
    	
    	@Override
    	protected void onPreExecute() {
    		// TODO Auto-generated method stub
    		super.onPreExecute();
    		
    		mPd = ProgressDialog.show(MainActivity.this, "Descargando", "Getting nodes");
    	}
    	
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			
			mArray = mApi.searchNodes(mLatitude, mLongitude, mRadio, mFrom, mTo);
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (mPd.isShowing())
				mPd.dismiss();
			
			mAdapter = new MyAdapterVolley();
			setListAdapter(mAdapter);
		}
    	
    }
    
    public class ViewHolder {
    	ImageView image;
    	TextView title;
    	int position;
    }

    public class MyAdapter extends BaseAdapter
    {
    	

		@Override
		public int getCount() {
			return mArray.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			
			ViewHolder holder = null;			

            if (convertView == null) {
                    // Make up a new view
                    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    convertView = inflater.inflate(R.layout.item, null);
                    
                    holder = new ViewHolder();
                    holder.position = position;
                    holder.title = (TextView) convertView.findViewById(R.id.tvTitle);
                    holder.image = (ImageView) convertView .findViewById(R.id.ivPicture);
                    convertView.setTag(holder);
                    
            } else {
                    // Use convertView if it is available
            		holder = (ViewHolder)convertView.getTag();
            }
            
            holder.title.setText(mArray.get(position).photo_title);
                        
            new AsyncTask<ViewHolder, Void, Bitmap>() 
            {
            	private ViewHolder mHolder;
				@Override
				protected Bitmap doInBackground(ViewHolder... params) {
					
					mHolder = params[0];
					return mApi.doGetImagePetition(mArray.get(params[0].position).photo_file_url);
					
				}
            	
				protected void onPostExecute(Bitmap result) {
					
					mHolder.image.setImageBitmap(result);
				};
            }.execute(holder);
            

            return convertView;
		}    
    }
}
