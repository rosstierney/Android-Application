/* Copyright 2010 ESRI
 *
 * All rights reserved under the copyright laws of the United States
 * and applicable international laws, treaties, and conventions.
 *
 * You may freely redistribute and use this sample code, with or
 * without modification, provided you include the original copyright
 * notice and use restrictions.
 *
 * See the Sample code usage restrictions document for further information.
 *
 */

package com.esri.arcgis.android.samples.maprotation;

import android.app.Activity;
import android.os.Bundle;

import com.esri.android.map.MapView;
import com.esri.android.map.event.OnSingleTapListener;

/**
 * The Map Rotation sample app shows how to allow a user to rotate a map, and also shows a compass that displays the
 * current map rotation angle. The setAllowRotationByPinch method allows rotation of the map using a pinch gesture; the
 * current angle of rotation is then retrieved from the MapView using getRotationAngle. A custom View showing a compass
 * image is added to the map, which rotates itself in response to the OnPinchListener set on the MapView. An
 * OnSingleTapListener allows the map rotation angle to be reset to 0 by tapping on the map.
 */
public class MapRotation extends Activity {

  MapView mMapView = null;

  Compass mCompass;

  int mProgress;

  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    // Get a reference to the MapView.
    mMapView = (MapView) findViewById(R.id.map);

    // Set the MapView to allow the user to rotate the map when as part of a pinch gesture.
    mMapView.setAllowRotationByPinch(true);
    
    // Enabled wrap around map.
    mMapView.enableWrapAround(true);

    // Create the Compass custom view, and add it onto the MapView.
    mCompass = new Compass(this, null, mMapView);
    mMapView.addView(mCompass);

    // Set a single tap listener on the MapView.
    mMapView.setOnSingleTapListener(new OnSingleTapListener() {

      private static final long serialVersionUID = 1L;

      @Override
      public void onSingleTap(float x, float y) {

        // When a single tap gesture is received, reset the map to its default rotation angle,
        // where North is shown at the top of the device.
        mMapView.setRotationAngle(0);

        // Also reset the compass angle.
        mCompass.setRotationAngle(0);
      }
    });

  }

  @Override
  protected void onPause() {
    super.onPause();

    // Call MapView.pause to suspend map rendering while the activity is paused, which can save battery usage.
    mMapView.pause();
  }

  @Override
  protected void onResume() {
    super.onResume();

    // Call MapView.unpause to resume map rendering when the activity returns to the foreground.
    mMapView.unpause();
  }

}
