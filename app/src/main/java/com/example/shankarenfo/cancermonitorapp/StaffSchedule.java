package com.example.shankarenfo.cancermonitorapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * This fragment shows the staff their appointed schedules by order of earliest first.
 */

public class StaffSchedule extends Fragment
{
    private static final int FILEDATA = 311;
    private ArrayList events;
    private ListView eventlist;
    private String currentuser;
    // Required empty public constructor
    public StaffSchedule()
    {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        FrameLayout frame = (FrameLayout) inflater.inflate(R.layout.fragment_staff_schedule, container, false);
        //build the list of patients for this specialist
        currentuser = getActivity().getIntent().getStringExtra("UserID");
        eventlist = (ListView) frame.findViewById(R.id.lv_staffscheduledappointments);

        //send intent to get data from server- the staff's schedule.
        Intent i = new Intent(getContext(),phpRetrieveFileActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("Filename","schedules"+currentuser+".txt");
        i.putExtras(bundle);
        startActivityForResult(i,FILEDATA);

        // Inflate the layout for this fragment
        return frame;
    }

    @Override
    public void onStop()
    {
        super.onStop();
        //send intent to get data from server- the staff's schedule.
        Intent i = new Intent(getContext(),phpRetrieveFileActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("Filename","schedules"+currentuser+".txt");
        i.putExtras(bundle);
        startActivityForResult(i,FILEDATA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent returnIntent)
    {
        //if result comes from request filedata and is ok
        if(requestCode == FILEDATA && resultCode == Activity.RESULT_OK)
        {
            //setup the contents of listview.
            Bundle results = returnIntent.getExtras();
            events = new ArrayList();
            String combineditems = results.getString("Contents");
            String[] items = combineditems.split("[|||]");
            for(String line : items)
            {events.add(line);}
            //attach adapter to listview
            if(events.size() != 0)
            {
                AdapterEventList eventadapter = new AdapterEventList(this.getContext(), events, currentuser);
                eventlist.setAdapter(eventadapter);
            }
        }
        else
        {}
    }

}
