package org.grupogjl.model.game.elements.level;

import org.grupogjl.model.game.elements.Mario;
import org.grupogjl.model.game.elements.blocks.DestroyableBlock;
import org.grupogjl.model.game.elements.blocks.GoalBlock;
import org.grupogjl.model.game.elements.blocks.SurpriseBlock;
import org.grupogjl.model.game.elements.camera.Camera;
import org.grupogjl.model.game.elements.enemies.Enemy;
import org.grupogjl.model.game.elements.enemies.Goomba;
import org.grupogjl.model.game.elements.enemies.KoopaTroopa;
import org.grupogjl.model.game.elements.generalobjects.GameObject;
import org.grupogjl.model.game.elements.props.FireBall;
import org.grupogjl.model.game.elements.surprises.Coin;
import org.grupogjl.model.game.elements.surprises.Surprise;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class TestLevel {

    private Level level;

    @BeforeEach
    void setUp() {
        level = new Level(150, 100);
    }

    @Test
    void testConstructor() {
        assertEquals(150, level.getWidth());
        assertEquals(100, level.getHeight());
        assertTrue(level.getObjects().isEmpty());
    }

    @Test
    void testSetAndGetHeight() {
        level.setHeight(200);
        assertEquals(200, level.getHeight());
    }

    @Test
    void testSetAndGetWidth() {
        level.setWidth(200);
        assertEquals(200, level.getWidth());
    }

    @Test
    void testSetAndGetMario() {
        Mario marioMock = mock(Mario.class);
        level.setMario(marioMock);
        assertEquals(marioMock, level.getMario());
    }

    @Test
    void testSetAndGetGoalBlock() {
        GoalBlock mockGoalBlock = mock(GoalBlock.class);
        level.setGoalBlock(mockGoalBlock);
        assertEquals(mockGoalBlock, level.getGoalBlock());
    }

    @Test
    void testSetAndGetCamera() {
        Camera cameraMock = mock(Camera.class);
        level.setCamera(cameraMock);
        assertEquals(cameraMock, level.getCamera());
    }

    @Test
    void testSetAndGetObjects(){
        List<GameObject> objectMocks = new ArrayList<>();
        GameObject objectMock = mock(GameObject.class);
        objectMocks.add(objectMock);
        level.setObjects(objectMocks);
        assertEquals(objectMocks, level.getObjects());
    }

    @Test
    void testGetDestroyableBlocks(){
        DestroyableBlock destroyableBlockMock = mock(DestroyableBlock.class);
        GameObject objectMock = mock(GameObject.class);
        level.setObjects(List.of(destroyableBlockMock, objectMock));

        List<DestroyableBlock> destroyableBlocks = level.getDestroyableBlocks();

        assertEquals(1, destroyableBlocks.size());
        assertTrue(destroyableBlocks.contains(destroyableBlockMock));
    }

    @Test
    void testSetDestroyableBlocks(){
        DestroyableBlock dbMock1 = mock(DestroyableBlock.class);
        DestroyableBlock dbMock2 = mock(DestroyableBlock.class);
        GoalBlock dbMock3 = mock(GoalBlock.class);
        List<GameObject> objectMocks = new ArrayList<>();
        objectMocks.add(dbMock1);
        objectMocks.add(dbMock2);
        objectMocks.add(dbMock3);
        level.setObjects(objectMocks);

        level.setDestroyableBlocks(List.of(dbMock1, dbMock2));

        assertEquals(2, level.getDestroyableBlocks().size());
        assertTrue(level.getDestroyableBlocks().containsAll(List.of(dbMock1, dbMock2)));
    }

    @Test
    void testGetSurpriseBlocks(){
        SurpriseBlock surpriseBlockMock = mock(SurpriseBlock.class);
        GameObject objectMock = mock(GameObject.class);
        level.setObjects(List.of(surpriseBlockMock, objectMock));

        List<SurpriseBlock> surpriseBlocks = level.getSurpriseBlocks();

        assertEquals(1, surpriseBlocks.size());
        assertTrue(surpriseBlocks.contains(surpriseBlockMock));
    }

    @Test
    void testGetEnemies(){
        Goomba goombaMock = mock(Goomba.class);
        KoopaTroopa koopaTroopaMock = mock(KoopaTroopa.class);
        GameObject objectMock = mock(GameObject.class);
        level.setObjects(List.of(goombaMock, koopaTroopaMock, objectMock));

        List<Enemy> enemies = level.getEnemies();

        assertEquals(2, enemies.size());
        assertTrue(enemies.contains(goombaMock));
        assertTrue(enemies.contains(koopaTroopaMock));
    }

    @Test
    void testSetEnemies(){
        Goomba goombaMock = mock(Goomba.class);
        KoopaTroopa koopaTroopaMock = mock(KoopaTroopa.class);
        GoalBlock goalBlockMock = mock(GoalBlock.class);
        List<GameObject> objectMocks = new ArrayList<>();
        objectMocks.add(goombaMock);
        objectMocks.add(koopaTroopaMock);
        objectMocks.add(goalBlockMock);
        level.setObjects(objectMocks);

        level.setEnemies(List.of(goombaMock, koopaTroopaMock));

        assertEquals(2, level.getEnemies().size());
        assertTrue(level.getEnemies().containsAll(List.of(goombaMock, koopaTroopaMock)));
    }

    @Test
    void testGetSurprises(){
        SurpriseBlock surpriseBlockMock = mock(SurpriseBlock.class);
        GameObject objectMock = mock(GameObject.class);
        level.setObjects(List.of(surpriseBlockMock, objectMock));

        List<SurpriseBlock> surpriseBlocks = level.getSurpriseBlocks();

        assertEquals(1, surpriseBlocks.size());
        assertTrue(surpriseBlocks.contains(surpriseBlockMock));
    }

    @Test
    void testSetSurprises(){
        Surprise surpriseBlockMock = mock(Surprise.class);
        GoalBlock goalBlockMock = mock(GoalBlock.class);
        List<GameObject> objectMocks = new ArrayList<>();
        objectMocks.add(surpriseBlockMock);
        objectMocks.add(goalBlockMock);
        level.setObjects(objectMocks);

        level.setSurprises(List.of(surpriseBlockMock));

        assertEquals(1, level.getSurprises().size());
        assertTrue(level.getSurprises().contains(surpriseBlockMock));
    }

    @Test
    void testGetCoins() {
        Coin coinMock = mock(Coin.class);
        GameObject objectMock = mock(GameObject.class);
        level.setObjects(List.of(coinMock, objectMock));

        List<Coin> coins = level.getCoins();

        assertEquals(1, coins.size());
        assertTrue(coins.contains(coinMock));
    }

    @Test
    void testGetFireBalls() {
        FireBall fireBallMock = mock(FireBall.class);
        GameObject objectMock = mock(GameObject.class);
        level.setObjects(List.of(fireBallMock, objectMock));

        List<FireBall> fireBalls = level.getFireBalls();

        assertEquals(1, fireBalls.size());
        assertTrue(fireBalls.contains(fireBallMock));
    }

    @Test
    void testSetFireBalls() {
        FireBall fireBallMock1 = mock(FireBall.class);
        FireBall fireBallMock2 = mock(FireBall.class);
        GameObject objectMock = mock(GameObject.class);

        level.setObjects(new ArrayList<>(List.of(fireBallMock1, objectMock)));

        List<FireBall> newFireBalls = List.of(fireBallMock2);
        level.setFireBalls(newFireBalls);

        List<FireBall> fireBalls = level.getFireBalls();
        assertEquals(1, fireBalls.size());
        assertTrue(fireBalls.contains(fireBallMock2));
        assertFalse(fireBalls.contains(fireBallMock1));
    }
}
