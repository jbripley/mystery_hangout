package com.markupartist.mysteryevent;

import java.text.DecimalFormat;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class Shake extends Activity {
    private static String CONSUMER_KEY = "";
    private static String CONSUMER_SECRET = "";

    Vibrator vibe;
    ShakeListener mShaker;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        vibe = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        ImageView im = (ImageView) findViewById(R.id.shake_imageview);
        
        im.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
		        final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

				vibe.vibrate(300);
				ProgressDialog.show(Shake.this, "", 
				        getText(R.string.loading), true);

		        Location loc = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

		        OAuthHttpHelper httpHelper = new OAuthHttpHelper(CONSUMER_KEY, CONSUMER_SECRET);
		        MysteryService mysteryService = new MysteryService(httpHelper);
		        Hangout hangout = null;
		        try {
                    //String latitude = "52.35554";
                    //String longitude = "4.88856";
		            
		            DecimalFormat fmt = new DecimalFormat();
		            fmt.setMinimumFractionDigits(2);
		            fmt.setMaximumFractionDigits(5);
		            String latitude = fmt.format(loc.getLatitude());
		            String longitude = fmt.format(loc.getLongitude());

		            Log.e("Shake", "Searching lat:" + latitude + "lon:" + longitude);
                    hangout = mysteryService.getRandomHangoutByGeoLocation(latitude, longitude);

                    Log.d("Shake", hangout.getTitle());
                    Log.d("Shake", hangout.getLongitude());
                    Log.d("Shake", hangout.getLatitude());

                    // open maps view
                    startDirectionActivity(
                            loc.getLatitude() + "," + loc.getLongitude(),
                            hangout.getLatitude() + "," + hangout.getLongitude());
                } catch (HangoutNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
			}
        	
        });

        mShaker = new ShakeListener(this);
        mShaker.setOnShakeListener(new ShakeListener.OnShakeListener () {
          public void onShake()
          {
              final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

              vibe.vibrate(300);
              ProgressDialog.show(Shake.this, "", 
                      getText(R.string.loading), true);

              Location loc = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

              OAuthHttpHelper httpHelper = new OAuthHttpHelper(CONSUMER_KEY, CONSUMER_SECRET);
              MysteryService mysteryService = new MysteryService(httpHelper);
              Hangout hangout = null;
              try {
                  //String latitude = "52.35554";
                  //String longitude = "4.88856";
                  
                  DecimalFormat fmt = new DecimalFormat();
                  fmt.setMinimumFractionDigits(2);
                  fmt.setMaximumFractionDigits(5);
                  String latitude = fmt.format(loc.getLatitude());
                  String longitude = fmt.format(loc.getLongitude());

                  Log.e("Shake", "Searching lat:" + latitude + "lon:" + longitude);
                  hangout = mysteryService.getRandomHangoutByGeoLocation(latitude, longitude);

                  Log.d("Shake", hangout.getTitle());
                  Log.d("Shake", hangout.getLongitude());
                  Log.d("Shake", hangout.getLatitude());

                  // open maps view
                  startDirectionActivity(
                          loc.getLatitude() + "," + loc.getLongitude(),
                          hangout.getLatitude() + "," + hangout.getLongitude());
              } catch (HangoutNotFoundException e) {
                  // TODO Auto-generated catch block
                  e.printStackTrace();
              }
          }
        });
    }
    
    protected void wasShaken() {
        final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        mShaker.pause();
		vibe.vibrate(300);
		ProgressDialog.show(Shake.this, "", 
		        getText(R.string.loading), true);
        
        Location loc = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        
        // open maps view
		startDirectionActivity("", "");
	}

	private void startDirectionActivity(String start, String destination)
    {
         startActivity(new Intent(Intent.ACTION_VIEW,  
                Uri.parse("http://maps.google.com/maps?f=d&saddr=" + start + "&daddr=" + destination + "&hl=en&dirflg=w")));    	
    }
    
}