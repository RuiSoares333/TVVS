package org.grupogjl.viewer;

import org.grupogjl.gui.GeneralGui;
import org.grupogjl.model.game.elements.Mario;
import org.grupogjl.model.game.elements.generalobjects.GameObject;
import org.grupogjl.model.game.elements.camera.Camera;
import org.grupogjl.model.game.elements.level.Level;
import org.grupogjl.state.StateGame;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Collections;

import static org.mockito.Mockito.*;

public class TestViewerGame extends SuperTestViewer {

    private StateGame stateGame;
    private Mario mario;
    private Camera camera;
    private Level level;
    private GameObject object;

    void initMocks(){
        viewer = new ViewerGame();
        gui = mock(GeneralGui.class);
        stateGame = mock(StateGame.class);
        mario = mock(Mario.class);
        camera = mock(Camera.class);
        level = mock(Level.class);
        object = mock(GameObject.class);
    }

    void initReturns(){
        when(stateGame.getModel()).thenReturn(level);
        when(level.getMario()).thenReturn(mario);
        when(level.getCamera()).thenReturn(camera);
        when(level.getObjects()).thenReturn(Collections.singletonList(object));
        when(camera.getLeftCamLimit()).thenReturn(0f);
        when(camera.getRightCamLimit()).thenReturn(10f);
        when(object.getImage()).thenReturn("image");
        when(mario.getImage()).thenReturn("mario");
    }

    @Test
    void drawClearsGui() throws IOException {
        when(level.getObjects()).thenReturn(Collections.emptyList());

        viewer.draw(stateGame, gui);

        verify(gui).clear();
    }

    @Test
    void drawDisplaysGameOverWhenGameIsOver() throws IOException {
        when(stateGame.isGameOver()).thenReturn(true);

        viewer.draw(stateGame, gui);

        verify(gui).drawGameOver();
    }

    @Test
    void drawDisplaysGameObjectsWithinCameraLimits() throws IOException {
        when(object.getX()).thenReturn(5f);

        viewer.draw(stateGame, gui);

        verify(gui).drawImage(0f, 0f, "image");
    }

    @Test
    void drawDisplaysMarioImage() throws IOException {
        when(level.getObjects()).thenReturn(Collections.emptyList());

        viewer.draw(stateGame, gui);

        verify(gui).drawImage(0f, 0f, "mario");
    }

    @Test
    void drawNothingBecauseOutOfCameraXLeft() throws IOException {
        when(object.getX()).thenReturn(-15f);

        viewer.draw(stateGame, gui);

        verify(gui).drawImage(anyFloat(), anyFloat(), any());
    }

    @Test
    void drawNothingBecauseOutOfCameraXRight() throws IOException {
        when(object.getX()).thenReturn(15f);

        viewer.draw(stateGame, gui);

        verify(gui).drawImage(anyFloat(), anyFloat(), any());
    }

    @Test
    void drawDisplaysMarioStats() throws IOException {
        when(mario.getCoins()).thenReturn(10);
        when(mario.getLives()).thenReturn(3);

        viewer.draw(stateGame, gui);

        verify(gui).drawMenuText(1, 8, "coins: 10");
        verify(gui).drawMenuText(1, 24, "lives: 3");
    }
}
