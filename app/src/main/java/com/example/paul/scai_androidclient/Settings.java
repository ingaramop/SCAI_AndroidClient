package com.example.paul.scai_androidclient;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by root on 6/27/16.
 */
public class Settings extends Fragment {

    private Button applySettingsButton;// apply settings button
    private Button cancelSettingsButton;// cancel settings button
    View view;


    public static Settings newInstance() {
        return new Settings();
    }

    public Settings() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.settings_screen, container, false);

        loadCurrentSettings(view);

        applySettingsButton = (Button) view.findViewById(R.id.configApply);// get reference for the applyconfig button
        applySettingsButton.setOnClickListener(new View.OnClickListener() {// Its action hides the config menu (more to be added)
            public void onClick(View v) {

                applySettings();
                selfRemove();
            }
        });

        cancelSettingsButton = (Button) view.findViewById(R.id.configCancel);// get reference for the applyconfig button
        cancelSettingsButton.setOnClickListener(new View.OnClickListener() {// Its action hides the config menu (more to be added)
            public void onClick(View v) {
                selfRemove();
            }
        });

        return view;
    }

    private void applySettings() {
        EditText auxEditText;
        auxEditText = (EditText) view.findViewById(R.id.CamURL);
        GlobalParams.VIDEO_ADDRESS = auxEditText.getText().toString();
        auxEditText = (EditText) view.findViewById(R.id.GpsURL);
        GlobalParams.GPS_QUERY_ADDRESS = auxEditText.getText().toString();
        auxEditText = (EditText) view.findViewById(R.id.ImuURL);
        GlobalParams.IMU_QUERY_ADDRESS = auxEditText.getText().toString();

        GlobalParams.savePreferences(getActivity());

    }

    private void loadCurrentSettings(View view) {
        EditText auxEditText;
        auxEditText = (EditText) view.findViewById(R.id.CamURL);
        auxEditText.setText(GlobalParams.VIDEO_ADDRESS);
        auxEditText = (EditText) view.findViewById(R.id.ImuURL);
        auxEditText.setText(GlobalParams.IMU_QUERY_ADDRESS);
        auxEditText = (EditText) view.findViewById(R.id.GpsURL);
        auxEditText.setText(GlobalParams.GPS_QUERY_ADDRESS);
        TextView auxTextView;
        auxTextView = (TextView) view.findViewById(R.id.calibrateInclinationCurrent);
        auxTextView.setText("X: "+GlobalParams.TIPPER_INCLINATION_CALIBRATION+", Y: "+ GlobalParams.SIDE_INCLINATION_CALIBRATION);
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
}
