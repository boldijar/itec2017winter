package com.gym.app.parts.profile;

import com.gym.app.data.Prefs;
import com.gym.app.data.model.User;
import com.gym.app.di.InjectionHelper;
import com.gym.app.presenter.Presenter;
import com.gym.app.server.ITecService;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * TODO: Class description
 *
 * @author Paul
 * @since 2017.12.09
 */

public class ProfilePresenter extends Presenter<ProfileView> {

    @Inject
    ITecService mITecService;

    public ProfilePresenter(ProfileView view) {
        super(view);
        InjectionHelper.getApplicationComponent().inject(this);
    }

    public void updateProfile(final User user) {
        mITecService.updateUser(user, user.mId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action() {
                               @Override
                               public void run() throws Exception {
                                   Timber.d("Profile updated");
                                   Prefs.User.putAsJson(user);
                                   Prefs.ProfileCreated.put(true);
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                getView().onError();
                            }
                        });
    }

}
