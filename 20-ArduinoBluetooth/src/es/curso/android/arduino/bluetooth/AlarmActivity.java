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

package es.curso.android.arduino.bluetooth;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class AlarmActivity extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alarm);
		
		// Cancel and remove the notification
		NotificationManager nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		nm.cancel(1);
		
		TextView tvAlarm = (TextView) this.findViewById(R.id.tvAlarm);
		tvAlarm.setText("Last notification of open door! \n");
		
		Intent i = getIntent();
		if (i != null)
		{
			long currentMilis =i.getLongExtra("DATE", 0);
			
			SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");			
	        Date resultdate = new Date(currentMilis);	   
	        
	        tvAlarm.setText(tvAlarm.getText() + sdf.format(resultdate));
		}
		
		
	}

}
