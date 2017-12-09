package com.gym.app.parts.confirmation;

import java.util.List;

/**
 * TODO: Class description
 *
 * @author Paul
 * @since 2017.12.09
 */

public interface ConfirmationView {

    void showModels(List<ConfirmationModel> models);

    void showChanged(int id);
}
