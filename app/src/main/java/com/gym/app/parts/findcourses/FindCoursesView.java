package com.gym.app.parts.findcourses;

import com.gym.app.data.model.Course;
import com.gym.app.data.model.Day;
import com.gym.app.data.model.Event;

import java.util.List;

/**
 * Contains UI related method for the FindCoursesFragment
 *
 *
 * @since 2017.10.31
 */

public interface FindCoursesView {

    void initDays(List<Day> days,int todayIndex);

    void setLoaded();

    void setError();

    List<Event> getCoursesForDay(long dayStartTime, long dayEndTime);
}
