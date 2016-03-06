package com.seankeating.focalpointModel;



import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EventStats implements Parcelable {

    @SerializedName("attendingCount")
    @Expose
    private Integer attendingCount;
    @SerializedName("declinedCount")
    @Expose
    private Integer declinedCount;
    @SerializedName("maybeCount")
    @Expose
    private Integer maybeCount;
    @SerializedName("noreplyCount")
    @Expose
    private Integer noreplyCount;

    /**
     *
     * @return
     * The attendingCount
     */
    public Integer getAttendingCount() {
        return attendingCount;
    }

    /**
     *
     * @param attendingCount
     * The attendingCount
     */
    public void setAttendingCount(Integer attendingCount) {
        this.attendingCount = attendingCount;
    }

    /**
     *
     * @return
     * The declinedCount
     */
    public Integer getDeclinedCount() {
        return declinedCount;
    }

    /**
     *
     * @param declinedCount
     * The declinedCount
     */
    public void setDeclinedCount(Integer declinedCount) {
        this.declinedCount = declinedCount;
    }

    /**
     *
     * @return
     * The maybeCount
     */
    public Integer getMaybeCount() {
        return maybeCount;
    }

    /**
     *
     * @param maybeCount
     * The maybeCount
     */
    public void setMaybeCount(Integer maybeCount) {
        this.maybeCount = maybeCount;
    }

    /**
     *
     * @return
     * The noreplyCount
     */
    public Integer getNoreplyCount() {
        return noreplyCount;
    }

    /**
     *
     * @param noreplyCount
     * The noreplyCount
     */
    public void setNoreplyCount(Integer noreplyCount) {
        this.noreplyCount = noreplyCount;
    }



    public EventStats(Parcel in){
        this.attendingCount = (Integer) in.readSerializable();

        this.declinedCount = (Integer) in.readSerializable();

        this.maybeCount = (Integer) in.readSerializable();

        this.noreplyCount = (Integer) in.readSerializable();
    }

    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeSerializable(this.attendingCount);
        dest.writeSerializable(this.declinedCount );
        dest.writeSerializable(this.maybeCount);
        dest.writeSerializable(this.noreplyCount);

    }
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public EventStats createFromParcel(Parcel in) {
            return new EventStats(in);
        }

        public EventStats[] newArray(int size) {
            return new EventStats[size];
        }
    };
}