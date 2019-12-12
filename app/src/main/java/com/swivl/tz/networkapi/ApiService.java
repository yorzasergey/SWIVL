package com.swivl.tz.networkapi;


import com.google.gson.JsonArray;
import com.swivl.tz.BuildConfig;
import com.swivl.tz.networkmodel.UserDetailResponse;
import com.swivl.tz.networkmodel.UserResponse;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface ApiService {

    @GET("/users")
    Single<List<UserResponse>> getUsers();

    @GET("/users")
    Single<List<UserResponse>> getUsers(@Query ("since") String since);

    @GET
    Single<UserDetailResponse> getUserProfile(@Url String url);
}
