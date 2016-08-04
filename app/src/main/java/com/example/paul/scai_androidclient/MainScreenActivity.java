package com.example.paul.scai_androidclient;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.github.niqdev.mjpeg.DisplayMode;
import com.github.niqdev.mjpeg.Mjpeg;
import com.github.niqdev.mjpeg.MjpegSurfaceView;
import com.github.niqdev.mjpeg.MjpegView;

import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.MinimapOverlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainScreenActivity extends AppCompatActivity {
    final Handler myHandler = new Handler();
    //final private static String VIDEO_ADDRESS = "rtsp://192.168.1.10:554/user=admin_password=tlJwpbo6_channel=1_stream=0.sdp?real_stream";


    SCAICore scaiCore;// Main system class that handles connections

    VideoPlayer videoPlayer;

    Map map;
    FrameLayout mapLayout;

    //settings menu
    private ImageButton settingsButton;// settings button on top right

    //about menu

    private ImageButton aboutButton;// about button on top right.


    private Switch mapCamSwitch;//switch that toggles camera and map views




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();// hides top action bar
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);// set fixed landscape orientation
        setContentView(R.layout.activity_main_screen);

        scaiCore = new SCAICore();// instantiate scai core object
        scaiCore.start();// start running http, timer and other connections
        GlobalParams.loadPreferences(this);


        mapLayout = (FrameLayout) findViewById(R.id.mapLayout);
        map = Map.newInstance();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.mapLayout, map, "Map")
                .commit();
        mapLayout.setVisibility(View.INVISIBLE);
        ////////CAMERA VIEW INITIALIZATION////////////
        videoPlayer = VideoPlayer.newInstance();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.videoLayout, videoPlayer, "Video")
                .commit();

        ////////MAP-CAM TOGGLE SWITCH INITIALIZATION////////////
        mapCamSwitch = (Switch) findViewById(R.id.mapCamSwitch);//Get a reference for map/camera selector switch
        mapCamSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {//define change Listener action
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){//if it is checked, hides cam and shows map
                    mapLayout.setVisibility(View.VISIBLE);
                    //mapContainer.setVisibility(View.VISIBL
                    videoPlayer.stop();

                    //videoContainer.setVisibility(View.INVISIBLE);
                }else{//if it is unchecked, does the oposite
                    mapLayout.setVisibility(View.INVISIBLE);
                    videoPlayer = VideoPlayer.newInstance();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .add(R.id.videoLayout, videoPlayer, "Video")
                            .commit();
                    //videoContainer.setVisibility(View.VISIBLE);
                    ////////CAMERA VIEW INITIALIZATION////////////
                    //loadIpCam();

                    //mapContainer.setVisibility(View.GONE);
                }
            }
        });

        ////////SETTINGS BUTTON INITIALIZATION////////////
        settingsButton = (ImageButton) findViewById(R.id.configButton);// get reference for the config button
        settingsButton.setOnClickListener(new View.OnClickListener() {// Its action makes visible the configuration menu
            public void onClick(View v) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.popUpMenu, Settings.newInstance(scaiCore), "Settings")
                        .commit();
            }
        });

        ////////ABOUT BUTTON INITIALIZATION////////////
        aboutButton = (ImageButton) findViewById(R.id.aboutButton);// get reference for the config button
        aboutButton.setOnClickListener(new View.OnClickListener() {// Its action makes visible the configuration menu
            public void onClick(View v) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.popUpMenu, About.newInstance(), "About")
                        .commit();
            }
        });



        //////// GUI UPDATE TIMERS INITIALIZATION////////////
        new Timer().scheduleAtFixedRate(new TimerTask() { //update screen information every GUI_TEXT_UPDATE_INTERVAL milliseconds
            @Override
            public void run() {
                myHandler.post(updateGUITextAndStatusIcons);
            }
        }, 0, GlobalParams.GUI_TEXT_UPDATE_INTERVAL);

        new Timer().scheduleAtFixedRate(new TimerTask() { //update screen roll animation every GUI_ROLL_ANIMATION_UPDATE_INTERVAL milliseconds
            @Override
            public void run() {
                myHandler.post(updateGUIRollAnimation);
            }
        }, 0, GlobalParams.GUI_ROLL_ANIMATION_UPDATE_INTERVAL);

        new Timer().scheduleAtFixedRate(new TimerTask() { //update screen compass animation every GUI_ROLL_ANIMATION_UPDATE_INTERVAL milliseconds
            @Override
            public void run() {
                myHandler.post(updateGUICompassAnimation);
            }
        }, 0, GlobalParams.GUI_COMPASS_ANIMATION_UPDATE_INTERVAL);

        new Timer().scheduleAtFixedRate(new TimerTask() { //update screen compass animation every GUI_ROLL_ANIMATION_UPDATE_INTERVAL milliseconds
            @Override
            public void run() {
                myHandler.post(updateGUITipperAnimation);
            }
        }, 0, GlobalParams.GUI_TIPPER_ANIMATION_UPDATE_INTERVAL);

        new Timer().scheduleAtFixedRate(new TimerTask() { //update screen compass animation every GUI_ROLL_ANIMATION_UPDATE_INTERVAL milliseconds
            @Override
            public void run() {
                myHandler.post(updateMAPLocation);
            }
        }, 0, GlobalParams.MAP_LOCATION_UPDATE_INTERVAL);

        new Timer().scheduleAtFixedRate(new TimerTask() { //update screen compass animation every GUI_ROLL_ANIMATION_UPDATE_INTERVAL milliseconds
            @Override
            public void run() {
                myHandler.post(beepIfAlerts);
            }
        }, 0, GlobalParams.BEEP_ALERT_INTERVAL);


    }

    final Runnable updateMAPLocation= new Runnable() {
        public void run() {
            if (scaiCore.getPositionX() != GlobalParams.INVALID_FLOAT_VALUE && scaiCore.getPositionY() != GlobalParams.INVALID_FLOAT_VALUE &&
                    scaiCore.getPositionX() != GlobalParams.INVALID_FLOAT_VALUE ) {// if values are valid...

                try{
                    if (map!= null && map.isRunning()) {
                        GeoPoint currentPosition = new GeoPoint(scaiCore.getPositionY(), scaiCore.getPositionX());
                        map.updatePosition(currentPosition);
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }

            }
        }
    };



    final Runnable updateGUITextAndStatusIcons = new Runnable() {
        public void run() {

            //set UI text values
            //IMU VALUES//
            TextView auxTextView;

            auxTextView = (TextView) findViewById(R.id.tipperValue);
            if (scaiCore.getTipperInclination() != GlobalParams.INVALID_INT_VALUE) {
                auxTextView.setText(scaiCore.getTipperInclination() + "°");
                if (scaiCore.getSpeed() > GlobalParams.MAX_SPEED_WITH_TIPPER_UP &&
                        scaiCore.getTipperInclination()>GlobalParams.MIN_TIPPER_UP_ANGLE) {
                    auxTextView.setTextColor(Color.parseColor("#FFFFFF"));
                    LinearLayout layoutAux = (LinearLayout) findViewById(R.id.tipperLayout);
                    layoutAux.setBackgroundColor(Color.parseColor("#FC331F"));

                } else {
                    auxTextView.setTextColor(Color.parseColor("#424242"));
                    LinearLayout layoutAux = (LinearLayout) findViewById(R.id.tipperLayout);
                    layoutAux.setBackgroundColor(Color.parseColor("#FFFFFF"));
                }
            }
            else auxTextView.setText("?");

            auxTextView = (TextView) findViewById(R.id.rollValue);
            if (scaiCore.getSideInclination() != GlobalParams.INVALID_INT_VALUE)
                auxTextView.setText(scaiCore.getSideInclination() +"°");
            else auxTextView.setText("?");

            auxTextView = (TextView) findViewById(R.id.altitudeValue);
            if (scaiCore.getAltitude() != GlobalParams.INVALID_INT_VALUE)
                auxTextView.setText(scaiCore.getAltitude() +"");
            else auxTextView.setText("?");

            auxTextView = (TextView) findViewById(R.id.temperatureValue);
            if (scaiCore.getTemperature() != GlobalParams.INVALID_FLOAT_VALUE)
                auxTextView.setText(scaiCore.getTemperature() +"");
            else auxTextView.setText("?");

            auxTextView = (TextView) findViewById(R.id.pressureValue);
            if (scaiCore.getPressure() != GlobalParams.INVALID_FLOAT_VALUE)
                auxTextView.setText(Math.round(scaiCore.getPressure()) +"");
            else auxTextView.setText("?");
            //GPS VALUES//
            auxTextView = (TextView) findViewById(R.id.speedValue);
            if (scaiCore.getSpeed() != GlobalParams.INVALID_INT_VALUE) {
                auxTextView.setText(scaiCore.getSpeed() + "");
                if (scaiCore.getSpeed() > GlobalParams.SPEED_LIMIT) {
                    auxTextView.setTextColor(Color.parseColor("#FFFFFF"));
                    LinearLayout layoutAux = (LinearLayout) findViewById(R.id.speedLayout);
                    layoutAux.setBackgroundColor(Color.parseColor("#FC331F"));

                } else {
                    auxTextView.setTextColor(Color.parseColor("#424242"));
                    LinearLayout layoutAux = (LinearLayout) findViewById(R.id.speedLayout);
                    layoutAux.setBackgroundColor(Color.parseColor("#FFFFFF"));
                }
            }
            else auxTextView.setText("?");
            //SYSTEM DATE VALUES//
            auxTextView = (TextView) findViewById(R.id.dateValue);
            auxTextView.setText(scaiCore.getDate());

            //update status icons color
            ImageView auxImageView;

            //gps status icon
            auxImageView = (ImageView) findViewById(R.id.gpsStatusView);
            if(scaiCore.getGpsErrorsInARow()==0){
                auxImageView.setImageDrawable(getResources().getDrawable(getResources().getIdentifier("@drawable/gps_green", null, getPackageName())));
            }
            else{
                auxImageView.setImageDrawable(getResources().getDrawable(getResources().getIdentifier("@drawable/gps_red", null, getPackageName())));
            }

            //gps status icon
            auxImageView = (ImageView) findViewById(R.id.imuStatusView);
            if(scaiCore.getImuErrorsInARow()==0){
                auxImageView.setImageDrawable(getResources().getDrawable(getResources().getIdentifier("@drawable/imu_green", null, getPackageName())));
            }
            else{
                auxImageView.setImageDrawable(getResources().getDrawable(getResources().getIdentifier("@drawable/imu_red", null, getPackageName())));
            }
        }
    };


    final Runnable updateGUIRollAnimation = new Runnable() {
        public void run() {

            ///ROLL ANIMATION///////
            if(scaiCore.getSideInclinationOld()!= GlobalParams.INVALID_INT_VALUE &&
                    scaiCore.getSideInclination()!= GlobalParams.INVALID_INT_VALUE) {// execute only if values are valid

                int aux = scaiCore.getSideInclination();
                int auxOld = scaiCore.getSideInclinationOld();

                if(aux<-45) aux =-45;// do not allow an inclination over 0
                if(aux>45) aux =45; // do not allow an inclination lower bellow -50
                if(auxOld<-45) auxOld =-45;// do not allow an inclination over 0
                if(auxOld>45) auxOld =45; // do not allow an inclination lower bellow -50

                ImageView imageview = (ImageView) findViewById(R.id.truckFrontView);
                RotateAnimation rotate = new RotateAnimation(
                        auxOld, aux,Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                rotate.setDuration(GlobalParams.GUI_ROLL_ANIMATION_UPDATE_INTERVAL);
                imageview.startAnimation(rotate);
                scaiCore.setSideInclinationOld(scaiCore.getSideInclination());
            }
        }
    };

    final Runnable updateGUICompassAnimation = new Runnable() {
        public void run() {

            ///COMPASS ANIMATION///////
            if(scaiCore.getCompassOld()!= GlobalParams.INVALID_INT_VALUE && scaiCore.getCompass() != GlobalParams.INVALID_INT_VALUE) {// execute only if values are valid

                ImageView imageview2 = (ImageView) findViewById(R.id.compassArrow);
                RotateAnimation rotate2 = new RotateAnimation(scaiCore.getCompassOld(), scaiCore.getCompass(),
                        Animation.RELATIVE_TO_SELF, 0.54f, Animation.RELATIVE_TO_SELF, 0.5f);
                rotate2.setDuration(GlobalParams.GUI_COMPASS_ANIMATION_UPDATE_INTERVAL);
                imageview2.startAnimation(rotate2);
                scaiCore.setCompassOld(scaiCore.getCompass());
            }
        }
    };

    final Runnable updateGUITipperAnimation = new Runnable() {
        public void run() {
            int aux = scaiCore.getTipperInclination();
            int auxOld = scaiCore.getTipperInclinationOld();

            if(aux<0) aux =0;// do not allow an inclination over 0
            if(aux>50) aux =50; // do not allow an inclination lower bellow -50
            if(auxOld<0) auxOld =0;// do not allow an inclination over 0
            if(auxOld>50) auxOld =50; // do not allow an inclination lower bellow -50
            ///COMPASS ANIMATION///////
            if(scaiCore.getTipperInclinationOld()!= GlobalParams.INVALID_INT_VALUE &&
                    scaiCore.getTipperInclination()!= GlobalParams.INVALID_INT_VALUE ) {// execute only if values != null and !="?"
                ImageView imageview = (ImageView) findViewById(R.id.tipper);
                RotateAnimation rotate = new RotateAnimation(
                        (-1)*auxOld, (-1)*aux, Animation.RELATIVE_TO_SELF, 0.2f, Animation.RELATIVE_TO_SELF, 0.7f);
                rotate.setDuration(GlobalParams.GUI_TIPPER_ANIMATION_UPDATE_INTERVAL);
                imageview.startAnimation(rotate);
                scaiCore.setTipperInclinationOld(scaiCore.getTipperInclination());
            }
        }
    };

    final Runnable beepIfAlerts = new Runnable() {
        public void run() {
            boolean beep = false;

            if (scaiCore.getSpeed() != GlobalParams.INVALID_INT_VALUE)
                if (scaiCore.getSpeed()>GlobalParams.SPEED_LIMIT && GlobalParams.SPEED_LIMIT_BEEP_ENABLED) beep = true;

            if (scaiCore.getSpeed() != GlobalParams.INVALID_INT_VALUE && scaiCore.getTipperInclination() != GlobalParams.INVALID_INT_VALUE )
                if (scaiCore.getSpeed()>GlobalParams.MAX_SPEED_WITH_TIPPER_UP &&
                        scaiCore.getTipperInclination()>GlobalParams.MIN_TIPPER_UP_ANGLE &&
                        GlobalParams.TIPPER_UP_BEEP_ENABLED) beep = true;

            if (beep) {

                try {
                    ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 30);
                    toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 50);
                } catch (Exception e) {
                }
            }
        }
    };



}
