package com.joggo.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * @author Paul
 * @since 2017.12.08
 */

public class User implements Serializable {
    @SerializedName("id")
    public int mId;
    @SerializedName("role")
    public String mRole;
    @SerializedName("email")
    public String mEmail;
    @SerializedName("distance")
    public int mDistance;
    @SerializedName("minutes")
    public int mMinutes;
    @SerializedName("password")
    public String mPassword;
    @SerializedName("success")
    public boolean mSuccess;
}
