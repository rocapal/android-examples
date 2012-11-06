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

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;


public class SimpleList extends ListActivity {

	
	private String[] testValues = new String[] {
			"URJC",
			"EOI",
			"Android"
	};
	
	private ListView lv1 = null;
	private ListAdapter la1 = null;
	
	@Override
    public void onCreate(Bundle savedInstanceState) 
	{                                           
			   
        super.onCreate(savedInstanceState);        
        setContentView(R.layout.list);
                        
        lv1 = (ListView)findViewById(android.R.id.list);
        
        
        la1 = new ArrayAdapter<String>(this,        							   
        		                       android.R.layout.simple_list_item_1, 
        		                       testValues);
        
        
        lv1.setAdapter(la1);
        
	}
	
	 protected void onListItemClick (ListView l, View v, int position, long id) 
	 {	 		 
		 
		 Toast.makeText(this, testValues[position], Toast.LENGTH_SHORT).show();
	 }
	
}
