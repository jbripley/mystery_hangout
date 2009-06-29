package com.markupartist.mysteryevent;

/* The following code was written by Matthew Wiggins
 * and is released under the APACHE 2.0 license
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.content.Context;
import java.lang.UnsupportedOperationException;

public class ShakeListener implements SensorEventListener 
{
  private static final int FORCE_THRESHOLD = 350;
  private static final int TIME_THRESHOLD = 100;
  private static final int SHAKE_TIMEOUT = 500;
  private static final int SHAKE_DURATION = 1000;
  private static final int SHAKE_COUNT = 3;

  private SensorManager mSensorMgr;
  private float mLastX=-1.0f, mLastY=-1.0f, mLastZ=-1.0f;
  private long mLastTime;
  private OnShakeListener mShakeListener;
  private Context mContext;
  private int mShakeCount = 0;
  private long mLastShake;
  private long mLastForce;

  public interface OnShakeListener
  {
    public void onShake();
  }

  public ShakeListener(Context context) 
  { 
    mContext = context;
    resume();
  }

  public void setOnShakeListener(OnShakeListener listener)
  {
    mShakeListener = listener;
  }

  public void resume() {
    mSensorMgr = (SensorManager)mContext.getSystemService(Context.SENSOR_SERVICE);
    if (mSensorMgr == null) {
      throw new UnsupportedOperationException("Sensors not supported");
    }
    boolean supported = mSensorMgr.registerListener(this, mSensorMgr.getDefaultSensor(SensorManager.SENSOR_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
    if (!supported) {
      mSensorMgr.unregisterListener(this, mSensorMgr.getDefaultSensor(SensorManager.SENSOR_ACCELEROMETER));
      throw new UnsupportedOperationException("Accelerometer not supported");
    }
  }

  public void pause() {
    if (mSensorMgr != null) {
      mSensorMgr.unregisterListener(this, mSensorMgr.getDefaultSensor(SensorManager.SENSOR_ACCELEROMETER));
      mSensorMgr = null;
    }
  }

  public void onAccuracyChanged(Sensor se, int i) { }

  public void onSensorChanged(SensorEvent se) 
  {
	float[] values = se.values;
    if (se.sensor.getType() != Sensor.TYPE_ACCELEROMETER) return;
    long now = System.currentTimeMillis();

    if ((now - mLastForce) > SHAKE_TIMEOUT) {
      mShakeCount = 0;
    }

    if ((now - mLastTime) > TIME_THRESHOLD) {
      long diff = now - mLastTime;
      float speed = Math.abs(values[SensorManager.DATA_X] + values[SensorManager.DATA_Y] + values[SensorManager.DATA_Z] - mLastX - mLastY - mLastZ) / diff * 10000;
      if (speed > FORCE_THRESHOLD) {
        if ((++mShakeCount >= SHAKE_COUNT) && (now - mLastShake > SHAKE_DURATION)) {
          mLastShake = now;
          mShakeCount = 0;
          if (mShakeListener != null) { 
            mShakeListener.onShake(); 
          }
        }
        mLastForce = now;
      }
      mLastTime = now;
      mLastX = values[SensorManager.DATA_X];
      mLastY = values[SensorManager.DATA_Y];
      mLastZ = values[SensorManager.DATA_Z];
    }
  }

}
