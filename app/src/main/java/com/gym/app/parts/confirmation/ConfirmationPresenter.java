package com.gym.app.parts.confirmation;

import com.gym.app.data.model.ChangeTimeRequest;
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

public class ConfirmationPresenter extends Presenter<ConfirmationView> {

    @Inject
    ITecService mITecService;

    public ConfirmationPresenter(ConfirmationView view) {
        super(view);
        InjectionHelper.getApplicationComponent().inject(this);
    }

    public void loadConfirmations() {
        ConfirmationObservable.create()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MvpObserver<List<ConfirmationModel>>(this) {
                    @Override
                    public void onNext(List<ConfirmationModel> value) {
                        getView().showModels(value);
                    }
                });
    }

    public void confirmChange(final ChangeTimeRequest request) {
        mITecService.confirmChange(request.mId, request.mUserId, request.mEventId, request.mNewTime)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action() {
                    @Override
                    public void run() throws Exception {
                        getView().showChanged(request.mId);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Timber.e(throwable);
                    }
                });
    }
}
