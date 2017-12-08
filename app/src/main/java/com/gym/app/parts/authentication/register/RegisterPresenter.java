package com.gym.app.parts.authentication.register;

import com.gym.app.data.model.User;
import com.gym.app.di.InjectionHelper;
import com.gym.app.presenter.Presenter;
import com.gym.app.server.ITecService;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @author catalinradoiu
 * @since 2017.11.15
 */

public class RegisterPresenter extends Presenter<RegisterView> {

    @Inject
    ITecService mITecService;

    RegisterPresenter(RegisterView view) {
        super(view);
        InjectionHelper.getApplicationComponent().inject(this);
    }

    void register(String username, String email, String password) {
        User user = new User();
        user.mUsername = username;
        user.mMail = email;
        user.mPassword = password;
        addDisposable(mITecService.addUser(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action() {
                    @Override
                    public void run() throws Exception {
                        getView().displayRegistrationSuccess();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        getView().displayRegistrationError();
                    }
                }));
    }
}
