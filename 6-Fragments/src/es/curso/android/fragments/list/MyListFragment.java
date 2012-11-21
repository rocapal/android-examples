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
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import es.curso.android.fragments.R;

public class MyListFragment extends ListFragment {

	private static String[] TITLES = {
		"Imagen1",
		"Imagen2",
		"Imagen3",
		"Imagen4",
		"Imagen5",
		"Imagen6"
	};
	
	private static int[] IMAGES = {
		R.drawable.image1,
		R.drawable.image2,
		R.drawable.image3,
		R.drawable.image4,
		R.drawable.image5,
		R.drawable.image6,		
	};

	
	public interface IListFragment
	{
		void itemClick (Integer imageResource);
	}
	
	
	public MyListFragment() {}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		
		
		super.onActivityCreated(savedInstanceState);

		setListAdapter(new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1, TITLES));
				
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Log.i("MyListFragment", "Item clicked: " + id);
		
		IListFragment activity = (IListFragment) getActivity();
		if (activity != null)
			activity.itemClick(IMAGES[position]);
	}

}
