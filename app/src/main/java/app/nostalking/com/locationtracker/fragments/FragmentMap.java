package app.nostalking.com.locationtracker.fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import app.nostalking.com.locationtracker.R;
import app.nostalking.com.locationtracker.activities.TrackerApplication;
import app.nostalking.com.locationtracker.model.Locations;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class FragmentMap extends android.support.v4.app.Fragment implements OnMapReadyCallback {
    public static View mView;
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

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }

    public interface LogDetailsListener{
        public void onLogClick(String deviceName, Locations locations);
    }

    public static FragmentMap getInstance(){
        return new FragmentMap();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMap = ((SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map)).getMap();

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
        downloadData();
    }

    private static void downloadData(){
        TrackerApplication.getInstance().getmApi().getLocationById(mSearchId, new Callback<Locations>() {
            @Override
            public void success(Locations locations, Response response) {

                if(locations.getmLocations().size() > 0){

                   Collections.reverse(locations.getmLocations());
                   DrawOnMap action = new DrawOnMap();
                   action.mFullLocations = locations;
                   action.mLocations = locations.getmLocations();
                   action.execute();


                }  else {
                    Toast.makeText(mContext, "this device contains no locations", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("LOL", error.getMessage());
                Toast.makeText(mContext, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public static void enableLogs(Locations mFullLocations){
        mCallback.onLogClick(mDeviceName, mFullLocations);
    }


    public static void animateCameraTo(final double lat, final double lng, Locations mFullLocations){
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 15.0f));
        enableLogs(mFullLocations);
    }

    static class DrawOnMap extends AsyncTask<String, Void, String>{
        private ArrayList<Locations.LocationObject> mLocations;
        private Locations mFullLocations;
        private ArrayList<MarkerOptions> mMarkers = new ArrayList<>();
        private PolylineOptions mLine;
        private String mPhoneLog;
        @Override
        protected String doInBackground(String... params) {
            Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
            List<Address> addresses = null;

            for(Locations.LocationObject log : mLocations){

                int lenght = log.getmPhoneLog().length;
                for(int i = 0; i < lenght; i++){

                    if(i == lenght - 1){
                        mPhoneLog += log.getmPhoneLog()[i];
                    } else {
                        mPhoneLog += log.getmPhoneLog()[i] + "\n";
                    }

                }

                log.setPhoneLog(mPhoneLog);
                mPhoneLog = "";
            }

            mLine =
                    new PolylineOptions().add(new LatLng(mLocations.get(0).getmLatitude(),
                                    mLocations.get(0).getmLongitude()),
                            new LatLng(mLocations.get(1).getmLatitude(),
                                    mLocations.get(1).getmLongitude()),
                            new LatLng(mLocations.get(2).getmLatitude(),
                                    mLocations.get(2).getmLongitude()),
                            new LatLng(mLocations.get(3).getmLatitude(),
                                    mLocations.get(3).getmLongitude()))
                            .width(5).color(Color.RED);

            int size = mLocations.size();

            for(int i = 0; i < 10; i++){

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

                        if(title.equals("null") || title.equals("")){
                            title = "Unknown";
                        }

                        if (content.equals("null") || content.equals("")){
                            content = "Unknown";
                        }

                        mLocations.get(i).setmStreetAddress(title + " " + content);

                        if(i < 5){

                            MarkerOptions mOptions = new MarkerOptions()
                                    .position(new LatLng(latitude, longitude))
                                    .title(title)
                                    .snippet(content)
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));

                            mMarkers.add(mOptions);

                        }

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

            mMap.clear();
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

                sb.append(address.getCountryName()).append(" ");
                sb.append(address.getCountryCode()).append(" ");
            }

            return sb.toString();
        }

        private String getContent(List<Address> addresses){
            StringBuilder sb = new StringBuilder();

            if (addresses.size() > 0) {
                Address address = addresses.get(0);

                for(int i = 0; i <address.getMaxAddressLineIndex(); i++){
                    sb.append(address.getAddressLine(i)).append(" ");
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
            throw new ClassCastException(activity.toString()
                    + " must implement LogDetailListener");
        }
    }


}
