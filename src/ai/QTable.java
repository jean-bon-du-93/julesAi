package ai;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Represents the Q-table for the Q-learning algorithm.
 */
public class QTable implements Serializable {
    private static final long serialVersionUID = 1L;
    private final Map<String, double[]> table = new HashMap<>();
    private final Random random = new Random();

    /**
     * Gets the Q-values for a given state.
     * @param state the state
     * @return the Q-values for the state
     */
    public double[] get(String state) {
        return table.computeIfAbsent(state, k -> new double[4]); // 4 actions: UP, DOWN, LEFT, RIGHT
    }

    /**
     * Updates the Q-value for a given state and action.
     * @param state the state
     * @param action the action
     * @param value the new Q-value
     */
    public void update(String state, int action, double value) {
        get(state)[action] = value;
    }

    /**
     * Gets the best action for a given state.
     * @param state the state
     * @return the best action
     */
    public int getBestAction(String state) {
        double[] qValues = get(state);
        int bestAction = 0;
        double maxQValue = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < qValues.length; i++) {
            if (qValues[i] > maxQValue) {
                maxQValue = qValues[i];
                bestAction = i;
            }
        }
        return bestAction;
    }

    /**
     * Saves the Q-table to a file.
     * @param filename the filename
     * @throws IOException if an I/O error occurs
     */
    public void save(String filename) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(this);
        }
    }

    /**
     * Loads a Q-table from a file.
     * @param filename the filename
     * @return the loaded Q-table
     * @throws IOException if an I/O error occurs
     * @throws ClassNotFoundException if the class of a serialized object cannot be found
     */
    public static QTable load(String filename) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            return (QTable) ois.readObject();
        }
    }
}
