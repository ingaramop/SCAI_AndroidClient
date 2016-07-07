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
    public static final int BEEP_ALERT_INTERVAL = 3000;
    public static final int HTTP_READ_TIMEOUT = 800;
    public static final int HTTP_CONNECT_TIMEOUT = 1000;
    public static final int MAX_GPS_ERRORS_IN_A_ROW = 2;
    public static final int MAX_IMU_ERRORS_IN_A_ROW = 2;

    public static final float INVALID_FLOAT_VALUE = -9999.9f;
    public static final int INVALID_INT_VALUE = -9999;

    public static final float SEA_LEVEL_PRESSURE = 1014f;

    public static final int VIDEO_TIMEOUT = 5;


    ////////////DEFAULT PREFERENCES VALUES/////////////////
    //url parameters
    public static String VIDEO_ADDRESS = "http://192.168.0.8/videostream.cgi?user=admin&pwd=&resolution=32&rate=2";
    //public static String IMU_QUERY_ADDRESS = "http://192.168.1.7/cgi-bin/imuQuery.cgi";
    public static String IMU_QUERY_ADDRESS = "http://192.168.0.5/cgi-bin/imuQueryMock.fcgi";
    public static String GPS_QUERY_ADDRESS = "http://192.168.0.5/cgi-bin/gpsQueryMock.fcgi";
    //public static String GPS_QUERY_ADDRESS = "http://192.168.1.7/cgi-bin/gpsQueryMock.fcgi";
    //calibration parameters
    public static float PRESSURE_CALIBRATION = 0.0f , TEMPERATURE_CALIBRATION = 0.0f ;
    public static int TIPPER_INCLINATION_CALIBRATION = 0,SIDE_INCLINATION_CALIBRATION = 0, COMPASS_CALIBRATION = 0;
    //alert parameters
    public static int SPEED_LIMIT = 40, MAX_SPEED_WITH_TIPPER_UP = 20, MIN_TIPPER_UP_ANGLE = 20;
    public static boolean SPEED_LIMIT_BEEP_ENABLED = true, TIPPER_UP_BEEP_ENABLED = true;


    ///////////STATIC FINAL PREFERENCES IDENTIFIERS///////////////
    private static Preferences preferences;
    private final static String ID_MY_PREFERENCES = "myPrefs";
    //urls
    private final static String ID_VIDEO_ADDRESS = "videoAddress";
    private final static String ID_IMU_QUERY_ADDRESS = "imuAddress";
    private final static String ID_GPS_QUERY_ADDRESS = "gpsAddress";
    //calibration
    private final static String ID_TIPPER_INCLINATION_CALIBRATION = "tipperInclinationCalibration";
    private final static String ID_COMPASS_CALIBRATION = "compassCalibration";
    private final static String ID_SIDE_INCLINATION_CALIBRATION = "sideInclinationCalibration";
    private final static String ID_PRESSURE_CALIBRATION = "pressureCalibration";
    private final static String ID_TEMPERATURE_CALIBRATION = "temperatureCalibration";
    //alerts
    private final static String ID_SPEED_LIMIT = "speedLimit";
    private final static String ID_MAX_SPEED_WITH_TIPPER_UP = "MaxSpeedWithTipperUp";
    private final static String ID_MIN_TIPPER_UP_ANGLE = "MinTipperUpAngle";
    private final static String ID_SPEED_LIMIT_BEEP_ENABLED = "SpeedLimitBeepEnabled";
    private final static String ID_TIPPER_UP_BEEP_ENABLED = "TipperUpBeepEnabled";

    public static void loadPreferences(Context context){
        SharedPreferences preferences = context.getSharedPreferences(ID_MY_PREFERENCES, Context.MODE_PRIVATE);
        //url
        VIDEO_ADDRESS = preferences.getString(ID_VIDEO_ADDRESS, VIDEO_ADDRESS);
        IMU_QUERY_ADDRESS = preferences.getString(ID_IMU_QUERY_ADDRESS, IMU_QUERY_ADDRESS);
        GPS_QUERY_ADDRESS = preferences.getString(ID_GPS_QUERY_ADDRESS, GPS_QUERY_ADDRESS);
        //calibration
        TIPPER_INCLINATION_CALIBRATION = preferences.getInt(ID_TIPPER_INCLINATION_CALIBRATION, TIPPER_INCLINATION_CALIBRATION);
        COMPASS_CALIBRATION  = preferences.getInt(ID_COMPASS_CALIBRATION, COMPASS_CALIBRATION);
        SIDE_INCLINATION_CALIBRATION = preferences.getInt(ID_SIDE_INCLINATION_CALIBRATION, SIDE_INCLINATION_CALIBRATION);
        PRESSURE_CALIBRATION = preferences.getFloat(ID_PRESSURE_CALIBRATION, PRESSURE_CALIBRATION);
        TEMPERATURE_CALIBRATION = preferences.getFloat(ID_TEMPERATURE_CALIBRATION, TEMPERATURE_CALIBRATION);
        //alerts
        SPEED_LIMIT = preferences.getInt(ID_SPEED_LIMIT,SPEED_LIMIT);
        MAX_SPEED_WITH_TIPPER_UP = preferences.getInt(ID_MAX_SPEED_WITH_TIPPER_UP,MAX_SPEED_WITH_TIPPER_UP);
        MIN_TIPPER_UP_ANGLE= preferences.getInt(ID_MIN_TIPPER_UP_ANGLE,MIN_TIPPER_UP_ANGLE);
        SPEED_LIMIT_BEEP_ENABLED= preferences.getBoolean(ID_SPEED_LIMIT_BEEP_ENABLED,SPEED_LIMIT_BEEP_ENABLED);
        TIPPER_UP_BEEP_ENABLED = preferences.getBoolean(ID_TIPPER_UP_BEEP_ENABLED,TIPPER_UP_BEEP_ENABLED);
    }

    public static void savePreferences(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(ID_MY_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        //urls
        editor.putString(ID_VIDEO_ADDRESS, VIDEO_ADDRESS);
        editor.putString(ID_IMU_QUERY_ADDRESS, IMU_QUERY_ADDRESS);
        editor.putString(ID_GPS_QUERY_ADDRESS, GPS_QUERY_ADDRESS);
        //calibration
        editor.putInt(ID_TIPPER_INCLINATION_CALIBRATION, TIPPER_INCLINATION_CALIBRATION);
        editor.putInt(ID_COMPASS_CALIBRATION, COMPASS_CALIBRATION);
        editor.putInt(ID_SIDE_INCLINATION_CALIBRATION, SIDE_INCLINATION_CALIBRATION);
        editor.putFloat(ID_PRESSURE_CALIBRATION, PRESSURE_CALIBRATION);
        editor.putFloat(ID_TEMPERATURE_CALIBRATION, TEMPERATURE_CALIBRATION);
        //alerts
        editor.putInt(ID_SPEED_LIMIT,SPEED_LIMIT);
        editor.putInt(ID_MAX_SPEED_WITH_TIPPER_UP, MAX_SPEED_WITH_TIPPER_UP);
        editor.putInt(ID_MIN_TIPPER_UP_ANGLE, MIN_TIPPER_UP_ANGLE);
        editor.putBoolean(ID_SPEED_LIMIT_BEEP_ENABLED, SPEED_LIMIT_BEEP_ENABLED);
        editor.putBoolean(ID_TIPPER_UP_BEEP_ENABLED,TIPPER_UP_BEEP_ENABLED);


        editor.commit();
    }

}
