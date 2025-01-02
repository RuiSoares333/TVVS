package org.grupogjl.state;

import org.grupogjl.Game;
import org.grupogjl.controller.Controller;
import org.grupogjl.gui.GeneralGui;
import org.grupogjl.gui.LanternaGui;
import org.grupogjl.model.game.elements.menu.MenuModel;
import org.grupogjl.viewer.Viewer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TestStateMenu {

    private StateMenu stateMenu;

    @BeforeEach
    void setUp() {
        stateMenu = spy(new StateMenu());
    }

    @Test
    void stateMenuInitializesControllerAndViewer() {
        assertNotNull(stateMenu.getController());
        assertNotNull(stateMenu.getViewer());
    }

    @Test
    void getModelReturnsMenuModel() {
        assertNotNull(stateMenu.getModel());
        assertInstanceOf(MenuModel.class, stateMenu.getModel());
    }

    @Test
    void getStateReturnsCorrectState() {
        assertEquals(1, stateMenu.getState());
    }

    @Test
    void stepCallsControllerAndViewer() throws IOException {
        Game game = mock(Game.class);
        LanternaGui gui = mock(LanternaGui.class);
        Controller controller = mock(Controller.class);
        Viewer viewer = mock(Viewer.class);

        when(game.getStateMenu()).thenReturn(stateMenu);
        when(gui.getNextAction()).thenReturn(GeneralGui.ACTION.NONE);
        stateMenu.setController(controller);
        stateMenu.setViewer(viewer);

        stateMenu.step(game, gui, 0L);

        verify(gui).getNextAction();
        verify(stateMenu.getController()).step(eq(game), eq(GeneralGui.ACTION.NONE), eq(0L));
        verify(stateMenu.getViewer()).draw(eq(stateMenu), eq(gui));
    }
}
