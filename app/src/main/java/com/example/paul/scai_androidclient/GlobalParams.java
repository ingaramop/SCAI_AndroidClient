package com.example.paul.scai_androidclient;

/**
 * Created by root on 6/25/16.
 */
public class GlobalParams {
    public static int GUI_TEXT_UPDATE_INTERVAL = 500;
    public static int GUI_ROLL_ANIMATION_UPDATE_INTERVAL = 600;
    public static int GUI_COMPASS_ANIMATION_UPDATE_INTERVAL = 650;
    public static int GUI_TIPPER_ANIMATION_UPDATE_INTERVAL = 700;
    public static int MAP_LOCATION_UPDATE_INTERVAL = 1500;

    public static String USER_AGENT = "Mozilla/5.0";
    public static int DATE_UPDATE_INTERVAL = 30000;
    public static int IMU_UPDATE_INTERVAL = 2000;
    public static int GPS_UPDATE_INTERVAL = 2950;
    public static int HTTP_READ_TIMEOUT = 800;
    public static int HTTP_CONNECT_TIMEOUT = 1000;
    public static int MAX_GPS_ERRORS_IN_A_ROW = 2;
    public static int MAX_IMU_ERRORS_IN_A_ROW = 2;

    public static float tipperInclinationCalibration, compassCalibration, sideInclinationCalibration, pressureCalibration, temperatureCalibration;

    public static String VIDEO_ADDRESS = "rtsp://wowzaec2demo.streamlock.net/vod/mp4:BigBuckBunny_115k.mov";
    //public static String IMU_QUERY = "http://192.168.1.7/cgi-bin/imuQuery.cgi";
    public static String IMU_QUERY = "http://192.168.1.3/cgi-bin/imuQueryMock.fcgi";
    public static String GPS_QUERY = "http://192.168.1.3/cgi-bin/gpsQueryMock.fcgi";
    //public static String GPS_QUERY = "http://192.168.1.7/cgi-bin/gpsQueryMock.fcgi";
}
