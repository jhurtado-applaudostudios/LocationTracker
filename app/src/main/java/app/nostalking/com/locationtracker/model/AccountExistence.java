package app.nostalking.com.locationtracker.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Applaudo Dev on 4/13/2015.
 */
public class AccountExistence {
    @SerializedName("stalker_id")
    private String mStalkerId;

    @SerializedName("status")
    private String mSatus;

    public String getStalkerId(){
        return mStalkerId;
    }

    public String getStatus(){
        return mSatus;
    }
}
