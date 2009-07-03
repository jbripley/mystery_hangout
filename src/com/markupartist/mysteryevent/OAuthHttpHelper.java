package com.markupartist.mysteryevent;

import java.io.IOException;
import java.io.InputStream;
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

import android.util.Log;

/**
 * Wrapper around HTTPClient and Sign post that sign request and sends them.
 */
public class OAuthHttpHelper {
	private static String TAG = "OAuthHttpHelper";
    private DefaultHttpClient mHttpClient = new DefaultHttpClient();
    private OAuthConsumer mConsumer;

    public OAuthHttpHelper(String consumerKey, String consumerSecret) {
        mConsumer = new CommonsHttpOAuthConsumer(consumerKey, consumerSecret, 
                SignatureMethod.HMAC_SHA1);
    }

    /**
     * Posts provided parameters to the provided url.
     * TODO: Need to add a communication exception or something like that.
     * @param url the url to post to
     * @param params list of parameters
     * @return response of the post
     */
    public InputStream post(String url, List<NameValuePair> params) {
    	HttpPost httpPost = new HttpPost(url);
        InputStream responseStream = null;
        try {
        	UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
        	httpPost.setEntity(entity);

            mConsumer.sign(httpPost);

            HttpResponse response = mHttpClient.execute(httpPost);
            responseStream = response.getEntity().getContent();
        } catch (ClientProtocolException e) {
            Log.e(TAG, e.getMessage());
        } catch (IOException e) {
        	Log.e(TAG, e.getMessage());
        } catch (OAuthMessageSignerException e) {
        	Log.e(TAG, e.getMessage());
        } catch (OAuthExpectationFailedException e) {
        	Log.e(TAG, e.getMessage());
        }

        return responseStream;
    }
}
