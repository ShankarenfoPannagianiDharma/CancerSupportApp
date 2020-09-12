package com.example.shankarenfo.cancermonitorapp;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.FileNotFoundException;

/**
 * This is the screen that shows when the user looks at their own status.
 * A mugshot as well as general info is shown, taken from database.
 */

public class PatientStatus extends Fragment {

    private static final int NEWAVATAR = 2345;
    ImageButton mugshot;
    String currentuser;

    public PatientStatus()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        FrameLayout fragmentlayout = (FrameLayout) inflater.inflate(R.layout.fragment_patient_status, container, false);

        //fill in the ui credentials of the patient
        currentuser = getArguments().getString("UserID");

        mugshot = (ImageButton) fragmentlayout.findViewById(R.id.ib_patient_mugshot);
        TextView patientname = (TextView) fragmentlayout.findViewById(R.id.txt_patientname);
        TextView patientaccount = (TextView) fragmentlayout.findViewById(R.id.txt_patientaccount);
        TextView patientailment = (TextView) fragmentlayout.findViewById(R.id.txt_patientailment);
        TextView patientspecialist = (TextView) fragmentlayout.findViewById(R.id.txt_patientspecialist);

        new phpPatientGetStatus(getContext(),patientname,patientaccount,patientailment,patientspecialist, mugshot).execute(currentuser);

        //imagebutton becomes clickable- upload a new avatar.
        mugshot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {ChangeAvatar();}
        });

        // Inflate the layout for this fragment
        return fragmentlayout;
    }

    //method to change the user's avatar
    public void ChangeAvatar()
    {
        //intent to get the image file to upload
        Intent imageintent = new Intent(Intent.ACTION_PICK);
        imageintent.setType("image/jpg");   //only jpg images are acceptable
        startActivityForResult(Intent.createChooser(imageintent,"Select your avatar"),NEWAVATAR);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NEWAVATAR)
            if (resultCode == Activity.RESULT_OK)
            {
                Uri selectedImage = data.getData();
                String[] projection = { MediaStore.Images.Media.DATA };

                Cursor cursor = getContext().getContentResolver().query(selectedImage,projection,null,null,null);
                cursor.moveToFirst();

                int columnindex = cursor.getColumnIndex(projection[0]);
                String path = cursor.getString(columnindex);
                //now path is the path to image
                int i = path.lastIndexOf(".");
                String filetype = path.substring(i+1);

                //upload that image to server, Overwrite existing image
                new phpUploadAvatarImage(path,currentuser,filetype).execute();
                //set the image from selected image
                mugshot.setImageURI(data.getData());
            }
    }

}
