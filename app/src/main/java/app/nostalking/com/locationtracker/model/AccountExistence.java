package app.nostalking.com.locationtracker.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Juan Hurtado on 4/13/2015.
 */
public class AccountExistence implements Serializable{
    @SerializedName("stalker_id")
    private String mStalkerId;

    @SerializedName("status")
    private String mStatus;

    public String getStalkerId(){
        return mStalkerId;
    }

    public String getStatus(){
        return mStatus;
    }
}
