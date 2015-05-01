package app.nostalking.com.locationtracker.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import app.nostalking.com.locationtracker.R;
import app.nostalking.com.locationtracker.activities.MainActivity;
import app.nostalking.com.locationtracker.activities.TrackerApplication;
import app.nostalking.com.locationtracker.adapters.deviceListAdapter;
import app.nostalking.com.locationtracker.model.TrackingDevices;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Applaudo Dev on 4/16/2015.
 */
public class FragmentTrackingDevices extends android.support.v4.app.Fragment {

    private TextView mErrorTextView;
    private Context mContext;
    private RecyclerView mTrackingListView;
    private TrackingDevicesFragmentTransaction mCallback;

    public static FragmentTrackingDevices getInstance(){
        return new FragmentTrackingDevices();
    }

    public interface TrackingDevicesFragmentTransaction{
        public void onTransaction(int trackingId,String deviceName,int action);
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
        mErrorTextView = (TextView) view.findViewById(R.id.txt_error_msg);
        mTrackingListView = (RecyclerView) view.findViewById(R.id.rv_tracking_devices_list);
        mTrackingListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        getTrackingDevices();
    }

    public void getTrackingDevices(){;
        mCallback.onTransaction(0,null,MainActivity.ACTION_SHOW_DIALOG);
        String myId = TrackerApplication.getInstance().getDataSharedPreferences().getMyId();
        TrackerApplication.getInstance().getmApi().getTrackedDevices(myId, new Callback<TrackingDevices>() {
            @Override
            public void success(TrackingDevices trackingDevices, Response response) {
                if(trackingDevices.getmStatus().equals("200")){
                    setList(trackingDevices);
                }else{
                    connectionError();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                connectionError();
            }
        });
    }

    public void connectionError(){
        mCallback.onTransaction(0,null, MainActivity.ACTION_CLOSE_DIALOG);
        mErrorTextView.setVisibility(View.VISIBLE);
        mErrorTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mErrorTextView.setVisibility(View.GONE);
                getTrackingDevices();
            }
        });
    }

    public void setList(final TrackingDevices trackingDevices){
        mCallback.onTransaction(0,null,MainActivity.ACTION_CLOSE_DIALOG);
        TrackingDevices items = trackingDevices;
        deviceListAdapter adapter = new deviceListAdapter(items, new deviceListAdapter.onItemClickListenr() {
            @Override
            public void onItemClick(View view, int position) {
                int trackingId = Integer.valueOf(trackingDevices.getmDevices().get(position).getSmSearchId());
                TrackerApplication.getInstance().getDataSharedPreferences().
                storeTrackId(trackingId);
                mCallback.onTransaction(trackingId, trackingDevices.getmDevices().get(position).getmDevice(),MainActivity.ACTION_EXECUTE);
            }
        });

        mTrackingListView.setAdapter(adapter);


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
