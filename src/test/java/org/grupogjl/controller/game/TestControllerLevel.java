package org.grupogjl.controller.game;


import org.grupogjl.controller.game.blocks.ControllerBlocks;
import org.grupogjl.controller.game.physicalobjects.ControllerEnemy;
import org.grupogjl.controller.game.physicalobjects.ControllerFireBall;
import org.grupogjl.controller.game.physicalobjects.ControllerMario;
import org.grupogjl.controller.game.surprises.ControllerSurprises;
import org.grupogjl.gui.GeneralGui;
import org.grupogjl.model.game.elements.Mario;
import org.grupogjl.model.game.elements.blocks.BuildingBlock;
import org.grupogjl.model.game.elements.camera.Camera;
import org.grupogjl.model.game.elements.generalobjects.GameObject;
import org.grupogjl.model.game.elements.generalobjects.PhysicalObject;
import org.grupogjl.model.game.elements.generalobjects.StaticObject;
import org.grupogjl.model.game.elements.level.Level;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TestControllerLevel {
    private ControllerLevel controllerLevel;
    private Mario mario;
    private Camera camera;
    private PhysicalObject physicalObject;
    private StaticObject staticObject;
    private BuildingBlock block;
    List<GameObject> objects = new ArrayList<>();

    @BeforeEach
    void setUp() {
        controllerLevel = spy(new ControllerLevel());

        physicalObject = mock(PhysicalObject.class);
        staticObject = mock(StaticObject.class);
        block = mock(BuildingBlock.class);
        mario = mock(Mario.class);
        camera = mock(Camera.class);
    }

    @Test
    void testGetControllerMario() {
        assertNotNull(controllerLevel.getControllerMario());
    }

    @Test
    void testGetControllerEnemy() {
        assertNotNull(controllerLevel.getControllerEnemy());
    }

    @Test
    void testGetControllerBlocks() {
        assertNotNull(controllerLevel.getControllerBlocks());
    }

    @Test
    void testGetControllerSurprises() {
        assertNotNull(controllerLevel.getControllerSurprises());
    }

    @Test
    void testGetControllerFireBall() {
        assertNotNull(controllerLevel.getControllerFireBall());
    }

    @Test
    void testStep() {
        ControllerMario controllerMario = mock(ControllerMario.class);
        ControllerEnemy controllerEnemy = mock(ControllerEnemy.class);
        ControllerBlocks controllerBlocks = mock(ControllerBlocks.class);
        ControllerSurprises controllerSurprises = mock(ControllerSurprises.class);
        ControllerFireBall controllerFireBall = mock(ControllerFireBall.class);
        Level level = mock(Level.class);
        GeneralGui.ACTION action = GeneralGui.ACTION.NONE;
        long time = 100L;

        when(level.getMario()).thenReturn(mario);
        when(level.getCamera()).thenReturn(camera);
        when(level.getObjects()).thenReturn(objects);

        doReturn(controllerMario).when(controllerLevel).getControllerMario();
        doReturn(controllerEnemy).when(controllerLevel).getControllerEnemy();
        doReturn(controllerBlocks).when(controllerLevel).getControllerBlocks();
        doReturn(controllerSurprises).when(controllerLevel).getControllerSurprises();
        doReturn(controllerFireBall).when(controllerLevel).getControllerFireBall();

        controllerLevel.step(level, action, time);

        verify(controllerFireBall).step(level, action, time);
        verify(controllerMario).step(level, action, time);
        verify(controllerEnemy).step(level, time);
        verify(controllerSurprises).step(level, action, time);
        verify(controllerBlocks).step(level, action, time);
        verify(controllerLevel).checkCollisions(mario, objects, camera);
    }

    @Test
    void testCheckCollisions_withStaticObjects() {
        objects.add(staticObject);

        controllerLevel.checkCollisions(mario, objects, camera);

        verify(controllerLevel).CheckPhysicalCollisionsX(mario, objects, camera);
        verify(controllerLevel).CheckPhysicalCollisionsY(mario, objects);
        verify(controllerLevel, atMostOnce()).CheckPhysicalCollisionsX(any(PhysicalObject.class), eq(objects), eq(camera));
        verify(controllerLevel, atMostOnce()).CheckPhysicalCollisionsY(any(PhysicalObject.class), eq(objects));
    }

    @Test
    void testCheckCollisions_withPhysicalObjects() {
        objects.add(physicalObject);

        controllerLevel.checkCollisions(mario, objects, camera);

        verify(controllerLevel).CheckPhysicalCollisionsX(mario, objects, camera);
        verify(controllerLevel).CheckPhysicalCollisionsY(mario, objects);
        verify(controllerLevel).CheckPhysicalCollisionsX(physicalObject, objects, camera);
        verify(controllerLevel).CheckPhysicalCollisionsY(physicalObject, objects);
    }

    @Test
    void testCheckCollisions_withMixedObjects() {
        objects.add(staticObject);
        objects.add(physicalObject);

        controllerLevel.checkCollisions(mario, objects, camera);

        verify(controllerLevel).CheckPhysicalCollisionsX(mario, objects, camera);
        verify(controllerLevel).CheckPhysicalCollisionsY(mario, objects);
        verify(controllerLevel).CheckPhysicalCollisionsX(physicalObject, objects, camera);
        verify(controllerLevel).CheckPhysicalCollisionsY(physicalObject, objects);
        verify(controllerLevel, times(2)).CheckPhysicalCollisionsX(any(), eq(objects), eq(camera));
        verify(controllerLevel, times(2)).CheckPhysicalCollisionsY(any(), eq(objects));
    }

    @Test
    void testCheckPhysicalCollisionsY_blockBelowPreventsFalling() {
        objects.add(block);

        when(physicalObject.getX()).thenReturn(5.0f);
        when(physicalObject.getWidth()).thenReturn(2.0f);
        when(physicalObject.getY()).thenReturn(8.0f);
        when(physicalObject.isJumping()).thenReturn(false);

        when(block.getX()).thenReturn(4.0f);
        when(block.getWidth()).thenReturn(4.0f);
        when(block.getY()).thenReturn(10.0f);
        when(block.getHeight()).thenReturn(2.0f);

        controllerLevel.CheckPhysicalCollisionsY(physicalObject, objects);

        verify(physicalObject).setFalling(false);
        verify(physicalObject, never()).setFalling(true);
    }

    @Test
    void testCheckPhysicalCollisionsY_noBlockBelowCausesFalling() {
        objects.add(block);

        when(physicalObject.getX()).thenReturn(5.0f);
        when(physicalObject.getWidth()).thenReturn(2.0f);
        when(physicalObject.getY()).thenReturn(8.0f);
        when(physicalObject.isJumping()).thenReturn(false);

        when(block.getX()).thenReturn(10.0f);
        when(block.getWidth()).thenReturn(4.0f);
        when(block.getY()).thenReturn(10.0f);
        when(block.getHeight()).thenReturn(2.0f);

        controllerLevel.CheckPhysicalCollisionsY(physicalObject, objects);

        verify(physicalObject).setFalling(true);
        verify(physicalObject, never()).setFalling(false);
    }

    @Test
    void testCheckPhysicalCollisionsY_edgeX() {
        objects.add(block);

        when(physicalObject.getX()).thenReturn(5.0f);
        when(physicalObject.getWidth()).thenReturn(2.0f);
        when(physicalObject.getY()).thenReturn(8.0f);
        when(physicalObject.isJumping()).thenReturn(false);

        when(block.getX()).thenReturn(10.0f);
        when(block.getWidth()).thenReturn(4.0f);
        when(block.getY()).thenReturn(10.0f);
        when(block.getHeight()).thenReturn(2.0f);

        controllerLevel.CheckPhysicalCollisionsY(physicalObject, objects);

        verify(physicalObject).setFalling(true);
        verify(physicalObject, never()).setFalling(false);
    }

    @Test
    void testCheckPhysicalCollisionsY_objectJumping() {
        when(physicalObject.isJumping()).thenReturn(true);

        controllerLevel.CheckPhysicalCollisionsY(physicalObject, objects);

        verify(physicalObject, never()).setFalling(anyBoolean());
    }

    @Test
    void testCheckPhysicalCollisionsY_zeroVelocity() {
        when(physicalObject.getVy()).thenReturn(0f);
        when(physicalObject.getY()).thenReturn(10f);

        controllerLevel.CheckPhysicalCollisionsY(physicalObject, List.of(block));

        verify(physicalObject, never()).setY(anyFloat());
        verify(physicalObject, never()).handleCollision(any(), anyChar());
    }

    @Test
    void testCheckPhysicalCollisionsY_positiveVelocity_collisionDetected() {
        when(physicalObject.getVy()).thenReturn(5f);
        when(physicalObject.getY()).thenReturn(10f);
        when(physicalObject.collidesWith(block)).thenReturn(true);

        controllerLevel.CheckPhysicalCollisionsY(physicalObject, List.of(block));

        verify(physicalObject).setY(11.666667f); // New Y position after moving down
        verify(physicalObject).handleCollision(block, 'D');
    }

    @Test
    void testCheckPhysicalCollisionsY_negativeVelocity_collisionDetected() {
        when(physicalObject.getVy()).thenReturn(3f); // Moving up
        when(physicalObject.getY()).thenReturn(5f); // Initial Y position
        when(physicalObject.collidesWith(block)).thenReturn(true);
        when(physicalObject.isJumping()).thenReturn(true);

        controllerLevel.CheckPhysicalCollisionsY(physicalObject, List.of(block, block));

        // After collision, position should be updated based on collision
        verify(physicalObject, atLeastOnce()).setY(4f); // New Y position after moving up
        verify(physicalObject).handleCollision(block, 'U'); // Direction should be 'U' (up)
    }

    @Test
    void testCheckPhysicalCollisionsY_noCollision() {
        when(physicalObject.getVy()).thenReturn(3f); // Moving down
        when(physicalObject.getY()).thenReturn(5f); // Initial Y position
        when(physicalObject.collidesWith(block)).thenReturn(false); // No collision

        controllerLevel.CheckPhysicalCollisionsY(physicalObject, List.of(block));

        // Since there's no collision, the position should move by the full distance
        verify(physicalObject, times(4)).setY(anyFloat()); // Position changes in steps
    }

    @Test
    void testCheckPhysicalCollisionsY_positionResetOnNoCollision() {
        float initialPosition = 5f;
        when(physicalObject.getVy()).thenReturn(3f); // Moving down
        when(physicalObject.getY()).thenReturn(initialPosition); // Initial Y position
        when(physicalObject.collidesWith(physicalObject)).thenReturn(false); // No collision

        controllerLevel.CheckPhysicalCollisionsY(physicalObject, List.of(block));

        verify(physicalObject).setY(initialPosition); // Position should be reset
    }

    @Test
    void testLeftCameraLimitCollision() {
        // Setup object and camera state
        when(mario.getX()).thenReturn(5f); // Main object at x = 5
        when(mario.getVx()).thenReturn(-2f); // Moving left
        when(camera.getLeftCamLimit()).thenReturn(5f); // Camera left limit at x = 5

        // Call the method indirectly
        controllerLevel.CheckPhysicalCollisionsX(mario, List.of(), camera);

        // Verify wall collision handling was called with the correct limit
        verify(mario).handleWallColision(5f);
        verify(mario, never()).setX(anyFloat()); // No position update beyond collision
        verify(mario, never()).handleCollision(any(), anyChar()); // No object collision
    }

    @Test
    void testHorizontalCollision_noVelocity() {
        when(mario.getVx()).thenReturn(0f); // No horizontal movement

        controllerLevel.CheckPhysicalCollisionsX(mario, List.of(block), camera);

        verify(mario, never()).collidesWith(block); // Collision check should not occur
        verify(mario, never()).handleCollision(any(), anyChar()); // No collision handling
    }

    @Test
    void testHorizontalCollision_detectedMovingRight() {
        when(mario.getVx()).thenReturn(3f); // Moving right
        when(mario.getX()).thenReturn(10f); // Initial position
        when(mario.collidesWith(block)).thenReturn(false) // No collision initially
                .thenReturn(true); // Collision detected later

        controllerLevel.CheckPhysicalCollisionsX(mario, List.of(block), camera);

        verify(mario).handleCollision(block, 'R'); // Collision detected moving right
        verify(mario, times(2)).setX(anyFloat()); // Position updated twice (movement steps)
    }

    @Test
    void testHorizontalCollision_detectedMovingLeft() {
        when(mario.getVx()).thenReturn(-2f); // Moving left
        when(mario.getX()).thenReturn(15f); // Initial position
        when(mario.collidesWith(block)).thenReturn(false) // No collision initially
                .thenReturn(true); // Collision detected later

        controllerLevel.CheckPhysicalCollisionsX(mario, List.of(block), camera);

        verify(mario).handleCollision(block, 'L'); // Collision detected moving left
        verify(mario, times(2)).setX(anyFloat()); // Position updated twice (movement steps)
    }

    @Test
    void testHorizontalCollision_noCollision() {
        when(mario.getVx()).thenReturn(5f); // Moving right
        when(mario.getX()).thenReturn(20f); // Initial position
        when(mario.collidesWith(block)).thenReturn(false); // No collision

        controllerLevel.CheckPhysicalCollisionsX(mario, List.of(block), camera);

        verify(mario, times(5)).setX(anyFloat()); // Position updated for each movement step
        verify(mario, never()).handleCollision(any(), anyChar()); // No collision handling
        verify(mario, times(2)).setX(20f); // Reset position after no collision
    }

    @Test
    void testHorizontalCollision_edgeCaseZeroVelocity() {
        when(mario.getVx()).thenReturn(0f); // No movement
        when(mario.getX()).thenReturn(10f);

        controllerLevel.CheckPhysicalCollisionsX(mario, List.of(block), camera);

        verify(mario, atMostOnce()).setX(anyFloat()); // No movement occurs
        verify(mario, never()).handleCollision(any(), anyChar()); // No collision handling
    }

    @Test
    void testCheckPhysicalCollisionsY_noCollisionResetsPosition() {
        float startPosition = 10f;

        when(physicalObject.getVy()).thenReturn(3f); // Downward movement
        when(physicalObject.getY()).thenReturn(startPosition); // Initial Y position
        when(physicalObject.collidesWith(block)).thenReturn(false); // collision
        when(physicalObject.isJumping()).thenReturn(true);

        controllerLevel.CheckPhysicalCollisionsY(physicalObject, List.of(block));

        // Verify position is reset if no collision occurs
        verify(physicalObject).setY(startPosition);
    }

    @Test
    void testCollisionFromLeft() {
        PhysicalObject enemy = mock(PhysicalObject.class);

        when(mario.collidesWithPhysical(eq(enemy), anyFloat(), eq(-0.2f))).thenReturn(true);
        when(enemy.getX()).thenReturn(5f); // Enemy to the right
        when(mario.getX()).thenReturn(3f);
        when(mario.getY()).thenReturn(5f);
        when(enemy.getY()).thenReturn(4f);

        // Invoke the method
        controllerLevel.checkCollisions(mario, List.of(enemy), camera);

        // Verify collision is handled as 'L'
        verify(mario).handleCollision(enemy, 'L');
    }

    @Test
    void testCollisionFromRight() {
        PhysicalObject enemy = mock(PhysicalObject.class);

        when(mario.collidesWithPhysical(eq(enemy), anyFloat(), eq(-0.2f))).thenReturn(true);
        when(enemy.getX()).thenReturn(2f); // Enemy to the left
        when(mario.getX()).thenReturn(5f);
        when(mario.getY()).thenReturn(5f);
        when(enemy.getY()).thenReturn(4f);

        // Invoke the method
        controllerLevel.checkCollisions(mario, List.of(enemy), camera);

        // Verify collision is handled as 'R'
        verify(mario).handleCollision(enemy, 'R');
    }

    @Test
    void testNoCollision() {
        PhysicalObject enemy = mock(PhysicalObject.class);

        when(mario.collidesWithPhysical(eq(enemy), anyFloat(), eq(-0.2f))).thenReturn(false);

        // Invoke the method
        controllerLevel.checkCollisions(mario, List.of(enemy), camera);

        // Verify no collision handling
        verify(mario, never()).handleCollision(eq(enemy), anyChar());
    }

    @Test
    void testNoCollisionVerticalMisalignment() {
        PhysicalObject enemy = mock(PhysicalObject.class);

        when(mario.collidesWithPhysical(eq(enemy), anyFloat(), eq(-0.2f))).thenReturn(true);
        when(enemy.getX()).thenReturn(5f); // Enemy to the right
        when(mario.getX()).thenReturn(3f);
        when(mario.getY()).thenReturn(3f); // Main object is lower
        when(enemy.getY()).thenReturn(5f);

        // Invoke the method
        controllerLevel.checkCollisions(mario, List.of(enemy), camera);

        // Verify no collision handling
        verify(mario, never()).handleCollision(eq(enemy), anyChar());
    }

    @Test
    void testSetXCalledWhenNoCollision() {
        float startPosition = 10.0f;
        when(mario.getX()).thenReturn(startPosition);
        when(mario.getVx()).thenReturn(-3.0f);
        when(mario.collidesWith(any())).thenReturn(false);

        controllerLevel.CheckPhysicalCollisionsX(mario, List.of(physicalObject), camera);

        verify(mario).setX(startPosition);
    }
}