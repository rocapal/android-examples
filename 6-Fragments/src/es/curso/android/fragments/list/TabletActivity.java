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

package es.curso.android.fragments.list;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import es.curso.android.fragments.R;
import es.curso.android.fragments.list.MyListFragment.IListFragment;

public class TabletActivity extends FragmentActivity implements IListFragment
{
	
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_tablet);
        
        //show Fragment of List
        FragmentManager fm = getSupportFragmentManager();
        Fragment frag = fm.findFragmentByTag("mylist");
        if (frag == null) {
           FragmentTransaction ft = fm.beginTransaction();
           ft.add (R.id.llList, new MyListFragment(), "mylist");
           ft.commit();
        }       

	}
	@Override
	public void itemClick(Integer imageResource) {
		
		
		FragmentManager fm = getSupportFragmentManager();
	    Fragment frag = fm.findFragmentByTag("myImage");
	    
	    if (frag == null) {
	    	FragmentTransaction ft = fm.beginTransaction();
	    	ContentFragment myfrag = new ContentFragment();
	    	myfrag.setImage(imageResource);
	        ft.add (R.id.llImage, myfrag, "myImage");
	        ft.commit();
	    }       		
	    else
	    {
	    	ContentFragment myfrag = (ContentFragment) frag;
	    	myfrag.setImage(imageResource);
	    	myfrag.updateImage();
	    }
		
	}
	
}





