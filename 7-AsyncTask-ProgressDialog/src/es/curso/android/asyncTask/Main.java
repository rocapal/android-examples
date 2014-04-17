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
import java.util.Timer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Main extends Activity {

	private final String TAG = getClass().getSimpleName();	
	public static Context mContext = null;
	
	private TextView tvTitle;
	private final Integer TIMER_TASK_DELAY = 1000;	

	private Timer mTimer = null;
	private MyTimerTask mTimerTask = null;
	
	private MyThread mThread = null;
	private MyThreadHandler mThreadHandler = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);       	
		
        mContext = this;            
        tvTitle = (TextView) this.findViewById(R.id.tvTitle);
        
        MyAsyncTask task = new MyAsyncTask();
        task.execute(null,null,null);
        
        setupWidgets ();
        
    }
    
    @Override
    protected void onResume() {
    	// TODO Auto-generated method stub
    	super.onResume();
    	
    	//
    	
    }
    
    private void setupWidgets ()
    {
        
    	// AsynTask
        Button btFile = (Button) this.findViewById(R.id.btFile);
        btFile.setOnClickListener(new OnClickListener() { 
			
			@Override
			public void onClick(View v) {
				
				final DownloadFileTask task = new DownloadFileTask();				                
                try {
                	task.execute(new URI("https://codeload.github.com/rocapal/android-examples/zip/master"), null, null);
					
				} catch (URISyntaxException e) {
					Log.d(TAG, e.getMessage());
				}
			}
		});
        
        // ProgressDialog Test
        Button btTest = (Button) this.findViewById(R.id.btTest);
        btTest.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				MyAsyncTask2 task2 = new MyAsyncTask2();
		        task2.execute(null,null,null);
				
			}
		});
        
        // TimerTask
        Button btTimerTask = (Button) this.findViewById(R.id.btTimerTask);
        btTimerTask.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TimerTask				
				
				mTimer= new Timer();
				
				mTimerTask = new MyTimerTask(mContext);				
				
				mTimer.schedule(mTimerTask, TIMER_TASK_DELAY, 
											MyTimerTask.TIMER_TASK_PERIOD);
			}
			
			
		});
        
        // Thread
        Button btThread = (Button) this.findViewById(R.id.btThread);
        btThread.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				mThread = new MyThread();
				mThread.setContext(mContext);
				mThread.run();				
			}
		});     
        
        
        Button btThreadHandler = (Button) this.findViewById(R.id.btThreadHandler);
        btThreadHandler.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View v) {				
		        
		        //Create thread and send handle
		        mThreadHandler = new MyThreadHandler();
		        mThreadHandler.setHandler(myHandler);
		        mThreadHandler.run();
			}
		});                
    }             
    
    
    
    
    // Handler
    Handler myHandler = new Handler(new Callback() {
		
		@Override
		public boolean handleMessage(Message msg) {
			
			Toast.makeText(mContext, 
					getString(R.string.toast_msg_thread_handler, 
							msg.what), 
							Toast.LENGTH_SHORT).show();
			return true;
		}
	});
    
    
       
     
    @Override
    protected void onPause() {    	
    	super.onPause();
    	
    	// Stop TimerTask
    	if (mTimerTask != null)
    		mTimerTask.cancel();
    	
    	if (mTimer != null)
    		mTimer.purge();
    		
    	// Stop Thread
    	if (mThread != null)
    	{
    		mThread.shouldContinue = false;
    		try {
				mThread.join();
			} catch (InterruptedException e) {
				Log.d(TAG, e.getMessage());
			}
    	}
    	
    	// Stop Thread Handler
    	if (mThreadHandler != null)
    	{
    		mThreadHandler.shouldContinue = false;
    		try {
    			mThreadHandler.join();
			} catch (InterruptedException e) {
				Log.d(TAG, e.getMessage());
			}
    	}
    }
    
    
    

    
    // This asyncTasks is an example of use. Just it shows de progress dialog
    // by 3 seconds and then close the progress dialog.
    public class MyAsyncTask extends AsyncTask<Void, Void, Void>{

    	ProgressDialog pd = null;
    	
    	protected void onPreExecute()
    	{
    		pd = ProgressDialog.show(mContext, "AsyncTask Example", 
    											"Initializing ...", true, true);
    		
    		
    		    	
    		
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
    
    
    public class MyAsyncTask2 extends AsyncTask<Void, Void, Void>{

    	ProgressDialog pd = null;
    	
    	protected void onPreExecute()
    	{
    		
    		tvTitle.setText("Descargando fichero ...");
    		pd = ProgressDialog.show(Main.this, "AsyncTask Example", "Initializing ...", true, true);
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
    		
    		
    		// You can interact with the UI using runOnUiThread
    		Main.this.runOnUiThread( new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					if (pd.isShowing())
		    			pd.dismiss();   
					
				}
			});
    		
    		return null;
    	}

    	
    	@Override
        protected void onPostExecute(Void unused)
        {
    		tvTitle.setText("Fichero Descargado");
    		 
        }

    }


}
