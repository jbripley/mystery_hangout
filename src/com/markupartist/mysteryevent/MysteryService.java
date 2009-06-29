package com.markupartist.mysteryevent;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MysteryService {
    private static String API_URL = "http://data.hyves-api.nl";
    private OAuthHttpHelper httpHelper;

    public MysteryService(OAuthHttpHelper httpHelper) {
        this.httpHelper = httpHelper;
    }

    public Hangout getRandomHangoutByLocality(String locality) {
        ArrayList<Hangout> hangouts = getHangoutsByLocality(locality);
        for (Hangout hangout : hangouts) {
            System.out.println(hangout);
        }
        return null;
    }

    private ArrayList<Hangout> getHangoutsByLocality(String locality) {
        // TODO: add pagination...

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("searchterms", locality));
        params.add(new BasicNameValuePair("ha_method", "hangouts.search"));
        params.add(new BasicNameValuePair("ha_resultsperpage", "20"));
        params.add(new BasicNameValuePair("ha_responsefields", "geolocation"));
        params.add(new BasicNameValuePair("ha_version", "1.2"));
        params.add(new BasicNameValuePair("ha_fancylayout", "false"));
        params.add(new BasicNameValuePair("ha_format", "json"));

        String result = httpHelper.post(API_URL, params);

        ArrayList<Hangout> hangouts = new ArrayList<Hangout>();

        JSONObject json;
        try {
            json = new JSONObject(result);
            JSONArray jsonHangoutsArray = json.getJSONArray("hangout");

            for(int i = 0; i < jsonHangoutsArray.length(); i++) {
                hangouts.add(jsonToHangout(jsonHangoutsArray.getJSONObject(i)));
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    private Hangout jsonToHangout(JSONObject jsonHangout) {
        Hangout hangout = new Hangout();
        try {
            JSONObject jsonGeoLocation = jsonHangout.getJSONObject("geolocation");
            //hangout.setTitle(title);
            hangout.setLatitude(jsonGeoLocation.getString("latitude"));
            hangout.setLongitude(jsonGeoLocation.getString("longitude"));
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return hangout;
    }
}
