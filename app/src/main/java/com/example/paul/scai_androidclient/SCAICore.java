package com.example.paul.scai_androidclient;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by paul on 22/05/16.
 */
public class SCAICore {
    //values
    private int altitude, tipperInclination, compass, sideInclination, speed;
    private float positionX, positionY, pressure, temperature;
    private long timestamp;
    private String date;

    //old values
    private int tipperInclinationOld, compassOld, sideInclinationOld;

    //error counters
    private int gpsErrorsInARow, imuErrorsInARow;


    public void SCAICore(){
        //initialize sensor data variables IMU
        compass = GlobalParams.INVALID_INT_VALUE;
        altitude = GlobalParams.INVALID_INT_VALUE;
        pressure=  GlobalParams.INVALID_FLOAT_VALUE;
        temperature = GlobalParams.INVALID_FLOAT_VALUE;
        tipperInclination = GlobalParams.INVALID_INT_VALUE;
        sideInclination = GlobalParams.INVALID_INT_VALUE;
        tipperInclinationOld = GlobalParams.INVALID_INT_VALUE;
        compassOld = GlobalParams.INVALID_INT_VALUE;
        sideInclinationOld = GlobalParams.INVALID_INT_VALUE;
        imuErrorsInARow = 0;
        gpsErrorsInARow = 0;
        //initialize time and date variables
        date="";
        //initialize gps variables
        positionX = GlobalParams.INVALID_FLOAT_VALUE;
        positionY = GlobalParams.INVALID_FLOAT_VALUE;
        speed = GlobalParams.INVALID_INT_VALUE;
        timestamp = GlobalParams.INVALID_INT_VALUE;

    }

