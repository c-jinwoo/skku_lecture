package com.swe.buddybud.account;

import com.swe.buddybud.user.UserApiData;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AccountApiService {
    @GET("/account/{userId}/post")
    Call<List<PostData>> getUserPosts(@Path("userId") String userId); // Update to List<PostData>

    @GET("/account/{userId}/scrap")
    Call<List<ScrapData>> getUserScraps(@Path("userId") String userId); // Update to List<ScrapData>

    @PUT("/account/{userId}")
    Call<UpdateResponse> updateUserAccount(@Path("userId") String userId, @Body UserAccountUpdate userAccountUpdate);

}
