package org.grupogjl.model.game.elements.enemies;

import org.grupogjl.model.game.elements.blocks.Pipe;
import org.grupogjl.model.game.elements.generalobjects.GameObject;
import org.grupogjl.model.game.elements.generalobjects.StaticObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TestEnemy {

    private Enemy enemy;
    StaticObject staticObject;
    private final float ENEMY_X = 10;
    private final float ENEMY_Y = 20;
    private final float ENEMY_WIDTH = 30;
    private final float ENEMY_HEIGHT = 40;

    @BeforeEach
    void setUp() {
        enemy = spy(new Goomba(ENEMY_X, ENEMY_Y, ENEMY_WIDTH, ENEMY_HEIGHT));
        staticObject = spy(new Pipe(ENEMY_X, ENEMY_Y, ENEMY_WIDTH, ENEMY_HEIGHT));
    }

    @Test
    void constructorInitializesFieldsCorrectly() {
        assertEquals(ENEMY_X, enemy.getX());
        assertEquals(ENEMY_Y, enemy.getY());
        assertEquals(ENEMY_WIDTH, enemy.getWidth());
        assertEquals(ENEMY_HEIGHT, enemy.getHeight());
        assertEquals(1, enemy.getLives());
    }

    @Test
    void wasRevealedReturnsCorrectValue() {
        enemy.setRevealed(true);

        assertTrue(enemy.wasRevealed());
    }

    @Test
    void setRevealedUpdatesValue() {
        enemy.setRevealed(true);

        assertTrue(enemy.wasRevealed());
    }

    @Test
    void handleWallCollisionUpdatesPositionAndVelocity() {
        enemy.setVx(0.5f);

        enemy.handleWallColision(5);

        assertEquals(5, enemy.getX());
        assertEquals(-0.5f, enemy.getVx());
    }

    @Test
    void moveLeftSetsNegativeVelocity() {
        enemy.moveLeft();

        assertEquals(-0.2f, enemy.getVx());
    }

    @Test
    void moveRightSetsPositiveVelocity() {
        enemy.moveRight();

        assertEquals(0.2f, enemy.getVx());
    }

    @Test
    void setLivesUpdatesValue() {
        enemy.setLives(3);

        assertEquals(3, enemy.getLives());
    }

    @Test
    void handleCollisionWithEnemyFromRightSetsNegativeVelocity() {
        Enemy enemy = new Goomba(ENEMY_X, ENEMY_Y, ENEMY_WIDTH, ENEMY_HEIGHT);
        enemy.handleCollision(new Goomba(ENEMY_X, ENEMY_Y, ENEMY_WIDTH, ENEMY_HEIGHT), 'R');

        assertEquals(-0.2f, enemy.getVx());
    }

    @Test
    void handleCollisionWithEnemyFromLeftSetsPositiveVelocity() {
        Enemy enemy = new Goomba(ENEMY_X, ENEMY_Y, ENEMY_WIDTH, ENEMY_HEIGHT);
        enemy.handleCollision(new Goomba(ENEMY_X, ENEMY_Y, ENEMY_WIDTH, ENEMY_HEIGHT), 'L');

        assertEquals(0.2f, enemy.getVx());
    }

    @Test
    void handleCollisionWithStaticObjectFromAboveSetsPositionAndVelocity() {
        enemy.handleCollision(staticObject, 'U');

        assertEquals(ENEMY_Y + ENEMY_HEIGHT, enemy.getY());
        assertEquals(0, enemy.getVy());
    }

    @Test
    void handleCollisionWithStaticObjectFromBelowSetsPositionAndVelocity() {
        enemy.handleCollision(staticObject, 'D');

        assertEquals(ENEMY_Y - ENEMY_HEIGHT, enemy.getY());
        assertEquals(0, enemy.getVy());
        assertFalse(enemy.isFalling());
        assertFalse(enemy.isJumping());
    }

    @Test
    void handleCollisionWithStaticObjectFromLeftSetsPositionAndVelocity() {
        when(enemy.getVx()).thenReturn(0.5f);
        enemy.handleCollision(staticObject, 'L');

        assertEquals(ENEMY_X + ENEMY_WIDTH, enemy.getX());
        verify(enemy).setVx(-0.5f);
    }

    @Test
    void handleCollisionWithStaticObjectFromRightSetsPositionAndVelocity() {
        when(enemy.getVx()).thenReturn(0.5f);
        enemy.handleCollision(staticObject, 'R');

        assertEquals(ENEMY_X - ENEMY_WIDTH, enemy.getX());
        verify(enemy).setVx(-0.5f);
    }

    @Test
    void handleInvalidCharacter() {
        enemy.handleCollision(staticObject, 'Z');

        verify(enemy, never()).setVx(anyFloat());
    }

    @Test
    void handleNotExpectedObject(){
        GameObject object = mock(GameObject.class);
        enemy.handleCollision(object, 'U');

        verify(enemy, never()).setVx(anyFloat());
    }
}
