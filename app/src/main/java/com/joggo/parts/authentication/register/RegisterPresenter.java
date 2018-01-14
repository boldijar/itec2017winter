package com.joggo.parts.authentication.register;

import com.joggo.data.model.User;
import com.joggo.di.InjectionHelper;
import com.joggo.presenter.Presenter;
import com.joggo.server.ApiService;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @since 2017.11.15
 */

public class RegisterPresenter extends Presenter<RegisterView> {

    @Inject
    ApiService mApiService;

    RegisterPresenter(RegisterView view) {
        super(view);
        InjectionHelper.getApplicationComponent().inject(this);
    }

    void register(String email, String password) {
        User user = new User();
        user.mEmail = email;
        user.mPassword = password;
        addDisposable(mApiService.register(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action() {
                    @Override
                    public void run() throws Exception {
                        getView().displayRegistrationSuccess();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        getView().displayRegistrationError();
                    }
                }));
    }
}
