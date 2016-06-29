package com.example.paul.scai_androidclient;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by root on 6/27/16.
 */
public class Settings extends Fragment {

    private Button applySettingsButton;// apply settings button
    private Button cancelSettingsButton;// cancel settings button
    private Button calibrateInclinometerButton;
    private Button calibrateCompassButton;
    private Button calibrateTemperatureButton;
    private Button calibratePressureButton;
    View view;
    SCAICore scaiCore;



    public static Settings newInstance(SCAICore scaiCore) {
        Settings settings = new Settings();
        settings.setScaiCore(scaiCore);

        return settings;
    }

    public Settings() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.settings_screen, container, false);

        loadCurrentSettings(view);// initialze setting menu variables with current configuration values

        //////cancel button initialization////////////
        applySettingsButton = (Button) view.findViewById(R.id.configApply);// get reference for the applyconfig button
        applySettingsButton.setOnClickListener(new View.OnClickListener() {// Its action hides the config menu (more to be added)
            public void onClick(View v) {// implement on click action
                applySettings();//apply settings changed by the user
                selfRemove();// destroy fragment from UI
            }
        });

        //////cancel button initialization////////////
        cancelSettingsButton = (Button) view.findViewById(R.id.configCancel);// get reference for the applyconfig button
        cancelSettingsButton.setOnClickListener(new View.OnClickListener() {// Its action destroys the config menu
            public void onClick(View v) {
                selfRemove();//destroy fragment from UI without saving
            }
        });
        //////calibrate compass button initialization////////////
        calibrateCompassButton = (Button) view.findViewById(R.id.calibrateCompassButton);// get reference for the applyconfig button
        calibrateCompassButton.setOnClickListener(new View.OnClickListener() {// Its action hides the config menu (more to be added)
            public void onClick(View v) {
                                try {
                    Thread.sleep(GlobalParams.IMU_UPDATE_INTERVAL);//waits until next sensor query is performed
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int compassAux = scaiCore.getCompass();//obtain current sensor value
                TextView auxTextView2 = (TextView) view.findViewById(R.id.calibrateCompassStatus);//obtains reference to status text in settings menu
                if(compassAux != GlobalParams.INVALID_INT_VALUE){// checks if value is valid
                    TextView auxTextView = (TextView) view.findViewById(R.id.calibrateCompassResult);//obtains reference to result text in settings menu
                    auxTextView.setText(GlobalParams.COMPASS_CALIBRATION-compassAux+"");//calculates calibration value and sets text on config ui
                    auxTextView2.setText(R.string.success);
                    auxTextView2.setTextColor(Color.parseColor("#4AEA3F"));// paint green
                }
                else{
                    auxTextView2.setText(R.string.failed);
                    auxTextView2.setTextColor(Color.parseColor("#FC331F"));// paint red
                }
            }
        });

        //////calibrate inclination button initialization////////////
        calibrateInclinometerButton = (Button) view.findViewById(R.id.calibrateInclinometerButton);// get reference for the applyconfig button
        calibrateInclinometerButton.setOnClickListener(new View.OnClickListener() {// Its action hides the config menu (more to be added)
            public void onClick(View v) {
                try {
                    Thread.sleep(GlobalParams.IMU_UPDATE_INTERVAL);//waits until next sensor query is performed
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int sideInclinationAux = scaiCore.getSideInclination();//obtain current sensor value
                int tipperInclinationAux = scaiCore.getTipperInclination();//obtain current sensor value

                TextView auxTextView2 = (TextView) view.findViewById(R.id.calibrateInclinationStatus);//obtains reference to status text in settings menu
                if(sideInclinationAux != GlobalParams.INVALID_INT_VALUE && tipperInclinationAux != GlobalParams.INVALID_INT_VALUE){// checks if value is valid
                    TextView auxTextView = (TextView) view.findViewById(R.id.calibrateSideInclinationResult);//obtains reference to result text in settings menu
                    auxTextView.setText(-(sideInclinationAux-GlobalParams.SIDE_INCLINATION_CALIBRATION)+"");//calculates calibration value and sets text on config ui
                    auxTextView = (TextView) view.findViewById(R.id.calibrateTipperInclinationResult);//obtains reference to result text in settings menu
                    auxTextView.setText(-(tipperInclinationAux - GlobalParams.TIPPER_INCLINATION_CALIBRATION)+"");//calculates calibration value and sets text on config ui

                    auxTextView2.setText(R.string.success);
                    auxTextView2.setTextColor(Color.parseColor("#4AEA3F"));// paint green
                }
                else{
                    auxTextView2.setText(R.string.failed);
                    auxTextView2.setTextColor(Color.parseColor("#FC331F"));// paint red
                }
            }
        });

        //////calibrate temperature button initialization////////////
        calibrateTemperatureButton = (Button) view.findViewById(R.id.calibrateTemperatureButton);// get reference for the applyconfig button
        calibrateTemperatureButton.setOnClickListener(new View.OnClickListener() {// Its action hides the config menu (more to be added)
            public void onClick(View v) {
                try {
                    Thread.sleep(GlobalParams.IMU_UPDATE_INTERVAL);//waits until next sensor query is performed
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                float temperatureAux = scaiCore.getTemperature();//obtain current sensor value
                EditText auxEditTest = (EditText) view.findViewById(R.id.currentTemperture);// get reference to edit text with current temperature
                TextView auxTextView2 = (TextView) view.findViewById(R.id.calibrateTemperatureStatus);//obtains reference to status text in settings menu
                if(temperatureAux != GlobalParams.INVALID_FLOAT_VALUE && isInteger(auxEditTest.getText().toString())){// checks if value is valid
                    TextView auxTextView = (TextView) view.findViewById(R.id.calibrateTemperatureResult);//obtains reference to result text in settings menu

                    auxTextView.setText(Integer.valueOf(auxEditTest.getText().toString())-GlobalParams.TEMPERATURE_CALIBRATION-temperatureAux+"");//calculates calibration value and sets text on config ui
                    auxTextView2.setText(R.string.success);
                    auxTextView2.setTextColor(Color.parseColor("#4AEA3F"));// paint green
                }
                else{
                    auxTextView2.setText(R.string.failed);
                    auxTextView2.setTextColor(Color.parseColor("#FC331F"));// paint red
                }
            }
        });

        //////calibrate pressure button initialization////////////
        calibratePressureButton = (Button) view.findViewById(R.id.calibratePressureButton);// get reference for the applyconfig button
        calibratePressureButton.setOnClickListener(new View.OnClickListener() {// Its action hides the config menu (more to be added)
            public void onClick(View v) {
                try {
                    Thread.sleep(GlobalParams.IMU_UPDATE_INTERVAL);//waits until next sensor query is performed
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                float pressureAux = scaiCore.getPressure();//obtain current sensor value
                EditText auxEditTest = (EditText) view.findViewById(R.id.currentPressure);// get reference to edit text with current temperature
                TextView auxTextView2 = (TextView) view.findViewById(R.id.calibratePressureStatus);//obtains reference to status text in settings menu

                if(pressureAux != GlobalParams.INVALID_FLOAT_VALUE && isInteger(auxEditTest.getText().toString())){// checks if value is valid
                    TextView auxTextView = (TextView) view.findViewById(R.id.calibratePressureResult);//obtains reference to result text in settings menu

                    auxTextView.setText(Integer.valueOf(auxEditTest.getText().toString())-GlobalParams.PRESSURE_CALIBRATION-pressureAux+"");//calculates calibration value and sets text on config ui
                    auxTextView2.setText(R.string.success);
                    auxTextView2.setTextColor(Color.parseColor("#4AEA3F"));// paint green
                }
                else{
                    auxTextView2.setText(R.string.failed);
                    auxTextView2.setTextColor(Color.parseColor("#FC331F"));// paint red
                }
            }
        });



        return view;
    }

    private void applySettings() {

        CheckBox auxCheckBox = (CheckBox) view.findViewById(R.id.tipperBeepEnabled);
        GlobalParams.TIPPER_UP_BEEP_ENABLED = auxCheckBox.isChecked();
        auxCheckBox = (CheckBox) view.findViewById(R.id.maxSpeedBeepEnabled);
        GlobalParams.SPEED_LIMIT_BEEP_ENABLED = auxCheckBox.isChecked();

        EditText auxEditText;
        auxEditText = (EditText) view.findViewById(R.id.isUpAngleValue);
        if(isFloat(auxEditText.getText().toString())) GlobalParams.MIN_TIPPER_UP_ANGLE =
                Math.round(Float.valueOf(auxEditText.getText().toString()));
        auxEditText = (EditText) view.findViewById(R.id.maximumSpeedTipperValue);
        if(isFloat(auxEditText.getText().toString())) GlobalParams.MAX_SPEED_WITH_TIPPER_UP =
                Math.round(Float.valueOf(auxEditText.getText().toString()));
        auxEditText = (EditText) view.findViewById(R.id.speedLimitValue);
        if(isFloat(auxEditText.getText().toString())) GlobalParams.SPEED_LIMIT =
                Math.round(Float.valueOf(auxEditText.getText().toString()));


        auxEditText = (EditText) view.findViewById(R.id.CamURL);
        GlobalParams.VIDEO_ADDRESS = auxEditText.getText().toString();
        auxEditText = (EditText) view.findViewById(R.id.GpsURL);
        GlobalParams.GPS_QUERY_ADDRESS = auxEditText.getText().toString();
        auxEditText = (EditText) view.findViewById(R.id.ImuURL);
        GlobalParams.IMU_QUERY_ADDRESS = auxEditText.getText().toString();

        TextView auxTextView;
        auxTextView = (TextView) view.findViewById(R.id.calibrateCompassResult);
        if(isInteger(auxTextView.getText().toString())) GlobalParams.COMPASS_CALIBRATION = Integer.valueOf(auxTextView.getText().toString());

        auxTextView = (TextView) view.findViewById(R.id.calibrateSideInclinationResult);
        if(isInteger(auxTextView.getText().toString())) GlobalParams.SIDE_INCLINATION_CALIBRATION = Integer.valueOf(auxTextView.getText().toString());

        auxTextView = (TextView) view.findViewById(R.id.calibrateTipperInclinationResult);
        if(isInteger(auxTextView.getText().toString()))  GlobalParams.TIPPER_INCLINATION_CALIBRATION = Integer.valueOf(auxTextView.getText().toString());

        auxTextView = (TextView) view.findViewById(R.id.calibrateTemperatureResult);
        if(isFloat(auxTextView.getText().toString()))  GlobalParams.TEMPERATURE_CALIBRATION = Float.valueOf(auxTextView.getText().toString());

        auxTextView = (TextView) view.findViewById(R.id.calibratePressureResult);
        if(isFloat(auxTextView.getText().toString()))  GlobalParams.PRESSURE_CALIBRATION = Float.valueOf(auxTextView.getText().toString());



        GlobalParams.savePreferences(getActivity());

    }

    private void loadCurrentSettings(View view) {
        CheckBox auxCheckBox = (CheckBox) view.findViewById(R.id.tipperBeepEnabled);
        auxCheckBox.setChecked(GlobalParams.TIPPER_UP_BEEP_ENABLED);
        auxCheckBox = (CheckBox) view.findViewById(R.id.maxSpeedBeepEnabled);
        auxCheckBox.setChecked(GlobalParams.SPEED_LIMIT_BEEP_ENABLED);

        EditText auxEditText;
        auxEditText = (EditText) view.findViewById(R.id.isUpAngleValue);
        auxEditText.setText(GlobalParams.MIN_TIPPER_UP_ANGLE+"");
        auxEditText = (EditText) view.findViewById(R.id.maximumSpeedTipperValue);
        auxEditText.setText(GlobalParams.MAX_SPEED_WITH_TIPPER_UP+"");
        auxEditText = (EditText) view.findViewById(R.id.speedLimitValue);
        auxEditText.setText(GlobalParams.SPEED_LIMIT+"");

        auxEditText = (EditText) view.findViewById(R.id.CamURL);
        auxEditText.setText(GlobalParams.VIDEO_ADDRESS);
        auxEditText = (EditText) view.findViewById(R.id.ImuURL);
        auxEditText.setText(GlobalParams.IMU_QUERY_ADDRESS);
        auxEditText = (EditText) view.findViewById(R.id.GpsURL);
        auxEditText.setText(GlobalParams.GPS_QUERY_ADDRESS);
        TextView auxTextView;
        auxTextView = (TextView) view.findViewById(R.id.calibrateInclinationCurrent);
        auxTextView.setText("X: "+GlobalParams.SIDE_INCLINATION_CALIBRATION+", Y: "+ GlobalParams.TIPPER_INCLINATION_CALIBRATION);
        auxTextView = (TextView) view.findViewById(R.id.calibrateCompassCurrent);
        auxTextView.setText(""+GlobalParams.COMPASS_CALIBRATION);
        auxTextView = (TextView) view.findViewById(R.id.calibratePressureCurrent);
        auxTextView.setText(""+GlobalParams.PRESSURE_CALIBRATION);
        auxTextView = (TextView) view.findViewById(R.id.calibrateTemperatureCurrent);
        auxTextView.setText(""+GlobalParams.TEMPERATURE_CALIBRATION);

    }

    private void selfRemove(){
        getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
    }

    public void setScaiCore(SCAICore scaiCore) {
        this.scaiCore = scaiCore;
    }

    public boolean isInteger( String input ) {
        try {
            Integer.parseInt( input );
            return true;
        }
        catch( Exception e ) {
            return false;
        }
    }

    public boolean isFloat( String input ) {
        try {
            Float.parseFloat( input );
            return true;
        }
        catch( Exception e ) {
            return false;
        }
    }
}
