package game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GameTest {

    private Game game;

    @BeforeEach
    public void setUp() {
        game = new Game();
    }

    @Test
    public void testInitialState() {
        assertFalse(game.isGameOver());
        assertEquals(0, game.getScore());
        assertNotNull(game.getSnake());
        assertNotNull(game.getFood());
    }

    @Test
    public void testWallCollision() {
        game.getSnake().setDirection(Direction.LEFT);
        for (int i = 0; i < 15; i++) {
            game.update();
        }
        assertTrue(game.isGameOver());
    }

    @Test
    public void testEatFood() {
        Point foodPosition = new Point(game.getSnake().getHead().x + 1, game.getSnake().getHead().y);
        game.getFood().setPosition(foodPosition); // Force food position for testing
        game.update();
        assertEquals(1, game.getScore());
        assertEquals(2, game.getSnake().getBody().size());
        assertNotEquals(foodPosition, game.getFood().getPosition());
    }

    @Test
    public void testRestart() {
        game.update();
        game.restart();
        assertFalse(game.isGameOver());
        assertEquals(0, game.getScore());
    }
}
