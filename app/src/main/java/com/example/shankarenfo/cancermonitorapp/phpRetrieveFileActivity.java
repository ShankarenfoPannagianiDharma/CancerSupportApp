package com.example.shankarenfo.cancermonitorapp;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by Shankarenfo on 3/21/2017.
 * This javaclass will access a php to read a text file with the corresponding name.
 * Will return the contents of said file
 */
public class phpRetrieveFileActivity extends Activity {
    private String filename;
    private String phpurl = "http://10.0.2.2/CancerAppPHPs/File_GetFile.php";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //get credentials from intent extras
        Bundle bundle = getIntent().getExtras();
        this.filename = bundle.getString("Filename");
        //run async operation
        new phpRetrieveFileContent().execute();
    }

    class phpRetrieveFileContent extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params)
        {
            try {
                //set up the php uri
                String link = phpurl+"?filename=" + filename;
                DefaultHttpClient httpclient = new DefaultHttpClient();
                HttpGet get = new HttpGet();
                get.setURI(new URI(link));
                HttpResponse response = httpclient.execute(get);
                BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                StringBuilder total = new StringBuilder();
                //read the items in file- each item is separated with '|||'
                String line =br.readLine();
                if(line !=null)
                {total.append(line);}
                while ((line = br.readLine()) != null)
                {total.append("|||"+line);}
                br.close();
                return total.toString();
            }
            catch (ClientProtocolException e)
            {e.printStackTrace();}
            catch (IOException e)
            {e.printStackTrace();}
            catch (URISyntaxException e)
            {e.printStackTrace();}
            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            //send back intent with the database qry results
            Intent returnval = new Intent();
            returnval.putExtra("Contents", result);
            setResult(Activity.RESULT_OK, returnval);
            finish();
        }

    }
}
