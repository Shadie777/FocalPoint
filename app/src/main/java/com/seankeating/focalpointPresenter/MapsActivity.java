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
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.seankeating.focalpoint.R;
import com.seankeating.focalpointModel.Event;
import com.seankeating.focalpointModel.EventList;
import com.seankeating.focalpointModel.VenueLocation;
import com.seankeating.focalpointViews.DisplayEventDetails;

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
import java.security.spec.ECField;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback,
        LocationListener, GoogleMap.OnInfoWindowClickListener{

    private static GoogleMap mMap;
    public LocationManager locationManager;
    private GoogleApiClient mGoogleApiClient;
    public static final String TAG = MapsActivity.class.getSimpleName();
    Location mLocation;
    private final static int CONN_FAILURE_RES_REQUEST = 9000;
    private LocationRequest mLocationRequest;
    static HashMap<Marker, Event> eventMarkerMap;
    LatLng latLng;
    public static List<Event> eventList1 = new ArrayList<Event>();
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
                .setInterval(10 * 1000)
                .setFastestInterval(1 * 1000);

    }


    private void setUpMap() {
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14.0f));

        mMap.setOnInfoWindowClickListener(this);
//        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
//            @Override
//            public void onInfoWindowClick(Marker marker) {
//                // if (mLocation == null) return;
//                LatLng latLon = marker.getPosition();
//                String title = marker.getTitle();
//                //Cycle through places array
//                for (Event e : eventList1) {
//                    if (title.equals(e.getEventName())) {
//                        Intent intent = new Intent(MapsActivity.this, DisplayEventDetails.class);
//                        intent.putExtra("Event", e);
//                        startActivity(intent);
//                    }
//
//                }
//            }
//        });
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

        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        mMap.setMyLocationEnabled(true);

        if (mLocation == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

        } else {
            handleNewLocation(mLocation);
        }

        Log.i(TAG, "Location services connected.");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Location services suspended. Please reconnect.");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(this, CONN_FAILURE_RES_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    private void handleNewLocation(Location location) {
        Log.d(TAG, location.toString());

        double currentLat = location.getLatitude();
        double currentLon = location.getLongitude();

        latLng = new LatLng(currentLat, currentLon);


        MarkerOptions options = new MarkerOptions()
                .draggable(true)
                .position(latLng)
                .title("You");

        mMap.addMarker(options);
        //   mMap.setOnMarkerClickListener(this);

        getEvents mGetEvents = new getEvents(currentLat, currentLon);
        mGetEvents.execute();
        setUpMap();
//        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
//            @Override
//            public void onInfoWindowClick(Marker marker) {
//                for (Map.Entry<Marker, Event> entry : eventMarkerMap.entrySet()) {
//
//                    Marker myMarker = entry.getKey();
//                    if (marker.equals(myMarker) && marker.equals(entry.getKey())) {
//                        Intent intent = new Intent(MapsActivity.this, DisplayEventDetails.class);
//                        Event x = entry.getValue();
//                        intent.putExtra("Event", x);
//                        startActivity(intent);
//
//                    }
//                }
//            }
//        });
    }

    public void onInfoWindowClick(Marker marker) {


                // if (mLocation == null) return;
                LatLng latLon = marker.getPosition();
                String title = marker.getTitle();
                //Cycle through places array
                for (Event e : eventList1) {
                    if (title.equals(e.getEventName())) {
                        Intent intent = new Intent(MapsActivity.this, DisplayEventDetails.class);
                        intent.putExtra("Event", e);
                        startActivity(intent);
                    }

                }
            }
//

//        Event event = eventMarkerMap.get(marker);
//        Intent intent = new Intent(MapsActivity.this, DisplayEventDetails.class);
//        intent.putExtra("Event", event);
//        startActivity(intent);


    @Override
    protected void onResume() {
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


    public void onMyLocationButtonClick() {

    }

    public static void displayEvents(List<Event> eventList) {

        eventMarkerMap = new HashMap<Marker, Event>();
        for (int i = 0; i < eventList.size(); i++) {
            Event event = eventList.get(i);
            eventList1.add(event);
            System.out.println(event);
            VenueLocation vl = event.getVenueLocation();
            System.out.println(vl);
            String venueTitle = event.getVenueName();
            String eventTitle = event.getEventName();

            LatLng pos = new LatLng(vl.getLatitude(), vl.getLongitude());
            Marker m = mMap.addMarker(new MarkerOptions()
                    .position(pos)
                    .title(venueTitle)
                    .snippet(eventTitle)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

            eventMarkerMap.put(m, event);

        }


    }


//    @Override
//    public boolean onMarkerClick(final Marker marker) {
//
//
//        for (Map.Entry<Marker, Event> entry : eventMarkerMap.entrySet()) {
//
//            if (marker.equals(myMarker) && marker.equals(entry.getKey())) {
//                Intent intent = new Intent(this, DisplayEventDetails.class);
//                Event x = entry.getValue();
//                intent.putExtra("Event", x);
//                startActivity(intent);
//
//                return true;
//            }
//        }
//        return false;
//    }
//}

}



class getEvents extends AsyncTask<Double, Integer, List<Event>> {

    double lat;
    double lon;
    public static final String TAG = getEvents.class.getSimpleName();
    public List<Event> eventList = new ArrayList<Event>();


    public getEvents(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    @Override
    protected List<Event> doInBackground(Double... params) {

        Log.i(TAG, "do in background");
        // home 192.168.42.158
        // brighton 192.168.42.69
        String urlString = ("http://192.168.42.158:3000/events?lat=" + lat + "&lng=" + lon + "&distance=3500&sort=venue&access_token=");
        //"1038263616207618|iuAkTxRvDGNVRZUSdqfz4M4T6aU");
        InputStream in = null;
        int resCode = -1;

        BufferedReader br = null;
        String output = null;

        Gson gson = new Gson();


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

                JsonParser parser = new JsonParser();
                JsonObject rootObejct = parser.parse(output).getAsJsonObject();
                JsonElement eventElement = rootObejct.get("events");

                List<Event> projectList = new ArrayList<>();

//Check if "project" element is an array or an object and parse accordingly...
                if (eventElement.isJsonObject()) {
                    //The returned list has only 1 element
                    Event Event = gson.fromJson(eventElement, Event.class);
                    eventList.add(Event);
                } else if (eventElement.isJsonArray()) {
                    //The returned list has >1 elements
                    Type type = new TypeToken<List<Event>>() {
                    }.getType();
                    eventList = gson.fromJson(eventElement, type);
                }


            }

            httpConn.disconnect();
        } catch (MalformedURLException e) {

            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();

        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }

        return eventList;
    }


    //runs on UI thread and therefore has access to UI Objects unlike doinBackground

    @Override
    protected void onPostExecute(List<Event> eventList) {
        System.out.println("post .... \n");

        MapsActivity.displayEvents(eventList);
    }
}


