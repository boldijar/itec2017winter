package com.gym.app.parts.authentication;


/**
 * Interface for communication between the RegisterFragment, LoginFragment and AuthenticationActivity
 *
 *
 * @since 2017.10.25
 */
public interface AuthenticationNavigation {

    void goToRegister();

    void goBack();
}
