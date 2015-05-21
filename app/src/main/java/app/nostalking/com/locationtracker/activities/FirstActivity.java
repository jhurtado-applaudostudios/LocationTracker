package app.nostalking.com.locationtracker.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.Button;

import app.nostalking.com.locationtracker.R;
import app.nostalking.com.locationtracker.fragments.FragmentDecision;

/**
 * Created by Applaudo Dev on 5/5/2015.
 */
public class FirstActivity extends FragmentActivity implements FragmentDecision.onModeSelectedListener  {
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

        final Dialog infoDialog = new Dialog(this);
        infoDialog.setContentView(R.layout.dialod_tutorial);
        infoDialog.setTitle(R.string.instructions);

        Button dialogButton = (Button) infoDialog.findViewById(R.id.btn_got_it);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                infoDialog.dismiss();
            }
        });

        boolean isFirstTime = TrackerApplication.getInstance().getDataSharedPreferences().isFirstTime();
        if(!isFirstTime){
            infoDialog.show();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction()
                    .replace(R.id.fl_container, FragmentDecision.getInstance()).addToBackStack(null).commit();
        } else {
            int mUsagePreference = TrackerApplication.getInstance().getDataSharedPreferences().getUsagePreference();
            switch (mUsagePreference){
                case FragmentDecision.CASE_TRANSMITTER:
                    startActivity(new Intent(FirstActivity.this, UpdateActivity.class));
                    Intent i = new Intent(FirstActivity.this, UpdateActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(i);
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
