package org.grupogjl.model.game.elements.camera;

import org.grupogjl.model.game.elements.Mario;
import org.grupogjl.model.game.elements.enemies.Enemy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class TestCamera {

    private Camera camera;

    @BeforeEach
    public void setUp() {
        camera = new Camera();
    }

    @Test
    void cameraInitializationSetsCorrectLimits() {
        assertEquals(0, camera.getLeftCamLimit());
        assertEquals(26, camera.getRightCamLimit());
    }

    @Test
    void updateCameraMovesCameraWhenMarioIsFarEnough() {
        Mario mario = mock(Mario.class);
        when(mario.getX()).thenReturn(20.0f);
        camera.updateCamera(mario);
        assertEquals(7.0f, camera.getLeftCamLimit());
        assertEquals(33.0f, camera.getRightCamLimit());
    }

    @Test
    void updateCameraDoesNotMoveCameraWhenMarioIsClose() {
        Mario mario = mock(Mario.class);
        when(mario.getX()).thenReturn(10.0f);
        camera.updateCamera(mario);
        assertEquals(0, camera.getLeftCamLimit());
        assertEquals(26, camera.getRightCamLimit());
    }

    @Test
    void isEnemyOnCamRevealsEnemyWhenOnCamera() {
        Enemy enemy = mock(Enemy.class);
        when(enemy.getX()).thenReturn(10.0f);
        when(enemy.getWidth()).thenReturn(1.0f);
        when(enemy.wasRevealed()).thenReturn(false);
        camera.isEnemyOnCam(enemy);
        verify(enemy).setRevealed(true);
    }

    @Test
    void isEnemyOnCamDoesNotRevealEnemyWhenOffCamera() {
        Enemy enemy = mock(Enemy.class);
        when(enemy.getX()).thenReturn(30.0f);
        when(enemy.getWidth()).thenReturn(1.0f);
        when(enemy.wasRevealed()).thenReturn(false);
        camera.isEnemyOnCam(enemy);
        verify(enemy, never()).setRevealed(true);
    }


    @Test
    void isEnemyOnCamDoesNotRevealEnemyWhenAlreadyRevealed() {
        Enemy enemy = mock(Enemy.class);
        when(enemy.getX()).thenReturn(10.0f);
        when(enemy.getWidth()).thenReturn(1.0f);
        when(enemy.wasRevealed()).thenReturn(true);
        camera.isEnemyOnCam(enemy);
        verify(enemy, never()).setRevealed(true);
    }

    @Test
    void isEnemyOnCamRevealsEnemyWhenOnCameraAndWasRevealed() {
        Enemy enemy = mock(Enemy.class);
        when(enemy.getX()).thenReturn(-10.0f);
        when(enemy.getWidth()).thenReturn(1.0f);
        when(enemy.wasRevealed()).thenReturn(false);
        camera.isEnemyOnCam(enemy);
        verify(enemy, never()).setRevealed(true);
    }
}
