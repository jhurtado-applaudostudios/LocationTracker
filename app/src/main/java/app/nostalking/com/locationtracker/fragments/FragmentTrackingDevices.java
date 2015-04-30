package app.nostalking.com.locationtracker.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;
import java.util.ArrayList;

import app.nostalking.com.locationtracker.R;
import app.nostalking.com.locationtracker.activities.MainActivity;
import app.nostalking.com.locationtracker.activities.TrackerApplication;
import app.nostalking.com.locationtracker.adapters.TrackingDeviceAdapter;
import app.nostalking.com.locationtracker.model.TrackingDevices;
import app.nostalking.com.locationtracker.retrofitinterface.RetrofitEndPoints;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

/**
 * Created by Applaudo Dev on 4/16/2015.
 */
public class FragmentTrackingDevices extends android.support.v4.app.Fragment {

    private Context mContext;
    private ListView mTrackingListView;
    private ProgressBar mProgressBar;
    private TrackingDevicesFragmentTransaction mCallback;

    public static FragmentTrackingDevices getInstance(){
        return new FragmentTrackingDevices();
    }

    public interface TrackingDevicesFragmentTransaction{
        public void onTransaction(int trackingId,String deviceName,boolean isClosed);
    }

    public FragmentTrackingDevices(){};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity().getApplicationContext();
        return inflater.inflate(R.layout.fragment_tracking_devices, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTrackingListView = (ListView) view.findViewById(R.id.lv_tracking_devices_list);
        mProgressBar = (ProgressBar) view.findViewById(R.id.pg_loading_devices);
        mProgressBar.setVisibility(View.VISIBLE);
        getTrackingDevices();
    }

    public void getTrackingDevices(){;
        String myId = TrackerApplication.getInstance().getDataSharedPreferences().getMyId();
       TrackerApplication.getInstance().getmApi().getTrackedDevices(myId, new Callback<TrackingDevices>() {
            @Override
            public void success(TrackingDevices trackingDevices, Response response) {
                if(trackingDevices.getmStatus().equals("200")){
                    mCallback.onTransaction(0,null,true);
                    setList(trackingDevices);
                }else{
                    Toast.makeText(mContext, "Ups something went wrong", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(mContext, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void setList(final TrackingDevices trackingDevices){
        mProgressBar.setVisibility(View.INVISIBLE);
        TrackingDevices items = trackingDevices;
        TrackingDeviceAdapter adapter = new TrackingDeviceAdapter(mContext, items);
        mTrackingListView.setAdapter(adapter);

        mTrackingListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                int trackingId = Integer.valueOf(trackingDevices.getmDevices().get(position).getSmSearchId());
                TrackerApplication.getInstance().getDataSharedPreferences().
                        storeTrackId(trackingId);
                mCallback.onTransaction(trackingId, trackingDevices.getmDevices().get(position).getmDevice(),false);

            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallback = (TrackingDevicesFragmentTransaction) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement TrackingDevicesFragmentTransaction");
        }
    }
}
