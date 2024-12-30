package org.grupogjl.model.game.elements.generalobjects;

import org.grupogjl.model.game.elements.blocks.Pipe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.spy;

public class TestStaticObject {

    private StaticObject staticObject;

    @BeforeEach
    public void setUp() {
        staticObject = spy(new Pipe(10, 5, 1, 1));
    }

    @Test
    public void testSetsAndGets() {
        assertEquals(10, staticObject.getX());
        assertEquals(5, staticObject.getY());
        assertEquals(1, staticObject.getWidth());
        assertEquals(1, staticObject.getHeight());

        staticObject.setX(5);
        staticObject.setY(10);
        staticObject.setWidth(2);
        staticObject.setHeight(2);

        assertEquals(5, staticObject.getX());
        assertEquals(10, staticObject.getY());
        assertEquals(2, staticObject.getWidth());
        assertEquals(2, staticObject.getHeight());
    }
}
