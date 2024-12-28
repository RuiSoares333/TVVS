package org.grupogjl.model.game.elements;

import org.grupogjl.model.game.elements.blocks.BuildingBlock;
import org.grupogjl.model.game.elements.blocks.GoalBlock;
import org.grupogjl.model.game.elements.blocks.InteractableBlock;
import org.grupogjl.model.game.elements.camera.Camera;
import org.grupogjl.model.game.elements.enemies.Enemy;
import org.grupogjl.model.game.elements.generalobjects.GameObject;
import org.grupogjl.model.game.elements.surprises.Surprise;
import org.grupogjl.state.StateGame;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TestMario {

    private Mario mario;
    private StateGame observer;

    private final float MARIO_X = 1.0f;
    private final float MARIO_Y = 2.0f;
    private final float MARIO_WIDTH = 3.0f;
    private final float MARIO_HEIGHT = 4.0f;

    @BeforeEach
    void setUp() {
        mario = spy(new Mario(MARIO_X, MARIO_Y, MARIO_WIDTH, MARIO_HEIGHT));
        observer = mock(StateGame.class);
        mario.setObserver(observer);
    }

    @Test
    void constructorInitializesMarioWithGivenValues() {
        assertEquals(MARIO_X, mario.getX());
        assertEquals(MARIO_Y, mario.getY());
        assertEquals(MARIO_WIDTH, mario.getWidth());
        assertEquals(MARIO_HEIGHT, mario.getHeight());
        assertEquals(3, mario.getLives());
        assertEquals(0, mario.getCoins());
    }

    @Test
    void setAndGetInvencibleTime() {
        mario.setInvencibleTime(10);

        assertEquals(10, mario.getInvencibleTime());
    }

    @Test
    void setAndGetStateInvencible() {
        mario.setStateInvencible(true);

        assertTrue(mario.isStateInvencible());
    }

    @Test
    void setAndGetCoins() {
        mario.setCoins(5);

        assertEquals(5, mario.getCoins());
    }

    @Test
    void setAndGetObserver() throws IOException {
        mario.notifyState("lives");
        verify(observer).resetLevel();
    }

    @Test
    void setAndGetHitCooldown() {
        mario.setHitCooldown(true);

        assertTrue(mario.isHitCooldown());
    }

    @Test
    void setAndGetStateFire() {
        mario.setStateFire(true);

        assertTrue(mario.isStateFire());
    }

    @Test
    void setAndGetStateBig() {
        mario.setStateBig(true);

        assertTrue(mario.isStateBig());
    }

    @Test
    void handleWallCollisionSetsXAndVx() {
        mario.handleWallColision(5.0f);

        assertEquals(5.0f, mario.getX());
        assertEquals(0, mario.getVx());
    }

    @Test
    void jumpSetsJumpingAndVyWhenNotJumpingOrFalling() {
        mario.setJumping(false);
        mario.setFalling(false);

        mario.jump();

        assertTrue(mario.isJumping());
        assertEquals(1.3f, mario.getVy());
    }

    @Test
    void jumpDoesNothingWhenJumping() {
        mario.setJumping(true);

        mario.jump();

        assertTrue(mario.isJumping());
        assertNotEquals(1.3f, mario.getVy());
    }

    @Test
    void jumpDoesNothingWhenFalling() {
        mario.setFalling(true);

        mario.jump();

        assertTrue(mario.isFalling());
        assertNotEquals(1.3f, mario.getVy());
    }

    @Test
    void moveLeftSetsNegativeVx() {
        mario.moveLeft();

        assertEquals(-0.5f, mario.getVx());
    }

    @Test
    void moveRightSetsPositiveVx() {
        mario.moveRight();

        assertEquals(0.5f, mario.getVx());
    }

    @Test
    void getAndSetLives() {
        mario.setLives(5);

        assertEquals(5, mario.getLives());
    }

    @Test
    void notifyStateCallsObserverResetLevelOnLivesNotification() throws IOException {
        mario.notifyState("lives");
        verify(observer).resetLevel();
    }

    @Test
    void notifyStateCallsObserverNextLevelOnGoalNotification() throws IOException {
        mario.notifyState("goal");
        verify(observer).nextLevel();
    }

    @Test
    void notifyStateCallsObserverButNoMatchFound() throws IOException {
        mario.notifyState("nomatch");

        verify(observer, never()).resetLevel();
        verify(observer, never()).nextLevel();
    }

    @Test
    void notifyStateThrowsRuntimeExceptionOnIOException() throws IOException {
        doThrow(IOException.class).when(observer).resetLevel();
        mario.setObserver(observer);

        assertThrows(RuntimeException.class, () -> mario.notifyState("lives"));
    }


    @Test
    void handleCollisionWithSurpriseActivatesAndDeactivatesSurprise() {
        Surprise surprise = mock(Surprise.class);

        mario.handleCollision(surprise, 'U');

        verify(surprise).activate(mario);
        verify(surprise).setActivated(false);
    }

    @Test
    void handleCollisionWithEnemyWhenInvencibleAndNotInCooldownKillsEnemy() {
        Enemy enemy = mock(Enemy.class);
        mario.setStateInvencible(true);
        mario.setHitCooldown(false);

        mario.handleCollision(enemy, 'U');

        verify(enemy).setLives(0);
        assertEquals(0, mario.getVx());
    }

    @Test
    void handleCollisionWithEnemyWhenNotInvencibleAndNotJumpingResetsMario() {
        Enemy enemy = mock(Enemy.class);
        mario.setStateInvencible(false);
        mario.setStateBig(false);
        mario.setStateFire(false);

        mario.handleCollision(enemy, 'U');

        verify(enemy, never()).setLives(anyInt());
        assertEquals(2, mario.getLives());
    }

    @Test
    void handleCollisionWithEnemyWhenJumpingDecreasesEnemyLives() {
        Enemy enemy = mock(Enemy.class);
        mario.setStateInvencible(false);

        mario.handleCollision(enemy, 'D');

        verify(enemy).setLives(anyInt());
        assertTrue(mario.isJumping());
        assertFalse(mario.isFalling());
        assertEquals(0.7f, mario.getVy());
    }

    @Test
    void handleCollisionWithEnemyWhenBigOrFireSetsMarioToSmallAndInvencible() {
        Enemy enemy = mock(Enemy.class);
        mario.setStateBig(true);
        mario.setStateFire(true);

        mario.handleCollision(enemy, 'U');

        assertFalse(mario.isStateBig());
        assertFalse(mario.isStateFire());
        assertEquals(MARIO_HEIGHT / 2, mario.getHeight());
        assertEquals(15, mario.getInvencibleTime());
        assertTrue(mario.isStateInvencible());
        assertTrue(mario.isHitCooldown());
    }

    @Test
    void handleCollisionWithEnemyWhenNotBigOrFireResetsMario() {
        Enemy enemy = mock(Enemy.class);
        mario.setStateBig(false);
        mario.setStateFire(false);

        mario.handleCollision(enemy, 'U');

        assertEquals(2, mario.getLives());
        assertFalse(mario.isStateBig());
        assertFalse(mario.isStateFire());
        assertFalse(mario.isStateInvencible());
        assertFalse(mario.isHitCooldown());
        assertEquals(0, mario.getInvencibleTime());
    }

    @Test
    void handleCollisionWithEnemyWhenMarioInvincibleAndHitCooldown() {
        Enemy enemy = mock(Enemy.class);
        mario.setStateInvencible(true);
        mario.setHitCooldown(true);

        mario.handleCollision(enemy, 'U');

        verify(mario, never()).setX(anyFloat());
        verify(mario, never()).setY(anyFloat());
        verify(mario, never()).setVx(anyFloat());
        verify(mario, never()).setVy(anyFloat());
    }

    @Test
    void handleCollisionWithEnemyWhenMarioNotInvincibleAndHitCooldown() {
        Enemy enemy = mock(Enemy.class);
        mario.setStateInvencible(false);
        mario.setStateBig(false);
        mario.setStateFire(true);

        mario.handleCollision(enemy, 'U');

        verify(mario, never()).reset();
    }

    @Test
    void handleCollisionWithGoalBlockNotifiesGoal() throws IOException {
        GoalBlock goalBlock = mock(GoalBlock.class);

        mario.handleCollision(goalBlock, 'U');

        verify(observer).nextLevel();
    }

    @Test
    void handleCollisionWithInteractableBlockHitsBlockAndSetsMarioState() {
        InteractableBlock interactableBlock = mock(InteractableBlock.class);

        mario.handleCollision(interactableBlock, 'U');

        verify(interactableBlock).gotHit(mario);
        assertEquals(0, mario.getVy());
        assertFalse(mario.isJumping());
        assertTrue(mario.isFalling());
        assertEquals(interactableBlock.getY() + mario.getHeight(), mario.getY());
    }

    @Test
    void handleCollisionWithBuildingBlockSetsMarioState() {
        BuildingBlock block = mock(BuildingBlock.class);
        float blockY = 5.0f;
        float blockHeight = 2.0f;
        float blockX = 3.0f;
        float blockWidth = 1.0f;
        when(block.getY()).thenReturn(blockY);
        when(block.getHeight()).thenReturn(blockHeight);
        when(block.getX()).thenReturn(blockX);
        when(block.getWidth()).thenReturn(blockWidth);

        mario.handleCollision(block, 'D');
        assertEquals(0, mario.getVy());
        assertFalse(mario.isFalling());
        assertFalse(mario.isJumping());
        assertEquals(MARIO_X, mario.getX());
        assertEquals(blockY - blockHeight, mario.getY());

        mario.handleCollision(block, 'L');
        assertEquals(blockX + blockWidth, mario.getX());
        assertEquals(0, mario.getVx());

        mario.handleCollision(block, 'R');
        assertEquals(blockX - MARIO_WIDTH, mario.getX());
        assertEquals(0, mario.getVx());
    }

    @Test
    void gameObjectNotSurpriseNotEnemyNotBuildingBlock() {
        GameObject object = new GameObject() {
            @Override
            public float getX() {
                return 0;
            }

            @Override
            public float getY() {
                return 0;
            }

            @Override
            public float getVirtX(Camera camera) {
                return 0;
            }

            @Override
            public float getVirtY() {
                return 0;
            }

            @Override
            public String getImage() {
                return "";
            }
        };
        mario.handleCollision(object, 'U');
        verify(mario, never()).setX(anyFloat());
        verify(mario, never()).setY(anyFloat());
        verify(mario, never()).setVx(anyFloat());
        verify(mario, never()).setVy(anyFloat());
    }

    @Test
    void buildingBlockValidButNoValidDirection() {
        BuildingBlock block = mock(BuildingBlock.class);
        mario.handleCollision(block, 'X');
        verify(mario, never()).setX(anyFloat());
        verify(mario, never()).setY(anyFloat());
        verify(mario, never()).setVx(anyFloat());
        verify(mario, never()).setVy(anyFloat());
    }

    @Test
    void jumpToBuildingBlockButNotInteractable(){
        BuildingBlock block = mock(BuildingBlock.class);
        mario.handleCollision(block, 'U');
        verify(mario).setVy(0f);
        verify(mario).setJumping(false);
        verify(mario).setFalling(true);
        verify(mario).setY(MARIO_HEIGHT);
    }

    @Test
    void resetNormal(){
        mario.reset();
        assertEquals(2, mario.getLives());
        assertFalse(mario.isStateBig());
        assertFalse(mario.isStateFire());
        assertFalse(mario.isStateInvencible());
        assertFalse(mario.isHitCooldown());
        assertEquals(0, mario.getInvencibleTime());
    }

    @Test
    void resetIsStateBigTrue(){
        mario.setStateBig(true);
        mario.reset();
        assertEquals(2, mario.getLives());
        assertFalse(mario.isStateBig());
        assertFalse(mario.isStateFire());
        assertFalse(mario.isStateInvencible());
        assertFalse(mario.isHitCooldown());
        assertEquals(0, mario.getInvencibleTime());
        assertEquals(1, mario.getHeight());
    }

    @Test
    void resetIsStateFireTrue(){
        mario.setStateFire(true);
        mario.reset();
        assertEquals(2, mario.getLives());
        assertFalse(mario.isStateBig());
        assertFalse(mario.isStateFire());
        assertFalse(mario.isStateInvencible());
        assertFalse(mario.isHitCooldown());
        assertEquals(0, mario.getInvencibleTime());
        assertEquals(1, mario.getHeight());
    }


    @Test
    void virtXCalculatesCorrectly() {
        Camera camera = mock(Camera.class);
        when(camera.getLeftCamLimit()).thenReturn(0.5f);

        assertEquals(0.5f, mario.getVirtX(camera));
    }

    @Test
    void virtXCalculatesCorrectlyWithNegativeCameraLimit() {
        Camera camera = mock(Camera.class);
        when(camera.getLeftCamLimit()).thenReturn(-0.5f);

        assertEquals(1.5f, mario.getVirtX(camera));
    }

    @Test
    void virtYCalculatesCorrectly() {
        mario.setY(5.0f);
        mario.setHeight(2.0f);

        assertEquals(4.0f, mario.getVirtY());
    }

    @Test
    void virtYCalculatesCorrectlyWithHeightOne() {
        mario.setY(5.0f);
        mario.setHeight(1.0f);

        assertEquals(5.0f, mario.getVirtY());
    }


    @Test
    void getImageReturnsMarioStarBigWhenBigAndInvencibleAndNotInCooldownAndRandomTrue() {
        mario.setStateBig(true);
        mario.setStateInvencible(true);
        mario.setHitCooldown(false);

        findRandomString("marioStarBig.png");
    }

    @Test
    void getImageReturnsMarioStarBigWhenBigAndInvencibleAndInCooldownAndRandomTrue() {
        mario.setStateBig(true);
        mario.setStateInvencible(true);
        mario.setHitCooldown(true);

        assertEquals("marioSuper.png", mario.getImage());
    }

    @Test
    void getImageReturnsMarioSuperWhenBigAndNotInvencible() {
        mario.setStateBig(true);
        mario.setStateInvencible(false);

        assertEquals("marioSuper.png", mario.getImage());
    }

    @Test
    void getImageReturnsMarioFlowerWhenFireAndNotInvencible() {
        mario.setStateFire(true);
        mario.setStateInvencible(false);

        assertEquals("marioFlower.png", mario.getImage());
    }

    @Test
    void getImageReturnsMarioStarBigWhenFireAndInvencibleAndNotInCooldownAndRandomTrue() {
        mario.setStateFire(true);
        mario.setStateInvencible(true);
        mario.setHitCooldown(false);

        findRandomString("marioStarBig.png");
    }

    @Test
    void getImageReturnsMarioStarBigWhenFireAndInvencibleAndInCooldownAndRandomTrue() {
        mario.setStateFire(true);
        mario.setStateInvencible(true);
        mario.setHitCooldown(true);

        assertEquals("marioFlower.png", mario.getImage());
    }

    @Test
    void getImageReturnsHitMarioWhenNotBigOrFireAndInvencibleAndInCooldownAndRandomTrue() {
        mario.setStateInvencible(true);
        mario.setHitCooldown(true);

        findRandomString("hitMario.png");
    }

    @Test
    void getImageReturnsHitMarioWhenNotBigOrFireAndNotInvencibleAndInCooldownAndRandomTrue() {
        mario.setStateInvencible(true);
        mario.setHitCooldown(false);

        findRandomString("marioStar.png");
    }

    @Test
    void getImageReturnsMarioStarWhenNotBigOrFireAndInvencibleAndNotInCooldownAndRandomTrue() {
        mario.setStateInvencible(true);
        mario.setHitCooldown(false);

        findRandomString("marioStar.png");
    }

    @Test
    void getImageReturnsMarioWhenNotBigOrFireAndNotInvencible() {
        mario.setStateBig(false);
        mario.setStateFire(false);
        mario.setStateInvencible(false);

        assertEquals("mario.png", mario.getImage());
    }

    void findRandomString(String expected){
        boolean notFound = false, found = false;
        String image;

        while(!(found && notFound)){
            image = mario.getImage();
            if(image.equals(expected)){
                found = true;
                assertEquals(expected, image);
            } else {
                notFound = true;
                assertNotEquals(expected, image);
            }
        }
    }

    @Test
    void nullReturn(){
        mario.setStateFire(false);
        mario.setStateBig(false);
        mario.setHitCooldown(false);
        when(mario.isStateInvencible()).thenReturn(true).thenReturn(false);

        String image = mario.getImage();
        while(image != null){
            assertNotEquals(null, image);
            image = mario.getImage();
        }
        assertNull(image);
    }
}
