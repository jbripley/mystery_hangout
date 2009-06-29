package com.markupartist.mysteryevent;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
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
        mShaker = new ShakeListener(this);
        
        
        ImageView im = (ImageView) findViewById(R.id.shake_imageview);
        
        im.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				// TODO Auto-generated method stub
				wasShaken();
			}
        	
        });
        
        mShaker.setOnShakeListener(new ShakeListener.OnShakeListener () {
          public void onShake()
          {
        	  wasShaken();
          }
        });
    }
    
    protected void wasShaken() {
		mShaker.pause();
		vibe.vibrate(300);
		ProgressDialog.show(Shake.this, "", 
		        getText(R.string.loading), true);
        // open maps view
		startDirectionActivity("", "");
	}

	private void startDirectionActivity(String start, String destination)
    {
    	 start = "Fredriksplein 7, amsterdam";
         destination = "leidseplein, amsterdam";
        
        startActivity(new Intent(Intent.ACTION_VIEW,  
                Uri.parse("http://maps.google.com/maps?f=d&saddr=" + start + "&daddr=" + destination + "&hl=en&dirflg=w")));    	
    }
    
}