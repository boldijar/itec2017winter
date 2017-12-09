package com.gym.app.data.model;

import com.google.gson.annotations.SerializedName;
import com.gym.app.parts.confirmation.ConfirmationModel;
import com.gym.app.parts.confirmation.ConfirmationType;

import java.io.Serializable;
import java.util.List;

/**
 * @author Paul
 * @since 2017.12.09
 */

public class Event extends ConfirmationModel implements Serializable {
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

    @SerializedName("user_id")
    public int mUserId;
    @SerializedName("lesson_id")
    public int mLessonId;

    @Override
    public ConfirmationType getType() {
        return ConfirmationType.EVENT;
    }

    @Override
    public int getId() {
        return mId;
    }
}
