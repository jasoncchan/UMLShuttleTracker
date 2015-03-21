package com.DC2.UMLBusApp;



import com.DC2.UMLBusApp.UI.BusSchedule;
import com.DC2.UMLBusApp.UI.DisplayUMLMap;
import com.DC2.UMLBusApp.UI.About;
import com.DC2.UMLBusApp.UI.SettingsMenu;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class UMLBusActivity extends ListActivity {

	String[] mainMenuItems;
	
    /** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	  super.onCreate(savedInstanceState);

	  mainMenuItems = getResources().getStringArray(R.array.mainMenu_array);
	  setListAdapter(new ArrayAdapter<String>(this, R.layout.main, mainMenuItems));

	  ListView lv = getListView();
	  lv.setTextFilterEnabled(true);

	  lv.setOnItemClickListener(new OnItemClickListener() {
	    public void onItemClick(AdapterView<?> parent, View view,
	        int position, long id) {

	    	String itemNameClicked = (String) ((TextView) view).getText();
	    	if(itemNameClicked.equalsIgnoreCase(mainMenuItems[0]))
	    	{
		    	 Intent myIntent = new Intent(view.getContext(), DisplayUMLMap.class);
	             startActivityForResult(myIntent, 0);
	    	}
	    	else if (itemNameClicked.equalsIgnoreCase(mainMenuItems[1]))
	    	{
		    	 Intent myIntent = new Intent(view.getContext(), BusSchedule.class);
	             startActivityForResult(myIntent, 0);
	    	}
	    	else if (itemNameClicked.equalsIgnoreCase(mainMenuItems[2]))
	    	{
		    	 Intent myIntent = new Intent(view.getContext(), SettingsMenu.class);
	             startActivityForResult(myIntent, 0);
	    	}	    	
	    	else if (itemNameClicked.equalsIgnoreCase(mainMenuItems[3]))
	    	{
		    	 Intent myIntent = new Intent(view.getContext(), About.class);
	             startActivityForResult(myIntent, 0);
	    	}
	    }


	  });
	}



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
        }
        return super.onKeyDown(keyCode, event);
    }

    public void onStart(Bundle savedInstanceState) {
        //locationManager.removeUpdates(locationListener);
        //locationManager = null;
    }
    
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
      super.onRestoreInstanceState(savedInstanceState);
    }

    public void onPause(Bundle savedInstanceState) {
        //locationManager.removeUpdates(locationListener);
        //locationManager = null;
    }

    public void onDestroy(Bundle savedInstanceState) {
        //mapHandler.getLooper().quit();
        //super.onDestroy();
    }

    public void onStop(Bundle savedInstanceState) {
    	//mapHandler.getLooper().quit();
        //super.onStop();
    }



} 