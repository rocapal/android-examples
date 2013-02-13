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
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Main extends Activity {
	
   
	// Esta variable es un atributo de clase y su contexto es toda la clase
	// Cualquier método de esta clase puede acceder a él
	
	private Button btList, btAdvList, bt2Lists;
	
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		 
        super.onCreate(savedInstanceState);        
        
        setContentView(R.layout.main);
        
        setupWidgets();        
    }
	
	private void setupWidgets()
	{
		
		// Obtenemos instancias del XML
		btList = (Button) findViewById(R.id.btList);
		btAdvList = (Button) findViewById(R.id.btAdvlist);	
		bt2Lists = (Button) findViewById(R.id.btTwoLists);	
		
		//Asignamos listener a los botones
		btList.setOnClickListener(getOnClickDoSomething(1));
		btAdvList.setOnClickListener(getOnClickDoSomething(2));
		bt2Lists.setOnClickListener(getOnClickDoSomething(3));				
	}	
	
	// Este método lanza una nueva actividad dependiendo del parámetro
	
	private void goLayout(final Integer list)
	{
		Intent i = null;
		if (list == 1)		
			i = new Intent(this, SimpleList.class);
		else if (list == 2)
			i = new Intent(this, AdvanceList.class);
		else if (list == 3)
			i = new Intent(this, TwoLists.class);
		
		if (i!=null)
			startActivity(i);
		
	}
	
	// Este método devuelve un listener que ejecutará el método goLayout
	// con el valor 'layoutId' que le pasemos
	
	private View.OnClickListener getOnClickDoSomething(final Integer list)  {
	    return new View.OnClickListener() {
			@Override
			public void onClick(View v) {			
				goLayout(list);
			}
	    };
	}
	
	
	
}