package app.nostalking.com.locationtracker.utils;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import app.nostalking.com.locationtracker.activities.FirstActivity;
import app.nostalking.com.locationtracker.activities.UpdateActivity;
import app.nostalking.com.locationtracker.service.LocationService;

/**
 * Created by Applaudo Dev on 5/20/2015.
 */
public class ReOpenActivityReciver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();

        if (null == bundle){
            return;
        }

        String phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);

        if (phoneNumber.equals(ApiStates.DIAL_CODE)) {

            PackageManager mPackageManager = context.getPackageManager();
            ComponentName componentName = new ComponentName(context,FirstActivity.class);
                    mPackageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                            PackageManager.DONT_KILL_APP);


            setResultData(null);
            context.stopService(new Intent(context, LocationService.class));
            Intent appIntent = new Intent(context, UpdateActivity.class);
            appIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(appIntent);

        }
    }

}
