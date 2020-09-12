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
 *  This fragment shows a selection of available information for cancer variants in the Abdominal region.
 */
public class PatientInfoAbdomen extends Fragment {


    public PatientInfoAbdomen() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FrameLayout frame = (FrameLayout) inflater.inflate(R.layout.fragment_patient_info_abdomen, container, false);

        final LinearLayout Menu = (LinearLayout) frame.findViewById(R.id.ll_pitypes);
        final LinearLayout CancerVariantStomach = (LinearLayout) frame.findViewById(R.id.ll_pivariantStomach);
        final LinearLayout CancerVariantPancreas = (LinearLayout) frame.findViewById(R.id.ll_pivariantPancreas);

        //buttons to show infodump contents
        Button targetbtn = (Button) frame.findViewById(R.id.btn_pivStomach);
        targetbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Menu.setVisibility(View.GONE);
                CancerVariantStomach.setVisibility(View.VISIBLE);
            }
        });

        targetbtn = (Button) frame.findViewById(R.id.btn_pivPancreas);
        targetbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Menu.setVisibility(View.GONE);
                CancerVariantPancreas.setVisibility(View.VISIBLE);
            }
        });

        //buttons to return to category menu
        targetbtn = (Button) frame.findViewById(R.id.btn_pivariantStomachBack);
        targetbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Menu.setVisibility(View.VISIBLE);
                CancerVariantStomach.setVisibility(View.GONE);
            }
        });

        targetbtn = (Button) frame.findViewById(R.id.btn_pivariantPancreasBack);
        targetbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Menu.setVisibility(View.VISIBLE);
                CancerVariantPancreas.setVisibility(View.GONE);
            }
        });

        return frame;
    }

}
