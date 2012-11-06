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
 *  Author : Roberto Calvo Palomino <rocapal at libresoft dot es>
 *           
 */
package es.curso.android.Gallery;


import com.libresoft.pruebagallery.R;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

public class PruebaGallery extends Activity {

    // Array of image resources
	int[] image_array = {R.drawable.android, R.drawable.libresoft, R.drawable.splash, R.drawable.urjc};
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        // Getting gallery from XML
        Gallery mGallery = (Gallery)findViewById(R.id.my_gallery);
        
        // Creating a custom adapter to set the gallery items
        MyAdapter adapter = new MyAdapter(getBaseContext());
        mGallery.setAdapter(adapter);
    }
    
	public class MyAdapter extends BaseAdapter {

		private Context mContext;

		public MyAdapter(Context c) {
			mContext = c;
		}

		public int getCount() {
			// TODO Auto-generated method stub
			return image_array.length;
		}

		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			View view;

			if (convertView == null) {
				// Make up a new view
				LayoutInflater inflater = (LayoutInflater) mContext
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = inflater.inflate(R.layout.item, null);
			} else {
				// Use convertView if it is available
				view = convertView;
			}
			
			ImageView image_view = (ImageView)view;
			image_view.setImageResource(image_array[position]);
			
			return view;

		}

	}
}