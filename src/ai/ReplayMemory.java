package ai;

import org.nd4j.linalg.api.ndarray.INDArray;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Stores and samples experiences for Prioritized Experience Replay.
 */
public class ReplayMemory {

    private final SumTree tree;
    private final double alpha;
    private final Random random = new Random();
    public static final double epsilon = 0.01;
    public static final double beta = 0.4;

    public record Experience(INDArray state, int action, double reward, INDArray nextState, boolean done) {}
    public record PrioritizedExperience(int index, double priority, Experience experience) {}

    /**
     * Constructs a new ReplayMemory.
     * @param capacity the capacity of the memory
     * @param alpha the alpha parameter for PER
     */
    public ReplayMemory(int capacity, double alpha) {
        this.tree = new SumTree(capacity);
        this.alpha = alpha;
    }

    public void add(Experience experience) {
        double maxPriority = tree.total() > 0 ? Arrays.stream(tree.tree).max().getAsDouble() : 1.0;
        tree.add(maxPriority, experience);
    }

    public List<PrioritizedExperience> sample(int batchSize) {
        List<PrioritizedExperience> batch = new ArrayList<>(batchSize);
        double segment = tree.total() / batchSize;

        for (int i = 0; i < batchSize; i++) {
            double a = segment * i;
            double b = segment * (i + 1);
            double s = random.nextDouble() * (b - a) + a;
            int idx = tree.get(s);
            double priority = tree.tree[idx];
            Experience data = tree.getData(idx);
            batch.add(new PrioritizedExperience(idx, priority, data));
        }
        return batch;
    }

    public void update(int idx, double error) {
        double priority = Math.pow(Math.abs(error) + epsilon, alpha);
        tree.update(idx, priority);
    }

    public int size() {
        return tree.getWrite();
    }

    public double totalPriority() {
        return tree.total();
    }
}
