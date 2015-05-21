package app.nostalking.com.locationtracker.activities;

import android.app.Application;

import app.nostalking.com.locationtracker.R;
import app.nostalking.com.locationtracker.intefaces.RetrofitEndPoints;
import app.nostalking.com.locationtracker.utils.DataSharedPreferences;
import app.nostalking.com.locationtracker.utils.IgnoreExtra;
import retrofit.RestAdapter;

/**
 * Created by Juan Hurtado on 4/10/2015.
 */
public class TrackerApplication extends Application {

    private DataSharedPreferences mSharedPreferences;
    private static IgnoreExtra extra;
    private RetrofitEndPoints mApi;
    private static TrackerApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        String API_URL = getResources().getString(R.string.url_api);
        extra = new IgnoreExtra();
        mInstance = this;
        mSharedPreferences = new DataSharedPreferences(getApplicationContext());

        RestAdapter restAdapter = new RestAdapter.Builder()
            .setLogLevel(RestAdapter.LogLevel.FULL).setEndpoint(API_URL).build();
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
    
    public DataSharedPreferences getDataSharedPreferences(){
        return mSharedPreferences;
    }


}
