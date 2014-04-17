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


package es.curso.android.asyncTask;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

public class MyThread extends Thread {
	
	private Context mContext;
	
	public Boolean shouldContinue = true;
	
	public MyThread() {};
	
	public void setContext (Context ctx)
	{
		mContext = ctx;
	}
	
	@Override
	public void run() {		
		
		
		
			if (!shouldContinue)
				return;

			((Activity) mContext).runOnUiThread(new Runnable() {

				@Override
				public void run() {

					Toast.makeText(mContext, 
							mContext.getString(R.string.toast_msg_thread), 
							Toast.LENGTH_SHORT).show();								
				}
			});
			
			
		}

}
