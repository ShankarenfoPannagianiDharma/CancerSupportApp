package com.example.shankarenfo.cancermonitorapp;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by Shankarenfo on 3/8/2017.
 * This is the php java bridge for the patient to get the
 */

public class phpPatientGetSpecialist extends AsyncTask<String, String, String>
{
    //the url of the server's php file
    private static String phpurl = "http://10.0.2.2/CancerAppPHPs/DB_PatientGetSpecialist.php";
    Context context;
    TextView spname;
    TextView spmail;
    Button spphone;
    ImageView avatar;

    public phpPatientGetSpecialist(Context context, TextView specialistname, TextView specialistmail, Button specialistcall, ImageView specialistavatar)
    {
        this.avatar = specialistavatar;
        this.spmail = specialistmail;
        this.spname = specialistname;
        this.spphone = specialistcall;
        this.context = context;
    }


    @Override
    protected String doInBackground(String... params)
    {
        String currentuser = params[0];
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
            String line;
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
        this.spname.setText(data[0]);
        this.spphone.setText(data[1]);
        this.spmail.setText(data[2]);
        new phpGetAvatarImage(null,avatar);
    }
}
