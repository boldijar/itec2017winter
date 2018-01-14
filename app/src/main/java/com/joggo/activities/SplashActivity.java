package com.joggo.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.joggo.R;
import com.joggo.data.Prefs;
import com.joggo.parts.authentication.AuthenticationActivity;

import java.util.concurrent.TimeUnit;

import butterknife.BindAnim;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author Paul
 * @since 2017.08.29
 */

public class SplashActivity extends BaseActivity {

    @BindView(R.id.imageViewSplash)
    ImageView mImageViewSplash;
    @BindAnim(R.anim.down_to_up)
    Animation mLogoAnimation;
    @BindAnim(R.anim.up_to_down)
    Animation mTextAnimation;

    public static Intent createIntent(Context context) {
        return new Intent(context, SplashActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        setUIElements();
        timer();
    }

    private void timer() {
        Observable.timer(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(Long value) {
                        doneWaiting();
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    private void doneWaiting() {
        if (Prefs.User.get() != null) {
            startActivity(new Intent(this, HomeActivity.class));
        } else {
            startActivity(AuthenticationActivity.createIntent(this));
        }
        finish();
    }

    private void setUIElements() {
        ButterKnife.bind(this);
    }
}