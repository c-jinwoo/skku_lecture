package com.swe.buddybud.user;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface UserApiService {

    // 로그인 확인
    @POST("/user/login")
    Call<UserApiData> userLogin(@Body RequestBody requestBody);

    // 이메일 중복 체크
    @GET("/user/emailDuplCheck")
    Call<UserApiData> userEmailDuplCheck(@Query("email") String email);

    // 아이디 중복 체크
    @GET("/user/idDuplCheck")
    Call<UserApiData> userIdDuplCheck(@Query("id") String id);

    // 회원가입 성공 여부
    @POST("/user/insertUserInfo")
    Call<UserApiData> insertUserInfo(@Body RequestBody requestBody);

    // 회원정보수정 성공 여부
    @POST("/user/updateUserInfo")
    Call<UserApiData> updateUserInfo(@Body RequestBody requestBody);
}
