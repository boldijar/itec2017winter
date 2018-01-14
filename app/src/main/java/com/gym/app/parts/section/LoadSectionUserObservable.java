package com.gym.app.parts.section;

import com.gym.app.data.Prefs;
import com.gym.app.data.model.User;
import com.gym.app.data.model.UserResponse;
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

public class LoadSectionUserObservable implements ObservableOnSubscribe<List<User>> {

    @Inject
    ITecService mITecService;

    public LoadSectionUserObservable(){
        InjectionHelper.getApplicationComponent().inject(this);
    }
    @Override
    public void subscribe(ObservableEmitter<List<User>> e) throws Exception {
        User user = Prefs.User.getFromJson(User.class);
        UserResponse users = mITecService.getUsers("section,eq," + user.mSection).execute().body();
        e.onNext(users.mUsers);
        e.onComplete();
    }

    public static Observable<List<User>> create() {
        return Observable.create(new LoadSectionUserObservable());
    }
}
