package com.seankeating.focalpointModel;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Event implements Parcelable {

    @SerializedName("venueId")
    @Expose
    private String venueId;
    @SerializedName("venueName")
    @Expose
    private String venueName;
    @SerializedName("venueCoverPicture")
    @Expose
    private String venueCoverPicture;
    @SerializedName("venueProfilePicture")
    @Expose
    private String venueProfilePicture;
    @SerializedName("venueLocation")
    @Expose
    private VenueLocation venueLocation;
    @SerializedName("eventId")
    @Expose
    private String eventId;
    @SerializedName("eventName")
    @Expose
    private String eventName;
    @SerializedName("eventCoverPicture")
    @Expose
    private String eventCoverPicture;
    @SerializedName("eventProfilePicture")
    @Expose
    private String eventProfilePicture;
    @SerializedName("eventDescription")
    @Expose
    private String eventDescription;
    @SerializedName("eventStarttime")
    @Expose
    private String eventStarttime;
    @SerializedName("eventDistance")
    @Expose
    private String eventDistance;
    @SerializedName("eventTimeFromNow")
    @Expose
    private Integer eventTimeFromNow;
    @SerializedName("eventStats")
    @Expose
    private EventStats eventStats;

    /**
     *
     * @return
     * The venueId
     */
    public String getVenueId() {
        return venueId;
    }

    /**
     *
     * @param venueId
     * The venueId
     */
    public void setVenueId(String venueId) {
        this.venueId = venueId;
    }

    /**
     *
     * @return
     * The venueName
     */
    public String getVenueName() {
        return venueName;
    }

    /**
     *
     * @param venueName
     * The venueName
     */
    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }

    /**
     *
     * @return
     * The venueCoverPicture
     */
    public String getVenueCoverPicture() {
        return venueCoverPicture;
    }

    /**
     *
     * @param venueCoverPicture
     * The venueCoverPicture
     */
    public void setVenueCoverPicture(String venueCoverPicture) {
        this.venueCoverPicture = venueCoverPicture;
    }

    /**
     *
     * @return
     * The venueProfilePicture
     */
    public String getVenueProfilePicture() {
        return venueProfilePicture;
    }

    /**
     *
     * @param venueProfilePicture
     * The venueProfilePicture
     */
    public void setVenueProfilePicture(String venueProfilePicture) {
        this.venueProfilePicture = venueProfilePicture;
    }

    /**
     *
     * @return
     * The venueLocation
     */
    public VenueLocation getVenueLocation() {
        return venueLocation;
    }

    /**
     *
     * @param venueLocation
     * The venueLocation
     */
    public void setVenueLocation(VenueLocation venueLocation) {
        this.venueLocation = venueLocation;
    }

    /**
     *
     * @return
     * The eventId
     */
    public String getEventId() {
        return eventId;
    }

    /**
     *
     * @param eventId
     * The eventId
     */
    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    /**
     *
     * @return
     * The eventName
     */
    public String getEventName() {
        return eventName;
    }

    /**
     *
     * @param eventName
     * The eventName
     */
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    /**
     *
     * @return
     * The eventCoverPicture
     */
    public String getEventCoverPicture() {
        return eventCoverPicture;
    }

    /**
     *
     * @param eventCoverPicture
     * The eventCoverPicture
     */
    public void setEventCoverPicture(String eventCoverPicture) {
        this.eventCoverPicture = eventCoverPicture;
    }

    /**
     *
     * @return
     * The eventProfilePicture
     */
    public String getEventProfilePicture() {
        return eventProfilePicture;
    }

    /**
     *
     * @param eventProfilePicture
     * The eventProfilePicture
     */
    public void setEventProfilePicture(String eventProfilePicture) {
        this.eventProfilePicture = eventProfilePicture;
    }

    /**
     *
     * @return
     * The eventDescription
     */
    public String getEventDescription() {
        return eventDescription;
    }

    /**
     *
     * @param eventDescription
     * The eventDescription
     */
    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    /**
     *
     * @return
     * The eventStarttime
     */
    public String getEventStarttime() {
        return eventStarttime;
    }

    /**
     *
     * @param eventStarttime
     * The eventStarttime
     */
    public void setEventStarttime(String eventStarttime) {
        this.eventStarttime = eventStarttime;
    }

    /**
     *
     * @return
     * The eventDistance
     */
    public String getEventDistance() {
        return eventDistance;
    }

    /**
     *
     * @param eventDistance
     * The eventDistance
     */
    public void setEventDistance(String eventDistance) {
        this.eventDistance = eventDistance;
    }

    /**
     *
     * @return
     * The eventTimeFromNow
     */
    public Integer getEventTimeFromNow() {
        return eventTimeFromNow;
    }

    /**
     *
     * @param eventTimeFromNow
     * The eventTimeFromNow
     */
    public void setEventTimeFromNow(Integer eventTimeFromNow) {
        this.eventTimeFromNow = eventTimeFromNow;
    }

    /**
     *
     * @return
     * The eventStats
     */
    public EventStats getEventStats() {
        return eventStats;
    }

    /**
     *
     * @param eventStats
     * The eventStats
     */
    public void setEventStats(EventStats eventStats) {
        this.eventStats = eventStats;
    }


    public Event(Parcel in){
        this.venueId = in.readString();
        this.venueName = in.readString();
        this.venueCoverPicture = in.readString();
        this.venueLocation = (VenueLocation) in.readParcelable(VenueLocation.class.getClassLoader());
        this.eventId = in.readString();
        this.eventName = in.readString();
        this.eventCoverPicture = in.readString();
        this.eventProfilePicture = in.readString();
        this.eventDescription = in.readString();
        this.eventStarttime = in.readString();
        this.eventDistance = in.readString();
        this.eventTimeFromNow = (Integer) in.readSerializable();
        this.eventStats = (EventStats) in.readParcelable(EventStats.class.getClassLoader());




    }

    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.venueId);
        dest.writeString(this.venueName);
        dest.writeString(this.venueCoverPicture);
        dest.writeParcelable(venueLocation, flags);
        dest.writeString(this.eventId);
        dest.writeString(this.eventName);
        dest.writeString(this.eventCoverPicture);
        dest.writeString(this.eventProfilePicture);
        dest.writeString(this.eventDescription);
        dest.writeString(this.eventStarttime);
        dest.writeString(this.eventDistance);
        dest.writeSerializable(this.eventTimeFromNow);
        dest.writeParcelable(eventStats, flags);
    }
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        public Event[] newArray(int size) {
            return new Event[size];
        }
    };
}