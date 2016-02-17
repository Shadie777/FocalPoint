package com.seankeating.focalpointPresenter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.seankeating.focalpoint.R;
import com.seankeating.focalpointModel.Event;
import com.seankeating.focalpointModel.VenueLocation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,OnMapReadyCallback, LocationListener
{

    private GoogleMap mMap;
    public LocationManager locationManager;
    private GoogleApiClient mGoogleApiClient;
    public static final String TAG = MapsActivity.class.getSimpleName();

    private final static int CONN_FAILURE_RES_REQUEST = 9000;
    private LocationRequest mLocationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        //initialise client
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
                .setInterval(10*1000)
                .setFastestInterval(1 * 1000);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);

        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
    }

    @Override
    public void onConnected(Bundle bundle) {
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);

        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        mMap.setMyLocationEnabled(true);

        if (location == null){
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

        }else{
             handleNewLocation(location);
        }

        Log.i(TAG, "Location services connected.");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Location services suspended. Please reconnect.");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    if(connectionResult.hasResolution()){
        try{
          connectionResult.startResolutionForResult(this, CONN_FAILURE_RES_REQUEST);
        }catch (IntentSender.SendIntentException e){
           e.printStackTrace();
        }
     }else{
        Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
    }
    }

    private void handleNewLocation(Location location) {
        Log.d(TAG, location.toString());

        double currentLat = location.getLatitude();
        double currentLon = location.getLongitude();

        LatLng latLng = new LatLng(currentLat, currentLon);


        MarkerOptions options = new MarkerOptions()
                .draggable(true)
                .position(latLng)
                .title("You");

        mMap.addMarker(options);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }

    @Override
    protected void onResume(){
        super.onResume();
      //  setUpMapIfNeeded();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }


    @Override
    public void onLocationChanged(Location location) {
        handleNewLocation(location);
    }


    public void onMyLocationButtonClick(){

    }
}

class getEvents extends AsyncTask<Double, Integer, String> {

    double lat;
    double lon;
    public static final String TAG = getEvents.class.getSimpleName();
    public List<Event>eventList = new ArrayList<Event>();

    private GoogleMap mMap;

    public getEvents(double lat, double lon){
        this.lat = lat;
        this.lon = lon;
    }

    @Override
    protected String doInBackground (Double... params) {

        Log.i(TAG, "do in background");
        String urlString = ("http://192.168.42.69:3000/events?lat=" + lat + "&lng=" + lon + "&distance=1000&sort=venue&access_token=");
        //"1038263616207618|iuAkTxRvDGNVRZUSdqfz4M4T6aU");
        InputStream in = null;
        int resCode = -1;

        BufferedReader br = null;
        String output = null;



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

                return output;


            }

            httpConn.disconnect();
        } catch (MalformedURLException e) {

            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();

        }

        return output;
    }


    //runs on UI thread and therefore has access to UI Objects unlike doinBackground

    @Override
    protected void onPostExecute(String output) {
        System.out.println("post .... \n");

        Gson gson = new Gson();

        String json = new Gson().toJson(output);
        Type type = new TypeToken<List<Event>>() {
        }.getType();
        eventList = gson.fromJson(json, type);

        for (int i = 0; i < eventList.size(); i++) {
            Event x = eventList.get(i);
            VenueLocation vl = x.getVenueLocation();
            System.out.println(vl);
            System.out.println(x);

        }
        System.out.println(output);


        for (int i = 0; i < eventList.size(); i++) {
            Event event = eventList.get(i);
            System.out.println(event);
            VenueLocation vl = event.getVenueLocation();
            System.out.println(vl);
            String venueTitle = event.getVenueName();
            String eventTitle = event.getEventName();

            LatLng pos = new LatLng(vl.getLatitude(),vl.getLongitude());

            MarkerOptions options = new MarkerOptions()
                    .position(pos)
                    .title(venueTitle)
                    .snippet(eventTitle)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));


            mMap.addMarker(options);        }
    }
}



