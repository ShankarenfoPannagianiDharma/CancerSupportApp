package com.example.shankarenfo.cancermonitorapp;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * This interface provides the user with list of patients under their care.
 * Clicking on an item redirects to another interface that shows details of patient as well as
 * actions to be done to patient (contact, mail, call, make an event/routine)
 */

public class StaffPatients extends Fragment {


    public StaffPatients() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FrameLayout fragmentlayout = (FrameLayout) inflater.inflate(R.layout.fragment_staff_patients, container, false);

        ListView patientlisting = (ListView) fragmentlayout.findViewById(R.id.lv_specialistpatients);
        //fill in the ui credentials of the patient
        final String currentuser = getArguments().getString("UserID");
        //get list of patients from database, attach a listadapter to listview
        new phpStaffGetPatientList(getContext(), patientlisting).execute(currentuser);


        //add listener to listview
        patientlisting.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //when clicked, open another fragment StaffPatientDetail
                TextView patientname = (TextView) view.findViewById(R.id.txt_patientaccount);
                String patient = patientname.getText().toString();
                Fragment fragment =  new StaffPatientDetail();
                Bundle bundle = new Bundle();
                bundle.putString("PATIENTACC",patient);
                bundle.putString("STAFFACC",currentuser);
                fragment.setArguments(bundle);
                ((StaffView)getActivity()).switchfragment(fragment);
            }
        });

        return fragmentlayout;
    }
}
