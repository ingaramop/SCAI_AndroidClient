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


    public static final int VIDEO_TIMEOUT = 5;


    ////////////DEFAULT PREFERENCES VALUES/////////////////
    //url parameters
    public static final String CAMERA_CONTROL_URL = "http://192.168.1.10/decoder_control.cgi?user=pi&pwd=raspberry&command=";

    public static final String VIDEO_ADDRESS_DEFAULT = "http://192.168.1.10/videostream.cgi?user=pi&pwd=raspberry&resolution=32&rate=2";
    public static final String IMU_QUERY_ADDRESS_DEFAULT = "http://192.168.1.6/cgi-bin/imuQuery.cgi";
    //public static final String IMU_QUERY_ADDRESS_DEFAULT = "http://192.168.0.5/cgi-bin/imuQueryMock.fcgi";
    public static final String GPS_QUERY_ADDRESS_DEFAULT = "http://192.168.1.6/cgi-bin/gpsQuery.fcgi";
    //public static final String GPS_QUERY_ADDRESS_DEFAULT = "http://192.168.1.7/cgi-bin/gpsQueryMock.fcgi";
    //calibration parameters
    public static final float PRESSURE_CALIBRATION_DEFAULT = 0.0f , TEMPERATURE_CALIBRATION_DEFAULT = 0.0f ;
    public static final int TIPPER_INCLINATION_CALIBRATION_DEFAULT = 0,SIDE_INCLINATION_CALIBRATION_DEFAULT = 0, COMPASS_CALIBRATION_DEFAULT = 0;
    //alert parameters
    public static final int SPEED_LIMIT_DEFAULT = 40, MAX_SPEED_WITH_TIPPER_UP_DEFAULT = 20, MIN_TIPPER_UP_ANGLE_DEFAULT = 20;
    public static final boolean SPEED_LIMIT_BEEP_ENABLED_DEFAULT = false, TIPPER_UP_BEEP_ENABLED_DEFAULT = false;




    public static String VIDEO_ADDRESS;
    //public static String IMU_QUERY_ADDRESS;
    public static String IMU_QUERY_ADDRESS;
    public static String GPS_QUERY_ADDRESS;
    //public static String GPS_QUERY_ADDRESS;
    //calibration parameters
    public static float PRESSURE_CALIBRATION , TEMPERATURE_CALIBRATION;
    public static int TIPPER_INCLINATION_CALIBRATION, SIDE_INCLINATION_CALIBRATION, COMPASS_CALIBRATION;
    //alert parameters
    public static int SPEED_LIMIT, MAX_SPEED_WITH_TIPPER_UP, MIN_TIPPER_UP_ANGLE;
    public static boolean SPEED_LIMIT_BEEP_ENABLED, TIPPER_UP_BEEP_ENABLED;


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
        VIDEO_ADDRESS = preferences.getString(ID_VIDEO_ADDRESS, VIDEO_ADDRESS_DEFAULT);
        IMU_QUERY_ADDRESS = preferences.getString(ID_IMU_QUERY_ADDRESS, IMU_QUERY_ADDRESS_DEFAULT);
        GPS_QUERY_ADDRESS = preferences.getString(ID_GPS_QUERY_ADDRESS, GPS_QUERY_ADDRESS_DEFAULT);
        //calibration
        TIPPER_INCLINATION_CALIBRATION = preferences.getInt(ID_TIPPER_INCLINATION_CALIBRATION, TIPPER_INCLINATION_CALIBRATION_DEFAULT);
        COMPASS_CALIBRATION  = preferences.getInt(ID_COMPASS_CALIBRATION, COMPASS_CALIBRATION_DEFAULT);
        SIDE_INCLINATION_CALIBRATION = preferences.getInt(ID_SIDE_INCLINATION_CALIBRATION, SIDE_INCLINATION_CALIBRATION_DEFAULT);
        PRESSURE_CALIBRATION = preferences.getFloat(ID_PRESSURE_CALIBRATION, PRESSURE_CALIBRATION_DEFAULT);
        TEMPERATURE_CALIBRATION = preferences.getFloat(ID_TEMPERATURE_CALIBRATION, TEMPERATURE_CALIBRATION_DEFAULT);
        //alerts
        SPEED_LIMIT = preferences.getInt(ID_SPEED_LIMIT,SPEED_LIMIT_DEFAULT);
        MAX_SPEED_WITH_TIPPER_UP = preferences.getInt(ID_MAX_SPEED_WITH_TIPPER_UP,MAX_SPEED_WITH_TIPPER_UP_DEFAULT);
        MIN_TIPPER_UP_ANGLE= preferences.getInt(ID_MIN_TIPPER_UP_ANGLE,MIN_TIPPER_UP_ANGLE_DEFAULT);
        SPEED_LIMIT_BEEP_ENABLED= preferences.getBoolean(ID_SPEED_LIMIT_BEEP_ENABLED,SPEED_LIMIT_BEEP_ENABLED_DEFAULT);
        TIPPER_UP_BEEP_ENABLED = preferences.getBoolean(ID_TIPPER_UP_BEEP_ENABLED,TIPPER_UP_BEEP_ENABLED_DEFAULT);
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
