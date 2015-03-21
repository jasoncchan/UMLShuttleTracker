package com.DC2.UMLBusApp;

import com.DC2.UMLBusApp.UI.About;
import com.DC2.UMLBusApp.UI.BusSchedule;
import com.DC2.UMLBusApp.UI.DisplayUMLMap;
import com.DC2.UMLBusApp.UI.SettingsMenu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainScreen extends Activity {
	
	private Button busScheduleBtn;
    private Button busLocationBtn;
    private Button settingBtn;
    private Button aboutBtn;
    private Intent newActivity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_screen);
		
		busLocationBtn = (Button) findViewById(R.id.BusLocation);
		busScheduleBtn = (Button) findViewById(R.id.BusSchedule);
		settingBtn = (Button) findViewById(R.id.Setting);
		aboutBtn = (Button) findViewById(R.id.About);
		
		busLocationBtn.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showMap(1);
			}
		});
	     
		busScheduleBtn.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showMap(2);
			}
		});
		
		settingBtn.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showMap(3);
			}
		});
		
		aboutBtn.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showMap(4);
			}
		});
	}
	
	
	public void showMap(int arg){
		
		switch (arg){
			case 1: {
				newActivity = new Intent(this, DisplayUMLMap.class);
				newActivity.putExtra("notify", false);
				startActivityForResult(newActivity, 0);
				break;
			}
			case 2: {
				newActivity = new Intent(this, BusSchedule.class);
				startActivityForResult(newActivity, 0);
				break;
			}
			case 3: {
				newActivity = new Intent(this,  SettingsMenu.class);
				newActivity.putExtra("Option", 3);
				startActivityForResult(newActivity, 0);
				break;
			}
			case 4: {
				newActivity = new Intent(this, About.class);
				startActivityForResult(newActivity, 0);
				break;
			}
			default: break;
		
		}
	}
	
}
