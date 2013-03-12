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

import java.sql.Time;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

public class Main extends Activity {
    /** Called when the activity is first created. */
	
	private volatile Thread th;
	private Activity mContext;

	private String time_str = "00:00";
	private String name = "audio";
	
	private static AudioManager aManager;
	private ProgressBar pb;
	private TextView time;
	private VideoView mVideoView;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_audio);
        
        mContext = this;
        
        time = (TextView) findViewById (R.id.record_tv);
		pb = (ProgressBar) findViewById (R.id.record_pb);
		pb.setMax(10000);
		
        ImageButton bt_record = (ImageButton) findViewById (R.id.record_button_record);
		ImageButton bt_stop = (ImageButton) findViewById (R.id.record_button_stop);
		ImageButton bt_play = (ImageButton) findViewById (R.id.record_button_play);
		
		
		bt_record.setOnClickListener(recordListener);
		bt_stop.setOnClickListener(stopListener);
		bt_play.setOnClickListener(playListener);
		
	    mVideoView = (VideoView) findViewById(R.id.vv);
	    
	    mVideoView.setVideoURI( Uri.parse("http://dl3.streamzilla.jet-stream.nl/jet-stream/progressivedemos/goldfish_H264_Web.mp4"));
        mVideoView.setMediaController(new MediaController(this));
        mVideoView.requestFocus();
        
        mVideoView.start();
    }
    
    
	final Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			if(th!=null){
				int progress = aManager.getMaxAmplitude();
				time.setText(time_str);
				if(progress > pb.getMax())
					progress = pb.getMax();
				pb.setProgress(progress);
			}
		}
	};
	
	final Handler playHandler = new Handler(){
		public void handleMessage(Message msg) {
			if(th!=null){
				int progress = (int)(aManager.getProgress(250) * pb.getMax());
				time.setText(time_str);
				if(progress > pb.getMax())
					progress = pb.getMax();
				pb.setProgress(progress);
			}
		}
	};
	
	final OnCompletionListener onCompletionListener = new OnCompletionListener() {
		
		@Override
		public void onCompletion(MediaPlayer mp) {
			th = null;
			if(aManager != null)
				aManager.stopPlayer();
		}
	};
	
	
    private OnClickListener playListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if((name == null) || (th != null))
				return;
			aManager = new AudioManager(mContext);
			aManager.setName(name);
			aManager.setOnCompletionListener(onCompletionListener);
			aManager.startPlayer();
			
			th = new Thread(){
				public void run(){
					int counter = 0;
					long clock = 0;
					while(Thread.currentThread().equals(th)){
						try {
							Thread.sleep(250);
							counter += 250;
							if(counter >= 1000){
								counter = 0;
								clock ++;
							}
							time_str = calculateTime(clock);
							playHandler.sendEmptyMessage(0);
							
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							Log.e("ContentCreation", "", e);
						}
					}

				}
			};

			th.start();
		}
	};
	
	private OnClickListener stopListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			th = null;
			if(aManager != null){
				aManager.stopRecording();
				aManager.stopPlayer();
			}
		}
	};
	
	private OnClickListener recordListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if((name == null) || (th != null))
				return;
			
			aManager = new AudioManager(mContext);
			aManager.setName(name);
			aManager.startRecording();
			
			//rt = new recordTask();
			//rt.execute();
			
			
			th = new Thread(){
				public void run(){
					
					int counter = 0;
					long clock = 0;
					
					while(Thread.currentThread().equals(th)){
					
						try {
							Thread.sleep(100);
							counter += 100;
						
							if(counter >= 1000){
								counter = 0;
								clock ++;
							}
							
							time_str = calculateTime(clock);
							mHandler.sendEmptyMessage(0);
							
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							Log.e("ContentCreation", "", e);
						}
					}

				}
			};

			th.start();
			
			
		}
	};
	
	
	
	private String calculateTime(long time){
		Time tme = new Time( time * 1000);
		String minutes = Integer.toString(tme.getMinutes());
		
		if(tme.getMinutes() < 10)
			minutes = "0" + minutes;
		
		String seconds = Integer.toString(tme.getSeconds());
		
		if(tme.getSeconds() < 10)
			seconds = "0" + seconds;
		
		return minutes + ":" + seconds;
	}
}