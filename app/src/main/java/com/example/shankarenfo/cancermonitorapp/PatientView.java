package com.example.shankarenfo.cancermonitorapp;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.Calendar;

import static java.lang.Thread.sleep;

/**
 * This is the activity where the patient will use.
 * UI Screens are exchanged using FRAGMENTS and FragmentManager.
 * This also holds a custom sidebar.
 */

public class PatientView extends AppCompatActivity {

    //setup the fragment UIs
    FragmentManager uimanager = getSupportFragmentManager();
    Bundle bundle;
    private ArrayList<PendingIntent> notifications = new ArrayList<>();
    AlarmManager am;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_view);
        am = (AlarmManager) this.getSystemService(this.ALARM_SERVICE);

        //get extra bundles
        String extra = getIntent().getStringExtra("UserID");
        bundle = new Bundle();
        bundle.putString("UserID",extra);

        //setup custom toolbar
        Toolbar generaltoolbar = (Toolbar) findViewById(R.id.tlb_generaltoolbar);
        setSupportActionBar(generaltoolbar);

        //deploy the home ui
        Fragment firstui = new PatientHome();
        firstui.setArguments(bundle);
        uimanager.beginTransaction().replace(R.id.fly_patientuiplaceholder, firstui, "UIHOME").commit();

        //setup navigation drawer
        NavigationView navigationpanel = (NavigationView) findViewById(R.id.nvv_navigationpanel);
        setupDrawerContent(navigationpanel);
    }

    //add icons to the menu bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menubar_items, menu);
        return true;
    }

    /*/ `onPostCreate` called when activity start-up is complete after `onStart()`
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }/*/

    //onclick listener for the navdrawer setup
    private void setupDrawerContent (final NavigationView navigationView)
    {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(MenuItem selecteditem)
            {
                selectFragment(selecteditem);
                return true;
            }
        });
    }

    //adds the listener functions to selected item
    public void selectFragment(MenuItem selecteditem)
    {
        //create a new fragment based on the selection
        Fragment newui = null;
        Class fragmentclass;

        //switch case by ID of selected item- define fragment case-by-case
        switch(selecteditem.getItemId()) {
            case R.id.nav_HOME:
                fragmentclass = PatientHome.class;
                break;
            case R.id.nav_SPECIALIST:
                fragmentclass = PatientSpecialist.class;
                break;
            case R.id.nav_STATUS:
                fragmentclass = PatientStatus.class;
                break;
            case R.id.nav_LOGOUT:
                fragmentclass = PatientHome.class;
                finish();
                break;
            default:
                fragmentclass = PatientHome.class;
                finish();
        }

        //assign a new fragment to shift into
        try
        {newui = (Fragment) fragmentclass.newInstance();}
        catch(Exception e)
        {e.printStackTrace();}

        //add extra message (current user) to fragments
        assert newui != null;
        newui.setArguments(bundle);

        //replace the fragments using fragmentmanager
        FragmentManager fgmanager = getSupportFragmentManager();
        fgmanager.beginTransaction().replace(R.id.fly_patientuiplaceholder, newui).commit();

        selecteditem.setChecked(true);

        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drw_drawerlayout);
        drawerLayout.closeDrawers();
    }


    public void showDrawer(MenuItem mi)
    {
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drw_drawerlayout);
        drawerLayout.openDrawer(Gravity.LEFT);
    }

    //method to be called to make notifications using alarmManager.
    //arraylist items  is the string that contains schedule data
    //add the notification datas into storage in case we need to cancel them
    public void createNotifications(final ArrayList<String> items)
    {
        Calendar calendar = Calendar.getInstance();
        //create notifications for each scheduleitem
        for(String item : items)
        {
            //setup the alarm intents
            Intent intent = new Intent(this.getApplicationContext(), NotificationReceiver.class);
            PendingIntent pi = PendingIntent.getBroadcast(this.getApplicationContext(),0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
            //grab data from item
            String[] data = item.split("[,]");
            String[] date;
            String[] time;
            int hour,minute,day,month,year;
            //format for two itemkinds: event or routine
            //EVENT,date,time,name,desc
            //RTINE,time,name,desc
            if(data[0].equals("EVENT"))
            {
                //get timeset
                date = data[1].split("[/]");
                day = Integer.parseInt(date[0]);
                month = Integer.parseInt(date[1]);
                year =  Integer.parseInt(date[2]);
                time = data[2].split("[:]");
                hour = Integer.parseInt(time[0]);
                minute = Integer.parseInt(time[1]);
                //place into calendar
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,day);
                calendar.set(Calendar.HOUR_OF_DAY,hour);
                calendar.set(Calendar.MINUTE,minute);

                Bundle bundle = new Bundle();
                bundle.putString("Descript",data[4].replaceAll("TY@JB",","));
                bundle.putString("Title",data[3].replaceAll("TY@JB",","));
                intent.putExtras(bundle);
                //set a one-time notification
            }
            else if(data[0].equals("RTINE"))
            {
                //get timeset
                time = data[1].split("[:]");
                hour = Integer.parseInt(time[0]);
                minute = Integer.parseInt(time[1]);
                //place into calendar
                calendar.set(Calendar.HOUR_OF_DAY,hour);
                calendar.set(Calendar.MINUTE,minute);

                Bundle bundle = new Bundle();
                bundle.putString("Descript",data[3].replaceAll("TY@JB",","));
                bundle.putString("Title",data[2].replaceAll("TY@JB",","));
                intent.putExtras(bundle);
                //set repeatable notification
                am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pi);
            }
        }

    }

    //method to delete/cancel all pending notifications.
    public void cancelNotifications()
    {
        for(PendingIntent pi : notifications)
        {
            Intent intent = new Intent(this.getApplicationContext(), NotificationReceiver.class);
            pi.getBroadcast(this.getApplicationContext(),0,intent,PendingIntent.FLAG_UPDATE_CURRENT).cancel();
        }
    }

    //method to allow changing fragment from child fragment itself- used mainly to toggle more info on cancer
    public void changeInterface(Class fragmentclass)
    {
        //generate fragment
        Fragment targetfrag = null;

        try
        {targetfrag = (Fragment) fragmentclass.newInstance();}
        catch (InstantiationException e)
        {e.printStackTrace();}
        catch (IllegalAccessException e)
        {e.printStackTrace();}
        //make the cjhange

        uimanager.beginTransaction().replace(R.id.fly_patientuiplaceholder,targetfrag).commit();
    }

}





