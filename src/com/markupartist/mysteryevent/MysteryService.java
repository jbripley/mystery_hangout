package com.markupartist.mysteryevent;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class MysteryService {
    private static final String TAG = "MysteryService";
    private static String API_URL = "http://data.hyves-api.nl";

    private OAuthHttpHelper mHttpHelper;

    public MysteryService(OAuthHttpHelper httpHelper) {
        mHttpHelper = httpHelper;
    }

    public Hub getRandomHangoutByGeoLocation(String latitude, String longitude) 
        throws HangoutNotFoundException {

        ArrayList<Hub> hubs = getHangoutsByGeoLocation(latitude, longitude);
        Hub hangout = null;
        if (hubs.size() == 0) {
            // TODO: If not found try a new search but with a wider radius repeat to we reach 10000.
            throw new HangoutNotFoundException("No hangout found for location");
        }

        Collections.shuffle(hubs);
        hangout = hubs.get(0);
        return hangout;
    }

    private ArrayList<Hub> getHangoutsByGeoLocation(String latitude, String longitude) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("ha_method", "hubs.getBySpatialRadiusMostPopulair"));
        params.add(new BasicNameValuePair("latitude", latitude));
        params.add(new BasicNameValuePair("longitude", longitude));
        params.add(new BasicNameValuePair("radius", "2000"));
        params.add(new BasicNameValuePair("hubtype", "hangout"));
        params.add(new BasicNameValuePair("ha_resultsperpage", "50"));
        params.add(new BasicNameValuePair("ha_responsefields", "geolocation,hubaddress,hubopeninghours"));
        params.add(new BasicNameValuePair("ha_format", "xml"));
        params.add(new BasicNameValuePair("ha_fancylayout", "false"));
        params.add(new BasicNameValuePair("ha_version", "experimental"));

        InputStream result = mHttpHelper.post(API_URL, params);
        HubParser hubParser = new HubParser();
        ArrayList<Hub> hangouts = hubParser.getHubs(result);

        return hangouts;
    }
}
