package org.grupogjl.viewer;

import org.grupogjl.gui.GeneralGui;
import org.grupogjl.model.game.elements.buttons.Button;
import org.grupogjl.model.game.elements.pause.PauseModel;
import org.grupogjl.state.StatePause;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Vector;

import static org.mockito.Mockito.*;

public class TestViewerPause extends SuperTestViewer {

    private StatePause statePause;
    private PauseModel pauseModel;

    void initMocks(){
        viewer = new ViewerPause();
        gui = mock(GeneralGui.class);
        statePause = mock(StatePause.class);
        pauseModel = mock(PauseModel.class);
    }

    void initReturns(){
        Button button1 = mock(Button.class);
        Button button2 = mock(Button.class);
        when(statePause.getModel()).thenReturn(pauseModel);
        when(pauseModel.getButtons()).thenReturn(new Vector<>(List.of(button1, button2)));
        when(button1.getText()).thenReturn("Button1");
        when(button2.getText()).thenReturn("Button2");
        when(pauseModel.getSelectedButton()).thenReturn((byte) 0);
    }


    @Test
    void drawClearsGui() throws IOException {
        when(pauseModel.getButtons()).thenReturn(new Vector<>());

        viewer.draw(statePause, gui);

        verify(gui).refresh();
    }

    @Test
    void drawDisplaysButtonsWithCorrectColors() throws IOException {
        viewer.draw(statePause, gui);

        verify(gui).drawMenuText(anyInt(), eq(89), eq("Button1"), eq("#ea9e22"));
        verify(gui).drawMenuText(anyInt(), eq(113), eq("Button2"), eq(""));
    }

    @Test
    void drawDisplaysButtonsWithCorrectPositions() throws IOException {
        viewer.draw(statePause, gui);

        verify(gui).drawMenuText(eq((416 - "Button1".length() * 8) / 2 + 1), eq(89), eq("Button1"), anyString());
        verify(gui).drawMenuText(eq((416 - "Button2".length() * 8) / 2 + 1), eq(113), eq("Button2"), anyString());
    }

    @Test
    void drawRefreshesGui() throws IOException {
        when(pauseModel.getButtons()).thenReturn(new Vector<>());

        viewer.draw(statePause, gui);

        verify(gui).refresh();
    }
}
