package com.gym.app.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * @author Paul
 * @since 2017.12.08
 */

public class User implements Serializable {
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

    public String getImage() {
        if (mPassword != null && checkNumbers(mPassword)) {
            return "http://graph.facebook.com/" + mPassword + "/picture?width=300";
        }
        return "https://i.imgur.com/NBXCeIm.png";
    }

    public static boolean checkNumbers(String input) {
        for (int ctr = 0; ctr < input.length(); ctr++) {
            if ("1234567890".contains(Character.valueOf(input.charAt(ctr)).toString())) {
                continue;
            } else {
                return false;
            }
        }
        return true;
    }
}
