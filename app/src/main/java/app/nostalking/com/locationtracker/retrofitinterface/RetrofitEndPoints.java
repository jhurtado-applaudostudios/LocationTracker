package app.nostalking.com.locationtracker.retrofitinterface;

import app.nostalking.com.locationtracker.model.AccountExistence;
import app.nostalking.com.locationtracker.model.Locations;
import app.nostalking.com.locationtracker.model.SimpleConfirmation;
import app.nostalking.com.locationtracker.model.TrackingDevices;
import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by Applaudo Dev on 4/10/2015.
 */
public interface RetrofitEndPoints {

    @FormUrlEncoded
    @POST("/register_users.php")
       public void registerUser(@Field("username") String username, @Field("email") String email, @Field("password") String password, Callback<SimpleConfirmation> responce);

    @FormUrlEncoded
    @POST("/check_user_existence.php")
       public void ceckUserExistence(@Field("username") String username, @Field("password") String password, Callback<AccountExistence> responce);

    @FormUrlEncoded
    @POST("/get_tracked_devices.php")
       public void getTrackedDevices(@Field("id_stalker") String stalkerId, Callback<TrackingDevices> responce);

    @FormUrlEncoded
    @POST("/get_location_by_id.php")
       public void getLocationById(@Field("unique_id") String uniqueId, Callback<Locations> responce);
}
