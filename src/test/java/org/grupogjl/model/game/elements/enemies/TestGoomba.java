package org.grupogjl.model.game.elements.enemies;

import org.grupogjl.model.game.elements.camera.Camera;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class TestGoomba {

    private Camera camera;
    private Goomba goomba;

    @BeforeEach
    void setUp() {
        camera = mock(Camera.class);
        goomba = spy(new Goomba(10.0f, 0.0f, 1.0f, 1.0f));
    }

    @Test
    void getVirtXReturnsCorrectVirtualX() {
        when(camera.getLeftCamLimit()).thenReturn(5.0f);

        assertEquals(5.0f, goomba.getVirtX(camera));
    }

    @Test
    void getVirtYReturnsCorrectVirtualY() {
        assertEquals(0.0f, goomba.getVirtY());
    }

    @Test
    void getImageReturnsCorrectImage() {
        assertEquals("goomba.png", goomba.getImage());
    }
}
