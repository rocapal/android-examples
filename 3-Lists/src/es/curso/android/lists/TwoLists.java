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

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

public class TwoLists extends Activity {
	
	private ListView mLv2;
	
	private String[] mainList = new String[] {
			"Empresas",
			"Sistemas Operativos",
			"Android"
	};
	
	private String[] secondList1 = new String[] {
			"Google",
			"Android",
			"Samsung"
	};
	
	private String[] secondList2 = new String[] {
			"Android",
			"GNU/Linux",
			"Debian"
	};
	
	private String[] secondList3 = new String[] {
			"2.3.3",
			"4.1.2",
			"4.2.0"
	};
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.twolists);
		
		ListView lv1 = (ListView) this.findViewById(R.id.list1);
		mLv2 = (ListView) this.findViewById(R.id.list2);
		
		ListAdapter la1 = new ArrayAdapter<String>(this,        							   
                android.R.layout.simple_list_item_1, 
                mainList);
		
		lv1.setAdapter(la1);
		
		lv1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				
				ListAdapter la2 = null;
				
				if (position == 0)
				{
					la2 = new ArrayAdapter<String>(getApplicationContext(),
			                android.R.layout.simple_list_item_1, 
			                secondList1);
				}
				else if (position == 1)
				{
					la2 = new ArrayAdapter<String>(getApplicationContext(),
			                android.R.layout.simple_list_item_1, 
			                secondList2);
				}
				else if (position == 2)
				{
					la2 = new ArrayAdapter<String>(getApplicationContext(),
			                android.R.layout.simple_list_item_1, 
			                secondList3);
				}
				
				if (la2 != null)
				{
					mLv2.setAdapter(la2);
				}
				
			}
		});
		
	}
}
