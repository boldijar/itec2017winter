package com.gym.app.parts.event;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.gym.app.R;
import com.gym.app.activities.BaseActivity;
import com.gym.app.data.model.Event;

/**
 * TODO: Class description
 *
 * @author Paul
 * @since 2017.12.09
 */

public class SuggestionActivity extends BaseActivity {

    private static String ARG_EVENT = "event";

    public static Intent createIntent(Context context, Event event) {
        Intent intent = new Intent(context, SuggestionActivity.class);
        intent.putExtra(ARG_EVENT, event);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestion);
    }
}
