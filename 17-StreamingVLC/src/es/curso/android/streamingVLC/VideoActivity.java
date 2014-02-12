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
 *  Code based in: http://gitorious.org/libvlc-android-sample/ 
 */

package es.curso.android.streamingVLC;

import java.io.IOException;
import java.lang.ref.WeakReference;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.videolan.libvlc.EventHandler;
import org.videolan.libvlc.IVideoPlayer;
import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.LibVlcException;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.Toast;

public class VideoActivity extends Activity implements SurfaceHolder.Callback, 
													   IVideoPlayer {
	
    public final static String TAG = "streamingVLC/VideoActivity";

    private String mFilePath;

    private Button btStart, btStop;
 
    private SurfaceView mSurface;
    private SurfaceHolder holder;

    private LibVLC libvlc;
    private int mVideoWidth;
    private int mVideoHeight;
    private final static int VideoSizeChanged = -1;
    private static Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample);
               
        //mFilePath = Environment.getExternalStorageDirectory().getPath() + "/DCIM/Camera/VID_20131226_195102.mp4";
        //mFilePath = LibVLC.PathToURI("http://10.15.181.28:8090");  
        
        mContext = this;
             
        
        btStart = (Button) this.findViewById(R.id.btStart);
        btStop = (Button) this.findViewById(R.id.btStop);
        
        btStart.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				MyTask task = new MyTask();
				task.execute();
				
			}
		});
        
        btStop.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				stopStreaming();
				releasePlayer();
			}
		});
        
        Log.d(TAG, "Playing back " + mFilePath);

        mSurface = (SurfaceView) findViewById(R.id.surface);
        
        holder = mSurface.getHolder();
        holder.addCallback(this);
        
        createPlayer("tcp://YOUR_IP:8888/");
      
    }
    
    private void stopStreaming()
    {
    	Thread th = new Thread(new Runnable() {

    		@Override
    		public void run() {

    			doGetPetition("http://10.15.181.28:8000/streaming/stop/");

    		}
    	});

    	th.start();

    	try {
    		th.join(3000);
    	} catch (InterruptedException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}

		
    }

    public class MyTask extends AsyncTask<Void, Void, Void>
    {
    	ProgressDialog pd;
    	String mStreamingUrl;
    	
    	@Override
    	protected void onPreExecute() {
    		// TODO Auto-generated method stub
    		super.onPreExecute();
    		
    		pd = ProgressDialog.show(mContext, "Start Streaming", "Start Streaming");
    	}

		@Override
		protected Void doInBackground(Void... params) {
			
			String jsonStr = doGetPetition("http://10.15.181.28:8000/streaming/start/");
			try {
				JSONObject json = new JSONObject(jsonStr);
				if (json.has("streaming_url"))
					mStreamingUrl = json.getString("streaming_url");
				
				Thread.sleep(6000);
				
			} catch (JSONException e) {				
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
    	
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			if (pd.isShowing())
				pd.cancel();
			
			createPlayer(LibVLC.PathToURI(mStreamingUrl));
			
		}
    }
    
    
    private String doGetPetition (String url)
	{
		
		try
		{
			DefaultHttpClient httpclient = new DefaultHttpClient();
			HttpGet httpGet = null;

			httpGet = new HttpGet(url);
			HttpResponse response = httpclient.execute(httpGet);
			HttpEntity entity = response.getEntity();

			String str = EntityUtils.toString(entity);
			
			return str;

		}catch (IOException e) {
			Log.e("doGet",e.getMessage());
			return null;
		}	
	}			
    
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setSize(mVideoWidth, mVideoHeight);
    }

    @Override
    protected void onResume() {
        super.onResume();            
    }

    @Override
    protected void onPause() {
        super.onPause();
        
        stopStreaming();
        destroyPlayer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releasePlayer(); 
    }
    
    /*************
     * Surface
     *************/

    public void surfaceCreated(SurfaceHolder holder) {
    }

    public void surfaceChanged(SurfaceHolder surfaceholder, int format,
            int width, int height) {
        if (libvlc != null)
            libvlc.attachSurface(holder.getSurface(), this);
    }

    public void surfaceDestroyed(SurfaceHolder surfaceholder) {
    }

    private void setSize(int width, int height) {
        mVideoWidth = width;
        mVideoHeight = height;
        if (mVideoWidth * mVideoHeight <= 1)
            return;

        // get screen size
        int w = getWindow().getDecorView().getWidth();
        int h = getWindow().getDecorView().getHeight();

        // getWindow().getDecorView() doesn't always take orientation into
        // account, we have to correct the values
        boolean isPortrait = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
        if (w > h && isPortrait || w < h && !isPortrait) {
            int i = w;
            w = h;
            h = i;
        }

        float videoAR = (float) mVideoWidth / (float) mVideoHeight;
        float screenAR = (float) w / (float) h;

        if (screenAR < videoAR)
            h = (int) (w / videoAR);
        else
            w = (int) (h * videoAR);

        // force surface buffer size
        holder.setFixedSize(mVideoWidth, mVideoHeight);

        // set display size
        LayoutParams lp = mSurface.getLayoutParams();
        lp.width = w;
        lp.height = h;
        mSurface.setLayoutParams(lp);
        mSurface.invalidate();
    }

    @Override
    public void setSurfaceSize(int width, int height, int visible_width,
            int visible_height, int sar_num, int sar_den) {
        Message msg = Message.obtain(mHandler, VideoSizeChanged, width, height);
        msg.sendToTarget();
    }

    /*************
     * Player
     *************/

    private void createPlayer(String media) {
        releasePlayer();
        

        if (media.length() > 0) {
        	Toast toast = Toast.makeText(this, media, Toast.LENGTH_LONG);
        	toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0,
        			0);
        	toast.show();
        }

        // Create a new media player
        try {
        	libvlc = LibVLC.getInstance();
        } catch (LibVlcException e) {
        	Toast.makeText(this, "Can't create player", Toast.LENGTH_LONG).show();
        	return;
        }
        libvlc.setIomx(false);
        libvlc.setSubtitlesEncoding("");
        libvlc.setAout(LibVLC.AOUT_OPENSLES);
        libvlc.setTimeStretching(true);
        libvlc.setChroma("RV32");
        libvlc.setVerboseMode(true);
        LibVLC.restart(this);
        EventHandler.getInstance().addHandler(mHandler);
        if (holder == null)
        	Log.d(TAG, "holder==null");
        holder.setFormat(PixelFormat.RGBX_8888);
        holder.setKeepScreenOn(true);
        MediaList list = libvlc.getMediaList();
        list.clear();
        list.add(new Media(libvlc, LibVLC.PathToURI(media)), false);
        libvlc.playIndex(0);

    }

    private void releasePlayer() {
        if (libvlc == null)
            return;
        
        EventHandler.getInstance().removeHandler(mHandler);
        libvlc.stop();
        
    }
    
    private void destroyPlayer()
    {
    	if (libvlc == null)
            return;
        
        EventHandler.getInstance().removeHandler(mHandler);
        libvlc.stop();
                
        libvlc.detachSurface();
        holder = null;
        libvlc.closeAout();
        libvlc.destroy();
        libvlc = null;

        mVideoWidth = 0;
        mVideoHeight = 0;
       
    }
    

    /*************
     * Events
     *************/

    private Handler mHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        private WeakReference<VideoActivity> mOwner;

        public MyHandler(VideoActivity owner) {
            mOwner = new WeakReference<VideoActivity>(owner);
        }

        @Override
        public void handleMessage(Message msg) {
        	VideoActivity player = mOwner.get();

        	// SamplePlayer events
        	if (msg.what == VideoSizeChanged) {
        		player.setSize(msg.arg1, msg.arg2);
        		return;
        	}

        	// Libvlc events
        	Bundle b = msg.getData();
        	switch (b.getInt("event")) {
        	
        	case EventHandler.MediaPlayerEndReached:
        		player.releasePlayer();
        		break;
        		
        	case EventHandler.MediaPlayerPlaying:        	
        	case EventHandler.MediaPlayerPaused:
        	case EventHandler.MediaPlayerStopped:
        	default:
        		break;
        	}
        }
    }
}
