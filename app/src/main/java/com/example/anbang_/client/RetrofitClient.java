package com.example.anbang_.client;

import com.example.anbang_.service.PropertyRetrofitService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL = "http://43.201.146.252:8080/anbang/";
    private PropertyRetrofitService propertyService;

    public static Retrofit getInstance(){
        Gson gson = new GsonBuilder().setLenient().create();
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public RetrofitClient(PropertyRetrofitService propertyService) {
        this.propertyService = propertyService;
    }
}