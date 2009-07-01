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
    private static final String TAG = "Shake";
    private static String CONSUMER_KEY = "";
    private static String CONSUMER_SECRET = "";

    private Vibrator mVibe;
    private ShakeListener mShaker;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mVibe = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        ImageView im = (ImageView) findViewById(R.id.shake_imageview);

        im.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
			    wasShaken();
			}
        });

        // Shaker stuff needs to be commented out when testing in the emulator...
        mShaker = new ShakeListener(this);
        mShaker.setOnShakeListener(new ShakeListener.OnShakeListener () {
            public void onShake() {
                wasShaken();
            }
        });
    }

    protected void wasShaken() {
        final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        mVibe.vibrate(300);
        ProgressDialog progressDialog = ProgressDialog.show(Shake.this, "", 
                getText(R.string.loading), true);

        Location loc = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        OAuthHttpHelper httpHelper = new OAuthHttpHelper(CONSUMER_KEY, CONSUMER_SECRET);
        MysteryService mysteryService = new MysteryService(httpHelper);
        Hub hangout = null;
        try {
            DecimalFormat fmt = new DecimalFormat();
            fmt.setMinimumFractionDigits(2);
            fmt.setMaximumFractionDigits(5);

            String latitude = fmt.format(loc.getLatitude());
            String longitude = fmt.format(loc.getLongitude());

            //String latitude = "52.35554";
            //String longitude = "4.88856";

            Log.d(TAG, "Shake searching lat:" + latitude + "lon:" + longitude);
            hangout = mysteryService.getRandomHangoutByGeoLocation(latitude, longitude);

            Log.d(TAG, hangout.getTitle());
            Log.d(TAG, hangout.getLongitude());
            Log.d(TAG, hangout.getLatitude());

            // open maps view
            startDirectionActivity(
                    latitude + "," + longitude,
                    hangout.getLatitude() + "," + hangout.getLongitude());
        } catch (HangoutNotFoundException e) {
            // TODO: Need to handle this in the view!
            progressDialog.dismiss();
            Log.i(TAG, e.getMessage());
        }
	}

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "Closing application");
        finish();
    }

	private void startDirectionActivity(String start, String destination) {
	    startActivity(new Intent(Intent.ACTION_VIEW,  
                Uri.parse("http://maps.google.com/maps?f=d&saddr=" + start 
                        + "&daddr=" + destination + "&hl=en&dirflg=w")));    	
    }
    
}