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
    private double explorationRate;
    private final double minExplorationRate;
    private final double explorationDecayRate;
    private final Random random = new Random();

    /**
     * Constructs a new Agent.
     * @param qTable the Q-table
     * @param learningRate the learning rate
     * @param discountFactor the discount factor
     * @param explorationRate the initial exploration rate
     * @param minExplorationRate the minimum exploration rate
     * @param explorationDecayRate the exploration decay rate
     */
    public Agent(QTable qTable, double learningRate, double discountFactor, double explorationRate, double minExplorationRate, double explorationDecayRate) {
        this.qTable = qTable;
        this.learningRate = learningRate;
        this.discountFactor = discountFactor;
        this.explorationRate = explorationRate;
        this.minExplorationRate = minExplorationRate;
        this.explorationDecayRate = explorationDecayRate;
    }

    /**
     * Gets the current state of the game.
     * @param game the game instance
     * @return the state as a string
     */
    public String getState(Game game) {
        Point head = game.getSnake().getHead();
        Point food = game.getFood().getPosition();

        // Directions: N, NE, E, SE, S, SW, W, NW
        Point[] directions = {
            new Point(0, -1), new Point(1, -1), new Point(1, 0), new Point(1, 1),
            new Point(0, 1), new Point(-1, 1), new Point(-1, 0), new Point(-1, -1)
        };

        StringBuilder stateBuilder = new StringBuilder();

        for (Point dir : directions) {
            double distanceToWall = 0;
            double distanceToBody = 0;
            boolean foodInDirection = false;

            Point current = new Point(head.x + dir.x, head.y + dir.y);
            double distance = 1.0;

            while (current.x >= 0 && current.x < Game.GRID_WIDTH && current.y >= 0 && current.y < Game.GRID_HEIGHT) {
                if (game.getSnake().getBody().contains(current) && distanceToBody == 0) {
                    distanceToBody = 1.0 / distance;
                }
                if (current.equals(food)) {
                    foodInDirection = true;
                }
                current = new Point(current.x + dir.x, current.y + dir.y);
                distance++;
            }
            distanceToWall = 1.0 / distance;

            stateBuilder.append(String.format("%.2f,%.2f,%d,", distanceToWall, distanceToBody, foodInDirection ? 1 : 0));
        }

        // Food direction relative to head
        int foodDx = Integer.compare(food.x, head.x);
        int foodDy = Integer.compare(food.y, head.y);
        stateBuilder.append(foodDx).append(",").append(foodDy);

        return stateBuilder.toString();
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
     * Decays the exploration rate.
     */
    public void decayExplorationRate() {
        explorationRate = Math.max(minExplorationRate, explorationRate * explorationDecayRate);
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
