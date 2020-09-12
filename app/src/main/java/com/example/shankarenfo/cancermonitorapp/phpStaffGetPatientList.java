package com.example.shankarenfo.cancermonitorapp;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.ListView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Shankarenfo on 3/8/2017.
 */
public class phpStaffGetPatientList extends AsyncTask<String, String, String> {

    private Context context;
    private String phpurl = "http://10.0.2.2/CancerAppPHPs/DB_StaffGetPatientList.php" ;
    ListView patientlisting;
    ArrayList<String> Nlists = new ArrayList<>();
    ArrayList<String> Alists = new ArrayList<>();

    public phpStaffGetPatientList(Context context, ListView patientlisting)
    {
        this.patientlisting = patientlisting;
        this.context = context;
    }

    @Override
    protected String doInBackground(String... params)
    {
        String currentuser = params[0];
        String link = phpurl+"?staff="+currentuser;
        try
        {
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet();
            request.setURI(new URI(link));
            HttpResponse response = client.execute(request);
            BufferedReader br;
            br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            StringBuffer sb = new StringBuffer("");
            String line="";
            while ((line = br.readLine()) != null)
            {sb.append(line);}
            br.close();
            return sb.toString();
        }
        catch (IOException e)
        {e.printStackTrace();}
        catch (URISyntaxException e)
        {e.printStackTrace();}
        return null;

    }

    @Override
    protected void onPostExecute(String result)
    {
        //adaptor uses built in simple one item layout
        ArrayList<String> patients = new ArrayList<>();
        if (result.contains("|"))
        {
            String[] data = result.split("|");
            patients.addAll(Arrays.asList(data));
        }
        else
        {patients.add(result);}

        for(String line : patients)
        {
            String[] data = line.split(",");
            if(data.length > 0)
            {
                Nlists.add(data[0]);
                Alists.add(data[1]);
            }
        }

        AdapterPatientList ca = new AdapterPatientList(context, Nlists, Alists);
        patientlisting.setAdapter(ca);
    }
}
