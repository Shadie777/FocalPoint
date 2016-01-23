package com.seankeating.focalpointPresenter;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import com.facebook.AccessToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import android.os.AsyncTask;

import com.google.android.gms.analytics.ecommerce.Product;

/**
 * Created by Sean on 13/01/2016.
 */
public class InitiateConnection {
    double lat;
    double lon;


public static void main(double lat, double lon){
    AccessToken accessToken;
    accessToken = AccessToken.getCurrentAccessToken();

    //new ClientGet(lat, lon, accessToken).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

   new ClientGet(lat,lon, accessToken).execute();
   // new UpdateInfoAsyncTask(lat,lon).execute();
}
}

