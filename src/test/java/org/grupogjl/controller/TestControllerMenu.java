package org.grupogjl.controller;

import org.grupogjl.Game;
import org.grupogjl.gui.GeneralGui;
import org.grupogjl.model.game.elements.Mario;
import org.grupogjl.model.game.elements.level.Level;
import org.grupogjl.model.game.elements.menu.MenuModel;
import org.grupogjl.state.StateGame;
import org.grupogjl.state.StateMenu;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class TestControllerMenu {

    private ControllerMenu controllerMenu;
    private Mario mario;
    private Game game;
    private StateGame stateGame;
    private Level level;
    private MenuModel menuModel;
    private StateMenu stateMenu;

    @BeforeEach
    void setUp() {
        controllerMenu = spy(new ControllerMenu());
        mario = mock(Mario.class);
        game = mock(Game.class);

        stateGame = mock(StateGame.class);
        level = mock(Level.class);
        stateMenu = mock(StateMenu.class);

        when(game.getStateMenu()).thenReturn(stateMenu);
        menuModel = mock(MenuModel.class);
        when(stateMenu.getModel()).thenReturn(menuModel);

        when(game.getStateGame()).thenReturn(stateGame);
        when(stateGame.getModel()).thenReturn(level);
        when(level.getMario()).thenReturn(mario);
    }

    @Test
    void stepDoesNotChangeSelectionWhenOptionIsSelectedAndActionIsNotSelect() {
        when(menuModel.isSelectedOption()).thenReturn(true);

        controllerMenu.step(game, GeneralGui.ACTION.DOWN, 100L);

        verify(menuModel, never()).setSelectedOption(false);
    }

    @Test
    void stepDeselectsOptionWhenOptionIsSelectedAndActionIsSelect() {
        when(menuModel.isSelectedOption()).thenReturn(true);

        controllerMenu.step(game, GeneralGui.ACTION.SELECT, 100L);

        verify(menuModel).setSelectedOption(false);
    }

    @Test
    void stepMovesToNextPositionWhenOptionIsNotSelectedAndActionIsDown() {
        when(menuModel.isSelectedOption()).thenReturn(false);

        controllerMenu.step(game, GeneralGui.ACTION.DOWN, 100L);

        verify(menuModel).nextPosition();
    }

    @Test
    void stepMovesToLastPositionWhenOptionIsNotSelectedAndActionIsUp() {
        when(menuModel.isSelectedOption()).thenReturn(false);

        controllerMenu.step(game, GeneralGui.ACTION.UP, 100L);

        verify(menuModel).lastPosition();
    }

    @Test
    void stepExecutesSelectedOptionWhenOptionIsNotSelectedAndActionIsSelect() {
        when(menuModel.isSelectedOption()).thenReturn(false);

        controllerMenu.step(game, GeneralGui.ACTION.SELECT, 100L);

        verify(menuModel).execute(game);
    }
}
