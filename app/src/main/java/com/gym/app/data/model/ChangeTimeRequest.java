package com.gym.app.data.model;

import com.google.gson.annotations.SerializedName;
import com.gym.app.parts.confirmation.ConfirmationModel;
import com.gym.app.parts.confirmation.ConfirmationType;

/**
 * @author Paul
 * @since 2017.12.09
 */

public class ChangeTimeRequest extends ConfirmationModel {
    @SerializedName("id")
    public int mId;
    @SerializedName("votes")
    public int mVotes;
    @SerializedName("old_time")
    public long mOldTime;
    @SerializedName("new_time")
    public long mNewTime;
    @SerializedName("event_id")
    public int mEventId;
    @SerializedName("user_id")
    public int mUserId;
    @SerializedName("event")
    public Event[] mEvent;

    @Override
    public ConfirmationType getType() {
        return ConfirmationType.TIME;
    }
}
