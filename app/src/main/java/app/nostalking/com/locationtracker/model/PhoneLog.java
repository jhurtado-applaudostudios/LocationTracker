package app.nostalking.com.locationtracker.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Applaudo Dev on 5/18/2015.
 */
public class PhoneLog {
    @SerializedName("mLogList")
    private ArrayList<LogList> mLogList;

    public ArrayList<LogList> getmLogList(){
        return mLogList;
    }

    public void setmLogList(ArrayList<LogList> logList){
        mLogList = logList;
    }

    public LogList getInstance(){
        return new LogList();
    }

    public class LogList implements Serializable {
        @SerializedName("mDate")
        private String mDate;

        @SerializedName("mType")
        private String mType;

        @SerializedName("mName")
        private String mName;

        @SerializedName("mDuration")
        private String mDuration;

        @SerializedName("mPhoneNumber")
        private String mPhoneNumber;

        public String getmPhoneNumber(){
            return mPhoneNumber;
        }

        public void setmPhoneNumber(String mPhoneNumber){
            this.mPhoneNumber = mPhoneNumber;
        }

        public String getmDuration(){
            return mDuration;
        }

        public void setmDuration(String mDuration){
            this.mDuration = mDuration;
        }

        public String getmDate(){
            return mDate;
        }

        public void setmDate(String mDate){
            this.mDate = mDate;
        }

        public String getmType(){
            return mType;
        }

        public void setmType(String mType){
            this.mType = mType;
        }

        public String getmName(){
            return mName;
        }

        public void setmName(String mName){
            this.mName = mName;
        }
    }
}


