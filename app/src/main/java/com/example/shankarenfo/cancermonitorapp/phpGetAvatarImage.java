package com.example.shankarenfo.cancermonitorapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

/**
 * Created by Shankarenfo on 3/26/2017.
 * uses php to get image data, then set it.
 */
public class phpGetAvatarImage extends AsyncTask<String, Void, Bitmap>
{
    ImageButton btarget;
    ImageView vtarget;
    String url = "http://10.0.2.2/CancerAppPHPs/Avatars/";

    //constructor, can be either avatar imagebutton or mugshot imageview. The other needs to be null.
    public phpGetAvatarImage(ImageButton avatar, ImageView mugshot)
    {
        btarget = avatar;
        vtarget = mugshot;
    }

    @Override
    protected Bitmap doInBackground(String... params)
    {
        String extensions = ".jpg";
        String targetuser = params[0];
        String targetfile = url+targetuser+extensions;
        Bitmap avatar = null;

        try
        {
            InputStream in = new java.net.URL(targetfile).openStream();
            avatar = BitmapFactory.decodeStream(in);
        }
        catch (MalformedURLException e)
        {e.printStackTrace();}
        catch (IOException e)
        {return null;}

        return avatar;
    }

    protected void onPostExecute(Bitmap result)
    {
        //if there is a result, set the image. Else, do nothing.
        if(result != null)
        {
            if(btarget != null)
            {btarget.setImageBitmap(result);}
            else
            {vtarget.setImageBitmap(result);}
        }
        else
        {}
    }
}
