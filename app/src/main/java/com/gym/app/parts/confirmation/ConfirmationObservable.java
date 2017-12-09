package com.gym.app.parts.confirmation;

import com.gym.app.data.Prefs;
import com.gym.app.data.model.ChangeTimeRequest;
import com.gym.app.data.model.Event;
import com.gym.app.data.model.User;
import com.gym.app.di.InjectionHelper;
import com.gym.app.server.ITecService;

import java.util.ArrayList;
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

public class ConfirmationObservable implements ObservableOnSubscribe<List<ConfirmationModel>> {
    @Inject
    ITecService mITecService;

    public ConfirmationObservable() {
        InjectionHelper.getApplicationComponent().inject(this);
    }

    @Override
    public void subscribe(ObservableEmitter<List<ConfirmationModel>> e) throws Exception {
        User user = Prefs.User.getFromJson(User.class);
        List<ChangeTimeRequest> changes = mITecService.getChangeRequests("user_id,neq," + user.mId).execute().body().mChangeTimeRequest;
        List<Event> events = mITecService.getEventRequest("user_id,neq," + user.mId).execute().body().mEvents;

        List<ConfirmationModel> confirmationModels = new ArrayList<>();
        confirmationModels.addAll(changes);
        confirmationModels.addAll(events);
        e.onNext(confirmationModels);
        e.onComplete();
    }

    public static Observable<List<ConfirmationModel>> create() {
        return Observable.create(new ConfirmationObservable());
    }
}
