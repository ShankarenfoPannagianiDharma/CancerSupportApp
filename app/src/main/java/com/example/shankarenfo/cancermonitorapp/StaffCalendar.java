package com.example.shankarenfo.cancermonitorapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shankarenfo.cancermonitorapp.R;

/**
 * This screen shows a visual representation of events that are in line for the staff.
 */

public class StaffCalendar extends Fragment {


    public StaffCalendar() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_staff_calendar, container, false);
    }

}
