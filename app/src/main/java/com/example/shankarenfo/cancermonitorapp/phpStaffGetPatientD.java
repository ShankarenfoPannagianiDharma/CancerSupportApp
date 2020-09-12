package com.example.shankarenfo.cancermonitorapp;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
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
 */

public class phpStaffGetPatientD extends AsyncTask<String, String, String>
{
    private String phpurl = "http://10.0.2.2/CancerAppPHPs/DB_StaffGetPatientD.php";
    private Context context;
    private ImageView avatar;
    TextView pname;
    TextView pacnt;
    TextView pilln;
    TextView pmail;
    TextView pphne;
    private String currentuser;

    public phpStaffGetPatientD(Context context, TextView pname, TextView paccount, TextView pailment, TextView pmail, TextView pphone, ImageView avatar)
    {
        this.avatar = avatar;
        this.context = context;
        this.pacnt = paccount;
        this.pilln = pailment;
        this.pname = pname;
        this.pmail = pmail;
        this.pphne = pphone;
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
        pacnt.setText(data[0]);
        pname.setText(data[1]);
        pilln.setText(data[2]);
        pphne.setText(data[3]);
        pmail.setText(data[4]);
        new phpGetAvatarImage(null,avatar).execute(currentuser);
    }
}
