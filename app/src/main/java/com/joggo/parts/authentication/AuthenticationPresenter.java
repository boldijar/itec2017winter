package com.joggo.parts.authentication;

import com.joggo.data.model.User;
import com.joggo.di.InjectionHelper;
import com.joggo.presenter.Presenter;
import com.joggo.server.ApiService;
import com.joggo.utils.MvpObserver;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author Paul
 * @since 2017.10.29
 */

public class AuthenticationPresenter extends Presenter<AuthenticationView> {

    @Inject
    ApiService mApiService;

    public AuthenticationPresenter(AuthenticationView view) {
        super(view);
        InjectionHelper.getApplicationComponent().inject(this);
    }

    void login(String email, String password) {
        User user = new User();
        user.mEmail = email;
        user.mPassword = password;
        mApiService.login(user)
                .subscribeOn(Schedulers.io()) // where the request should be done
                .observeOn(AndroidSchedulers.mainThread()) // where the response should be handled
                .subscribe(new MvpObserver<User>(this) { // this is a cool class to help disposing observables
                    @Override
                    public void onNext(User user) {
                        if (user.mSuccess) {
                            getView().showLoginResponse(user);
                        } else {
                            getView().showError(new Exception("Invalid login"));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        getView().showError(e);
                    }
                });
    }
}
