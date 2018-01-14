package com.gym.app.parts.event;

import com.gym.app.data.Prefs;
import com.gym.app.data.model.ChangeTimeRequest;
import com.gym.app.data.model.Event;
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
    private final User mUser;
    @Inject
    ITecService mITecService;

    public EventPresenter(EventView view) {
        super(view);
        InjectionHelper.getApplicationComponent().inject(this);
        mUser = Prefs.User.getFromJson(User.class);

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
        Message message = new Message();
        message.mUserId = mUser.mId;
        message.mUser = new User[1];
        message.mUser[0] = mUser;
        message.mText = text;
        message.mSection = mUser.mSection;
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

    public void doChangeRequest(long timeInMillis, Event event) {
        ChangeTimeRequest request = new ChangeTimeRequest();
        request.mId = 0;
        request.mEventId = event.mId;
        request.mNewTime = timeInMillis;
        request.mOldTime = event.mTime;
        request.mUserId = mUser.mId;

        mITecService.addChangeTimeRequest(request)
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

    public void checkin(int eventId) {
        mITecService.checkin(mUser.mId, eventId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action() {
                    @Override
                    public void run() throws Exception {
                        getView().checkinSuccess();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Timber.e(throwable);
                    }
                });
    }

    public void checkout(int eventId) {
        mITecService.checkout(mUser.mId, eventId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action() {
                    @Override
                    public void run() throws Exception {
                        getView().checkoutSuccess();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Timber.e(throwable);
                    }
                });
    }
}
