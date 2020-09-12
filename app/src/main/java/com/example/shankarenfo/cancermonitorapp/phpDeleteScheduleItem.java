package com.example.shankarenfo.cancermonitorapp;

import android.os.AsyncTask;
import android.util.Log;
;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;

/**
 * Created by Shankarenfo on 3/22/2017.
 */
public class phpDeleteScheduleItem extends AsyncTask<String, String, String>
{
    String phpurl = "http://10.0.2.2/CancerAppPHPs/File_ScheduleDelete.php";
    String targetevent;
    String filename;

    public phpDeleteScheduleItem(String clearedevent, String currentuser)
    {
        this.targetevent = clearedevent;
        this.filename = "schedules"+currentuser+".txt";
    }

    @Override
    protected String doInBackground(String... params)
    {
        try
        {
            String fixedvar = URLEncoder.encode(filename);
            String fixedvar2 = URLEncoder.encode(targetevent);
            String url = phpurl+"?filename="+fixedvar+"&targetline="+fixedvar2;
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet();
            request.setURI(new URI(url));

            //test use
            HttpResponse response = client.execute(request);
            BufferedReader br;
            br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            String line="";
            while ((line = br.readLine()) != null)
            {
                Log.d("DELETION", line);}
            br.close();
            //
        }
        catch (URISyntaxException e)
        {e.printStackTrace();} catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
