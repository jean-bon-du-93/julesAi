package gui;

import game.Game;
import game.Point;

import javax.swing.*;
import java.awt.*;

/**
 * The panel that draws the game.
 */
public class GamePanel extends JPanel {
    private static final int CELL_SIZE = 20;
    private Game game;

    /**
     * Constructs a new GamePanel.
     * @param game the game instance
     */
import utils.Config;

    public GamePanel(Game game) {
        this.game = game;
        setPreferredSize(new Dimension(Config.GRID_WIDTH * CELL_SIZE, Config.GRID_HEIGHT * CELL_SIZE));
        setBackground(Color.BLACK);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawGrid(g);
        drawSnake(g);
        drawFood(g);
        drawScore(g);
        if (game.isGameOver()) {
            drawGameOver(g);
        }
    }

    private void drawScore(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Score: " + game.getScore(), 10, 20);
    }

    private void drawGameOver(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 40));
        FontMetrics fm = g.getFontMetrics();
        String gameOverText = "Game Over";
        int x = (getWidth() - fm.stringWidth(gameOverText)) / 2;
        int y = getHeight() / 2;
        g.drawString(gameOverText, x, y);

        g.setFont(new Font("Arial", Font.PLAIN, 20));
        fm = g.getFontMetrics();
        String restartText = "Press any key to restart";
        x = (getWidth() - fm.stringWidth(restartText)) / 2;
        y += fm.getHeight();
        g.drawString(restartText, x, y);
    }

    private void drawGrid(Graphics g) {
        g.setColor(Color.DARK_GRAY);
        for (int i = 0; i <= Config.GRID_WIDTH; i++) {
            g.drawLine(i * CELL_SIZE, 0, i * CELL_SIZE, Config.GRID_HEIGHT * CELL_SIZE);
        }
        for (int i = 0; i <= Config.GRID_HEIGHT; i++) {
            g.drawLine(0, i * CELL_SIZE, Config.GRID_WIDTH * CELL_SIZE, i * CELL_SIZE);
        }
    }

    private void drawSnake(Graphics g) {
        if (utils.ResourceManager.head != null && utils.ResourceManager.body != null) {
            java.util.List<Point> body = game.getSnake().getBody();
            for (int i = 0; i < body.size(); i++) {
                Point p = body.get(i);
                if (i == 0) {
                    g.drawImage(utils.ResourceManager.head, p.x * CELL_SIZE, p.y * CELL_SIZE, CELL_SIZE, CELL_SIZE, null);
                } else {
                    g.drawImage(utils.ResourceManager.body, p.x * CELL_SIZE, p.y * CELL_SIZE, CELL_SIZE, CELL_SIZE, null);
                }
            }
        } else { // Fallback to geometric shapes
            g.setColor(Color.GREEN);
            for (Point p : game.getSnake().getBody()) {
                g.fillRect(p.x * CELL_SIZE, p.y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
            }
        }
    }

    private void drawFood(Graphics g) {
        if (utils.ResourceManager.food != null) {
            Point foodPosition = game.getFood().getPosition();
            g.drawImage(utils.ResourceManager.food, foodPosition.x * CELL_SIZE, foodPosition.y * CELL_SIZE, CELL_SIZE, CELL_SIZE, null);
        } else { // Fallback to geometric shapes
            g.setColor(Color.RED);
            Point foodPosition = game.getFood().getPosition();
            g.fillOval(foodPosition.x * CELL_SIZE, foodPosition.y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
        }
    }

    public void setGame(Game game) {
        this.game = game;
    }
}
