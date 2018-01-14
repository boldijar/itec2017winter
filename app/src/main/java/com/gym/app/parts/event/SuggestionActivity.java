package com.gym.app.parts.event;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.gym.app.R;
import com.gym.app.activities.BaseActivity;
import com.gym.app.data.Prefs;
import com.gym.app.data.model.Event;
import com.gym.app.data.model.Lesson;
import com.gym.app.data.model.User;
import com.gym.app.utils.Constants;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * TODO: Class description
 *
 * @author Paul
 * @since 2017.12.09
 */

public class SuggestionActivity extends BaseActivity implements SuggestionView {

    @BindView(R.id.suggestion_lessons)
    Spinner mSpinner;
    @BindView(R.id.suggestion_address)
    EditText mAddress;
    @BindView(R.id.suggestion_teacher)
    EditText mTeacher;
    @BindView(R.id.suggestion_time)
    TextView mTime;
    @BindView(R.id.suggestion_name)
    EditText mName;
    private SuggestionPresenter mSuggestionPresenter = new SuggestionPresenter(this);

    public static Intent createIntent(Context context) {
        Intent intent = new Intent(context, SuggestionActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestion);
        ButterKnife.bind(this);
        mSuggestionPresenter.loadLessons();
    }

    Calendar mCalendar = Calendar.getInstance();

    @Override
    public void showLessons(List<Lesson> lessonList) {
        ArrayAdapter<Lesson> spinnerArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, lessonList);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        mSpinner.setAdapter(spinnerArrayAdapter);
    }

    @Override
    public void done() {
        Toast.makeText(getApplicationContext(), R.string.wait_event_approval, Toast.LENGTH_SHORT).show();
        finish();
    }

    private TimePickerDialog.OnTimeSetListener mTimeListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int hour, int minute) {
            mCalendar.set(Calendar.HOUR_OF_DAY, hour);
            mCalendar.set(Calendar.MINUTE, minute);
            mTime.setTag(mCalendar.getTimeInMillis());
            mTime.setText("Time: " + Constants.DAY_FORMAT_TIME.format(mCalendar.getTime()));
        }
    };
    private DatePickerDialog.OnDateSetListener mDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            mCalendar.set(Calendar.YEAR, year);
            mCalendar.set(Calendar.MONTH, month);
            mCalendar.set(Calendar.DAY_OF_MONTH, day);

            Calendar calendar = mCalendar;
            TimePickerDialog timePickerDialog = new TimePickerDialog(SuggestionActivity.this, mTimeListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
            timePickerDialog.show();
        }
    };

    @OnClick(R.id.suggestion_change_time)
    void changeTime() {
        Calendar calendar = mCalendar;

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, mDateListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @OnClick(R.id.suggestion_save)
    void onSave() {
        User user = Prefs.User.getFromJson(User.class);
        if (mTime.getTag() == null) {
            showMessage(R.string.please_set_time);
            return;
        }
        if (mTeacher.getText().length() == 0 || mAddress.getText().length() == 0 || mName.getText().length() == 0) {
            showMessage(R.string.please_fill_fields);
            return;
        }

        Lesson selectedLesson = (Lesson) mSpinner.getSelectedItem();
        if (selectedLesson == null) {
            showMessage(R.string.select_a_lesson);
            return;
        }
        Event event = new Event();
        event.mAddress = mAddress.getText().toString();
        event.mName = mName.getText().toString();
        event.mTeacher = mTeacher.getText().toString();
        event.mLessonId = selectedLesson.mId;
        event.mSection = user.mSection;
        event.mUserId = user.mId;
        event.mTime = (long) mTime.getTag();

        mSuggestionPresenter.requestAddEvent(event);

    }
}
