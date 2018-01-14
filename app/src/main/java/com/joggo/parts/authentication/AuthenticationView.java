package com.joggo.parts.authentication;

import com.joggo.data.model.User;

/**
 * Contains only UI related methods for login
 *
 * @author Paul
 * @since 2017.10.29
 */

interface AuthenticationView {

    void showLoginResponse(User user);

    void showError(Throwable throwable);
}
