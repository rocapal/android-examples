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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Main extends Activity {
	
   
	// Esta variable es un atributo de clase y su contexto es toda la clase
	// Cualquier método de esta clase puede acceder a él
	
	private Button btFrame, btLinearH, btLinearV, btRelative, btTable, btCard;
	
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		 
        super.onCreate(savedInstanceState);        
        
        setContentView(R.layout.main);
        
        setupWidgets();
        /*
        tvName = (TextView) this.findViewById(R.id.name);
        
        Button btLocalizame = (Button) this.findViewById(R.id.btLocalizame);
        btLocalizame.setOnClickListener(myListener);
                
        //SetSelect es necesario para iniciar la marquesina en un TextView
        TextView txtSessionsInfo = (TextView) this.findViewById(R.id.tvSessionsInfo);        
        txtSessionsInfo.setSelected(true);
          */  
    }
	
	private void setupWidgets()
	{
		
		// Obtenemos instancias del XML
		btFrame = (Button) findViewById(R.id.btFrame);
		btLinearH = (Button) findViewById(R.id.btLinearH);
		btLinearV = (Button) findViewById(R.id.btLinearV);
		btRelative = (Button) findViewById(R.id.btRelative);
		btTable = (Button) findViewById(R.id.btTable);
		btCard = (Button) findViewById(R.id.btCard);
		
		//Asignamos listener a los botones
		btFrame.setOnClickListener(getOnClickDoSomething(R.layout.frame_layout));
		btLinearH.setOnClickListener(getOnClickDoSomething(R.layout.linear_layout_h));
		btLinearV.setOnClickListener(getOnClickDoSomething(R.layout.linear_layout));
		btRelative.setOnClickListener(getOnClickDoSomething(R.layout.relative_layout));
		btTable.setOnClickListener(getOnClickDoSomething(R.layout.tablelayout));
		btCard.setOnClickListener(getOnClickDoSomething(R.layout.profile));
		
		
	}	
	
	// Este método lanza una nueva actividad (Layout.class) y le pasa
	// el id del layout a mostrar 'layoutId'
	
	private void goLayout(final Integer layoutId)
	{
		Intent i = new Intent(this, Layout.class);
		i.putExtra(HelperMain.KEY_LAYOUT_PARAM, layoutId);
		startActivity(i);		
	}
	
	// Este método devuelve un listener que ejecutará el método goLayout
	// con el valor 'layoutId' que le pasemos
	
	private View.OnClickListener getOnClickDoSomething(final Integer layoutId)  {
	    return new View.OnClickListener() {
			@Override
			public void onClick(View v) {			
				goLayout(layoutId);
			}
	    };
	}
	
}