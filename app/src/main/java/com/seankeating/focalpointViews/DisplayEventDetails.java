package com.seankeating.focalpointViews;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.model.Marker;
import com.seankeating.focalpoint.R;
import com.seankeating.focalpointModel.Event;

import java.util.HashMap;

public class DisplayEventDetails extends Activity {

   Event event;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        Bundle data = intent.getExtras();
        this.event =  data.getParcelable("Event");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_event_details);
        TextView tv = (TextView) findViewById(R.id.EventTitle);
        String eventName = event.getEventName();
        tv.setText(eventName);

    }
}
