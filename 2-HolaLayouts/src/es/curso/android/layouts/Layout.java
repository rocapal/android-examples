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

package es.curso.android.layouts;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class Layout extends Activity {

	private final String TAG = getClass().getSimpleName();
			
	@Override
    public void onCreate(Bundle savedInstanceState) {
	
		super.onCreate(savedInstanceState);
		
		// Obtenemos el intent que llega a esta actividad
		Intent i = this.getIntent();
		
		if (i==null)
		{
			Log.e(TAG, "Intent null");
			finish();
			return;
		}
		
		// Obtenemos el parámetro que nos dice el LayoutID que mostrar
		Integer layoutId = i.getIntExtra(HelperMain.KEY_LAYOUT_PARAM, -1);
		
		if (layoutId == -1)
		{
			Log.e(TAG, "layoutId not sent!");
			finish();		
		}
		else
		{
			setContentView(layoutId);
		}
		
	}
}
