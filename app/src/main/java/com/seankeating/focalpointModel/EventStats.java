package com.seankeating.focalpointModel;



import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EventStats {

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

}