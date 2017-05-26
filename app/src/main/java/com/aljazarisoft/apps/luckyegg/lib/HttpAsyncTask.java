package com.aljazarisoft.apps.luckyegg.lib;


import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;


public class HttpAsyncTask extends AsyncTask<String, Void, String>   {
    public static final int CONNECTION_TIMEOUT=3000;
    public static final int READ_TIMEOUT=15000;
    private Context context;
    private HashMap<String,String> Data;

    Session session;

    public String RESULT;

    private ProgressDialog dialog;
    HttpURLConnection con;
    URL url = null;
    public HttpAsyncTask(Context context,HashMap h) {
        session = new Session(context);
        this.Data=h;
        this.Data.put("Content-Type", "application/json; charset=utf-8");
        this.context = context;



    }




    public String start( Context cn,HashMap h, String ur){
        this.context=cn;

        new HttpAsyncTask(context,h).execute(ur);


        return this.RESULT;
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            String link=params[0];

            // Enter URL address where your php file resides
            url = new URL(link);

        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

            return "exception";
        }
        try {
            // Setup HttpURLConnection class to send and receive data from php and mysql
            con = (HttpURLConnection)url.openConnection();
            con.setReadTimeout(READ_TIMEOUT);
            con.setConnectTimeout(CONNECTION_TIMEOUT);
            con.setRequestMethod("POST");

            // setDoInput and setDoOutput method depict handling of both send and receive
            con.setDoInput(true);
            con.setDoOutput(true);

            // Append parameters to URL
            String urlParameters=null;
            Uri.Builder builder=null;
            if(this.Data.isEmpty())
            {
             builder = new Uri.Builder()
                        .appendQueryParameter("","");

            }else
            {
                builder = new Uri.Builder();
                for (Map.Entry<String, String> entry : this.Data.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    if (key != null || ! value.equals("")) {
                        builder.appendQueryParameter(String.valueOf(key),String.valueOf(URLEncoder.encode(value, "UTF-8")));
                    }else{


                    }



                }
            }

            String query = builder.build().getEncodedQuery();


            OutputStream os = con.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();
            con.connect();

        } catch (IOException e1) {

            // TODO Auto-generated catch block
            e1.printStackTrace();

            return "noconnection";
        }

        try {

            int response_code = con.getResponseCode();

            // Check if successful connection made
            if (response_code == HttpURLConnection.HTTP_OK) {

                // Read data sent from server
                InputStream input =  con.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                StringBuilder result = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

                // Pass data to onPostExecute method
                String rs =(result.toString());
                return(rs);

            }else{
                Toast.makeText(context, "sorrryyy", Toast.LENGTH_SHORT).show();
                return("unsuccessful");
            }

        } catch (IOException e) {
            e.printStackTrace();
            return "failed";
        } finally {
            con.disconnect();
        }


    }




    @Override
    protected void onPostExecute(String result) {

        if(result.equals("exception"))
        {

            new Tools().makeAlert(context,"Fail","Cannot connect the url","Okay i got it","","LoginActivity");
            System.exit(0);
        }else if(result.equals("noconnection")){
            new Tools().makeAlert(context,"Fail","Cannot connect to server ","Okay i got it","","LoginActivity");
            System.exit(0);
        }else if(result.equals("failed")){
            new Tools().makeAlert(context,"Fail","Cannot get any data ","Okay i got it","","LoginActivity");
            System.exit(0);
        }

        if (result != null) {


        } else {


            new Tools().makeAlert(context,"Fail","Soory we could not get any JSON data","Okay i got it","","LoginActivity");
        }



    }


    @Override
    protected void onPreExecute() {

    }
}
