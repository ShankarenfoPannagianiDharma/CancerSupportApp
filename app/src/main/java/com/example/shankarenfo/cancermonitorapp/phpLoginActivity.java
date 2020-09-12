package com.example.shankarenfo.cancermonitorapp;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Created by Shankarenfo on 3/3/2017.
 */

public class phpLoginActivity extends Activity
{
    //the url of the server's php file
    private static String phpurl = "http://10.0.2.2/CancerAppPHPs/DB_Logincheck.php";   //LOCAL FILE- HAVE TO CHANGE LATER
    private String user,pass;
    private int isstaff;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //get credentials from intent extras
        Bundle bundle = getIntent().getExtras();
        this.user = bundle.getString("USER");
        this.pass = bundle.getString("PASS");
        //run async php operation
        new CheckLoginCreds().execute();
    }

    class CheckLoginCreds extends AsyncTask<String, String, String>
    {

        @Override
        protected String doInBackground(String... args)
        {
            try
            {
                /**use POST method to preserve masking
                String data = URLEncoder.encode("user",encoding)+"="+URLEncoder.encode(user,encoding);
                data += "&" + URLEncoder.encode("pass",encoding)+"="+URLEncoder.encode(pass,encoding);

                URL url = new URL(phpurl);
                URLConnection conn = url.openConnection();
                conn.setDoOutput(true);
                OutputStreamWriter ow = new OutputStreamWriter(conn.getOutputStream());
                ow.write(data);
                ow.flush();
                 POST METHOD DIDNT WORK. USE GET*/

                //use get method
                String link = phpurl+"?user="+user+"&pass="+pass;
                URL url = new URL(link);
                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet();
                request.setURI(new URI(link));
                HttpResponse response = client.execute(request);

                BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                StringBuilder sb = new StringBuilder();
                String line = "";
                // Read Server Response
                while((line = reader.readLine()) != null)
                {sb.append(line);}
                return sb.toString();
            }
            catch (UnsupportedEncodingException e)
            {e.printStackTrace();}
            catch (MalformedURLException e)
            {e.printStackTrace();}
            catch (IOException e)
            {e.printStackTrace();} catch (URISyntaxException e) {
                e.printStackTrace();
            }

            return "";
        }

        @Override
        protected void onPostExecute(String result)
        {
            if (result.equals("IsStaff")) {
                isstaff = 1;
            } else if (result.equals("IsPatient")) {
                isstaff = 0;
            } else {
                isstaff = 2;
                Log.d("Something is wrong",result);
            }

            //send back intent with the database qry results
            Intent returnval = new Intent();
            returnval.putExtra("isstaff",isstaff);
            setResult(Activity.RESULT_OK,returnval);
            finish();
        }
    }
}


