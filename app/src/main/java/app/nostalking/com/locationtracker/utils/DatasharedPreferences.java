package app.nostalking.com.locationtracker.utils;

import android.content.Context;
import android.content.SharedPreferences;

import app.nostalking.com.locationtracker.activities.UpdateActivity;
import app.nostalking.com.locationtracker.fragments.FragmentDecision;

/**
 * Created by Applaudo Dev on 4/13/2015.
 */
public class DatasharedPreferences {
    private static final String REPORT_ID = "id";
    private static final String UPDATE_FREQUENCY = "frequency";
    private static final String GPS_PRIORITY = "priority";
    private static final String FIRST_TIME = "firstTime";
    private static final String STAY_LOGED = "stay_loged";
    private static final String MY_PREFS_NAME = "TracerPreferences";
    private static final String PERSONAL_ID = "MyDeviceId";
    private static final String EMPTY_ID = "";
    private static final String USAGE_PREFERENCE = "pref";
    private static final String TRACK_ID = "currentTrack";
    private static final int EMPTY_TRACK_ID = 0;
    private SharedPreferences mPreferences;

    public DatasharedPreferences(Context context){
        mPreferences = context.getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
    }

    public int getUsagePreference(){
        return mPreferences.getInt(USAGE_PREFERENCE, FragmentDecision.CASE_DEFAULT);
    }

    public void saveUsagePreference(int preference){
        mPreferences.edit().putInt(USAGE_PREFERENCE, preference).commit();
    }

    public void saveFirstTime(boolean isFirstTime){
        mPreferences.edit().putBoolean(FIRST_TIME, isFirstTime).commit();
    }

    public boolean isFirstTime(){
        return mPreferences.getBoolean(FIRST_TIME, false);
    }

    public void storeMyId(String id){
        mPreferences.edit().putString(PERSONAL_ID, id).commit();
    }

    public void saveReportingId(String myReportingId){
        mPreferences.edit().putString(REPORT_ID, myReportingId).commit();
    }

    public String getReportId(){
        return mPreferences.getString(REPORT_ID, "");
    }

    public String getMyId(){
       return mPreferences.getString(PERSONAL_ID, EMPTY_ID);
    }

    public void storeTrackId(int trackID){
        mPreferences.edit().putInt(TRACK_ID, trackID);
    }

    public void saveUpdateFrequency(int updateFrequency){
        mPreferences.edit().putInt(UPDATE_FREQUENCY, updateFrequency).commit();
    }

    public int getUpdateFrequency(){
        return mPreferences.getInt(UPDATE_FREQUENCY, UpdateActivity.PRIORITY_LOW);
    }

    public int getTrackId(){
        return mPreferences.getInt(TRACK_ID, EMPTY_TRACK_ID);
    }

    public void stayLogged(boolean stayLogged){
        mPreferences.edit().putBoolean(STAY_LOGED, stayLogged).commit();
    }

    public void savePriority(int gpsPriority){
        mPreferences.edit().putInt(GPS_PRIORITY, gpsPriority).commit();
    }

    public void getPriority(){
        mPreferences.getInt(GPS_PRIORITY, UpdateActivity.PRIORITY_LOW);
    }

    public boolean isLoged(){
        return mPreferences.getBoolean(STAY_LOGED, false);
    }
}
