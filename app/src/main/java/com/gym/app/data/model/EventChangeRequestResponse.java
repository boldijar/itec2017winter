package com.gym.app.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * TODO: Class description
 *
 * @author Paul
 * @since 2017.12.09
 */

public class EventChangeRequestResponse {

    @SerializedName("event_change_time")
    public List<ChangeTimeRequest> mChangeTimeRequest;
}
