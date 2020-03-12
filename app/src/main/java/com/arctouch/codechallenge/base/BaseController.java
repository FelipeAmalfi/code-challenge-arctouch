package com.arctouch.codechallenge.base;

import android.support.v7.app.AppCompatActivity;

import com.arctouch.codechallenge.api.TmdbApi;

import java.util.logging.Level;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;

public  class BaseController  {

// set your desired log level

    public TmdbApi getApi() {
        return new Retrofit.Builder()
                .baseUrl(TmdbApi.URL)
                .client(new OkHttpClient.Builder().build())
                .addConverterFactory(MoshiConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(TmdbApi.class);
    }

}
