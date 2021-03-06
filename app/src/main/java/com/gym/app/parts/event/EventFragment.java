package com.gym.app.parts.event;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.gym.app.R;
import com.gym.app.data.model.Event;
import com.gym.app.data.model.Message;
import com.gym.app.parts.home.BaseHomeFragment;
import com.gym.app.parts.section.MessageAdapter;
import com.gym.app.parts.section.SectionAdapter;
import com.gym.app.utils.Constants;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Paul
 * @since 2017.12.09
 */

public class EventFragment extends BaseHomeFragment implements EventView, SectionAdapter.SectionListener {

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
    private SectionAdapter mSectionAdapter;


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
        mEventName.setText(mEvent.mName + " - " + mEvent.mLesson.mName + " with " + mEvent.mTeacher);
        mEventTime.setText(Constants.DAY_FORMAT_TIME.format(new Date(mEvent.mTime)));

          mSectionAdapter = new SectionAdapter(mEvent.mParticipants, Color.BLACK, this);
        mAttendees.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        mAttendees.setAdapter(mSectionAdapter);

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

    @OnClick(R.id.event_directions)
    void getDirections() {
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse("google.navigation:q=" + mEvent.mAddress));
        getContext().startActivity(intent);
    }

    @OnClick(R.id.event_calendar)
    void calendarAdd() {
        Intent intent = new Intent(Intent.ACTION_EDIT);
        intent.setType("vnd.android.cursor.item/event");
        intent.putExtra(CalendarContract.Events.TITLE, mEvent.mName + " - " + mEvent.mLesson.mName);
        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,
                mEvent.mTime);
        intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,
                mEvent.mTime + 1000 * 60 * 60 * 2);
        intent.putExtra(CalendarContract.Events.ALL_DAY, false);// periodicity
        intent.putExtra(CalendarContract.Events.EVENT_LOCATION, mEvent.mAddress);// periodicity
        intent.putExtra(CalendarContract.Events.DESCRIPTION, "Teacher: " + mEvent.mTeacher);
        startActivity(intent);
    }

    @Override
    public void showMessages(List<Message> messages) {
        mMessageAdapter.add(messages);
        mChatRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true));
        mChatRecycler.setAdapter(mMessageAdapter);
    }

    Calendar mCalendar = Calendar.getInstance();

    @OnClick(R.id.event_suggest)
    void suggestChange() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(mEvent.mTime);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), mDateListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private TimePickerDialog.OnTimeSetListener mTimeListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int hour, int minute) {
            mCalendar.set(Calendar.HOUR_OF_DAY, hour);
            mCalendar.set(Calendar.MINUTE, minute);

            Toast.makeText(getContext(), R.string.change_revieing, Toast.LENGTH_SHORT).show();
            mEventPresenter.doChangeRequest(mCalendar.getTimeInMillis(), mEvent);
        }
    };
    private DatePickerDialog.OnDateSetListener mDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            mCalendar.set(Calendar.YEAR, year);
            mCalendar.set(Calendar.MONTH, month);
            mCalendar.set(Calendar.DAY_OF_MONTH, day);

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(mEvent.mTime);
            TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), mTimeListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
            timePickerDialog.show();


        }
    };

    @Override
    public void addMessage(Message message) {
        mMessageAdapter.add(message);
    }

    @Override
    public void checkinSuccess() {
        mSectionAdapter.makeCheckinable(false);
    }

    @Override
    public void checkoutSuccess() {
        mSectionAdapter.makeCheckinable(true);
    }

    @Override
    public void onCheckin(boolean add) {
       if (add){
           mEventPresenter.checkin(mEvent.mId);
       }else{
           mEventPresenter.checkout(mEvent.mId);
       }
    }
}
