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
import com.gym.app.data.model.User;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Paul
 * @since 2017.12.09
 */

public class SectionAdapter extends RecyclerView.Adapter<SectionAdapter.SectionHolder> {

    private final List<User> mUsers;
    private int mTextColor = Color.WHITE;

    public SectionAdapter(List<User> users) {
        mUsers = users;
    }

    public SectionAdapter(List<User> users, int textColor) {
        mUsers = users;
        mTextColor = textColor;
    }

    @Override
    public SectionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SectionHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false));
    }

    @Override
    public void onBindViewHolder(SectionHolder holder, int position) {
        User user = mUsers.get(position);
        Glide.with(holder.mImage.getContext()).load(user.getImage()).into(holder.mImage);
        holder.mText.setText(user.mUsername);
        holder.mText.setTextColor(mTextColor);
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
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
}
