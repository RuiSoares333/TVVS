package org.grupogjl.controller.game.physicalobjects;

import org.grupogjl.gui.GeneralGui;
import org.grupogjl.model.game.elements.level.Level;
import org.grupogjl.model.game.elements.props.FireBall;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class TestControllerFireBall {

    private ControllerFireBall controller;
    private Level level;
    private FireBall fireBall;
    private List<FireBall> fireBalls;

    @BeforeEach
    void setUp() {
        controller = spy(new ControllerFireBall());
        level = mock(Level.class);
        fireBall = mock(FireBall.class);
        fireBalls = Collections.singletonList(fireBall);

        when(level.getFireBalls()).thenReturn(new ArrayList<>(fireBalls));
    }

    @Test
    void stepUpdatesStatusOfActiveFireBalls() {
        when(fireBall.isActive()).thenReturn(true);

        controller.step(level, GeneralGui.ACTION.NONE, 100L);

        verify(fireBall).updateLocation();
        verify(level).setFireBalls(fireBalls);
    }

    @Test
    void stepRemovesInactiveFireBalls() {
        when(fireBall.isActive()).thenReturn(false);

        controller.step(level, GeneralGui.ACTION.NONE, 100L);

        verify(level).setFireBalls(Collections.emptyList());
    }

    @Test
    void stepHandlesNullLevel() {
        assertThrows(NullPointerException.class, () -> controller.step(null, GeneralGui.ACTION.NONE, 100L));
    }

    @Test
    void updateStatusUpdatesLocationOfActiveFireBalls() {
        when(fireBall.isActive()).thenReturn(true);

        controller.updateStatus(level, 100L);

        verify(fireBall).updateLocation();
        verify(level).setFireBalls(fireBalls);
    }

    @Test
    void updateStatusRemovesInactiveFireBalls() {
        when(fireBall.isActive()).thenReturn(false);

        controller.updateStatus(level, 100L);

        verify(level).setFireBalls(Collections.emptyList());
    }
}
