package com.example.paul.scai_androidclient;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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

        applySettingsButton = (Button) view.findViewById(R.id.configApply);// get reference for the applyconfig button
        applySettingsButton.setOnClickListener(new View.OnClickListener() {// Its action hides the config menu (more to be added)
            public void onClick(View v) {


                selfRemove();
            }
        });

        cancelSettingsButton = (Button) view.findViewById(R.id.configApply);// get reference for the applyconfig button
        cancelSettingsButton.setOnClickListener(new View.OnClickListener() {// Its action hides the config menu (more to be added)
            public void onClick(View v) {
                selfRemove();
            }
        });

        return view;
    }

    private void selfRemove(){
        getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
    }
}
