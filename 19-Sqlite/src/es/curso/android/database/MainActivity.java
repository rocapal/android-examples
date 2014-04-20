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

package es.curso.android.database;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Vector;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import es.curso.android.database.ClientsSQLite.Client;

public class MainActivity extends Activity {

	
	ClientsSQLite mClients = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mClients = new ClientsSQLite(this);
		
		final EditText etName = (EditText) this.findViewById(R.id.etName);
		final EditText etEmail = (EditText) this.findViewById(R.id.etEmail);		
		Button btAdd = (Button) this.findViewById(R.id.btAdd);
		btAdd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				SimpleDateFormat dateFormat = new SimpleDateFormat(
		                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
		        Date date = new Date();
		        		        
				Boolean ret = mClients.saveClient(etName.getText().toString(), etEmail.getText().toString(), 
						dateFormat.format(date));
				
				Toast.makeText(getApplicationContext(), 
						ret ? getApplicationContext().getString(R.string.msg_record_ok) : 
							getApplicationContext().getString(R.string.msg_record_fail), Toast.LENGTH_SHORT).show();
							
			}
		});
		
		final TextView tvPanel = (TextView) this.findViewById(R.id.tvPanel);
		final EditText etNameSearch = (EditText) this.findViewById(R.id.etSearchName);
		final CheckBox cbDateSearch = (CheckBox) this.findViewById(R.id.cbDateSearch);
		cbDateSearch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
								
					etNameSearch.setEnabled(!isChecked);
				
			}
		});
		
		final Button btSearch = (Button) this.findViewById(R.id.btsearch);
		btSearch.setOnClickListener(new OnClickListener() {
						
			@Override
			public void onClick(View v) {
			
				tvPanel.setText("");
				
				Vector<Client> result = null;
				
				if ( cbDateSearch.isChecked() )
					result = mClients.getClient(null);
				else
					result = mClients.getClient(etNameSearch.getText().toString());													
												
				for (int i = 0; i < result.size(); i++) {
					
					
					tvPanel.setText(tvPanel.getText() + "\n" + 
							result.get(i).id + getApplication().getResources().getString(R.string.tab) + 
							result.get(i).name + getApplication().getResources().getString(R.string.tab) + 
							result.get(i).email + getApplication().getResources().getString(R.string.tab) + 
							result.get(i).datetime);
				}
			}
		});
		
		
	}

	
}
