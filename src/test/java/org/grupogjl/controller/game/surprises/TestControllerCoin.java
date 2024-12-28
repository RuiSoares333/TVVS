package org.grupogjl.controller.game.surprises;

import org.grupogjl.gui.GeneralGui;
import org.grupogjl.model.game.elements.Mario;
import org.grupogjl.model.game.elements.level.Level;
import org.grupogjl.model.game.elements.surprises.Coin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class TestControllerCoin {

    private ControllerCoin controller;
    private Level level;
    private Mario mario;
    private Coin coin;

    @BeforeEach
    void setUp() {
        controller = new ControllerCoin();
        level = mock(Level.class);
        mario = mock(Mario.class);
        coin = mock(Coin.class);

        when(level.getMario()).thenReturn(mario);
        when(level.getCoins()).thenReturn(Collections.singletonList(coin));
    }

    @Test
    void stepUpdatesCoinStatus() {
        controller.step(level, GeneralGui.ACTION.NONE, 100L);

        verify(level).getMario();
        verify(level).getCoins();
        verify(coin).getActivationTimer();
    }

    @Test
    void updateStatusIncreasesMarioCoinsWhenCoinDeactivates() {
        when(coin.getActivationTimer()).thenReturn(0);
        when(mario.getCoins()).thenReturn(5);

        controller.updateStatus(level, 100L);

        verify(coin).setActivated(false);
        verify(mario).setCoins(6);
    }

    @Test
    void updateStatusDecreasesActivationTimerWhenCoinIsActive() {
        when(coin.getActivationTimer()).thenReturn(5);

        controller.updateStatus(level, 100L);

        verify(coin).setActivationTimer(4);
    }

    @Test
    void updateStatusHandlesNullLevel() {
        assertThrows(NullPointerException.class, () -> controller.updateStatus(null, 100L));
    }

    @Test
    void updateStatusHandlesNullCoinsList() {
        when(level.getCoins()).thenReturn(null);

        assertThrows(NullPointerException.class, () -> controller.updateStatus(level, 100L));

        verify(level).getMario();
        verify(level).getCoins();
    }
}
