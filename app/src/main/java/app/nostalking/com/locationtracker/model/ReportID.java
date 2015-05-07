package app.nostalking.com.locationtracker.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Applaudo Dev on 5/5/2015.
 */
public class ReportID {
    @SerializedName("unique_id")
    private String mPostingId;

    @SerializedName("status")
    private String mStatus;

    public String getmPostingId(){
        return mPostingId;
    }

    public String getmStatus(){
        return mStatus;
    }
}
