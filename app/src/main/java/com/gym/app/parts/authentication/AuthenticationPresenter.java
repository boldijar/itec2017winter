package com.gym.app.parts.authentication;

import android.os.Bundle;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.gym.app.data.model.User;
import com.gym.app.data.model.UserResponse;
import com.gym.app.di.InjectionHelper;
import com.gym.app.presenter.Presenter;
import com.gym.app.server.ITecService;
import com.gym.app.utils.MvpObserver;

import org.json.JSONObject;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

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

    void facebookLogin(final AccessToken token) {
        GraphRequest mGraphRequest = GraphRequest.newMeRequest(
                token, new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject me, GraphResponse response) {
                        if (response.getError() != null) {
                            getView().showError(new Exception("Can't login with facebook!"));
                        } else {
                            String email = me.optString("email");
                            String name = me.optString("name");
                            gotEmailAndPassword(email, name, token.getUserId());
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email");
        mGraphRequest.setParameters(parameters);
        mGraphRequest.executeAsync();
    }

    private void gotEmailAndPassword(final String email, String name, final String userId) {
        User user = new User();
        user.mUsername = name;
        user.mMail = email;
        user.mPassword = userId;
        mApiService.addUser(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action() {
                    @Override
                    public void run() throws Exception {
                        login(email, userId);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        getView().showError(new Exception("Can't login with facebook!"));
                        Timber.e(throwable);
                    }
                });
    }
}
