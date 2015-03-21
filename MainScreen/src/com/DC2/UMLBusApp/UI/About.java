package com.DC2.UMLBusApp.UI;

import com.DC2.UMLBusApp.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class About extends Activity {
	private TextView msg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		
		msg = (TextView) findViewById(R.id.msg);
		msg.setText("Version 0.1 User Location live update\n"
		 		+ "Version 0.2 Add Menus\n"
		 		+ "Version 0.3 Add Bus Stop Location\n"
		 		+ "Version 0.4 HTTP Looper/Handler Refactor\n"
		 		+ "Version 0.5 Add Vibration and Sound Notification\n"
		 		+ "Version 0.6 Added Settings Menu\n"
		 		+ "Version 0.7 Added Schedule\n"
		 		+ "Version 0.8 Re-overlayed menus\n"
		 		+ "Version 0.9 Bug Fixes\n"
		 		+ "\n\n\n\n\n Developers:\n"
		 		+ "\tAmanda Liu, Eason Lin, Jason Chan\n");
	}
	
	

}
