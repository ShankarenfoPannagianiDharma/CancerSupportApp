package com.example.shankarenfo.cancermonitorapp;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

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
 * Created by Shankarenfo on 3/9/2017.
 * This php will run a php, returning all specialists in the database by name, in one line separated by commas.
 * This will then fill the spinner with that data.
 */
public class phpGetAllSpecs extends AsyncTask<String, String, String>
{
    private String phpurl = "http://10.0.2.2/CancerAppPHPs/DB_GetAllSpecs.php";
    Context context;
    Spinner spinner;

    public phpGetAllSpecs(Context context, Spinner availablespecs)
    {
        this.context = context;
        this.spinner = availablespecs;
    }

    @Override
    protected String doInBackground(String... params)
    {
        try
        {
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet();
            request.setURI(new URI(phpurl));
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
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,android.R.layout.simple_dropdown_item_1line,data);
        spinner.setAdapter(adapter);
    }
}
