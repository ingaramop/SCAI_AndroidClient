package com.example.paul.scai_androidclient;

import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class MainScreenActivity extends AppCompatActivity {
    final Handler myHandler = new Handler();
    final static int GUI_UPDATE_INTERVAL = 300;
    SCAICore scaiCore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();// hides top action bar
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);// set fixed landscape orientation
        setContentView(R.layout.activity_main_screen);

        scaiCore = new SCAICore();
        scaiCore.start();

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                myHandler.post(updateGUI);
            }
        }, 0,GUI_UPDATE_INTERVAL);


    }

    final Runnable updateGUI = new Runnable() {
        public void run() {
            TextView auxTextView;
            auxTextView = (TextView) findViewById(R.id.tipperValue);
            auxTextView.setText(scaiCore.getTipperInclination());
            auxTextView = (TextView) findViewById(R.id.speedValue);
            auxTextView.setText(scaiCore.getSpeed());
            auxTextView = (TextView) findViewById(R.id.compassValue);
            auxTextView.setText(scaiCore.getCompass());
            auxTextView = (TextView) findViewById(R.id.altitudeValue);
            auxTextView.setText(scaiCore.getAltitude());
            auxTextView = (TextView) findViewById(R.id.temperatureValue);
            auxTextView.setText(scaiCore.getTemperature());
            auxTextView = (TextView) findViewById(R.id.timeValue);
            auxTextView.setText(scaiCore.getTime());
            auxTextView = (TextView) findViewById(R.id.dateValue);
            auxTextView.setText(scaiCore.getDate());
        }
    };
}
