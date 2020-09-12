package com.example.shankarenfo.cancermonitorapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Shankarenfo on 3/26/2017.
 * This php will upload an image to the server through php file.
 */
public class phpUploadAvatarImage extends AsyncTask<String, String, String>
{
    String phpurl = "http://10.0.2.2/CancerAppPHPs/Avatar_Upload.php";
    String targetuser;
    String localfilepath;
    String filetype;

    public phpUploadAvatarImage(String path, String user, String filetype)
    {
        targetuser = user;
        localfilepath = path;
        this.filetype = filetype;
    }

    @Override
    protected String doInBackground(String... params)
    {
        //encode image to string using base64
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 3;
        Bitmap bitmap = BitmapFactory.decodeFile(localfilepath, options);

        ByteArrayOutputStream byteout = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.valueOf(filetype), 50, byteout);
        byte[] bytarray = byteout.toByteArray();

        //the encoded image as string-
        String EncodedImage = Base64.encodeToString(bytarray,0);

        //now make the php operation
        try
        {
            String datatosend = "name="+ URLEncoder.encode(targetuser,"UTF-8")+
                                "&image="+URLEncoder.encode(EncodedImage,"UTF-8")+
                                "&filetype="+URLEncoder.encode(filetype,"UTF-8");

            URL url = new URL(phpurl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            //set timeout of 30 seconds
            con.setConnectTimeout(1000 * 30);
            con.setReadTimeout(1000 * 30);
            //method
            con.setRequestMethod("POST");
            con.setDoOutput(true);

            OutputStream os = con.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));

            //make request
            writer.write(datatosend);
            writer.flush();
            writer.close();
            os.close();
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
