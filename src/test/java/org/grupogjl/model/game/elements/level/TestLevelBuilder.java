package org.grupogjl.model.game.elements.level;
import org.grupogjl.model.game.elements.camera.Camera;
import org.grupogjl.model.game.elements.generalobjects.GameObject;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TestLevelBuilder {

    @Test
    void testCreateLevel() throws IOException {
        LevelBuilder levelBuilderSpy = spy(LevelBuilder.class);

        doReturn(10).when(levelBuilderSpy).getWidth();
        doReturn(5).when(levelBuilderSpy).getHeight();
        doReturn(List.of(mock(GameObject.class))).when(levelBuilderSpy).createGameObjects();

        Level level = levelBuilderSpy.createLevel();

        assertNotNull(level);
        assertEquals(10, level.getWidth());
        assertEquals(5, level.getHeight());
        assertNotNull(level.getObjects());
        assertEquals(1, level.getObjects().size());
        assertNotNull(level.getCamera());
        assertInstanceOf(Camera.class, level.getCamera());

        verify(levelBuilderSpy).getWidth();
        verify(levelBuilderSpy).getHeight();
        verify(levelBuilderSpy).createGameObjects();
    }
}
