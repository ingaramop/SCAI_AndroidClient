package com.example.paul.scai_androidclient;

import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

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
    final private static String VIDEO_ADDRESS = "rtsp://wowzaec2demo.streamlock.net/vod/mp4:BigBuckBunny_115k.mov";
    SCAICore scaiCore;

    ImageView rotateImage;


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

        scaiCore = new SCAICore();
        scaiCore.start();


   /*     VideoView vidView = (VideoView)findViewById(R.id.myVideo);
        String vidAddress = VIDEO_ADDRESS;
        Uri vidUri = Uri.parse(vidAddress);
        vidView.setVideoURI(vidUri);
        vidView.start();*/



        MapView map = (MapView) findViewById(R.id.map);
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
        map.setUseDataConnection(false); //keeps the mapView from loading online tiles using network connection.



        new Timer().scheduleAtFixedRate(new TimerTask() { //update screen information every GUI_TEXT_UPDATE_INTERVAL milliseconds
            @Override
            public void run() {
                myHandler.post(updateGUIText);
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


    }


    final Runnable updateGUIText = new Runnable() {
        public void run() {

            //set UI text values
            TextView auxTextView;
            auxTextView = (TextView) findViewById(R.id.tipperValue);
            auxTextView.setText(scaiCore.getTipperInclination());
            //auxTextView = (TextView) findViewById(R.id.rollValue);
            auxTextView.setText(scaiCore.getSideInclination() );
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


        }
    };


    final Runnable updateGUIRollAnimation = new Runnable() {
        public void run() {

            ///ROLL ANIMATION///////
            if(scaiCore.getSideInclinationOld()!= null && scaiCore.getSideInclination()!=null) {// execute only if values != null

                ImageView imageview = (ImageView) findViewById(R.id.truckFrontView);
                RotateAnimation rotate = new RotateAnimation(
                        Integer.parseInt(scaiCore.getSideInclinationOld()), Integer.parseInt(scaiCore.getSideInclination()),
                        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                rotate.setDuration(600);
                imageview.startAnimation(rotate);
                scaiCore.setSideInclinationOld(scaiCore.getSideInclination());
            }
        }
    };

    final Runnable updateGUICompassAnimation = new Runnable() {
        public void run() {

            ///COMPASS ANIMATION///////
            if(scaiCore.getCompassOld()!= null && scaiCore.getCompass()!=null) {// execute only if values != null

                ImageView imageview2 = (ImageView) findViewById(R.id.compassArrow);
                RotateAnimation rotate2 = new RotateAnimation(
                        Integer.parseInt(scaiCore.getCompassOld()), Integer.parseInt(scaiCore.getCompass()),
                        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                rotate2.setDuration(650);
                imageview2.startAnimation(rotate2);
                scaiCore.setCompassOld(scaiCore.getCompass());
            }
        }
    };
}
