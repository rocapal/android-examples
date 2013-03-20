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

package es.curso.android.tts;

import java.util.ArrayList;
import java.util.Locale;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements TextToSpeech.OnInitListener {

	private EditText etTTS;
	private TextToSpeech mTTS;
	private Button btTTS, btVoice;
	private TextView tvResults;
	
	protected static final int RESULT_SPEECH = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mTTS = new TextToSpeech(this, this);
		
		setupWidgets();
	}
	
	@Override
	protected void onDestroy() {
		
		if (mTTS != null)
		{
			mTTS.stop();
			mTTS.shutdown();			
		}					
		
		super.onDestroy();
				
	}

	
	private void setupWidgets()
	{
		
		tvResults = (TextView) this.findViewById(R.id.tvResults);
		etTTS = (EditText) this.findViewById(R.id.etText);
		
		btTTS = (Button) this.findViewById(R.id.btTTS);
		btTTS.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				String text = etTTS.getText().toString();				 
		        mTTS.speak(text, TextToSpeech.QUEUE_FLUSH, null);
				
			}
		});		
		
		btVoice = (Button) this.findViewById(R.id.btVoice);
		
		btVoice.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "es-ES");

                try {
                	
                	tvResults.setText("");
                    startActivityForResult(intent, RESULT_SPEECH);
                   
                } catch (ActivityNotFoundException a) {
                	
                	
                    Toast t = Toast.makeText(getApplicationContext(),
                            "Opps! Your device doesn't support Speech to Text",
                            Toast.LENGTH_SHORT);
                    t.show();
                }
				
			}
		});
		
		
	}


	@Override
	public void onInit(int status) {
		
		if (status == TextToSpeech.SUCCESS) {
			 
            int result = mTTS.setLanguage(new Locale("spa"));
 
            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
            	                
                Toast.makeText(this, "TTS: Language is not supported", Toast.LENGTH_SHORT).show();
                
            } 
            else
            {
            	Toast.makeText(this, "TTS: Initilization OK!", Toast.LENGTH_SHORT).show();
            	btTTS.setEnabled(true);
            }
 
        } else {
            Log.d("TTS", "Initilization Failed!");
            Toast.makeText(this, "TTS: Initilization Failed!", Toast.LENGTH_SHORT).show();
        }
		
	}
	
	private void showResults (ArrayList<String> results)
	{
		for (String sentence : results) 			
			tvResults.setText( tvResults.getText() + sentence + "\n");
		
	}
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
 
        switch (requestCode) {
        case RESULT_SPEECH: {
            if (resultCode == RESULT_OK && null != data) {
 
                ArrayList<String> results = data
                        .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
 
                
                showResults(results);
            }
            break;
        }
 
        }
    }

}
