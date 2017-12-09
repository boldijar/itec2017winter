package com.gym.app.parts.home;

import com.gym.app.data.model.Event;

/**
 * @author Paul
 * @since 2017.10.25
 */

public interface HomeNavigator {

    void goToSectionDashboard();

    void goToFindCourses();

    void goToShop();

    void goToProfile();

    void logout();

    void goToEventDetails(Event event);
}
