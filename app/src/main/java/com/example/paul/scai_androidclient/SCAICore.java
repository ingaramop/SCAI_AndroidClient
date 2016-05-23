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
    private String tipperInclination, compass, sideInclination, speed, positionX, positionY, altitude, temperature, time, date;
    final private static String SENSOR_QUERY = "http://192.168.1.106/cgi-bin/sensor_data.fcgi";
    private final String USER_AGENT = "Mozilla/5.0";

    public void SCAICore(){
        compass="ere";
        altitude="ere";
        temperature="ert";
        tipperInclination="dfg";
        sideInclination="gdf";
        speed="gdf";
        positionX="dgfd";
        positionY="gdf";
        time="gdf";
        date="dfg";

    }

    public void start() {
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                getSystemTime();
            }
        }, 0,5000);

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                getDataFromXML(HTTPGet(SENSOR_QUERY));
            }
        }, 0,2000);
    }

    private InputStream HTTPGet(String URL) {
        try {
            URL url = new URL(URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(800 /* milliseconds */);
            conn.setConnectTimeout(1000 /* milliseconds */);
            conn.setRequestMethod("GET");// optional default is GET
            conn.setRequestProperty("User-Agent", USER_AGENT);//add request header
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


    private void getDataFromXML(InputStream stream) {
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
                        if (name.equals("inclination")) {
                            tipperInclination = text;
                        } else if (name.equals("sideInclination")) {
                            sideInclination = text;
                        } else if (name.equals("altitude")) {
                            altitude = text;
                        } else if (name.equals("temperature")) {
                            temperature = text;
                        } else if (name.equals("orientation")) {
                            compass = text;
                        } else {
                        }
                        break;
                }
                event = myparser.next();
            }
            stream.close();
            return;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        compass="";
        altitude="";
        temperature="";
        tipperInclination="";
        sideInclination="";
    }

    public void getSystemTime(){//reads system time and update time and date strings
        Calendar c = Calendar.getInstance();
        //System.out.println("Current time => "+c.getTime());
        SimpleDateFormat date = new SimpleDateFormat("dd-MM-yy");
        SimpleDateFormat time = new SimpleDateFormat("HH:mm");
        this.date = date.format(c.getTime());
        this.time = time.format(c.getTime());
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

    public String getSideInclination() {
        return sideInclination;
    }

    public String getSpeed() {
        return speed;
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

    public String getAltitude() {
        return altitude;
    }

    public String getTemperature() {
        return temperature;
    }

    public String getTime() {
        return time;
    }
}
