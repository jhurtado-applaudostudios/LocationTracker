package app.nostalking.com.locationtracker.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Applaudo Dev on 4/10/2015.
 */
public class SimpleConfirmation {
    @SerializedName("status")
    private String mStatus;

    public String getmStatus(){
        return mStatus;
    }
}
