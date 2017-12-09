package com.gym.app.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * TODO: Class description
 *
 * @author Paul
 * @since 2017.12.09
 */

public class Lesson implements Serializable {
    @SerializedName("name")
    public String mName;
    @SerializedName("image")
    public String mImage;
}
