package com.example.shankarenfo.cancermonitorapp;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Created by Shankarenfo on 12/20/2016.
 * this class file is a custom adapter  for use on listview with two items, namely a list of events.
 */

public class AdapterEventList extends BaseAdapter
{
    private Context context;
    private String currentuser;
    private ArrayList<String> eventlist = new ArrayList<>();
    private ArrayList<String> todaylist = new ArrayList<>();
    private ArrayList<String> laterlist = new ArrayList<>();
    private ArrayList<String> schdllist = new ArrayList<>();
    private Calendar calendar = Calendar.getInstance();
    private String cdate = calendar.get(Calendar.DAY_OF_MONTH)+"/"+calendar.get(Calendar.MONTH)+"/"+calendar.get(Calendar.YEAR);


    public AdapterEventList(Context context, ArrayList<String> lines, String currentuser)
    {
        this.context = context;
        this.currentuser = currentuser;

        //for every line in arraylist, separate to list parts.
        //[RTINE/EVENT,(date),time,name,desc] format
        for(String line : lines)
        {
            //check if input is empty (NO SCHEDULE)
            if(!line.isEmpty())
            {
                //read the first tag- EVENT or RTINE?
                String[] parts = line.split(",");
                //add them to their own list
                if(parts[0].equals("EVENT"))
                {eventlist.add(line);}
                else if (parts[0].equals("RTINE"))
                {todaylist.add(line);}
            }
        }

        //sorts eventlist- looks to the second tag, place them in either TODAY or not,
        //if true sort into todaylist if not add into tommorowlist
        if(!eventlist.isEmpty())
        {
            for (String line : eventlist)
            {
                String[] parts = line.split(",");
                if (cdate.equals(parts[1]))
                {todaylist.add(line);}
                else
                {laterlist.add(line);}
            }

            //sort laterlist by time then date
            Collections.sort(laterlist, new Comparator<String>() {
                @Override
                public int compare(String lhs, String rhs)
                {
                    //get the time values
                    String[] parts = lhs.split(",");
                    String timel = parts[2];
                    parts = rhs.split(",");
                    String timer = parts[2];
                    DateFormat timeform = new SimpleDateFormat("hh:mm");
                    Date ldate = new Date();
                    Date rdate = new Date();
                    try {
                        ldate = timeform.parse(timel);
                        rdate = timeform.parse(timer);
                    } catch (ParseException e)
                    {e.printStackTrace();}
                    //compare
                    return ldate.compareTo(rdate);
                }
            });
            Collections.sort(laterlist, new Comparator<String>() {
                @Override
                public int compare(String lhs, String rhs)
                {
                    //get the time values
                    String[] parts = lhs.split(",");
                    String timel = parts[1];
                    parts = rhs.split(",");
                    String timer = parts[1];
                    DateFormat timeform = new SimpleDateFormat("dd/MM/yyyy");
                    Date ldate = new Date();
                    Date rdate = new Date();
                    try {
                        ldate = timeform.parse(timel);
                        rdate = timeform.parse(timer);
                    } catch (ParseException e)
                    {e.printStackTrace();}
                    //compare
                    return ldate.compareTo(rdate);
                }
            });
        }

        //sort todaylist by time
        if(!todaylist.isEmpty()) {
            Collections.sort(todaylist, new Comparator<String>() {
                @Override
                public int compare(String lhs, String rhs) {
                    //get the time values
                    String[] parts = lhs.split(",");
                    String timel = parts[1];
                    parts = rhs.split(",");
                    String timer = parts[1];
                    DateFormat timeform = new SimpleDateFormat("hh:mm");
                    Date ldate = new Date();
                    Date rdate = new Date();
                    try {
                        ldate = timeform.parse(timel);
                        rdate = timeform.parse(timer);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    //compare
                    return ldate.compareTo(rdate);
                }
            });
        }

        //add sorted todaylist into schedulelist, then laterlist at the end
        if(!todaylist.isEmpty() && !laterlist.isEmpty())
            for(String line : todaylist)
            {schdllist.add(line);}
            for(String line : laterlist)
            {schdllist.add(line);}
    }

    @Override
    public int getCount()
    {
        if(schdllist == null)
        {return 0;}
        else
        {return schdllist.size();}
    }

    //get item returns a string array of event,time
    @Override
    public Object getItem(int position) {
        String result = schdllist.get(position);
        return result;
    }

    private String getStringPart(String line, int part)
    {
        String[] parts = line.split(",");
        return parts[part];
    }

    @Override
    public long getItemId(int position)
    {return position;}

    //abstract method to fill in the listview rows, using listview_2items layout
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        if(row == null)
        {row = inflater.inflate(R.layout.listview_2items,null);}

        LinearLayout lv = (LinearLayout) row.findViewById(R.id.ll_content);
        if(getStringPart(schdllist.get(position),0).equals("RTINE"))
        {
            //if a routine, fill with title, description and time.
            TextView targettext = (TextView) row.findViewById(R.id.txt_item1);
            String title = getStringPart(schdllist.get(position), 2).replaceAll("TY@JB",",");
            targettext.setText(title);
            targettext = (TextView) row.findViewById(R.id.txt_item2);
            targettext.setVisibility(View.INVISIBLE);
            targettext = (TextView) row.findViewById(R.id.txt_item3);
            String descript = getStringPart(schdllist.get(position), 3).replaceAll("TY@JB",",");
            targettext.setText(descript);
            targettext = (TextView) row.findViewById(R.id.txt_item4);
            targettext.setText(getStringPart(schdllist.get(position), 1));
        }
        else if(getStringPart(schdllist.get(position),0).equals("EVENT"))
        {
            //if an event, fill title, description, name and date.
            TextView targettext = (TextView) row.findViewById(R.id.txt_item1);
            String title = getStringPart(schdllist.get(position), 3).replaceAll("TY@JB",",");
            targettext.setText(title);
            targettext = (TextView) row.findViewById(R.id.txt_item2);
            targettext.setText(getStringPart(schdllist.get(position), 1));
            targettext = (TextView) row.findViewById(R.id.txt_item3);
            String descript = getStringPart(schdllist.get(position), 4).replaceAll("TY@JB",",");
            targettext.setText(descript);
            targettext = (TextView) row.findViewById(R.id.txt_item4);
            targettext.setText(getStringPart(schdllist.get(position), 2));

            //set an onchecked listener to event checkbox- remove event from
            final CheckBox cb = (CheckBox) row.findViewById(R.id.cb_completed);
            cb.setVisibility(View.VISIBLE);
            cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                {
                    //when checked, this means that the event is completed: remove event from file, restart listview
                    //get the original string of event
                    String title = getStringPart(schdllist.get(position), 3).replaceAll("TY@JB",",");
                    String descript = getStringPart(schdllist.get(position), 4).replaceAll("TY@JB",",");
                    String clearedevent =   getStringPart(schdllist.get(position), 0) + "," +
                                            getStringPart(schdllist.get(position), 1) + "," +
                                            getStringPart(schdllist.get(position), 2) + "," +
                                            title + "," +
                                            descript;

                    //delete line from file using php
                    new phpDeleteScheduleItem(clearedevent,currentuser).execute();

                    //restart listview
                    notifyDataSetChanged();

                    //disable checkbox clickability
                    cb.setEnabled(false);
                }
            });

            //also change the background color
            lv.setBackgroundColor(0xff00ffff);
        }

        //else do nothing
        return row;
    }
}
