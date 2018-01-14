package com.gym.app.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * TODO: Class description
 *
 * @author Paul
 * @since 2017.12.09
 */

public class MessageResponse {
    @SerializedName(value = "message", alternate = "message_event")
    public List<Message> mMessage;
}
