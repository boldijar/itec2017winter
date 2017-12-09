package com.gym.app.parts.event;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gym.app.R;
import com.gym.app.data.model.Event;
import com.gym.app.data.model.Message;
import com.gym.app.parts.home.BaseHomeFragment;
import com.gym.app.parts.section.MessageAdapter;
import com.gym.app.parts.section.SectionAdapter;
import com.gym.app.utils.Constants;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Paul
 * @since 2017.12.09
 */

public class EventFragment extends BaseHomeFragment implements EventView {

    @BindView(R.id.event_image)
    ImageView mImageView;
    @BindView(R.id.event_name)
    TextView mEventName;
    @BindView(R.id.event_time)
    TextView mEventTime;
    @BindView(R.id.event_attendees)
    RecyclerView mAttendees;
    @BindView(R.id.event_chat)
    RecyclerView mChatRecycler;
    @BindView(R.id.event_input)
    EditText mInput;

    private Event mEvent;
    private EventPresenter mEventPresenter = new EventPresenter(this);
    private MessageAdapter mMessageAdapter = new MessageAdapter();


    public void setEvent(Event event) {
        mEvent = event;
    }

    @Override
    protected int getTitle() {
        return R.string.event_details;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_event;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        Glide.with(getContext()).load(mEvent.mLesson.mImage).into(mImageView);
        mEventName.setText(mEvent.mName + " - " + mEvent.mLesson.mName);
        mEventTime.setText(Constants.DAY_FORMAT.format(new Date(mEvent.mTime)));

        SectionAdapter sectionAdapter = new SectionAdapter(mEvent.mParticipants, Color.BLACK);
        mAttendees.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        mAttendees.setAdapter(sectionAdapter);

        mEventPresenter.loadMessages(mEvent.mId);
    }

    @OnClick(R.id.event_send)
    void sendMessage() {
        if (mInput.getText().length() == 0) {
            return;
        }
        mEventPresenter.sendMessage(mInput.getText().toString(), mEvent.mId);
        mInput.setText(null);
    }

    @OnClick(R.id.event_retry)
    void onRetry() {
        mEventPresenter.loadMessages(mEvent.mId);
    }

    @Override
    public void showMessages(List<Message> messages) {
        mMessageAdapter.add(messages);
        mChatRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true));
        mChatRecycler.setAdapter(mMessageAdapter);
    }

    @Override
    public void addMessage(Message message) {
        mMessageAdapter.add(message);
    }
}
