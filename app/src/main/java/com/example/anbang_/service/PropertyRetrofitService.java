package com.example.anbang_.service;

import com.example.anbang_.model.Property;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface PropertyRetrofitService {

    @POST("/")
    @FormUrlEncoded
    Call<ResponseBody> registerProperty(Property property);

    @GET("test")
    Call<ResponseBody> buildChannelCA();
}
