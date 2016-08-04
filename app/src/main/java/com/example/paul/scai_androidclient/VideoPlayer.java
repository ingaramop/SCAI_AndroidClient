package com.example.paul.scai_androidclient;

/**
 * Created by root on 7/6/16.
 */

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.github.niqdev.mjpeg.DisplayMode;
import com.github.niqdev.mjpeg.Mjpeg;
import com.github.niqdev.mjpeg.MjpegSurfaceView;
import com.github.niqdev.mjpeg.MjpegView;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by root on 6/27/16.
 */
public class VideoPlayer extends Fragment {

    MjpegView mjpegView;
    private FrameLayout videoContainer;
    RelativeLayout standByScreen;

    private ImageButton btn_cam_up, btn_cam_down, btn_cam_left, btn_cam_right;

    View view;


    public static VideoPlayer newInstance() {
        return new VideoPlayer();
    }

    public VideoPlayer() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.video_player_screen, container, false);
        mjpegView = (MjpegSurfaceView) view.findViewById(R.id.mjpegView);
        loadIpCam();
        videoContainer = (FrameLayout) view.findViewById(R.id.videoLayout);// Get a reference for the map view container
        standByScreen = (RelativeLayout) view.findViewById(R.id.standByScreen);


        btn_cam_up = (ImageButton) view.findViewById(R.id.camUpButton);
        btn_cam_down = (ImageButton) view.findViewById(R.id.camDownButton);
        btn_cam_left = (ImageButton) view.findViewById(R.id.camLeftButton);
        btn_cam_right = (ImageButton) view.findViewById(R.id.camRightButton);

        btn_cam_up.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                //Toast.makeText(getActivity(), "Up", Toast.LENGTH_LONG).show();
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    //Log.d(TAG, "Camp Up");
                    String URL = GlobalParams.CAMERA_CONTROL_URL + "0";
                    new WebPageTask().execute(URL);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    //Log.d(TAG, "Camp Down");
                    String URL = GlobalParams.CAMERA_CONTROL_URL + "1";
                    new WebPageTask().execute(URL);
                }
                return false;
            }
        });

        btn_cam_down.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    String URL = GlobalParams.CAMERA_CONTROL_URL + "2";
                    new WebPageTask().execute(URL);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    String URL = GlobalParams.CAMERA_CONTROL_URL + "3";
                    new WebPageTask().execute(URL);
                }
                return false;
            }
        });

        btn_cam_left.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    String URL = GlobalParams.CAMERA_CONTROL_URL + "6";
                    new WebPageTask().execute(URL);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    String URL = GlobalParams.CAMERA_CONTROL_URL + "7";
                    new WebPageTask().execute(URL);
                }
                return false;
            }
        });

        btn_cam_right.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    String URL = GlobalParams.CAMERA_CONTROL_URL + "4";
                    new WebPageTask().execute(URL);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    String URL = GlobalParams.CAMERA_CONTROL_URL + "5";
                    new WebPageTask().execute(URL);
                }
                return false;
            }
        });

        return view;
    }

    private void selfRemove(){
        getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
    }

    public void stop(){
        standByScreen.setVisibility(View.VISIBLE);
        mjpegView.stopPlayback();
        //mjpegView.freeCameraMemory();
        getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
    }

    private class WebPageTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            for (String url : urls) {
                DefaultHttpClient client = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(url);
                try {
                    HttpResponse execute = client.execute(httpGet);
                    InputStream content = execute.getEntity().getContent();

                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s = "";
                    while ((s = buffer.readLine()) != null) {
                        response += s;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return response;
        }
    }

    private void loadIpCam() {
        Mjpeg.newInstance()
                .open(GlobalParams.VIDEO_ADDRESS, GlobalParams.VIDEO_TIMEOUT)
                .subscribe(
                        inputStream -> {
                            mjpegView.setSource(inputStream);
                            mjpegView.setDisplayMode(DisplayMode.BEST_FIT);
                            mjpegView.showFps(true);
                        },
                        throwable -> {
                            Log.e(getClass().getSimpleName(), "mjpeg error", throwable);
                            Toast.makeText(getActivity(), "Error", Toast.LENGTH_LONG).show();
                        });
    }
}

