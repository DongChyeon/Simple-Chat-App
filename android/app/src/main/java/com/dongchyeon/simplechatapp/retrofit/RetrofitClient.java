package com.dongchyeon.simplechatapp.retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static RetrofitClient instance = null;
    private static ApiService apiService;
    // 에뮬레이터 기준 10.0.2.2이며 실제 스마트폰에서 실험시 자신의 ip 주소로 해야함
    private static String baseUrl = "http://10.0.2.2:80";

    private RetrofitClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);
    }

    public static RetrofitClient getInstance() {
        if (instance == null) {
            instance = new RetrofitClient();
        }
        return instance;
    }

    public static ApiService getApiService() {
        return apiService;
    }
}
