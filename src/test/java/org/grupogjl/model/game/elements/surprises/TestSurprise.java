package org.grupogjl.model.game.elements.surprises;

import org.grupogjl.model.game.elements.Mario;
import org.grupogjl.model.game.elements.camera.Camera;
import org.grupogjl.model.game.elements.generalobjects.StaticObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

public class TestSurprise {

    private static Mario marioMock;
    private static Camera cameraMock;
    private static StaticObject objectMock;

    @BeforeEach
    public void setUp() {
        marioMock = Mockito.mock(Mario.class);
        when(marioMock.getCoins()).thenReturn(1);

        cameraMock = Mockito.mock(Camera.class);
        when(cameraMock.getLeftCamLimit()).thenReturn(5f);

        objectMock = Mockito.mock(StaticObject.class);
        when(objectMock.getX()).thenReturn(5f);
        when(objectMock.getY()).thenReturn(5f);
        when(objectMock.getHeight()).thenReturn(1f);
        when(objectMock.getWidth()).thenReturn(1f);
    }

    @Test
    public void testSurprise(){
        Coin coin = new Coin(1, 1);
        coin.setVx(5);
        coin.handleWallColision(5);
        assert coin.getX() == 5;
        assert coin.getVx() == -5;

        coin.setActivated(true);
        assert coin.isActivated();

        coin.setActivated(false);
        assert !coin.isActivated();
    }

    @Test
    public void testCoin() {
        Coin coin = new Coin(1, 1);

        coin.setActivationTimer(5);
        assert coin.getActivationTimer() == 5;

        coin.born();
        assert coin.getVy() == 0.7f;
        assert coin.isJumping();

        coin.activate(marioMock);
        verify(marioMock, times(1)).getCoins();
        verify(marioMock, times(1)).setCoins(2);

        coin.setVy(5);
        coin.setVx(5);
        coin.setFalling(true);
        coin.setJumping(true);
        coin.handleCollision(null,'U');
        assert coin.getVy() == 0;
        assert coin.getVx() == 0;
        assert !coin.isFalling();
        assert !coin.isJumping();

        assert coin.getVirtX(cameraMock) == -4f; // 1 - 5
        assert coin.getVirtY() == 1;

        assert coin.getImage().equals("coin.png");
    }

    @Test
    public void testFlower() {
        Flower flower = new Flower(1, 1);

        flower.handleCollision(null, 'U');
        assert flower.getY() == 1;
        flower.handleCollision(objectMock, 'X');
        assert flower.getY() == 1;

        flower.setVy(5);
        flower.handleCollision(objectMock, 'U');
        assert flower.getY() == 6;
        assert flower.getVy() == 0;

        flower.setFalling(true);
        flower.setVy(5);
        flower.setJumping(true);
        flower.handleCollision(objectMock, 'D');
        assert flower.getY() == 4;
        assert !flower.isFalling();
        assert flower.getVy() == 0;
        assert !flower.isJumping();

        flower.handleCollision(objectMock, 'L');
        assert flower.getX() == 6;

        flower.handleCollision(objectMock, 'R');
        assert flower.getX() == 4;

        flower.born();
        assert flower.getY() == 3;
        assert flower.isFalling();

        flower.activate(marioMock);
        verify(marioMock, times(1)).setHeight(2);
        verify(marioMock, times(1)).setStateFire(true);
        verify(marioMock, times(1)).setStateBig(false);

        Camera cameraMock = Mockito.mock(Camera.class);
        when(cameraMock.getLeftCamLimit()).thenReturn(5f);

        assert flower.getVirtX(cameraMock) == -1f; // 4 - 5

        assert flower.getVirtY() == 3;

        assert flower.getImage().equals("flower.png");
    }

    @Test
    public void testStar() {
        Star star = new Star(1, 1);

        star.handleCollision(null, 'U');
        assert star.getY() == 1;
        star.handleCollision(objectMock, 'X');
        assert star.getY() == 1;

        star.setVy(5);
        star.handleCollision(objectMock, 'U');
        assert star.getY() == 6;
        assert star.getVy() == 0;

        star.setFalling(true);
        star.setVy(5);
        star.setJumping(false);
        star.handleCollision(objectMock, 'D');
        assert star.getY() == 4;
        assert !star.isFalling();
        assert star.getVy() == -0.1f;
        assert star.isJumping();

        star.handleCollision(objectMock, 'L');
        assert star.getX() == 6;
        assert star.getVx() == 0.2f;

        star.handleCollision(objectMock, 'R');
        assert star.getX() == 4;
        assert star.getVx() == -0.2f;

        star.born();
        assert star.isFalling();
        assert star.getVx() == 0.2f;

        star.activate(marioMock);
        verify(marioMock, times(1)).setInvencibleTime(600);
        verify(marioMock, times(1)).setStateInvencible(true);

        assert star.getVirtX(cameraMock) == -1f; // 4 - 5

        assert star.getVirtY() == 4;

        assert star.getImage().equals("star.png");
    }

}
