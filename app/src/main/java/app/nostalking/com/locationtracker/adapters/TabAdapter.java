package app.nostalking.com.locationtracker.adapters;

import android.app.Activity;
import android.location.Location;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import app.nostalking.com.locationtracker.activities.MainActivity;
import app.nostalking.com.locationtracker.fragments.FragmentLogDetails;
import app.nostalking.com.locationtracker.fragments.FragmentMap;
import app.nostalking.com.locationtracker.fragments.FragmentTrackingDevices;
import app.nostalking.com.locationtracker.model.Locations;

/**
 * Created by Applaudo Dev on 4/27/2015.
 */
public class TabAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 3;
    private static Locations location = null;
    private String mTabTitles[] = new String[] { "Device", "Logs", "Map" };

    public TabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

       switch (position){
           case 0:
               return FragmentTrackingDevices.getInstance();
           case 1:
               return FragmentLogDetails.getInstance();
           case 2:
               return FragmentMap.getInstance();

       }
       return null;

    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTabTitles[position];
    }

}
