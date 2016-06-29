package com.example.paul.scai_androidclient;

import android.content.pm.ActivityInfo;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

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
    private MapView map;// map view
    private IMapController mapController;
    private RelativeLayout mapContainer;
    private ArrayList<OverlayItem> items;
    private ItemizedOverlay<OverlayItem> mMyLocationOverlay;
    private ImageButton pinButton;
    private boolean isMapPinned;


    VideoView cam;//Camera view

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

/*        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);*/

        scaiCore = new SCAICore();// instantiate scai core object
        scaiCore.start();// start running http, timer and other connections
        GlobalParams.loadPreferences(this);


        ////////CAMERA VIEW INITIALIZATION////////////
        cam = (VideoView)findViewById(R.id.myVideo);// Get a reverence for the videoview
        String vidAddress = GlobalParams.VIDEO_ADDRESS;//configure URL
        Uri vidUri = Uri.parse(vidAddress);
        cam.setVideoURI(vidUri);
        cam.start();//Start stream

        ////////MAP VIEW INITIALIZATION////////////
        mapContainer = (RelativeLayout) findViewById(R.id.mapContainer);// Get a reference for the map view container
        pinButton = (ImageButton) findViewById(R.id.pinButton);// get reference for the pin/unpin button
        pinButton.setImageDrawable(getResources().getDrawable(getResources().getIdentifier("@drawable/unpin", null, getPackageName()))); //starts pinned
        isMapPinned = true;//starts Pinned
        pinButton.setOnClickListener(new View.OnClickListener() {// Its action makes visible the configuration menu
            public void onClick(View v) {
                if(isMapPinned){
                    isMapPinned = false;
                    pinButton.setImageDrawable(getResources().getDrawable(getResources().getIdentifier("@drawable/pin", null, getPackageName()))); //starts pinned
                }
                else{
                    isMapPinned = true;
                    pinButton.setImageDrawable(getResources().getDrawable(getResources().getIdentifier("@drawable/unpin", null, getPackageName()))); //starts pinned
                }
            }
        });

        map = (MapView) findViewById(R.id.map);// Get a reference for the map view
        map.setTileSource(TileSourceFactory.MAPQUESTOSM);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
        mapController = map.getController();
        //mapController.setInvertedTiles(true);
        map.setTileSource(new XYTileSource("MapQuest", 0, 18, 256, ".jpg", new String[] {
                "http://otile1.mqcdn.com/tiles/1.0.0/map/",
                "http://otile2.mqcdn.com/tiles/1.0.0/map/",
                "http://otile3.mqcdn.com/tiles/1.0.0/map/",
                "http://otile4.mqcdn.com/tiles/1.0.0/map/"}));
        mapController.setZoom(17);
        map.setUseDataConnection(false); //keeps the mapView from loading online tiles using network connection
        mapContainer.setVisibility(View.GONE);// starts invisible


        //// minimap overlay
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        MinimapOverlay mMinimapOverlay;
        mMinimapOverlay = new MinimapOverlay(getApplicationContext(), map.getTileRequestCompleteHandler());
        mMinimapOverlay.setWidth(dm.widthPixels / 5);
        mMinimapOverlay.setHeight(dm.heightPixels / 5);
        mMinimapOverlay.setZoomDifference(4);
//optionally, you can set the minimap to a different tile source
//mMinimapOverlay.setTileSource(....);
        map.getOverlays().add(mMinimapOverlay);

        //////map markers
        GeoPoint startPosition = new GeoPoint(-31.435253, -64.193881);
        mapController.setCenter(startPosition);
        items = new ArrayList<OverlayItem>();
        items.add(new OverlayItem("Title", "Description", startPosition)); // Lat/Lon decimal degrees
        mMyLocationOverlay = new ItemizedIconOverlay<OverlayItem>(items, new Glistener(), map.getResourceProxy());
        map.getOverlays().add(mMyLocationOverlay);
        //map.invalidate();




        ///////////map scale bar
        ScaleBarOverlay mScaleBarOverlay = new ScaleBarOverlay(map);
        mScaleBarOverlay.enableScaleBar();
        map.getOverlayManager().add(mScaleBarOverlay);



        ////////MAP-CAM TOGGLE SWITCH INITIALIZATION////////////
        mapCamSwitch = (Switch) findViewById(R.id.mapCamSwitch);//Get a reference for map/camera selector switch
        mapCamSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {//define change Listener action
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){//if it is checked, hides cam and shows map
                    mapContainer.setVisibility(View.VISIBLE);
                    cam.stopPlayback();
                    cam.setVisibility(View.INVISIBLE);
                }else{//if it is unchecked, does the oposite
                    cam.setVisibility(View.VISIBLE);
                    cam.start();
                    mapContainer.setVisibility(View.GONE);
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


    }

    final Runnable updateMAPLocation= new Runnable() {
        public void run() {
            if (scaiCore.getPositionX() != GlobalParams.INVALID_FLOAT_VALUE && scaiCore.getPositionY() != GlobalParams.INVALID_FLOAT_VALUE &&
                    scaiCore.getPositionX() != GlobalParams.INVALID_FLOAT_VALUE ) {// if values are valid...

                GeoPoint currentPosition = new GeoPoint(scaiCore.getPositionX(), scaiCore.getPositionY());

                //mapController.setCenter(startPoint);
                map.getOverlays().remove(mMyLocationOverlay);
                items.clear();
                if (isMapPinned)mapController.animateTo(currentPosition);
                items.add(new OverlayItem("Title", "Description", currentPosition)); // Lat/Lon decimal degrees
                mMyLocationOverlay = new ItemizedIconOverlay<OverlayItem>(items, new Glistener(), map.getResourceProxy());

                map.getOverlays().add(mMyLocationOverlay);
                map.invalidate();
            }
        }
    };

    class Glistener implements ItemizedIconOverlay.OnItemGestureListener<OverlayItem> {
        @Override
        public boolean onItemLongPress(int index, OverlayItem item) {
            return false;
        }

        @Override
        public boolean onItemSingleTapUp(int index, OverlayItem item) {
            return true; // We 'handled' this event.

        }

    }

    final Runnable updateGUITextAndStatusIcons = new Runnable() {
        public void run() {

            //set UI text values
            //IMU VALUES//
            TextView auxTextView;

            auxTextView = (TextView) findViewById(R.id.tipperValue);
            if (scaiCore.getTipperInclination() != GlobalParams.INVALID_INT_VALUE)
                auxTextView.setText(scaiCore.getTipperInclination() +"°");
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
            if (scaiCore.getSpeed() != GlobalParams.INVALID_INT_VALUE)
                auxTextView.setText(scaiCore.getSpeed() +"");
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
}
