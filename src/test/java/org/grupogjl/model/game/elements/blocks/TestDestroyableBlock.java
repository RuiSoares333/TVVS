package org.grupogjl.model.game.elements.blocks;

import org.grupogjl.model.game.elements.Mario;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestDestroyableBlock {

    @Test
    void testGotHitWhenStateFire(){
        Mario mario = mock(Mario.class);
        when(mario.isStateBig()).thenReturn(false);
        when(mario.isStateFire()).thenReturn(true);

        DestroyableBlock block = new DestroyableBlock(0, 0, 0, 0);

        block.gotHit(mario);

        assert(block.getStrenght() == 0);
    }
}
