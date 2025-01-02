package org.grupogjl.model.game.elements.menu;

import org.grupogjl.Game;
import org.grupogjl.commands.GameCommand;
import org.grupogjl.model.game.elements.buttons.Button;
import org.grupogjl.model.game.elements.menu.MenuModel;
import org.grupogjl.model.game.elements.menu.MenuPauseModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Vector;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TestMenuPauseModel {


    private MenuPauseModel menuPauseModel;
    private Vector<Button> buttons;
    private Game game;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        menuPauseModel = spy(new MenuModel());
        game = mock(Game.class);
        GameCommand command = mock(GameCommand.class);
        doNothing().when(command).execute(game);

        buttons = new Vector<>();
        buttons.add(spy(new Button("string1", command)));
        buttons.add(spy(new Button("string2", command)));

        Field modelButtons = MenuPauseModel.class.getDeclaredField("buttons");
        modelButtons.setAccessible(true);
        modelButtons.set(menuPauseModel, buttons);
    }

    @Test
    void isSelectedOptionReturnsTrueWhenOptionIsSelected() {
        menuPauseModel.setSelectedOption(true);
        assertTrue(menuPauseModel.isSelectedOption());
    }

    @Test
    void isSelectedOptionReturnsFalseWhenOptionIsNotSelected() {
        menuPauseModel.setSelectedOption(false);
        assertFalse(menuPauseModel.isSelectedOption());
    }

    @Test
    void getTextOptionReturnsCorrectText() {
        menuPauseModel.setTextOption("Pause");
        assertEquals("Pause", menuPauseModel.getTextOption());
    }

    @Test
    void getButtonsReturnsButtonList() throws NoSuchFieldException, IllegalAccessException {
        assertEquals(buttons, menuPauseModel.getButtons());
    }

    @Test
    void getSelectedButtonReturnsCorrectIndex() {
        assertEquals(Byte.valueOf((byte) 0), menuPauseModel.getSelectedButton());
    }

    @Test
    void nextPositionIncrementsSelectedButton() {
        menuPauseModel.nextPosition();
        assertEquals(Byte.valueOf((byte) 1), menuPauseModel.getSelectedButton());
    }

    @Test
    void nextPositionIncrementsSelectedButtonA() {
        for(int i = 0; i < 10; i++){
            menuPauseModel.nextPosition();
        }
        assertEquals(Byte.valueOf((byte) 1), menuPauseModel.getSelectedButton());
    }

    @Test
    void lastPositionDecrementsSelectedButton() {
        menuPauseModel.nextPosition();
        menuPauseModel.lastPosition();
        assertEquals(Byte.valueOf((byte) 0), menuPauseModel.getSelectedButton());
    }

    @Test
    void lastPositionDecrementsSelectedButtonA() {
        for(int i = 0; i < 10; i++){
            menuPauseModel.lastPosition();
        }
        assertEquals(Byte.valueOf((byte) 0), menuPauseModel.getSelectedButton());
    }

    @Test
    void executeCallsOnPressedOnSelectedButton() {
        Button button = buttons.elementAt(0);
        menuPauseModel.execute(game);
        verify(button).OnPressed(game);
    }
}
