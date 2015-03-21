package com.DC2.UMLBusApp.UI;

import com.DC2.UMLBusApp.R;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;



/**
 * Demonstrates how to use a seek bar
 */
public class SettingsMenu extends Activity implements SeekBar.OnSeekBarChangeListener {

    SeekBar mSeekBar;
    TextView mProgressText;
    TextView mTrackingText;
    TextView info;
    public static final String PERSISTENT_SETTINGS = "UMLBusTrackerPreferences.dat";
    int frequency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.settingsmenu);

        mSeekBar = (SeekBar)findViewById(R.id.updateFreqency);

        
        mProgressText = (TextView)findViewById(R.id.updateValue);
        info = (TextView)findViewById(R.id.msg);  
        
        SharedPreferences settings = getSharedPreferences(PERSISTENT_SETTINGS, 0);
		frequency = settings.getInt("updateFrequency", 50);
		
		if(frequency == 50)
		{
			info.setTextSize(18);
			info.setText("Please slide the bar to set how frequently you'd like the " +
        		"bus to update it's position.");
		}
		else
		{
			info.setTextSize(30);
			info.setText("Currently updating every " + (frequency * 100) + " milliseconds");
		}
			
        
		
		mSeekBar.setProgress(frequency);
		mSeekBar.setOnSeekBarChangeListener(this);
        
    }

    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {
        //mProgressText.setText(getString(R.string.seekbar_from_touch) + " :" + progress);
    	info.setText("UpdateFrequency:");
    	info.setTextSize(30);
    	mProgressText.setText("" + progress);
    	
    }    
    
    
    public void onStartTrackingTouch(SeekBar seekBar) {
        //mTrackingText.setText(getString(R.string.seekbar_tracking_on));
    }

    public void onStopTrackingTouch(SeekBar seekBar) {
    	
    	

        SharedPreferences settings = getSharedPreferences(PERSISTENT_SETTINGS, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("updateFrequency", seekBar.getProgress());
        editor.commit();    	
    	
		frequency = settings.getInt("updateFrequency", 30);
        
		if(frequency == 50)
		{
			info.setTextSize(18);
			info.setText("Please slide the bar to set how frequently you'd like the " +
        		"bus to update it's position.");
		}
		else
		{
			info.setTextSize(30);
			info.setText("Currently updating every " + (frequency * 100) + " milliseconds");
			mProgressText.setText("");
		}
        
        
    	Context context = getApplicationContext();
    	CharSequence text = "Settings Saved";
    	int duration = Toast.LENGTH_SHORT;
    	Toast toast = Toast.makeText(context, text, duration);toast.show();
    	
    }
}