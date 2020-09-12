package com.example.shankarenfo.cancermonitorapp;

import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;

/**
 * Created by Shankarenfo on 3/20/2017.
 * this class will call a php file which writes the string content to a txt file.
 */

public class phpRewriteFile extends AsyncTask<String, String, String>
{
    private static String phpurl = "http://10.0.2.2/CancerAppPHPs/File_RewriteScheduleFile.php";
    private String filename;
    private String contents;

    phpRewriteFile(String filename, String contents)
    {
        this.filename = filename;
        this.contents = contents;
    }

    @Override
    protected String doInBackground(String... params)
    {
        try
        {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            String fixedcontents = URLEncoder.encode(contents);
            String fixedfilename = URLEncoder.encode(filename+".txt");
            HttpGet httpget = new HttpGet();
            httpget.setURI(new URI(phpurl+"?filename="+fixedfilename+"&value="+fixedcontents));
            httpClient.execute(httpget);
        }
        catch (IOException e)
        {e.printStackTrace();}
        catch (URISyntaxException e)
        {e.printStackTrace();}

        return null;
    }
}
