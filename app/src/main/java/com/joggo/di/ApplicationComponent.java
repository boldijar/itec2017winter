package com.joggo.di;

import com.joggo.Shaorma;
import com.joggo.activities.HomeActivity;
import com.joggo.parts.authentication.AuthenticationActivity;
import com.joggo.parts.authentication.AuthenticationPresenter;
import com.joggo.parts.authentication.register.RegisterPresenter;

import dagger.Component;

@Component(modules = {ApplicationModule.class, ApiModule.class})
public interface ApplicationComponent {

    void inject(Shaorma shaorma);

    void inject(AuthenticationPresenter authenticationPresenter);

    void inject(RegisterPresenter registerPresenter);

    void inject(AuthenticationActivity authenticationActivity);

    void inject(HomeActivity homeActivity);

}