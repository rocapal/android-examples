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


package es.curso.android.concurrency;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

	private Integer mCount = 0;
	private final Integer NUM_THREADS = 1000;
	private ArrayList<Thread> mThreads = new ArrayList<Thread>();
	private TextView mTvResult;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mTvResult = (TextView) this.findViewById(R.id.tvResult);
		
		Button btThreads = (Button) this.findViewById(R.id.btThreads);
		btThreads.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mCount = 0;
				runThreads();				
			}
		});
	} 
	
	private void runThreads ()
	{
		for (int i=0; i<NUM_THREADS; i++)
		{
			final int j = i;
			Thread th = new Thread( new Runnable() {
				
				@Override
				public void run() {
												
					foo(j);
					
				}
			});
			
			mThreads.add(th);			
		}
		
		for (int i=0; i<NUM_THREADS; i++)
		{
			mThreads.get(i).start();
		}
		
		for (int i=0; i<NUM_THREADS; i++)
		{
			try {
				mThreads.get(i).join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		mThreads.clear();
		
		mTvResult.setText(mTvResult.getText() + "\n" + String.valueOf(mCount));
	}
	
	
	private synchronized void foo (Integer i)
	{
		for(int j=0; j<10000; j++)
			mCount = mCount +i;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	
}
