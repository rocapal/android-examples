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

import java.net.URI;
import java.net.URISyntaxException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Main extends Activity {

	private final String TAG = getClass().getSimpleName();
	
	public static Context context = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        context = this;
        
        MyAsyncTask task = new MyAsyncTask();
        task.execute(null,null,null);
        
        Button btFile = (Button) this.findViewById(R.id.btFile);
        btFile.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				final DownloadFileTask task = new DownloadFileTask();				                
                try {
					task.execute(new URI("https://github.com/rocapal/android-examples/archive/master.zip"), null, null);
					
				} catch (URISyntaxException e) {
					Log.d(TAG, e.getMessage());
				}
			}
		});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    
    // This asyncTasks is an example of use. Just it shows de progress dialog
    // by 3 seconds and then close the progress dialog.
    public class MyAsyncTask extends AsyncTask<Void, Void, Void>{

    	ProgressDialog pd = null;
    	
    	protected void onPreExecute()
    	{
    		pd = ProgressDialog.show(Main.this, "AsyncTask Example", "Initializing ...");
    	}
    	
    	@Override
    	protected Void doInBackground(Void... arg0) {
    		// No interact with the UI in this method
    		
    		// Wait 3 seconds
    		try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		
    		return null;
    	}

    	
    	@Override
        protected void onPostExecute(Void unused)
        {
        	pd.dismiss();         
        }

    }

}
