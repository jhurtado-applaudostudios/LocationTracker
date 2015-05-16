package app.nostalking.com.locationtracker.service;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.CallLog;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationRequest;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import app.nostalking.com.locationtracker.activities.TrackerApplication;
import app.nostalking.com.locationtracker.activities.UpdateActivity;
import app.nostalking.com.locationtracker.model.SimpleConfirmation;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Applaudo Dev on 5/6/2015.
 */
public class LocationService extends Service {
    private int mLoop = 0;
    private int mPriorityType = 0;
    private String mCallType = "";
    private LocationRequest mLocationRequest;
    private LocationClient mLocationClient;
    private static final ScheduledExecutorService worker =
            Executors.newSingleThreadScheduledExecutor();
    private GooglePlayServicesClient.OnConnectionFailedListener mFailListener = new GooglePlayServicesClient.OnConnectionFailedListener() {
        @Override
        public void onConnectionFailed(ConnectionResult arg0) {
        }
    };
    private GooglePlayServicesClient.ConnectionCallbacks mCallbacks = new  GooglePlayServicesClient.ConnectionCallbacks() {

        @Override
        public void onDisconnected() {
            // TODO Auto-generated method stub

        }

        @Override
        public void onConnected(Bundle arg0) {
            mLocationClient.requestLocationUpdates(mLocationRequest, mLocationListener);
        }

    };
    private com.google.android.gms.location.LocationListener mLocationListener = new com.google.android.gms.location.LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            mLoop = 0;
            final String logs = getCallDetails();
            Runnable task = new Runnable() {
                public void run() {
                    saveToDb(location, logs);
                }
            };
            worker.schedule(task, 5, TimeUnit.SECONDS);
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mLocationRequest = LocationRequest.create();
        mPriorityType = TrackerApplication.getInstance().getDataSharedPreferences().getUpdateFrequency();

        switch (mPriorityType){
            case UpdateActivity.FREQUENCY_15MIN:
                mLocationRequest.setInterval(1);
                break;
            case UpdateActivity.FREQUENCY_30MIN:
                mLocationRequest.setInterval(1800000);
                break;
            case UpdateActivity.FREQUENCY_45MIN:
                mLocationRequest.setInterval(2700000);
                break;
            case UpdateActivity.FREQUENCY_1HOUR:
                mLocationRequest.setInterval(3600000);
                break;
            default:
                mLocationRequest.setInterval(1);
                break;
        }

        mPriorityType = TrackerApplication.getInstance().getDataSharedPreferences().getPriority();
        switch (mPriorityType){
            case UpdateActivity.PRIORITY_LOW:
                mLocationRequest.setPriority(LocationRequest.PRIORITY_NO_POWER);
                break;
            case UpdateActivity.PRIORITY_MEDIUM:
                mLocationRequest.setPriority(LocationRequest.PRIORITY_LOW_POWER);
                break;
            case UpdateActivity.PRIORITY_NORMAL:
                mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
                break;
            case UpdateActivity.PRIORITY_HIGH:
                mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                break;
            default:
                mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
                break;
        }


        mLocationClient = new LocationClient(getApplicationContext(), mCallbacks , mFailListener);
        mLocationClient.connect();
        return super.onStartCommand(intent, flags, startId);
    }

    private void saveToDb(Location location, String logs){
        if(location != null){
            TrackerApplication.getInstance().getmApi().saveLocationInServer(location.getLatitude(), location.getLongitude(),TrackerApplication.getInstance()
            .getDataSharedPreferences().getReportId(),logs , new Callback<Response>() {
                @Override
                public void success(Response simpleConfirmation, Response response) {
                }

                @Override
                public void failure(RetrofitError error) {
                }
            });
        }

    }

    private String getCallDetails() {

        StringBuffer sb = new StringBuffer();
        Cursor managedCursor = getApplicationContext().getContentResolver().query(CallLog.Calls.CONTENT_URI, null,
                null, null, null);
        int lenght = managedCursor.getColumnCount();
        int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
        int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
        int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);
        int name = managedCursor.getColumnIndex(CallLog.Calls.CACHED_NAME);
        while (managedCursor.moveToNext()) {
            mLoop++;
            if(mLoop <= lenght - 3){
                String callName = managedCursor.getString(name);
                String phNumber = managedCursor.getString(number);
                String callType = managedCursor.getString(type);
                String callDate = managedCursor.getString(date);
                Date callDayTime = new Date(Long.valueOf(callDate));
                String callDuration = managedCursor.getString(duration);
                String dir = null;
                int dircode = Integer.parseInt(callType);
                switch (dircode) {
                    case CallLog.Calls.OUTGOING_TYPE:
                        dir = "Outgoing";
                        mCallType = " call to ";
                        break;

                    case CallLog.Calls.INCOMING_TYPE:
                        mCallType = " call from ";
                        dir = "Incoming";
                        break;

                    case CallLog.Calls.MISSED_TYPE:
                        mCallType = " call from ";
                        dir = "Missed";
                        break;
                }

                sb.append("\n" + dir + mCallType + callName + " (" + phNumber + ")" +
                "\nat " + callDayTime + "\nDuration: " + callDuration + "seconds\n");
            }

        }
        managedCursor.close();
        return sb.toString();

    }
}
