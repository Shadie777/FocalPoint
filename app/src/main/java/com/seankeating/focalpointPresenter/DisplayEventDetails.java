package com.seankeating.focalpointPresenter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.seankeating.focalpoint.R;
import com.seankeating.focalpointModel.Event;
import com.seankeating.focalpointModel.VenueLocation;

import java.io.InputStream;
import java.net.URL;

public class DisplayEventDetails extends Activity {

    static ImageView imgView;
    Event event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        Bundle data = intent.getExtras();
        this.event = data.getParcelable("Event");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_event_details);


        TextView tv = (TextView) findViewById(R.id.EventTitle);
        String eventName = event.getEventName();
        tv.setText(eventName);


        String datetime = event.getEventStarttime();


        TextView dateview = (TextView) findViewById(R.id.dateview);

        String mYear = datetime.substring(0, 4);
        String mMonth = datetime.substring(5, 7);
        String mDay = datetime.substring(8, 10);

        String date = new StringBuilder()
                .append(mDay).append("-")
                .append(mMonth).append("-")
                .append(mYear).toString();

        dateview.setText(date);


        TextView timeview = (TextView) findViewById(R.id.timeView);
        String time = datetime.substring(11, 16);
        timeview.setText(time);

        VenueLocation vl = event.getVenueLocation();

        String street = vl.getStreet();
        String zip = vl.getZip();

        TextView locationview = (TextView) findViewById(R.id.LocationView);
        locationview.setText(street + "" + ", " + zip);


        TextView descview = (TextView) findViewById(R.id.descview);
        String desc = event.getEventDescription();
        descview.setText(desc);


        this.imgView = (ImageView) findViewById(R.id.imageView);
        loadImage loadImage = new loadImage(event.getEventCoverPicture());
        loadImage.execute();
    }


    public static void drawPic(Drawable d) {
        imgView.setImageDrawable(d);
    }


    class loadImage extends AsyncTask<String, Integer, Drawable> {

        String url;


        public loadImage(String url) {
            this.url = url;
        }

        @Override
        protected Drawable doInBackground(String... params) {
            Drawable d;
            try {
                InputStream is = (InputStream) new URL(url).getContent();
                d = Drawable.createFromStream(is, "src name");
            } catch (Exception e) {
                return null;
            }

            return d;
        }


        //runs on UI thread and therefore has access to UI Objects unlike doinBackground

        @Override
        protected void onPostExecute(Drawable d) {
            DisplayEventDetails.drawPic(d);
        }
    }
}
