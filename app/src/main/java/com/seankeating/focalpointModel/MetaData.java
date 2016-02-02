package com.seankeating.focalpointModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MetaData {


        @SerializedName("venues")
        @Expose
        private Integer venues;
        @SerializedName("venuesWithEvents")
        @Expose
        private Integer venuesWithEvents;
        @SerializedName("events")
        @Expose
        private Integer events;

        /**
         *
         * @return
         * The venues
         */
        public Integer getVenues() {
            return venues;
        }

        /**
         *
         * @param venues
         * The venues
         */
        public void setVenues(Integer venues) {
            this.venues = venues;
        }

        /**
         *
         * @return
         * The venuesWithEvents
         */
        public Integer getVenuesWithEvents() {
            return venuesWithEvents;
        }

        /**
         *
         * @param venuesWithEvents
         * The venuesWithEvents
         */
        public void setVenuesWithEvents(Integer venuesWithEvents) {
            this.venuesWithEvents = venuesWithEvents;
        }

        /**
         *
         * @return
         * The events
         */
        public Integer getEvents() {
            return events;
        }

        /**
         *
         * @param events
         * The events
         */
        public void setEvents(Integer events) {
            this.events = events;
        }

    }

