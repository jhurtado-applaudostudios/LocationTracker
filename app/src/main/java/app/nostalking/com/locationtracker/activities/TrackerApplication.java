package app.nostalking.com.locationtracker.activities;

import android.app.Application;
import android.content.SharedPreferences;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import app.nostalking.com.locationtracker.retrofitinterface.RetrofitEndPoints;
import app.nostalking.com.locationtracker.utils.DatasharedPreferences;
import retrofit.RestAdapter;

/**
 * Created by Applaudo Dev on 4/10/2015.
 */
public class TrackerApplication extends Application {

    private DatasharedPreferences mSharedPreferences;
    private RetrofitEndPoints mApi;
    private static String  API_URL = "http://locationtracking.byethost8.com/";
    private static TrackerApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        mSharedPreferences = new DatasharedPreferences(getApplicationContext());
        RestAdapter restAdapter = new RestAdapter.Builder().setLogLevel(RestAdapter.LogLevel.FULL).setEndpoint(API_URL).build();
        mApi = restAdapter.create(RetrofitEndPoints.class);
    }

    public static TrackerApplication getInstance(){
        return mInstance;
    }

    public RetrofitEndPoints getmApi(){
        return mApi;
    }
    
    public DatasharedPreferences getDataSharedPreferences(){
        return mSharedPreferences;
    }
}
