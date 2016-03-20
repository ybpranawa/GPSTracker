package com.pranawa.gpstracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;


public class AndroidGPSTrackingActivity extends Activity {

    Button btnShowLocation;
    Button btnSendLocation;

    // GPSTracker class
    GPSTracker gps;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_android_gpstracking);

        btnShowLocation = (Button) findViewById(R.id.btnShowLocation);
        btnSendLocation = (Button) findViewById(R.id.btnSendLocation);

        // show location button click event
        btnShowLocation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // create class object
                gps = new GPSTracker(AndroidGPSTrackingActivity.this);

                // check if GPS enabled
                if(gps.canGetLocation()){

                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();
                    double acc=gps.getAccuracy();

                    // \n is for new line
                    Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude + "\nAcc: " + acc, Toast.LENGTH_SHORT).show();
                }else{
                    // can't get location
                    // GPS or Network is not enabled
                    // Ask user to enable GPS/network in settings
                    gps.showSettingsAlert();
                }

            }
        });

        btnSendLocation.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                gps = new GPSTracker(AndroidGPSTrackingActivity.this);
                try {
                    URL url = new URL("http://localhost/gpstracker");
                    HttpURLConnection urlConnection=(HttpURLConnection) url.openConnection();
                    urlConnection.setDoOutput(true);
                    urlConnection.setChunkedStreamingMode(0);

                    if (gps.canGetLocation()){
                        double latitude = gps.getLatitude();
                        double longitude = gps.getLongitude();
                        double acc=gps.getAccuracy();

                        //send it to server

                        OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());

                    }else{
                        gps.showSettingsAlert();
                    }
                    urlConnection.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

}
