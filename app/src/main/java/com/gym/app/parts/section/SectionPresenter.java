package com.gym.app.parts.section;

import com.gym.app.data.Prefs;
import com.gym.app.data.model.Message;
import com.gym.app.data.model.User;
import com.gym.app.di.InjectionHelper;
import com.gym.app.presenter.Presenter;
import com.gym.app.server.ITecService;
import com.gym.app.utils.MvpObserver;

import java.util.List;

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

public class SectionPresenter extends Presenter<SectionView> {


    @Inject
    ITecService mITecService;

    public SectionPresenter(SectionView view) {
        super(view);
        InjectionHelper.getApplicationComponent().inject(this);
    }

    public void loadUsers() {
        LoadSectionUserObservable.create()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MvpObserver<List<User>>(this) {
                    @Override
                    public void onNext(List<User> value) {
                        getView().showUsers(value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        getView().showErrorGettingUsers();
                    }
                });
    }

    public void loadMessages() {
        LoadMessagesObservable.create()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MvpObserver<List<Message>>(this) {
                    @Override
                    public void onNext(List<Message> value) {
                        getView().showMessage(value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        getView().showErrorGettingUsers();
                    }
                });
    }

    public void sendMessage(String text) {
        User user = Prefs.User.getFromJson(User.class);
        Message message = new Message();
        message.mUserId = user.mId;
        message.mUser = new User[1];
        message.mUser[0] = user;
        message.mText = text;
        message.mSection = user.mSection;
        message.mTime = System.currentTimeMillis();
        getView().addMessage(message);
        mITecService.addMessage(message)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action() {
                    @Override
                    public void run() throws Exception {

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Timber.e(throwable);
                    }
                });
    }
}
