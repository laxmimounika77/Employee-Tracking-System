package ct.employeelocation;




import java.io.IOException;
import java.util.List;
import java.util.Locale;

import ct.employeelocation.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements LocationListener, OnInitListener,ConnectionCallbacks,
OnConnectionFailedListener
{
	 TextToSpeech talker;
	 String message;
	 private static final String TAG1= MainActivity.class.getSimpleName();
	 
	    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
	 
	    private Location mLastLocation;
	 
	    // Google client to interact with Google API
	    private GoogleApiClient mGoogleApiClient;
	 
	    // boolean flag to toggle periodic location updates
	    private boolean mRequestingLocationUpdates = false;
	 
	    private LocationRequest mLocationRequest;
	 
	    // Location updates intervals in sec
	    private static int UPDATE_INTERVAL = 10000; // 10 sec
	    private static int FATEST_INTERVAL = 5000; // 5 sec
	    private static int DISPLACEMENT = 10; // 10 meters
	 
	    // UI elements
	    private TextView lblLocation;
	    private Button btnShowLocation, btnStartLocationUpdates;
	 
	private static final String TAG = "drive";
	private static final boolean D=true;
	private SensorManager sensorManager;
	private Object sensor;
	private int x, y, z;
	private TextView result;
	private TextView pattern;
	   LocationManager locationManager ;
	   	String provider;
	private Button bdrive;
   public boolean mQuitting = false;
 //  private PowerManager.WakeLock wl;
   private  static boolean drive =false;
   Button b1,b2,b3;
  TextView tv1;
  boolean isPressed=true;
	    
   
   /** Called when the activity is first created. */
   @Override
   public void onCreate(Bundle savedInstanceState)
   {
   	
       super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_main);
       b1=(Button)findViewById(R.id.badd);
       //b2=(Button)findViewById(R.id.bview);
       b3=(Button)findViewById(R.id.bexit);
       if (checkPlayServices()) {
    	   
           // Building the GoogleApi client
           buildGoogleApiClient();

           createLocationRequest();
       }
       
      // Button 1 (add recipents) event
      b1.setOnClickListener(new OnClickListener()
      {
 		@Override
 		public void onClick(View v) {
 			// TODO Auto-generated method stub
 		Intent i=new Intent(getApplicationContext(),add.class);	
 		startActivity(i);
 		finish();
 		}
 	  });
     
     // button 2 view recipents
      /* b2.setOnClickListener(new OnClickListener() 
       {
     	  @Override
 		 public void onClick(View v) {
 			// TODO Auto-generated method stub
 			
     	  Intent i=new Intent(getApplicationContext(),view.class);
     	  startActivity(i);
     
     	  }
 	    });*/
     
     //button3 exit
       b3.setOnClickListener(new OnClickListener() 
      {
 		@Override
 		public void onClick(View v) {
 			// TODO Auto-generated method stub
 			//finish();
 			System.exit(0);
 		  }
 	});
     
       locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);        
       
       // Creating an empty criteria object
       Criteria criteria = new Criteria();
       
       // Getting the name of the provider that meets the criteria
       provider = locationManager.getBestProvider(criteria, false);
    
      // LocationManager locationManager;
      // String context = Context.LOCATION_SERVICE;
      // locationManager = (LocationManager)getSystemService(context);
       //String provider = LocationManager.GPS_PROVIDER;
       
     //  Location location =locationManager.getLastKnownLocation(provider);
       //updateWithNewLocation(location);

       if(provider!=null && !provider.equals("")){
       	
       	// Get the location from the given provider 
    	   //Toast.makeText(getApplicationContext(),provider,Toast.LENGTH_LONG).show();
           Location location = locationManager.getLastKnownLocation(provider);
                       
           locationManager.requestLocationUpdates(provider, 20000, 1, this);
           
           
           if(location!=null)
           	onLocationChanged(location);
           //else
           //	Toast.makeText(getBaseContext(), "Location can't be retrieved", Toast.LENGTH_SHORT).show();
           
       }else{
       	Toast.makeText(getBaseContext(), "No Provider Found", Toast.LENGTH_SHORT).show();
       }
       sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		sensor = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0);
		
	
       bdrive = (Button)findViewById(R.id.btnstart);
       bdrive.setOnClickListener(new OnClickListener()
       { 
       	public void onClick (View v){ 
       		
       	 if(isPressed){
       		bdrive.setBackgroundResource(R.drawable.green_button);
       		startdriving(); 
             }else{
            	 bdrive.setBackgroundResource(R.drawable.women_stop_logo);
             }
             isPressed=!isPressed;
      
       		
       		}});
       
       result = (TextView) findViewById(R.id.tv);
		result.setText("Off Mode");
		
		pattern = (TextView) findViewById(R.id.tv1);
		
		
		// Getting a WakeLock. This insures that the phone does not sleep
		/*	PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
       wl = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "My Tag");
       wl.acquire();  */
       
       //tts in oncreate
       talker = new TextToSpeech(this, this);

    /*   Intent checkIntent = new Intent();
		checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
		startActivityForResult(checkIntent, MY_DATA_CHECK_CODE);*/
		
   }
   protected void createLocationRequest() {
       mLocationRequest = new LocationRequest();
       mLocationRequest.setInterval(UPDATE_INTERVAL);
       mLocationRequest.setFastestInterval(FATEST_INTERVAL);
       mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
       mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
   }
   public void onInit(int status) 
   {
		
	//	say("Welcome to women safety");
		
	}
   
   @Override
   public void onConnectionFailed(ConnectionResult result) {
       Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = "
               + result.getErrorCode());
   }
   
  private void say(String text2say) 
  {
		// TODO Auto-generated method stub
	   talker.speak(text2say, TextToSpeech.QUEUE_FLUSH, null);

	}

   
	private void refreshDisplay() 
   {
		String output = String
				.format("You Are in Safety Mode", x, y, z);
		result.setText(output);
	}
   
   @Override
   public void onStart() 
   {
       super.onStart();
       if (mGoogleApiClient != null) {
           mGoogleApiClient.connect();
       }
       if(D) Log.e(TAG, "++ ON START ++");
 
      }
   
   public void onConnected(Bundle arg0) {
	   
       // Once connected with google api, get the location
       try {
		displayLocation();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

       if (mRequestingLocationUpdates) {
           //startLocationUpdates();
       }
   }
   @Override
	protected void onResume() 
   {
		super.onResume();
		sensorManager.registerListener(accelerationListener, (Sensor) sensor,
				SensorManager.SENSOR_DELAY_GAME);
		checkPlayServices();
		 
        // Resuming the periodic location updates
        if (mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {
            //startLocationUpdates();
        }
   }
   @Override
	protected void onStop()
   {
		sensorManager.unregisterListener(accelerationListener);
		super.onStop();
		//wl.release();
	}
   @Override
   public void onDestroy() 
   {
   	
   	if (talker != null) {
			talker.stop();
			talker.shutdown();
		}

       super.onDestroy();
      
       if(D) Log.e(TAG, "--- ON DESTROY ---");
   }
   
   private boolean checkPlayServices() {
       int resultCode = GooglePlayServicesUtil
               .isGooglePlayServicesAvailable(this);
       if (resultCode != ConnectionResult.SUCCESS) {
           if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
               GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                       PLAY_SERVICES_RESOLUTION_REQUEST).show();
           } else {
               Toast.makeText(getApplicationContext(),
                       "This device is not supported.", Toast.LENGTH_LONG)
                       .show();
               finish();
           }
           return false;
       }
       return true;
   }
   
   
   ////////////////////////////////message////////////////////////////////////
   private void sendSMS( String message) throws IOException
   {        
      /* PendingIntent pi = PendingIntent.getActivity(this, 0,
           new Intent(this, sms.class), 0);                
     SmsManager sms = SmsManager.getDefault();
       sms.sendTextMessage(phoneNumber, null, message, pi, null);  */
   	
   	String SENT = "SMS_SENT";
   	String DELIVERED = "SMS_DELIVERED";
   	
       PendingIntent sentPI = PendingIntent.getBroadcast(this, 0,
           new Intent(SENT), 0);
       
       PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
           new Intent(DELIVERED), 0);
   	
       //---when the SMS has been sent---
       registerReceiver(new BroadcastReceiver()
       {
			@Override
			public void onReceive(Context arg0, Intent arg1) 
			{
				switch (getResultCode())
				{
				    case Activity.RESULT_OK:
					    Toast.makeText(getBaseContext(), "SMS sent", 
					    		Toast.LENGTH_SHORT).show();
					    break;
				    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
					    Toast.makeText(getBaseContext(), "Generic failure", 
					    		Toast.LENGTH_SHORT).show();
					    break;
				    case SmsManager.RESULT_ERROR_NO_SERVICE:
					    Toast.makeText(getBaseContext(), "No service", 
					    		Toast.LENGTH_SHORT).show();
					    break;
				    case SmsManager.RESULT_ERROR_NULL_PDU:
					    Toast.makeText(getBaseContext(), "Null PDU", 
					    		Toast.LENGTH_SHORT).show();
					    break;
				    case SmsManager.RESULT_ERROR_RADIO_OFF:
					    Toast.makeText(getBaseContext(), "Radio off", 
					    		Toast.LENGTH_SHORT).show();
					    break;
				}
			}
       }, new IntentFilter(SENT));
       
       //---when the SMS has been delivered---
       registerReceiver(new BroadcastReceiver()
       {
			@Override
			public void onReceive(Context arg0, Intent arg1) 
			{
				switch (getResultCode())
				{
				    case Activity.RESULT_OK:
					    Toast.makeText(getBaseContext(), "SMS delivered", 
					    		Toast.LENGTH_SHORT).show();
					    break;
				    case Activity.RESULT_CANCELED:
					    Toast.makeText(getBaseContext(), "SMS not delivered", 
					    		Toast.LENGTH_SHORT).show();
					    break;					    
				}
			}
       }, new IntentFilter(DELIVERED));        
   	
       SmsManager sms = SmsManager.getDefault();
       message=displayLocation();
       SQLiteDatabase db;
       db=openOrCreateDatabase("Recipent_DB", MODE_PRIVATE, null);
		Cursor cursor=db.rawQuery("select *from vendor_table",null);
		if (cursor != null) {

			if (cursor.moveToFirst()) {
				do {
				String	vno=cursor.getString(cursor.getColumnIndex("vendor_num"));
					//Toast.makeText(getApplicationContext(),vid,Toast.LENGTH_SHORT).show();
										
				sms.sendTextMessage(vno, null, message, sentPI, deliveredPI);
				} while (cursor.moveToNext());

			}
		}
       
   }    
   
   /////////////////////gps //////////////////////////////
   private void turnGPSOn()
	{   

	    String provider = Settings.Secure.getString(getContentResolver(), 
	    		Settings.Secure.LOCATION_PROVIDERS_ALLOWED);   
	    if(!provider.contains("gps"))
	    {      
	        final Intent poke = new Intent();  
	        poke.setClassName("com.android.settings","com.android.settings.widget.SettingsAppWidgetProvider");        
	        poke.addCategory(Intent.CATEGORY_ALTERNATIVE);   
	        poke.setData(Uri.parse("3"));      
	        sendBroadcast(poke);  
	     }
	   }    
   //////////////////////////GPSOFF/////////////////////////
	private void turnGPSOFF()
	{
		String provider = Settings.Secure.getString(getContentResolver(), 
	    		Settings.Secure.LOCATION_PROVIDERS_ALLOWED);   
	    if(provider.contains("gps"))
	    {      
	        final Intent poke = new Intent();  
	        poke.setClassName("com.android.settings","com.android.settings.widget.SettingsAppWidgetProvider");        
	        poke.addCategory(Intent.CATEGORY_ALTERNATIVE);   
	        poke.setData(Uri.parse("3"));      
	        sendBroadcast(poke);  
	     }
		
	}
	
	   //////////////////////////accelerometer////////////////////////////////////
   public void startdriving()
	{
   	     	
		if(drive==false)
		{
			turnGPSOn();
			drive = true;
			
		}
		else
		{
			
			turnGPSOFF();
			drive=false;
		}
	}
   
   
   private SensorEventListener accelerationListener = new SensorEventListener()
	{
		public void onAccuracyChanged(Sensor sensor, int acc)
		{
		}

		public void onSensorChanged(SensorEvent event)
		{
		
			x = (int) event.values[0];
           y = (int) event.values[1];
			z = (int) event.values[2];
			
			if(drive)
			{
				
				String str1= "VR SIDDHARTHA ENGG COLLEGE,KANURU VJA.";
			  //  String phoneNo = "+919492580083";
               String message = str1;
               
		// weaving...................		
			if(y<-9||y>9)
			  {
									     
						   try {
							sendSMS(message);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						   pattern.setText("Your in Danger.");
						   //Toast.makeText(Drive.this, "Car Drifting.",Toast.LENGTH_LONG).show();
						   talker.speak("Your in Danger", TextToSpeech.QUEUE_ADD, null);
						   talker.stop();
						   talker.shutdown();
						   stopSelf();
						   drive=false;
						   
					   
			  }
			
			else
				
		
				
			//Moving Straight...............
			 if(y>=-2&& y<=2)
			 {
						 talker.stop();
				 talker.shutdown();
			 }
			
			refreshDisplay();
		}
	  }

		private void stopSelf() {
			// TODO Auto-generated method stub
			
		}
	};
	///////////////GPS///////////////////////////////////////////////////////
	  public void onProviderDisabled(String provider) 
	    {
	    	//Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			//startActivity(intent);

	    }
	  
		public void onProviderEnabled(String provider) 
		{
			// TODO Auto-generated method stub
			
			Toast.makeText(this, "GPS Enabled", Toast.LENGTH_SHORT).show();
			//onLocationChanged(null);
			
		}
	  

		/*public void onLocationChanged(Location location) {
			// Getting reference to TextView tv_longitude
			//TextView tvLongitude = (TextView)findViewById(R.id.tv_longitude);
			
			// Getting reference to TextView tv_latitude
			//TextView tvLatitude = (TextView)findViewById(R.id.tv_latitude);
			
			// Setting Current Longitude
			//tvLongitude.setText("Longitude:" + location.getLongitude());
			
			// Setting Current Latitude
			//tvLatitude.setText("Latitude:" + location.getLatitude() );
			//tvLatitude.setText("Provider:" + provider );
			Toast.makeText(getApplicationContext(),new Double(location.getLongitude()).toString(),Toast.LENGTH_SHORT).show();
			Toast.makeText(getApplicationContext(),new Double(location.getLatitude()).toString(),Toast.LENGTH_SHORT).show();
		}*/
		public void onLocationChanged(Location location) {
	        // Assign the new location
	        mLastLocation = location;
	 
	        Toast.makeText(getApplicationContext(), "Location changed!",
	                Toast.LENGTH_SHORT).show();
	 
	        // Displaying the new location on UI
	        try {
				displayLocation();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	

		public void onStatusChanged(String provider, int status, Bundle arg2)
		{
			// TODO Auto-generated method stub
			
			
			switch (status)
			{
			case LocationProvider.OUT_OF_SERVICE:
				
				Toast.makeText(this, "Status Changed: Out of Service",
						Toast.LENGTH_SHORT).show();
				break;
			case LocationProvider.TEMPORARILY_UNAVAILABLE:
				
				Toast.makeText(this, "Status Changed: Temporarily Unavailable",
						Toast.LENGTH_SHORT).show();
				break;
			case LocationProvider.AVAILABLE:
				
				Toast.makeText(this, "Status Changed: Available",
						Toast.LENGTH_SHORT).show();
				break;
			}

			
		}
		
	// Menu option for Quit .....///////////////////////////////////////////////
	private static final int MENU_QUIT = 9;
   @Override
	public boolean onPrepareOptionsMenu(Menu menu) 
   {
       menu.clear();
       menu.add(0, MENU_QUIT, 0,null)
       .setIcon(android.R.drawable.ic_lock_power_off)
       .setShortcut('9', 'q');
       return true;
   }
   
   @Override
   public void onConnectionSuspended(int arg0) {
       mGoogleApiClient.connect();
   }

   protected synchronized void buildGoogleApiClient() {
       mGoogleApiClient = new GoogleApiClient.Builder(this)
               .addConnectionCallbacks(this)
               .addOnConnectionFailedListener(this)
               .addApi(LocationServices.API).build();
   }
   private String displayLocation() throws IOException {

       String address=null,city=null,state=null,country=null,postalCode=null,knownName=null;
       mLastLocation = LocationServices.FusedLocationApi
               .getLastLocation(mGoogleApiClient);
       double latitude=0.0,longitude=0.0;
       if (mLastLocation != null) {
            latitude = mLastLocation.getLatitude();
          longitude = mLastLocation.getLongitude();
          Geocoder geocoder;
          List<Address> addresses;
          geocoder = new Geocoder(this, Locale.getDefault());

          addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

          address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
           city = addresses.get(0).getLocality();
          state = addresses.get(0).getAdminArea();
         country = addresses.get(0).getCountryName();
           postalCode = addresses.get(0).getPostalCode();
           knownName = addresses.get(0).getFeatureName();
          // lblLocation.setText(latitude + ", " + longitude);
           //Toast.makeText(getApplicationContext(),new Double(latitude).toString(),Toast.LENGTH_LONG).show();

       } else {

           lblLocation
                   .setText("(Couldn't get the location. Make sure location is enabled on the device)");
       }
       return  address+","+city+","+state+","+country;
   }

   
   /* Handles item selections */
   @Override
	public boolean onOptionsItemSelected(MenuItem item) 
   {
       switch (item.getItemId())
       {
       case MENU_QUIT:
       	
       	sensorManager.unregisterListener(accelerationListener);
       	talker.stop();
			talker.shutdown();
			turnGPSOFF();
           mQuitting = true;
           finish();
           return true;
        }
   return false;
 }
}