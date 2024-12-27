package org.grupogjl.controller;

import org.grupogjl.Game;
import org.grupogjl.commands.ExitToMenuCommand;
import org.grupogjl.commands.GameOverCommand;
import org.grupogjl.commands.PauseCommand;
import org.grupogjl.controller.game.ControllerLevel;
import org.grupogjl.gui.GeneralGui;
import org.grupogjl.model.game.elements.Mario;
import org.grupogjl.model.game.elements.level.Level;
import org.grupogjl.state.StateGame;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.mockito.Mockito.*;

public class TestControllerGame {

    private ControllerGame controllerGame;
    private Mario mario;
    private Game game;
    private StateGame stateGame;
    private Level level;

    @BeforeEach
    void setUp() {
        controllerGame = spy(new ControllerGame());
        mario = mock(Mario.class);
        game = mock(Game.class);

        stateGame = mock(StateGame.class);
        level = mock(Level.class);

        when(game.getStateGame()).thenReturn(stateGame);
        when(stateGame.getModel()).thenReturn(level);
        when(level.getMario()).thenReturn(mario);
    }

    @Test
    void gameOverCommandExecutedWhenMarioHasNoLives() throws IOException {
        GameOverCommand gameOverCommand = mock(GameOverCommand.class);

        when(mario.getLives()).thenReturn(0);
        when(controllerGame.getGameOverCommand()).thenReturn(gameOverCommand);

        controllerGame.step(game, GeneralGui.ACTION.NONE, 100L);

        verify(gameOverCommand).execute(game);
    }

    @Test
    void exitToMenuCommandExecutedWhenMarioHasNoLivesAndActionIsSelect() throws IOException {
        GameOverCommand gameOverCommand = mock(GameOverCommand.class);
        ExitToMenuCommand exitToMenuCommand = mock(ExitToMenuCommand.class);

        when(mario.getLives()).thenReturn(0);
        when(controllerGame.getGameOverCommand()).thenReturn(gameOverCommand);
        when(controllerGame.getExitToMenuCommand()).thenReturn(exitToMenuCommand);

        controllerGame.step(game, GeneralGui.ACTION.SELECT, 100L);

        verify(gameOverCommand).execute(game);
        verify(exitToMenuCommand).execute(game);
    }

    @Test
    void pauseCommandExecutedWhenActionIsQuit() throws IOException {
        PauseCommand pauseCommand = mock(PauseCommand.class);

        when(mario.getLives()).thenReturn(1);
        when(controllerGame.getPauseCommand()).thenReturn(pauseCommand);

        controllerGame.step(game, GeneralGui.ACTION.QUIT, 100L);

        verify(pauseCommand).execute(game);
    }

    @Test
    void controllerLevelStepCalledWhenMarioHasLivesAndActionIsNotQuitOrSelect() throws IOException {
        ControllerLevel controllerLevel = mock(ControllerLevel.class);

        when(mario.getLives()).thenReturn(1);
        when(controllerGame.getControllerLevel()).thenReturn(controllerLevel);

        controllerGame.step(game, GeneralGui.ACTION.RIGHT, 100L);

        verify(controllerLevel).step(level, GeneralGui.ACTION.RIGHT, 100L);
    }
}