    public void start() {
        //start time and date update thread
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                getSystemDate();
            }
        }, 0,GlobalParams.DATE_UPDATE_INTERVAL);

        //start sensor data http querier thread
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                getDataFromXML(GlobalParams.IMU_QUERY_ADDRESS);
            }
        }, 0,GlobalParams.IMU_UPDATE_INTERVAL);

        //start sensor data http querier thread
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                getDataFromXML(GlobalParams.GPS_QUERY_ADDRESS);
            }
        }, 0,GlobalParams.GPS_UPDATE_INTERVAL);

    }

    private InputStream HTTPGet(String URL) {
        try {
            URL url = new URL(URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(GlobalParams.HTTP_READ_TIMEOUT /* milliseconds */);
            conn.setConnectTimeout(GlobalParams.HTTP_CONNECT_TIMEOUT /* milliseconds */);
            conn.setRequestMethod("GET");// optional default is GET
            conn.setRequestProperty("User-Agent", GlobalParams.USER_AGENT);//add request header
            conn.setDoInput(true);
            conn.connect();

            //add request header
            // con.setRequestProperty("User-Agent", USER_AGENT);

            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                conn.disconnect();
                return null;// if response is not success (200), then return null
            }

            InputStream stream = conn.getInputStream();

            // conn.disconnect();
            return stream;// else, return inputstream
        } catch (ProtocolException e) { //print stack trace and return null for all exceptions
            e.printStackTrace();
        } catch (MalformedURLException f) {
            f.printStackTrace();
        } catch (IOException g) {
            g.printStackTrace();
        }
        return null;
    }


    private void getDataFromXML(String URL) {
        InputStream stream = HTTPGet(URL);
        XmlPullParserFactory xmlFactoryObject;
        try {
            xmlFactoryObject = XmlPullParserFactory.newInstance();
            XmlPullParser myparser = xmlFactoryObject.newPullParser();

            myparser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            myparser.setInput(stream, null);

            int event;
            String text = null;

            event = myparser.getEventType();

            while (event != XmlPullParser.END_DOCUMENT) {
                String name = myparser.getName();

                switch (event) {
                    case XmlPullParser.START_TAG:
                        break;

                    case XmlPullParser.TEXT:
                        text = myparser.getText();
                        break;

                    case XmlPullParser.END_TAG:
                            if (name.equals("tipperInclination")) {
                                tipperInclinationOld = tipperInclination;
                                tipperInclination = Integer.parseInt(text);
                                if (tipperInclination != GlobalParams.INVALID_INT_VALUE) tipperInclination += GlobalParams.TIPPER_INCLINATION_CALIBRATION;
                            } else if (name.equals("sideInclination")) {
                                sideInclinationOld = sideInclination;
                                sideInclination = Integer.parseInt(text);
                                if (sideInclination != GlobalParams.INVALID_INT_VALUE) sideInclination += GlobalParams.SIDE_INCLINATION_CALIBRATION;
                            } else if (name.equals("compass")) {
                                compassOld = compass;
                                compass = Integer.parseInt(text);
                                if (compass != GlobalParams.INVALID_INT_VALUE) compass += GlobalParams.COMPASS_CALIBRATION;
                            } else if (name.equals("temperature")) {
                                temperature = Float.parseFloat(text);
                                if (temperature != GlobalParams.INVALID_FLOAT_VALUE) temperature += GlobalParams.TEMPERATURE_CALIBRATION;
                            } else if (name.equals("pressure")) {
                                pressure = Float.parseFloat(text);
                                if (pressure != GlobalParams.INVALID_FLOAT_VALUE) pressure += GlobalParams.PRESSURE_CALIBRATION;
                            }  else if (name.equals("altitude")) {
                                if (Integer.parseInt(text) != GlobalParams.INVALID_INT_VALUE) altitude = Integer.parseInt(text);
                            }  else if (name.equals("speed")) {
                                speed = Integer.parseInt(text);
                            }  else if (name.equals("positionX")) {
                                positionX = Float.parseFloat(text);
                            } else if (name.equals("positionY")) {
                                positionY = Float.parseFloat(text);
                            } else if (name.equals("timestamp")) {
                                timestamp = Long.parseLong(text);
                        }
                        else {
                        }
                        break;
                }
                event = myparser.next();
            }
            stream.close();
            if (URL == GlobalParams.IMU_QUERY_ADDRESS) imuErrorsInARow =0;//reset error in a row counter
            if (URL == GlobalParams.GPS_QUERY_ADDRESS)gpsErrorsInARow =0;
            return;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        if (URL == GlobalParams.IMU_QUERY_ADDRESS) imuErrorsInARow++;//increase amount of errors in a row
        if (URL == GlobalParams.GPS_QUERY_ADDRESS) gpsErrorsInARow++;

        if(gpsErrorsInARow > GlobalParams.MAX_GPS_ERRORS_IN_A_ROW){//if more errors in a row than permitted, set values to unknown
            timestamp = GlobalParams.INVALID_INT_VALUE;
            speed = GlobalParams.INVALID_INT_VALUE;
            altitude = GlobalParams.INVALID_INT_VALUE;
            positionX = GlobalParams.INVALID_FLOAT_VALUE;
            positionY = GlobalParams.INVALID_FLOAT_VALUE;
        }
        if(imuErrorsInARow > GlobalParams.MAX_IMU_ERRORS_IN_A_ROW){//if more errors in a row than permitted, set values to unknown
            compass = GlobalParams.INVALID_INT_VALUE;
            temperature = GlobalParams.INVALID_FLOAT_VALUE;
            tipperInclination = GlobalParams.INVALID_INT_VALUE;
            sideInclination = GlobalParams.INVALID_INT_VALUE;
            pressure= GlobalParams.INVALID_FLOAT_VALUE;
        }
        return;
    }


    public void getSystemDate(){//reads system time and update time and date strings
        Calendar c = Calendar.getInstance();
        SimpleDateFormat date = new SimpleDateFormat("dd-MM-yy");
        this.date = date.format(c.getTime());
    }


    /**
     * Created by User on 18/05/2016.
     */

    public int getAltitude() {
        return altitude;
    }

    public int getTipperInclination() {
        return tipperInclination;
    }

    public int getCompass() {
        return compass;
    }

    public int getSideInclination() {
        return sideInclination;
    }

    public int getSpeed() {
        return speed;
    }

    public float getPositionX() {
        return positionX;
    }

    public float getPositionY() {
        return positionY;
    }

    public float getPressure() {
        return pressure;
    }

    public float getTemperature() {
        return temperature;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getDate() {
        return date;
    }

    public int getTipperInclinationOld() {
        return tipperInclinationOld;
    }

    public int getCompassOld() {
        return compassOld;
    }

    public int getSideInclinationOld() {
        return sideInclinationOld;
    }

    public int getGpsErrorsInARow() { return gpsErrorsInARow;   }

    public int getImuErrorsInARow() { return imuErrorsInARow;   }

    public void setTipperInclinationOld(int tipperInclinationOld) {
        this.tipperInclinationOld = tipperInclinationOld;
    }

    public void setSideInclinationOld(int sideInclinationOld) {
        this.sideInclinationOld = sideInclinationOld;
    }

    public void setCompassOld(int compassOld) {
        this.compassOld = compassOld;
    }
}
