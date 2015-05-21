package app.nostalking.com.locationtracker.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import app.nostalking.com.locationtracker.service.LocationService;

public class BootEventReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            Intent service = new Intent(context, LocationService.class);
            service.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startService(service);
        }
    }
}
