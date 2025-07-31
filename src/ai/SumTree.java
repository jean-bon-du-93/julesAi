package ai;

import java.util.Arrays;

/**
 * A SumTree data structure for prioritized experience replay.
 */
public class SumTree {
    private final int capacity;
    private final double[] tree;
    private final ReplayMemory.Experience[] data;
    private int write;

    public SumTree(int capacity) {
        this.capacity = capacity;
        this.tree = new double[2 * capacity - 1];
        this.data = new ReplayMemory.Experience[capacity];
        this.write = 0;
    }

    private void propagate(int idx, double change) {
        int parent = (idx - 1) / 2;
        tree[parent] += change;
        if (parent != 0) {
            propagate(parent, change);
        }
    }

    public void update(int idx, double priority) {
        double change = priority - tree[idx];
        tree[idx] = priority;
        propagate(idx, change);
    }

    public void add(double priority, ReplayMemory.Experience sample) {
        int idx = write + capacity - 1;
        data[write] = sample;
        update(idx, priority);
        write = (write + 1) % capacity;
    }

    private int retrieve(int idx, double s) {
        int left = 2 * idx + 1;
        int right = left + 1;
        if (left >= tree.length) {
            return idx;
        }
        if (s <= tree[left]) {
            return retrieve(left, s);
        } else {
            return retrieve(right, s - tree[left]);
        }
    }

    public int get(double s) {
        return retrieve(0, s);
    }

    public double total() {
        return tree[0];
    }

    public ReplayMemory.Experience getData(int idx) {
        return data[idx];
    }

    public int getWrite() {
        return write;
    }
}
