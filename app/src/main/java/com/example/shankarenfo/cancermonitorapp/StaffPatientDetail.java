package com.example.shankarenfo.cancermonitorapp;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


/**
 * This fragment shows the details of a single specialist's patient,
 * as well as buttons to allow actions to that patient.
 */
public class StaffPatientDetail extends Fragment {

    private int GETPATIENTSCHED = 13;
    private String phonenum = "0";
    private String msg;
    private ListView lv;
    private String patientacc;
    private String staffacc;

    public StaffPatientDetail() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final FrameLayout frame = (FrameLayout) inflater.inflate(R.layout.fragment_staff_patient_detail, container, false);

        //grab data of the patient from database
        patientacc = getArguments().getString("PATIENTACC");
        staffacc = getArguments().getString("STAFFACC");

        //set data to interface
        ImageView pavatar = (ImageView) frame.findViewById(R.id.iv_patient_mugshot);
        TextView pname = (TextView) frame.findViewById(R.id.txt_patientname);
        TextView paccount = (TextView) frame.findViewById(R.id.txt_patientaccount);
        TextView pailment = (TextView) frame.findViewById(R.id.txt_patientailment);
        TextView pmail = (TextView) frame.findViewById(R.id.txt_patientmail);
        TextView pphone = (TextView) frame.findViewById(R.id.txt_patientphone);
        new phpStaffGetPatientD(getContext(), pname, paccount, pailment, pmail, pphone, pavatar).execute(patientacc);

        //populate listview of patient's schedule
        lv = (ListView) frame.findViewById(R.id.lv_patientschedule);
        final LinearLayout ll = (LinearLayout) frame.findViewById(R.id.ll_mainview);

        //send php to get patient's schedule
        Intent i = new Intent(getContext(),phpRetrieveFileActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("Filename","schedules"+patientacc+".txt");
        i.putExtras(bundle);
        startActivityForResult(i,GETPATIENTSCHED);

        //add onclick listeners
        Button targetbutton = (Button) frame.findViewById(R.id.btn_makecall);
        targetbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callpatient = new Intent(Intent.ACTION_CALL);
                callpatient.setData(Uri.parse("tel:"+phonenum));
                startActivity(callpatient);
            }
        });

        targetbutton = (Button) frame.findViewById(R.id.btn_makeevent);
        targetbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                //switch out to another fragment- StaffMakeEvent.
                Fragment newfrag = new StaffMakeEvent();
                Bundle bundle = new Bundle();
                bundle.putString("PATIENTACC",patientacc);
                bundle.putString("STAFFACC",staffacc);
                newfrag.setArguments(bundle);
                ((StaffView)getActivity()).switchfragment(newfrag);
            }
        });

        //button to make a repeated reminder and send it to patient
        targetbutton = (Button) frame.findViewById(R.id.btn_makeroutine);
        targetbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                //switch out to another fragment- StaffMakeRoutine.
                Fragment newfrag = new StaffMakeRoutine();
                Bundle bundle = new Bundle();
                bundle.putString("PATIENTACC",patientacc);
                newfrag.setArguments(bundle);
                ((StaffView)getActivity()).switchfragment(newfrag);
            }
        });
        //button to send simple sms message
        targetbutton = (Button) frame.findViewById(R.id.btn_makemessage);
        targetbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pop out dialogbox for sms content
                LayoutInflater li = LayoutInflater.from(getContext());
                View dialogbox = li.inflate(R.layout.dialogbox_inputtext,null);
                AlertDialog.Builder adb = new AlertDialog.Builder(getContext());
                adb.setView(dialogbox);
                final EditText msgcontent = (EditText) dialogbox.findViewById(R.id.txt_input);
                //setup the dialogvox
                adb.setCancelable(false)
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        setmsg(msgcontent.getText().toString());
                        //send sms message
                        SmsManager smsmessenger = SmsManager.getDefault();
                        smsmessenger.sendTextMessage(phonenum,null,msg,null,null);
                    }
                })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {dialog.cancel();}
                });
                AlertDialog ad = adb.create();
                ad.show();
            }
        });
        //button to view patient's schedule
        targetbutton = (Button) frame.findViewById(R.id.btn_ViewSchedule);
        targetbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                //check if action should be open or close
                if(lv.getVisibility() == View.GONE)
                {
                    lv.setVisibility(View.VISIBLE);
                    ll.setVisibility(View.GONE);
                }
                else
                {
                    lv.setVisibility(View.GONE);
                    ll.setVisibility(View.VISIBLE);
                }
            }
        });

        return frame;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent returnIntent)
    {
        //if result comes from request filedata and is ok
        if(requestCode == GETPATIENTSCHED && resultCode == Activity.RESULT_OK)
        {
            //setup the contents of listview.
            Bundle results = returnIntent.getExtras();
            ArrayList<String> events = new ArrayList();
            String combineditems = results.getString("Contents");
            String[] items = combineditems.split("[|||]");
            for(String line : items)
            {events.add(line);}
            //attach adapter to listview
            AdapterEventList eventadapter = new AdapterEventList(this.getContext(), events, patientacc);
            lv.setAdapter(eventadapter);

        }
    }

    //method for dialogbox to set message
    public void setmsg(String msg)
    {this.msg = msg;}
}
