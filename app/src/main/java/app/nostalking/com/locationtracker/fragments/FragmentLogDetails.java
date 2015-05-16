package app.nostalking.com.locationtracker.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import app.nostalking.com.locationtracker.R;
import app.nostalking.com.locationtracker.adapters.deviceLogListAdapter;
import app.nostalking.com.locationtracker.model.Locations;

/**
 * Created by Applaudo Dev on 4/22/2015.
 */
public class FragmentLogDetails extends android.support.v4.app.Fragment {

    private static TextView mEmptyLogText;
    private AdView mBanner;
    private static deviceLogListAdapter mAdapter;
    private static RecyclerView mLogList;
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
            mAdapter = new deviceLogListAdapter(locationObjects, mContext, new deviceLogListAdapter.onItemClickListenr() {
                @Override
                public void onItemClick(View view, int position) {

                }
            });
            mLogList.setAdapter(mAdapter);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        createBanner();
    }

    private void initViews(View view){
        mBanner = (AdView) view.findViewById(R.id.log_ad);
        mLogList = (RecyclerView) view.findViewById(R.id.lv_logs);
        mEmptyLogText = (TextView) view.findViewById(R.id.txt_empty_log);
        mLogList.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void createBanner(){
        AdRequest adRequest = new AdRequest.Builder().build();
        mBanner.loadAd(adRequest);
    }
}
