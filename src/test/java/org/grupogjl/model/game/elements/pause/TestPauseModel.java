package org.grupogjl.model.game.elements.pause;

import org.grupogjl.model.game.elements.buttons.Button;
import org.junit.jupiter.api.Test;

import java.util.Vector;

public class TestPauseModel {

    @Test
    void testPauseModel() {
        PauseModel pauseModel = new PauseModel();
        Vector<Button> buttons = pauseModel.getButtons();
        assert buttons.size() == 2;
    }
}
