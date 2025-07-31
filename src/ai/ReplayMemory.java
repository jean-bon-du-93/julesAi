package ai;

import org.nd4j.linalg.api.ndarray.INDArray;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Stores and samples experiences for the DQN.
 */
public class ReplayMemory {

    private final int capacity;
    private final List<Experience> memory;
    private final Random random = new Random();

    public record Experience(INDArray state, int action, double reward, INDArray nextState, boolean done) {}

    /**
     * Constructs a new ReplayMemory.
     * @param capacity the capacity of the memory
     */
    public ReplayMemory(int capacity) {
        this.capacity = capacity;
        this.memory = new ArrayList<>(capacity);
    }

    /**
     * Adds an experience to the memory.
     * @param experience the experience to add
     */
    public void add(Experience experience) {
        if (memory.size() >= capacity) {
            memory.remove(0);
        }
        memory.add(experience);
    }

    /**
     * Samples a batch of experiences from the memory.
     * @param batchSize the size of the batch
     * @return a list of experiences
     */
    public List<Experience> sample(int batchSize) {
        int size = Math.min(batchSize, memory.size());
        List<Experience> batch = new ArrayList<>(size);
        Collections.shuffle(memory, random);
        for (int i = 0; i < size; i++) {
            batch.add(memory.get(i));
        }
        return batch;
    }

    public int size() {
        return memory.size();
    }
}
