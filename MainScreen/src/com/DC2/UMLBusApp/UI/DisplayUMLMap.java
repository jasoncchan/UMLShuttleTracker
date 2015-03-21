package com.DC2.UMLBusApp.UI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.DC2.UMLBusApp.MainScreen;
import com.DC2.UMLBusApp.R;
import com.DC2.UMLBusApp.BusinessObjects.BusStop;
import com.DC2.UMLBusApp.BusinessObjects.UMLBusItemizedOverlay;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

public class DisplayUMLMap extends MapActivity {

	private LocationManager locationManager = null;
	private LocationListener locationListener = null;
	static UMLBusItemizedOverlay studentItemizedoverlay = null;
	static UMLBusItemizedOverlay busItemizedoverlay = null;
	static UMLBusItemizedOverlay busStopItemizedoverlay = null;

	private static double studentLocationLat;
	private static double studentLocationLon;
	private static double busLocationLat;
	private static double busLocationLon;
	private GeoPoint centerOfCampus;
	private int updateFrequency = 3000;
	private static final int MY_ID = 99;

	private Handler mapHandler;
	private ArrayList<BusStop> busStops;
	
	private static boolean userflag;
	private static boolean stopflag;
	private static boolean show_notify;

	private enum menuChoices {
		MyLocation, BusStop, About
	}

	private MapView UMLmapView;
	
