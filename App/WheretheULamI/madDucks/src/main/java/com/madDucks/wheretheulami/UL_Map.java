package com.madDucks.wheretheulami;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class UL_Map extends Activity {


    private String destination = null;

    private GPS gps;
    private Intent callMainActivity;

    private ImageView view;

    private Bundle extras;
    private TextView infoBox;

    private double latitude, longitude;
    private int gridX, gridY;

    private Point2D myPoint;

    public UL_Map()
    {

    }

    @Override    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ul_map);

        extras = getIntent().getExtras();
        infoBox = (TextView) findViewById(R.id.infoBox);

        // load ImageView
        view = (ImageView) findViewById(R.id.campusmap);
        view.setImageResource(R.drawable.ulcampusmap);

        destination = extras.getString("destination");

        if (destination!=null) {
            // Bundle received = Show Destination clicked in MainActivity

            // set TextView infoBox text
            infoBox.setText("Show " + destination);

            // set int gridX int gridY
            //getGridRef();

            // draw blue dot and centre view on destination
            drawDotOnImageView(gridX, gridY, "Blue");
        }
        else {
            // no Bundle received = View My Location clicked in MainActivity
            myLocation(view);
        }
    }

    // if click View My Location button - linked via android:onClick="myLocation" /xml
    public void myLocation(View view) {
        gps = new GPS(UL_Map.this);

        // if on map and GSP works
        if (GPS.checkGPS(gps).equals("You are on our UL campus map")) {
            // set TextView as location lat/lon
            infoBox.setText("View Location " + String.valueOf(gps.getLatitude()
                    + " | " + String.valueOf(gps.getLongitude())));

            drawDotOnImageView(gridX, gridY,"Red");

        } else
            // no GPS or off our map - Toast message
            infoBox.setText(GPS.checkGPS(gps));
    }

    // if click TextView infoBox - see android:onClick="infoBox" /xml
    public void infoBox(View view) {
        // infoBox starts with S for Show Destination
        if(infoBox.getText().toString().contains("Show"))
            showDestination();
        else if(infoBox.getText().toString().contains("View"))
            // infoBox starts with S for View lat/lon
            myLocation(view);
        else
            Toast.makeText(getApplicationContext(),
                    "error",
                    Toast.LENGTH_SHORT).show();;
    }

    private void showDestination() {
        // todo jump to destination

        double[] grid = new double[2];
        grid = myPoint.whatGrid();
        double b = grid[0];
        double c = grid[1];
        float y = (float)c;
        y = Math.abs(y);
        float x = (float)b;

        // draw dot on ImageView
        String colour = "";
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
        canvas.drawCircle(x, y, 12, paint);
        ImageView imageView = (ImageView) findViewById(R.id.campusmap);
        imageView.setAdjustViewBounds(true);
        imageView.setImageBitmap(mutableBitmap);

    }

    // if click Change Destination button - linked via android:onClick="changeDestination" /xml
    public void changeDestination(View view) {
        // call MainActivity to change Destination
        callMainActivity = new Intent(UL_Map.this, MainActivity.class);
        startActivity(callMainActivity);
    }

    private void drawDotOnImageView(int gridX, int gridY, String colour) {

        // convert gridX and gridY to dp from (0,0) = TopLeft
        // hard-coding dp centre of gridX. gridY

            // increase this = moves dot right   TopLeft = 0
            // increase this = moves dot down    TopLeft = 0
        double[] grid = new double[2];
        grid = myPoint.whatGrid();
        double b = grid[0];
        double c = grid[1];
        float y = (float)c;
        Math.abs(y);
        float x = (float)b;

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
        canvas.drawCircle(x, y, 12, paint);
        ImageView imageView = (ImageView) findViewById(R.id.campusmap);
        imageView.setAdjustViewBounds(true);
        imageView.setImageBitmap(mutableBitmap);


        ////////////////////////////////////////////////////////////////////////////

        // TODO CENTRE MAP ON USERS LOCATION
        ;

        ////////////////////////////////////////////////////////////////////////////

    }

    private void getGridRef() {

        // convert String destination into double lat/lon
        // must correspond with values in String[] ulBuildings MainActivity

        final String[] ulBuildings = {"Kemmy Building", "CSIS Building", "Library Building",
                "Block A", "Block B", "Block C", "Block D", "Block E", "Sports Arena", "Schrodinger Building",
                "Foundation Building", "Students Union", "Engineering Building", "Languages Building",
                "Schumann Building", "Tierney Building", "Lonsdale Building", "PESS Building",
                "Health Sciences"};

        int building = 0;

        for (int i = 0 ; i < ulBuildings.length ; i++)
            if(destination.equals(ulBuildings[i]))
                building = i;

        switch (building) {

            case 0:
                // Kemmy Building
                latitude = 52.672609;
                longitude = -8.577168;
                break;

            case 1:
                // CSIS Building
                latitude = 52.673887;
                longitude = -8.575575;
                break;

            case 2:
                // Library Building
                latitude = 52.673311;
                longitude = -8.57352;
                break;

            case 3:
                // Block A
                latitude = 52.673887;
                longitude = -8.570028;
                break;

            case 4:
                // Block B
                latitude = 52.673581;
                longitude = -8.570768;
                break;

            case 5:
                // Block C
                latitude = 52.673571;
                longitude = -8.572088;
                break;

            case 6:
                // Block D
                latitude = 52.673994;
                longitude = -8.571863;
                break;

            case 7:
                // Block E
                latitude = 52.674336;
                longitude = -8.572018;
                break;

            case 8:
                // Sports Arena
                latitude = 52.67362;
                longitude = -8.565211;
                break;

            case 9:
                // Schrodinger Building
                latitude = 52.673812;
                longitude = -8.567394;
                break;

            case 10:
                // Foundation Building
                latitude = 52.674358;
                longitude = -8.57352;
                break;

            case 11:
                // Students Union
                latitude = 52.673174;
                longitude = -8.570307;
                break;

            case 12:
                // Engineering Building
                latitude = 52.675458;
                longitude = -8.573391;
                break;

            case 13:
                // Languages Building
                latitude = 52.675614;
                longitude = -8.573472;
                break;

            case 14:
                // Schumann Building
                latitude = 52.673103;
                longitude = -8.577887;
                break;

            case 15:
                // Tierney Building
                latitude = 52.674489;
                longitude = -8.577152;
                break;

            case 16:
                // Lonsdale Building
                latitude = 52.673854;
                longitude = -8.568856;
                break;

            case 17:
                // PESS Building
                latitude = 52.67469;
                longitude = -8.567869;
                break;

            case 18:
                // Health Sciences
                latitude = 52.677634;
                longitude = -8.568915;
                break;

            default:
                // nothing found
                //TODO issue error message, prevent null crash
                ;
                break;
        }

        // call whatGrid method
        whatGrid(latitude, longitude);
    }

    private void whatGrid(double latitude, double longitude)
    {
        // void method sets global variables as int gridX, gridY
        // gridX, gridY hardcoded in drawDotOnImageView method
    }
}
