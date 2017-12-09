package com.gym.app.parts.confirmation;

import com.gym.app.di.InjectionHelper;
import com.gym.app.presenter.Presenter;
import com.gym.app.utils.MvpObserver;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * TODO: Class description
 *
 * @author Paul
 * @since 2017.12.09
 */

public class ConfirmationPresenter extends Presenter<ConfirmationView> {


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
}
