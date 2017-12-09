package com.gym.app.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * TODO: Class description
 *
 * @author Paul
 * @since 2017.12.09
 */

public class Event {
    @SerializedName("id")
    public int mId;
    @SerializedName("time")
    public long mTime;
    @SerializedName("name")
    public String mName;
    @SerializedName("section")
    public String mSection;
    @SerializedName("participants")
    public List<User> mParticipants;
    @SerializedName("lesson")
    public Lesson mLesson;
}
