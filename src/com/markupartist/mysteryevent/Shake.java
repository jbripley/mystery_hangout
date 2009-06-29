package com.markupartist.mysteryevent;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class Shake extends Activity {
    private static String CONSUMER_KEY = "";
    private static String CONSUMER_SECRET = "";

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

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
}