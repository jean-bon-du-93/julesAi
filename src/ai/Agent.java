package ai;

import game.Direction;
import game.Game;
import game.Point;

import java.util.Random;

/**
 * The AI agent that learns to play Snake.
 */
public class Agent {
    private final QTable qTable;
    private final double learningRate;
    private final double discountFactor;
    private final double explorationRate;
    private final Random random = new Random();

    /**
     * Constructs a new Agent.
     * @param qTable the Q-table
     * @param learningRate the learning rate
     * @param discountFactor the discount factor
     * @param explorationRate the exploration rate
     */
    public Agent(QTable qTable, double learningRate, double discountFactor, double explorationRate) {
        this.qTable = qTable;
        this.learningRate = learningRate;
        this.discountFactor = discountFactor;
        this.explorationRate = explorationRate;
    }

    /**
     * Gets the current state of the game.
     * @param game the game instance
     * @return the state as a string
     */
    public String getState(Game game) {
        Point head = game.getSnake().getHead();
        Point food = game.getFood().getPosition();

        // Food direction
        int foodDx = Integer.compare(food.x, head.x);
        int foodDy = Integer.compare(food.y, head.y);

        // Obstacle detection
        boolean dangerUp = isObstacle(game, new Point(head.x, head.y - 1));
        boolean dangerDown = isObstacle(game, new Point(head.x, head.y + 1));
        boolean dangerLeft = isObstacle(game, new Point(head.x - 1, head.y));
        boolean dangerRight = isObstacle(game, new Point(head.x + 1, head.y));

        return foodDx + "," + foodDy + "," + dangerUp + "," + dangerDown + "," + dangerLeft + "," + dangerRight;
    }

    private boolean isObstacle(Game game, Point p) {
        return p.x < 0 || p.x >= Game.GRID_WIDTH || p.y < 0 || p.y >= Game.GRID_HEIGHT || game.getSnake().getBody().contains(p);
    }

    /**
     * Chooses an action based on the current state.
     * @param state the current state
     * @return the chosen action
     */
    public int chooseAction(String state) {
        if (random.nextDouble() < explorationRate) {
            return random.nextInt(4); // Explore
        } else {
            return qTable.getBestAction(state); // Exploit
        }
    }

    /**
     * Updates the Q-table based on the reward.
     * @param state the state
     * @param action the action
     * @param reward the reward
     * @param nextState the next state
     */
    public void learn(String state, int action, double reward, String nextState) {
        double oldQValue = qTable.get(state)[action];
        double nextMaxQ = qTable.get(nextState)[qTable.getBestAction(nextState)];
        double newQValue = oldQValue + learningRate * (reward + discountFactor * nextMaxQ - oldQValue);
        qTable.update(state, action, newQValue);
    }

    /**
     * Converts an action index to a Direction.
     * @param action the action index
     * @return the corresponding Direction
     */
    public static Direction getDirectionFromAction(int action) {
        return switch (action) {
            case 0 -> Direction.UP;
            case 1 -> Direction.DOWN;
            case 2 -> Direction.LEFT;
            default -> Direction.RIGHT;
        };
    }
}
