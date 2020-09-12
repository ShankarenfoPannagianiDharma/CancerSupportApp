package com.example.shankarenfo.cancermonitorapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

/**
 * This fragment shows a selection of available information for cancer variants in the HEAD region.
 */

public class PatientInfoHead extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        FrameLayout frame = (FrameLayout) inflater.inflate(R.layout.fragment_patient_info_head, container, false);

        final LinearLayout Menu = (LinearLayout) frame.findViewById(R.id.ll_pitypes);
        final LinearLayout CancerVariantHnN = (LinearLayout) frame.findViewById(R.id.ll_pivariantHnN);
        final LinearLayout CancerVariantEye = (LinearLayout) frame.findViewById(R.id.ll_pivariantEye);

        //buttons to show infodump contents
        Button targetbtn = (Button) frame.findViewById(R.id.btn_pivHnN);
        targetbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Menu.setVisibility(View.GONE);
                CancerVariantHnN.setVisibility(View.VISIBLE);
            }
        });

        targetbtn = (Button) frame.findViewById(R.id.btn_pivEye);
        targetbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Menu.setVisibility(View.GONE);
                CancerVariantEye.setVisibility(View.VISIBLE);
            }
        });

        //buttons to return to category menu
        targetbtn = (Button) frame.findViewById(R.id.btn_pivariantHnNBack);
        targetbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Menu.setVisibility(View.VISIBLE);
                CancerVariantHnN.setVisibility(View.GONE);
            }
        });

        targetbtn = (Button) frame.findViewById(R.id.btn_pivariantEyeBack);
        targetbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Menu.setVisibility(View.VISIBLE);
                CancerVariantEye.setVisibility(View.GONE);
            }
        });

        return frame;
    }

}
