package com.gym.app.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * @author Paul
 * @since 2017.12.08
 */

public class User {
    @SerializedName("id")
    public int mId;
    @SerializedName("username")
    public String mUsername;
    @SerializedName("password")
    public String mPassword;
    @SerializedName("collegeId")
    public int mCollegeid;
    @SerializedName("year")
    public int mYear;
    @SerializedName("section")
    public String mSection;
    @SerializedName("group_name")
    public int mGroupName;
    @SerializedName("mail")
    public String mMail;
}
