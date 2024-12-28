package org.grupogjl.controller.game.physicalobjects;

import org.grupogjl.model.game.elements.camera.Camera;
import org.grupogjl.model.game.elements.enemies.Enemy;
import org.grupogjl.model.game.elements.level.Level;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class TestControllerEnemy {

    private ControllerEnemy controller;
    private Level level;
    private Camera camera;
    private List<Enemy> enemies;
    private Enemy enemy1;
    private Enemy enemy2;

    @BeforeEach
    void setUp() {
        controller = spy(new ControllerEnemy());
        level = mock(Level.class);
        camera = mock(Camera.class);
        enemy1 = mock(Enemy.class);
        enemy2 = mock(Enemy.class);
        enemies = Arrays.asList(enemy1, enemy2);

        when(level.getEnemies()).thenReturn(new ArrayList<>(enemies));
        when(level.getCamera()).thenReturn(camera);
    }

    @Test
    void stepMoveEnemiesCalled(){
        when(enemy1.getLives()).thenReturn(1);
        when(enemy2.getLives()).thenReturn(1);

        controller.step(level, 100L);

        verify(controller).moveEnemies(enemies, camera);
    }

    @Test
    void moveEnemiesIsEnemyOnCamCalled(){
        when(enemy1.getLives()).thenReturn(1);
        when(enemy2.getLives()).thenReturn(1);

        controller.step(level, 100L);

        verify(camera, atLeastOnce()).isEnemyOnCam(any());
    }

    @Test
    void stepUpdatesStatusAndMovesEnemies() {
        when(enemy1.getLives()).thenReturn(1);
        when(enemy2.getLives()).thenReturn(1);

        controller.step(level, 100L);

        verify(level, times(2)).getEnemies();
        verify(level).getCamera();
        verify(level).setEnemies(enemies);
    }

    @Test
    void updateStatusRemovesEnemiesWithZeroOrLessLives() {
        when(enemy1.getLives()).thenReturn(0);
        when(enemy2.getLives()).thenReturn(1);

        controller.updateStatus(level, 100L);

        verify(level).setEnemies(argThat(list -> list.size() == 1 && list.contains(enemy2)));
    }

    @Test
    void updateStatusUpdatesLocationOfRevealedEnemies() {
        when(enemy1.wasRevealed()).thenReturn(true);

        controller.updateStatus(level, 100L);

        verify(enemy1).updateLocation();
    }

    @Test
    void moveEnemiesMovesLeftWhenVxIsNonPositive() {
        when(enemy1.wasRevealed()).thenReturn(true);
        when(enemy1.isFalling()).thenReturn(false);
        when(enemy1.getVx()).thenReturn(0f);

        controller.moveEnemies(enemies, camera);

        verify(enemy1).moveLeft();
    }

    @Test
    void moveEnemiesMovesRightWhenVxIsPositive() {
        when(enemy1.wasRevealed()).thenReturn(true);
        when(enemy1.isFalling()).thenReturn(false);
        when(enemy1.getVx()).thenReturn(1f);

        controller.moveEnemies(enemies, camera);

        verify(enemy1).moveRight();
    }

    @Test
    void moveEnemiesDoesNotMoveWhenFalling() {
        when(enemy1.wasRevealed()).thenReturn(true);
        when(enemy1.isFalling()).thenReturn(true);

        controller.moveEnemies(enemies, camera);

        verify(enemy1, never()).moveLeft();
        verify(enemy1, never()).moveRight();
    }

    @Test
    void stepHandlesNullLevel() {
        ControllerEnemy controller = new ControllerEnemy();
        assertThrows(NullPointerException.class, () -> controller.step(null, 100L));
    }
}
