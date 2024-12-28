package org.grupogjl.controller.game.blocks;

import org.grupogjl.gui.GeneralGui;
import org.grupogjl.model.game.elements.blocks.SurpriseBlock;
import org.grupogjl.model.game.elements.level.Level;
import org.grupogjl.model.game.elements.surprises.Surprise;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

public class TestControllerSurpriseBlocks {

    private ControllerSurpriseBlocks controller;
    private Level level;
    private Surprise surprise;
    private SurpriseBlock surpriseBlock;

    @BeforeEach
    void setUp() {
        controller = spy(new ControllerSurpriseBlocks());

        level = mock(Level.class);
        surprise = mock(Surprise.class);
        surpriseBlock = mock(SurpriseBlock.class);
    }

    @Test
    void stepAddsActivatedSurpriseToObjects() {
        when(surpriseBlock.isUsed()).thenReturn(true);
        when(surpriseBlock.getSurprise()).thenReturn(surprise);
        when(surprise.isActivated()).thenReturn(true);
        when(level.getObjects()).thenReturn(new ArrayList<>());
        when(level.getSurpriseBlocks()).thenReturn(Collections.singletonList(surpriseBlock));

        controller.step(level, GeneralGui.ACTION.NONE, 100L);

        verify(level).setObjects(argThat(list -> list.size() == 1 && list.contains(surprise)));
    }

    @Test
    void stepDoesNotAddNonActivatedSurpriseToObjects() {
        when(surpriseBlock.isUsed()).thenReturn(true);
        when(surpriseBlock.getSurprise()).thenReturn(surprise);
        when(surprise.isActivated()).thenReturn(false);
        when(level.getObjects()).thenReturn(new ArrayList<>());
        when(level.getSurpriseBlocks()).thenReturn(Collections.singletonList(surpriseBlock));

        controller.step(level, GeneralGui.ACTION.NONE, 100L);

        verify(level).setObjects(argThat(List::isEmpty));
    }

    @Test
    void stepDoesNotAddAlreadyPresentSurpriseToObjects() {
        when(surpriseBlock.isUsed()).thenReturn(true);
        when(surpriseBlock.getSurprise()).thenReturn(surprise);
        when(surprise.isActivated()).thenReturn(true);
        when(level.getObjects()).thenReturn(new ArrayList<>(Collections.singletonList(surprise)));
        when(level.getSurpriseBlocks()).thenReturn(Collections.singletonList(surpriseBlock));

        controller.step(level, GeneralGui.ACTION.NONE, 100L);

        verify(level).setObjects(argThat(list -> list.size() == 1 && list.contains(surprise)));
    }

    @Test
    void stepHandlesNullLevel() {
        assertThrows(NullPointerException.class, () -> controller.step(null, GeneralGui.ACTION.NONE, 100L));
    }

    @Test
    void stepHandlesEmptySurpriseBlockList() {
        when(level.getSurpriseBlocks()).thenReturn(Collections.emptyList());
        when(level.getObjects()).thenReturn(new ArrayList<>());

        controller.step(level, GeneralGui.ACTION.NONE, 100L);

        verify(level).setObjects(Collections.emptyList());
    }

    @Test
    void stepSurpriseBlockIsNotUsed() {
        when(surpriseBlock.isUsed()).thenReturn(false);
        when(surpriseBlock.getSurprise()).thenReturn(surprise);
        when(surprise.isActivated()).thenReturn(true);
        when(level.getObjects()).thenReturn(new ArrayList<>());
        when(level.getSurpriseBlocks()).thenReturn(Collections.singletonList(surpriseBlock));

        controller.step(level, GeneralGui.ACTION.NONE, 100L);

        verify(level).setObjects(argThat(List::isEmpty));
    }

}
