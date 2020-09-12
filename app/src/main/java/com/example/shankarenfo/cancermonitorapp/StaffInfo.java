package com.example.shankarenfo.cancermonitorapp;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * this is the interface that shows the staff's own data, taken from database.
 */

public class StaffInfo extends Fragment {

    int NEWAVATAR = 16542;
    private ImageButton avatar;
    private String currentuser;

    public StaffInfo() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FrameLayout frameLayout = (FrameLayout) inflater.inflate(R.layout.fragment_staff_info, container, false);

        //set data from database
        currentuser = getArguments().getString("UserID");
        avatar = (ImageButton) frameLayout.findViewById(R.id.ib_staff_mugshot);
        TextView staffname = (TextView) frameLayout.findViewById(R.id.txt_staff_name);
        TextView staffspec = (TextView) frameLayout.findViewById(R.id.txt_staff_specialists);
        TextView staffacc = (TextView) frameLayout.findViewById(R.id.txt_staff_account);
        new phpStaffGetStatus(getContext(), staffname, staffspec, staffacc, avatar).execute(currentuser);

        //clickable button to change specialist's avatar
        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                //intent to get the image file to upload
                Intent imageintent = new Intent(Intent.ACTION_PICK);
                imageintent.setType("image/jpg");   //only jpg images are acceptable
                startActivityForResult(Intent.createChooser(imageintent,"Select your avatar"),NEWAVATAR);
            }
        });

        return frameLayout;
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
                avatar.setImageURI(data.getData());
            }
    }
}
