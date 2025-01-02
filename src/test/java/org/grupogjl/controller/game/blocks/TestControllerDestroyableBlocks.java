package org.grupogjl.controller.game.blocks;

import org.grupogjl.gui.GeneralGui;
import org.grupogjl.model.game.elements.Mario;
import org.grupogjl.model.game.elements.blocks.DestroyableBlock;
import org.grupogjl.model.game.elements.level.Level;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class TestControllerDestroyableBlocks {

    ControllerDestroyableBlocks controller;
    private Level level;
    private DestroyableBlock block1;
    private DestroyableBlock block2;

    @BeforeEach
    void setUp() {
        controller = spy(new ControllerDestroyableBlocks());
        level = mock(Level.class);
        block1 = mock(DestroyableBlock.class);
        block2 = mock(DestroyableBlock.class);
        List<DestroyableBlock> blocks = Arrays.asList(block1, block2);
        when(level.getDestroyableBlocks()).thenReturn(new ArrayList<>(blocks));
    }


    @Test
    void stepRemovesBlocksWithZeroOrLessStrength() {
        when(block1.getStrenght()).thenReturn(0);
        when(block2.getStrenght()).thenReturn(1);

        controller.step(level, GeneralGui.ACTION.NONE, 100L);

        verify(level).setDestroyableBlocks(argThat(list -> list.size() == 1 && list.contains(block2)));
    }

    @Test
    void stepDoesNotRemoveBlocksWithPositiveStrength() {
        when(block1.getStrenght()).thenReturn(1);
        when(block2.getStrenght()).thenReturn(2);

        controller.step(level, GeneralGui.ACTION.NONE, 100L);

        verify(level).setDestroyableBlocks(argThat(list -> list.size() == 2 && list.contains(block1) && list.contains(block2)));
    }

    @Test
    void stepHandlesNullLevel() {
        assertThrows(NullPointerException.class, () -> controller.step(null, GeneralGui.ACTION.NONE, 100L));
    }

    @Test
    void stepHandlesEmptyBlockList() {
        when(level.getDestroyableBlocks()).thenReturn(Collections.emptyList());

        controller.step(level, GeneralGui.ACTION.NONE, 100L);

        verify(level).setDestroyableBlocks(Collections.emptyList());
    }

}
