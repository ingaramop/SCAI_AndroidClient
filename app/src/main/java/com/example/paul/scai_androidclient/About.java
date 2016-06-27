package com.example.paul.scai_androidclient;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

/**
 * Created by root on 6/27/16.
 */
public class About extends Fragment {

    private Button aboutOkButton;// done viewing about menu
    View view;


    public static About newInstance() {
        return new About();
    }

    public About() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.about_screen, container, false);

        aboutOkButton = (Button) view.findViewById(R.id.aboutOkButton);// get reference for the applyconfig button
        aboutOkButton.setOnClickListener(new View.OnClickListener() {// Its action hides the config menu (more to be added)
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
