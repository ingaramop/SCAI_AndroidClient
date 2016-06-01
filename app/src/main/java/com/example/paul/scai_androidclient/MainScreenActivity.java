package com.example.paul.scai_androidclient;

import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.VideoView;

import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import java.util.Timer;
import java.util.TimerTask;

public class MainScreenActivity extends AppCompatActivity {
    final Handler myHandler = new Handler();
    final static int GUI_TEXT_UPDATE_INTERVAL = 500;
    final static int GUI_ROLL_ANIMATION_UPDATE_INTERVAL = 600;
    final static int GUI_COMPASS_ANIMATION_UPDATE_INTERVAL = 650;
    final static int GUI_TIPPER_ANIMATION_UPDATE_INTERVAL = 700;
    final private static String VIDEO_ADDRESS = "rtsp://wowzaec2demo.streamlock.net/vod/mp4:BigBuckBunny_115k.mov";

    SCAICore scaiCore;// Main system class that handles connections
    private MapView map;// map view
    VideoView cam;//Camera view
    private RelativeLayout settings;// settings menu

    private ImageButton settingsButton;// settings button on top right.
    private Button applySettingsButton;// apply settings button
    private Button cancelSettingsButton;// cancel settings button

    private Switch mapCamSwitch;//switch that toggles camera and map views




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();// hides top action bar
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);// set fixed landscape orientation
        setContentView(R.layout.activity_main_screen);

