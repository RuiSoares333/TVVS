package org.grupogjl.model.game.elements.generalobjects;

import org.grupogjl.model.game.elements.enemies.Goomba;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TestPhysicalObject {

    private PhysicalObject obj;
    private StaticObject other;
    private PhysicalObject otherobj;

    @BeforeEach
    void setUp() {
        obj = spy(new Goomba(10, 10, 1, 1));
        other = mock(StaticObject.class);
        otherobj = mock(PhysicalObject.class);
    }

    @Test
    void testSetsAndGets(){
        obj.setHeight(1);
        obj.setWidth(2);
        obj.setX(3);
        obj.setY(4);
        obj.setVx(5);
        obj.setVy(6);
        obj.setG(7);
        obj.setFalling(true);
        obj.setJumping(true);

        assertEquals(1, obj.getHeight());
        assertEquals(2, obj.getWidth());
        assertEquals(3, obj.getX());
        assertEquals(4, obj.getY());
        assertEquals(5, obj.getVx());
        assertEquals(6, obj.getVy());
        assertEquals(7, obj.getG());
        assertTrue(obj.isFalling());
        assertTrue(obj.isJumping());
    }

    @Test
    void updateLocationWhenJumpingUpdatesYAndVy() {
        obj.setJumping(true);
        obj.setVy(5);

        obj.updateLocation();

        assertEquals(5, obj.getY(), 0.01);
        assertEquals(4.77, obj.getVy(), 0.01);
    }

    @Test
    void updateLocationWhenFallingUpdatesYAndVy() {
        obj.setFalling(true);
        obj.setVy(1);

        obj.updateLocation();

        assertEquals(11, obj.getY(), 0.01);
        assertEquals(1.23, obj.getVy(), 0.01);
    }

    @Test
    void checkMaximumVert() {
        obj.setFalling(true);
        obj.setVy(2);

        obj.updateLocation();

        assertEquals(1.4f, obj.getVy(), 0.01);
    }

    @Test
    void updateLocationWhenNotJumpingOrFallingResetsVy() {
        obj.setVy(1);

        obj.updateLocation();

        assertEquals(0, obj.getVy(), 0.01);
    }

    @Test
    void updateLocationWhenJumpingAndVyLessThanOrEqualToZeroSetsFalling() {
        obj.setJumping(true);
        obj.setVy(0);

        obj.updateLocation();

        assertFalse(obj.isJumping());
        assertTrue(obj.isFalling());
    }

    @Test
    void collidesWithStaticWhenOverlappingReturnsTrue() {
        when(other.getX()).thenReturn(5.0f);
        when(other.getY()).thenReturn(5.0f);
        when(other.getWidth()).thenReturn(2.0f);
        when(other.getHeight()).thenReturn(2.0f);

        obj.setX(4.0f);
        obj.setY(6.0f);
        obj.setWidth(2.0f);
        obj.setHeight(2.0f);

        assertTrue(obj.collidesWithStatic(other));
    }

    @Test
    void collidesWithStaticWhenNotOverlappingReturnsFalse() {
        when(other.getX()).thenReturn(10.0f);
        when(other.getY()).thenReturn(10.0f);
        when(other.getWidth()).thenReturn(2.0f);
        when(other.getHeight()).thenReturn(2.0f);

        obj.setX(4.0f);
        obj.setY(6.0f);
        obj.setWidth(2.0f);
        obj.setHeight(2.0f);

        assertFalse(obj.collidesWithStatic(other));
    }

    @Test
    void collidesWithStaticWhenNotOverlappingReturnsFalseA() {
        when(other.getX()).thenReturn(3.0f);
        when(other.getY()).thenReturn(10.0f);
        when(other.getWidth()).thenReturn(2.0f);
        when(other.getHeight()).thenReturn(2.0f);

        obj.setX(10.0f);
        obj.setY(6.0f);
        obj.setWidth(2.0f);
        obj.setHeight(2.0f);

        assertFalse(obj.collidesWithStatic(other));
    }

    @Test
    void collidesWithStaticWhenNotOverlappingReturnsFalseB() {
        when(other.getX()).thenReturn(3.0f);
        when(other.getY()).thenReturn(10.0f);
        when(other.getWidth()).thenReturn(2.0f);
        when(other.getHeight()).thenReturn(2.0f);

        obj.setX(4.0f);
        obj.setY(0.0f);
        obj.setWidth(2.0f);
        obj.setHeight(2.0f);

        assertFalse(obj.collidesWithStatic(other));
    }

    @Test
    void collidesWithStaticWhenNotOverlappingReturnsFalseC() {
        when(other.getX()).thenReturn(3.0f);
        when(other.getY()).thenReturn(10.0f);
        when(other.getWidth()).thenReturn(2.0f);
        when(other.getHeight()).thenReturn(2.0f);

        obj.setX(4.0f);
        obj.setY(20.0f);
        obj.setWidth(2.0f);
        obj.setHeight(2.0f);

        assertFalse(obj.collidesWithStatic(other));
    }

    @Test
    void collidesWithStaticWhenTouchingEdgesReturnsTrue() {
        when(other.getX()).thenReturn(5.0f);
        when(other.getY()).thenReturn(6.0f);
        when(other.getWidth()).thenReturn(2.0f);
        when(other.getHeight()).thenReturn(2.0f);

        obj.setX(4.0f);
        obj.setY(6.0f);
        obj.setWidth(2.0f);
        obj.setHeight(2.0f);

        assertTrue(obj.collidesWithStatic(other));
    }

    @Test
    void collidesWithStaticWhenOtherObjectIsInsideReturnsTrue() {
        when(other.getX()).thenReturn(5.5f);
        when(other.getY()).thenReturn(5.5f);
        when(other.getWidth()).thenReturn(1.0f);
        when(other.getHeight()).thenReturn(1.0f);

        obj.setX(5.0f);
        obj.setY(6.0f);
        obj.setWidth(2.0f);
        obj.setHeight(2.0f);

        assertTrue(obj.collidesWithStatic(other));
    }

    @Test
    void collidesWithStaticWhenThisObjectIsInsideReturnsTrue() {
        when(other.getX()).thenReturn(4.0f);
        when(other.getY()).thenReturn(6.0f);
        when(other.getWidth()).thenReturn(4.0f);
        when(other.getHeight()).thenReturn(4.0f);

        obj.setX(5.0f);
        obj.setY(6.0f);
        obj.setWidth(2.0f);
        obj.setHeight(2.0f);

        assertTrue(obj.collidesWithStatic(other));
    }



    @Test
    void collidesWithPhysicalWhenOverlappingReturnsTrue() {
        when(otherobj.getX()).thenReturn(5.0f);
        when(otherobj.getY()).thenReturn(5.0f);
        when(otherobj.getWidth()).thenReturn(2.0f);
        when(otherobj.getHeight()).thenReturn(2.0f);

        obj.setX(4.0f);
        obj.setY(6.0f);
        obj.setWidth(2.0f);
        obj.setHeight(2.0f);

        assertTrue(obj.collidesWithPhysical(otherobj, 0, 0));
    }

    @Test
    void collidesWithPhysicalWhenNotOverlappingReturnsFalse() {
        when(otherobj.getX()).thenReturn(10.0f);
        when(otherobj.getY()).thenReturn(10.0f);
        when(otherobj.getWidth()).thenReturn(2.0f);
        when(otherobj.getHeight()).thenReturn(2.0f);

        obj.setX(4.0f);
        obj.setY(6.0f);
        obj.setWidth(2.0f);
        obj.setHeight(2.0f);

        assertFalse(obj.collidesWithPhysical(otherobj, 0, 0));
    }

    @Test
    void collidesWithPhysicalWhenNotOverlappingReturnsFalseA() {
        when(otherobj.getX()).thenReturn(4f);
        when(otherobj.getY()).thenReturn(10.0f);
        when(otherobj.getWidth()).thenReturn(2.0f);
        when(otherobj.getHeight()).thenReturn(2.0f);

        obj.setX(10.0f);
        obj.setY(6.0f);
        obj.setWidth(2.0f);
        obj.setHeight(2.0f);

        assertFalse(obj.collidesWithPhysical(otherobj, 0, 0));
    }

    @Test
    void collidesWithPhysicalWhenNotOverlappingReturnsFalseB() {
        when(otherobj.getX()).thenReturn(4f);
        when(otherobj.getY()).thenReturn(10f);
        when(otherobj.getWidth()).thenReturn(2.0f);
        when(otherobj.getHeight()).thenReturn(2.0f);

        obj.setX(4.0f);
        obj.setY(6.0f);
        obj.setWidth(2.0f);
        obj.setHeight(2.0f);

        assertFalse(obj.collidesWithPhysical(otherobj, 0, 0));
    }

    @Test
    void collidesWithPhysicalWhenNotOverlappingReturnsFalseC() {
        when(otherobj.getX()).thenReturn(4f);
        when(otherobj.getY()).thenReturn(6f);
        when(otherobj.getWidth()).thenReturn(2.0f);
        when(otherobj.getHeight()).thenReturn(2.0f);

        obj.setX(4.0f);
        obj.setY(10.0f);
        obj.setWidth(2.0f);
        obj.setHeight(2.0f);

        assertFalse(obj.collidesWithPhysical(otherobj, 0, 0));
    }

    @Test
    void collidesWithPhysicalWhenOtherObjectIsInsideReturnsTrue() {
        when(otherobj.getX()).thenReturn(5.5f);
        when(otherobj.getY()).thenReturn(5.5f);
        when(otherobj.getWidth()).thenReturn(1.0f);
        when(otherobj.getHeight()).thenReturn(1.0f);

        obj.setX(5.0f);
        obj.setY(6.0f);
        obj.setWidth(2.0f);
        obj.setHeight(2.0f);

        assertTrue(obj.collidesWithPhysical(otherobj, 0, 0));
    }

    @Test
    void collidesWithPhysicalWhenThisObjectIsInsideReturnsTrue() {
        when(otherobj.getX()).thenReturn(4.0f);
        when(otherobj.getY()).thenReturn(6.0f);
        when(otherobj.getWidth()).thenReturn(4.0f);
        when(otherobj.getHeight()).thenReturn(4.0f);

        obj.setX(5.0f);
        obj.setY(6.0f);
        obj.setWidth(2.0f);
        obj.setHeight(2.0f);

        assertTrue(obj.collidesWithPhysical(otherobj, 0, 0));
    }

    @Test
    void collidesWithPhysicalWithOffsetReturnsTrue() {
        when(otherobj.getX()).thenReturn(5.0f);
        when(otherobj.getY()).thenReturn(5.0f);
        when(otherobj.getWidth()).thenReturn(2.0f);
        when(otherobj.getHeight()).thenReturn(2.0f);

        obj.setX(4.0f);
        obj.setY(6.0f);
        obj.setWidth(2.0f);
        obj.setHeight(2.0f);

        assertTrue(obj.collidesWithPhysical(otherobj, 1.0f, 1.0f));
    }

    @Test
    void collidesWithPhysicalWithNegativeOffsetReturnsFalse() {
        when(otherobj.getX()).thenReturn(5.0f);
        when(otherobj.getY()).thenReturn(5.0f);
        when(otherobj.getWidth()).thenReturn(2.0f);
        when(otherobj.getHeight()).thenReturn(2.0f);

        obj.setX(4.0f);
        obj.setY(6.0f);
        obj.setWidth(2.0f);
        obj.setHeight(2.0f);

        assertFalse(obj.collidesWithPhysical(otherobj, -1.0f, -1.0f));
    }

    @Test
    void collidesWithPhysical(){
        when(otherobj.getX()).thenReturn(5.0f);
        when(otherobj.getY()).thenReturn(5.0f);
        when(otherobj.getWidth()).thenReturn(2.0f);
        when(otherobj.getHeight()).thenReturn(2.0f);

        obj.setX(4.0f);
        obj.setY(6.0f);
        obj.setWidth(2.0f);
        obj.setHeight(2.0f);

        obj.collidesWith(otherobj);

        verify(obj).collidesWithPhysical(any(), anyFloat(), anyFloat());
    }

    @Test
    void collidesWithStatic(){
        when(other.getX()).thenReturn(5.0f);
        when(other.getY()).thenReturn(5.0f);
        when(other.getWidth()).thenReturn(2.0f);
        when(other.getHeight()).thenReturn(2.0f);

        obj.setX(4.0f);
        obj.setY(6.0f);
        obj.setWidth(2.0f);
        obj.setHeight(2.0f);

        obj.collidesWith(other);

        verify(obj).collidesWithStatic(any());
    }
}
