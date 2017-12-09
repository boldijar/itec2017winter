package com.gym.app.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.messaging.FirebaseMessaging;
import com.gym.app.R;
import com.gym.app.data.Prefs;
import com.gym.app.data.model.Event;
import com.gym.app.data.model.User;
import com.gym.app.di.InjectionHelper;
import com.gym.app.parts.confirmation.ConfirmationFragment;
import com.gym.app.parts.event.EventFragment;
import com.gym.app.parts.findcourses.FindCoursesFragment;
import com.gym.app.parts.home.BaseHomeFragment;
import com.gym.app.parts.home.HomeNavigator;
import com.gym.app.parts.profile.ProfileFragment;
import com.gym.app.parts.section.SectionFragment;
import com.gym.app.parts.shop.ShopFragment;
import com.gym.app.server.ITecService;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * @author Paul
 * @since 2017.08.29
 */

public class HomeActivity extends BaseActivity implements HomeNavigator {

    private static final String ARG_EVENT = "argevent";

    @BindView(R.id.home_drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    private ActionBarDrawerToggle mDrawerToggle;

    @Inject
    ITecService mITecService;

    public static Intent createIntent(Context context) {
        return new Intent(context, HomeActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        InjectionHelper.getApplicationComponent().inject(this);
        ButterKnife.bind(this);
        initDrawer();
        if (!Prefs.ProfileCreated.getBoolean(false)) {
            goToProfile();
        } else {
            goToSectionDashboard();
        }
        onNewIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        final int eventId = intent.getIntExtra(ARG_EVENT, -1);
        if (eventId == -1) {
            return;
        }

        User user = Prefs.User.getFromJson(User.class);
        mITecService.getEvents(user.mSection)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Event>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<Event> value) {
                        for (Event event : value) {
                            if (event.mId == eventId) {
                                goToEventDetails(event);
                                return;
                            }
                        }
                        Timber.e("Not found any event with id " + eventId);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e);
                    }

                    @Override
                    public void onComplete() {

                    }


                });
    }

    private void initDrawer() {
        setSupportActionBar(mToolbar);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.appname, R.string.appname) {

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.addDrawerListener(mDrawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void setFragment(BaseHomeFragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.home_container, fragment)
                .commit();
        mDrawerLayout.closeDrawer(Gravity.START);
    }

    @Override
    public void goToSectionDashboard() {
        setFragment(new SectionFragment());
    }

    @Override
    public void goToFindCourses() {
        setFragment(new FindCoursesFragment());
    }

    @Override
    public void goToShop() {
        setFragment(new ShopFragment());
    }

    @Override
    public void goToProfile() {
        setFragment(new ProfileFragment());
    }

    @Override
    public void logout() {
        User user = Prefs.User.getFromJson(User.class);
        FirebaseMessaging.getInstance().unsubscribeFromTopic(user.mId + "");
        Prefs.User.put(null);
        Intent intent = SplashActivity.createIntent(this);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void goToEventDetails(Event event) {
        EventFragment eventFragment = new EventFragment();
        eventFragment.setEvent(event);
        setFragment(eventFragment);
    }

    @Override
    public void goToConfirmations() {
        setFragment(new ConfirmationFragment());
    }

    public static Intent createIntent(Context context, int eventId) {
        Intent intent = new Intent(context, HomeActivity.class);
        intent.putExtra(ARG_EVENT, eventId);
        return intent;
    }
}
