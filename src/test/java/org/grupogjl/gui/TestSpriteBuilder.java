package org.grupogjl.gui;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TestSpriteBuilder {

    private SpriteBuilder spriteBuilder;

    @BeforeEach
    void setUp() {
        spriteBuilder = spy(new SpriteBuilder());
    }

    @Test
    void loadImageReturnsImageWhenInCache() {
        BufferedImage image = mock(BufferedImage.class);
        spriteBuilder.setToCache("test.png", image);

        BufferedImage result = spriteBuilder.loadImage("test.png");

        assertEquals(image, result);
    }

    @Test
    void loadImageReturnsNullWhenResourceNotFound() {
        BufferedImage result = spriteBuilder.loadImage("nonexistent.png");

        assertNull(result);
    }

    @Test
    void loadImageCachesImageAfterLoading() throws IOException {
        URL resource = getClass().getResource("/Sprites/goomba.png");
        BufferedImage image = ImageIO.read(resource);
        when(spriteBuilder.loadImage("goomba.png")).thenReturn(image);

        BufferedImage result = spriteBuilder.loadImage("goomba.png");

        assertTrue(spriteBuilder.isInCache("goomba.png"));
        assertEquals(image, result);
    }

    @Test
    void isInCacheReturnsTrueWhenImageIsInCache() {
        BufferedImage image = mock(BufferedImage.class);
        spriteBuilder.setToCache("test.png", image);

        assertTrue(spriteBuilder.isInCache("test.png"));
    }

    @Test
    void isInCacheReturnsFalseWhenImageIsNotInCache() {
        assertFalse(spriteBuilder.isInCache("nonexistent.png"));
    }
}
