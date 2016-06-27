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
    private String tipperInclination, compass, sideInclination, speed, timestamp, positionX, positionY, altitude, pressure, temperature, date;
    private String tipperInclinationOld, compassOld, sideInclinationOld, timestampOld, positionXOld, positionYOld;
    private int gpsErrorsInARow, imuErrorsInARow;


    public void SCAICore(){
        //initialize sensor data variables IMU
        compass="0";
        altitude="0";
        pressure="0";
        temperature="0";
        tipperInclination="0";
        sideInclination="0";
        timestamp="0";
        speed="0";
        positionX="0";
        positionY="0";
        positionXOld="0";
        positionYOld="0";
        tipperInclinationOld="0";
        compassOld="0";
        sideInclinationOld="0";
        timestampOld = "0";
        imuErrorsInARow =0;
        gpsErrorsInARow =0;
        //initialize time and date variables
        date="";
        //initialize gps variables
        //poner aqui la info del gps

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
                getDataFromXML(GlobalParams.IMU_QUERY);
            }
        }, 0,GlobalParams.IMU_UPDATE_INTERVAL);

        //start sensor data http querier thread
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                getDataFromXML(GlobalParams.GPS_QUERY);
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
                            if(Integer.parseInt(text)<0) text ="0";// do not allow an inclination over 0
                            if(Integer.parseInt(text)>50) text ="50"; // do not allow an inclination lower bellow -50
                            tipperInclinationOld = tipperInclination;
                            tipperInclination = text;
                        } else if (name.equals("sideInclination")) {
                            if(Integer.parseInt(text)<-45) text ="-45";// do not allow an inclination below -50
                            if(Integer.parseInt(text)>45) text ="45";// do not allow an inclination over 50
                            sideInclinationOld = sideInclination;
                            sideInclination = text;
                        } else if (name.equals("compass")) {
                            compassOld = compass;
                            compass = text;
                        }else if (name.equals("altitude")) {
                            altitude = text;
                        }else if (name.equals("pressure")) {
                            pressure = text;
                        } else if (name.equals("temperature")) {
                            temperature = text;
                        }  else if (name.equals("positionX")) {
                            positionXOld = positionX;
                            positionX = text;
                        } else if (name.equals("positionY")) {
                            positionYOld = positionY;
                            positionY = text;
                        } else if (name.equals("timestamp")) {
                            timestampOld = timestamp;
                            timestamp = text;
                            speed = calculateSpeed(positionX, positionXOld, positionY, positionYOld, timestamp, timestampOld);
                        }
                        else {
                        }
                        break;
                }
                event = myparser.next();
            }
            stream.close();
            if (URL == GlobalParams.IMU_QUERY) imuErrorsInARow =0;//reset error in a row counter
            if (URL == GlobalParams.GPS_QUERY)gpsErrorsInARow =0;
            return;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        if (URL == GlobalParams.IMU_QUERY) imuErrorsInARow++;//increase amount of errors in a row
        if (URL == GlobalParams.GPS_QUERY) gpsErrorsInARow++;

        if(gpsErrorsInARow > GlobalParams.MAX_GPS_ERRORS_IN_A_ROW){//if more errors in a row than permitted, set values to unknown
            timestamp="?";
            speed = "?";
            //positionX="?";
            //positionY="?";
        }
        if(gpsErrorsInARow > GlobalParams.MAX_IMU_ERRORS_IN_A_ROW){//if more errors in a row than permitted, set values to unknown
            compass="?";
            altitude="?";
            temperature="?";
            tipperInclination="?";
            sideInclination="?";
            pressure="?";
        }
        return;
    }

    private String calculateSpeed(String positionX, String positionXOld, String positionY, String positionYOld, String timestamp, String timestampOld) {

        if (positionX == "0" || positionXOld == "0" || positionY == "0" || positionYOld == "0" || timestamp == "0" || timestampOld == "0")
            return "?"; //check for valid data before calculating
        if (positionX == "?" || positionXOld == "?" || positionY == "?" || positionYOld == "?" || timestamp == "?" || timestampOld == "?")
            return "?";

        double d2r = Math.PI / 180;
        double distance = 0;

        try{
            double dlong = (Float.valueOf(positionXOld) - Float.valueOf(positionX)) * d2r;
            double dlat = (Float.valueOf(positionYOld) - Float.valueOf(positionY)) * d2r;
            double a =
                    Math.pow(Math.sin(dlat / 2.0), 2)
                            + Math.cos(Float.valueOf(positionY) * d2r)
                            * Math.cos(Float.valueOf(positionYOld) * d2r)
                            * Math.pow(Math.sin(dlong / 2.0), 2);
            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
            double d = 6367 * c;

            long speed = Math.round(d / ((Float.valueOf(timestamp)-Float.valueOf(timestampOld))/3600000));

            return String.valueOf(speed);

        } catch(Exception e){
            e.printStackTrace();
            return "?";
        }
    }

    public void getSystemDate(){//reads system time and update time and date strings
        Calendar c = Calendar.getInstance();
        SimpleDateFormat date = new SimpleDateFormat("dd-MM-yy");
        this.date = date.format(c.getTime());
    }


    /**
     * Created by User on 18/05/2016.
     */

    public String getTipperInclination() {
        return tipperInclination;
    }

    public String getCompass() {
        return compass;
    }

    public String getSideInclination() {   return sideInclination; }

    public String getTimestamp() {
        return timestamp;
    }

    public String getTimestampOld() {
        return timestampOld;
    }

    public String getDate() {
        return date;
    }

    public String getPositionX() {
        return positionX;
    }

    public String getPositionY() {
        return positionY;
    }

    public String getPositionXOld() {
        return positionXOld;
    }

    public String getPositionYOld() {
        return positionYOld;
    }

    public String getSpeed() { return speed; }

    public String getAltitude() {
        return altitude;
    }

    public String getTemperature() {
        return temperature;
    }

    public String getTipperInclinationOld() { return tipperInclinationOld; }

    public String getCompassOld() {return compassOld; }

    public String getSideInclinationOld() {return sideInclinationOld; }

    public String getPressure() { return pressure; }

    public int getGpsErrorsInARow() { return gpsErrorsInARow;   }

    public int getImuErrorsInARow() { return imuErrorsInARow;   }

    public void setTipperInclinationOld(String tipperInclinationOld) { this.tipperInclinationOld = tipperInclinationOld;   }

    public void setCompassOld(String compassOld) {
        this.compassOld = compassOld;
    }

    public void setSideInclinationOld(String sideInclinationOld) { this.sideInclinationOld = sideInclinationOld; }
}
