package app.nostalking.com.locationtracker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import app.nostalking.com.locationtracker.R;
import app.nostalking.com.locationtracker.fragments.FragmentDecision;

/**
 * Created by Applaudo Dev on 5/5/2015.
 */
public class FirstActivity extends FragmentActivity implements FragmentDecision.onModeSelectedListener  {
    private boolean isFirstTime;
    private int mUsagePreference;

    @Override
    public void onSelection(int selection) {
        switch (selection){
            case FragmentDecision.CASE_RECEPTOR:
                TrackerApplication.getInstance().getDataSharedPreferences().saveUsagePreference(FragmentDecision.CASE_RECEPTOR);
                startActivity(new Intent(FirstActivity.this, LogInActivity.class));
                break;
            case FragmentDecision.CASE_TRANSMITTER:
                TrackerApplication.getInstance().getDataSharedPreferences().saveUsagePreference(FragmentDecision.CASE_TRANSMITTER);
                startActivity(new Intent(FirstActivity.this, UpdateActivity.class));
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decision);

        isFirstTime = TrackerApplication.getInstance().getDataSharedPreferences().isFirstTime();
        if(!isFirstTime){
            TrackerApplication.getInstance().getDataSharedPreferences().saveFirstTime(true);
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction()
                    .replace(R.id.fl_container, FragmentDecision.getInstance()).addToBackStack(null).commit();
        } else {
            mUsagePreference = TrackerApplication.getInstance().getDataSharedPreferences().getUsagePreference();
            switch (mUsagePreference){
                case FragmentDecision.CASE_TRANSMITTER:
                    startActivity(new Intent(FirstActivity.this, UpdateActivity.class));
                    break;
                case FragmentDecision.CASE_RECEPTOR:
                    startActivity(new Intent(FirstActivity.this, LogInActivity.class));
                    break;
                default:
                    FragmentManager manager = getSupportFragmentManager();
                    manager.beginTransaction()
                            .replace(R.id.fl_container, FragmentDecision.getInstance()).addToBackStack(null).commit();
                    break;

            }
        }


    }
}
