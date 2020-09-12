package com.example.shankarenfo.cancermonitorapp;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * The fragment that shows the patient's assigned specialist
 * data is taken from database.
 */

public class PatientSpecialist  extends Fragment {

    public PatientSpecialist()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        FrameLayout fragmentlayout = (FrameLayout) inflater.inflate(R.layout.fragment_patient_specialist, container, false);

        //fill in the ui credentials of the patient
        String currentuser = getArguments().getString("UserID");

        ImageView specialistavatar = (ImageView) fragmentlayout.findViewById(R.id.iv_doctor_mugshot);
        TextView specialistname = (TextView) fragmentlayout.findViewById(R.id.txt_doctorname);
        TextView specialistmail = (TextView) fragmentlayout.findViewById(R.id.txt_patientmail);
        Button specialistcall = (Button) fragmentlayout.findViewById(R.id.btn_patientcall);

        //get user data from database
        new phpPatientGetSpecialist(getContext(), specialistname, specialistmail, specialistcall, specialistavatar).execute(currentuser);

        //add onclick listener for phone call button
        specialistcall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {patientcall(v);}
        });

        // Inflate the layout for this fragment
        return fragmentlayout;
    }

    //when the specialist's number is clicked, it will make a call to them
    public void patientcall(View view)
    {
        Button specialistcall = (Button) this.getView().findViewById(R.id.btn_patientcall);
        String phonenumb = specialistcall.getText().toString();
        Intent phonecall = new Intent(Intent.ACTION_CALL);
        phonecall.setData(Uri.parse("tel:"+phonenumb));
        startActivity(phonecall);
    }
}
