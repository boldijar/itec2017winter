package com.gym.app.parts.event;

import com.gym.app.data.Prefs;
import com.gym.app.data.model.Message;
import com.gym.app.data.model.MessageResponse;
import com.gym.app.data.model.User;
import com.gym.app.di.InjectionHelper;
import com.gym.app.presenter.Presenter;
import com.gym.app.server.ITecService;
import com.gym.app.utils.MvpObserver;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * @author Paul
 * @since 2017.12.09
 */

public class EventPresenter extends Presenter<EventView> {
    @Inject
    ITecService mITecService;

    public EventPresenter(EventView view) {
        super(view);
        InjectionHelper.getApplicationComponent().inject(this);
    }

    public void loadMessages(int eventId) {
        mITecService.getEventMessages("event_id,eq," + eventId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MvpObserver<MessageResponse>(this) {
                    @Override
                    public void onNext(MessageResponse value) {
                        getView().showMessages(value.mMessage);
                    }
                });
    }

    public void sendMessage(String text, int eventId) {
        User user = Prefs.User.getFromJson(User.class);
        Message message = new Message();
        message.mUserId = user.mId;
        message.mUser = new User[1];
        message.mUser[0] = user;
        message.mText = text;
        message.mSection = user.mSection;
        message.mTime = System.currentTimeMillis();
        message.mEventId = eventId;
        getView().addMessage(message);
        mITecService.addEventMessage(message)
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
