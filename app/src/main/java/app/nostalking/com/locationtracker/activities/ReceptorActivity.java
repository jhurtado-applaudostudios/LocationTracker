package app.nostalking.com.locationtracker.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
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
import app.nostalking.com.locationtracker.model.PhoneLog;


public class ReceptorActivity extends ActionBarActivity
        implements FragmentTrackingDevices.TrackingDevicesFragmentTransaction,
        FragmentMap.LogDetailsListener{
    private Dialog mDialog;
    private Dialog mPrivacyPolicy;
    private ViewPager mViewPager;
    public static final int ACTION_SHOW_DIALOG =3;
    public static final int ACTION_CLOSE_DIALOG =1;
    public static final int ACTION_EXECUTE =2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        customizeActionBar();
        setDialogs();

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setAdapter(new TabAdapter(getSupportFragmentManager()));

        PagerSlidingTabStrip mSlidingStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        mSlidingStrip.setViewPager(mViewPager);

    }

    private void setDialogs(){
        mDialog = new Dialog(ReceptorActivity.this);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.dialog_row);
        mDialog.show();

        mPrivacyPolicy = new Dialog(ReceptorActivity.this);
        mPrivacyPolicy.setContentView(R.layout.privacy_policy);
        mPrivacyPolicy.setTitle(R.string.privacy_policy);
        Button okButton = (Button) mPrivacyPolicy.findViewById(R.id.btn_ok);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPrivacyPolicy.dismiss();
            }
        });
    }

    private void customizeActionBar(){
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#339595")));
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.ic_action_bar, null);
        actionBar.setCustomView(view, new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
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
                startActivity(new Intent(ReceptorActivity.this, LogInActivity.class));
                break;
            case R.id.action_share:
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, R.string.share_text);
                startActivity(Intent.createChooser(shareIntent, getResources().getString(R.string.share_text_title)));
                break;
            case R.id.action_privacy_policy:
                mPrivacyPolicy.show();
                break;
        }
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }

    @Override
    public void onTransaction(int trackingId,String deviceName,int action) {

        switch (action){
            case ACTION_CLOSE_DIALOG:
                mDialog.dismiss();
                break;
            case ACTION_EXECUTE:
                mDialog.show();
                FragmentMap.updateMap(trackingId, deviceName);
                mViewPager.setCurrentItem(2);
                break;
            case ACTION_SHOW_DIALOG:
                if(mViewPager.getCurrentItem() != 1){
                    mDialog.show();
                }
                break;
        }

    }

    @Override
    public void onLogClick(String deviceName, PhoneLog locationObjects, int action) {
        switch (action){
            case ACTION_CLOSE_DIALOG:
                mDialog.dismiss();
                break;
            case ACTION_EXECUTE:
                FragmentLogDetails.updateList(locationObjects);
                mDialog.dismiss();
                break;
        }

    }
}
