package app.nostalking.com.locationtracker.fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import app.nostalking.com.locationtracker.R;
import app.nostalking.com.locationtracker.activities.ReceptorActivity;
import app.nostalking.com.locationtracker.activities.TrackerApplication;
import app.nostalking.com.locationtracker.model.Locations;
import app.nostalking.com.locationtracker.model.PhoneLog;
import app.nostalking.com.locationtracker.model.StatusLog;
import app.nostalking.com.locationtracker.utils.ApiStates;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class FragmentMap extends android.support.v4.app.Fragment {
    private static View mView;
    private AdView mBanner;
    private static StatusLog mPartialLog;
    private static PhoneLog mFullLogs;
    private static String mSearchId;
    private static String mDeviceName;
    private static Context mContext;
    private static GoogleMap mMap;
    private static LogDetailsListener mCallback;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getActivity().getApplicationContext();

        if(mView != null){
            ViewGroup parent = (ViewGroup) mView.getParent();
            if (parent != null){
                parent.removeView(mView);
            }
        }

        try {
            mView = inflater.inflate(R.layout.fragment_map, container, false);
        } catch (InflateException e) {
            e.printStackTrace();
        }
        return mView;
    }

    public interface LogDetailsListener{
        public void onLogClick(String deviceName, PhoneLog locations, int action);
    }

    public static FragmentMap getInstance(){
        return new FragmentMap();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMap = ((SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map)).getMap();
        mBanner = (AdView) view.findViewById(R.id.map_ad);
        createBanner();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        android.support.v4.app.FragmentManager manager = getActivity().getSupportFragmentManager();
        manager.beginTransaction().remove(this).commit();
    }

    public static void updateMap(int trackingId, String deviceName){
        mDeviceName = deviceName;
        mSearchId = String.valueOf(trackingId);
        downloadLogs(String.valueOf(trackingId));
    }

    private static void downloadLogs(String trackId){

        TrackerApplication.getInstance().getmApi().getFullLogs(trackId, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {

                try {
                    mPartialLog = TrackerApplication.getInstance()
                            .getTrashCodeIgnorer()
                            .fromJsonIgnoreExtra(response.getBody().in(), StatusLog.class);

                    mFullLogs = new Gson().fromJson(mPartialLog.getmFullLog(), PhoneLog.class);
                    downloadData();

                } catch (IOException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void failure(RetrofitError error) {
            }
        });
    }

    private void createBanner(){
        AdRequest adRequest = new AdRequest.Builder().build();
        mBanner.loadAd(adRequest);
    }

    private static void downloadData(){
        TrackerApplication.getInstance().getmApi().getLocationById(mSearchId, new Callback<Response>() {
            @Override
            public void success(Response locations, Response response) {

                try {
                    Locations parsedObject = TrackerApplication.getInstance()
                            .getTrashCodeIgnorer()
                            .fromJsonIgnoreExtra(locations.getBody().in(), Locations.class);

                    if(parsedObject.getmLocations().size() > 0){

                        Collections.reverse(parsedObject.getmLocations());
                        DrawOnMap action = new DrawOnMap();
                        action.mFullLocations = mFullLogs;
                        action.mLocations = parsedObject.getmLocations();
                        action.execute();


                    }  else {
                        Toast.makeText(mContext, R.string.no_location, Toast.LENGTH_SHORT).show();
                        mCallback.onLogClick(null, null, ReceptorActivity.ACTION_CLOSE_DIALOG);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                mCallback.onLogClick(null, null, ReceptorActivity.ACTION_CLOSE_DIALOG);
                Toast.makeText(mContext, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private static void enableLogs(PhoneLog mFullLocations){
        mCallback.onLogClick(mDeviceName, mFullLocations, ReceptorActivity.ACTION_EXECUTE);
    }


    private static void animateCameraTo(final double lat, final double lng, PhoneLog mFullLocations){
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 15.0f));
        enableLogs(mFullLocations);
    }

    static class DrawOnMap extends AsyncTask<String, Void, String>{
        private ArrayList<Locations.LocationObject> mLocations;
        private final ArrayList<LatLng> mPoints = new ArrayList<>();
        private PhoneLog mFullLocations;
        private final ArrayList<MarkerOptions> mMarkers = new ArrayList<>();
        private PolylineOptions mLine;
        @Override
        protected String doInBackground(String... params) {
            Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
            List<Address> addresses = null;

            PolylineOptions options = new PolylineOptions();

            for(Locations.LocationObject loc: mLocations){
                mPoints.add(new LatLng(loc.getmLatitude(), loc.getmLongitude()));
            }

            for(LatLng point : mPoints){
                options.add(point);
            }

            options.width(5).color(Color.RED);

            mLine = options;


            int size = mLocations.size();

            for(int i = 0; i < 20; i++){

                if(i < size){
                    if(addresses != null){
                        addresses.clear();
                    }

                    double latitude = mLocations.get(i).getmLatitude();
                    double longitude = mLocations.get(i).getmLongitude();

                    try {
                        addresses = geocoder.getFromLocation(latitude, longitude, 1);

                        String title = getTitle(addresses);
                        String content = getContent(addresses);

                        if(title.equals(ApiStates.NULL) || title.equals(ApiStates.EMPTY)){
                            title = mContext.getResources().getString(R.string.unknown);
                        }

                        if (content.equals(ApiStates.NULL) || content.equals(ApiStates.EMPTY)){
                            content = mContext.getResources().getString(R.string.unknown);
                        }

                        mLocations.get(i).setmStreetAddress(title + " " + content);

                         MarkerOptions mOptions = new MarkerOptions()
                                    .position(new LatLng(latitude, longitude))
                                    .title(title)
                                    .snippet(content)
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));

                            mMarkers.add(mOptions);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            mMap.addPolyline(mLine);

            for(MarkerOptions options : mMarkers){
                mMap.addMarker(options);
            }


            animateCameraTo(mLocations.get(0).getmLatitude(), mLocations.get(0).getmLongitude(), mFullLocations);

        }

        private String getTitle(List<Address> addresses){
            StringBuilder sb = new StringBuilder();

            if (addresses.size() > 0) {
                Address address = addresses.get(0);

                sb.append(address.getCountryName()).append(ApiStates.BLANK);
                sb.append(address.getCountryCode()).append(ApiStates.BLANK);
            }
            return sb.toString();
        }

        private String getContent(List<Address> addresses){
            StringBuilder sb = new StringBuilder();

            if (addresses.size() > 0) {
                Address address = addresses.get(0);

                for(int i = 0; i <address.getMaxAddressLineIndex(); i++){
                    sb.append(address.getAddressLine(i)).append(ApiStates.BLANK);
                }
            }

            return sb.toString();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallback = (LogDetailsListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString());
        }
    }

}
