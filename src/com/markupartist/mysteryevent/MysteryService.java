package com.markupartist.mysteryevent;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class MysteryService {
    private static final String TAG = "MysteryService";
    private static String API_URL = "http://data.hyves-api.nl";
    private OAuthHttpHelper httpHelper;

    public MysteryService(OAuthHttpHelper httpHelper) {
        this.httpHelper = httpHelper;
    }

    public Hangout getRandomHangoutByGeoLocation(String latitude, String longitude) 
        throws HangoutNotFoundException {
        ArrayList<Hangout> hangouts = getHangoutsByGeoLocation(latitude, longitude);
        Hangout hangout = null;
        if (hangouts.size() == 0) {
            throw new HangoutNotFoundException("No hangout found for location");
        }

        Collections.shuffle(hangouts);
        hangout = hangouts.get(0);
        return hangout;
    }

    private ArrayList<Hangout> getHangoutsByGeoLocation(String latitude, String longitude) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("ha_method", "hubs.getBySpatialRadiusMostPopulair"));
        params.add(new BasicNameValuePair("latitude", latitude));
        params.add(new BasicNameValuePair("longitude", longitude));
        params.add(new BasicNameValuePair("radius", "5000"));
        params.add(new BasicNameValuePair("hubtype", "hangout"));
        params.add(new BasicNameValuePair("ha_resultsperpage", "50"));
        params.add(new BasicNameValuePair("ha_responsefields", "geolocation,hubaddress,hubopeninghours"));
        params.add(new BasicNameValuePair("ha_format", "xml"));
        params.add(new BasicNameValuePair("ha_fancylayout", "false"));
        params.add(new BasicNameValuePair("ha_version", "experimental"));

        InputStream result = httpHelper.post(API_URL, params);
        HubParser hubParser = new HubParser();
        ArrayList<Hangout> hangouts = hubParser.getHangouts(result);
        return hangouts;
    }
}
