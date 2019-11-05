package com.nibou.niboucustomer.api;


import com.nibou.niboucustomer.models.*;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.HashMap;
import java.util.List;


public interface ApiEndPoint {

    @Headers("Content-Type: application/json")
    @GET("_api/past_events?")
    Call<ListResponseModel> getPastEventsNetworkCall(@Query("limit") int limit, @Query("n") int pageNumber);

    @Headers("Content-Type: application/json")
    @GET("_api/pages?")
    Call<ListResponseModel> getTermAndPrivacyNetworkCall(@Query("url") String type);

    @Headers("Content-Type: application/json")
    @GET("_api/brands")
    Call<ListResponseModel> getBrandNetworkCall(@Query("limit") int limit, @Query("n") int pageNumber);

    @Headers("Content-Type: application/json")
    @GET("_api/models")
    Call<ListResponseModel> getModelNetworkCall(@Query("limit") int limit, @Query("n") int pageNumber, @Query("brand") int brand);

    @Headers("Content-Type: application/json")
    @GET("_api/colors")
    Call<ListResponseModel> getColorNetworkCall(@Header("token") String token, @Query("limit") int limit, @Query("n") int pageNumber);

    @Headers("Content-Type: application/json")
    @POST("_api/user_registration")
    Call<ErrorResponseModel> registerUserNetworkCall(@Body HashMap<String, Object> map);

    @Headers("Content-Type: application/json")
    @GET("_api/users")
    Call<ListResponseModel> getUserDetailsNetworkCall(@Header("token") String token);

    @Headers("Content-Type: application/json")
    @PUT("_api/users/{id}")
    Call<ListResponseModel> updateUserDetailNetworkCall(@Path("id") String userId, @Header("token") String token, @Body HashMap<String, Object> map);

    @Headers("Content-Type: application/json")
    @GET("_api/cars")
    Call<ListResponseModel> getCarsDetailsNetworkCall(@Header("token") String token);

    @Headers("Content-Type: application/json")
    @PUT("_api/cars/{id}")
    Call<ErrorResponseModel> updateCarDetailNetworkCall(@Path("id") String userId, @Header("token") String token, @Body HashMap<String, Object> map);

    @Headers("Content-Type: application/json")
    @POST("_api/cars")
    Call<ErrorResponseModel> addCarDetailNetworkCall(@Header("token") String token, @Body HashMap<String, Object> map);

    @Headers("Content-Type: application/json")
    @DELETE("_api/cars/{id}")
    Call<ListResponseModel> deleteCarNetworkCall(@Path("id") String userId, @Header("token") String token);


    @Headers("Content-Type: application/json")
    @PUT("_api/user_docs/{id}")
    Call<ListResponseModel> updateUserDocumentNetworkCall(@Path("id") String userId, @Header("token") String token, @Body HashMap<String, Object> map);


    @Headers("Content-Type: application/json")
    @POST("_api/user_auth")
    Call<ListResponseModel> loginNetworkCall(@Body HashMap<String, Object> map);

    @Headers("Content-Type: application/json")
    @POST("_api/user_remind")
    Call<ErrorResponseModel> forgetPasswordNetworkCall(@Body HashMap<String, Object> map);

    //	1 - upcoming, 2 - closed
    @Headers("Content-Type: application/json")
    @GET("_api/events?")
    Call<ListResponseModel> getEventNetworkCall(@Header("token") String token, @Query("limit") int limit, @Query("n") int pageNumber, @Query("type") int type);


    @Headers("Content-Type: application/json")
    @GET("_api/events?")
    Call<ListResponseModel> getEventDetailNetworkCall(@Header("token") String token, @Query("id") String id);


    @Headers("Content-Type: application/json")
    @GET("_api/checkpoints")
    Call<ListResponseModel> getCheckpointNetworkCall(@Header("token") String token, @Query("limit") int limit, @Query("n") int pageNumber, @Query("event") String event);


    @Headers("Content-Type: application/json")
    @POST("_api/user_password")
    Call<ErrorResponseModel> changePasswordNetworkCall(@Header("token") String token, @Body HashMap<String, Object> map);

    @Headers("Content-Type: application/json")
    @PUT("_api/users/{id}")
    Call<ErrorResponseModel> updateProfileNetworkCall(@Header("token") String token, @Path("id") String userId, @Body HashMap<String, Object> map);

    @Headers("Content-Type: application/json")
    @POST("_api/reservations")
    Call<ErrorResponseModel> reserveSpotNetworkCall(@Header("token") String token, @Body HashMap<String, Object> map);

    @HTTP(method = "DELETE", path = "_api/reservations", hasBody = true)
    Call<ErrorResponseModel> deleteReserveSpotNetworkCall(@Header("token") String token, @Body HashMap<String, Object> map);

    @Headers("Content-Type: application/json")
    @POST("_api/reservation_checks")
    Call<ErrorResponseModel> checkInNetworkCall(@Header("token") String token, @Body HashMap<String, Object> map);

    //	1 - active, 2 - suspended
    @Headers("Content-Type: application/json")
    @GET("_api/promotions")
    Call<ListResponseModel> getPromotionNetworkCall(@Header("token") String token, @Query("limit") int limit, @Query("n") int pageNumber, @Query("type") int type);

    @Headers("Content-Type: application/json")
    @POST("_api/promotions_code")
    Call<ErrorResponseModel> redeemNetworkCall(@Header("token") String token, @Body HashMap<String, Object> map);
}