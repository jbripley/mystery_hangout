package com.markupartist.mysteryevent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.signature.SignatureMethod;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

/**
 * Hacked up class, should be separated, but we leave that for another time.
 */
public class OAuthHttpHelper {
    DefaultHttpClient httpClient = new DefaultHttpClient();
    HttpContext localContext = new BasicHttpContext();
    HttpResponse response = null;
    HttpPost httpPost = null;
    OAuthConsumer consumer;
    String consumerKey;
    String consumerSecret;

    public OAuthHttpHelper(String consumerKey, String consumerSecret) {
        consumer = new CommonsHttpOAuthConsumer(consumerKey, consumerSecret, 
                SignatureMethod.HMAC_SHA1);
    }

    public void clearCookies() {
        httpClient.getCookieStore().clear();
    }

    public void abort() {
        try {
            if (httpClient != null) {
                System.out.println("Abort.");
                httpPost.abort();
            }
        } catch (Exception e) {
            System.out.println("HTTPHelp : Abort Exception : " + e);
        }
    }

    public String post(String url, List<NameValuePair> params) {
        httpPost = new HttpPost(url);

        UrlEncodedFormEntity entity = null;
        try {
            entity = new UrlEncodedFormEntity(params, "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        httpPost.setEntity(entity);

        response = null;
        String data = "";
        try {
            consumer.sign(httpPost);
            
            response = httpClient.execute(httpPost, localContext);
            data = convertStreamToString(response.getEntity().getContent());
        } catch (ClientProtocolException e) {
            System.out.println("HTTPHelp : ClientProtocolException : " + e);
        } catch (IOException e) {
            System.out.println("HTTPHelp : IOException : " + e);
        } catch (OAuthMessageSignerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (OAuthExpectationFailedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return data;
    }

    /**
     * http://senior.ceng.metu.edu.tr/2009/praeda/2009/01/11/a-simple-restful-client-at-android/
     * @param is
     * @return
     */
    private static String convertStreamToString(InputStream is) {
        /*
         * To convert the InputStream to String we use the BufferedReader.readLine()
         * method. We iterate until the BufferedReader return null which means
         * there's no more data to read. Each line will appended to a StringBuilder
         * and returned as String.
         */
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
 
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

}
