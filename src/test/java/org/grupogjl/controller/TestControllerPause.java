package org.grupogjl.controller;

import org.grupogjl.Game;
import org.grupogjl.gui.GeneralGui;
import org.grupogjl.model.game.elements.Mario;
import org.grupogjl.model.game.elements.level.Level;
import org.grupogjl.model.game.elements.menu.MenuModel;
import org.grupogjl.model.game.elements.pause.PauseModel;
import org.grupogjl.state.StateGame;
import org.grupogjl.state.StatePause;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

public class TestControllerPause {
    private ControllerPause controllerPause;
    private Mario mario;
    private Game game;
    private StateGame stateGame;
    private Level level;
    private StatePause statePause;
    private PauseModel pauseModel;

    @BeforeEach
    void setUp() {
        controllerPause = spy(new ControllerPause());
        mario = mock(Mario.class);
        game = mock(Game.class);

        stateGame = mock(StateGame.class);
        level = mock(Level.class);
        statePause = mock(StatePause.class);
        pauseModel = mock(PauseModel.class);

        when(game.getStatePause()).thenReturn(statePause);
        when(statePause.getModel()).thenReturn(pauseModel);

        when(game.getStateGame()).thenReturn(stateGame);
        when(stateGame.getModel()).thenReturn(level);
        when(level.getMario()).thenReturn(mario);
    }

    @Test
    void stepMovesToNextPositionWhenActionIsDown() {
        controllerPause.step(game, GeneralGui.ACTION.DOWN, 100L);

        verify(pauseModel).nextPosition();
    }

    @Test
    void stepMovesToLastPositionWhenActionIsUp() {
        controllerPause.step(game, GeneralGui.ACTION.UP, 100L);

        verify(game.getStatePause().getModel()).lastPosition();
    }

    @Test
    void stepExecutesSelectedOptionWhenActionIsSelect() {
        controllerPause.step(game, GeneralGui.ACTION.SELECT, 100L);

        verify(game.getStatePause().getModel()).execute(game);
    }

    @Test
    void stepDoesNothingWhenActionIsNone() {
        controllerPause.step(game, GeneralGui.ACTION.NONE, 100L);

        verify(game.getStatePause().getModel(), never()).nextPosition();
        verify(game.getStatePause().getModel(), never()).lastPosition();
        verify(game.getStatePause().getModel(), never()).execute(game);
    }
}
