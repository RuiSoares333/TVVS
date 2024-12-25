package org.grupogjl.model.game.elements.blocks;

import org.grupogjl.model.game.elements.camera.Camera;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

public class TestBuildingBlock {
    Camera cameraMock = Mockito.mock(Camera.class);

    @Test
    public void testGoalBlock(){
        GoalBlock block = new GoalBlock(5, 5, 250, 250);

        // getVirtX() = 5 - 10 = -5
        Mockito.when(cameraMock.getLeftCamLimit()).thenReturn(10f);
        assertEquals(-5f, block.getVirtX(cameraMock));

        // getVirtY() = 5 - (250 - 1) = -244
        assertEquals(-244f, block.getVirtY());

        // getImage() = "goalBlock.png"
        assertEquals("goalBlock.png", block.getImage());
    }

    @Test
    public void testPipe(){
        Pipe pipe = new Pipe(5, 5, 250, 250);

        // getVirtX() = 5 - 10 = -5
        Mockito.when(cameraMock.getLeftCamLimit()).thenReturn(10f);
        assertEquals(-5f, pipe.getVirtX(cameraMock));

        // getVirtY() = 5 - (250 - 1) = -244
        assertEquals(-244f, pipe.getVirtY());

        // getImage() = "pipe250.png"
        assertEquals("pipe250.png", pipe.getImage());

        // getConection() = null
        assertNull(pipe.getConection());

        // setConection() = new Pipe(5, 5, 250, 250)
        Pipe newPipe = new Pipe(5, 5, 250, 250);
        pipe.setConection(newPipe);

        // getConection() = newPipe
        assertNotNull(pipe.getConection());
        assertEquals(newPipe, pipe.getConection());
    }

    @Test
    public void testUnbreakableBlock(){
        UnbreakableBlock block = new UnbreakableBlock(5, 5, 250, 250);

        // getVirtX() = 5 - 10 = -5
        Mockito.when(cameraMock.getLeftCamLimit()).thenReturn(10f);
        assertEquals(-5f, block.getVirtX(cameraMock));

        // getVirtY() = 5
        assertEquals(5f, block.getVirtY());

        // getImage() = "unbreakableBlock.png"
        assertEquals("unbreakableBlock.png", block.getImage());
    }

}
