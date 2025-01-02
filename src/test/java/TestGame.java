import org.grupogjl.Game;
import org.grupogjl.gui.LanternaGui;
import org.grupogjl.state.StateGame;
import org.grupogjl.state.StateMenu;
import org.grupogjl.state.StatePause;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class TestGame {
    
    private Game game;
    LanternaGui gui;

    @BeforeEach
    void setUp() {
        gui = mock(LanternaGui.class);
        game = new Game(gui);
    }

    @Test
    void getInstanceInitializesGui(){
        assertNotNull(game.getGui());
    }

    @Test
    void getStateReturnsCurrentState(){
        assertInstanceOf(StateMenu.class, game.getState());
    }

    @Test
    void constructorWithGuiInitializesGui() {
        assertEquals(gui, game.getGui());
    }

    @Test
    void setStateMenuChangesStateToMenu(){
        game.setStateMenu();
        assertInstanceOf(StateMenu.class, game.getState());
    }

    @Test
    void setStateGameChangesStateToGame() throws IOException {
        game.setStateGame();
        assertInstanceOf(StateGame.class, game.getState());
    }

    @Test
    void setStatePauseChangesStateToPause() throws IOException {
        game.setStateGame();
        game.setStatePause();
        assertInstanceOf(StatePause.class, game.getState());
    }

    @Test
    void setStateNullChangesStateToNull(){
        game.setStateNull();
        assertNull(game.getState());
    }

    @Test
    void getStateGameReturnsStateGameInstance() throws IOException {
        game.setStateGame();
        assertInstanceOf(StateGame.class, game.getStateGame());
    }

    @Test
    void setStateGameWithParentChangesState() {
        StateGame parentState = mock(StateGame.class);
        game.setStateGame(parentState);
        assertEquals(parentState, game.getState());
    }

    @Test
    void getStateMenuReturnsStateMenuInstance() {
        game.setStateMenu();
        assertInstanceOf(StateMenu.class, game.getStateMenu());
    }

    @Test
    void getStatePauseReturnsStatePauseInstance() throws IOException {
        game.setStateGame();
        game.setStatePause();
        assertInstanceOf(StatePause.class, game.getStatePause());
    }
}
