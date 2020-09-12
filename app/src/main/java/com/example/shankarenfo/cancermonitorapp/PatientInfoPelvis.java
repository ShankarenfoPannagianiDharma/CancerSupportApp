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
 * This fragment shows a selection of available information for cancer variants in the Pelvic region.
 */
public class PatientInfoPelvis extends Fragment {


    public PatientInfoPelvis() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FrameLayout frame = (FrameLayout) inflater.inflate(R.layout.fragment_patient_info_pelvis, container, false);
        final LinearLayout Menu = (LinearLayout) frame.findViewById(R.id.ll_pitypes);
        final LinearLayout CancerVariantLung = (LinearLayout) frame.findViewById(R.id.ll_pivariantBladderC);
        final LinearLayout CancerVariantColorectal = (LinearLayout) frame.findViewById(R.id.ll_pivariantColorectal);
        final LinearLayout CancerVariantProstate = (LinearLayout) frame.findViewById(R.id.ll_pivariantProstate);
        final LinearLayout CancerVariantUterine = (LinearLayout) frame.findViewById(R.id.ll_pivariantUterine);

        //buttons to show infodump contents
        Button targetbtn = (Button) frame.findViewById(R.id.btn_pivBladderC);
        targetbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Menu.setVisibility(View.GONE);
                CancerVariantLung.setVisibility(View.VISIBLE);
            }
        });

        targetbtn = (Button) frame.findViewById(R.id.btn_pivColorectal);
        targetbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Menu.setVisibility(View.GONE);
                CancerVariantColorectal.setVisibility(View.VISIBLE);
            }
        });

        targetbtn = (Button) frame.findViewById(R.id.btn_pivProstate);
        targetbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Menu.setVisibility(View.GONE);
                CancerVariantProstate.setVisibility(View.VISIBLE);
            }
        });

        targetbtn = (Button) frame.findViewById(R.id.btn_pivUterine);
        targetbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Menu.setVisibility(View.GONE);
                CancerVariantUterine.setVisibility(View.VISIBLE);
            }
        });

        //buttons to return to category menu
        targetbtn = (Button) frame.findViewById(R.id.btn_pivariantBladderCBack);
        targetbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Menu.setVisibility(View.VISIBLE);
                CancerVariantLung.setVisibility(View.GONE);
            }
        });

        targetbtn = (Button) frame.findViewById(R.id.btn_pivariantColorectalBack);
        targetbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Menu.setVisibility(View.VISIBLE);
                CancerVariantColorectal.setVisibility(View.GONE);
            }
        });

        targetbtn = (Button) frame.findViewById(R.id.btn_pivariantProstateBack);
        targetbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Menu.setVisibility(View.VISIBLE);
                CancerVariantProstate.setVisibility(View.GONE);
            }
        });

        targetbtn = (Button) frame.findViewById(R.id.btn_pivariantUterineBack);
        targetbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Menu.setVisibility(View.VISIBLE);
                CancerVariantUterine.setVisibility(View.GONE);
            }
        });

        return frame;
    }

}
