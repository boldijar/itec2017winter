package com.gym.app.data.model;

import android.text.format.DateUtils;

import com.google.gson.annotations.SerializedName;

public class Message {
    @SerializedName("id")
    public int mId;
    @SerializedName("text")
    public String mText;
    @SerializedName("section")
    public String mSection;
    @SerializedName("time")
    public long mTime;
    @SerializedName("user_id")
    public int mUserId;
    @SerializedName("user")
    public User[] mUser;

    public String getImage() {
        if (mUser == null || mUser.length == 0) {
            return new User().getImage();
        }
        return mUser[0].getImage();
    }

    public String getAgo() {
        return DateUtils.getRelativeTimeSpanString(mTime, System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS).toString();
    }
}
