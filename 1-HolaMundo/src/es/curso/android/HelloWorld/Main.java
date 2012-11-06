/*
 *    Copyright (C) 2012 - Roberto Calvo Palomino
 * 
 *    This program is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation; either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program; if not, write to the Free Software
 *    Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *    
 *    
 *    Authors: Roberto Calvo <rocapal [at] libresoft [dot] es>
 */


package es.curso.android.HelloWorld;

import es.curso.android.HelloWorld.R;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class Main extends Activity {
	
	private static String TAG = "Main"; 
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Log.e(TAG, "mensage");
        
    }
}