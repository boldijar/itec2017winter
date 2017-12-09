package com.gym.app.parts.event;

import com.gym.app.data.model.Lesson;

import java.util.List;

/**
 * @author Paul
 * @since 2017.12.09
 */

public interface SuggestionView {

    void showLessons(List<Lesson> lessonList);
}
