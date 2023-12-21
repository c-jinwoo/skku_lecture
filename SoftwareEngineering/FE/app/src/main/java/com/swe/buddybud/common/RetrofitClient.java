package com.swe.buddybud.common;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    public static <T> T getService(Class<T> serviceClass) {
        String BASE_URL = "";

        // 통신할 URL 세팅
        if(ServerConfig.isDev){
            BASE_URL = ServerConfig.devServerUrl + ":" + ServerConfig.devServerPort + "/";
        }
        else{
            BASE_URL = ServerConfig.realServerUrl + ":" + ServerConfig.realServerPort + "/";
        }

        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();

        return retrofit.create(serviceClass);
    }
}