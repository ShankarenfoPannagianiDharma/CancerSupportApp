package com.example.shankarenfo.cancermonitorapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.ButtonBarLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;


/**
 * A simple {@link Fragment} subclass.
 */
public class PatientInfoThorax extends Fragment {


    public PatientInfoThorax() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        FrameLayout frame = (FrameLayout) inflater.inflate(R.layout.fragment_patient_info_thorax, container, false);
        final LinearLayout Menu = (LinearLayout) frame.findViewById(R.id.ll_pitypes);
        final LinearLayout CancerVariantLung = (LinearLayout) frame.findViewById(R.id.ll_pivariantLungC);
        final LinearLayout CancerVariantBreast = (LinearLayout) frame.findViewById(R.id.ll_pivariantBreast);
        final LinearLayout CancerVariantThyroid = (LinearLayout) frame.findViewById(R.id.ll_pivariantThyroid);

        //buttons to show infodump contents
        Button targetbtn = (Button) frame.findViewById(R.id.btn_pivLungC);
        targetbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Menu.setVisibility(View.GONE);
                CancerVariantLung.setVisibility(View.VISIBLE);
            }
        });

        targetbtn = (Button) frame.findViewById(R.id.btn_pivBreast);
        targetbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Menu.setVisibility(View.GONE);
                CancerVariantBreast.setVisibility(View.VISIBLE);
            }
        });

        targetbtn = (Button) frame.findViewById(R.id.btn_pivThyroid);
        targetbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Menu.setVisibility(View.GONE);
                CancerVariantThyroid.setVisibility(View.VISIBLE);
            }
        });

        //buttons to return to category menu
        targetbtn = (Button) frame.findViewById(R.id.btn_pivariantLungCBack);
        targetbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Menu.setVisibility(View.VISIBLE);
                CancerVariantLung.setVisibility(View.GONE);
            }
        });

        targetbtn = (Button) frame.findViewById(R.id.btn_pivariantBreastBack);
        targetbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Menu.setVisibility(View.VISIBLE);
                CancerVariantBreast.setVisibility(View.GONE);
            }
        });

        targetbtn = (Button) frame.findViewById(R.id.btn_pivariantThyroidBack);
        targetbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Menu.setVisibility(View.VISIBLE);
                CancerVariantThyroid.setVisibility(View.GONE);
            }
        });

        return frame;
    }

}
