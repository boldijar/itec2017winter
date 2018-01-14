package com.joggo;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.joggo.data.Prefs;
import com.joggo.di.ApiModule;
import com.joggo.di.ApplicationComponent;
import com.joggo.di.ApplicationModule;
import com.joggo.di.DaggerApplicationComponent;
import com.joggo.di.InjectionHelper;

import timber.log.Timber;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class Shaorma extends Application {

    private ApplicationComponent mApplicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        Prefs.init(this);
        buildGraphAndInject();
        InjectionHelper.init(this);
        initTimber();
        initStetho();
        initCalligraphy();
    }

    private void initCalligraphy() {
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Lato-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());
    }

    private void initStetho() {
        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this);
        }
    }

    private void initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }

    private void buildGraphAndInject() {
        final ApplicationModule applicationModule = new ApplicationModule(this);

        mApplicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(applicationModule)
                .apiModule(new ApiModule(this))
                .build();
        mApplicationComponent.inject(this);
    }

    public ApplicationComponent getApplicationComponent() {
        return mApplicationComponent;
    }
}
