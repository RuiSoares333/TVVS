package org.grupogjl.gui;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.Terminal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class TestLanternaGui {

    private LanternaGui gui;
    private Screen screen;
    private SpriteBuilder spriteBuilder;
    Terminal terminal;
    BufferedImage image;
    private TextGraphics textGraphics;

    @BeforeEach
    void setUp() {
        screen = mock(Screen.class);
        gui = spy(new LanternaGui(screen));
        spriteBuilder = mock(SpriteBuilder.class);
        terminal = mock(Terminal.class);
        image = mock(BufferedImage.class);
        gui.setSpriteBuilder(spriteBuilder);
        textGraphics = mock(TextGraphics.class);
        when(screen.newTextGraphics()).thenReturn(textGraphics);
        when(spriteBuilder.loadImage(anyString())).thenReturn(image);
    }

    @Test
    void getNextActionReturnsNoneWhenKeyStrokeIsNull() throws IOException {
        when(screen.pollInput()).thenReturn(null);

        assertEquals(GeneralGui.ACTION.NONE, gui.getNextAction());
    }

    @Test
    void getNextActionReturnsQuitWhenKeyStrokeIsEOF() throws IOException {
        KeyStroke keyStroke = mock(KeyStroke.class);
        when(keyStroke.getKeyType()).thenReturn(KeyType.EOF);
        when(screen.pollInput()).thenReturn(keyStroke);

        assertEquals(GeneralGui.ACTION.QUIT, gui.getNextAction());
    }

    @Test
    void getNextActionReturnsQuitWhenKeyStrokeIsCharacterQ() throws IOException {
        KeyStroke keyStroke = mock(KeyStroke.class);
        when(keyStroke.getKeyType()).thenReturn(KeyType.Character);
        when(keyStroke.getCharacter()).thenReturn('q');
        when(screen.pollInput()).thenReturn(keyStroke);

        assertEquals(GeneralGui.ACTION.QUIT, gui.getNextAction());
    }

    @Test
    void getNextActionReturnsUpWhenKeyStrokeIsArrowUp() throws IOException {
        KeyStroke keyStroke = mock(KeyStroke.class);
        when(keyStroke.getKeyType()).thenReturn(KeyType.ArrowUp);
        when(screen.pollInput()).thenReturn(keyStroke);

        assertEquals(GeneralGui.ACTION.UP, gui.getNextAction());
    }

    @Test
    void getNextActionReturnsRightWhenKeyStrokeIsArrowRight() throws IOException {
        KeyStroke keyStroke = mock(KeyStroke.class);
        when(keyStroke.getKeyType()).thenReturn(KeyType.ArrowRight);
        when(screen.pollInput()).thenReturn(keyStroke);

        assertEquals(GeneralGui.ACTION.RIGHT, gui.getNextAction());
    }

    @Test
    void getNextActionReturnsDownWhenKeyStrokeIsArrowDown() throws IOException {
        KeyStroke keyStroke = mock(KeyStroke.class);
        when(keyStroke.getKeyType()).thenReturn(KeyType.ArrowDown);
        when(screen.pollInput()).thenReturn(keyStroke);

        assertEquals(GeneralGui.ACTION.DOWN, gui.getNextAction());
    }

    @Test
    void getNextActionReturnsLeftWhenKeyStrokeIsArrowLeft() throws IOException {
        KeyStroke keyStroke = mock(KeyStroke.class);
        when(keyStroke.getKeyType()).thenReturn(KeyType.ArrowLeft);
        when(screen.pollInput()).thenReturn(keyStroke);

        assertEquals(GeneralGui.ACTION.LEFT, gui.getNextAction());
    }

    @Test
    void getNextActionReturnsThrowBallWhenKeyStrokeIsCharacterB() throws IOException {
        KeyStroke keyStroke = mock(KeyStroke.class);
        when(keyStroke.getKeyType()).thenReturn(KeyType.Character);
        when(keyStroke.getCharacter()).thenReturn('b');
        when(screen.pollInput()).thenReturn(keyStroke);

        assertEquals(GeneralGui.ACTION.THROWBALL, gui.getNextAction());
    }

    @Test
    void getNextActionReturnsThrowBallWhenKeyStrokeIsCharacterC() throws IOException {
        KeyStroke keyStroke = mock(KeyStroke.class);
        when(keyStroke.getKeyType()).thenReturn(KeyType.Character);
        when(keyStroke.getCharacter()).thenReturn('c');
        when(screen.pollInput()).thenReturn(keyStroke);

        assertEquals(GeneralGui.ACTION.NONE, gui.getNextAction());
    }

    @Test
    void getNextActionReturnsSelectWhenKeyStrokeIsEnter() throws IOException {
        KeyStroke keyStroke = mock(KeyStroke.class);
        when(keyStroke.getKeyType()).thenReturn(KeyType.Enter);
        when(screen.pollInput()).thenReturn(keyStroke);

        assertEquals(GeneralGui.ACTION.SELECT, gui.getNextAction());
    }

    @Test
    void getNextActionReturnsNoneWhenKeyStrokeIsNotRecognized() throws IOException {
        KeyStroke keyStroke = mock(KeyStroke.class);
        when(keyStroke.getKeyType()).thenReturn(KeyType.Backspace);
        when(screen.pollInput()).thenReturn(keyStroke);

        assertEquals(GeneralGui.ACTION.NONE, gui.getNextAction());
    }


    @Test
    void drawImageDrawsCorrectPixels() {
        when(spriteBuilder.loadImage("test.png")).thenReturn(image);
        when(image.getWidth()).thenReturn(2);
        when(image.getHeight()).thenReturn(2);
        when(image.getRGB(0, 0)).thenReturn(Color.RED.getRGB());
        when(image.getRGB(1, 0)).thenReturn(Color.GREEN.getRGB());
        when(image.getRGB(0, 1)).thenReturn(Color.BLUE.getRGB());
        when(image.getRGB(1, 1)).thenReturn(Color.BLACK.getRGB());
        when(spriteBuilder.loadImage("test.png")).thenReturn(image);

        gui.drawImage(1.0f, 1.0f, "test.png");

        verify(gui).drawPixel(16, 16, "#ff0000");
        verify(gui).drawPixel(17, 16, "#00ff00");
        verify(gui).drawPixel(16, 17, "#0000ff");
        verify(gui).drawPixel(17, 17, "#000000");
    }

    @Test
    void drawImageSkipsTransparentPixels() {
        when(spriteBuilder.loadImage("test.png")).thenReturn(image);
        when(image.getWidth()).thenReturn(2);
        when(image.getHeight()).thenReturn(2);
        when(image.getRGB(0, 0)).thenReturn(0x00FFFFFF); // Transparent pixel
        when(image.getRGB(1, 0)).thenReturn(Color.GREEN.getRGB());
        when(image.getRGB(0, 1)).thenReturn(Color.BLUE.getRGB());
        when(image.getRGB(1, 1)).thenReturn(Color.BLACK.getRGB());
        when(spriteBuilder.loadImage("test.png")).thenReturn(image);
        when(gui.isTransparent(image, 0, 0)).thenReturn(true);

        gui.drawImage(1.0f, 1.0f, "test.png");

        verify(gui, never()).drawPixel(16, 16, "#ffffff");
        verify(gui).drawPixel(17, 16, "#00ff00");
        verify(gui).drawPixel(16, 17, "#0000ff");
        verify(gui).drawPixel(17, 17, "#000000");
    }

    @Test
    void drawImageHandlesEmptyImage() {
        when(spriteBuilder.loadImage("test.png")).thenReturn(image);
        when(image.getWidth()).thenReturn(0);
        when(image.getHeight()).thenReturn(0);
        when(spriteBuilder.loadImage("empty.png")).thenReturn(image);

        gui.drawImage(1.0f, 1.0f, "empty.png");

        verify(gui, never()).drawPixel(anyInt(), anyInt(), anyString());
    }


    @Test
    void drawMenuImageDrawsCorrectPixels() {
        when(spriteBuilder.loadImage("menu.png")).thenReturn(image);
        when(image.getWidth()).thenReturn(2);
        when(image.getHeight()).thenReturn(2);
        when(image.getRGB(0, 0)).thenReturn(Color.RED.getRGB());
        when(image.getRGB(1, 0)).thenReturn(Color.GREEN.getRGB());
        when(image.getRGB(0, 1)).thenReturn(Color.BLUE.getRGB());
        when(image.getRGB(1, 1)).thenReturn(Color.BLACK.getRGB());

        gui.drawMenuImage(10, 10, "menu.png");

        verify(gui).drawPixel(10, 10, "#ff0000");
        verify(gui).drawPixel(11, 10, "#00ff00");
        verify(gui).drawPixel(10, 11, "#0000ff");
        verify(gui).drawPixel(11, 11, "#000000");
    }

    @Test
    void drawMenuImageUsesDefaultColorWhenProvided() {
        when(spriteBuilder.loadImage("menu.png")).thenReturn(image);
        when(image.getWidth()).thenReturn(2);
        when(image.getHeight()).thenReturn(2);
        when(image.getRGB(0, 0)).thenReturn(Color.RED.getRGB());
        when(image.getRGB(1, 0)).thenReturn(Color.GREEN.getRGB());
        when(image.getRGB(0, 1)).thenReturn(Color.BLUE.getRGB());
        when(image.getRGB(1, 1)).thenReturn(Color.BLACK.getRGB());

        gui.drawMenuImage(10, 10, "menu.png", "#123456");

        verify(gui).drawPixel(10, 10, "#123456");
        verify(gui).drawPixel(11, 10, "#123456");
        verify(gui).drawPixel(10, 11, "#123456");
        verify(gui).drawPixel(11, 11, "#123456");
    }

    @Test
    void drawMenuImageSkipsTransparentPixels() {
        when(spriteBuilder.loadImage("menu.png")).thenReturn(image);
        when(image.getWidth()).thenReturn(2);
        when(image.getHeight()).thenReturn(2);
        when(image.getRGB(0, 0)).thenReturn(0x00FFFFFF); // Transparent pixel
        when(image.getRGB(1, 0)).thenReturn(Color.GREEN.getRGB());
        when(image.getRGB(0, 1)).thenReturn(Color.BLUE.getRGB());
        when(image.getRGB(1, 1)).thenReturn(Color.BLACK.getRGB());
        when(gui.isTransparent(image, 0, 0)).thenReturn(true);

        gui.drawMenuImage(10, 10, "menu.png");

        verify(gui, never()).drawPixel(10, 10, "#ffffff");
        verify(gui).drawPixel(11, 10, "#00ff00");
        verify(gui).drawPixel(10, 11, "#0000ff");
        verify(gui).drawPixel(11, 11, "#000000");
    }

    @Test
    void drawMenuImageHandlesEmptyImage() {
        when(spriteBuilder.loadImage("empty.png")).thenReturn(image);
        when(image.getWidth()).thenReturn(0);
        when(image.getHeight()).thenReturn(0);

        gui.drawMenuImage(10, 10, "empty.png");

        verify(gui, never()).drawPixel(anyInt(), anyInt(), anyString());
    }


    @Test
    void drawMenuTextDrawsCorrectImages() {
        gui.drawMenuText(10, 10, "A.B:C-!");

        verify(gui).drawMenuImage(10, 10, "/Letters/A.png", "");
        verify(gui).drawMenuImage(18, 10, "/Letters/dot.png", "");
        verify(gui).drawMenuImage(26, 10, "/Letters/B.png", "");
        verify(gui).drawMenuImage(34, 10, "/Letters/doubledot.png", "");
        verify(gui).drawMenuImage(42, 10, "/Letters/C.png", "");
        verify(gui).drawMenuImage(50, 10, "/Letters/hifen.png", "");
        verify(gui).drawMenuImage(58, 10, "/Letters/exclamationMark.png", "");
    }

    @Test
    void drawMenuTextUsesDefaultColorWhenProvided() {

        gui.drawMenuText(10, 10, "A.B:C-!", "#123456");

        verify(gui).drawMenuImage(10, 10, "/Letters/A.png", "#123456");
        verify(gui).drawMenuImage(18, 10, "/Letters/dot.png", "#123456");
        verify(gui).drawMenuImage(26, 10, "/Letters/B.png", "#123456");
        verify(gui).drawMenuImage(34, 10, "/Letters/doubledot.png", "#123456");
        verify(gui).drawMenuImage(42, 10, "/Letters/C.png", "#123456");
        verify(gui).drawMenuImage(50, 10, "/Letters/hifen.png", "#123456");
        verify(gui).drawMenuImage(58, 10, "/Letters/exclamationMark.png", "#123456");
    }

    @Test
    void drawMenuTextHandlesSpacesCorrectly() {
        gui.drawMenuText(10, 10, "A B");

        verify(gui).drawMenuImage(10, 10, "/Letters/A.png", "");
        verify(gui, never()).drawMenuImage(18, 10, "/Letters/ .png", "");
        verify(gui).drawMenuImage(26, 10, "/Letters/B.png", "");
    }

    @Test
    void drawMenuTextHandlesEmptyString() {
        gui.drawMenuText(10, 10, "");

        verify(gui, never()).drawMenuImage(anyInt(), anyInt(), anyString(), anyString());
    }

    @Test
    void drawGameOverClearsScreenAndDrawsImage() throws IOException {
        gui.drawGameOver();

        verify(gui).clear();
        verify(gui).drawMenuImage(65, 89, "gameOver.png");
        verify(gui).refresh();
    }

    @Test
    void clearFillsScreenWithBackgroundColor() {
        gui.clear();

        verify(textGraphics).setBackgroundColor(TextColor.Factory.fromString("#6c9cfc"));
        verify(textGraphics).fillRectangle(new TerminalPosition(0, 0), screen.getTerminalSize(), ' ');
    }

    @Test
    void refreshRefreshesScreen() throws IOException {
        gui.refresh();

        verify(screen).refresh();
    }
}
