package com.seankeating.focalpointModel;


import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EventList {

    @SerializedName("events")
    @Expose
    private List<Event> events;
    @SerializedName("metadata")
    @Expose
    private MetaData metadata;

    /**
     *
     * @return
     * The events
     */
    public List<Event> getEvents() {return events;}

    /**
     *
     * @param events
     * The events
     */
    public void setEvents(List<Event> events) {
        this.events = events;
    }

    /**
     *
     * @return
     * The metadata
     */
    public MetaData getMetadata() {
        return metadata;
    }

    /**
     *
     * @param metadata
     * The metadata
     */
    public void setMetadata(MetaData metadata) {
        this.metadata = metadata;
    }

}