/*        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);*/

        scaiCore = new SCAICore();// instantiate scai core object
        scaiCore.start();// start running http, timer and other connections


        ////////CAMERA VIEW INITIALIZATION////////////
        cam = (VideoView)findViewById(R.id.myVideo);// Get a reverence for the videoview
        String vidAddress = VIDEO_ADDRESS;//configure URL
        Uri vidUri = Uri.parse(vidAddress);
        cam.setVideoURI(vidUri);
        cam.start();//Start stream

        ////////MAP VIEW INITIALIZATION////////////
        map = (MapView) findViewById(R.id.map);// Get a reference for the map view
        map.setTileSource(TileSourceFactory.MAPQUESTOSM);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
        IMapController mapController = map.getController();
        GeoPoint startPoint = new GeoPoint(-31.435253, -64.193881);
        mapController.setCenter(startPoint);
        map.setTileSource(new XYTileSource("MapQuest", 0, 18, 256, ".jpg", new String[] {
                "http://otile1.mqcdn.com/tiles/1.0.0/map/",
                "http://otile2.mqcdn.com/tiles/1.0.0/map/",
                "http://otile3.mqcdn.com/tiles/1.0.0/map/",
                "http://otile4.mqcdn.com/tiles/1.0.0/map/"}));
        mapController.setZoom(17);
        map.setUseDataConnection(false); //keeps the mapView from loading online tiles using network connection
        map.setVisibility(View.GONE);// starts invisible

        ////////MAP-CAM TOGGLE SWITCH INITIALIZATION////////////
        mapCamSwitch = (Switch) findViewById(R.id.mapCamSwitch);//Get a reference for map/camera selector switch
        mapCamSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {//define change Listener action
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){//if it is cheched, hides cam and shows map
                    map.setVisibility(View.VISIBLE);
                    cam.stopPlayback();
                    cam.setVisibility(View.INVISIBLE);
                }else{//if it is unchecked, does the oposite
                    cam.setVisibility(View.VISIBLE);
                    cam.start();
                    map.setVisibility(View.GONE);
                }
            }
        });

        ////////CONFIG BUTTONS INITIALIZATION////////////
        settingsButton = (ImageButton) findViewById(R.id.configButton);// get reference for the config button
        settingsButton.setOnClickListener(new View.OnClickListener() {// Its action makes visible the configuration menu
            public void onClick(View v) {
                settings.setVisibility(View.VISIBLE);
            }
        });
        applySettingsButton = (Button) findViewById(R.id.configApply);// get reference for the applyconfig button
        applySettingsButton.setOnClickListener(new View.OnClickListener() {// Its action hides the config menu (more to be added)
            public void onClick(View v) {
                settings.setVisibility(View.INVISIBLE);
            }
        });
        cancelSettingsButton = (Button) findViewById(R.id.configCancel);// get reference for the cancelconfig button
        cancelSettingsButton.setOnClickListener(new View.OnClickListener() {// Its action hides the config menu
            public void onClick(View v) {
                settings.setVisibility(View.INVISIBLE);
            }
        });

        ////////SETTINGS SCREEN INITIALIZATION////////////
        settings = (RelativeLayout) findViewById(R.id.settingsScreen);// Get a reference for configuration menu
        settings.setVisibility(View.INVISIBLE);//starts invisible


        //////// GUI UPDATE TIMERS INITIALIZATION////////////
        new Timer().scheduleAtFixedRate(new TimerTask() { //update screen information every GUI_TEXT_UPDATE_INTERVAL milliseconds
            @Override
            public void run() {
                myHandler.post(updateGUITextAndStatusIcons);
            }
        }, 0, GUI_TEXT_UPDATE_INTERVAL);

        new Timer().scheduleAtFixedRate(new TimerTask() { //update screen roll animation every GUI_ROLL_ANIMATION_UPDATE_INTERVAL milliseconds
            @Override
            public void run() {
                myHandler.post(updateGUIRollAnimation);
            }
        }, 0, GUI_ROLL_ANIMATION_UPDATE_INTERVAL);

        new Timer().scheduleAtFixedRate(new TimerTask() { //update screen compass animation every GUI_ROLL_ANIMATION_UPDATE_INTERVAL milliseconds
            @Override
            public void run() {
                myHandler.post(updateGUICompassAnimation);
            }
        }, 0, GUI_COMPASS_ANIMATION_UPDATE_INTERVAL);

        new Timer().scheduleAtFixedRate(new TimerTask() { //update screen compass animation every GUI_ROLL_ANIMATION_UPDATE_INTERVAL milliseconds
            @Override
            public void run() {
                myHandler.post(updateGUITipperAnimation);
            }
        }, 0, GUI_TIPPER_ANIMATION_UPDATE_INTERVAL);


    }


    final Runnable updateGUITextAndStatusIcons = new Runnable() {
        public void run() {

            //set UI text values
            TextView auxTextView;
            auxTextView = (TextView) findViewById(R.id.tipperValue);
            auxTextView.setText(scaiCore.getTipperInclination() +"°");
            auxTextView = (TextView) findViewById(R.id.rollValue);
            auxTextView.setText(scaiCore.getSideInclination() +"°");
            auxTextView = (TextView) findViewById(R.id.speedValue);
            auxTextView.setText(scaiCore.getSpeed());
          //  auxTextView = (TextView) findViewById(R.id.compassValue);
          //  auxTextView.setText(scaiCore.getCompass());
            auxTextView = (TextView) findViewById(R.id.altitudeValue);
            auxTextView.setText(scaiCore.getAltitude());
            auxTextView = (TextView) findViewById(R.id.temperatureValue);
            auxTextView.setText(scaiCore.getTemperature());
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
            if(scaiCore.getSideInclinationOld()!= null && scaiCore.getSideInclination()!=null &&
                    scaiCore.getSideInclination()!="?" && scaiCore.getSideInclinationOld()!="?") {// execute only if values != null and !="?"

                ImageView imageview = (ImageView) findViewById(R.id.truckFrontView);
                RotateAnimation rotate = new RotateAnimation(
                        Integer.parseInt(scaiCore.getSideInclinationOld()), Integer.parseInt(scaiCore.getSideInclination()),
                        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                rotate.setDuration(GUI_ROLL_ANIMATION_UPDATE_INTERVAL);
                imageview.startAnimation(rotate);
                scaiCore.setSideInclinationOld(scaiCore.getSideInclination());
            }
        }
    };

    final Runnable updateGUICompassAnimation = new Runnable() {
        public void run() {

            ///COMPASS ANIMATION///////
            if(scaiCore.getCompassOld()!= null && scaiCore.getCompass()!=null
                    && scaiCore.getCompass()!="?"&& scaiCore.getCompassOld()!="?") {// execute only if values != null and !="?"

                ImageView imageview2 = (ImageView) findViewById(R.id.compassArrow);
                RotateAnimation rotate2 = new RotateAnimation(
                        Integer.parseInt(scaiCore.getCompassOld()), Integer.parseInt(scaiCore.getCompass()),
                        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                rotate2.setDuration(GUI_COMPASS_ANIMATION_UPDATE_INTERVAL);
                imageview2.startAnimation(rotate2);
                scaiCore.setCompassOld(scaiCore.getCompass());
            }
        }
    };

    final Runnable updateGUITipperAnimation = new Runnable() {
        public void run() {

            ///COMPASS ANIMATION///////
            if(scaiCore.getTipperInclinationOld()!= null && scaiCore.getTipperInclination()!=null &&
                    scaiCore.getTipperInclination()!="?" && scaiCore.getTipperInclinationOld()!="?") {// execute only if values != null and !="?"
                ImageView imageview = (ImageView) findViewById(R.id.tipper);
                RotateAnimation rotate = new RotateAnimation(
                        (-1)*Integer.parseInt(scaiCore.getTipperInclinationOld()), (-1)*Integer.parseInt(scaiCore.getTipperInclination()),
                        Animation.RELATIVE_TO_SELF, 0.2f, Animation.RELATIVE_TO_SELF, 0.7f);
                rotate.setDuration(GUI_TIPPER_ANIMATION_UPDATE_INTERVAL);
                imageview.startAnimation(rotate);
                scaiCore.setTipperInclinationOld(scaiCore.getTipperInclination());
            }
        }
    };
}
