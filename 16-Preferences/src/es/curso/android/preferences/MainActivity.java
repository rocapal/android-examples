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

package es.curso.android.preferences;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

	EditText etKey, etValue;
	SharedPreferences  mSharedPreferences;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		
		etKey =  (EditText) this.findViewById(R.id.etKey);
		etValue =  (EditText) this.findViewById(R.id.etValue);
		
		Button btLoad = (Button) this.findViewById(R.id.btLoad);
		Button btSave = (Button) this.findViewById(R.id.btSave);
		
		btSave.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View v) {			
				if (etKey.getText().toString().length() > 0 && 
				    etValue.getText().toString().length() > 0) {
					
					savePreference(etKey.getText().toString(), etValue.getText().toString());
				}
				else {
					Toast.makeText(getApplicationContext(), getString(R.string.msg_empty_fields), Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		btLoad.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View v) {			
				
				if (etKey.getText().toString().length() > 0) {
					
					String value = loadPreference(etKey.getText().toString());
					if (value == null)
						Toast.makeText(getApplicationContext(), getString(R.string.msg_not_found_key), Toast.LENGTH_LONG).show();
					else						
						etValue.setText(value);
				} 
				else {
					Toast.makeText(getApplicationContext(), getString(R.string.msg_empty_key), Toast.LENGTH_SHORT).show();
				}
			}
		});
		
	}
	
	private void savePreference (String key, String value)
	{
		Editor editor = mSharedPreferences.edit();
		editor.putString(key,value);
		editor.commit();
	}
	
	private String loadPreference(String key)
	{
		return mSharedPreferences.getString(key, null);		
	}
	
	
	


}
