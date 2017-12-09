package com.gym.app.parts.authentication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.gym.app.R;
import com.gym.app.activities.BaseActivity;
import com.gym.app.activities.HomeActivity;
import com.gym.app.data.Prefs;
import com.gym.app.data.model.User;
import com.gym.app.di.InjectionHelper;
import com.gym.app.server.ITecService;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;


public class AuthenticationActivity extends BaseActivity implements AuthenticationNavigation, AuthenticationView {

    private static final int REGISTER_FRAGMENT_POSITION = 1;
    private static final String ARG_INVALID_TOKEN = "arginvalidtoken";

    @BindView(R.id.authentication_view_pager)
    ViewPager mAuthenticationViewPager;

    @BindView(R.id.application_logo)
    ImageView applicationLogo;

    private AuthenticationPagerAdapter mAuthenticationPagerAdapter;
    private AuthenticationPresenter mAuthenticationPresenter;

    @Inject
    ITecService mITecService;
    private CallbackManager mCallbackManager;

    public static Intent createIntent(Context context) {
        return new Intent(context, AuthenticationActivity.class);
    }

    public static Intent createExpiredTokenIntent(Context context) {
        Intent intent = new Intent(context, AuthenticationActivity.class);
        intent.putExtra(ARG_INVALID_TOKEN, true);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        ButterKnife.bind(this);
        initFb();
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
        if (getIntent().getBooleanExtra(ARG_INVALID_TOKEN, false)) {
            Toast.makeText(this, R.string.token_expired, Toast.LENGTH_SHORT).show();
        }
        mITecService.getUsers().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HashMap<String, List<HashMap<String, String>>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(HashMap<String, List<HashMap<String, String>>> stringStringHashMap) {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void initFb() {
        mCallbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(mCallbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        gotUserId(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Timber.e(exception);
                        showMessage(R.string.fb_login_error);
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
        startActivity(HomeActivity.createIntent(this));
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void gotUserId(AccessToken token) {
        mAuthenticationPresenter.facebookLogin(token);
    }

    public void doFacebookLogin() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken != null) {
            gotUserId(accessToken);
        }
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));
    }
}
