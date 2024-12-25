package org.grupogjl.controller.game;


import org.grupogjl.gui.GeneralGui;
import org.grupogjl.model.game.elements.Mario;
import org.grupogjl.model.game.elements.camera.Camera;
import org.grupogjl.model.game.elements.generalobjects.GameObject;
import org.grupogjl.model.game.elements.level.Level;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class TestControllerLevel {
    @Test
    void returnsControllerMarioInstance() {
        ControllerLevel controllerLevel = new ControllerLevel();
        assertNotNull(controllerLevel.getControllerMario());
    }

    @Test
    void returnsControllerEnemyInstance() {
        ControllerLevel controllerLevel = new ControllerLevel();
        assertNotNull(controllerLevel.getControllerEnemy());
    }

    @Test
    void returnsControllerBlocksInstance() {
        ControllerLevel controllerLevel = new ControllerLevel();
        assertNotNull(controllerLevel.getControllerBlocks());
    }

    @Test
    void returnsControllerSurprisesInstance() {
        ControllerLevel controllerLevel = new ControllerLevel();
        assertNotNull(controllerLevel.getControllerSurprises());
    }

    @Test
    void returnsControllerFireBallInstance() {
        ControllerLevel controllerLevel = new ControllerLevel();
        assertNotNull(controllerLevel.getControllerFireBall());
    }

    @Test
    void stepsControllersAndChecksCollisions() {
        Level levelMock = mock(Level.class);
        GeneralGui.ACTION actionMock = mock(GeneralGui.ACTION.class);
        Mario marioMock = mock(Mario.class);
        Camera cameraMock = mock(Camera.class);
        List<GameObject> objectsMock = mock(List.class);

        when(levelMock.getMario()).thenReturn(marioMock);
        when(levelMock.getCamera()).thenReturn(cameraMock);
        when(levelMock.getObjects()).thenReturn(objectsMock);

        ControllerLevel controllerLevel = new ControllerLevel();
        controllerLevel.step(levelMock, actionMock, 100L);

        verify(levelMock, times(1)).getMario();
        verify(levelMock, times(1)).getCamera();
        verify(levelMock, times(1)).getObjects();
    }

    @Test
    void handlesStepWithNullLevel() {
        ControllerLevel controllerLevel = new ControllerLevel();
        assertThrows(NullPointerException.class, () -> controllerLevel.step(null, GeneralGui.ACTION.UP, 100L));
    }

    @Test
    void handlesStepWithNullAction() {
        Level levelMock = mock(Level.class);
        Mario marioMock = mock(Mario.class);
        Camera cameraMock = mock(Camera.class);
        List<GameObject> objectsMock = mock(List.class);

        when(levelMock.getMario()).thenReturn(marioMock);
        when(levelMock.getCamera()).thenReturn(cameraMock);
        when(levelMock.getObjects()).thenReturn(objectsMock);

        ControllerLevel controllerLevel = new ControllerLevel();
        assertThrows(NullPointerException.class, () -> controllerLevel.step(levelMock, null, 100L));
    }

    @Test
    void handlesStepWithZeroTime() {
        Level levelMock = mock(Level.class);
        GeneralGui.ACTION actionMock = mock(GeneralGui.ACTION.class);
        Mario marioMock = mock(Mario.class);
        Camera cameraMock = mock(Camera.class);
        List objectsMock = mock(List.class);

        when(levelMock.getMario()).thenReturn(marioMock);
        when(levelMock.getCamera()).thenReturn(cameraMock);
        when(levelMock.getObjects()).thenReturn(objectsMock);

        ControllerLevel controllerLevel = new ControllerLevel();
        controllerLevel.step(levelMock, actionMock, 0L);

        verify(levelMock, times(1)).getMario();
        verify(levelMock, times(1)).getCamera();
        verify(levelMock, times(1)).getObjects();
    }
}