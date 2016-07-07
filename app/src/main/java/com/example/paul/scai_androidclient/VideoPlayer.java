package com.example.paul.scai_androidclient;

/**
 * Created by root on 7/6/16.
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.github.niqdev.mjpeg.DisplayMode;
import com.github.niqdev.mjpeg.Mjpeg;
import com.github.niqdev.mjpeg.MjpegSurfaceView;
import com.github.niqdev.mjpeg.MjpegView;

/**
 * Created by root on 6/27/16.
 */
public class VideoPlayer extends Fragment {

    MjpegView mjpegView;
    private FrameLayout videoContainer;

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
        videoContainer = (FrameLayout) view.findViewById(R.id.videoMapContainer);// Get a reference for the map view container



        return view;
    }

    private void selfRemove(){
        getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
    }

    public void stop(){
        mjpegView.stopPlayback();
        //mjpegView.freeCameraMemory();
        getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
    }

    private void loadIpCam() {
        Mjpeg.newInstance()
                .open(GlobalParams.VIDEO_ADDRESS, GlobalParams.VIDEO_TIMEOUT)
                .subscribe(
                        inputStream -> {
                            mjpegView.setSource(inputStream);
                            mjpegView.setDisplayMode(DisplayMode.BEST_FIT);
                            mjpegView.showFps(true);
                        });
    }
}

