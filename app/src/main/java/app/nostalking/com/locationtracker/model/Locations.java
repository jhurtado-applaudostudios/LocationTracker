package app.nostalking.com.locationtracker.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Applaudo Dev on 4/20/2015.
 */
public class Locations {

    @SerializedName("status")
    private String mStatus;

    @SerializedName("locations")
    private ArrayList<LocationObject> mLocations;

    public String getmStatus(){
        return mStatus;
    }

    public ArrayList<LocationObject> getmLocations(){
        return mLocations;
    }

    public class LocationObject {

        private String mStreetAddress;

        @SerializedName("latitude")
        private Double mLatitude;

        @SerializedName("longitude")
        private Double mLongitude;

        @SerializedName("device_id")
        private String mDeviceId;

        @SerializedName("phone_log")
        private String mPhoneLog;

        @SerializedName("sent_time")
        private String mTime;

        public void setmStreetAddress(String mStreetAddress){
            this.mStreetAddress = mStreetAddress;
        }

        public String getmStreetAddress(){
            return mStreetAddress;
        }

        public Double getmLatitude(){
            return mLatitude;
        }

        public Double getmLongitude(){
            return mLongitude;
        }

        public String getmDeviceId(){
            return mDeviceId;
        }

        public String[] getmPhoneLog(){
            return mPhoneLog.split(",");
        }

        public void setPhoneLog(String mPhoneLog){
            this.mPhoneLog = mPhoneLog;
        }

        public String getModifyPhoneLog(){
            if(mPhoneLog.contains("null")){
                mPhoneLog = "no phone record";
            }

            return mPhoneLog;
        }

        public String getmTime(){
            return mTime;
        }

        public String getParsedDate(){
            String[] date = mTime.split(" ");
            return date[0] + " at " + date[1];
        }
    }
}
