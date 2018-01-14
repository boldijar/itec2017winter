package com.joggo.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 *
 * @author Paul
 * @since 2017.12.08
 */

public class UserResponse {

    @SerializedName("user")
    public List<User> mUsers;
}
