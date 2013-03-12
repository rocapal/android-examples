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


package es.libresoft.clua.apps.AudioVideo;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.content.ContextWrapper;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Environment;
import android.util.Log;

public class AudioManager extends ContextWrapper {
	 
	private String mFileName = "tmpaudio.3gpp";
	
	private static final String AUDIO_DIR = Environment.getExternalStorageDirectory() +  "/my_audio/";
	
	private MediaRecorder mRecorder;
	
	private MediaPlayer mPlayer;
	
	
	
	public AudioManager (Context base) {
		super(base);	
		
		mRecorder = new MediaRecorder();
		mPlayer = new MediaPlayer();
		
		File dirHandler = new File(AUDIO_DIR); 
		
		if (!dirHandler.exists())
			dirHandler.mkdir();
		
		
		
	}

	public void setName (String name)
	{
		mFileName = name + ".3gpp";
	}
	
	public String getPath ()
	{
		return AUDIO_DIR + mFileName;
	}
	
	public boolean saveAudio (InputStream is, String file)
	{
		
		try {
			
			setName(file);
			
			DataOutputStream out = new DataOutputStream(
										new BufferedOutputStream(
												new FileOutputStream( getPath() )));
			
			int c;
			while((c = is.read()) != -1) {
				out.writeByte(c);
			}
			is.close();
			out.close();
			
			
		} catch (FileNotFoundException e) {
			Log.e("AudioManager: ", e.getMessage());
		}catch(IOException e) {
			Log.e("AudioManager: ", e.getMessage());
		}
		
		return true;		
	}
	
	public void startRecording ()
	{		
		
		// TODO: Set Media Volume
		
		mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		mRecorder.setOutputFile(AUDIO_DIR + mFileName);
		
		Log.e("AudioManager","Starting recording " + mFileName);
		
	    try {
	    	 
	    	mRecorder.prepare();
	    	
		} catch (IllegalStateException e) {
			Log.e("AudioManager",e.getMessage());
			
		} catch (IOException e) {
			Log.e("AudioManager",e.getMessage());
		}
			
		mRecorder.start();
		
	}
	
	public int getMaxAmplitude(){
		return mRecorder.getMaxAmplitude();
	}
	
	public void stopRecording ()
	{
		if(mRecorder != null){
			try{
				mRecorder.stop();
			}catch(Exception e){
				Log.e("AudioManager", e.toString());
			}
			mRecorder.release();
			mRecorder = null;
		}
        
	}
	
	
	public void startPlayer() 
	{
		try {			
			
			mPlayer.setDataSource(AUDIO_DIR + mFileName);
			mPlayer.prepare();
			
		} catch (IllegalArgumentException e) {
			Log.e("AudioManager",e.getMessage());
			
		} catch (IllegalStateException e) {
			Log.e("AudioManager",e.getMessage());
			
		} catch (IOException e) {
			Log.e("AudioManager",e.getMessage());
		}
		
		mPlayer.start();
	}
	
	public void stopPlayer()
	{		
		try{
			mPlayer.stop();
		}catch(Exception e){
			Log.e("AudioManager", e.toString());
		}
		mPlayer.release();
	}
	
	public void setOnCompletionListener(OnCompletionListener listener){
		mPlayer.setOnCompletionListener(listener);
	}
	
	public boolean isPlaying(){
		return mPlayer.isPlaying();
	}
	
	public double getProgress(int offset){
		return (((double)mPlayer.getCurrentPosition() + offset)/((double)mPlayer.getDuration()));
	}
	
}
