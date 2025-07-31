package game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SnakeTest {

    private Snake snake;

    @BeforeEach
    public void setUp() {
        snake = new Snake(new Point(10, 10));
    }

    @Test
    public void testInitialState() {
        assertEquals(1, snake.getBody().size());
        assertEquals(new Point(10, 10), snake.getHead());
        assertEquals(Direction.RIGHT, snake.getDirection());
    }

    @Test
    public void testMove() {
        snake.move();
        assertEquals(new Point(11, 10), snake.getHead());
        assertEquals(1, snake.getBody().size());
    }

    @Test
    public void testGrow() {
        snake.grow();
        assertEquals(2, snake.getBody().size());
        snake.move();
        assertEquals(3, snake.getBody().size()); // Growth should be persistent
    }

    @Test
    public void testSetDirection() {
        snake.setDirection(Direction.UP);
        assertEquals(Direction.UP, snake.getDirection());
        snake.move();
        assertEquals(new Point(10, 9), snake.getHead());
    }

    @Test
    public void testPreventReverse() {
        snake.setDirection(Direction.LEFT);
        assertEquals(Direction.RIGHT, snake.getDirection()); // Should not change
    }

    @Test
    public void testSelfCollision() {
        snake.grow();
        snake.grow();
        snake.grow();
        snake.setDirection(Direction.UP);
        snake.move();
        snake.setDirection(Direction.LEFT);
        snake.move();
        snake.setDirection(Direction.DOWN);
        snake.move();
        assertTrue(snake.checkSelfCollision());
    }
}
