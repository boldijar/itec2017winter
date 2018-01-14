package com.gym.app.parts.section;

import com.gym.app.data.Prefs;
import com.gym.app.data.model.Message;
import com.gym.app.data.model.MessageResponse;
import com.gym.app.data.model.User;
import com.gym.app.di.InjectionHelper;
import com.gym.app.server.ITecService;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 * TODO: Class description
 *
 * @author Paul
 * @since 2017.12.09
 */

public class LoadMessagesObservable implements ObservableOnSubscribe<List<Message>> {

    @Inject
    ITecService mITecService;

    public LoadMessagesObservable() {
        InjectionHelper.getApplicationComponent().inject(this);
    }

    @Override
    public void subscribe(ObservableEmitter<List<Message>> e) throws Exception {
        User user = Prefs.User.getFromJson(User.class);
        MessageResponse messageResponse = mITecService.getMessages("section,eq," + user.mSection).execute().body();
        e.onNext(messageResponse.mMessage);
        e.onComplete();
    }

    public static Observable<List<Message>> create() {
        return Observable.create(new LoadMessagesObservable());
    }
}
