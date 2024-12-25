package org.grupogjl.model.game.elements.level;

import org.grupogjl.model.game.elements.blocks.*;
import org.grupogjl.model.game.elements.enemies.Goomba;
import org.grupogjl.model.game.elements.enemies.KoopaTroopa;
import org.grupogjl.model.game.elements.generalobjects.GameObject;
import org.grupogjl.model.game.elements.surprises.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TestLoaderLevelBuilder {

    @Mock
    private BufferedReader mockBufferedReader = mock(BufferedReader.class);

    @Test
    void testReadLines() throws IOException {
        when(mockBufferedReader.readLine())
                .thenReturn("#p?", "g$+", null);

        LoaderLevelBuilder builder = new LoaderLevelBuilder(1);

        List<String> lines = builder.readLines(mockBufferedReader);

        assertEquals(2, lines.size());
        assertEquals("#p?", lines.get(0));
        assertEquals("g$+", lines.get(1));
        verify(mockBufferedReader, times(3)).readLine();
    }

    @Test
    void testGetWidth() throws IOException {
        LoaderLevelBuilder builder = new LoaderLevelBuilder(1) {
            @Override
            public List<String> readLines(BufferedReader br) throws IOException {
                return List.of("####", "#p+", "#");
            }
        };

        assertEquals(4, builder.getWidth());
    }

    @Test
    void testGetHeight() throws IOException {
        LoaderLevelBuilder builder = new LoaderLevelBuilder(1) {
            @Override
            public List<String> readLines(BufferedReader br) throws IOException {
                return List.of("####", "#p+", "#");
            }
        };

        assertEquals(3, builder.getHeight());
    }

    @Test
    void testCreateGameObjects() throws IOException {
        List<GameObject> objects = getGameObjects();

        assertEquals(12, objects.size());
        assertInstanceOf(Pipe.class, objects.get(0));
        assertInstanceOf(Pipe.class, objects.get(1));
        assertInstanceOf(Pipe.class, objects.get(2));
        assertInstanceOf(Pipe.class, objects.get(3));
        assertInstanceOf(Pipe.class, objects.get(4));
        assertInstanceOf(Pipe.class, objects.get(5));
        assertInstanceOf(UnbreakableBlock.class, objects.get(6));
        assertInstanceOf(DestroyableBlock.class, objects.get(7));
        assertInstanceOf(GoalBlock.class, objects.get(8));
        assertInstanceOf(SurpriseBlock.class, objects.get(9));
        assertInstanceOf(KoopaTroopa.class, objects.get(10));
        assertInstanceOf(Goomba.class, objects.get(11));

        Pipe pipe1 = (Pipe) objects.getFirst();
        assertEquals(4, pipe1.getX());
        assertEquals(0, pipe1.getY());
        assertEquals(2, pipe1.getWidth());
        assertEquals(2, pipe1.getHeight());
    }

    private static List<GameObject> getGameObjects() throws IOException {
        LoaderLevelBuilder builder = new LoaderLevelBuilder(1) {
            @Override
            public List<String> readLines(BufferedReader br) throws IOException {
                return List.of("#$+?uvwxyzp", "g");
            }
        };

        List<GameObject> objects = builder.createGameObjects();
        return objects;
    }

    @Test
    void testRandomSurprise() throws IOException {
        LoaderLevelBuilder builder = new LoaderLevelBuilder(1);
        when(mockBufferedReader.readLine()).thenReturn("?????");

        boolean missingMushroom1UP = true,
                missingMushroomSuper = true,
                missingCoin = true,
                missingFlower = true,
                missingStar = true;

        while(missingMushroom1UP || missingMushroomSuper || missingCoin || missingFlower || missingStar) {
            List<GameObject> objects = builder.createGameObjects();
            for (GameObject object : objects) {
                if (object instanceof SurpriseBlock surpriseBlock) {
                    switch (surpriseBlock.getSurprise().getClass().getSimpleName()) {
                        case "Mushroom1UP" -> missingMushroom1UP = false;
                        case "MushroomSuper" -> missingMushroomSuper = false;
                        case "Coin" -> missingCoin = false;
                        case "Flower" -> missingFlower = false;
                        case "Star" -> missingStar = false;
                    }
                }
            }
        }
    }

    @Test
    void testConnectPipes() throws IOException {
        Pipe pipe1 = mock(Pipe.class);
        Pipe pipe2 = mock(Pipe.class);
        GameObject otherObject = mock(GameObject.class);

        List<GameObject> objects = List.of(pipe1, pipe2, otherObject);

        LoaderLevelBuilder builder = new LoaderLevelBuilder(1);

        List<GameObject> connectedObjects = builder.connectPipes(objects);

        verify(pipe1).setConection(pipe2);
        verify(pipe2).setConection(pipe2);
        assertEquals(3, connectedObjects.size());
    }

    @Test
    void testPipeCharacterHeightMapping() throws IOException {
        LoaderLevelBuilder builder = new LoaderLevelBuilder(1) {
            @Override
            public List<String> readLines(BufferedReader br) {
                return List.of("uvwxyz");
            }
        };

        List<GameObject> objects = builder.createGameObjects();

        // Assert the correct number of pipes is created
        assertEquals(6, objects.size());
        for (int i = 0; i < 6; i++) {
            GameObject object = objects.get(i);
            assertInstanceOf(Pipe.class, object);
            Pipe pipe = (Pipe) object;

            int expectedHeight = 2 + i; // 'u' -> 2, 'v' -> 3, ..., 'z' -> 7
            assertEquals(expectedHeight, pipe.getHeight(), "Pipe height does not match for character: " + (char) ('u' + i));
        }
    }
}
