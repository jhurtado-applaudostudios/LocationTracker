package app.nostalking.com.locationtracker.service;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.CallLog;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationRequest;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import app.nostalking.com.locationtracker.R;
import app.nostalking.com.locationtracker.activities.TrackerApplication;
import app.nostalking.com.locationtracker.activities.UpdateActivity;
import app.nostalking.com.locationtracker.model.PhoneLog;
import app.nostalking.com.locationtracker.utils.ApiStates;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Juan Hurtado on 5/6/2015.
 */
public class LocationService extends Service {
    private String mReportId;
    private final PhoneLog plog = new PhoneLog();
    private final Gson gson = new Gson();
    private LocationRequest mLocationRequest;
    private LocationClient mLocationClient;
    private static final ScheduledExecutorService worker =
            Executors.newSingleThreadScheduledExecutor();
    private final GooglePlayServicesClient.OnConnectionFailedListener mFailListener = new GooglePlayServicesClient.OnConnectionFailedListener() {
        @Override
        public void onConnectionFailed(ConnectionResult arg0) {
            startActivity(new Intent(getApplicationContext(), LocationService.class));
            stopSelf();
        }
    };
    private final GooglePlayServicesClient.ConnectionCallbacks mCallbacks = new  GooglePlayServicesClient.ConnectionCallbacks() {

        @Override
        public void onDisconnected() {
            startActivity(new Intent(getApplicationContext(), LocationService.class));
            stopSelf();
        }

        @Override
        public void onConnected(Bundle arg0) {
            mLocationClient.requestLocationUpdates(mLocationRequest, mLocationListener);
        }

    };
    private final com.google.android.gms.location.LocationListener mLocationListener = new com.google.android.gms.location.LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            Runnable task = new Runnable() {
                public void run() {
                    saveToDb(location);
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
        mReportId = TrackerApplication.getInstance().getDataSharedPreferences().getReportId();
        int mPriorityType = TrackerApplication.getInstance().getDataSharedPreferences().getUpdateFrequency();

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
                mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
                break;
            case UpdateActivity.PRIORITY_MEDIUM:
                mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
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

    private void updateLogs(PhoneLog mPhoneLog){
        String logs = gson.toJson(mPhoneLog);
        TrackerApplication.getInstance().getmApi().updateLogs(logs, mReportId, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                Toast.makeText(getApplicationContext(), "sent", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void failure(RetrofitError error) {
            }
        });
    }

    private void saveToDb(Location location){

        if(location != null){
            TrackerApplication.getInstance().getmApi().saveLocationInServer(location.getLatitude(), location.getLongitude(),mReportId ,new Callback<Response>() {
                @Override
                public void success(Response simpleConfirmation, Response response) {
                        PhoneLog phoneLog = new PhoneLog();
                        phoneLog.setmLogList(getCallDetails());
                        updateLogs(phoneLog);
                }

                @Override
                public void failure(RetrofitError error) {
                }
            });
        }

    }

    private ArrayList<PhoneLog.LogList> getCallDetails() {

        ArrayList<PhoneLog.LogList> allLogs = new ArrayList<>();
        Cursor managedCursor = getApplicationContext().getContentResolver().query(CallLog.Calls.CONTENT_URI, null,
                null, null, null);
        int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
        int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
        int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);
        int name = managedCursor.getColumnIndex(CallLog.Calls.CACHED_NAME);
        int count = managedCursor.getColumnCount();
        int startIndex = count - 101;
        managedCursor.moveToLast();
        for(int i = startIndex; i < count; i++){
            if(count <= 100){
                allLogs.add(extractContactValues(managedCursor,name,number,type,date,duration));
            } else {
                if(i >= count - 100){
                    allLogs.add(extractContactValues(managedCursor,name,number,type,date,duration));
                }
            }
        }
        managedCursor.close();
        return allLogs;

    }

    private PhoneLog.LogList extractContactValues(Cursor managedCursor, int name, int number, int type, int date, int duration){
        managedCursor.moveToPrevious();
        String callName = managedCursor.getString(name);
        String phNumber = managedCursor.getString(number);
        String callType = managedCursor.getString(type);
        String callDate = managedCursor.getString(date);
        Date callDayTime = new Date(Long.valueOf(callDate));
        String callDuration = managedCursor.getString(duration) + ApiStates.BLANK + ApiStates.MINUTES;
        String dir = null;
        int dircode = Integer.parseInt(callType);
        switch (dircode) {
            case CallLog.Calls.OUTGOING_TYPE:
                dir = ApiStates.OUTGOING;
                break;

            case CallLog.Calls.INCOMING_TYPE:
                dir = ApiStates.IMCOMING;
                break;

            case CallLog.Calls.MISSED_TYPE:
                dir = ApiStates.MISSED;
                break;
        }

        PhoneLog.LogList log = plog.getInstance();

        if(callDayTime == null){
            log.setmDate(ApiStates.EMPTY_DATA);
        } else {
            log.setmDate(callDayTime.toGMTString());
        }

        if(callDuration == null){
            log.setmDuration(ApiStates.EMPTY_DATA);
        } else {
            log.setmDuration(callDuration);
        }

        if(callName == null){
            log.setmName(ApiStates.EMPTY_DATA);
        } else {
            if(callName.equals(ApiStates.NULL)){
                callName = getResources().getString(R.string.unknown);
            }
            log.setmName(callName);
        }

        if(dir == null){
            log.setmType(ApiStates.EMPTY_DATA);
        } else {
            log.setmType(dir);
        }

        if(phNumber == null){
            log.setmPhoneNumber(ApiStates.EMPTY_DATA);
        } else {
            log.setmPhoneNumber(phNumber);
        }

        return log;
    }
}
