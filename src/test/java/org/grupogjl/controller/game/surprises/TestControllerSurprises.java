package org.grupogjl.controller.game.surprises;

import org.grupogjl.gui.GeneralGui;
import org.grupogjl.model.game.elements.level.Level;
import org.grupogjl.model.game.elements.surprises.Surprise;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class TestControllerSurprises {


    private ControllerSurprises controller;
    private Level level;
    private Surprise surprise;
    private List<Surprise> surprises;

    @BeforeEach
    void setUp() {
        controller = new ControllerSurprises();
        level = mock(Level.class);
        surprise = mock(Surprise.class);

        surprises = Collections.singletonList(surprise);
        when(level.getSurprises()).thenReturn(new ArrayList<>(surprises));
    }


    @Test
    void stepUpdatesSurpriseStatusAndCallsControllerCoinStep() {
        controller.step(level, GeneralGui.ACTION.NONE, 100L);

        verify(level).getSurprises();
    }

    @Test
    void updateStatusRemovesDeactivatedSurprises() {
        when(surprise.isActivated()).thenReturn(false);

        controller.updateStatus(level, 100L);

        verify(level).setSurprises(Collections.emptyList());
    }

    @Test
    void updateStatusUpdatesLocationOfActivatedSurprises() {
        when(surprise.isActivated()).thenReturn(true);

        controller.updateStatus(level, 100L);

        verify(surprise).updateLocation();
        verify(level).setSurprises(surprises);
    }

    @Test
    void updateStatusHandlesNullSurprisesList() {
        when(level.getSurprises()).thenReturn(null);

        assertThrows(NullPointerException.class, () -> controller.updateStatus(level, 100L));

        verify(level, never()).setSurprises(anyList());
    }
}
