package com.example.shankarenfo.cancermonitorapp;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * This activity is the main container that the user will see.
 * The main visual holds buttons that redirects fragments the user will see,
 * while the fragmentlayout is at first invisible and dormant. When a button is clicked, it
 * will switch between the menu or the fragmentlayout.
 */

public class StaffView extends AppCompatActivity
{
    FragmentManager fragmentManager = getSupportFragmentManager();
    Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_view);

        //get user id from bundle extras sent from login
        String extra = getIntent().getStringExtra("UserID");
        bundle = new Bundle();
        bundle.putString("UserID",extra);

        //repeated/most common components
        final Button openmenu = (Button) findViewById(R.id.btn_openmenu);
        final LinearLayout content = (LinearLayout) findViewById(R.id.ll_staffcontent);
        final LinearLayout menu = (LinearLayout) findViewById(R.id.ll_staffmainmenu);


        //LISTENERS
        Button logout = (Button) findViewById(R.id.btn_stafflogout);
        assert logout != null;
        logout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent nextpage = new Intent(StaffView.this, Login.class);
                startActivity(nextpage);
            }
        });

        //view schedule of staff
        Button schedule = (Button) findViewById(R.id.btn_staffscheduling);
        assert schedule != null;
        schedule.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                menu.setVisibility(v.GONE);
                content.setVisibility(v.VISIBLE);
                Fragment newfrag = new StaffSchedule();
                newfrag.setArguments(bundle);
                switchfragment(newfrag);

            }
        });

        //button to toggle main menu
        assert openmenu != null;
        openmenu.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                content.setVisibility(v.GONE);
                menu.setVisibility(v.VISIBLE);
            }
        });

        //view schedule of staff
        Button cancerinfo = (Button) findViewById(R.id.btn_staffinfo);
        assert cancerinfo != null;
        cancerinfo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                menu.setVisibility(v.GONE);
                content.setVisibility(v.VISIBLE);
                Fragment newfrag = new StaffInfo();
                newfrag.setArguments(bundle);
                switchfragment(newfrag);
            }
        });


        //view patient list of staff
        Button patientlist = (Button) findViewById(R.id.btn_staffdoctoring);
        assert patientlist != null;
        patientlist.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                menu.setVisibility(v.GONE);
                content.setVisibility(v.VISIBLE);
                Fragment newfrag = new StaffPatients();
                newfrag.setArguments(bundle);
                switchfragment(newfrag);
            }
        });

        //view add new of staff
        Button addnew = (Button) findViewById(R.id.btn_staffaddnew);
        assert addnew != null;
        addnew.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                menu.setVisibility(v.GONE);
                content.setVisibility(v.VISIBLE);
                Fragment newfrag = new StaffAddAccount();
                newfrag.setArguments(bundle);
                switchfragment(newfrag);
            }
        });
    }

    //method to do fragment switching, parameter is the fragment itself
    public void switchfragment(Fragment fragment)
    {fragmentManager.beginTransaction().replace(R.id.fly_staffuiplaceholder,fragment).commit();}

}
