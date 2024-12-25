package org.grupogjl.model.game.elements.surprises;

import org.grupogjl.model.game.elements.Mario;
import org.grupogjl.model.game.elements.camera.Camera;
import org.grupogjl.model.game.elements.generalobjects.StaticObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TestMushroom {

    Camera cameraMock;
    Mario marioMock;

    @BeforeEach
    public void setUp() {
        cameraMock = Mockito.mock(Camera.class);
        when(cameraMock.getLeftCamLimit()).thenReturn(5f);
        marioMock = Mockito.mock(Mario.class);
    }

    @Test
    public void testMushroomSuper() {
        MushroomSuper mushroomSuper = new MushroomSuper(1, 1);
        assert mushroomSuper.getVirtX(cameraMock) == -4;
        assert mushroomSuper.getVirtY() == 1;
        assert mushroomSuper.getImage().equals("superMushroom.png");

        when(marioMock.isStateBig()).thenReturn(true);
        when(marioMock.isStateFire()).thenReturn(true);
        mushroomSuper.activate(marioMock);
        verify(marioMock, Mockito.times(0)).setStateBig(true);
        verify(marioMock, Mockito.times(0)).setHeight(2);

        when(marioMock.isStateBig()).thenReturn(true);
        when(marioMock.isStateFire()).thenReturn(false);
        mushroomSuper.activate(marioMock);
        verify(marioMock, Mockito.times(0)).setStateBig(true);
        verify(marioMock, Mockito.times(0)).setHeight(2);

        when(marioMock.isStateBig()).thenReturn(false);
        when(marioMock.isStateFire()).thenReturn(true);
        mushroomSuper.activate(marioMock);
        verify(marioMock, Mockito.times(0)).setStateBig(true);
        verify(marioMock, Mockito.times(0)).setHeight(2);

        when(marioMock.isStateBig()).thenReturn(false);
        when(marioMock.isStateFire()).thenReturn(false);
        mushroomSuper.activate(marioMock);
        verify(marioMock, Mockito.times(1)).setStateBig(true);
        verify(marioMock, Mockito.times(1)).setHeight(2);
    }

    @Test
    public void testMushroom1UP() {
        Mushroom1UP mushroom1UP = new Mushroom1UP(1, 1);
        assert mushroom1UP.getVirtX(cameraMock) == -4;
        assert mushroom1UP.getVirtY() == 1;
        assert mushroom1UP.getImage().equals("lifeMushroom.png");

        when(marioMock.getLives()).thenReturn(1);
        mushroom1UP.activate(marioMock);
        verify(marioMock, Mockito.times(1)).setLives(2);
    }

    @Test
    public void testMushroom(){
        Mushroom mushroom = new MushroomSuper(1, 1);
        StaticObject objectMock = Mockito.mock(StaticObject.class);
        when(objectMock.getX()).thenReturn(5f);
        when(objectMock.getY()).thenReturn(5f);
        when(objectMock.getHeight()).thenReturn(1f);
        when(objectMock.getWidth()).thenReturn(1f);

        mushroom.born();
        assert mushroom.getVx() == 0.2f;
        assert mushroom.isFalling();

        mushroom.handleCollision(null, 'U');
        assert mushroom.getY() == 1;
        mushroom.handleCollision(objectMock, 'X');
        assert mushroom.getY() == 1;

        mushroom.setVy(5);
        mushroom.handleCollision(objectMock, 'U');
        assert mushroom.getY() == 6;
        assert mushroom.getVy() == 0;

        mushroom.setFalling(true);
        mushroom.setVy(5);
        mushroom.setJumping(true);
        mushroom.handleCollision(objectMock, 'D');
        assert mushroom.getY() == 4;
        assert !mushroom.isFalling();
        assert mushroom.getVy() == 0;
        assert !mushroom.isJumping();

        mushroom.setVx(0);
        mushroom.handleCollision(objectMock, 'L');
        assert mushroom.getX() == 6;
        assert mushroom.getVx() == 0.2f;

        mushroom.handleCollision(objectMock, 'R');
        assert mushroom.getX() == 4;
        assert mushroom.getVx() == -0.2f;


    }

}
