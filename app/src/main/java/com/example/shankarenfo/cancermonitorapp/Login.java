package com.example.shankarenfo.cancermonitorapp;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Arrays;

/**
 * This is the first main activity that is loaded when the application is started
 * User enters account and password. Then proceed to either staff or patient views.
 */

public class Login extends AppCompatActivity
{
    AlertDialog.Builder DBD;
    private int LOGCHECK = 1233;    //logcheck is the code for activityresult
    private String user;
    private EditText username;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //make alert dialog in case account not found
        DBD = new AlertDialog.Builder(this);
        DBD.setTitle("Account not found.");
        DBD.setMessage("Please check your input.");
    }

    //LOGIN operation uses php to connect and exchange data, as determined by DBOps php file.
    public void loggin(View view)
    {
            //get data from android
            username = (EditText) findViewById(R.id.txt_Username);
            password = (EditText) findViewById(R.id.txt_Password);
            user = username.getText().toString();
            String Pass = password.getText().toString();
            boolean isstaff = false;

            //connect to database through php operation- start activity
            Intent i = new Intent(getApplicationContext(), phpLoginActivity.class);
            Bundle logincreds = new Bundle();
            logincreds.putString("USER", user);
            logincreds.putString("PASS", Pass);
            i.putExtras(logincreds);
            startActivityForResult(i, LOGCHECK);
    }//end login buttonpress method

    @Override
    protected void onActivityResult(int requestCode, int resultcode, Intent data)
    {
        //only continue if the returning result is from the specific activity.
        if (requestCode == LOGCHECK)
        {
            if(resultcode == Activity.RESULT_OK)
            {
                //clear login
                password.setText("");
                username.setText("");

                Bundle returns = data.getExtras();
                int result = returns.getInt("isstaff");

                //is staff boolean 0 false | 1 true | 2 error
                if (result == 0)
                {
                    Intent next = new Intent(Login.this, PatientView.class);
                    //pass extra values as intent extras
                    next.putExtra("UserID", user);
                    startActivity(next);
                }
                else if (result == 1)
                {
                    //pass current user data to intent
                    Intent next = new Intent(Login.this, StaffView.class);
                    //pass extra values as intent extras
                    next.putExtra("UserID", user);
                    startActivity(next);
                }
                else
                {DBD.create().show();}
            }
        }
    }//end intent onresult

}

