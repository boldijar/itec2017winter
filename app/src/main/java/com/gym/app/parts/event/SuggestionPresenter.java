package com.gym.app.parts.event;

import com.gym.app.data.model.Event;
import com.gym.app.data.model.LessonResponse;
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
 * TODO: Class description
 *
 * @author Paul
 * @since 2017.12.09
 */

public class SuggestionPresenter extends Presenter<SuggestionView> {

    @Inject
    ITecService mITecService;

    public SuggestionPresenter(SuggestionView view) {
        super(view);
        InjectionHelper.getApplicationComponent().inject(this);
    }

    public void loadLessons() {
        mITecService.getLessons()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MvpObserver<LessonResponse>(this) {
                    @Override
                    public void onNext(LessonResponse value) {
                        getView().showLessons(value.mLessonList);
                    }
                });
    }

    public void requestAddEvent(Event event) {
        mITecService.addEventRequest(event)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action() {
                    @Override
                    public void run() throws Exception {
                        getView().done();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Timber.e(throwable);
                    }
                });
    }
}
