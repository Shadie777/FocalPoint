package com.seankeating.focalpointPresenter;

import android.Manifest;
import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Typeface;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.TextView;

import com.google.android.gms.appindexing.AppIndex;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.seankeating.focalpoint.R;
import com.seankeating.focalpointModel.Event;
import com.seankeating.focalpointModel.VenueLocation;
import com.seankeating.focalpointViews.ScreenSliderActivity;

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

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class MapsActivity extends FragmentActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback,
        LocationListener, GoogleMap.OnInfoWindowClickListener, GoogleMap.OnMapClickListener {

    //list of variables storing the date
    private static int mYear;
    private static int mMonth;
    private static int mDay;

    static final int DATE_DIALOG_ID = 0;

    //variable storing the date text view
    TextView datetext;

    // Refresh menu item
    public MenuItem refreshMenuItem;

   //google maps variable storage
    private static GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    Location mLocation; //store location
    private final static int CONN_FAILURE_RES_REQUEST = 9000; //connection failure
    private LocationRequest mLocationRequest; //location request
    LatLng latLng;

    //hashmap storing marker with event
    static HashMap<Marker, Event> eventMarkerMap = new HashMap<Marker, Event>();
    //storage of event objects
    public static List<Event> eventList1 = new ArrayList<Event>();

    //used for testing
    public static final String TAG = MapsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        // get action bar
        ActionBar actionBar = getActionBar();

        // Enabling Up / Back navigation

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //initialise client
        // ATTENTION: This "addApi(AppIndex.API)"was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(AppIndex.API).build();

        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
                .setInterval(10 * 1000)
                .setFastestInterval(1 * 1000);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main_actions, menu);

         datetext = new TextView(this);
        datetext.setText(new StringBuilder()
                // Month is 0 based so add 1
                .append(mMonth + 1).append("-")
                .append(mDay).append("-")
                .append(mYear).append(" "));
        datetext.setPadding(5, 0, 5, 0);
        datetext.setTypeface(null, Typeface.BOLD);
        datetext.setTextSize(14);
        menu.add(0, Menu.FIRST, 1, "").setActionView(datetext).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {
            case R.id.action_filter:
                showDialog(DATE_DIALOG_ID);
                return true;
            case R.id.action_refresh:
                double currentLat = mLocation.getLatitude();
                double currentLon = mLocation.getLongitude();
                refreshMenuItem = item;
                mMap.clear();
                getEvents mGetEvents = new getEvents(currentLat, currentLon);
                mGetEvents.execute();
                return true;
            case R.id.action_tutorial:
                Intent intent = new Intent(MapsActivity.this, ScreenSliderActivity.class);
                startActivity(intent);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year,
                                      int monthOfYear, int dayOfMonth) {
                    mYear = year;
                    mMonth = monthOfYear;
                    mDay = dayOfMonth;
                    updateDisplay();

                    //getEvents mGetEvents = new getEvents(currentLat, currentLon);
                    //mGetEvents.execute();

                }
            };

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this,
                        mDateSetListener,
                        mYear, mMonth, mDay);
        }

        return null;
    }

    private void updateDisplay() {
        this.datetext.setText(
                new StringBuilder()
                        // Month is 0 based so add 1
                        .append(mMonth + 1).append("-")
                        .append(mDay).append("-")
                        .append(mYear).append(" "));

        double currentLat = mLocation.getLatitude();
        double currentLon = mLocation.getLongitude();
        mMap.clear();
        getEvents mGetEvents = new getEvents(currentLat, currentLon);
        mGetEvents.execute();
    }

    private void setUpMap() {
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14.0f));

        mMap.setOnInfoWindowClickListener(this);
        mMap.setOnMapClickListener(this);
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
        mMap.clear();
//
//        MarkerOptions options = new MarkerOptions()
//                .draggable(true)
//                .position(latLng)
//                .title("You");

       // mMap.addMarker(options);

        getEvents mGetEvents = new getEvents(currentLat, currentLon);
        mGetEvents.execute();

        setUpMap();
    }

    public void onInfoWindowClick(Marker marker) {

        Event event = eventMarkerMap.get(marker);
        Intent intent = new Intent(MapsActivity.this, DisplayEventDetails.class);
        intent.putExtra("Event", (Parcelable) event);
        startActivity(intent);
    }

    @Override
    public void onMapClick(LatLng point) {
        // TODO Auto-generated method stub
        mMap.clear();


        MarkerOptions options = new MarkerOptions()
               .draggable(true)
              .position(point)
                .title("You");

        double lat = point.latitude;
        double lng = point.longitude;

        mMap.addMarker(options);
        getEvents mGetEvents = new getEvents(lat, lng);
        mGetEvents.execute();
    }

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


    public static void displayEvents(List<Event> eventList) {
        String actualdate =  new StringBuilder()
                // Month is 0 based so add 1
                .append(mYear).append("-")
                .append("0").append(mMonth + 1).append("-")
                .append(mDay).toString();

        System.out.println(actualdate);
//      eventMarkerMap = new HashMap<Marker, Event>();


        for (int i = 0; i < eventList.size(); i++) {
            Event event = eventList.get(i);
            if(eventList1.size() == 0) {
                eventList1.add(event);
            }
            String datetime = event.getEventStarttime();
            String date = datetime.substring(0, 10);

            VenueLocation vl = event.getVenueLocation();
            String venueTitle = event.getVenueName();
            String eventTitle = event.getEventName();
          //  System.out.println(date);
            LatLng pos = new LatLng(vl.getLatitude(), vl.getLongitude());

            if(actualdate.contentEquals(date)) {
                System.out.println("true");
                Marker m = mMap.addMarker(new MarkerOptions()
                        .position(pos)
                        .title(venueTitle)
                        .snippet(eventTitle)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

                eventMarkerMap.put(m, event);
           }

        }


    }


    private class getEvents extends AsyncTask<Double, Integer, List<Event>> {

        double lat;
        double lon;
        public  final String TAG = getEvents.class.getSimpleName();
        public List<Event> eventList = new ArrayList<Event>();


        public getEvents(double lat, double lon) {
            this.lat = lat;
            this.lon = lon;
        }

        @Override
        protected void onPreExecute() {
            // set the progress bar view

            if(refreshMenuItem != null) {
                refreshMenuItem.setActionView(R.layout.action_progressbar);

                refreshMenuItem.expandActionView();
            }
        }


        @Override
        protected List<Event> doInBackground(Double... params) {

            Log.i(TAG, "do in background");
            // home 192.168.42.158
            // brighton 192.168.42.69
            String urlString = ("http://192.168.42.155:3000/events?lat=" + lat + "&lng=" + lon + "&distance=3500&sort=venue&access_token=");
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



//Check if  element is an array or an object
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

            if(refreshMenuItem != null) {
                refreshMenuItem.collapseActionView();
                // remove the progress bar view
                refreshMenuItem.setActionView(null);
            }
            MapsActivity.displayEvents(eventList);
        }
    }


}