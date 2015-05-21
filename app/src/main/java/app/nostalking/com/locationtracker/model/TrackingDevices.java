package app.nostalking.com.locationtracker.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Applaudo Dev on 4/16/2015.
 */
public class TrackingDevices {

    @SerializedName("status")
    private String mStatus;

    @SerializedName("devices")
    private ArrayList<Devices> mDevices;

    public String getmStatus(){
        return mStatus;
    }

    public ArrayList<Devices> getmDevices(){
        return mDevices;
    }

    public class Devices implements Serializable {
        @SerializedName("device_name")
        private String mDevice;

        @SerializedName("device_search_id")
        private String mDeviceRecordsId;

        @SerializedName("date_time")
        private String mDate;

        public String getParsedDate(){
            String[] date = mDate.split(" ");
            return date[0] + " at " + date[1];
        }


        public String getmDevice(){
            return mDevice;
        }

        public String getSmSearchId(){
            return mDeviceRecordsId;
        }
    }
}
