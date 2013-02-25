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


package es.curso.android.service;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Main extends Activity {

	public static Context context;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        context = this;
        
        final TextView tvDate = (TextView) this.findViewById(R.id.tvDate);
        final TextView tvTemp = (TextView) this.findViewById(R.id.tvTemp);

        
        
        
        Button btStart = (Button) this.findViewById(R.id.btStart);
        btStart.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			
				MyService.regListener(new IMyService() {
					
					@Override
					public void updateTime(String time) {
						tvDate.setText(time);						
					}

					@Override
					public void updateTemp(String temp) {
						tvTemp.setText(temp);
						
					}
				});

				startService(new Intent(getApplicationContext(), MyService.class));				
				
			}
		});
        
        Button btStop = (Button) this.findViewById(R.id.btStop);
        btStop.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				stopService(new Intent(getApplicationContext(), MyService.class));
			}
		});
        
        
    }
    
    @Override
    public void onDestroy()
    {
    	super.onDestroy();
    	stopService(new Intent(Main.this, MyService.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
