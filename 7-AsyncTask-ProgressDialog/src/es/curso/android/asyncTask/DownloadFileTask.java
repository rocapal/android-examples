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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

public class DownloadFileTask extends AsyncTask<URI, Integer, Integer>{

	ProgressDialog pd = null;
	private final String TAG = getClass().getSimpleName();
	
	private String urlFile, file;
	
	protected void onPreExecute()
	{
		// Show progressDialog
		pd = new ProgressDialog(Main.mContext);
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pd.setTitle("ProgressDialog");
		pd.setMessage("Download file ...");
		pd.show();
	}
	
	@Override
	protected Integer doInBackground(final URI... uris) {
		
		urlFile = uris[0].toString();	
		Log.d(TAG, urlFile);			
		
		
		
		try {
			file = downloadFile(new URL(urlFile),
			Environment.getExternalStorageDirectory().toString());
		} catch (MalformedURLException e) {
			Log.e(TAG, e.getMessage());
			return -1;
		} catch (IOException e) {
			return -1;				
		}
		
		return 1;
	}

	@Override
	protected void onProgressUpdate(final Integer... progress) {
        pd.setProgress(progress[0]);
     
    }
	
	@Override
    protected void onPostExecute(Integer result)
    {
    	pd.dismiss();
    	
    	
    	if (result == 1)
    		Toast.makeText(Main.mContext, urlFile + " downloaded correctly in " + file , Toast.LENGTH_LONG).show();    	
    	else
    		Toast.makeText(Main.mContext, "Download error file: " +  urlFile  , Toast.LENGTH_LONG).show();
    		
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
		publishProgress(0);

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
			// publishing the progress....                      
			publishProgress((int) (total * 100 / fileLength));
			output.write(data, 0, count);
		}

		output.flush();
		output.close();
		input.close();

		Log.d(TAG, "Finished download on: " + destination.toString());

		return destination.toString();

	}
}
