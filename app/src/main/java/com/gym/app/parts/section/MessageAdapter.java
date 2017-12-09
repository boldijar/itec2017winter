package com.gym.app.parts.section;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gym.app.R;
import com.gym.app.data.Prefs;
import com.gym.app.data.model.Message;
import com.gym.app.data.model.User;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * TODO: Class description
 *
 * @author Paul
 * @since 2017.12.09
 */

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    static private int TYPE_MESSAGE_LEFT = 1;
    static private int TYPE_MESSAGE_RIGHT = 2;

    private List<Message> mMessageList = new ArrayList<>();
    private User mUser = Prefs.User.getFromJson(User.class);

    public void add(List<Message> messages) {
        mMessageList.clear();
        mMessageList.addAll(messages);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MessageHolder(LayoutInflater.from(parent.getContext()).inflate(viewType == TYPE_MESSAGE_LEFT ? R.layout.item_message_left : R.layout.item_message_right, parent, false));
    }

    @Override
    public int getItemViewType(int position) {
//        Message message = mMessageList.get(position);
//        return mUser.mId == message.mUserId ? TYPE_MESSAGE_RIGHT : TYPE_MESSAGE_LEFT;
        return TYPE_MESSAGE_LEFT;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MessageHolder leftMessageHolder = (MessageHolder) holder;
        Message message = mMessageList.get(position);
        Glide.with(holder.itemView.getContext()).load(message.getImage()).into(leftMessageHolder.mImage);
        leftMessageHolder.mText.setText(message.mText);
        leftMessageHolder.mTime.setText(message.getAgo());
    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }

    public void add(Message message) {
        mMessageList.add(message);
        notifyItemInserted(mMessageList.size() - 1);
    }

    public static class MessageHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.message_image)
        ImageView mImage;
        @BindView(R.id.message_text)
        TextView mText;
        @BindView(R.id.message_time)
        TextView mTime;

        public MessageHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
