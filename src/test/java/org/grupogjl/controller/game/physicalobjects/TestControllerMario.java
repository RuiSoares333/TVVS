package org.grupogjl.controller.game.physicalobjects;

import org.grupogjl.gui.GeneralGui;
import org.grupogjl.model.game.elements.Mario;
import org.grupogjl.model.game.elements.blocks.Pipe;
import org.grupogjl.model.game.elements.camera.Camera;
import org.grupogjl.model.game.elements.generalobjects.GameObject;
import org.grupogjl.model.game.elements.level.Level;
import org.grupogjl.model.game.elements.props.FireBall;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class TestControllerMario {

    private ControllerMario controller;
    private Level level;
    private Camera camera;
    private Mario mario;
    private List<GameObject> objects;
    private Pipe pipe1;
    private Pipe pipe2;

    @BeforeEach
    void setUp() {
        controller = spy(new ControllerMario());
        level = mock(Level.class);
        camera = mock(Camera.class);
        mario = mock(Mario.class);
        pipe1 = mock(Pipe.class);
        pipe2 = mock(Pipe.class);

        objects = mock(List.class);

        when(pipe1.getX()).thenReturn(5.0f);
        when(pipe1.getY()).thenReturn(5.0f);
        when(pipe1.getHeight()).thenReturn(1.0f);
        when(pipe1.getConection()).thenReturn(pipe2);

        when(pipe2.getX()).thenReturn(10.0f);
        when(pipe2.getY()).thenReturn(10.0f);
        when(pipe2.getHeight()).thenReturn(1.0f);

        when(level.getCamera()).thenReturn(camera);
        when(level.getMario()).thenReturn(mario);
        when(level.getObjects()).thenReturn(objects);
    }

    @Test
    void stepUpdatesMarioStatusAndMovesMario() {
        controller.step(level, GeneralGui.ACTION.RIGHT, 100L);

        verify(level).getCamera();
        verify(level, times(3)).getMario();
        verify(level).getObjects();
        verify(mario).moveRight();
        verify(camera).updateCamera(mario);
    }

    @Test
    void stepHandlesNullLevel() {
        assertThrows(NullPointerException.class, () -> controller.step(null, GeneralGui.ACTION.RIGHT, 100L));
    }

    @Test
    void stepHandlesNullCamera() {
        when(level.getCamera()).thenReturn(null);

        assertThrows(NullPointerException.class, () -> controller.step(level, GeneralGui.ACTION.RIGHT, 100L));

        verify(level).getCamera();
        verify(level, times(3)).getMario();
        verify(level).getObjects();
        verify(mario).moveRight();
    }

    @Test
    void stepHandlesNullMario() {
        when(level.getMario()).thenReturn(null);

        assertThrows(NullPointerException.class, () -> controller.step(level, GeneralGui.ACTION.RIGHT, 100L));

        verify(level).getCamera();
        verify(level, times(2)).getMario();
        verify(level, never()).getObjects();
        verify(camera, never()).updateCamera(any());
    }

    @Test
    void stepHandlesNullObjects() {
        when(level.getObjects()).thenReturn(null);

        controller.step(level, GeneralGui.ACTION.RIGHT, 100L);

        verify(level).getCamera();
        verify(level, times(3)).getMario();
        verify(level).getObjects();
        verify(mario).moveRight();
        verify(camera).updateCamera(mario);
    }



    @Test
    void updateMarioStatusResetsMarioWhenBelowLevel() {
        when(mario.getY()).thenReturn(11.0f);
        when(level.getHeight()).thenReturn(10);

        controller.updateMarioStatus(level);

        verify(mario).reset();
    }

    @Test
    void updateMarioStatusUpdatesMarioLocation() {
        controller.updateMarioStatus(level);

        verify(mario).updateLocation();
    }

    @Test
    void updateMarioStatusIncreasesLivesWhenCoinsReachTen() {
        when(mario.getCoins()).thenReturn(10);

        controller.updateMarioStatus(level);

        verify(mario).setLives(mario.getLives() + 1);
        verify(mario).setCoins(0);
    }

    @Test
    void updateMarioStatusDisablesInvincibilityWhenTimeIsZero() {
        when(mario.getInvencibleTime()).thenReturn(0);
        when(mario.isStateInvencible()).thenReturn(true);
        when(mario.isHitCooldown()).thenReturn(true);

        controller.updateMarioStatus(level);

        verify(mario).setStateInvencible(false);
        verify(mario).setHitCooldown(false);
    }

    @Test
    void updateMarioStatusDisablesInvincibilityWhenTimeIsZeroButHitNotCooldown() {
        when(mario.getInvencibleTime()).thenReturn(0);
        when(mario.isStateInvencible()).thenReturn(true);
        when(mario.isHitCooldown()).thenReturn(false);

        controller.updateMarioStatus(level);

        verify(mario).setStateInvencible(false);
        verify(mario, never()).setHitCooldown(false);
    }

    @Test
    void updateMarioStatusDecreasesInvincibilityTimeWhenNonZero() {
        when(mario.getInvencibleTime()).thenReturn(5);
        when(mario.isStateInvencible()).thenReturn(true);

        controller.updateMarioStatus(level);

        verify(mario).setInvencibleTime(4);
    }

    @Test
    void updateMarioStatusSetInvincibility() {
        when(mario.getInvencibleTime()).thenReturn(5);
        when(mario.isStateInvencible()).thenReturn(false);

        controller.updateMarioStatus(level);

        verify(mario, never()).setStateInvencible(anyBoolean());
        verify(mario, never()).setHitCooldown(anyBoolean());
        verify(mario, never()).setInvencibleTime(anyInt());
    }



    @Test
    void moveMarioJumpsWhenActionIsUp() {
        controller.moveMario(GeneralGui.ACTION.UP, mario, objects);

        verify(mario).jump();
    }

    @Test
    void moveMarioMovesRightWhenActionIsRight() {
        controller.moveMario(GeneralGui.ACTION.RIGHT, mario, objects);

        verify(mario).moveRight();
    }

    @Test
    void moveMarioMovesLeftWhenActionIsLeft() {
        controller.moveMario(GeneralGui.ACTION.LEFT, mario, objects);

        verify(mario).moveLeft();
    }

    @Test
    void moveMarioStopsWhenActionIsNoneAndNotJumpingOrFalling() {
        when(mario.isJumping()).thenReturn(false);
        when(mario.isFalling()).thenReturn(false);

        controller.moveMario(GeneralGui.ACTION.NONE, mario, objects);

        verify(mario).setVx(0);
    }

    @Test
    void moveMarioSlowsDownWhenActionIsNoneAndFalling() {
        when(mario.isJumping()).thenReturn(false);
        when(mario.isFalling()).thenReturn(true);
        when(mario.getVx()).thenReturn(4f);

        controller.moveMario(GeneralGui.ACTION.NONE, mario, objects);

        verify(mario).setVx(1);
    }

    @Test
    void moveMarioSlowsDownWhenActionIsNoneAndJumping() {
        when(mario.isJumping()).thenReturn(true);
        when(mario.isFalling()).thenReturn(false);
        when(mario.getVx()).thenReturn(4f);

        controller.moveMario(GeneralGui.ACTION.NONE, mario, objects);

        verify(mario).setVx(1);
    }

    @Test
    void moveMarioTransportsWhenActionIsDown() {
        doNothing().when(controller).transportMario(mario, objects);

        controller.moveMario(GeneralGui.ACTION.DOWN, mario, objects);

        verify(controller).transportMario(mario, objects);
    }

    @Test
    void moveMarioThrowsFireBallWhenActionIsThrowBallAndMarioIsFire() {
        when(mario.isStateFire()).thenReturn(true);
        when(mario.getX()).thenReturn(5.0f);
        when(mario.getY()).thenReturn(5.0f);

        controller.moveMario(GeneralGui.ACTION.THROWBALL, mario, objects);

        verify(objects).add(any(FireBall.class));
    }

    @Test
    void moveMarioDoesNotThrowFireBallWhenActionIsThrowBallAndMarioIsNotFire() {
        when(mario.isStateFire()).thenReturn(false);

        controller.moveMario(GeneralGui.ACTION.THROWBALL, mario, objects);
        controller.moveMario(GeneralGui.ACTION.QUIT, mario, objects);

        verify(objects, never()).add(any(FireBall.class));
    }



    @Test
    void transportMarioMovesMarioToPipeConnectionWhenAbovePipe() {
        objects = Collections.singletonList(pipe1);

        when(mario.getX()).thenReturn(5f);
        when(mario.getY()).thenReturn(3.8f);

        controller.transportMario(mario, objects);

        verify(mario).setX(10.0f);
        verify(mario).setY(9.0f);
    }

    @Test
    void transportMarioDoesNotMoveMarioWhenNotAbovePipe() {
        objects = Collections.singletonList(pipe1);
        when(pipe1.getX()).thenReturn(5.0f);
        when(pipe1.getY()).thenReturn(5.0f);
        when(pipe1.getHeight()).thenReturn(1.0f);
        when(pipe1.getConection()).thenReturn(pipe2);
        when(mario.getX()).thenReturn(5.1f);
        when(mario.getY()).thenReturn(6.0f);

        controller.transportMario(mario, objects);

        verify(mario, never()).setX(anyFloat());
        verify(mario, never()).setY(anyFloat());
    }

    @Test
    void transportMarioDoesNotMoveMarioWhenFarFromPipe() {
        objects = Collections.singletonList(pipe1);
        when(pipe1.getX()).thenReturn(5.0f);
        when(pipe1.getY()).thenReturn(5.0f);
        when(pipe1.getHeight()).thenReturn(1.0f);
        when(pipe1.getConection()).thenReturn(pipe2);
        when(mario.getX()).thenReturn(7.0f);
        when(mario.getY()).thenReturn(4.5f);

        controller.transportMario(mario, objects);

        verify(mario, never()).setX(anyFloat());
        verify(mario, never()).setY(anyFloat());
    }

    @Test
    void transportMarioHandlesNonPipeObjects() {
        objects = Collections.singletonList(mock(GameObject.class));

        controller.transportMario(mario, objects);

        verify(mario, never()).setX(anyFloat());
        verify(mario, never()).setY(anyFloat());
    }

    @Test
    void transportMarioHandlesNullObjectsList() {
        assertThrows(NullPointerException.class, () -> controller.transportMario(mario, null));

        verify(mario, never()).setX(anyFloat());
        verify(mario, never()).setY(anyFloat());
    }

    @Test
    void transportMarioHandlesEmptyObjectsList() {
        objects = Collections.emptyList();

        controller.transportMario(mario, objects);

        verify(mario, never()).setX(anyFloat());
        verify(mario, never()).setY(anyFloat());
    }
}
