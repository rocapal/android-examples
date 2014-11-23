package es.curso.androidProgressDialog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        MyTask task = new MyTask();
        task.execute("http://developer.android.com/images/home/l-hero_2x.png",
				 "https://developer.android.com/design/media/creative_vision_main.png",
				 "https://developer.android.com/design/material/images/MaterialDark.png");
        
    }
    
    
    public class MyTask extends AsyncTask<String, Integer, Void>
    {

    	private ProgressDialog mPd;
    	private final String TAG = getClass().getSimpleName();
    	private Integer mFiles = 3;
    	private String[] mFilesNames;
    	
    	@Override
    	protected void onPreExecute() {
    		// TODO Auto-generated method stub
    		super.onPreExecute();
    		
    		//mPd = ProgressDialog.show(MainActivity.this, "title", "message");
    		
    		mPd = new ProgressDialog(MainActivity.this);
			mPd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			mPd.setTitle("ProgressDialog");
			mPd.setMessage("Download file ...");
			mPd.show();
    	}
    	
		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
			
			mFilesNames = params;
			
			for (int i=0; i<mFilesNames.length; i++)
			{
				publishProgress(i);
				
				try {
					downloadFile(new URL(mFilesNames[i]), 
							Environment.getExternalStorageDirectory().toString());
					
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					Log.e(TAG, e.getMessage());
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			return null;
		}
		
		@Override
		protected void onProgressUpdate(Integer... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
			
			String[] name = mFilesNames[values[0]].split("/");
			
			//mPd.setMessage("Downloading file #" + String.valueOf(values[0]) + 
			//		" Name= " + name[name.length-1]);
			
			mPd.setMessage(getApplicationContext().getResources().getString(
					R.string.pd_message, 
					values[0],
					name[name.length-1]));
			
			mPd.setProgress(( (values[0]+1)*100/mFiles ));
		}
		
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			if (mPd.isShowing())
				mPd.dismiss();
		}
		
		/**
	     * Download the file in fileSystem
	     * 
	     * @param destUrl The uri to get/download the file
	     * @param directory The base path where save the file
	     * @return String The filename path where it has been saved
	     */
		private String downloadFile (final URL destUrl, final String directory) throws IOException
		{				
			Log.d("downloadFile", "downloadFile(" + destUrl.toString() + ", " + directory);
			

			final  String fileName = destUrl.toString().substring(destUrl.toString().lastIndexOf("/") + 1);

			// This will be useful so that you can show a typical 0-100% progress bar
			final URLConnection connection = destUrl.openConnection();
			connection.connect();
			final int fileLength = connection.getContentLength();

			// Download the file
			final File destinationFolder = new File(directory);
			if (!destinationFolder.exists()) {
				destinationFolder.mkdirs();
			}

			final File destination = new File(destinationFolder, fileName);
			if (destination.exists())
			{
				Log.d(TAG,"File already exists! Remove it");
				destination.delete();
			}

			Log.d(TAG, "Starting download on: " + destination.toString());

			final InputStream input = new java.io.BufferedInputStream(connection.getInputStream());
			OutputStream output = null;
			try {
				output = new FileOutputStream(destination);
			} catch (IOException ex) {
				Log.e(TAG, "Error opening destination file: " + ex.getMessage());
				return null;
			}

			final byte data[] = new byte[4096];
			long total = 0;
			int count;
			while ((count = input.read(data)) != -1) {
				total += count;				
				output.write(data, 0, count);
			}

			output.flush();
			output.close();
			input.close();

			Log.d(TAG, "Finished download on: " + destination.toString());

			return destination.toString();

		}
		
		
	}

    

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