	private NotificationManager myNManager;
	private Notification myNotification;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.uml_map);
		
		//Set up the notification bar to notify the user when the bus is close by
		myNManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		myNotification = new Notification(R.drawable.bus, "Bus nearby!",
				System.currentTimeMillis());

		Context context = getApplicationContext();
		CharSequence contentTitle = "UML Shuttle Bus App";
		CharSequence contentText = "Bus nearby!";

		Intent notificationIntent = new Intent(this, MainScreen.class);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
		
		myNotification.setLatestEventInfo(context, contentTitle, contentText,
				contentIntent);
		
		myNotification.flags |= Notification.FLAG_AUTO_CANCEL;
		myNotification.flags |= Notification.DEFAULT_VIBRATE;
		
		myNotification.vibrate = new long[] {100, 250, 100, 500};
		
		myNotification.defaults |= Notification.DEFAULT_SOUND;
		myNotification.defaults |= Notification.DEFAULT_LIGHTS;

		// Restore preferences
		// SharedPreferences settings =
		// getSharedPreferences("PERSISTENT_SETTINGS", 0);
		// int frequency = settings.getInt("updateFrequency", 3);
		// updateFrequency = frequency * 1000; //time in milliseconds

		UMLmapView = (MapView) findViewById(R.id.mapView);
		busStops = populateBusStops();
		UMLmapView.setBuiltInZoomControls(true);
		//UMLmapView.setTraffic(true);

		// Start map off in center of UML North Campus
		MapController mapController = UMLmapView.getController();
		centerOfCampus = new GeoPoint((int) (42.65005868477507 * 1E6), (int) (-71.32650375366211 * 1E6));
		mapController.setZoom(15);
		mapController.animateTo(centerOfCampus);
		
		
		//Get the arguments pass from the MainScreen
		//First Extract the bundle from intent
		Bundle bundle = getIntent().getExtras();

		//Next extract the values using the key as
		show_notify = bundle.getBoolean("notify");

		// I have yet to understand why two threads are needed. If only 1 thread
		// kicks off, I can run this in the debugger
		// but I can not run it normally. If I start 2 threads, I can run
		// normally and in debug mode.
		new UpdateBusLocation().start();
		new UpdateBusLocation().start();
		mapHandler.sendEmptyMessage(1); // kick off the Update thread.

	}

	class UpdateBusLocation extends Thread {

		@Override
		public void run() {
			Looper.prepare();

			mapHandler = new Handler() {
				@Override
				public void handleMessage(Message msg) {

					while (true) {
						getBusCoordinates();
						
						List<Overlay> mapOverlays = UMLmapView.getOverlays();
						Drawable drawable = getResources().getDrawable(R.drawable.bus);
						if (busItemizedoverlay != null)
							mapOverlays.remove(busItemizedoverlay);
						busItemizedoverlay = new UMLBusItemizedOverlay(drawable, UMLmapView.getContext());

						// pawtucket blvd.
						// http://umlshuttletracker.info/saveLocation.php?Xcoord=42.649954&Ycoord=-71.325002&BusDriverUsername=Gandalf
						// http://umlshuttletracker.info/saveLocation.php?Xcoord=42.650058&Ycoord=-71.326503&BusDriverUsername=Gandalf

						GeoPoint busPoint = new GeoPoint((int) (busLocationLat * 1E6), (int) (busLocationLon * 1E6));
						OverlayItem busOverlay = new OverlayItem(busPoint, "Bus's Current Location", " ");
						busItemizedoverlay.addOverlay(busOverlay);
						mapOverlays.add(busItemizedoverlay);
						
						// loop through arraylist of busStops and find the closest bus stop
						for (BusStop bs : busStops) {
							float[] dis = new float[5];
							Location.distanceBetween(busLocationLat, busLocationLon, bs.getBusLat(), bs.getBusLon(), dis);
							if (dis[0] < 200) {
								if (stopflag != true ){
									Drawable busIcon = getResources().getDrawable(R.drawable.bustop);
									busStopItemizedoverlay = new UMLBusItemizedOverlay(busIcon, UMLmapView.getContext());
									GeoPoint tmpPoint = new GeoPoint((int) (bs.getBusLat() * 1E6), (int) (bs.getBusLon() * 1E6));
									OverlayItem tmpBusOverlay = new OverlayItem(tmpPoint, "The nearest bus stop is ", bs.getBusStopName());
									busStopItemizedoverlay.addOverlay(tmpBusOverlay);
									mapOverlays.add(busStopItemizedoverlay);
								}
								if (show_notify != true ){
									myNManager.notify(MY_ID, myNotification);
									show_notify = true;
								}
							}
						}
						
						UMLmapView.postInvalidate();

						try {
							Thread.sleep(updateFrequency);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			};

			Looper.loop();
		}
	}

	/**
	 * Generates an arraylist of BusStop Objects
	 * 
	 * @return ArrayList of BusStop Objects
	 */
	public ArrayList<BusStop> populateBusStops() {
		// tmp variables
		String[] tmp;

		// get resource
		ArrayList<BusStop> BusStops = new ArrayList<BusStop>();
		Resources res = getResources();
		String[] stops = res.getStringArray(R.array.busStops_array);

		// loop through and populate arraylist with busStop objects
		for (String s : stops) {
			tmp = s.split(";"); // tmp[] => busStopName, xCoord, yCoord
			BusStops.add(new BusStop(tmp[0].trim(), Double.parseDouble(tmp[1].trim()),
					Double.parseDouble(tmp[2].trim())));
		}
		return BusStops;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu);

		menu.setQwertyMode(true);

		MenuItem userLocation = menu.add(0, menuChoices.MyLocation.ordinal(), menuChoices.MyLocation.ordinal(),
				"My Location");
		userLocation.setAlphabeticShortcut('m');
		userLocation.setIcon(R.drawable.userlocation);

		MenuItem busStop = menu.add(0, menuChoices.BusStop.ordinal(), menuChoices.BusStop.ordinal(), "Bus Station");
		busStop.setAlphabeticShortcut('s');
		busStop.setIcon(R.drawable.bustop);

		MenuItem about = menu.add(0, menuChoices.About.ordinal(), menuChoices.About.ordinal(), "About");
		about.setAlphabeticShortcut('a');
		about.setIcon(R.drawable.about);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case 0: // User Current Location
			userflag = true;
			stopflag = false;
			displayStudentLocation();
			return true;
		case 1: // Bus Stop Locations
			stopflag = true;
			userflag = false;
			displayBusStopLocations();
			return true;
		case 2: // About
			showMessage();
			return true;

		}
		return super.onOptionsItemSelected(item);
	}

	public void showMessage() {
		Intent i = new Intent(this, About.class);
		startActivityForResult(i, 0);
	}

	public void displayStudentLocation() {

		// Acquire a reference to the system Location Manager
		locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		// Define a listener that responds to location updates
		locationListener = new LocationListener() {
			@Override
			public void onLocationChanged(Location location) {
				// Called when a new location is found by the network location
				// provider.
				studentLocationLat = location.getLatitude();
				studentLocationLon = location.getLongitude();
				List<Overlay> mapOverlays = UMLmapView.getOverlays();
				Drawable drawable = getResources().getDrawable(R.drawable.red);

				// tmpPoint is defaulted to the UniversityAve bus stop - used
				// for initial zoom in
				GeoPoint studentPoint = new GeoPoint((int) (studentLocationLat * 1E6), (int) (studentLocationLon * 1E6));
				
				String address = ConvertPointToLocation(studentPoint);

				if (address == "") {
					address = "Latitude: " + location.getLatitude()
							+ "\nLongitude: " + location.getLongitude();
				}

				OverlayItem studentOverlay = new OverlayItem(studentPoint, "Your current location", address);
				studentItemizedoverlay = new UMLBusItemizedOverlay(drawable, UMLmapView.getContext());
				studentItemizedoverlay.addOverlay(studentOverlay);
				mapOverlays.add(studentItemizedoverlay);

				MapController mapController = UMLmapView.getController();
				mapController.setCenter(studentPoint);
				mapController.setZoom(15);
				locationManager.removeUpdates(locationListener);
			}

			public String ConvertPointToLocation(GeoPoint point) {
				String address = "";
				Geocoder geoCoder = new Geocoder(getBaseContext(),
						Locale.getDefault());
				try {
					List<Address> addresses = geoCoder.getFromLocation(
							point.getLatitudeE6() / 1E6,
							point.getLongitudeE6() / 1E6, 1);

					if (addresses.size() > 0) {
						for (int index = 0; index < addresses.get(0)
								.getMaxAddressLineIndex(); index++)
							address += addresses.get(0).getAddressLine(index) + " ";
					}
				} catch (IOException e) {
					e.printStackTrace();
				}

				return address;
			}

			
			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {

			}

			@Override
			public void onProviderEnabled(String provider) {
			}

			@Override
			public void onProviderDisabled(String provider) {
			}
		};

		// Register the listener with the Location Manager to receive location
		// updates
		if (locationManager.isProviderEnabled("gps")) {
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
		} else if (locationManager.isProviderEnabled("network")) {
			locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
		}
	}

	public void displayBusStopLocations() {

		List<Overlay> mapOverlays = UMLmapView.getOverlays();
		Drawable drawable = this.getResources().getDrawable(R.drawable.bustop);

		GeoPoint tmpPoint;
		OverlayItem tmpBusOverlay;
		busStopItemizedoverlay = new UMLBusItemizedOverlay(drawable, UMLmapView.getContext());

		// loop through arraylist of busStops and add their points on the map
		for (BusStop bs : busStops) {
			tmpPoint = new GeoPoint((int) (bs.getBusLat() * 1E6), (int) (bs.getBusLon() * 1E6));
			tmpBusOverlay = new OverlayItem(tmpPoint, bs.getBusStopName(), " ");
			busStopItemizedoverlay.addOverlay(tmpBusOverlay);
		}
		mapOverlays.add(busStopItemizedoverlay);
		MapController mc = UMLmapView.getController();
		mc.animateTo(centerOfCampus);
		mc.setZoom(15);
	}

	public void getBusCoordinates() {

		try {

			URL umlshuttletracker = new URL("http://umlshuttletracker.info/getLastLocation.php");
			URLConnection yc = umlshuttletracker.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
			String inputLine;
			String tmp[];

			while ((inputLine = in.readLine()) != null) {
				tmp = inputLine.split(",");
				busLocationLat = Double.parseDouble(tmp[2].trim());
				busLocationLon = Double.parseDouble(tmp[3].trim());
			}
			in.close();

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			mapHandler.getLooper().quit();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	public void onStart(Bundle savedInstanceState) {
		// locationManager.removeUpdates(locationListener);
		// locationManager = null;
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
	}

	public void onPause(Bundle savedInstanceState) {
		mapHandler.getLooper().quit();		
		super.onPause();
	}

	public void onDestroy(Bundle savedInstanceState) {
		mapHandler.getLooper().quit();
		super.onDestroy();
	}

	public void onStop(Bundle savedInstanceState) {
		 mapHandler.getLooper().quit();
		 super.onStop();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (userflag == true){
			displayStudentLocation();
		}
		
		if(stopflag == true){
			displayBusStopLocations();
		}
	}

}