package org.grupogjl.model.game.elements.enemies;

import org.grupogjl.model.game.elements.camera.Camera;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class TestKoopaTroopa {

    private KoopaTroopa koopa;
    private final float ENEMY_X = 10;
    private final float ENEMY_Y = 20;
    private final float ENEMY_WIDTH = 30;
    private final float ENEMY_HEIGHT = 40;
    private final float KOOPA_DISPLACEMENT = 0.3125f;

    @BeforeEach
    void setUp() {
        koopa = spy(new KoopaTroopa(ENEMY_X, ENEMY_Y, ENEMY_WIDTH, ENEMY_HEIGHT));
    }

    @Test
    void moveLeftWhenLiveIsTwo() {
        koopa.moveLeft();
        assertEquals(-0.2f, koopa.getVx());
    }

    @Test
    void moveLeftWhenLiveIsNotTwo() {
        koopa.setLives(1);
        koopa.moveLeft();
        assertEquals(-2f, koopa.getVx());
    }

    @Test
    void moveRightWhenLiveIsTwo() {
        koopa.moveRight();
        assertEquals(0.2f, koopa.getVx());
    }

    @Test
    void moveRightWhenLiveIsNotTwo() {
        koopa.setLives(1);
        koopa.moveRight();
        assertEquals(2f, koopa.getVx());
    }

    @Test
    void getVirtX(){
        Camera camera = mock(Camera.class);
        when(camera.getLeftCamLimit()).thenReturn(5.0f);
        assertEquals(5.0f, koopa.getVirtX(camera));
    }

    @Test
    void getVirtYWhenLiveIsOne(){
        koopa.setLives(1);
        assertEquals(ENEMY_Y, koopa.getVirtY());
    }

    @Test
    void getVirtYWhenLiveIsNotOne(){
        assertEquals(ENEMY_Y - KOOPA_DISPLACEMENT, koopa.getVirtY());
    }

    @Test
    void getImageWhenLiveIsOne(){
        koopa.setLives(1);
        assertEquals("koopaShell.png", koopa.getImage());
    }

    @Test
    void getImageWhenLiveIsNotOne(){
        assertEquals("koopaTroopa.png", koopa.getImage());
    }
}
