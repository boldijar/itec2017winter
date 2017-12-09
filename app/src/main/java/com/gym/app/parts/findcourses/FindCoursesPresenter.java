package com.gym.app.parts.findcourses;

import android.annotation.SuppressLint;

import com.gym.app.data.Prefs;
import com.gym.app.data.SystemUtils;
import com.gym.app.data.model.Day;
import com.gym.app.data.model.Event;
import com.gym.app.data.model.User;
import com.gym.app.data.room.AppDatabase;
import com.gym.app.di.InjectionHelper;
import com.gym.app.presenter.Presenter;
import com.gym.app.server.ITecService;
import com.gym.app.utils.MvpObserver;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Presenter for the FindCoursesFragment
 *
 * @author catalinradoiu
 * @since 2017.10.31
 */
public class FindCoursesPresenter extends Presenter<FindCoursesView> {

    private static final String DAY_NAME_FORMAT = "EEEE, d.MM.yyyy";
    private static final long FIVE_DAYS_TIMESTAMP = 5 * 24 * 3600 * 1000;
    private static final long ONE_DAY_TIMESTAMP = 24 * 3600 * 1000;
    private static final int DAYS_NUMBER = 30;

    @Inject
    ITecService mService;

    @Inject
    SystemUtils mSystemUtils;

    @Inject
    AppDatabase mAppDatabase;

    private List<Event> mEvents;
    private List<Day> mDaysList;
    private String mToday;
    private String mTomorrow;

    FindCoursesPresenter(FindCoursesView view) {
        super(view);
        InjectionHelper.getApplicationComponent().inject(this);
        mEvents = new ArrayList<>();
        mDaysList = new ArrayList<>();
    }

    int todayIndex = 0;

    void initData() {
        //Initialize the days
        mDaysList = generateDaysList();
        getView().initDays(mDaysList, todayIndex);

        //Initialize the courses
        loadCourses();
    }

    List<Event> getCoursesForDay(long dayStartTime, long dayEndTime) {
        dayStartTime *= 1000;
        dayEndTime *= 1000;
        List<Event> result = new ArrayList<>();
        for (Event event : mEvents) {
            if (event.mTime >= dayStartTime && event.mTime <= dayEndTime) {
                result.add(event);
            }
        }
        return result;
    }

    /**
     * Set the mToday and mTomorrow names in order to support internationalisation
     * Should be called from the corresponding view where it is used with the corresponding string values
     *
     * @param today    the name of mToday day in the language of the device
     * @param tomorrow the name of mTomorrow day in the language of the device
     */
    void setTodayTomorrow(String today, String tomorrow) {
        this.mToday = today;
        this.mTomorrow = tomorrow;
    }

    private void loadCourses() {
        User user = Prefs.User.getFromJson(User.class);
        mService.getEvents(user.mSection)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MvpObserver<List<Event>>(this) {
                    @Override
                    public void onNext(List<Event> value) {
                        getView().setLoaded();
                        mEvents.addAll(value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        getView().setError();
                    }
                });
    }


    final SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");

    private List<Day> generateDaysList() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat =
                new SimpleDateFormat(DAY_NAME_FORMAT);
        List<Day> days = new ArrayList<>();
        Calendar currentCalendar = Calendar.getInstance();
        currentCalendar.add(Calendar.DATE, -5);
        long currentDayStartTime = currentCalendar.getTimeInMillis();
        String currentDayName = dateFormat.format(currentCalendar.getTime());
        currentCalendar.set(Calendar.HOUR_OF_DAY, 0);
        currentCalendar.set(Calendar.MINUTE, 0);
        currentCalendar.set(Calendar.SECOND, 0);
        currentCalendar.set(Calendar.MILLISECOND, 0);
        long currentDayEndTime = currentCalendar.getTimeInMillis() + ONE_DAY_TIMESTAMP - 1;
        for (int i = 0; i < DAYS_NUMBER; i++) {
            //For the day, the milliseconds are converted to seconds

            String currentStartTimeString = fmt.format(new Date(currentDayStartTime));
            String nowTimeString = fmt.format(new Date());
            if (nowTimeString.equals(currentStartTimeString)) {
                todayIndex = i;
            }
            days.add(new Day(currentDayName, currentDayStartTime / 1000, currentDayEndTime / 1000));
            currentDayStartTime = currentDayEndTime + 1;
            currentDayEndTime += ONE_DAY_TIMESTAMP;
            currentDayName = dateFormat.format(new Date(currentDayStartTime));

        }
        return days;
    }
}
