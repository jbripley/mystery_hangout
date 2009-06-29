package com.markupartist.mysteryevent;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;

public class Shake extends Activity {
    private static String CONSUMER_KEY = "";
    private static String CONSUMER_SECRET = "";

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        final Vibrator vibe = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        
        final ShakeListener mShaker = new ShakeListener(this);
        
        ProgressDialog dialog;
        
        mShaker.setOnShakeListener(new ShakeListener.OnShakeListener () {
          public void onShake()
          {
			mShaker.pause();
			vibe.vibrate(300);
			ProgressDialog.show(Shake.this, "", 
			        getText(R.string.loading), true);
            // open maps view
			startDirectionActivity("", "");
          }
        });
        
        
        OAuthHttpHelper httpHelper = new OAuthHttpHelper(CONSUMER_KEY, CONSUMER_SECRET);

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("searchterms", "amsterdam"));
        params.add(new BasicNameValuePair("ha_method", "hangouts.search"));
        params.add(new BasicNameValuePair("ha_resultsperpage", "2"));
        params.add(new BasicNameValuePair("ha_responsefields", "geolocation"));
        params.add(new BasicNameValuePair("ha_version", "1.2"));
        params.add(new BasicNameValuePair("ha_fancylayout", "false"));
        params.add(new BasicNameValuePair("ha_format", "json"));

        String result = httpHelper.post("http://data.hyves-api.nl", params);

        try {
            JSONObject json = new JSONObject(result);
            
            JSONArray jsonHangoutArray = json.getJSONArray("hangout");
            JSONObject jsonGeo = jsonHangoutArray.getJSONObject(0);
            JSONObject jsonGeoLocation = jsonGeo.getJSONObject("geolocation");

            Log.i("Shake", "lat: " + jsonGeoLocation.getString("latitude"));
            Log.i("Shake", "lon: " + jsonGeoLocation.getString("longitude"));
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        
        
        
    }
    
    private void startDirectionActivity(String start, String destination)
    {
    	 start = "Fredriksplein 7, amsterdam";
         destination = "leidseplein, amsterdam";
        
        startActivity(new Intent(Intent.ACTION_VIEW,  
                Uri.parse("http://maps.google.com/maps?f=d&saddr=" + start + "&daddr=" + destination + "&hl=en&dirflg=w")));    	
    }
    
}