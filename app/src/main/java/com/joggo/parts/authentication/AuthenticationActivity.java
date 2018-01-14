package com.joggo.parts.authentication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.Toast;

import com.joggo.R;
import com.joggo.activities.BaseActivity;
import com.joggo.activities.HomeActivity;
import com.joggo.data.Prefs;
import com.joggo.data.model.User;
import com.joggo.di.InjectionHelper;

import butterknife.BindView;
import butterknife.ButterKnife;


public class AuthenticationActivity extends BaseActivity implements AuthenticationNavigation, AuthenticationView {

    private static final int REGISTER_FRAGMENT_POSITION = 1;

    @BindView(R.id.authentication_view_pager)
    ViewPager mAuthenticationViewPager;

    @BindView(R.id.application_logo)
    ImageView applicationLogo;

    private AuthenticationPagerAdapter mAuthenticationPagerAdapter;
    private AuthenticationPresenter mAuthenticationPresenter;

    public static Intent createIntent(Context context) {
        return new Intent(context, AuthenticationActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        ButterKnife.bind(this);
        InjectionHelper.getApplicationComponent().inject(this);
        mAuthenticationPresenter = new AuthenticationPresenter(this);
        mAuthenticationPagerAdapter = new AuthenticationPagerAdapter(getSupportFragmentManager());
        mAuthenticationViewPager.setAdapter(mAuthenticationPagerAdapter);

        mAuthenticationViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position == REGISTER_FRAGMENT_POSITION && positionOffset == 0) {
                    positionOffset = 1;
                }
                if (positionOffset < 0.5) {
                    applicationLogo.setScaleX(1 - positionOffset);
                    applicationLogo.setScaleY(1 - positionOffset);
                } else {
                    applicationLogo.setScaleX(positionOffset);
                    applicationLogo.setScaleY(positionOffset);
                }

                applicationLogo.setRotation(positionOffset * 360);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void doLogin(String email, String password) {
        mAuthenticationPresenter.login(email, password);
    }

    @Override
    public void goToRegister() {
        mAuthenticationViewPager.setCurrentItem(AuthenticationPagerAdapter.REGISTER_FRAGMENT);
    }

    @Override
    public void goBack() {
        mAuthenticationViewPager.setCurrentItem(AuthenticationPagerAdapter.LOGIN_FRAGMENT);
    }

    @Override
    public void showLoginResponse(User user) {
        Prefs.User.putAsJson(user);
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }

    @Override
    public void showError(Throwable throwable) {
        Toast.makeText(this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAuthenticationPresenter.destroySubscriptions();
    }
}
