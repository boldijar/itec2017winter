package com.gym.app.parts.authentication;

import com.gym.app.data.model.User;
import com.gym.app.data.model.UserResponse;
import com.gym.app.di.InjectionHelper;
import com.gym.app.presenter.Presenter;
import com.gym.app.server.ITecService;
import com.gym.app.utils.MvpObserver;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author Paul
 * @since 2017.10.29
 */

public class AuthenticationPresenter extends Presenter<AuthenticationView> {

    @Inject
    ITecService mApiService;

    public AuthenticationPresenter(AuthenticationView view) {
        super(view);
        InjectionHelper.getApplicationComponent().inject(this);
    }

    void login(String email, String password) {
        mApiService.loginUsers("mail,eq," + email, "password,eq," + password)
                .subscribeOn(Schedulers.io()) // where the request should be done
                .observeOn(AndroidSchedulers.mainThread()) // where the response should be handled
                .subscribe(new MvpObserver<UserResponse>(this) { // this is a cool class to help disposing observables
                    @Override
                    public void onNext(UserResponse response) {
                        List<User> value = response.mUsers;
                        if (value == null || value.isEmpty()) {
                            getView().showError(new IllegalStateException("Invalid login"));
                            return;
                        }
                        getView().showLoginResponse(value.get(0));
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        getView().showError(e);
                    }
                });
    }
}
