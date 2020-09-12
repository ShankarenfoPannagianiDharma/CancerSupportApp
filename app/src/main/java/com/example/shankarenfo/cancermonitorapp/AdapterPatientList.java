package com.example.shankarenfo.cancermonitorapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shankarenfo on 3/8/2017.
 */
public class AdapterPatientList extends ArrayAdapter<String>
{
    private Context context;
    private List<String> Namelisting;
    private List<String> Acntlisting;

    public AdapterPatientList(Context context, ArrayList<String> resource1, ArrayList<String> resource2)
    {
        super(context, R.layout.listview_patientlisting,resource1);
        this.context = context;
        this.Namelisting = new ArrayList<>();
        this.Acntlisting = new ArrayList<>();
        this.Namelisting = resource1;
        this.Acntlisting = resource2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.listview_patientlisting, parent, false);

        TextView txtname = (TextView) rowView.findViewById(R.id.txt_patientname);
        TextView txtacnt = (TextView) rowView.findViewById(R.id.txt_patientaccount);

        txtname.setText(Namelisting.get(position));
        txtacnt.setText(Acntlisting.get(position));

        return rowView;
    }
}
