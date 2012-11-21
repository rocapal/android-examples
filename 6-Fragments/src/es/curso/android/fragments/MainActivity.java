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

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import es.curso.android.fragments.EditNameDialog.EditNameDialogListener;
import es.curso.android.fragments.list.MobileActivity;
import es.curso.android.fragments.list.TabletActivity;

public class MainActivity extends FragmentActivity implements EditNameDialogListener 
{

	Context mContext = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mContext = this;
        
        
        Button btActivity = (Button) this.findViewById(R.id.btFragActivity);
        btActivity.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(mContext, Activity1.class);
				startActivity(i);				
			}
		});
        
        Button btFragDialog = (Button) this.findViewById(R.id.btFragDialog);
        btFragDialog.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {			
				showEditDialog();
								
			}
		});
        
        Button btFragList = (Button) this.findViewById(R.id.btFragList);
        btFragList.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(mContext, MobileActivity.class);
				startActivity(i);				
			}
		});

        Button btFragTabletList = (Button) this.findViewById(R.id.btFragTabletList);
        btFragTabletList.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(mContext, TabletActivity.class);
				startActivity(i);				
			}
		});
    }
    
    private void showEditDialog() {
        FragmentManager fm = getSupportFragmentManager();
        EditNameDialog editNameDialog = new EditNameDialog();
        editNameDialog.show(fm, "fragment_edit_name");
        
    }

	@Override
	public void onFinishEditDialog(String inputText) {
		Toast.makeText(this, inputText, Toast.LENGTH_SHORT).show();
		
	}
    
    
	
}