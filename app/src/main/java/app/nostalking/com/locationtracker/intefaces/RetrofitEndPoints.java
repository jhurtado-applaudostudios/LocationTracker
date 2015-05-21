package app.nostalking.com.locationtracker.intefaces;

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
    @POST("/get_devices.php")
       public void getTrackedDevices(@Field("id_stalker") String stalkerId, Callback<Response> response);

    @FormUrlEncoded
    @POST("/get_information.php")
       public void getLocationById(@Field("unique_id") String uniqueId, Callback<Response> response);

    @FormUrlEncoded
    @POST("/register_device.php")
       public void registerMyDevice(@Field("stalker_id") String stalkerId, @Field("device_name") String deviceName, Callback<Response> response);

    @FormUrlEncoded
    @POST("/store_updates.php")
       public void saveLocationInServer(@Field("latitude") double latitude, @Field("longitude") double longitude, @Field("device_id") String postingId,
       Callback<Response> response);

    @FormUrlEncoded
    @POST("/delete_user.php")
    public void stopTracking(@Field("id") String is, Callback<Response> response);

    @FormUrlEncoded
    @POST("/replace_phone_log.php")
    public void updateLogs(@Field("phone_log") String phoneLog, @Field("id") String id, Callback<Response> response);

    @FormUrlEncoded
    @POST("/get_log.php")
    public void getFullLogs(@Field("stalker_id") String stalkerId, Callback<Response> response);
}
