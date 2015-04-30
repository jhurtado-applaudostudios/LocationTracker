package app.nostalking.com.locationtracker.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import app.nostalking.com.locationtracker.R;
import app.nostalking.com.locationtracker.activities.MainActivity;
import app.nostalking.com.locationtracker.adapters.DeviceLogAdapter;
import app.nostalking.com.locationtracker.model.Locations;

/**
 * Created by Applaudo Dev on 4/22/2015.
 */
public class FragmentLogDetails extends android.support.v4.app.Fragment {

    private static TextView mEmptyLogText;
    private static DeviceLogAdapter mAdapter;
    private static ListView mLogList;
    private static Context mContext;

    public static FragmentLogDetails getInstance(){
        FragmentLogDetails fragment = new FragmentLogDetails();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getActivity().getApplicationContext();
        return inflater.inflate(R.layout.fragment_log_detail, container, false);
    }


    public static void updateList(Locations locationObjects){
            mEmptyLogText.setVisibility(View.GONE);
            mAdapter = new DeviceLogAdapter(mContext, locationObjects);
            mLogList.setAdapter(mAdapter);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mLogList = (ListView) view.findViewById(R.id.lv_logs);
        mEmptyLogText = (TextView) view.findViewById(R.id.txt_empty_log);
    }
}
