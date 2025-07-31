package ai;

import game.Direction;
import game.Game;
import game.Point;

import java.util.Random;

/**
 * The AI agent that learns to play Snake.
 */
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

public class Agent {
    private final MultiLayerNetwork dqn;
    private final ReplayMemory replayMemory;
    private final double discountFactor;
    private double explorationRate;
    private final double minExplorationRate;
    private final double explorationDecayRate;
    private final Random random = new Random();

    /**
     * Constructs a new Agent.
     * @param dqn the DQN
     * @param replayMemory the replay memory
     * @param discountFactor the discount factor
     * @param explorationRate the initial exploration rate
     * @param minExplorationRate the minimum exploration rate
     * @param explorationDecayRate the exploration decay rate
     */
    public Agent(MultiLayerNetwork dqn, ReplayMemory replayMemory, double discountFactor, double explorationRate, double minExplorationRate, double explorationDecayRate) {
        this.dqn = dqn;
        this.replayMemory = replayMemory;
        this.discountFactor = discountFactor;
        this.explorationRate = explorationRate;
        this.minExplorationRate = minExplorationRate;
        this.explorationDecayRate = explorationDecayRate;
    }

    /**
     * Gets the current state of the game as an INDArray.
     * @param game the game instance
     * @return the state as an INDArray
     */
    public INDArray getState(Game game) {
        Point head = game.getSnake().getHead();
        Point food = game.getFood().getPosition();

        // Directions: N, NE, E, SE, S, SW, W, NW
        Point[] directions = {
            new Point(0, -1), new Point(1, -1), new Point(1, 0), new Point(1, 1),
            new Point(0, 1), new Point(-1, 1), new Point(-1, 0), new Point(-1, -1)
        };

        float[] state = new float[26];
        int i = 0;

        for (Point dir : directions) {
            float distanceToWall = 0;
            float distanceToBody = 0;
            float foodInDirection = 0;

            Point current = new Point(head.x + dir.x, head.y + dir.y);
            float distance = 1.0f;

            while (current.x >= 0 && current.x < Game.GRID_WIDTH && current.y >= 0 && current.y < Game.GRID_HEIGHT) {
                if (game.getSnake().getBody().contains(current) && distanceToBody == 0) {
                    distanceToBody = 1.0f / distance;
                }
                if (current.equals(food)) {
                    foodInDirection = 1.0f;
                }
                current = new Point(current.x + dir.x, current.y + dir.y);
                distance++;
            }
            distanceToWall = 1.0f / distance;

            state[i++] = distanceToWall;
            state[i++] = distanceToBody;
            state[i++] = foodInDirection;
        }

        // Food direction relative to head
        state[i++] = Integer.compare(food.x, head.x);
        state[i] = Integer.compare(food.y, head.y);

        return Nd4j.create(state);
    }

    /**
     * Chooses an action based on the current state.
     * @param state the current state
     * @return the chosen action
     */
    public int chooseAction(INDArray state) {
        if (random.nextDouble() < explorationRate) {
            return random.nextInt(4); // Explore
        } else {
            INDArray output = dqn.output(state);
            return Nd4j.argMax(output, 1).getInt(0); // Exploit
        }
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
