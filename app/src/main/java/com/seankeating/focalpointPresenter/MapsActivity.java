package com.seankeating.focalpointPresenter;

import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.facebook.FacebookSdk;
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
import com.seankeating.focalpointModel.MarkerStateManager;
import com.seankeating.focalpointModel.SessionManager;
import com.seankeating.focalpointModel.VenueLocation;
import com.seankeating.focalpointViews.LoginActivity;
import com.seankeating.focalpointViews.ScreenSliderActivity;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
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
        LocationListener, GoogleMap.OnInfoWindowClickListener, GoogleMap.OnMapClickListener,
        GoogleMap.OnMyLocationButtonClickListener {

    //counter for number of events found
    static int counter = 0;
    int error = 0;
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

    //location of user placed marker
    LatLng customLocation;
    MarkerOptions customMarker;

    //public variable of user/marker location to use anywhere
    LatLng latLng;

    //hashmap storing marker with event
    static HashMap<Marker, Event> eventMarkerMap = new HashMap<Marker, Event>();

    //used for testing
    public static final String TAG = MapsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_maps);

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        // get action bar
        ActionBar actionBar = getActionBar();

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


    //create the action bar
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main_actions, menu);

        //set a text view in action bar for date
        datetext = new TextView(this);

        String formatday;

        if(mDay < 10){
            formatday = new StringBuilder()
                    .append("0").append(mDay).toString();
        }else{
            formatday = new StringBuilder()
                    .append(mDay).toString();
        }

        datetext.setText(
                new StringBuilder()
                        // Month is 0 based so add 1
                        .append(formatday).append("-")
                        .append("0").append(mMonth + 1).append("-")
                        .append(mYear).append(" "));


        datetext.setPadding(5, 0, 5, 0);
        datetext.setTypeface(null, Typeface.BOLD);
        datetext.setTextSize(14);
        menu.add(0, Menu.FIRST, 1, "").setActionView(datetext).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        double currentLat = mLocation.getLatitude();
        double currentLon = mLocation.getLongitude();
        getEvents mGetEvents;
        // Take appropriate action for each action item click
        switch (item.getItemId()) {
            case R.id.action_filter:
                //if user placed marker
                if (customLocation != null) {
                    //save it
                    MarkerStateManager msm = new MarkerStateManager(this);
                    msm.saveMapState(customLocation);
                    customMarker = msm.getSavedMarkerPosition();
                    msm.delete();
                }
                showDialog(DATE_DIALOG_ID);
                return true;
            case R.id.action_refresh:
                refreshMenuItem = item;
                mMap.clear();
                //if user placed marker
                if (customLocation != null) {
                    //save it
                    MarkerStateManager msm = new MarkerStateManager(this);
                    msm.saveMapState(customLocation);
                    customMarker = msm.getSavedMarkerPosition();
                    msm.delete();

                    setUpMap();
                } else {
                    //otherwise just get events on location
                    mGetEvents = new getEvents(currentLat, currentLon);
                    mGetEvents.execute();
                }
                return true;
            case R.id.action_tutorial:
                Intent intent = new Intent(MapsActivity.this, ScreenSliderActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_logout:
                logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    //handles logout of system
    public void logout() {

        SessionManager sm = new SessionManager(this);
        sm.clearSession();

        com.facebook.login.LoginManager.getInstance().logOut();
        Intent i = new Intent(MapsActivity.this, LoginActivity.class);
        startActivity(i);
        finish();
    }

    //shows a date picker
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


    //returns the date picked by user by setting it to date variables
    private DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year,
                                      int monthOfYear, int dayOfMonth) {
                    mYear = year;
                    mMonth = monthOfYear;
                    mDay = dayOfMonth;
                    updateDisplay();
                }
            };


    //updates display of the date in actionbar and gets new events based on that date
    private void updateDisplay() {
        //format the day variable to show a 0 in front of it  ifits between the 1st and the 10th
        String formatday;

        if(mDay < 10){
            formatday = new StringBuilder()
                    .append("0").append(mDay).toString();
        }else{
            formatday = new StringBuilder()
                    .append(mDay).toString();
        }

        this.datetext.setText(
                new StringBuilder()
                        // Month is 0 based so add 1
                        .append(formatday).append("-")
                        .append("0").append(mMonth + 1).append("-")
                        .append(mYear).append(" "));

        mMap.clear();

        double currentLat = mLocation.getLatitude();
        double currentLon = mLocation.getLongitude();

        // if a marker is placed
        if (customLocation != null) {
            //filter based on marker
            setUpMap();
        } else {
            // otherwise filter based on location
            getEvents mGetEvents = new getEvents(currentLat, currentLon);
            mGetEvents.execute();
        }
    }

    //sets up the map to allow for click listeners and automatic camera moving
    private void setUpMap() {

        //for if the user placed a marker when actvity was paused
        if (customLocation != null) {
            mMap.addMarker(customMarker);

            //set latLng as customer marker instead of gps location
            latLng = customMarker.getPosition();

            //move to marker location
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14.0f));

            double lat = latLng.latitude;
            double lng = latLng.longitude;

            //get events around marker
            getEvents mGetEvents = new getEvents(lat, lng);
            mGetEvents.execute();
        } else { //else move camera to gps location
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14.0f));
        }

        mMap.setOnMyLocationButtonClickListener(this);
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

    // what happens when program connects to google maps api
    @Override
    public void onConnected(Bundle bundle) {
        //uses permission
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);

        //sets user location
        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        mMap.setMyLocationEnabled(true);

        if (mLocation == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

        } else {
            handleNewLocation(mLocation);
        }

        Log.i(TAG, "Location services connected.");
    }


    //what happens when connection is suspended
    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Location services suspended. Please reconnect.");
    }


    //what happens when connection fails
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


    //method that handles when the system tracks a new user location
    private void handleNewLocation(Location location) {
        Log.d(TAG, location.toString());

        //gets user lat and lon
        double currentLat = location.getLatitude();
        double currentLon = location.getLongitude();

        latLng = new LatLng(currentLat, currentLon);
        mMap.clear();

        setUpMap();


        //gets events based on this location
        getEvents mGetEvents = new getEvents(currentLat, currentLon);
        mGetEvents.execute();


    }

    //what happens when marker info window is clicked
    public void onInfoWindowClick(Marker marker) {

        String markertitle = marker.getTitle();
        //if click isnt on a custom marker
        if (markertitle.equals("You")) {
            //do nothing
        } else {
            //get event based on the marker and take it into display event details page
            Event event = eventMarkerMap.get(marker);
            Intent intent = new Intent(MapsActivity.this, DisplayEventDetails.class);
            intent.putExtra("Event", (Parcelable) event);
            startActivity(intent);
        }

    }


    //what happens when user clicks on map
    @Override
    public void onMapClick(LatLng point) {

        //clear map and add marker to the point of click
        mMap.clear();
        MarkerOptions options = new MarkerOptions()
                .draggable(true)
                .position(point)
                .title("You");
        mMap.addMarker(options);
        customLocation = point;

        //take lat and lon of point
        double lat = customLocation.latitude;
        double lng = customLocation.longitude;

        //and get events based on that location
        getEvents mGetEvents = new getEvents(lat, lng);
        mGetEvents.execute();
    }


    //connect to google maps when user returns
    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();

        //marker stored is remembered and used when activity resumes
        MarkerStateManager msm = new MarkerStateManager(this);
        customMarker = msm.getSavedMarkerPosition();
        msm.delete();

    }

    //what happens when user leaves app while logged in
    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }

        // if user placed a marker
        if (customLocation != null) {
            //store marker in shared preferences
            MarkerStateManager msm = new MarkerStateManager(this);
            msm.saveMapState(customLocation);

        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        mMap.clear();

        customLocation = null;

        //take lat and lon of point
        double lat = mLocation.getLatitude();
        double lng = mLocation.getLongitude();

        LatLng userlocation = new LatLng(lat, lng);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(userlocation));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userlocation, 14.0f));

        //and get events based on that location
        getEvents mGetEvents = new getEvents(lat, lng);
        mGetEvents.execute();

        return true;
    }

    //if the location changes enough, user new location
    @Override
    public void onLocationChanged(Location location) {
        handleNewLocation(location);
    }

    public void DisplayToast() {
        Activity activity = (Activity) this;
        Toast.makeText(activity, "No Events!", Toast.LENGTH_LONG).show();
    }

    public void DisplayError() {
        Activity activity = (Activity) this;
        Toast.makeText(activity, "Error, Refresh", Toast.LENGTH_LONG).show();
    }
    //display the events taken from facebook
    public static void displayEvents(List<Event> eventList) {
        String chosendate;
        counter = 0;

        //used to filter based on date
        //if statement formats date in order to filter correctly
        if(mDay < 10){
            chosendate = new StringBuilder()
                    .append(mYear).append("-")
                    .append("0").append(mMonth + 1).append("-")
                    .append("0").append(mDay).toString();
        }else {
            chosendate = new StringBuilder()
                    .append(mYear).append("-")
                    .append("0").append(mMonth + 1).append("-")
                    .append(mDay).toString();

        }

        System.out.println("chosendate: " + chosendate);
        System.out.println();

        //go through the events in list
        for (int i = 0; i < eventList.size(); i++) {
            //get event
            Event event = eventList.get(i);

            //get date of event
            String datetime = event.getEventStarttime();
            String date = datetime.substring(0, 10);


            System.out.println(date);

            //get venue name and event name
            VenueLocation vl = event.getVenueLocation();
            String venueTitle = event.getVenueName();
            String eventTitle = event.getEventName();

            //put lat and lon in LatLng to allow for placement of marker
            LatLng pos = new LatLng(vl.getLatitude(), vl.getLongitude());

            //if the date of event matches the filtered date
            if (chosendate.contentEquals(date)) {
                counter++;
                // add marker to map and put in marker hashmap
                Marker m = mMap.addMarker(new MarkerOptions()
                        .position(pos)
                        .title(venueTitle)
                        .snippet(eventTitle)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

                eventMarkerMap.put(m, event);
            }
        }
    }

    //asyn task that handles connection to node js server: gets event from events based on location
    private class getEvents extends AsyncTask<Double, Integer, List<Event>> {

        double lat;
        double lon;
        public final String TAG = getEvents.class.getSimpleName();
        public List<Event> eventList = new ArrayList<Event>();
        private ProgressBar bar;

        public getEvents(double lat, double lon) {
            //set variables of lat and lon of user
            this.lat = lat;
            this.lon = lon;
            error = 0;
        }


        //what happens before execution of connection
        @Override
        protected void onPreExecute() {

            // set the refresh progress bar view
            if (refreshMenuItem != null) {
                refreshMenuItem.setActionView(R.layout.action_progressbar);
                refreshMenuItem.expandActionView();
            }
        }


        //executes connection from another thread instead of the ui thread
        @Override
        protected List<Event> doInBackground(Double... params) {
            Log.i(TAG, "do in background");
            //url string for node js server,  on localhost for now
            // change to local ip address
            String urlString = ("http://10.1.52.125:3000/events?lat=" + lat + "&lng=" + lon + "&distance=8000&sort=time&access_token=");

            BufferedReader br = null;
            String output = null;

            Gson gson = new Gson();

            try {
                //connect to URL
                URL url = new URL(urlString);
                URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
                url = uri.toURL();
                URLConnection urlConn = url.openConnection();
                HttpURLConnection httpConn = (HttpURLConnection) urlConn;

                //get json from url
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
                error++;
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                error++;
                e.printStackTrace();
            } catch (IOException e) {
                error++;
                e.printStackTrace();
            } catch (URISyntaxException e) {
                error++;
                e.printStackTrace();
            } catch (JsonSyntaxException e) {
                error++;
                e.printStackTrace();
            }
            return eventList;
        }


        //runs on UI thread and therefore has access to UI Objects unlike doinBackground

        @Override
        protected void onPostExecute(List<Event> eventList) {
            System.out.println("post .... \n");

            if (refreshMenuItem != null) {
                refreshMenuItem.collapseActionView();
                // remove the progress bar view
                refreshMenuItem.setActionView(null);
            }

            if (error > 0){
                DisplayError();
            }

            //display events on map
            MapsActivity.displayEvents(eventList);

            //if there are no events then display toast
            if (counter == 0) {
                DisplayToast();
            }
        }
    }
}