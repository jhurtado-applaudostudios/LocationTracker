package app.nostalking.com.locationtracker.activities;

import android.app.Application;
import android.util.Log;
import app.nostalking.com.locationtracker.retrofitinterface.RetrofitEndPoints;
import app.nostalking.com.locationtracker.utils.DatasharedPreferences;
import app.nostalking.com.locationtracker.utils.IgnoreExtra;
import retrofit.RestAdapter;

/**
 * Created by Applaudo Dev on 4/10/2015.
 */
public class TrackerApplication extends Application {

    private DatasharedPreferences mSharedPreferences;
    private static IgnoreExtra extra;
    private RetrofitEndPoints mApi;
    private static String  API_URL = "http://tracking-api.site90.com/";
    private static TrackerApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        extra = new IgnoreExtra();
        mInstance = this;
        mSharedPreferences = new DatasharedPreferences(getApplicationContext());
        RestAdapter restAdapter = new RestAdapter.Builder()
            .setLogLevel(RestAdapter.LogLevel.FULL).setLog(new RestAdapter.Log() {
            @Override
            public void log(String message) {
                Log.d("LOL", message.toString());
            }
        }).setEndpoint(API_URL).build();
        mApi = restAdapter.create(RetrofitEndPoints.class);
    }

    public IgnoreExtra getTrashCodeIgnorer(){
        return extra;
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
