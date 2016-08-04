package com.example.paul.scai_androidclient;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.github.niqdev.mjpeg.DisplayMode;
import com.github.niqdev.mjpeg.Mjpeg;
import com.github.niqdev.mjpeg.MjpegSurfaceView;
import com.github.niqdev.mjpeg.MjpegView;

import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlay;
import org.osmdroid.views.overlay.MinimapOverlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.ScaleBarOverlay;

import java.util.ArrayList;

/**
 * Created by root on 7/7/16.
 */
public class Map extends Fragment {

    private MapView map;// map view
    private IMapController mapController;
    private RelativeLayout mapContainer;
    private ArrayList<OverlayItem> items;
    private ItemizedOverlay<OverlayItem> mMyLocationOverlay;
    private ImageButton pinButton;
    private boolean isMapPinned;

    public boolean isRunning() {
        return isRunning;
    }

    private boolean isRunning;

    View view;


    public static Map newInstance() {
        return new Map();
    }

    public Map() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.map_screen, container, false);
        map = (MapView) view.findViewById(R.id.map);
        initializeMap();
        isRunning = true;
        return view;
    }

    private void initializeMap() {
        ////////MAP VIEW INITIALIZATION////////////
        mapContainer = (RelativeLayout) view.findViewById(R.id.mapContainer);// Get a reference for the map view container
        pinButton = (ImageButton) view.findViewById(R.id.pinButton);// get reference for the pin/unpin button
        pinButton.setImageDrawable(getResources().getDrawable(getResources().getIdentifier("@drawable/unpin", null, getActivity().getPackageName()))); //starts pinned
        isMapPinned = true;//starts Pinned
        pinButton.setOnClickListener(new View.OnClickListener() {// Its action makes visible the configuration menu
            public void onClick(View v) {
                if(isMapPinned){
                    isMapPinned = false;
                    pinButton.setImageDrawable(getResources().getDrawable(getResources().getIdentifier("@drawable/pin", null, getActivity().getPackageName()))); //starts pinned
                }
                else{
                    isMapPinned = true;
                    pinButton.setImageDrawable(getResources().getDrawable(getResources().getIdentifier("@drawable/unpin", null, getActivity().getPackageName()))); //starts pinned
                }
            }
        });

        map = (MapView) view.findViewById(R.id.map);// Get a reference for the map view
        map.setTileSource(TileSourceFactory.MAPQUESTOSM);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
        mapController = map.getController();
        //mapController.setInvertedTiles(true);
        map.setTileSource(new XYTileSource("MapQuest", 0, 18, 256, ".jpg", new String[] {
                "http://otile1.mqcdn.com/tiles/1.0.0/map/",
                "http://otile2.mqcdn.com/tiles/1.0.0/map/",
                "http://otile3.mqcdn.com/tiles/1.0.0/map/",
                "http://otile4.mqcdn.com/tiles/1.0.0/map/"}));
        mapController.setZoom(17);
        map.setUseDataConnection(false); //keeps the mapView from loading online tiles using network connection



        //// minimap overlay
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        MinimapOverlay mMinimapOverlay;
        mMinimapOverlay = new MinimapOverlay(getActivity().getApplicationContext(), map.getTileRequestCompleteHandler());
        mMinimapOverlay.setWidth(dm.widthPixels / 5);
        mMinimapOverlay.setHeight(dm.heightPixels / 5);
        mMinimapOverlay.setZoomDifference(4);
//optionally, you can set the minimap to a different tile source
//mMinimapOverlay.setTileSource(....);
        map.getOverlays().add(mMinimapOverlay);

        //////map markers
        GeoPoint startPosition = new GeoPoint(-31.435253, -64.193881);
        mapController.setCenter(startPosition);
        items = new ArrayList<OverlayItem>();
        items.add(new OverlayItem("Title", "Description", startPosition)); // Lat/Lon decimal degrees
        mMyLocationOverlay = new ItemizedIconOverlay<OverlayItem>(items, new Glistener(), map.getResourceProxy());
        map.getOverlays().add(mMyLocationOverlay);
        //map.invalidate();




        ///////////map scale bar
        ScaleBarOverlay mScaleBarOverlay = new ScaleBarOverlay(map);
        mScaleBarOverlay.enableScaleBar();
        map.getOverlayManager().add(mScaleBarOverlay);


    }

    private void selfRemove(){
        getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
    }

    public void stop(){
        isRunning = false;
        getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
    }



    public void updatePosition(GeoPoint currentPosition) {
        //mapController.setCenter(startPoint);
        map.getOverlays().remove(mMyLocationOverlay);
        items.clear();
        if (isMapPinned)mapController.animateTo(currentPosition);
        items.add(new OverlayItem("Title", "Description", currentPosition)); // Lat/Lon decimal degrees
        mMyLocationOverlay = new ItemizedIconOverlay<OverlayItem>(items, new Glistener(), map.getResourceProxy());

        map.getOverlays().add(mMyLocationOverlay);
        map.invalidate();
    }


    class Glistener implements ItemizedIconOverlay.OnItemGestureListener<OverlayItem> {
        @Override
        public boolean onItemLongPress(int index, OverlayItem item) {
            return false;
        }

        @Override
        public boolean onItemSingleTapUp(int index, OverlayItem item) {
            return true; // We 'handled' this event.

        }

    }
}

