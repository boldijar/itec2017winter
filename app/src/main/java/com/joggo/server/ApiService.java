package com.joggo.server;

import com.joggo.data.model.User;

import io.reactivex.Completable;
import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {

    @POST("login")
    Observable<User> login(@Body User user);

    @POST("users")
    Completable register(@Body User user);
}
