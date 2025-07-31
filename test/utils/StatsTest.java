package utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class StatsTest {

    private Stats stats;

    @BeforeEach
    public void setUp() {
        stats = new Stats();
    }

    @Test
    public void testAddScore() {
        stats.addScore(10);
        assertEquals(1, stats.getScores().size());
        assertEquals(10, stats.getScores().get(0));
    }

    @Test
    public void testGetAverageScore() {
        stats.addScore(10);
        stats.addScore(20);
        assertEquals(15.0, stats.getAverageScore(2));
    }

    @Test
    public void testGetAverageScoreWithMoreScores() {
        stats.addScore(10);
        stats.addScore(20);
        stats.addScore(30);
        assertEquals(25.0, stats.getAverageScore(2));
    }

    @Test
    public void testClear() {
        stats.addScore(10);
        stats.clear();
        assertTrue(stats.getScores().isEmpty());
    }
}
