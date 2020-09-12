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
 *  This fragment shows a selection of available information for cancer variants in the whole body region.
 */
public class PatientInfoBody extends Fragment {


    public PatientInfoBody() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FrameLayout frame = (FrameLayout) inflater.inflate(R.layout.fragment_patient_info_body, container, false);
        final LinearLayout Menu = (LinearLayout) frame.findViewById(R.id.ll_pitypes);
        final LinearLayout CancerVariantLung = (LinearLayout) frame.findViewById(R.id.ll_pivariantLymphoma);
        final LinearLayout CancerVariantSkin = (LinearLayout) frame.findViewById(R.id.ll_pivariantSkinCancer);

        //buttons to show infodump contents
        Button targetbtn = (Button) frame.findViewById(R.id.btn_pivLymphoma);
        targetbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Menu.setVisibility(View.GONE);
                CancerVariantLung.setVisibility(View.VISIBLE);
            }
        });

        targetbtn = (Button) frame.findViewById(R.id.btn_pivSkinCancer);
        targetbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Menu.setVisibility(View.GONE);
                CancerVariantSkin.setVisibility(View.VISIBLE);
            }
        });

        //buttons to return to category menu
        targetbtn = (Button) frame.findViewById(R.id.btn_pivariantLymphomaBack);
        targetbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Menu.setVisibility(View.VISIBLE);
                CancerVariantLung.setVisibility(View.GONE);
            }
        });

        targetbtn = (Button) frame.findViewById(R.id.btn_pivariantSkinCancerBack);
        targetbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Menu.setVisibility(View.VISIBLE);
                CancerVariantSkin.setVisibility(View.GONE);
            }
        });


        return frame;
    }

}
