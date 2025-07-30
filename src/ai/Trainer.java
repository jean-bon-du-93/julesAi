package ai;

import game.Game;
import utils.Stats;

import java.io.File;
import java.io.IOException;

/**
 * Manages the training process of the AI agent.
 */
public class Trainer {
    private static final int NUM_EPISODES = 10000;
    private static final String Q_TABLE_FILE = "saves/q_table.ser";
    private final Stats stats = new Stats();

    /**
     * Starts the training process.
     * @return the statistics of the training session
     */
    public Stats startTraining() {
        new File("saves").mkdirs();
        stats.clear();

        QTable qTable;
        try {
            qTable = QTable.load(Q_TABLE_FILE);
            System.out.println("Loaded existing Q-table.");
        } catch (IOException | ClassNotFoundException e) {
            qTable = new QTable();
            System.out.println("Created new Q-table.");
        }

        Agent agent = new Agent(qTable, 0.1, 0.9, 0.1);
        Game game = new Game();

        for (int episode = 0; episode < NUM_EPISODES; episode++) {
            game.initGame();
            String state = agent.getState(game);
            int steps = 0;

            while (!game.isGameOver() && steps < 1000) { // Limit steps to prevent infinite loops
                int action = agent.chooseAction(state);
                game.setDirection(Agent.getDirectionFromAction(action));
                game.update();

                double reward = calculateReward(game);
                String nextState = agent.getState(game);
                agent.learn(state, action, reward, nextState);
                state = nextState;
                steps++;
            }

            stats.addScore(game.getScore());

            if ((episode + 1) % 100 == 0) {
                System.out.printf("Episode: %d, Average Score: %.2f%n", episode + 1, stats.getAverageScore());
            }

            if ((episode + 1) % (NUM_EPISODES / 10) == 0) {
                try {
                    qTable.save(Q_TABLE_FILE);
                    System.out.println("Saved Q-table at episode " + (episode + 1));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("Training finished.");
        return stats;
    }

    private double calculateReward(Game game) {
        if (game.isGameOver()) {
            return -100.0; // Penalty for dying
        }
        if (game.getSnake().getHead().equals(game.getFood().getPosition())) {
            return 10.0; // Reward for eating food
        }
        return -0.1; // Small penalty for each step to encourage speed
    }
}
