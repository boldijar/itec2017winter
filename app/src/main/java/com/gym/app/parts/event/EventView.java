package com.gym.app.parts.event;

import com.gym.app.data.model.Message;

import java.util.List;

/**
 * TODO: Class description
 *
 * @author Paul
 * @since 2017.12.09
 */

public interface EventView {

    void showMessages(List<Message> messages);

    void addMessage(Message message);

    void checkinSuccess();

    void checkoutSuccess();
}
