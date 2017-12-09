package com.gym.app.server;

import com.gym.app.data.model.User;
import com.gym.app.data.model.UserResponse;

import java.util.HashMap;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ITecService {

    @GET("api.php/user")
    Observable<HashMap<String, List<HashMap<String, String>>>> getUsers();

    @POST("api.php/user")
    Completable addUser(@Body User user);

    @PUT("api.php/user/{id}")
    Completable updateUser(@Body User user, @Path("id") int id);

    //http://192.168.1.122/itec/api.php/user?filter[]=username,eq,bolnizar&filter[]=password,eq,123123
    @GET("api.php/user")
    Observable<UserResponse> loginUsers(@Query("filter[]") String usernameFilter, @Query("filter[]") String passwordFilter);
}
