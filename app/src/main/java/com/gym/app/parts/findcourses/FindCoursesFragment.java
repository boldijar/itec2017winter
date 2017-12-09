package com.gym.app.parts.findcourses;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.gym.app.R;
import com.gym.app.data.model.Course;
import com.gym.app.data.model.Day;
import com.gym.app.data.model.Event;
import com.gym.app.parts.event.SuggestionActivity;
import com.gym.app.parts.home.BaseHomeFragment;
import com.gym.app.view.EmptyLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Paul
 *
 * @since 2017.10.25
 */

public class FindCoursesFragment extends BaseHomeFragment implements FindCoursesView {

    @BindView(R.id.find_courses_view_pager)
    ViewPager mFindCoursesViewPager;

    @BindView(R.id.find_courses_tab_layout)
    TabLayout mFindCoursesTabLayout;

    @BindView(R.id.find_courses_empty_layout)
    EmptyLayout mEmptyLayout;

    private FindCoursesPresenter mFindCoursesPresenter;
    private DayPagerAdapter mDayPagerAdapter;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        mEmptyLayout.setState(EmptyLayout.State.LOADING);
        initPager();
        initPresenters();
        setListeners();
        mFindCoursesPresenter.initData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mFindCoursesPresenter.destroySubscriptions();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_find_courses;
    }

    @Override
    protected int getTitle() {
        return R.string.find_courses;
    }


    @Override
    public void initDays(List<Day> days,int todayIndex) {
        mDayPagerAdapter.setDaysList(days);
        mFindCoursesViewPager.setCurrentItem(todayIndex);
    }

    @Override
    public void setLoaded() {
        mFindCoursesTabLayout.setVisibility(View.VISIBLE);
        mFindCoursesViewPager.setVisibility(View.VISIBLE);
        mEmptyLayout.setState(EmptyLayout.State.CLEAR);
    }

    @Override
    public void setError() {
        mEmptyLayout.setState(EmptyLayout.State.EMPTY, R.string.could_not_find_courses);
    }

    @Override
    public List<Event> getCoursesForDay(long dayStartTime, long dayEndTime) {
        return mFindCoursesPresenter.getCoursesForDay(dayStartTime, dayEndTime);
    }

    private void initPager() {
        mDayPagerAdapter = new DayPagerAdapter(getChildFragmentManager());
        mFindCoursesViewPager.setAdapter(mDayPagerAdapter);
        mFindCoursesTabLayout.setupWithViewPager(mFindCoursesViewPager);
        mFindCoursesTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        ViewCompat.setElevation(mFindCoursesTabLayout, getResources().getDimension(R.dimen.standard_elevation));
    }

    private void initPresenters() {
        mFindCoursesPresenter = new FindCoursesPresenter(this);
        mFindCoursesPresenter.setTodayTomorrow(getString(R.string.today),
                getString(R.string.tomorrow));
    }

    private void setListeners() {
        mEmptyLayout.setOnRetryListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEmptyLayout.setState(EmptyLayout.State.LOADING);
                mFindCoursesPresenter.initData();
            }
        });
    }
    @OnClick(R.id.fab)
    void fabClick(){
        startActivity(SuggestionActivity.createIntent(getContext()));
    }
}
