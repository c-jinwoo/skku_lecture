package com.swe.buddybud.willow;

import com.swe.buddybud.user.UserApiData;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface WillowApiService {
    @GET("/willow/{userId}")
    Call<List<WillowApiData>> getMyWillows(@Path("userId") Integer userId);

    @GET("/willow/{userId}/sent")
    Call<List<WillowApiData>> sentWillow(@Path("userId") Integer userId);

    @GET("/willow/{userId}/received")
    Call<List<WillowApiData>> receivedWillow(@Path("userId") Integer userId);

    @GET("/willow/getAllChat")
    Call<List<WillowApiData>> getAllChat(@Query("sender_no") Integer sender_no, @Query("receiver_no") Integer receiver_no);

    @POST("/willow")
    Call<WillowApiData> sendWillow(@Body RequestBody requestBody);

    @POST("/willow/sendChat")
    Call<WillowApiData> sendChat(@Body RequestBody requestBody);

    @POST("/willow/delete")
    Call<WillowApiData> deleteWillow(@Body RequestBody requestBody);

    @POST("/willow/accept")
    Call<WillowApiData> acceptWillow(@Body RequestBody requestBody);

}
