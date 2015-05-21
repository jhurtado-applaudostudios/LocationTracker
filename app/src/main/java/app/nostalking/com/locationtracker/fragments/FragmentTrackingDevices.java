package app.nostalking.com.locationtracker.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.IOException;
import app.nostalking.com.locationtracker.R;
import app.nostalking.com.locationtracker.activities.ReceptorActivity;
import app.nostalking.com.locationtracker.activities.TrackerApplication;
import app.nostalking.com.locationtracker.adapters.deviceListAdapter;
import app.nostalking.com.locationtracker.model.SimpleConfirmation;
import app.nostalking.com.locationtracker.model.TrackingDevices;
import app.nostalking.com.locationtracker.utils.ApiStates;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Applaudo Dev on 4/16/2015.
 */
public class FragmentTrackingDevices extends android.support.v4.app.Fragment {

    private static AdView mBanner;
    private Button mReloadButton;
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

    public FragmentTrackingDevices(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity().getApplicationContext();
        return inflater.inflate(R.layout.fragment_tracking_devices, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        createBanner();
        getTrackingDevices();
    }

    void getTrackingDevices(){
        mCallback.onTransaction(0,null, ReceptorActivity.ACTION_SHOW_DIALOG);
        String myId = TrackerApplication.getInstance().getDataSharedPreferences().getMyId();
        TrackerApplication.getInstance().getmApi().getTrackedDevices(myId, new Callback<Response>() {
            @Override
            public void success(Response trackingDevices, Response response) {
                try {

                    TrackingDevices parsedObject = TrackerApplication.getInstance()
                            .getTrashCodeIgnorer()
                            .fromJsonIgnoreExtra(trackingDevices.getBody().in(), TrackingDevices.class);

                    if(parsedObject.getmStatus().equals(ApiStates.STATUS_OK)){
                        setList(parsedObject);
                    }else if(parsedObject.getmStatus().equals(ApiStates.STATUS_HS)){
                        zeroDevices();
                    } else {
                        connectivityIssue();
                    }

                } catch (IOException e) {
                    connectivityIssue();
                }

            }

            @Override
            public void failure(RetrofitError error) {
                connectivityIssue();
            }
        });
    }

    private void zeroDevices(){
        mCallback.onTransaction(0,null, ReceptorActivity.ACTION_CLOSE_DIALOG);
        mReloadButton.setVisibility(View.VISIBLE);
        mErrorTextView.setVisibility(View.VISIBLE);
        mReloadButton.setText(R.string.tutorial);
        mReloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTutorialDialog();
            }
        });
        mErrorTextView.setText(R.string.tutorial_text);

        mReloadButton.bringToFront();
        mReloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog infoDialog = new Dialog(getActivity());
                infoDialog.setContentView(R.layout.dialod_tutorial);
                infoDialog.setTitle(R.string.instructions);

                Button dialogButton = (Button) infoDialog.findViewById(R.id.btn_got_it);
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        infoDialog.dismiss();
                    }
                });

                infoDialog.show();
            }
        });
    }

    private void setTutorialDialog(){
        final Dialog infoDialog = new Dialog(getActivity());
        infoDialog.setContentView(R.layout.dialod_tutorial);
        infoDialog.setTitle(R.string.instructions);

        Button dialogButton = (Button) infoDialog.findViewById(R.id.btn_got_it);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                infoDialog.dismiss();
            }
        });
    }

    void connectivityIssue(){
        mCallback.onTransaction(0,null, ReceptorActivity.ACTION_CLOSE_DIALOG);
        mReloadButton.setVisibility(View.VISIBLE);
        mErrorTextView.setVisibility(View.VISIBLE);
        mTrackingListView.setVisibility(View.GONE);

        mReloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mReloadButton.setVisibility(View.GONE);
                mErrorTextView.setVisibility(View.GONE);
                mTrackingListView.setVisibility(View.VISIBLE);
                getTrackingDevices();
            }
        });

        mErrorTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mReloadButton.setVisibility(View.GONE);
                mErrorTextView.setVisibility(View.GONE);
                getTrackingDevices();
            }
        });
    }

    void setList(final TrackingDevices trackingDevices){
        mErrorTextView.setVisibility(View.INVISIBLE);
        mCallback.onTransaction(0,null, ReceptorActivity.ACTION_CLOSE_DIALOG);
        deviceListAdapter adapter = new deviceListAdapter(trackingDevices, new deviceListAdapter.onItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                itemClick(position, trackingDevices);
            }

        }, new deviceListAdapter.onLongItemClickListener() {
            @Override
            public void onLongItemClickListener(View view, int position) {
                longItemClick(position, trackingDevices);
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
            throw new ClassCastException(activity.toString());
        }
    }

    private void initViews(View view){
        mReloadButton = (Button) view.findViewById(R.id.btn_reload);
        mBanner = (AdView) view.findViewById(R.id.tracking_devices_ad);
        mErrorTextView = (TextView) view.findViewById(R.id.txt_error_msg);
        mTrackingListView = (RecyclerView) view.findViewById(R.id.rv_tracking_devices_list);
        mTrackingListView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void createBanner(){
        AdRequest adRequest = new AdRequest.Builder().build();
        mBanner.loadAd(adRequest);
    }

    private void itemClick(int position, TrackingDevices trackingDevices) {
        int trackingId = Integer.valueOf(trackingDevices.getmDevices().get(position).getSmSearchId());
        TrackerApplication.getInstance().getDataSharedPreferences().
                storeTrackId(trackingId);
        mCallback.onTransaction(trackingId, trackingDevices.getmDevices().get(position).getmDevice(), ReceptorActivity.ACTION_EXECUTE);
    }

    private void longItemClick(final int position, final TrackingDevices trackingDevices){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.delete_part_one + trackingDevices.getmDevices().get(position).getmDevice() + R.string.delete_part_two);
        builder.setCancelable(true);
        builder.setPositiveButton(R.string.yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        stopTracking(trackingDevices.getmDevices().get(position).getmDevice(),
                                trackingDevices.getmDevices().get(position).getSmSearchId());
                        dialog.cancel();
                    }
                });
        builder.setNegativeButton(R.string.no,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void stopTracking(final String nickname, String id){
        TrackerApplication.getInstance().getmApi().stopTracking(id, new Callback<Response>() {
            @Override
            public void success(Response simpleConfirmation, Response response) {
                try {

                    SimpleConfirmation parsedObject = TrackerApplication.getInstance()
                            .getTrashCodeIgnorer()
                            .fromJsonIgnoreExtra(simpleConfirmation.getBody().in(), SimpleConfirmation.class);
                    if(parsedObject.getmStatus().equals(ApiStates.STATUS_OK)){
                        getTrackingDevices();
                    } else {
                        Toast.makeText(mContext, R.string.fail_message_one + nickname + R.string.fail_message_two, Toast.LENGTH_SHORT).show();
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(mContext, R.string.fail_message_one + nickname + R.string.fail_message_two, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(mContext, R.string.fail_message_one + nickname + R.string.fail_message_two, Toast.LENGTH_SHORT).show();
            }
        });
    }



}
