package org.grupogjl.commands;

import org.grupogjl.Game;
import org.grupogjl.commands.*;
import org.grupogjl.model.game.elements.menu.MenuModel;
import org.grupogjl.state.StateGame;
import org.grupogjl.state.StateMenu;
import org.grupogjl.state.StatePause;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class TestCommands {

    private Game game;

    @BeforeEach
    void setUp() {
        game = mock(Game.class);
    }

    // ExitGameCommand
    @Test
    void executeSetsGameStateToNull() {
        ExitGameCommand command = new ExitGameCommand();

        command.execute(game);

        verify(game).setStateNull();
    }


    // ExitToMenuCommand
    @Test
    void executeSetsGameStateToMenu() {
        ExitToMenuCommand command = new ExitToMenuCommand();

        command.execute(game);

        verify(game).setStateMenu();
    }

    // GameOverCommand
    @Test
    void executeSetsGameOverStateToTrue() {
        StateGame gameState = mock(StateGame.class);
        when(game.getStateGame()).thenReturn(gameState);
        GameOverCommand command = new GameOverCommand();

        command.execute(game);

        verify(gameState).setGameOver(true);
    }

    @Test
    void executeHandlesNullGameState() {
        when(game.getStateGame()).thenReturn(null);
        GameOverCommand command = new GameOverCommand();

        assertThrows(NullPointerException.class, () -> command.execute(game));
    }


    // InstructionsCommand
    void setupInstructionsCommand(StateMenu stateMenu, MenuModel model) {
        when(game.getStateMenu()).thenReturn(stateMenu);
        when(stateMenu.getModel()).thenReturn(model);
    }

    @Test
    void executeSetsSelectedOptionToTrue() {
        StateMenu stateMenu = mock(StateMenu.class);
        MenuModel model = mock(MenuModel.class);
        setupInstructionsCommand(stateMenu, model);
        InstructionsCommand command = new InstructionsCommand();

        command.execute(game);

        verify(model).setSelectedOption(true);
    }

    @Test
    void executeSetsTextOptionWithInstructions() {
        StateMenu stateMenu = mock(StateMenu.class);
        MenuModel model = mock(MenuModel.class);
        setupInstructionsCommand(stateMenu, model);
        InstructionsCommand command = new InstructionsCommand();

        command.execute(game);

        verify(model).setTextOption("""
            upper arrow to jump
            right arrow to move right
            left arrow to move left
            down arrow to enter pipe
            b to throw fireballs
            q to pause
            """);
    }

    @Test
    void executeHandlesNullStateMenu() {
        when(game.getStateMenu()).thenReturn(null);
        InstructionsCommand command = new InstructionsCommand();

        assertThrows(NullPointerException.class, () -> command.execute(game));
    }

    @Test
    void executeHandlesNullModel() {
        StateMenu stateMenu = mock(StateMenu.class);
        when(stateMenu.getModel()).thenReturn(null);
        InstructionsCommand command = new InstructionsCommand();

        assertThrows(NullPointerException.class, () -> command.execute(game));
    }


    // PauseCommand
    @Test
    void executeSetsGameStateToPaused() {
        PauseCommand command = new PauseCommand();

        command.execute(game);

        verify(game).setStatePause();
    }

    // ResumeCommand
    @Test
    void executeSetsGameStateToResumed() {
        ResumeCommand command = new ResumeCommand();
        when(game.getStatePause()).thenReturn(mock(StatePause.class));

        command.execute(game);

        verify(game).setStateGame(any());
    }


    // StartGameCommand
    @Test
    void executeSetsGameStateToGame() throws IOException {
        StartGameCommand command = new StartGameCommand();
        when(game.getStateGame()).thenReturn(mock(StateGame.class));

        command.execute(game);

        verify(game).setStateGame();
    }

    @Test
    void executeSetsGameOverToFalse() throws IOException {
        StateGame gameState = mock(StateGame.class);
        when(game.getStateGame()).thenReturn(gameState);
        StartGameCommand command = new StartGameCommand();

        command.execute(game);

        verify(gameState).setGameOver(false);
    }

    @Test
    void executeHandlesIOException() throws IOException {
        doThrow(IOException.class).when(game).setStateGame();
        StartGameCommand command = new StartGameCommand();

        assertThrows(RuntimeException.class, () -> command.execute(game));
    }


    @Test
    void executeHandlesNullGame() {
        GameCommand command = new ExitGameCommand();

        assertThrows(NullPointerException.class, () -> command.execute(null));
    }


}
