package org.grupogjl.state;

import org.grupogjl.Game;
import org.grupogjl.gui.GeneralGui;
import org.grupogjl.gui.LanternaGui;
import org.grupogjl.model.game.elements.level.Level;
import org.grupogjl.viewer.Viewer;
import org.grupogjl.viewer.ViewerGame;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static java.lang.System.exit;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TestStateGame {

    private StateGame stateGame;
    private Game game;
    private Level level;
    private LanternaGui gui;
    private Viewer viewer;

    @BeforeEach
    void setUp() throws IOException {
        stateGame = new StateGame();
        game = mock(Game.class);
        level = mock(Level.class);
        gui = mock(LanternaGui.class);
        viewer = mock(ViewerGame.class);

        when(game.getStateGame()).thenReturn(stateGame);
        when(gui.getNextAction()).thenReturn(GeneralGui.ACTION.NONE);
        stateGame.setViewer(viewer);
    }

    @Test
    void constructorInitializesLevelAndMarioCorrectly() {
        assertNotNull(stateGame.getLevel());
        assertNotNull(stateGame.getModel().getMario());
    }

    @Test
    void getLevelReturnsCurrentLevel() {
        stateGame.setLevel(level);
        assertEquals(level, stateGame.getLevel());
    }

    @Test
    void getLevelnReturnsCurrentLevelNumber() throws IOException {
        assertEquals(1, stateGame.getLeveln());
    }

    @Test
    void stepUpdatesGameAndView() throws IOException {
        stateGame.step(game, gui, 0L);
        verify(gui).refresh();
    }

    @Test
    void drawCallsViewerDraw() throws IOException {
        stateGame.step(game, gui, 0L);
        verify(gui).refresh();
    }

    @Test
    void drawCallsViewerDrawSuccessfully() throws IOException {
        stateGame.draw(gui);
        verify(viewer).draw(stateGame, gui);
    }

    @Test
    void drawThrowsRuntimeExceptionOnIOException() throws IOException {
        doThrow(IOException.class).when(viewer).draw(any(StateGame.class), any(GeneralGui.class));
        assertThrows(RuntimeException.class, () -> stateGame.draw(gui));
    }

    @Test
    void getStateReturnsCorrectState() {
        assertEquals(2, stateGame.getState());
    }

    @Test
    void isGameOverReturnsFalseInitially() {
        assertFalse(stateGame.isGameOver());
    }

    @Test
    void setGameOverSetsGameOverToTrue() {
        stateGame.setGameOver(true);
        assertTrue(stateGame.isGameOver());
    }

    @Test
    void setGameOverSetsGameOverToFalse() {
        stateGame.setGameOver(false);
        assertFalse(stateGame.isGameOver());
    }

    @Test
    void resetLevelInitializesNewLevelAndResetsMario() throws IOException {
        stateGame.resetLevel();
        assertNotNull(stateGame.getLevel());
        assertEquals(0, stateGame.getModel().getMario().getX());
        assertEquals(0, stateGame.getModel().getMario().getY());
        assertEquals(stateGame.getModel().getMario(), stateGame.getLevel().getMario());
    }

    @Test
    void nextLevelIncrementsLevelNumberAndResetsMarioPosition() throws IOException {
        stateGame.nextLevel();
        assertEquals(2, stateGame.getLeveln());
        assertNotNull(stateGame.getLevel());
        assertEquals(0, stateGame.getModel().getMario().getX());
        assertEquals(0, stateGame.getModel().getMario().getY());
    }

    @Test
    void nextLevelAssociatesMarioWithNewLevel() throws IOException {
        stateGame.nextLevel();
        assertEquals(stateGame.getModel().getMario(), stateGame.getLevel().getMario());
    }
}
