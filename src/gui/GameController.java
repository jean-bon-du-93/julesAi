package gui;

import game.Direction;
import game.Game;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Handles the game loop and user input.
 */
public class GameController {
    private Game game;
    private GamePanel gamePanel;
    private final GameFrame gameFrame;
    private final MenuPanel menuPanel;
    private Thread gameThread;
    private utils.Stats lastTrainingStats;

    /**
     * Constructs a new GameController.
     */
    public GameController() {
        gameFrame = new GameFrame();
        menuPanel = new MenuPanel(this);
        gameFrame.switchPanel(menuPanel);
    }

    public void startHumanMode() {
        game = new Game();
        gamePanel = new GamePanel(game);
        gameFrame.switchPanel(gamePanel);
        gamePanel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleKeyPress(e.getKeyCode());
            }
        });
        gamePanel.setFocusable(true);
        gamePanel.requestFocusInWindow();
        startGameLoop();
    }

    public void startAiTrainingMode() {
        new Thread(() -> {
            System.out.println("AI Training Mode started. This may take a while...");
            ai.Trainer trainer = new ai.Trainer();
            lastTrainingStats = trainer.startTraining();
            System.out.println("AI Training finished.");
            SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(gameFrame, "AI Training Finished.", "Info", JOptionPane.INFORMATION_MESSAGE));
        }).start();
    }

    public void showTrainingChart() {
        if (lastTrainingStats == null || lastTrainingStats.getScores().isEmpty()) {
            JOptionPane.showMessageDialog(gameFrame, "No training data available. Please run the AI training first.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        JFrame chartFrame = new JFrame("Training Stats");
        chartFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        chartFrame.add(utils.ChartGenerator.createScoreChart(lastTrainingStats.getScores()));
        chartFrame.pack();
        chartFrame.setLocationRelativeTo(gameFrame);
        chartFrame.setVisible(true);
    }

    public void startAiAutonomousMode() {
        game = new Game();
        gamePanel = new GamePanel(game);
        gameFrame.switchPanel(gamePanel);

        new Thread(() -> {
            try {
                ai.QTable qTable = ai.QTable.load("saves/q_table.ser");
                ai.Agent agent = new ai.Agent(qTable, 0, 1, 0); // No learning, no exploration

                while (!Thread.currentThread().isInterrupted()) {
                    if (game.isGameOver()) {
                        game.restart();
                    }

                    String state = agent.getState(game);
                    int action = agent.chooseAction(state);
                    game.setDirection(ai.Agent.getDirectionFromAction(action));
                    game.update();
                    gamePanel.repaint();

                    try {
                        Thread.sleep(50); // Speed for visualization
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(gameFrame, "Could not load AI model. Please train the AI first.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }).start();
    }

    private void handleKeyPress(int keyCode) {
        if (game.isGameOver()) {
            game.restart();
            return;
        }

        switch (keyCode) {
            case KeyEvent.VK_W -> game.setDirection(Direction.UP);
            case KeyEvent.VK_S -> game.setDirection(Direction.DOWN);
            case KeyEvent.VK_A -> game.setDirection(Direction.LEFT);
            case KeyEvent.VK_D -> game.setDirection(Direction.RIGHT);
        }
    }

    private void startGameLoop() {
        if (gameThread != null && gameThread.isAlive()) {
            gameThread.interrupt();
        }

        gameThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                if (!game.isGameOver()) {
                    game.update();
                    gamePanel.repaint();
                }
                try {
                    Thread.sleep(100); // Adjust for game speed
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        gameThread.start();
    }
}
