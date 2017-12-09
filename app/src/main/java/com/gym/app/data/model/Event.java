package com.gym.app.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * @author Paul
 * @since 2017.12.09
 */

public class Event implements Serializable {
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
    @SerializedName("lesson_obj")
    public Lesson mLesson;
    @SerializedName("lesson")
    public Lesson[] mLessons;
    @SerializedName("teacher")
    public String mTeacher;
    @SerializedName("address")
    public String mAddress;
}
