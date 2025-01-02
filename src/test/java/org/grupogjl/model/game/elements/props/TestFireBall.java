package org.grupogjl.model.game.elements.props;

import org.grupogjl.model.game.elements.camera.Camera;
import org.grupogjl.model.game.elements.enemies.Enemy;
import org.grupogjl.model.game.elements.generalobjects.StaticObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TestFireBall {

    private FireBall fireBall;

    @BeforeEach
    public void setUp() {
        fireBall = spy(new FireBall(0, 0));
    }

    @Test
    public void testIsActive() {
        fireBall.setActive(true);
        assertTrue(fireBall.isActive());
    }

    @Test
    void handleWallCollisionSetsXToLeftCamLimitAndStopsMovement() {
        fireBall.handleWallColision(5.0f);
        assertEquals(5.0f, fireBall.getX());
        assertEquals(0, fireBall.getVx());
    }

    @Test
    void getVirtXReturnsCorrectVirtualX() {
        Camera camera = mock(Camera.class);
        when(camera.getLeftCamLimit()).thenReturn(3.0f);
        fireBall.setX(8.0f);
        assertEquals(5.0f, fireBall.getVirtX(camera));
    }

    @Test
    void getVirtYReturnsCurrentY() {
        fireBall.setY(7.0f);
        assertEquals(7.0f, fireBall.getVirtY());
    }

    @Test
    void getImageReturnsFireBallImage() {
        assertEquals("fireBall.png", fireBall.getImage());
    }

    @Test
    void handleCollisionWithStaticObjectFromAboveStopsVerticalMovement() {
        StaticObject staticObject = mock(StaticObject.class);
        when(staticObject.getY()).thenReturn(5.0f);
        fireBall.handleCollision(staticObject, 'U');
        assertEquals(5.0f + fireBall.getHeight(), fireBall.getY());
        assertEquals(0, fireBall.getVy());
    }

    @Test
    void handleCollisionWithStaticObjectFromBelowStopsFallingAndStartsJumping() {
        StaticObject staticObject = mock(StaticObject.class);
        when(staticObject.getY()).thenReturn(5.0f);
        when(staticObject.getHeight()).thenReturn(2.0f);
        fireBall.handleCollision(staticObject, 'D');
        assertEquals(5.0f - 2.0f, fireBall.getY());
        assertFalse(fireBall.isFalling());
        assertEquals(-0.1f, fireBall.getVy());
        assertTrue(fireBall.isJumping());
    }

    @Test
    void handleCollisionWithStaticObjectFromLeftDeactivatesFireBall() {
        StaticObject staticObject = mock(StaticObject.class);
        when(staticObject.getX()).thenReturn(5.0f);
        when(staticObject.getWidth()).thenReturn(2.0f);
        fireBall.handleCollision(staticObject, 'L');
        assertEquals(5.0f + 2.0f, fireBall.getX());
        assertFalse(fireBall.isActive());
    }

    @Test
    void handleCollisionWithStaticObjectFromRightDeactivatesFireBall() {
        StaticObject staticObject = mock(StaticObject.class);
        when(staticObject.getX()).thenReturn(5.0f);
        fireBall.handleCollision(staticObject, 'R');
        assertEquals(5.0f - fireBall.getWidth(), fireBall.getX());
        assertFalse(fireBall.isActive());
    }

    @Test
    void handleCollisionWithEnemyReducesEnemyLivesAndDeactivatesFireBall() {
        Enemy enemy = mock(Enemy.class);
        when(enemy.getLives()).thenReturn(5);
        fireBall.handleCollision(enemy, ' ');
        verify(enemy).setLives(3);
        assertFalse(fireBall.isActive());
    }

    @Test
    void handleCollisionUnrecognizedCharDoesNothing() {
        fireBall.handleCollision(mock(StaticObject.class), ' ');
        verify(fireBall, never()).setY(anyFloat());
        verify(fireBall, never()).setX(anyFloat());
        verify(fireBall, never()).setActive(anyBoolean());
    }
}
