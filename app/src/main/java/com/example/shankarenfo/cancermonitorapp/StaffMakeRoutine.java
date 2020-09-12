package com.example.shankarenfo.cancermonitorapp;

import android.Manifest;
import android.app.TimePickerDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * This is the java file for staff- make a routine for the patient.
 * Contains two parts, one for MAKING the routine through UI and one to SEND the routine file.
 */
public class StaffMakeRoutine extends Fragment
{
    Calendar calendar = Calendar.getInstance();
    private TimePickerDialog tpicker;
    private int chr, cmn;
    String stm;

    public StaffMakeRoutine()
    {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final FrameLayout frame = (FrameLayout) inflater.inflate(R.layout.fragment_staff_make_routine, container, false);

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

        //add onclick listeners
        //back button
        Button targetbutton = (Button) frame.findViewById(R.id.btn_cancel);
        targetbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //go back to patientdetails after operation
                Fragment fragment =  new StaffPatientDetail();
                Bundle bundle = new Bundle();
                bundle.putString("PATIENTACC",getArguments().getString("PATIENTACC"));
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
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute)
                    {
                        String min = String.valueOf(minute);
                        String hr = String.valueOf(hourOfDay);
                        //transform input if needed (length is not two digits)
                        if(min.length() != 2)
                        {min = "0"+min;}
                        if(hr.length() != 2)
                        {hr = "0"+hr;}
                        stm = hr+":"+min;
                        updateseltime(stm);
                    }
                }, chr, cmn, true);
                //show dialog
                tpicker.show();
            }
        });

        //create new routine for patient
        targetbutton = (Button) frame.findViewById(R.id.btn_makeroutine);
        targetbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get data from interface- make sure all fields are filled
                String routinename, routinedesc, routinetime;
                EditText rt = (EditText) frame.findViewById(R.id.txt_addroutinename);
                routinename = rt.getText().toString();
                rt = (EditText) frame.findViewById(R.id.txt_addroutinedesc);
                routinedesc = rt.getText().toString();
                String filename = "schedules"+getArguments().getString("PATIENTACC");

                //make sure there are no blank spaces or empty strings
                if(routinedesc.trim().length() > 0 && routinename.trim().length() > 0 && stm != null)
                {
                    //transform commas from name and description to avoid errors.
                    //REPLACE ',' WITH 'TYJB' do the reverse when unpacking
                    routinename.replaceAll(",","TY@JB");
                    routinedesc.replaceAll(",","TY@JB");
                    //new ROUTINE line
                    String newRoutine = "RTINE," + stm + "," + routinename + "," + routinedesc;

                    //insert new routine to patientschedule
                    new phpRewriteFile(filename, newRoutine).execute();

                    //show confirmation
                    AlertDialog.Builder adb = new AlertDialog.Builder(getContext());
                    adb.setMessage("Routine added to Patient.");
                    adb.setTitle("Routine created.");
                    //setup the dialogvox
                    AlertDialog ad = adb.create();
                    ad.show();

                    //go back to patientdetails after operation
                    Fragment fragment = new StaffPatientDetail();
                    Bundle bundle = new Bundle();
                    bundle.putString("PATIENTACC", getArguments().getString("PATIENTACC"));
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
}
