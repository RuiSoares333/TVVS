package org.grupogjl.state;

import org.grupogjl.Game;
import org.grupogjl.controller.Controller;
import org.grupogjl.controller.ControllerPause;
import org.grupogjl.gui.GeneralGui;
import org.grupogjl.gui.LanternaGui;
import org.grupogjl.model.game.elements.pause.PauseModel;
import org.grupogjl.viewer.Viewer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class TestStatePause {

    private StatePause statePause;
    private StateGame parent;

    @BeforeEach
    void setUp() {
        parent = mock(StateGame.class);
        statePause = spy(new StatePause(parent));
    }

    @Test
    void statePauseInitializesControllerAndViewer() {
        assertNotNull(statePause.getController());
        assertNotNull(statePause.getViewer());
    }

    @Test
    void getModelReturnsPauseModel() {
        assertNotNull(statePause.getModel());
        assertInstanceOf(PauseModel.class, statePause.getModel());
    }

    @Test
    void getStateReturnsCorrectState() {
        assertEquals(3, statePause.getState());
    }

    @Test
    void getParentReturnsParentStateGame() {
        assertEquals(parent, statePause.getParent());
    }

    @Test
    void stepCallsControllerAndViewerAndParentDraw() throws IOException {
        Controller controller = mock(ControllerPause.class);
        Viewer viewer = mock(Viewer.class);
        Game game = mock(Game.class);
        LanternaGui gui = mock(LanternaGui.class);
        statePause.setController(controller);
        statePause.setViewer(viewer);
        when(statePause.getController()).thenReturn(controller);
        when(game.getStatePause()).thenReturn(statePause);
        when(gui.getNextAction()).thenReturn(GeneralGui.ACTION.NONE);

        statePause.step(game, gui, 0L);

        verify(gui).getNextAction();
        verify(statePause.getController()).step(eq(game), eq(GeneralGui.ACTION.NONE), eq(0L));
        verify(parent).draw(eq(gui));
        verify(statePause.getViewer()).draw(eq(statePause), eq(gui));
    }
}
