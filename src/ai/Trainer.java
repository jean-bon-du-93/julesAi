package ai;

import game.Game;
import utils.Stats;

import java.io.File;
import java.io.IOException;

/**
 * Manages the training process of the AI agent.
 */
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import java.util.List;
import java.util.ArrayList;


public class Trainer {
    private static final int NUM_EPISODES = 20000;
    private static final int REPLAY_MEMORY_CAPACITY = 10000;
    private static final int BATCH_SIZE = 128;
    private static final int TARGET_NETWORK_UPDATE_FREQUENCY = 100; // in episodes
    private static final String DQN_FILE = "saves/dqn_latest.zip";
    private static final String BEST_DQN_FILE = "saves/dqn_best.zip";
    private final Stats stats = new Stats();
    private final List<Double> lossHistory = new ArrayList<>();
    private int bestScore = 0;
    private MultiLayerNetwork targetDqn;

    /**
     * Starts the training process.
     * @return the statistics of the training session
     */
    public Stats startTraining() {
        new File("saves").mkdirs();
        stats.clear();

        MultiLayerNetwork dqn;
        try {
            File bestModel = new File(BEST_DQN_FILE);
            if (bestModel.exists()) {
                dqn = MultiLayerNetwork.load(bestModel, true);
                System.out.println("Loaded best DQN model.");
            } else {
                dqn = MultiLayerNetwork.load(new File(DQN_FILE), true);
                System.out.println("Loaded latest DQN model.");
            }
        } catch (IOException e) {
            dqn = DQN.createDQN();
            System.out.println("Created new DQN model.");
        }
        targetDqn = dqn.clone();
        dqn.setListeners(new ScoreIterationListener(1000));

        ReplayMemory replayMemory = new ReplayMemory(REPLAY_MEMORY_CAPACITY, 0.6);
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

            if (game.getScore() > bestScore) {
                bestScore = game.getScore();
                try {
                    dqn.save(new File(BEST_DQN_FILE), true);
                    System.out.println("Saved new best model with score: " + bestScore);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if ((episode + 1) % 1000 == 0) { // Save latest model every 1000 episodes
                try {
                    dqn.save(new File(DQN_FILE), true);
                    System.out.println("Saved latest DQN model at episode " + (episode + 1));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if ((episode + 1) % TARGET_NETWORK_UPDATE_FREQUENCY == 0) {
                targetDqn.setParams(dqn.params());
                System.out.println("Updated target network at episode " + (episode + 1));
            }
        }
        System.out.println("Training finished.");
        return stats;
    }

    private void train(MultiLayerNetwork dqn, ReplayMemory replayMemory, double discountFactor) {
        List<ReplayMemory.PrioritizedExperience> batch = replayMemory.sample(BATCH_SIZE);

        List<ReplayMemory.Experience> experiences = batch.stream().map(ReplayMemory.PrioritizedExperience::experience).toList();
        INDArray states = Nd4j.vstack(experiences.stream().map(ReplayMemory.Experience::state).toList());
        INDArray nextStates = Nd4j.vstack(experiences.stream().map(ReplayMemory.Experience::nextState).toList());

        INDArray qValues = dqn.output(states);
        INDArray nextQValuesFromMainDQN = dqn.output(nextStates);
        INDArray nextQValuesFromTargetDQN = targetDqn.output(nextStates);

        double[] errors = new double[batch.size()];
        for (int i = 0; i < batch.size(); i++) {
            ReplayMemory.Experience exp = experiences.get(i);
            double oldQValue = qValues.getDouble(i, exp.action());
            double targetQ;
            if (exp.done()) {
                targetQ = exp.reward();
            } else {
                int bestAction = Nd4j.argMax(nextQValuesFromMainDQN.getRow(i)).getInt(0);
                targetQ = exp.reward() + discountFactor * nextQValuesFromTargetDQN.getDouble(i, bestAction);
            }
            errors[i] = targetQ - oldQValue;
            qValues.putScalar(i, exp.action(), targetQ);
        }

        for (int i = 0; i < batch.size(); i++) {
            replayMemory.update(batch.get(i).index(), errors[i]);
        }

        // Importance-sampling weights
        double[] weights = new double[batch.size()];
        double maxWeight = 0.0;
        for (int i = 0; i < batch.size(); i++) {
            weights[i] = Math.pow((replayMemory.size() * batch.get(i).priority()) / replayMemory.totalPriority(), -ReplayMemory.beta);
            if (weights[i] > maxWeight) {
                maxWeight = weights[i];
            }
        }
        for (int i = 0; i < batch.size(); i++) {
            weights[i] /= maxWeight;
            errors[i] *= weights[i]; // Apply weights to the errors
        }

        // Create a new INDArray for the weighted target Q-values
        INDArray weightedQValues = qValues.dup();
        for (int i = 0; i < batch.size(); i++) {
            weightedQValues.putScalar(i, experiences.get(i).action(), qValues.getDouble(i, experiences.get(i).action()) + errors[i]);
        }

        dqn.fit(states, weightedQValues);
        lossHistory.add(dqn.score());
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

    public List<Double> getLossHistory() {
        return lossHistory;
    }
}
