package com.example.shankarenfo.cancermonitorapp;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by Shankarenfo on 3/8/2017.
 * sets the view of PatientStatus
 */

public class phpPatientGetStatus extends AsyncTask<String, String, String>
{
    //the url of the server's php file
    private static String phpurl = "http://10.0.2.2/CancerAppPHPs/DB_PatientGetStatus.php";
    ImageButton mugshot;
    TextView pname;
    TextView pacco;
    TextView pailme;
    TextView pspecl;
    Context context;
    private String currentuser;

    public phpPatientGetStatus(Context context, TextView patientname, TextView patientaccount, TextView patientailment, TextView patientspecialist, ImageButton mugshot)
    {
        this.mugshot = mugshot;
        this.pacco = patientaccount;
        this.pname = patientname;
        this.pailme = patientailment;
        this.pspecl = patientspecialist;
        this.context = context;
    }

    @Override
    protected String doInBackground(String... params)
    {
        currentuser = params[0];
        String link = phpurl+"?patient="+currentuser;
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
        String[] data = result.split(",");
        this.pacco.setText(data[0]);
        this.pname.setText(data[1]);
        this.pailme.setText(data[2]);
        this.pspecl.setText(data[3]);
        new phpGetAvatarImage(mugshot,null).execute(currentuser);
    }
}
