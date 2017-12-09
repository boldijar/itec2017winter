package com.gym.app.server;

import com.gym.app.data.model.Event;
import com.gym.app.data.model.Message;
import com.gym.app.data.model.MessageResponse;
import com.gym.app.data.model.User;
import com.gym.app.data.model.UserResponse;

import java.util.HashMap;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import retrofit2.Call;
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

    @POST("api.php/message")
    Completable addMessage(@Body Message message);

    @PUT("api.php/user/{id}")
    Completable updateUser(@Body User user, @Path("id") int id);

    //http://192.168.1.122/itec/api.php/user?filter[]=username,eq,bolnizar&filter[]=password,eq,123123
    @GET("api.php/user")
    Observable<UserResponse> loginUsers(@Query("filter[]") String usernameFilter, @Query("filter[]") String passwordFilter);

    @GET("index.php/events")
    Observable<List<Event>> getEvents(@Query("from") long from, @Query("to") long to, @Query("section") String section);

    @GET("api.php/user?order=group_name")
    Call<UserResponse> getUsers(@Query("filter") String filter);

    @GET("api.php/message?include=user&order=time")
    Call<MessageResponse> getMessages(@Query("filter") String filter);

}
