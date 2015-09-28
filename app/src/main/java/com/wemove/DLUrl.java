package com.wemove;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by utilisateur on 16/12/2014.
 */
public class DLUrl {

    public String downloadUrl(String myurl) throws IOException
    {
        InputStream is = null;
        try
        {
            URL url = new URL(myurl);
            HttpURLConnection conn =(HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            int response = conn.getResponseCode();
            is = conn.getInputStream();

            String contentAsString = readIt(is, 2048);
            return contentAsString;
        }
        finally
        {
            if(is!=null)
            {
                is.close();
            }
        }
    }

    public String sendUrl(String myurl, String donnees) throws IOException
    {
        InputStream is = null;
        try
        {
            URL url = new URL(myurl);
            HttpURLConnection conn =(HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
            writer.write(donnees);
            writer.flush();
            conn.connect();

            int response = conn.getResponseCode();
            is = conn.getInputStream();

            String contentAsString = readIt(is, 2048);

            return contentAsString;
        }
        finally
        {
            if(is!=null)
            {
                is.close();
            }
        }
    }

    public String readIt(InputStream stream,int len) throws IOException
    {
        Reader reader =null;
        reader =new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }
}
