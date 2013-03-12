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

package es.curso.android.contentProvider;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {

	private ImageView ivThumb;
	private TextView tvData;
	
	private final String TAG = getClass().getSimpleName();
	
	private Cursor mActualImageCursor;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);		
		
		ivThumb = (ImageView) this.findViewById(R.id.ivThumb);
		tvData = (TextView) this.findViewById(R.id.tvData);
		
		final String[] proj = { MediaStore.Images.Media.DATA, MediaStore.Images.ImageColumns._ID };
		mActualImageCursor = managedQuery(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, proj,
				null, null, null);
		
		
		Log.d(TAG, String.valueOf(mActualImageCursor.getCount()));
		
		Button btNext = (Button) this.findViewById(R.id.btNext);
		btNext.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if (mActualImageCursor.moveToNext())
				{
					String name = mActualImageCursor.getString(
							mActualImageCursor.getColumnIndex(MediaStore.Images.Media.DATA));
					
					Log.d(TAG, name);
					
					String id = mActualImageCursor.getString(
							mActualImageCursor.getColumnIndex(MediaStore.Images.ImageColumns._ID));
					
					long idC = Long.parseLong(id);
					Uri newUri = ContentUris.withAppendedId(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, idC);
					
					Bitmap bitmap;
					try {
						bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),newUri);
						if (bitmap != null)
						{	
							Bitmap compBit = Bitmap.createScaledBitmap(bitmap, 100, 100, true);
							bitmap.recycle();
                 
							if (compBit != null)
							{
								ivThumb.setImageBitmap(compBit);						
								tvData.setText(name);
							}
						}
						
					} catch (FileNotFoundException e) {						
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				
			}
		});
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
