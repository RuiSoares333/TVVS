package org.grupogjl.model.game.elements.blocks;

import org.grupogjl.model.game.elements.Mario;
import org.grupogjl.model.game.elements.camera.Camera;
import org.grupogjl.model.game.elements.surprises.Surprise;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

public class TestInteractableBlock {
    Camera cameraMock = Mockito.mock(Camera.class);
    Mario marioMock = Mockito.mock(Mario.class);

    @Test
    public void testDestroyableBlock(){
        DestroyableBlock block = new DestroyableBlock(5, 5, 250, 250);

        // getVirtX() = 5 - 10 = -5
        Mockito.when(cameraMock.getLeftCamLimit()).thenReturn(10f);
        assertEquals(-5f, block.getVirtX(cameraMock));

        // getVirtY() = 5
        assertEquals(5f, block.getVirtY());

        // getImage() = "breakableBlock.png"
        assertEquals("breakableBlock.png", block.getImage());

        // getStrenght() = 1
        assertEquals(1, block.getStrenght());

        Mockito.when(marioMock.isStateBig()).thenReturn(false);
        Mockito.when(marioMock.isStateFire()).thenReturn(false);
        block.gotHit(marioMock);
        assertEquals(1, block.getStrenght());

        Mockito.when(marioMock.isStateBig()).thenReturn(true);
        block.gotHit(marioMock);
        assertEquals(0, block.getStrenght());
    }

    @Test
    public void testSurpriseBlock(){
        SurpriseBlock block = new SurpriseBlock(5, 5, 250, 250);
        Surprise surpriseMock = Mockito.mock(Surprise.class);
        Mockito.when(surpriseMock.getY()).thenReturn(5f);
        Camera cameraMock = Mockito.mock(Camera.class);
        Mockito.when(cameraMock.getLeftCamLimit()).thenReturn(10f);

        // isOpen = false / used = false / surprise = null
        assertFalse(block.getOpen());
        assertFalse(block.isUsed());
        assertNull(block.getSurprise());
        assertEquals("surpriseBlock.png", block.getImage());

        // isOpen = false / used = false / surprise = Surprise
        block.setSurprise(surpriseMock);
        assertEquals(surpriseMock, block.getSurprise());

        block.gotHit(null);
        assertTrue(block.isUsed());
        assertEquals("emptySurpriseBlock.png", block.getImage());
        block.setUsed(false);

        // isOpen = true / used = true / surprise = Surprise
        block.setOpen(true);
        block.setUsed(true);
        block.gotHit(null);
        assertTrue(block.getOpen());
        assertTrue(block.isUsed());

        // getVirtX() = 5 - 10 = -5
        assertEquals(-5f, block.getVirtX(cameraMock));
        assertEquals(5f, block.getVirtY());

        // surprise mock tests
        Mockito.doReturn(5f).when(surpriseMock).getY();
        Mockito.verify(surpriseMock, Mockito.times(1)).getY();
        Mockito.verify(surpriseMock, Mockito.times(1)).setY(4);
        Mockito.verify(surpriseMock, Mockito.times(1)).setActivated(Mockito.anyBoolean());
        Mockito.verify(surpriseMock, Mockito.times(1)).born();
    }
}
