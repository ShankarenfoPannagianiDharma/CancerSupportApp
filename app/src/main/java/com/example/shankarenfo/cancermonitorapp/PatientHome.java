package com.example.shankarenfo.cancermonitorapp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * This is the main fragment that the user sees when the activity is loaded.
 * This is where the list of schedules is listed, as well as links to additional informations.
 */

public class PatientHome extends Fragment {

    private static final int FILEDATA = 311;
    private ArrayList events;
    private ListView eventlist;
    private String currentuser;
    private AdapterEventList eventadapter;

    public PatientHome() {// Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Define XML file for this fragment
        FrameLayout frame = (FrameLayout) inflater.inflate(R.layout.fragment_patient_home, container, false);

        Bundle bndl = new Bundle();
        currentuser = getActivity().getIntent().getStringExtra("UserID");
        bndl.putString("TargetFile",currentuser);

        eventlist = (ListView) frame.findViewById(R.id.lv_scheduleview);
        //send intent to get data from server- the patient's schedule.
        Intent i = new Intent(getContext(),phpRetrieveFileActivity.class);
        bndl = new Bundle();
        bndl.putString("Filename","schedules"+currentuser+".txt");
        i.putExtras(bndl);
        startActivityForResult(i,FILEDATA);

        return  frame;
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View v, Bundle savedInstanceState)
    {
        // Setup any handles to view objects here
        // EditText etFoo = (EditText) view.findViewById(R.id.etFoo);
        Button tb = (Button) v.findViewById(R.id.btn_ExpandSchedule);
        tb.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {ExpandSchedule(v);}
        });

        tb = (Button) v.findViewById(R.id.btn_createnotifs);
        tb.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {constructnotifications(v);}
        });

        //onclick listener for more info buttons- opens a new fragment showcasing info
        //switch to a detailed menu a about a selection of items in the respective region.
        tb= (Button) v.findViewById(R.id.btn_patientmenumoreinfohead);
        tb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Class fragmentclass = PatientInfoHead.class;
                ((PatientView)getActivity()).changeInterface(fragmentclass);
            }
        });
        tb= (Button) v.findViewById(R.id.btn_patientmenumoreinfoabdomen);
        tb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Class fragmentclass = PatientInfoAbdomen.class;
                ((PatientView)getActivity()).changeInterface(fragmentclass);
            }
        });
        tb= (Button) v.findViewById(R.id.btn_patientmenumoreinfopelvis);
        tb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Class fragmentclass = PatientInfoPelvis.class;
                ((PatientView)getActivity()).changeInterface(fragmentclass);
            }
        });
        tb= (Button) v.findViewById(R.id.btn_patientmenumoreinfobody);
        tb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Class fragmentclass = PatientInfoBody.class;
                ((PatientView)getActivity()).changeInterface(fragmentclass);
            }
        });
        tb= (Button) v.findViewById(R.id.btn_patientmenumoreinfothorax);
        tb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Class fragmentclass = PatientInfoThorax.class;
                ((PatientView)getActivity()).changeInterface(fragmentclass);
            }
        });

        tb= (Button) v.findViewById(R.id.btn_patientmenumoreinfogeneral);
        tb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Class fragmentclass = PatientInfoGeneral.class;
                ((PatientView)getActivity()).changeInterface(fragmentclass);
            }
        });
    }

    public void constructnotifications(View v) {
        //make a notification through service if there is an event
        if (events != null || events.size() != 0)
        {
            //delete pending notifications
            ((PatientView) getActivity()).cancelNotifications();
            //build new notifications
            ((PatientView) getActivity()).createNotifications(events);
        }
    }

    //method activates whem button to expand the schedule listview is activated
    //operates on component level
    public void ExpandSchedule(View v)
    {
        //get components to manipulate- as by this point the fragment is already in the activity,
        //use getActivity instead of View.
        ListView schedulescreen     = (ListView) getActivity().findViewById(R.id.lv_scheduleview);
        TableLayout moreinfoscreen  = (TableLayout) getActivity().findViewById(R.id.tbl_cancerinfo);
        Button readmore             = (Button) getActivity().findViewById(R.id.btn_ExpandSchedule);
        TextView infolabel          = (TextView) getActivity().findViewById(R.id.lbl_cancerinfo);

        //if not hidden, hide MoreInfo LV and setup button and ScheduleScreen
        if(moreinfoscreen.getVisibility() != v.GONE)
        {
            moreinfoscreen.setVisibility(v.GONE);
            infolabel.setVisibility(v.GONE);
            readmore.setText("Shrink");
        }
        else
        {
            moreinfoscreen.setVisibility(v.VISIBLE);
            infolabel.setVisibility(v.VISIBLE);
            readmore.setText("Expand");
        }
    }//end expandschedule method

    //when resumed, refresh the data.
    @Override
    public void onStop()
    {
        super.onStop();
        Intent i = new Intent(getContext(),phpRetrieveFileActivity.class);
        Bundle bndl = new Bundle();
        bndl.putString("Filename","schedules"+currentuser+".txt");
        i.putExtras(bndl);
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
            String combineditems = results.getString("Contents");
            if(combineditems.length() != 0 && !combineditems.isEmpty())
            {events = new ArrayList(Arrays.asList(combineditems.split("[|||]")));}
            else
            {events = new ArrayList();}
            //attach adapter to listview if there's item in it
            if(events.size() != 0 || events != null)
            {
                eventadapter = new AdapterEventList(this.getContext(), events, currentuser);
                eventlist.setAdapter(eventadapter);
            }

        }
    }


}

