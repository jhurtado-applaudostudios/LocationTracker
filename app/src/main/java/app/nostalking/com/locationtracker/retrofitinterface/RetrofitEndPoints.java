package app.nostalking.com.locationtracker.retrofitinterface;

import org.json.JSONObject;

import app.nostalking.com.locationtracker.model.AccountExistence;
import app.nostalking.com.locationtracker.model.Locations;
import app.nostalking.com.locationtracker.model.ReportID;
import app.nostalking.com.locationtracker.model.SimpleConfirmation;
import app.nostalking.com.locationtracker.model.TrackingDevices;
import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by Applaudo Dev on 4/10/2015.
 */
public interface RetrofitEndPoints {

    @FormUrlEncoded
    @POST("/register_users.php")
       public void registerUser(@Field("username") String username, @Field("email") String email, @Field("password") String password, Callback<Response> response);

    @FormUrlEncoded
    @POST("/check_user_existence.php")
       public void ceckUserExistence(@Field("username") String username, @Field("password") String password, Callback<Response> response);

    @FormUrlEncoded
    @POST("/get_tracked_devices.php")
       public void getTrackedDevices(@Field("id_stalker") String stalkerId, Callback<Response> response);

    @FormUrlEncoded
    @POST("/get_location_by_id.php")
       public void getLocationById(@Field("unique_id") String uniqueId, Callback<Response> response);

    @FormUrlEncoded
    @POST("/register_tracked_device.php")
       public void registerMyDevice(@Field("stalker_id") String stalkerId, @Field("device_name") String deviceName, Callback<Response> response);

    @FormUrlEncoded
    @POST("/store_location_updates.php")
       public void saveLocationInServer(@Field("latitude") double latitude, @Field("longitude") double longitude, @Field("device_id") String postingId
       , @Field("phone_log") String phoneLog, Callback<Response> response);

    @FormUrlEncoded
    @POST("/stop_tracking.php")
    public void stopTracking(@Field("id") String is, Callback<Response> response);
}
