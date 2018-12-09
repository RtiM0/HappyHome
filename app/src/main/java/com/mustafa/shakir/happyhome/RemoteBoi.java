package com.mustafa.shakir.happyhome;


import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.ToggleButton;

import io.github.controlwear.virtual.joystick.android.JoystickView;


/**
 * A simple {@link Fragment} subclass.
 */
public class RemoteBoi extends Fragment implements View.OnClickListener {
    Button fo, bk, lt, rt, stp;
    RelativeLayout rel;
    JoystickView joystickView;
    ToggleButton toggleButton;
    int flagA = 0,flagB=0,flagC=0,flagD=0,flagE=0;

    public RemoteBoi() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myView = inflater.inflate(R.layout.fragment_remote_boi, container, false);
        fo = (Button) myView.findViewById(R.id.going);
        bk = (Button) myView.findViewById(R.id.backboi);
        lt = (Button) myView.findViewById(R.id.lt);
        rt = (Button) myView.findViewById(R.id.rt);
        stp = (Button) myView.findViewById(R.id.no);
        rel = (RelativeLayout)myView.findViewById(R.id.controlboi);
        toggleButton = (ToggleButton)myView.findViewById(R.id.toggleButton);
        joystickView = (JoystickView)myView.findViewById(R.id.joy);
        fo.setOnClickListener(this);
        bk.setOnClickListener(this);
        lt.setOnClickListener(this);
        rt.setOnClickListener(this);
        stp.setOnClickListener(this);
        toggleButton.setOnClickListener(this);
        joystickView.setOnMoveListener(new JoystickView.OnMoveListener() {
            @Override
            public void onMove(int angle, int strength) {
                if(strength>50 && flagA==0 && angle>45 && angle<=135){
                    ((FridayMain)getActivity()).goForward();
                    flagA = 1;
                    flagB = 0;
                    flagC = 0;
                    flagD = 0;
                    flagE = 0;
                }
                if(strength<=50 && flagB==0){
                    ((FridayMain)getActivity()).ploxstop();
                    flagA = 0;
                    flagB = 1;
                    flagC = 0;
                    flagD = 0;
                    flagE = 0;
                }
                if(angle>135 && angle <225 && strength>50 && flagC == 0){
                    ((FridayMain)getActivity()).turnLeft();
                    flagB = 0;
                    flagA = 0;
                    flagC = 1;
                    flagD = 0;
                    flagE = 0;
                }
                if((angle>315 && angle<360) || (angle>0 && angle <=45) && strength>50 && flagD == 0 ){
                    ((FridayMain)getActivity()).turnRight();
                    flagB = 0;
                    flagA = 0;
                    flagC = 0;
                    flagD = 1;
                    flagE = 0;
                }

                if(strength>50 && angle>225 && angle<315 && strength>50 && flagE == 0){
                    ((FridayMain)getActivity()).goBack();
                    flagB = 0;
                    flagA = 0;
                    flagC = 0;
                    flagD = 0;
                    flagE = 1;
                }
            }
        });
        joystickView.setVisibility(View.GONE);
        return myView;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.going) {
            ((FridayMain) getActivity()).goForward();
            ((FridayMain) getActivity()).vibrationboi();
        }
        if (v.getId() == R.id.backboi) {
            ((FridayMain) getActivity()).goBack();
            ((FridayMain) getActivity()).vibrationboi();
        }
        if (v.getId() == R.id.lt) {
            ((FridayMain) getActivity()).turnLeft();
            ((FridayMain) getActivity()).vibrationboi();
        }
        if (v.getId() == R.id.rt) {
            ((FridayMain) getActivity()).turnRight();
            ((FridayMain) getActivity()).vibrationboi();
        }
        if(v.getId() == R.id.no){
            ((FridayMain) getActivity()).ploxstop();
            ((FridayMain) getActivity()).vibrationboi();
        }
        if(v.getId() == R.id.toggleButton) {
            boolean on = toggleButton.isChecked();
            if(on){
                rel.setVisibility(View.GONE);
                joystickView.setVisibility(View.VISIBLE);
            }
            if(!on){
                rel.setVisibility(View.VISIBLE);
                joystickView.setVisibility(View.GONE);
            }
        }
    }
}
