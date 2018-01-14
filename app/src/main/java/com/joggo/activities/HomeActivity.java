package com.joggo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.joggo.R;
import com.joggo.data.Prefs;
import com.joggo.di.InjectionHelper;
import com.joggo.parts.home.HomeNavigator;

import butterknife.ButterKnife;

/**
 * @author Paul
 * @since 2017.08.29
 */

public class HomeActivity extends BaseActivity implements HomeNavigator {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        InjectionHelper.getApplicationComponent().inject(this);
        ButterKnife.bind(this);
    }


    @Override
    public void logout() {
        Prefs.User.put(null);
        Intent intent = SplashActivity.createIntent(this);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
