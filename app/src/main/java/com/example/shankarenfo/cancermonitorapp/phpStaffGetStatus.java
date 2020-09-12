package com.example.shankarenfo.cancermonitorapp;

import android.content.Context;
import android.os.AsyncTask;
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
 */

public class phpStaffGetStatus extends AsyncTask<String, String, String>
{
    private static String phpurl = "http://10.0.2.2/CancerAppPHPs/DB_StaffGetStatus.php";
    TextView sname;
    TextView sspec;
    TextView saccn;
    ImageButton avatar;
    Context context;
    private String currentuser;

    public phpStaffGetStatus(Context context, TextView staffname, TextView staffspec, TextView staffacc, ImageButton avatar)
    {
        this.avatar = avatar;
        this.sspec = staffspec;
        this.sname = staffname;
        this.saccn = staffacc;
        this.context = context;
    }

    @Override
    protected String doInBackground(String... params)
    {
        currentuser = params[0];
        String link = phpurl+"?specialist="+currentuser;
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
            while ((line = br.readLine()) != null) {
                sb.append(line);
                break;
            }

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
        sname.setText(data[0]);
        sspec.setText(data[1]);
        saccn.setText(data[2]);
        new phpGetAvatarImage(avatar,null).execute(currentuser);
    }
}
