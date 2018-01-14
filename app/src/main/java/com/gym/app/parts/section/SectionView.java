package com.gym.app.parts.section;

import com.gym.app.data.model.Message;
import com.gym.app.data.model.User;

import java.util.List;

/**
 * @author Paul
 * @since 2017.12.09
 */

public interface SectionView {

    void showUsers(List<User> users);

    void showErrorGettingUsers();

    void showMessage(List<Message> value);

    void addMessage(Message message);
}
