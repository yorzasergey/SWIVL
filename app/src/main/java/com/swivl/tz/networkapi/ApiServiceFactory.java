package com.swivl.tz.networkapi;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.swivl.tz.BuildConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public final class ApiServiceFactory {

    private static volatile ApiService retrofitApiNetworkService;

    private ApiServiceFactory(){

    }

    public static ApiService getRetrofitApiService(){

        ApiService service = retrofitApiNetworkService;

        if(service == null){
            synchronized (ApiServiceFactory.class){
                service = retrofitApiNetworkService;
                if(service == null){
                    service = retrofitApiNetworkService = buildRetrofit().create(ApiService.class);
                }
            }
        }
        return service;
    }

    @NonNull
    private static Retrofit buildRetrofit(){

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        return new Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_API_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(createOkHttpClient())
                .build();
    }

    private static OkHttpClient createOkHttpClient() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .connectTimeout(15, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .cache(null);

        return builder.build();
    }
}
