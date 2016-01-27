package com.seankeating.focalpointPresenter;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import com.facebook.AccessToken;

import org.apache.http.impl.DefaultBHttpClientConnection;
import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Sean on 22/01/2016.
 */
public class ClientGet extends AsyncTask<Double, Integer, String> {

    static final int MAX_CONNECTIONS = 5;
    double lat;
    double lon;
    String accessToken;
  int failures;
    public static final String TAG = ClientGet.class.getSimpleName();

    public ClientGet(double lat, double lon, String accessToken){
        this.lat = lat;
        this.lon = lon;
        this.accessToken = accessToken;
    }

    @Override
    protected String doInBackground (Double... params) {

        Log.i(TAG, "do in background");
        String urlString = ("http://192.168.42.69:3000/events?lat=" + lat + "&lng=" + lon + "&distance=1000&sort=venue&access_token=" + accessToken);
        InputStream in = null;
        int resCode = -1;

        BufferedReader br = null;
        String data = null;
        String output= null;
        try {
            //HttpClient httpClient = new HttpClient();

            URL url = new URL(urlString);
            URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
            url = uri.toURL();
            URLConnection urlConn = url.openConnection();
            HttpURLConnection httpConn = (HttpURLConnection) urlConn;

            httpConn.setRequestMethod("GET");
            httpConn.setRequestProperty("Accept", "application/json");

            br = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));


            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                System.out.println(output);
            }

            httpConn.disconnect();

        } catch (MalformedURLException e) {

            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

//        final int MAX_RETRIES = 3;
//        int numTries = 0;
//        HttpURLConnection conn = null;
//        while (numTries < MAX_RETRIES) {
//
//            try {
//
//
//                URL url = new URL("http://192.168.1.150:3000/events?lat=" + lat + "&lng=" + lon + "&distance=1000&sort=venue&access_token=" + accessToken);
//                conn = (HttpURLConnection) url.openConnection();
//
//
//                conn.setRequestMethod("GET");
//
//                conn.setRequestProperty("Accept", "application/json");
//
//
//                if (conn.getResponseCode() != 200) {   //this
//                    throw new RuntimeException("Failed : HTTP error code : "
//                            + conn.getResponseCode());
//                }
//
//                BufferedReader br = new BufferedReader(new InputStreamReader(
//                        (conn.getInputStream())));
//
//                String output;
//                System.out.println("Output from Server .... \n");
//                while ((output = br.readLine()) != null) {
//                    System.out.println(output);
//                }
//
//                conn.disconnect();
//
//            } catch (MalformedURLException e) {
//
//                e.printStackTrace();
//
//            } catch (IOException e) {
//             //   conn.disconnect();
//                e.printStackTrace();
//
//            }finally {
//
//                if (conn != null)
//                    conn.disconnect();
//            }
//
//            numTries++;
//
//        }//return null;

        return output;

    }

    void disconnect(HttpURLConnection connection) {
        if (connection != null) {
            connection.disconnect();
        }
}
}
