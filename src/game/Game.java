package game;

/**
 * The main game engine.
 */
public class Game {
    public static final int GRID_WIDTH = 25;
    public static final int GRID_HEIGHT = 25;

    private Snake snake;
    private Food food;
    private int score;
    private boolean gameOver;

    /**
     * Constructs a new Game object.
     */
    public Game() {
        initGame();
    }

    /**
     * Initializes the game state.
     */
    public void initGame() {
        snake = new Snake(new Point(GRID_WIDTH / 2, GRID_HEIGHT / 2));
        food = new Food(GRID_WIDTH, GRID_HEIGHT);
        score = 0;
        gameOver = false;
    }

    /**
     * Updates the game state.
     */
    public void update() {
        if (gameOver) {
            return;
        }

        snake.move();

        if (snake.checkSelfCollision() || checkWallCollision()) {
            gameOver = true;
            return;
        }

        if (snake.getHead().equals(food.getPosition())) {
            snake.grow();
            food.respawn(snake);
            score++;
        }
    }

    /**
     * Restarts the game.
     */
    public void restart() {
        initGame();
    }

    private boolean checkWallCollision() {
        Point head = snake.getHead();
        return head.x < 0 || head.x >= GRID_WIDTH || head.y < 0 || head.y >= GRID_HEIGHT;
    }

    public Snake getSnake() {
        return snake;
    }

    public Food getFood() {
        return food;
    }

    public int getScore() {
        return score;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setDirection(Direction direction) {
        snake.setDirection(direction);
    }
}
