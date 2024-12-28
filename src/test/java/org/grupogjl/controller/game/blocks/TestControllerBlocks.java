package org.grupogjl.controller.game.blocks;

import org.grupogjl.gui.GeneralGui;
import org.grupogjl.model.game.elements.level.Level;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class TestControllerBlocks {

    ControllerBlocks controllerBlocks;
    Level level;

    @Test
    void testStep() {
        controllerBlocks = spy(new ControllerBlocks());
        level = mock(Level.class);

        controllerBlocks.step(level, GeneralGui.ACTION.RIGHT, 1000);

        verify(controllerBlocks, times(1)).step(level, GeneralGui.ACTION.RIGHT, 1000);
    }
}
