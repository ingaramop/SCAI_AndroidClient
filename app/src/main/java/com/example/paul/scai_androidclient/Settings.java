package com.example.paul.scai_androidclient;

/**
 * Created by root on 6/25/16.
 */
public class Settings {
    private int GUI_TEXT_UPDATE_INTERVAL = 500;
    private int GUI_ROLL_ANIMATION_UPDATE_INTERVAL = 600;
    private int GUI_COMPASS_ANIMATION_UPDATE_INTERVAL = 650;
    private int GUI_TIPPER_ANIMATION_UPDATE_INTERVAL = 700;
    private int MAP_LOCATION_UPDATE_INTERVAL = 1500;
    private String VIDEO_ADDRESS = "rtsp://wowzaec2demo.streamlock.net/vod/mp4:BigBuckBunny_115k.mov";
    private String IMU_QUERY = "http://192.168.1.7/cgi-bin/imuQuery.cgi";
    //private String IMU_QUERY = "http://192.168.1.3/cgi-bin/imuQueryMock.fcgi";
    //private String GPS_QUERY = "http://192.168.1.3/cgi-bin/gpsQueryMock.fcgi";
    private String GPS_QUERY = "http://192.168.1.7/cgi-bin/gpsQueryMock.fcgi";
    private String USER_AGENT = "Mozilla/5.0";
    private int DATE_UPDATE_INTERVAL = 30000;
    private int IMU_UPDATE_INTERVAL = 2000;
    private int GPS_UPDATE_INTERVAL = 2950;
    private int HTTP_READ_TIMEOUT = 800;
    private int HTTP_CONNECT_TIMEOUT = 1000;
    private int MAX_GPS_ERRORS_IN_A_ROW = 2;
    private int MAX_IMU_ERRORS_IN_A_ROW = 2;
}
