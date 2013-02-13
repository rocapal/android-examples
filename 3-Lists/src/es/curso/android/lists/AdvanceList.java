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

package es.curso.android.lists;


import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class AdvanceList extends ListActivity 
{
	private MyAdapter mAdapter = null;
	
	
	// We define a structure to save the data
	public class Node 
	{
		public String mTitle;
		public String mDescription;
		public Integer mImageResource;
	}
	
	// ArrayList
	private static ArrayList<Node> mArray = new ArrayList<Node>();
	

	@Override
    public void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);                       
        setContentView(R.layout.list);
        setData();
        
        mAdapter = new MyAdapter(this);
        setListAdapter(mAdapter);	                
	    	   
	}
	
    protected void onListItemClick(ListView l, View v, int position, long id) 
	{

    	// Create a new intent to call other Activity. 
    	// Using the methods "putExtra" we can
    	// send data to the new activity
    	   	
    	
    	Toast.makeText(this, mArray.get(position).mTitle, Toast.LENGTH_SHORT).show();
	}
	
    
    private void setData ()
    {

            mArray.clear();

            Node mynode = new Node();
            

            // Restaurant 1  
            
            
            mynode.mTitle = this.getResources().getString(R.string.title1);
            mynode.mDescription = this.getResources().getString(R.string.description1);
            mynode.mImageResource = R.drawable.r1;

            mArray.add(mynode);

            //Restaurant 2
            Node mynode2 = new Node();
           
            mynode2.mTitle = this.getResources().getString(R.string.title2);
            mynode2.mDescription = this.getResources().getString(R.string.description2);
            mynode2.mImageResource = R.drawable.r2;

            mArray.add(mynode2);

            
            //Restaurant 3
            Node mynode3 = new Node();
            

            mynode3.mTitle = this.getResources().getString(R.string.title3);
            mynode3.mDescription = this.getResources().getString(R.string.description3);
            mynode3.mImageResource = R.drawable.r3;

            mArray.add(mynode3);
            
           // mArray.addAll(mArray);

    }

    public static class MyAdapter2 extends BaseAdapter
    {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 0;
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
			// TODO Auto-generated method stub
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
			return mArray.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mArray.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			Log.d("TAG", "position " + String.valueOf(position));
			View view = null;
		
			if (convertView == null)
			{
			
				LayoutInflater inflater = (LayoutInflater) mContext
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				
				view = inflater.inflate(R.layout.myitem2, null);
			}
			else
				view = convertView;
			
			
			// Example to get an image resource	
			ImageView img = (ImageView) view.findViewById(R.id.image);
			img.setImageDrawable(mContext.getResources().getDrawable(mArray.get(position).mImageResource));			
						
			TextView tTitle = (TextView) view.findViewById(R.id.title);
			tTitle.setText(mArray.get(position).mTitle);
			
			TextView Tdescription = (TextView) view.findViewById(R.id.description);
			Tdescription.setText(mArray.get(position).mDescription);
			
			return view;
	
		}

	}
}
