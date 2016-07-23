package com.madDucks.ulmap.appmadducks;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class UL_Map extends Activity implements SensorEventListener {

    private Bundle extras;
    private String destination;
    private String colour;
    private TextView infoBox;
    private ImageView mapOfULCampus;
    private ImageView ULCompass;

    private Intent callActivity;

    private GPS gps;
    private int gridX, gridY;
    private double latitude, longitude;
    private Double[] grids;
    private Point2D Loc;

    private float currentDegree = 0f; // record the compass picture angle turned
    private SensorManager mSensorManager; // device sensor manager

    // new Medical Science Building ?
    private final String[] ulBuildings = {

            "Kemmy Building",
            "CSIS Building",
            "Library Building",
            "Block A",
            "Block B",
            "Block C",
            "Block D",
            "Block E",
            "Sports Arena",
            "Schrodinger Building",
            "Foundation Building",
            "Students Union",
            "Engineering Building",
            "Languages Building",
            "Schumann Building",
            "Tierney Building",
            "Lonsdale Building",
            "PESS Building",
            "Health Sciences"
    };

    private final double[] buildingsUL = {

            52.672609, -8.577168,
            52.673887, -8.575575,
            52.673311, -8.573520,
            52.673887, -8.570028,
            52.673581, -8.570768,
            52.673571, -8.572088,
            52.673994, -8.571863,
            52.674336, -8.572018,
            52.673620, -8.565211,
            52.673812, -8.567394,
            52.674358, -8.573520,
            52.673174, -8.570307,
            52.675458, -8.573391,
            52.675614, -8.573472,
            52.673103, -8.577887,
            52.674489, -8.577152,
            52.673854, -8.568856,
            52.674690, -8.567869,
            52.677634, -8.568915
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ul__map);

        extras = getIntent().getExtras();
        infoBox = (TextView) findViewById(R.id.topBar);

        // declare and set ImageView
        mapOfULCampus = (ImageView) findViewById(R.id.campusMap);
        mapOfULCampus.setImageResource(R.drawable.ulcampusmap);

        // set Image of compass
        ULCompass = (ImageView) findViewById(R.id.CompassXML);

        // set Bundle as String destination
        destination = extras.getString("destination");

        // initialize your android device sensor capabilities
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);


        if (destination != null) {

            // Bundle received = "Show Destination" clicked in MainActivity

            // set Point2D Loc for destination
            setBuilding();
            colour = "Blue";

        } else {

            // null Bundle received = "View My Location" clicked in MainActivity

            // set lat/lon for users location
            setLocUser();
            colour = "Red";
        }

        // test lat/lon
        Toast.makeText(getApplicationContext(),
                latitude + " " + longitude,
                Toast.LENGTH_SHORT).show();
    }


    // set latitude and longitude for users location
    private void setLocUser() {

        // set GPS object
        gps = new GPS(UL_Map.this);
        latitude = gps.getLatitude();
        longitude = gps.getLongitude();

        // if on map and GSP works
        if (gps.checkGPS(gps).equals("You are on our UL campus map")) {

            // set TextView as location lat/lon
            infoBox.setText("The red dot marks your location at lat " + latitude
                    + ", lon " + longitude);
        } else {
            // no GPS or off our map - display message
            infoBox.setText(GPS.checkGPS(gps));
        }
    }


    // set latitude and longitude for destination
    private void setBuilding() {

        for (int i = 0; i < ulBuildings.length; i++)
            if (ulBuildings[i].equals(destination)) {
                latitude = buildingsUL[i * 2];
                longitude = buildingsUL[(i * 2) + 1];
            }

        // set TextView infoBox text
        infoBox.setText("The blue dot marks the " + destination + " lat " + latitude
                + ", lon " + longitude);
    }

    @Override // stops users from exiting the app by pressing back #annoying
    public void onBackPressed() {
        // super.onBackPressed(); <---

        // call MainActivity
        destination = null;
        callActivity = new Intent(UL_Map.this, MainActivity.class);
        callActivity.putExtra("destination", destination);
        startActivity(callActivity);
    }

    // sets gridX and gridY ints
    private void setXYcoords() {

        // luke
        grids = Loc.whatGrid();


        // get screen size
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;
        int dens = dm.densityDpi;

        double wi = (double) width / (double) dens;
        double hi = (double) height / (double) dens;

        // test screen size
        Toast.makeText(getApplicationContext(),
                latitude + " " + longitude,
                Toast.LENGTH_SHORT).show();

        // luke
        double x = Math.pow(wi, 2);
        double y = Math.pow(hi, 2);
        grids[1] = grids[1] / (0.00752 / x);
        grids[0] = grids[0] / (0.015 / y);
        double p = grids[0];
        double q = grids[1];

        gridX = (int) p;
        gridY = (int) q;

        // luke incorrect gridX, gridY values returned
        Toast.makeText(getApplicationContext(),
                "gridX = " + gridX + ", gridY = " + gridY,
                Toast.LENGTH_SHORT).show();

        // hack to place dot on map - u can remove
        gridX /= 100;
        gridY /= 100;
    }

    // draw a Red or Blue dot at co-ordinates gridX and gridY
    private void drawDotOnImageView(int gridX, int gridY, String colour) {

        // draw dot on ImageView
        BitmapFactory.Options myOptions = new BitmapFactory.Options();
        myOptions.inDither = true;
        myOptions.inScaled = false;
        myOptions.inPreferredConfig = Bitmap.Config.ARGB_8888; // important
        myOptions.inPurgeable = true;
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ulcampusmap, myOptions);
        Paint paint = new Paint();
        paint.setAntiAlias(true);

        if (colour.equals("Red"))
            paint.setColor(Color.RED); // user location
        else
            paint.setColor(Color.BLUE); // destination

        Bitmap workingBitmap = Bitmap.createBitmap(bitmap);
        Bitmap mutableBitmap = workingBitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(mutableBitmap);
        canvas.drawCircle(gridX, gridY, 18, paint);
        ImageView imageView = (ImageView) findViewById(R.id.campusMap);
        imageView.setAdjustViewBounds(true);
        imageView.setImageBitmap(mutableBitmap);
    }


    @Override
    public void onSensorChanged(SensorEvent event) {

        // get the angle around the z-axis rotated
        float degree = Math.round(event.values[0]);

        /* create a rotation animation

         public RotateAnimation (float fromDegrees, float toDegrees, int pivotXType, float pivotXValue, int pivotYType, float pivotYValue)

Parameters

(fromDegrees,   Rotation offset to apply at the start of the animation.
toDegrees,	    Rotation offset to apply at the end of the animation.
pivotXType,
        Specifies how pivotXValue should be interpreted.
        One of Animation.ABSOLUTE, Animation.RELATIVE_TO_SELF, or Animation.RELATIVE_TO_PARENT.
pivotXValue,
        The X coordinate of the point about which the object is being rotated, specified as an absolute number where 0 is the left edge.
        This value can either be an absolute number if pivotXType is ABSOLUTE, or a percentage (where 1.0 is 100%) otherwise.
pivotYType,
        Specifies how pivotYValue should be interpreted. One of Animation.ABSOLUTE, Animation.RELATIVE_TO_SELF, or Animation.RELATIVE_TO_PARENT.
pivotYValue)
        The Y coordinate of the point about which the object is being rotated, specified as an absolute number where 0 is the top edge.
        This value can either be an absolute number if pivotYType is ABSOLUTE, or a percentage (where 1.0 is 100%) otherwise.*/

        RotateAnimation ra = new RotateAnimation(
                currentDegree,
                        -degree,
                Animation.RELATIVE_TO_SELF,
                    0.5f,
                Animation.RELATIVE_TO_SELF,
                    0.5f);

        // how long the animation will take place (durationMillis)
        ra.setDuration(210);

        // set the animation after the end of the reservation status
        ra.setFillAfter(true);

        // start the animation
        ULCompass.startAnimation(ra);
        currentDegree = -degree;

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

        //no need
    }

    @Override // needs to be done for GPS also
    protected void onResume() {
        super.onResume();
        // for the system's orientation sensor registered listeners
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_GAME);

    }

    @Override // needs to be done for GPS also
    protected void onPause() {

        super.onPause();
        // to stop the listener and save battery
        mSensorManager.unregisterListener(this);

    }
}