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
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Shankarenfo on 3/10/2017.
 * This activity checks that the account to be made is unique.
 * if true, proceed to create that account via php.
 *
 */

public class phpCreateAccountActivity extends Activity
{
    String category, name, account, cancer, mail, phone, specialist, password;
    String phpcheckurl = "http://10.0.2.2/CancerAppPHPs/DB_CheckNewAccount.php";
    String phpcreateurl = "http://10.0.2.2/CancerAppPHPs/DB_CreateNewAccount.php";
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //get credentials from intent extras
        Bundle bundle = getIntent().getExtras();
        this.category = bundle.getString("Catt");
        this.name = bundle.getString("Name");
        this.account = bundle.getString("Acnt");
        this.cancer = bundle.getString("Cncr");
        this.mail = bundle.getString("Mail");
        this.phone = bundle.getString("Phne");
        this.specialist = bundle.getString("Spcl");
        this.password = bundle.getString("Pass");

        //run async php operation to run a check if account is unique
        new CheckUnique().execute(category, account);
    }

    class CheckUnique extends AsyncTask<String,String,String>
    {
        //make php mysql query: is there an account with this same text?
        @Override
        protected String doInBackground(String... params)
        {
            try
            {
                //use get method
                String link = phpcheckurl + "?account=" + account;
                URL url = new URL(link);
                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet();
                request.setURI(new URI(link));
                HttpResponse response = client.execute(request);

                BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                StringBuilder sb = new StringBuilder();
                String line = "";
                // Read Server Response
                while ((line = reader.readLine()) != null)
                {sb.append(line);}
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
            if(!result.equals("exist"))
            {
                //if result does not exist, proceed to create a php instruction to add account to respective location
                new CreateAccount().execute(category,account,name, specialist, cancer, phone, mail);
            }
            else
            {
                //if an account does exist, send a failure intent (duplicate) back to main
                Intent reply = new Intent();
                reply.putExtra("Failtype","duplicate");
                setResult(Activity.RESULT_CANCELED, reply);
                finish();
            }
        }

        //create account through php
        class CreateAccount extends AsyncTask <String, String, String>
        {
            //make and php mysql order: add an account to
            @Override
            protected String doInBackground(String... params)
            {
                try
                {
                    //use get method
                    String link = "?category="+category+"&account=" + account + "&name=" + name +
                            "&cancer=" + cancer + "&specialist=" + specialist + "&phone=" + phone +
                            "&mail=" + mail + "&pssword="+password;
                    //preserve or hide whitespaces to avoid link error
                    String fixlink = URLEncoder.encode(link,"UTF-8");
                    HttpClient client = new DefaultHttpClient();
                    HttpGet request = new HttpGet();
                    request.setURI(new URI(phpcreateurl + fixlink));
                    HttpResponse response = client.execute(request);

                    BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                    StringBuilder sb = new StringBuilder();
                    String line = "";
                    // Read Server Response
                    while ((line = reader.readLine()) != null)
                    {sb.append(line);
                    Log.d("HEYA",line);}
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
                //if server response is valid, return confirmation ok
                if(result.equals("ok"))
                {
                    Intent reply = new Intent();
                    setResult(Activity.RESULT_OK,reply);
                    finish();
                }
                else
                {
                    //return error response
                    Intent reply = new Intent();
                    reply.putExtra("Failtype","insertion");
                    setResult(Activity.RESULT_CANCELED, reply);
                    finish();
                }
            }
        }//end create account class
    }


}
