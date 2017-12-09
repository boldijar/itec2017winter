package com.gym.app.parts.event;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.gym.app.R;
import com.gym.app.activities.BaseActivity;
import com.gym.app.data.model.Lesson;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

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

    @Override
    public void showLessons(List<Lesson> lessonList) {
        ArrayAdapter<Lesson> spinnerArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, lessonList);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        mSpinner.setAdapter(spinnerArrayAdapter);
    }
}
