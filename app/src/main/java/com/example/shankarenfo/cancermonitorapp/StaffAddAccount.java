package com.example.shankarenfo.cancermonitorapp;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;

import com.example.shankarenfo.cancermonitorapp.R;

/**
 * The fragment that allows user to make accounts
 */
public class StaffAddAccount extends Fragment {

    int addaccount = 180;
    AlertDialog.Builder DBD;

    public StaffAddAccount() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final FrameLayout frame = (FrameLayout) inflater.inflate(R.layout.fragment_staff_add_account, container, false);

        //fill in the specialists selection to new patients with php data
        Spinner availablespecs = (Spinner) frame.findViewById(R.id.spn_staffnewspecselect);
        new phpGetAllSpecs(getContext(), availablespecs).execute();

        //dialog in case of errors
        DBD = new AlertDialog.Builder(getContext());
        DBD.setTitle("Account failed to create.");

        //button for confirm make a new staff
        Button targetbtn = (Button) frame.findViewById(R.id.btn_newsconfirm);
        targetbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                //gather data from inputs
                EditText targettext = (EditText) frame.findViewById(R.id.txt_staffnewsname);
                String nametext = targettext.getText().toString();
                targettext = (EditText) frame.findViewById(R.id.txt_staffnewsaccount);
                String acnttext = targettext.getText().toString();
                targettext = (EditText) frame.findViewById(R.id.txt_staffnewscancers);
                String ilmttext = targettext.getText().toString();
                targettext = (EditText) frame.findViewById(R.id.txt_staffnewsemail);
                String mailtext = targettext.getText().toString();
                targettext = (EditText) frame.findViewById(R.id.txt_staffnewsphone);
                String phnetext = targettext.getText().toString();
                targettext = (EditText) frame.findViewById(R.id.txt_staffnewspassword);
                String passtext = targettext.getText().toString();
                String spcltxt = "dummy";

