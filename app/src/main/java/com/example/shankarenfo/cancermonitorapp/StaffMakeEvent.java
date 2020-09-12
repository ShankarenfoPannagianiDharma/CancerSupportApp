package com.example.shankarenfo.cancermonitorapp;


import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;


/**
 * This fragment is copypasted from StaffMakeRoutine.java- Functions almost identically,
 * but difference being that it makes one-off events instead of redoables.
 */
public class StaffMakeEvent extends Fragment {

    private String newEvent;
    private String patientacc;
    private String staffacc;

    public StaffMakeEvent() {}
    private Calendar calendar;
    private int cyear;
    private int cmonth;
    private int cday;
    private int chr;
    private int cmn;
    private int syear;
    private String smonth;
    private String sday;
    private String shr;
    private String smn;
    private DatePickerDialog dpicker;
    private TimePickerDialog tpicker;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final FrameLayout frame = (FrameLayout) inflater.inflate(R.layout.fragment_staff_make_event, container, false);
        calendar = Calendar.getInstance();
        cyear = calendar.get(Calendar.YEAR);
        cmonth = calendar.get(Calendar.MONTH);
        cday = calendar.get(Calendar.DAY_OF_MONTH);
        chr = calendar.get(Calendar.HOUR_OF_DAY);
        cmn = calendar.get(Calendar.MINUTE);

        //get user permission if not already (API 23+)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            int permission = getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if(permission != PackageManager.PERMISSION_GRANTED)
            {
                String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
                getActivity().requestPermissions(permissions,1);
            }
        }

        patientacc = getArguments().getString("PATIENTACC");
        staffacc = getArguments().getString("STAFFACC");

        //add onclick listeners
        //back button
        Button targetbutton = (Button) frame.findViewById(R.id.btn_cancel);
        targetbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //go back to patientdetails after operation
                Fragment fragment =  new StaffPatientDetail();
                Bundle bundle = new Bundle();
                bundle.putString("PATIENTACC", patientacc);
                fragment.setArguments(bundle);
                ((StaffView)getActivity()).switchfragment(fragment);
            }
        });

        //set time
        targetbutton = (Button) frame.findViewById(R.id.btn_settime);
        targetbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //create timepicker dialog  (ALWAYS 24 hour FORMAT!)
                tpicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String min = String.valueOf(minute);
                        String hr = String.valueOf(hourOfDay);
                        //transform input if needed (length is not two digits)
                        if(min.length() != 2)
                        {min = "0"+min;}
                        if(hr.length() != 2)
                        {hr = "0"+hr;}
                        smn = min;
                        shr = hr;
                        updateseltime(hr+":"+min);
                    }
                }, chr, cmn, true);
                //show dialog
                tpicker.show();
            }
        });

        //set date button
        targetbutton = (Button) frame.findViewById(R.id.btn_setdate);
        targetbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //create a datepicker dialog
                dpicker = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        //update ui with set date, as well as global val
                        sday = String.valueOf(dayOfMonth);
                        if(sday.length() < 2)
                        {sday = "0"+ dayOfMonth;}
                        smonth = String.valueOf(monthOfYear);
                        if(smonth.length() < 2)
                        {smonth = "0" + monthOfYear;}
                        syear = year;
                        updateseldate(dayOfMonth,monthOfYear,year);
                    }
                }, cyear,cmonth,cday);

                //show dialog
                dpicker.show();
            }
        });

        //create new routine for patient
        targetbutton = (Button) frame.findViewById(R.id.btn_makeroutine);
        targetbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get data from interface- make sure all fields are filled
                String EventName, EventDescription, EventTime;
                EditText rt = (EditText) frame.findViewById(R.id.txt_addroutinename);
                EventName = rt.getText().toString();
                rt = (EditText) frame.findViewById(R.id.txt_addroutinedesc);
                EventDescription = rt.getText().toString();
                String hour = shr;
                String mins = smn;
                 EventTime = hour+":"+mins;
                String EventDate = sday+"/"+smonth+"/"+syear;

                //make sure there are no blank spaces or empty strings
                if(EventDescription.trim().length() > 0 && EventName.trim().length() > 0 &&  EventTime.trim().length() > 0)
                {
                    //transform commas from name and description to avoid errors.
                    //REPLACE ',' WITH 'TY@JB' do the reverse when unpacking
                    EventName.replaceAll(",","TY@JB");
                    EventDescription.replaceAll(",","TY@JB");
                    //new EVENT string
                    newEvent = "\nEVENT," + EventDate + "," + EventTime+ "," + EventName + "," + EventDescription;

                    //create or append new event for patient and staff
                    new phpRewriteFile("schedules"+patientacc,newEvent).execute();
                    new phpRewriteFile("schedules"+staffacc,newEvent).execute();

                    //show confirmation
                    AlertDialog.Builder adb = new AlertDialog.Builder(getContext());
                    adb.setMessage("Event added to you and "+ patientacc);
                    adb.setTitle("Event created.");
                    //setup the dialogvox
                    AlertDialog ad = adb.create();
                    ad.show();

                    //go back to patientdetails after operation
                    Fragment fragment = new StaffPatientDetail();
                    Bundle bundle = new Bundle();
                    bundle.putString("PATIENTACC", patientacc);
                    bundle.putString("STAFFACC", staffacc);
                    fragment.setArguments(bundle);
                    ((StaffView) getActivity()).switchfragment(fragment);
                }
                else
                {
                    //if one of them Is empty, pop a message.
                    AlertDialog.Builder adb = new AlertDialog.Builder(getContext());
                    adb.setMessage("One or more fields are empty.");
                    adb.setTitle("Problem: All fields are required.");
                    //setup the dialogvox
                    AlertDialog ad = adb.create();
                    ad.show();
                }
            }
        });

        return frame;
    }

    private void updateseltime(String time)
    {
        TextView targettext = (TextView) this.getActivity().findViewById(R.id.txt_targettime);
        targettext.setText(time);
    }

    //method to update UI (selected date)
    private void updateseldate(int day, int month, int year)
    {
        TextView targettext = (TextView) this.getActivity().findViewById(R.id.txt_targetdate);
        targettext.setText(day+"/"+month+"/"+year);
    }

}
