package app.nostalking.com.locationtracker.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Applaudo Dev on 5/19/2015.
 */
public class StatusLog {
    @SerializedName("status")
    private String mStatus;

    @SerializedName("phone_log")
    private String mFullLog;

    public String getmFullLog(){
        return mFullLog;
    }

    public String getmStatus(){
        return mStatus;
    }
}
