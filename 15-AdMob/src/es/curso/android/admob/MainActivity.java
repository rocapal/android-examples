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

package es.curso.android.admob;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.ads.Ad;
import com.google.ads.AdListener;
import com.google.ads.AdRequest;
import com.google.ads.AdRequest.ErrorCode;
import com.google.ads.AdView;

public class MainActivity extends Activity {
	
	private AdView adView;
	private LinearLayout llHeader = null; 
	private Button btHide = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Button btRefresh = (Button) findViewById(R.id.btRefresh);
		btRefresh.setOnClickListener(refreshAction);

		btHide = (Button) findViewById(R.id.btOcultar);
		btHide.setOnClickListener(hideAction);
	      
		llHeader = (LinearLayout) findViewById (R.id.lladv);

		// Look up the AdView as a resource and load a request.
		adView = (AdView)this.findViewById(R.id.adView);
		
		adView.setAdListener(new AdListener() {
			
			@Override
			public void onReceiveAd(Ad arg0) {
				showAdv();
			}
			
			@Override
			public void onPresentScreen(Ad arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onLeaveApplication(Ad arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onFailedToReceiveAd(Ad arg0, ErrorCode arg1) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onDismissScreen(Ad arg0) {
				// TODO Auto-generated method stub
				
			}
		});

		getAds();

	}
	
	private void showAdv()
	{
		llHeader.setVisibility(View.VISIBLE);
 		btHide.setText("Ocultar");
 		
 		Thread th = new Thread(new Runnable() {
			
			@Override
			public void run() {				
				
				try {
					Thread.sleep(8000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				MainActivity.this.runOnUiThread( new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						llHeader.setVisibility(View.INVISIBLE);
				 		btHide.setText("Mostrar");
					}
				});
			}
		});
 		
 		th.start();
 		
	}
	

	@Override
	protected void onDestroy() {
	
		//destroy add
	    if (adView != null) {
	        adView.destroy();
	    }
	    
		super.onDestroy();
	}
	private void getAds()
	{
			
		AdRequest request = new AdRequest();
		adView.loadAd(request);
		
		
	}
	private OnClickListener refreshAction = new OnClickListener() {

		@Override
		public void onClick(View v) {
			 getAds();
		}
    	
    };
    
    
    
    private OnClickListener hideAction = new OnClickListener() {

		@Override
		public void onClick(View v) {
			 	if (llHeader.getVisibility() == View.VISIBLE )
			 	{
			 		llHeader.setVisibility(View.INVISIBLE);
			 		btHide.setText("Mostrar");
			 	}
			 	else
			 	{
			 		llHeader.setVisibility(View.VISIBLE);
			 		btHide.setText("Ocultar");
			 	}
		}
    	
    };
}
