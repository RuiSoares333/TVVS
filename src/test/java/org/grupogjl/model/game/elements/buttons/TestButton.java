package org.grupogjl.model.game.elements.buttons;

import org.grupogjl.Game;
import org.grupogjl.commands.GameCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class TestButton {

    private GameCommand command;
    private Button button;

    @BeforeEach
    void setUp() {
        command = mock(GameCommand.class);
        button = spy(new Button("Start", command));
    }

    @Test
    void onPressedExecutesCommand() {
        Game game = mock(Game.class);

        button.OnPressed(game);

        verify(command).execute(game);
    }

    @Test
    void getTextReturnsCorrectText() {
        assertEquals("Start", button.getText());
    }

}
