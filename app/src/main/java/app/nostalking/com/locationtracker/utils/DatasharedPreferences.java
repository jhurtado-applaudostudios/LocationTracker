package app.nostalking.com.locationtracker.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Applaudo Dev on 4/13/2015.
 */
public class DatasharedPreferences {
    private static final String STAY_LOGED = "stay_loged";
    private static final String MY_PREFS_NAME = "TracerPreferences";
    private static final String PERSONAL_ID = "MyDeviceId";
    private static final String EMPTY_ID = "";
    private static final String TRACK_ID = "currentTrack";
    private static final int EMPTY_TRACK_ID = 0;
    private SharedPreferences mPreferences;

    public DatasharedPreferences(Context context){
        mPreferences = context.getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void storeMyId(String id){
        mPreferences.edit().putString(PERSONAL_ID, id).commit();
    }

    public String getMyId(){
       return mPreferences.getString(PERSONAL_ID, EMPTY_ID);
    }

    public void storeTrackId(int trackID){
        mPreferences.edit().putInt(TRACK_ID, trackID);
    }

    public int getTrackId(){
        return mPreferences.getInt(TRACK_ID, EMPTY_TRACK_ID);
    }

    public void stayLogged(boolean stayLogged){
        mPreferences.edit().putBoolean(STAY_LOGED, stayLogged).commit();
    }

    public boolean isLoged(){
        return mPreferences.getBoolean(STAY_LOGED, false);
    }
}
