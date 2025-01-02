package org.grupogjl.viewer;

import org.grupogjl.gui.GeneralGui;
import org.grupogjl.model.game.elements.buttons.Button;
import org.grupogjl.model.game.elements.menu.MenuModel;
import org.grupogjl.state.StateMenu;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Vector;
import java.io.IOException;

import static org.mockito.Mockito.*;

public class TestViewerMenu extends SuperTestViewer {

    private GeneralGui gui;
    private StateMenu stateMenu;
    private MenuModel menuModel;
    private Button button1;
    private Button button2;

    void initMocks(){
        viewer = new ViewerMenu();
        gui = mock(GeneralGui.class);
        stateMenu = mock(StateMenu.class);
        menuModel = mock(MenuModel.class);
        button1 = mock(Button.class);
        button2 = mock(Button.class);
    }

    void initReturns(){
        when(stateMenu.getModel()).thenReturn(menuModel);
        when(menuModel.getButtons()).thenReturn(new Vector<>(List.of(button1, button2)));
        when(button1.getText()).thenReturn("Button1");
        when(button2.getText()).thenReturn("Button2");
        when(menuModel.getSelectedButton()).thenReturn((byte) 0);
    }

    @Test
    void drawClearsGui() throws IOException {
        when(menuModel.isSelectedOption()).thenReturn(false);

        viewer.draw(stateMenu, gui);

        verify(gui).clear();
    }

    @Test
    void drawDisplaysSelectedOptionText() throws IOException {
        when(menuModel.isSelectedOption()).thenReturn(true);
        when(menuModel.getTextOption()).thenReturn("Option1\nOption2");

        viewer.draw(stateMenu, gui);
        int base = 12;

        verify(gui).drawMenuText(181, base, "Option1", "");
        verify(gui).drawMenuText(181, base * 2, "Option2", "");
        verify(gui).drawMenuText(89, base * 4, "press enter to go back to menu", "");
    }

    @Test
    void drawDisplaysMenuButtons() throws IOException {
        viewer.draw(stateMenu, gui);

        verify(gui).drawMenuText(181, 121, "Button1", "#ea9e22");
        verify(gui).drawMenuText(181, 145, "Button2", "");
    }

    @Test
    void drawDisplaysMenuImage() throws IOException {
        when(menuModel.isSelectedOption()).thenReturn(false);

        viewer.draw(stateMenu, gui);

        verify(gui).drawMenuImage(121, 17, "MenuScreen.png");
    }

    @Test
    void drawRefreshesGui() throws IOException {
        when(menuModel.isSelectedOption()).thenReturn(false);

        viewer.draw(stateMenu, gui);

        verify(gui).refresh();
    }
}
