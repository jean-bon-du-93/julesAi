package ai;

import game.Game;
import utils.Stats;

import java.io.File;
import java.io.IOException;

/**
 * Manages the training process of the AI agent.
 */
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import java.util.List;

public class Trainer {
    private static final int NUM_EPISODES = 20000;
    private static final int REPLAY_MEMORY_CAPACITY = 10000;
    private static final int BATCH_SIZE = 128;
    private static final String DQN_FILE = "saves/dqn.zip";
    private final Stats stats = new Stats();

    /**
     * Starts the training process.
     * @return the statistics of the training session
     */
    public Stats startTraining() {
        new File("saves").mkdirs();
        stats.clear();

        MultiLayerNetwork dqn;
        try {
            dqn = MultiLayerNetwork.load(new File(DQN_FILE), true);
            System.out.println("Loaded existing DQN model.");
        } catch (IOException e) {
            dqn = DQN.createDQN();
            System.out.println("Created new DQN model.");
        }

        ReplayMemory replayMemory = new ReplayMemory(REPLAY_MEMORY_CAPACITY);
        Agent agent = new Agent(dqn, replayMemory, 0.95, 1.0, 0.01, 0.995);
        Game game = new Game();

        for (int episode = 0; episode < NUM_EPISODES; episode++) {
            game.initGame();
            INDArray state = agent.getState(game);
            int steps = 0;

            while (!game.isGameOver() && steps < 1000) { // Limit steps to prevent infinite loops
                int action = agent.chooseAction(state);
                game.setDirection(Agent.getDirectionFromAction(action));
                game.update();

                INDArray nextState = agent.getState(game);
                double reward = calculateReward(game, state, nextState);
                replayMemory.add(new ReplayMemory.Experience(state, action, reward, nextState, game.isGameOver()));
                state = nextState;

                if (replayMemory.size() > BATCH_SIZE) {
                    train(dqn, replayMemory, agent.getDiscountFactor());
                }
                steps++;
            }

            agent.decayExplorationRate();
            stats.addScore(game.getScore());

            if ((episode + 1) % 100 == 0) {
                System.out.printf("Episode: %d, Score: %d, Avg Score: %.2f%n", episode + 1, game.getScore(), stats.getAverageScore(100));
            }

            if ((episode + 1) % (NUM_EPISODES / 10) == 0) {
                try {
                    dqn.save(new File(DQN_FILE), true);
                    System.out.println("Saved DQN model at episode " + (episode + 1));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("Training finished.");
        return stats;
    }

    private void train(MultiLayerNetwork dqn, ReplayMemory replayMemory, double discountFactor) {
        List<ReplayMemory.Experience> batch = replayMemory.sample(BATCH_SIZE);

        INDArray states = Nd4j.vstack(batch.stream().map(ReplayMemory.Experience::state).toList());
        INDArray nextStates = Nd4j.vstack(batch.stream().map(ReplayMemory.Experience::nextState).toList());

        INDArray qValues = dqn.output(states);
        INDArray nextQValues = dqn.output(nextStates);

        for (int i = 0; i < batch.size(); i++) {
            ReplayMemory.Experience exp = batch.get(i);
            double targetQ;
            if (exp.done()) {
                targetQ = exp.reward();
            } else {
                targetQ = exp.reward() + discountFactor * Nd4j.max(nextQValues.getRow(i)).getDouble(0);
            }
            qValues.putScalar(i, exp.action(), targetQ);
        }
        dqn.fit(states, qValues);
    }

    private double calculateReward(Game game, INDArray state, INDArray nextState) {
        if (game.isGameOver()) {
            return -10.0; // Penalty for dying
        }
        if (game.getScore() > 0) {
            return (double) game.getScore(); // Reward for eating food
        }
        return -0.1; // Small penalty for each step
    }
}
