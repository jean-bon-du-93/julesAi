package ai;

import game.Game;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AgentTest {

    private Agent agent;
    private Game game;

    @BeforeEach
    public void setUp() {
        QTable qTable = new QTable();
        agent = new Agent(qTable, 0.1, 0.9, 0.1);
        game = new Game();
    }

    @Test
    public void testGetState() {
        // This is a basic test. A more thorough test would require mocking the game state.
        String state = agent.getState(game);
        assertNotNull(state);
        assertTrue(state.contains(","));
    }

    @Test
    public void testChooseAction() {
        String state = agent.getState(game);
        int action = agent.chooseAction(state);
        assertTrue(action >= 0 && action < 4);
    }
}
