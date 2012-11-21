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

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class FormFragment extends Fragment {


	public final static int OK = 0;
	public final static int CANCEL = 1;
	private formListener listener;
	
	// Interface
	public interface formListener {
		
		public void pushOk (String text);
		public void pushCancel (String text);
		
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (activity instanceof formListener) {
			listener = (formListener) activity;
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View vista = inflater.inflate(R.layout.fragment, container, false);
		
		((Button) vista.findViewById(R.id.aceptar))
		.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				pushButton(v);
			}
		});
		((Button) vista.findViewById(R.id.cancelar))
		.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				pushButton(v);
			}
		});

		return vista;
	}	

	public void pushButton(View v) {
		if (null == listener) {
			return;
		}
		if (((Button) v).getText().equals("Ok")) {
			listener.pushOk (((EditText) getActivity().findViewById(R.id.textoForumulario)).getText().toString());
		} else {
			listener.pushCancel("");
		}
	}

}