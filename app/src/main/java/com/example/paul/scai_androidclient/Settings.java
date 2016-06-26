package com.example.paul.scai_androidclient;

/**
 * Created by root on 6/25/16.
 */
public class Settings {
    public int GUI_TEXT_UPDATE_INTERVAL = 500;
    public int GUI_ROLL_ANIMATION_UPDATE_INTERVAL = 600;
    public int GUI_COMPASS_ANIMATION_UPDATE_INTERVAL = 650;
    public int GUI_TIPPER_ANIMATION_UPDATE_INTERVAL = 700;
    public int MAP_LOCATION_UPDATE_INTERVAL = 1500;

    public String USER_AGENT = "Mozilla/5.0";
    public int DATE_UPDATE_INTERVAL = 30000;
    public int IMU_UPDATE_INTERVAL = 2000;
    public int GPS_UPDATE_INTERVAL = 2950;
    public int HTTP_READ_TIMEOUT = 800;
    public int HTTP_CONNECT_TIMEOUT = 1000;
    public int MAX_GPS_ERRORS_IN_A_ROW = 2;
    public int MAX_IMU_ERRORS_IN_A_ROW = 2;

    public float tipperInclinationCalibration, compassCalibration, sideInclinationCalibration, pressureCalibration, temperatureCalibration;

    public String VIDEO_ADDRESS = "rtsp://wowzaec2demo.streamlock.net/vod/mp4:BigBuckBunny_115k.mov";
    public String IMU_QUERY = "http://192.168.1.7/cgi-bin/imuQuery.cgi";
    //public String IMU_QUERY = "http://192.168.1.3/cgi-bin/imuQueryMock.fcgi";
    //public String GPS_QUERY = "http://192.168.1.3/cgi-bin/gpsQueryMock.fcgi";
    public String GPS_QUERY = "http://192.168.1.7/cgi-bin/gpsQueryMock.fcgi";
}
