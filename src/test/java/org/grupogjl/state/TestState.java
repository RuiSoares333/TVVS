package org.grupogjl.state;

import org.grupogjl.controller.Controller;
import org.grupogjl.controller.ControllerGame;
import org.grupogjl.viewer.Viewer;
import org.grupogjl.viewer.ViewerGame;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

public class TestState {

    @Test
    void testState() throws IOException {
        State a = new StateGame();
        Controller controller = mock(Controller.class);
        Viewer viewer = mock(Viewer.class);

        a.setController(controller);
        a.setViewer(viewer);

        assertEquals(controller, a.getController());
        assertEquals(viewer, a.getViewer());
    }
}
