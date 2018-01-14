package com.gym.app.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author Paul
 * @since 2017.10.29
 */

public class LessonResponse {

    @SerializedName("lesson")
    public List<Lesson> mLessonList;
}
