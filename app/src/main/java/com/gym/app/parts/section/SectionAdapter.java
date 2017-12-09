package com.gym.app.parts.section;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gym.app.R;
import com.gym.app.data.Prefs;
import com.gym.app.data.model.User;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Paul
 * @since 2017.12.09
 */

public class SectionAdapter extends RecyclerView.Adapter<SectionAdapter.SectionHolder> {

    private static final String IMAGE_CHECKIN = "https://i.imgur.com/p1xC4Gq.png";
    private final List<User> mUsers;
    private int mTextColor = Color.WHITE;
    private User mUser = Prefs.User.getFromJson(User.class);
    private SectionListener mSectionListener;

    public SectionAdapter(List<User> users) {
        mUsers = users;
    }

    public SectionAdapter(List<User> users, int textColor, SectionListener sectionListener) {
        mUsers = users;
        mTextColor = textColor;
        mSectionListener = sectionListener;
        boolean found = false;
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            if (user.mId == mUser.mId) {
                mUsers.remove(user);
                user.mUsername = "Remove";
                mUsers.add(0, user);
                found = true;
                break;
            }
        }
        if (!found) {
            User user = new User();
            user.mImage = IMAGE_CHECKIN;
            user.mUsername = "Checkin";
            mUsers.add(0, user);
        }
    }

    @Override
    public SectionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SectionHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false));
    }

    @Override
    public void onBindViewHolder(SectionHolder holder, final int position) {
        final User user = mUsers.get(position);
        Glide.with(holder.mImage.getContext()).load(user.getImage()).into(holder.mImage);
        holder.mText.setText(user.mUsername);
        holder.mText.setTextColor(mTextColor);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position == 0) {
                    if (mSectionListener != null) {
                        mSectionListener.onCheckin(user.mId != mUser.mId);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public void makeCheckinable(boolean make) {
        if (mUsers.isEmpty()) {
            return;
        }
        User user = mUsers.get(0);
        if (make) {
            user.mImage = IMAGE_CHECKIN;
            user.mId = 0;
            user.mUsername = "Checkin";
        } else {
            user.mImage = null;
            user.mId = mUser.mId;
            user.mPassword = mUser.mPassword;
            user.mUsername = "Cancel";
        }
        notifyItemChanged(0);
    }

    public static class SectionHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.user_image)
        ImageView mImage;
        @BindView(R.id.user_text)
        TextView mText;

        public SectionHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface SectionListener {

        void onCheckin(boolean add);
    }
}
