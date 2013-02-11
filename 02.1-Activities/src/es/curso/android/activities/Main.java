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

package es.curso.android.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class Main extends Activity {
	
	public static final String VALUE_1 = "TEXT";
	public static final String VALUE_2 = "INT";

	public static String PARAM = "PARAMETER"; 
	
	private final int FROM_ACTIVITY_2 = 1;
	private final int FROM_ACTIVITY_3 = 2;
	
	Button btAct1, btAct2, btAct3, btBrowser, btCall;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		setupWidgets();
	}
	
	
    
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {	
		super.onActivityResult(requestCode, resultCode, data);
	
		if (resultCode == Activity.RESULT_OK)
		{
			Integer value = data.getIntExtra(PARAM, -1);
			
			if (requestCode == FROM_ACTIVITY_2)
			
				Toast.makeText(this, "Come back from Activity 2: " + String.valueOf(value), 
						Toast.LENGTH_SHORT).show();
			
			
			
			else if (requestCode == FROM_ACTIVITY_3)
				Toast.makeText(this, "Come back from Activity 3: " + String.valueOf(value), 
						Toast.LENGTH_SHORT).show();
		}
	}
	
	private void setupWidgets ()
	{
		
		btAct1 = (Button) this.findViewById(R.id.bt1);
		btAct2 = (Button) this.findViewById(R.id.bt2);
		btAct3 = (Button) this.findViewById(R.id.bt3);
		btBrowser = (Button) this.findViewById(R.id.bt4);
		btCall = (Button) this.findViewById(R.id.bt5);
		
		
		btAct1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent(Main.this, Activity1.class);
				intent.putExtra(VALUE_1, getString(R.string.app_name));
				intent.putExtra(VALUE_2, 3);
				
				
				startActivity(intent);			
				
			}
		});
		
		btAct2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent(Main.this, Activity2.class);
				startActivityForResult(intent, FROM_ACTIVITY_2);
				
			}
		});
		
		btAct3.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			
				Intent intent = new Intent(Main.this, Activity3.class);
				startActivityForResult(intent, FROM_ACTIVITY_3);
			}
		});
		
		
		btBrowser.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent browserIntent = 	new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.es"));
				startActivity(browserIntent);
				
			}
		});
		
		btCall.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent(Intent.ACTION_CALL);
				intent.setData(Uri.parse("tel:666-666-666"));
				startActivity(intent);
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        
            case R.id.Menu1:            	
            	Intent intent = new Intent(Main.this, Activity2.class);
				startActivityForResult(intent, FROM_ACTIVITY_2);								
                return true;
                
            case R.id.Menu2:
            	intent = new Intent(Main.this, Activity3.class);
				startActivityForResult(intent, FROM_ACTIVITY_3);							
            	return true;
            	
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
