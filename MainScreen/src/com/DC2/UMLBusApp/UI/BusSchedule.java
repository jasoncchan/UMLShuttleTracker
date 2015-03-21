package com.DC2.UMLBusApp.UI;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.DC2.UMLBusApp.R;

public class BusSchedule extends Activity{

	private TextView msg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.schedule);
		
		String s;
		s = "\n1001 Express: 1001 Pawtucket Blvd. to North Campus (7 a.m. - 6:30 p.m) Ending May 9, 2011\n\n\n" +
				"Blue Line: North, School Street, and South\n" +
				"\tDeparts every 10 minutes:\n" +
				"\t\tNorth on the 10's\n" +
				"\t\tSouth on the 5's\n\n\n" +
				"Red Line: East, Salem Parking Lot and South\n" +
				"\tDeparts every 15 minutes from North & South \n\n\n" +
				"Green North: East Meadow Lane, Pleasant St., and North\n\n\n" +
				"Green South: East Meadow Lane, Moody Street, Salem St. and South\n\n\n" +				
				"Yellow South: Inn & Conference Center and South - departs every 15 minutes\n\n\n" +
				"Orange Line: East, Salem Street, North, Wannalancit Mills, and Aiken Street\n\n\n";		
		
		msg = (TextView) findViewById(R.id.msg);
		msg.setText(s);
		
		
		
		
	}
}
