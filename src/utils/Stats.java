package utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages game statistics.
 */
public class Stats {
    private final List<Integer> scores = new ArrayList<>();

    /**
     * Adds a score to the statistics.
     * @param score the score to add
     */
    public void addScore(int score) {
        scores.add(score);
    }

    /**
     * Calculates the average score over the last N games.
     * @param lastNGames the number of games to average over
     * @return the average score
     */
    public double getAverageScore(int lastNGames) {
        if (scores.isEmpty()) {
            return 0;
        }
        int start = Math.max(0, scores.size() - lastNGames);
        return scores.subList(start, scores.size()).stream().mapToInt(Integer::intValue).average().orElse(0);
    }

    /**
     * Gets all the scores.
     * @return a list of all scores
     */
    public List<Integer> getScores() {
        return new ArrayList<>(scores);
    }

    /**
     * Clears all statistics.
     */
    public void clear() {
        scores.clear();
    }
}