                //ensure data is not missing
                if(nametext.trim().length() > 0 && acnttext.trim().length() > 0 && ilmttext.trim().length() > 0
                        && mailtext.trim().length() > 0 && phnetext.trim().length() > 0 && spcltxt.trim().length() > 0
                        && passtext.trim().length() > 0)
                {
                    //create new intent activity that will return confirm/fail data on completion
                    Intent i = new Intent(getContext(),phpCreateAccountActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("Catt","specialist");
                    bundle.putString("Name",nametext);
                    bundle.putString("Pass",passtext);
                    bundle.putString("Acnt",acnttext);
                    bundle.putString("Cncr",ilmttext);
                    bundle.putString("Mail",mailtext);
                    bundle.putString("Phne",phnetext);
                    bundle.putString("Spcl",spcltxt);
                    i.putExtras(bundle);
                    startActivityForResult(i,addaccount);
                }
                else
                {
                    //if data is missing, make a notification and pop it
                    String message = "Please check input:\n";
                    if(nametext.trim().length() > 0)
                    {message += "Name is missing\n";}
                    if(acnttext.trim().length() > 0 )
                    {message += "Account name is missing\n";}
                    if(ilmttext.trim().length() > 0)
                    {message += "Ailment is missing\n";}
                    if(mailtext.trim().length() > 0 )
                    {message += "Email is missing\n";}
                    if (phnetext.trim().length() > 0 )
                    {message += "Phone is missing\n";}
                    if(spcltxt.trim().length() > 0 )
                    {message += "Assigned specialist is missing\n";}
                    if( passtext.trim().length() > 0)
                    {message += "Password is missing\n";}
                    DBD.setMessage(message);
                    DBD.create().show();
                }
            }
        });
        //button for confirm make a new patient
        targetbtn = (Button) frame.findViewById(R.id.btn_newpconfirm);
        targetbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                //gather data from inputs
                EditText targettext = (EditText) frame.findViewById(R.id.txt_staffnewpname);
                String nametext = targettext.getText().toString();
                targettext = (EditText) frame.findViewById(R.id.txt_staffnewpaccount);
                String acnttext = targettext.getText().toString();
                targettext = (EditText) frame.findViewById(R.id.txt_staffnewpcancers);
                String ilmttext = targettext.getText().toString();
                targettext = (EditText) frame.findViewById(R.id.txt_staffnewpemail);
                String mailtext = targettext.getText().toString();
                targettext = (EditText) frame.findViewById(R.id.txt_staffnewpphone);
                String phnetext = targettext.getText().toString();
                targettext = (EditText) frame.findViewById(R.id.txt_staffnewppassword);
                String passtext = targettext.getText().toString();
                Spinner spin = (Spinner) frame.findViewById(R.id.spn_staffnewspecselect);
                String spcltxt = spin.getSelectedItem().toString();

                //ensure data is not missing
                if(nametext.trim().length() > 0 && acnttext.trim().length() > 0 && ilmttext.trim().length() > 0
                        && mailtext.trim().length() > 0 && phnetext.trim().length() > 0 && spcltxt.trim().length() > 0
                        && passtext.trim().length() > 0)
                {
                    //create new intent activity that will return confirm/fail data on completion
                    Intent i = new Intent(getContext(),phpCreateAccountActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("Catt","patient");
                    bundle.putString("Name",nametext);
                    bundle.putString("Pass",passtext);
                    bundle.putString("Acnt",acnttext);
                    bundle.putString("Cncr",ilmttext);
                    bundle.putString("Mail",mailtext);
                    bundle.putString("Phne",phnetext);
                    bundle.putString("Spcl",spcltxt);
                    i.putExtras(bundle);
                    startActivityForResult(i,addaccount);
                }
                else
                {
                    //if data is missing, make a notification and pop it
                    String message = "Please check input:\n";
                    if(nametext.trim().length() > 0)
                    {message += "Name is missing\n";}
                    if(acnttext.trim().length() > 0 )
                    {message += "Account name is missing\n";}
                    if(ilmttext.trim().length() > 0)
                    {message += "Ailment is missing\n";}
                    if(mailtext.trim().length() > 0 )
                    {message += "Email is missing\n";}
                    if (phnetext.trim().length() > 0 )
                    {message += "Phone is missing\n";}
                    if(spcltxt.trim().length() > 0 )
                    {message += "Assigned specialist is missing\n";}
                    if( passtext.trim().length() > 0)
                    {message += "Password is missing\n";}
                    DBD.setMessage(message);
                    DBD.create().show();
                }
            }
        });
        //buttons to change view visibilities
        //create new staff button
        targetbtn = (Button) frame.findViewById(R.id.btn_createnewspec);
        targetbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                TableLayout toshow = (TableLayout) frame.findViewById(R.id.tbl_staffnewstaff);
                LinearLayout tohide = (LinearLayout) frame.findViewById(R.id.ll_newselection);
                toshow.setVisibility(View.VISIBLE);
                tohide.setVisibility(View.GONE);
            }
        });
        //cancel new staff button
        targetbtn = (Button) frame.findViewById(R.id.btn_newscancel);
        targetbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                TableLayout tohide = (TableLayout) frame.findViewById(R.id.tbl_staffnewstaff);
                LinearLayout toshow = (LinearLayout) frame.findViewById(R.id.ll_newselection);
                toshow.setVisibility(View.VISIBLE);
                tohide.setVisibility(View.GONE);
            }
        });
        //create new patient button
        targetbtn = (Button) frame.findViewById(R.id.btn_createnewptnt);
        targetbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                TableLayout toshow = (TableLayout) frame.findViewById(R.id.tbl_staffnewpatient);
                LinearLayout tohide = (LinearLayout) frame.findViewById(R.id.ll_newselection);
                toshow.setVisibility(View.VISIBLE);
                tohide.setVisibility(View.GONE);
            }
        });
        //cancel new patient button
        targetbtn = (Button) frame.findViewById(R.id.btn_newpcancel);
        targetbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                TableLayout tohide = (TableLayout) frame.findViewById(R.id.tbl_staffnewpatient);
                LinearLayout toshow = (LinearLayout) frame.findViewById(R.id.ll_newselection);
                toshow.setVisibility(View.VISIBLE);
                tohide.setVisibility(View.GONE);
            }
        });
        return frame;
    }

    //code to be triggered when receiving response: account creation
    @Override
    public void onActivityResult(int requestCode, int resultcode, Intent data)
    {
        //if the requestcode is from this activity
        if(requestCode == addaccount)
        {
            //if result is ok
            if(resultcode == Activity.RESULT_OK)
            {
                //tell user confirmation
                DBD.setTitle("Account created.");
                DBD.setMessage("New user added to database.");
                DBD.create().show();

            }
            else if(resultcode == Activity.RESULT_CANCELED)
            {
                //if not ok find out why
                String reason = data.getStringExtra("Failtype");
                if(reason.equals("duplicate"))
                {
                    //if failure comes from being a duplicate, tell user
                    DBD.setMessage("That account already existed.");
                    DBD.create().show();
                }
                else if(reason.equals("insertion"))
                {
                    //if failure comes from insertion operation, tell user
                    DBD.setMessage("Database insertion error. Please inform developer.");
                    DBD.create().show();
                }
            }
        }
    }

}
