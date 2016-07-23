package com.madDucks.ulmap.appmadducks;

import android.app.Activity;
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
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class UL_Map extends Activity implements SensorEventListener {

    private static String destination;
    private String colour = "Blue";
    private Bundle extras;

    private TextView infoBox;
    private ImageView mapOfULCampus;
    private Intent callActivity;

    private GPS gps;
    private boolean gpsWorks;
    private double destinationLatitude, destinationLongitude;
    private double usersLatitude, usersLongitude;
    private int x, y;
    private double xDistanceY;
    private int howFar, redX, redY, blueX, blueY;

    private BitmapFactory.Options myOptions = new BitmapFactory.Options();
    private Bitmap bitmap, workingBitmap, mutableBitmap;
    private Paint paint = new Paint();
    private Canvas canvas;

    // define the display assembly compass picture
    private ImageView ulcompass;

    // record the compass picture angle turned
    private float currentDegree = 0f;

    // device sensor manager
    private SensorManager mSensorManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ul__map);

        extras = getIntent().getExtras();
        infoBox = (TextView) findViewById(R.id.topBar);

        // initialize your android device sensor capabilities
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        // set Bundle as String destination
        destination = extras.getString("destination");

        // set lat/lon for destination
        for (int i = 0; i < ulBuildings.length; i++)
            if (ulBuildings[i].equals(destination)) {
                // set lat/lon for destination
                destinationLatitude = buildingsUL[i * 2];
                destinationLongitude = buildingsUL[(i * 2) + 1];
            }

        // Toast "Blue dot : " + destination
        Toast.makeText(getApplicationContext(), getString(R.string.BlueDot) + " " + destination, Toast.LENGTH_SHORT).show();


        // settings for drawing either 1 or 2 dots
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ulcampusmap, myOptions);
        myOptions.inDither = true;
        myOptions.inScaled = false;
        myOptions.inPreferredConfig = Bitmap.Config.ARGB_8888; // important
        myOptions.inPurgeable = true;
        paint.setAntiAlias(true);
        workingBitmap = Bitmap.createBitmap(bitmap);
        mutableBitmap = workingBitmap.copy(Bitmap.Config.ARGB_8888, true);

        // method extracted to facilitate onResume()
        prepareDots();

        // end onCreate(Bundle savedInstanceState)
    }

    private void prepareDots() {

        // extracted to separate method to facilitate onResume()

        // declare and set ImageView for UL Map
        mapOfULCampus = (ImageView) findViewById(R.id.campusMap);
        mapOfULCampus.setImageResource(R.drawable.ulcampusmap);

        // declare and set ImageView for compass
        ulcompass = (ImageView) findViewById(R.id.compassXML);

        // get clean canvas to attach to ImageView
        canvas = new Canvas(mutableBitmap);

        // set xy and draw blue dot for destination
        set_XY_and_Draw_Dot();

        // set xy and draw red dot for user if applicable
        setUserLatLon();

        if (colour.equals("Red")) {

            // calculate distance between user and destination
            distance();
            xDistanceY = xDistanceY * 1000;
            howFar = (int) xDistanceY;
            infoBox.setText(getString(R.string.distancexy) + " " + destination + " = " +
                    howFar
                    + " " + getString(R.string.meters));

            Paint paint1 = new Paint();
            paint1.setColor(Color.YELLOW);
            paint1.setStrokeWidth(5);
            paint1.setStyle(Paint.Style.STROKE);
            canvas.drawLine(redX, redY, blueX, blueY, paint1);
        }

        // attach bitmap to ImageView
        mapOfULCampus.setAdjustViewBounds(true);
        mapOfULCampus.setImageBitmap(mutableBitmap);
    }

    // set latitude and longitude for users location
    private void setUserLatLon() {

        // set GPS object and get lat/lon
        gps = new GPS(UL_Map.this);

        // does GPS work - set boolean
        gpsWorks = gps.canGetLocation();

        // GSP works boolean set in thread
        if (gpsWorks) {

            // set lat/lon for user
            usersLatitude = gps.getLatitude();
            usersLongitude = gps.getLongitude();

            // stop gps
            gps.stopUsingGPS();

            // if on our UL map
            if (usersLatitude >= 52.67153000 && usersLatitude <= 52.679050) {
                if (usersLongitude >= -8.5789000 && usersLongitude <= -8.56390) {

                    // append to TextView infoBoc @string/onmap
                    // "Red dot = Your location"
                    Toast.makeText(getApplicationContext(), getString(R.string.onmap), Toast.LENGTH_SHORT).show();

                    // set xy, draw red dot
                    colour = "Red";
                    set_XY_and_Draw_Dot();
                }
            } else {

                // append to TextView infoBoc @string/offmap
                // "You are not on our UL campus map"
                infoBox.setText(getString(R.string.offmap));
            }
        } else {
            // append to TextView infoBoc @string/nogps
            // "GPS service is not available"
            infoBox.setText(getString(R.string.nogps));
        }

        // end setUserLatLon()
    }

    // sets x and y pixel coordinates and draws dot
    private void set_XY_and_Draw_Dot() {

        // for switch
        double lat, lon;
        int radius;

        // switch here between destination / user
        if (colour.equals("Blue")) {
            lat = destinationLatitude;
            lon = destinationLongitude;
            paint.setColor(Color.BLUE);
            radius = 20;
        } else {
            lat = usersLatitude;
            lon = usersLongitude;
            paint.setColor(Color.RED);
            radius = 15;
        }

        // MATH : find x,y coordinates for dot
        //////////////////////////////////////////////////////////////////////////////////

        // lat top to Bottom && lon left to right
        double latSpan = 52.67905 - 52.67153; // = 0.00752 lat high y
        double lonSpan = 8.578900 - 8.563900; // = 0.01500 lon wide x

        // how far are lat and lon from topLeft 0,0
        double latFrom_TopDown = 52.67905 - lat;      // lat high y
        double lonFrom_LeftAcross = 8.578900 + lon;  // lon wide x

        // what proportion of the map does these represent
        double proportionalChangeY = latFrom_TopDown / latSpan;   // lat high Y
        double proportionalChangeX = lonFrom_LeftAcross / lonSpan; // lon wide x

        // translate them into pixels
        y = ((int) (825 * proportionalChangeY)); // height
        x = ((int) (1000 * proportionalChangeX)); // width

        //////////////////////////////////////////////////////////////////////////////////
        // END MATH

        // record xy for drawLine()
        if (colour.equals("Blue")) {
            blueX = x;
            blueY = y;
        } else if (colour.equals("Red")) {
            redX = x;
            redY = y;
        }

        // draw a blue or red dot on canvas
        canvas.drawCircle(x, y, radius, paint);

        // end set_XY_and_Draw_Dot()
    }

    // if click "Select Destination" button
    // see android:onClick="selectDestination" /xml
    public void selectDestination(View view) {

        // call UL_Map Activity
        onBackPressed();

        // end selectDestination(View view)
    }

    // if click "Refresh Location" button
    // see android:onClick="refreshLocation" /xml
    public void refreshLocation(View view) {

        // get fresh users GPS location and draw new dots
        callActivity = new Intent(UL_Map.this, UL_Map.class);
        callActivity.putExtra("destination", destination);
        finish();
        startActivity(callActivity);

        // end selectDestination(View view)
    }

    @Override
    public void onBackPressed() {

        // overrides back button on users hardware
        //  call MainActivity

        callActivity = new Intent(UL_Map.this, MainActivity.class);
        callActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        callActivity.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        callActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(callActivity);

        // end onBackPressed()
    }

    @Override
    protected void onResume() {
        super.onResume();

        // for the system's orientation sensor registered listeners
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),

                SensorManager.SENSOR_DELAY_GAME);

    }

    @Override
    protected void onPause() {

        super.onPause();

        // to stop the listener and save battery
        mSensorManager.unregisterListener(this);

    }


    // calculates the distance between user location and destination
    protected void distance() {

                xDistanceY = Math.sin(deg2rad(destinationLatitude)) * Math.sin(deg2rad(usersLatitude)) + Math.cos(deg2rad(destinationLatitude)) * Math.cos(deg2rad(usersLatitude)) * Math.cos(deg2rad(destinationLongitude - usersLongitude));

                xDistanceY = Math.acos(xDistanceY);
                xDistanceY = rad2deg(xDistanceY);
                xDistanceY = xDistanceY * 60 * 1.1515;
                xDistanceY = xDistanceY * 1.609344;



        // end distance()
    }

    private double deg2rad(double deg) {

        return (deg * Math.PI / 180.0);

        // end deg2rad
    }

    private double rad2deg(double rad) {

        return (rad * 180 / Math.PI);

    // end rad2deg
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        // get the angle around the z-axis rotated
        float degree = Math.round(event.values[0]);


        // create a rotation animation (reverse turn degree degrees)
        RotateAnimation ra = new RotateAnimation(
                currentDegree,
                -degree,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f);

        // how long the animation will take place(Millis)
        ra.setDuration(210);

        // set the animation after the end of the reservation status
        ra.setFillAfter(true);

        // Start the animation
        ulcompass.startAnimation(ra);
        currentDegree = -degree;

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

        // not used
    }

    ///////////////////////////////////////////////////////////
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


// end class
}
