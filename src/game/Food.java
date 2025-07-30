package game;

import java.util.Random;

/**
 * Represents the food in the game.
 */
public class Food {
    private Point position;
    private final Random random = new Random();
    private final int gridWidth;
    private final int gridHeight;

    /**
     * Constructs a new Food object.
     * @param gridWidth the width of the game grid
     * @param gridHeight the height of the game grid
     */
    public Food(int gridWidth, int gridHeight) {
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;
        this.position = generatePosition();
    }

    /**
     * Generates a new random position for the food.
     * @return the new position
     */
    private Point generatePosition() {
        int x = random.nextInt(gridWidth);
        int y = random.nextInt(gridHeight);
        return new Point(x, y);
    }

    /**
     * Respawns the food at a new random location, avoiding the snake's body.
     * @param snake the snake to avoid
     */
    public void respawn(Snake snake) {
        do {
            this.position = generatePosition();
        } while (snake.getBody().contains(position));
    }

    public Point getPosition() {
        return position;
    }
}
