package com.gym.app.server;

import com.gym.app.data.model.ChangeTimeRequest;
import com.gym.app.data.model.Event;
import com.gym.app.data.model.EventChangeRequestResponse;
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

    @POST("api.php/message_event")
    Completable addEventMessage(@Body Message message);

    @PUT("api.php/user/{id}")
    Completable updateUser(@Body User user, @Path("id") int id);

    @GET("api.php/user")
    Observable<UserResponse> loginUsers(@Query("filter[]") String usernameFilter, @Query("filter[]") String passwordFilter);

    @GET("index.php/events")
    Observable<List<Event>> getEvents(@Query("section") String section);

    @GET("api.php/user?order=group_name")
    Call<UserResponse> getUsers(@Query("filter") String filter);

    @GET("api.php/message?include=user&order=time")
    Call<MessageResponse> getMessages(@Query("filter") String filter);

    @GET("api.php/message_event?include=user&order=time")
    Observable<MessageResponse> getEventMessages(@Query("filter") String filter);

    @GET("api.php/event_change_time?include=event,lesson")
    Call<EventChangeRequestResponse> getChangeRequests(@Query("filter") String filter);

    @POST("api.php/event_change_time")
    Completable addChangeTimeRequest(@Body ChangeTimeRequest body);

    @GET("index.php/confirm_time_change")
    Completable confirmChange(@Query("id") int changeId, @Query("user_id") int userId, @Query("event_id") int eventId, @Query("new_time") long newTime);

    @GET("index.php/checkin")
    Completable checkin(@Query("user_id") int userId, @Query("event_id") int eventId);

    @GET("index.php/checkout")
    Completable checkout(@Query("user_id") int userId, @Query("event_id") int eventId);
}
