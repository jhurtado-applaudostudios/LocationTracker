package app.nostalking.com.locationtracker.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.astuetz.PagerSlidingTabStrip;

import app.nostalking.com.locationtracker.adapters.TabAdapter;
import app.nostalking.com.locationtracker.fragments.FragmentLogDetails;
import app.nostalking.com.locationtracker.fragments.FragmentMap;
import app.nostalking.com.locationtracker.fragments.FragmentTrackingDevices;
import app.nostalking.com.locationtracker.R;
import app.nostalking.com.locationtracker.model.Locations;


public class MainActivity extends ActionBarActivity
        implements FragmentTrackingDevices.TrackingDevicesFragmentTransaction,
        FragmentMap.LogDetailsListener{
    private Dialog mDialog;
    private Dialog mPrivacyPolicy;
    private ViewPager mViewPager;
    private PagerSlidingTabStrip mSlidingStrip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#333333")));
        actionBar.setTitle(Html.fromHtml("<font color='#ffffff'>IM'stalkr </font>"));

        mDialog = new Dialog(MainActivity.this);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.dialog_row);
        mDialog.show();

        mPrivacyPolicy = new Dialog(MainActivity.this);
        mPrivacyPolicy.setContentView(R.layout.privacy_policy);
        mPrivacyPolicy.setTitle("Privacy policy");
        Button okButton = (Button) mPrivacyPolicy.findViewById(R.id.btn_ok);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPrivacyPolicy.dismiss();
            }
        });

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setAdapter(new TabAdapter(getSupportFragmentManager()));

        mSlidingStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        mSlidingStrip.setViewPager(mViewPager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.action_log_out:
                TrackerApplication.getInstance().getDataSharedPreferences().stayLogged(false);
                startActivity(new Intent(MainActivity.this, LogInActivity.class));
                break;
            case R.id.action_share:
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, "IM'stalker is a very accurate cell phone" +
                        " tracker service to monitor your family using GPS tracking technology." +
                        " A safe way to track the location of your kids on the map 24 hours per day." +
                        " Peace of mind for you when you are not close of your loved ones. All they need is an Android phone with the App installed." +
                        " \n[link to appstore]");
                startActivity(Intent.createChooser(shareIntent, "share to.."));
                break;
            case R.id.action_privacy_policy:
                mPrivacyPolicy.show();
                break;
        }
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }

    @Override
    public void onTransaction(int trackingId,String deviceName,boolean isClose) {
        if(isClose){
            mDialog.dismiss();
        } else {
            mDialog.show();
            FragmentMap.updateMap(trackingId, deviceName);
            mViewPager.setCurrentItem(2);
        }
    }

    @Override
    public void onLogClick(String deviceName, Locations locationObjects) {
         FragmentLogDetails.updateList(locationObjects);
         mDialog.dismiss();
    }
}
