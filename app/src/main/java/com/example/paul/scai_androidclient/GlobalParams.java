package com.example.paul.scai_androidclient;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.prefs.Preferences;

/**
 * Created by root on 6/25/16.
 */
public class GlobalParams {

    /////////// STATIC FINAL SETTINGS///////////////
    public static final int GUI_TEXT_UPDATE_INTERVAL = 500;
    public static final int GUI_ROLL_ANIMATION_UPDATE_INTERVAL = 600;
    public static final int GUI_COMPASS_ANIMATION_UPDATE_INTERVAL = 650;
    public static final int GUI_TIPPER_ANIMATION_UPDATE_INTERVAL = 700;
    public static final int MAP_LOCATION_UPDATE_INTERVAL = 1500;

    public static final String USER_AGENT = "Mozilla/5.0";
    public static final int DATE_UPDATE_INTERVAL = 30000;
    public static final int IMU_UPDATE_INTERVAL = 2000;
    public static final int GPS_UPDATE_INTERVAL = 2950;
    public static final int HTTP_READ_TIMEOUT = 800;
    public static final int HTTP_CONNECT_TIMEOUT = 1000;
    public static final int MAX_GPS_ERRORS_IN_A_ROW = 2;
    public static final int MAX_IMU_ERRORS_IN_A_ROW = 2;


    ////////////DEFAULT PREFERENCES VALUES/////////////////
    public static String VIDEO_ADDRESS = "rtsp://wowzaec2demo.streamlock.net/vod/mp4:BigBuckBunny_115k.mov";
    //public static String IMU_QUERY_ADDRESS = "http://192.168.1.7/cgi-bin/imuQuery.cgi";
    public static String IMU_QUERY_ADDRESS = "http://192.168.1.3/cgi-bin/imuQueryMock.fcgi";
    public static String GPS_QUERY_ADDRESS = "http://192.168.1.3/cgi-bin/gpsQueryMock.fcgi";
    //public static String GPS_QUERY_ADDRESS = "http://192.168.1.7/cgi-bin/gpsQueryMock.fcgi";
    public static float  TIPPER_INCLINATION_CALIBRATION = 0.0f , COMPASS_CALIBRATION = 0.0f ,
                          SIDE_INCLINATION_CALIBRATION = 0.0f , PRESSURE_CALIBRATION = 0.0f , TEMPERATURE_CALIBRATION = 0.0f ;



    ///////////STATIC FINAL PREFERENCES IDENTIFIERS///////////////
    private static Preferences preferences;
    private final static String ID_MY_PREFERENCES = "myPrefs";
    private final static String ID_VIDEO_ADDRESS = "videoAddress";
    private final static String ID_IMU_QUERY_ADDRESS = "imuAddress";
    private final static String ID_GPS_QUERY_ADDRESS = "gpsAddress";
    private final static String ID_TIPPER_INCLINATION_CALIBRATION = "tipperInclinationCalibration";
    private final static String ID_COMPASS_CALIBRATION = "compassCalibration";
    private final static String ID_SIDE_INCLINATION_CALIBRATION = "sideInclinationCalibration";
    private final static String ID_PRESSURE_CALIBRATION = "pressureCalibration";
    private final static String ID_TEMPERATURE_CALIBRATION = "temperatureCalibration";


    public static void loadPreferences(Context context){
        SharedPreferences preferences = context.getSharedPreferences(ID_MY_PREFERENCES, Context.MODE_PRIVATE);
        VIDEO_ADDRESS = preferences.getString(ID_VIDEO_ADDRESS, VIDEO_ADDRESS);
        IMU_QUERY_ADDRESS = preferences.getString(ID_IMU_QUERY_ADDRESS, IMU_QUERY_ADDRESS);
        GPS_QUERY_ADDRESS = preferences.getString(ID_GPS_QUERY_ADDRESS, GPS_QUERY_ADDRESS);
        TIPPER_INCLINATION_CALIBRATION = preferences.getFloat(ID_TIPPER_INCLINATION_CALIBRATION, TIPPER_INCLINATION_CALIBRATION);
        COMPASS_CALIBRATION  = preferences.getFloat(ID_COMPASS_CALIBRATION, COMPASS_CALIBRATION);
        SIDE_INCLINATION_CALIBRATION = preferences.getFloat(ID_SIDE_INCLINATION_CALIBRATION, SIDE_INCLINATION_CALIBRATION);
        PRESSURE_CALIBRATION = preferences.getFloat(ID_PRESSURE_CALIBRATION, PRESSURE_CALIBRATION);
        TEMPERATURE_CALIBRATION = preferences.getFloat(ID_TEMPERATURE_CALIBRATION, TEMPERATURE_CALIBRATION);
    }

    public static void savePreferences(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(ID_MY_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(ID_VIDEO_ADDRESS, VIDEO_ADDRESS);

        editor.putString(ID_IMU_QUERY_ADDRESS, IMU_QUERY_ADDRESS);

        editor.putString(ID_GPS_QUERY_ADDRESS, GPS_QUERY_ADDRESS);

        editor.putFloat(ID_TIPPER_INCLINATION_CALIBRATION, TIPPER_INCLINATION_CALIBRATION);

        editor.putFloat(ID_COMPASS_CALIBRATION, COMPASS_CALIBRATION);

        editor.putFloat(ID_SIDE_INCLINATION_CALIBRATION, SIDE_INCLINATION_CALIBRATION);

        editor.putFloat(ID_PRESSURE_CALIBRATION, PRESSURE_CALIBRATION);

        editor.putFloat(ID_TEMPERATURE_CALIBRATION, TEMPERATURE_CALIBRATION);

        editor.commit();
    }

}
