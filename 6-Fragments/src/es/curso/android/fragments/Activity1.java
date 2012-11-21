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

package es.curso.android.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import es.curso.android.fragments.FormFragment.formListener;

public class Activity1 extends FragmentActivity implements  formListener{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity1);          
        

        final Button btFragment = (Button) this.findViewById(R.id.btFragment);
        btFragment.setOnClickListener(new OnClickListener() {

        	@Override
        	public void onClick(View v) {
        		// TODO Auto-generated method stub
        		if ( btFragment.getText().toString().compareTo(getResources().getString(R.string.button_show)) == 0 )
        		{
        			showFragment();
        			btFragment.setText(getResources().getString(R.string.button_hide));					 
        		}
        		else
        		{
        			hideFragment();
        			btFragment.setText(getResources().getString(R.string.button_show));
        		}

        	}
        });
    }

    // Use the following source code to show the fragment from
    // Java source code
    private void showFragment()
    {
    	 FragmentManager fm = getSupportFragmentManager();
         Fragment editor = fm.findFragmentByTag("editor");
         if (null == editor) {
            FragmentTransaction ft = fm.beginTransaction();
            ft.add (R.id.container, new FormFragment(), "editor");
            ft.commit();
         }         
    }
    
    private void hideFragment()
    {
    	FragmentManager fm = getSupportFragmentManager();
		Fragment editor = fm.findFragmentByTag("editor");
		FragmentTransaction ft = fm.beginTransaction();
		ft.remove(editor);
		ft.commit();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    //
	@Override
	public void pushOk(String text) {
		Toast.makeText(this, text, Toast.LENGTH_LONG).show();
		
	}

	@Override
	public void pushCancel(String text) {
		Toast.makeText(this, "Cancel", Toast.LENGTH_LONG).show();		
	}
}
