package gui;

import javax.swing.*;

/**
 * The main window of the game.
 */
public class GameFrame extends JFrame {
    /**
     * Constructs a new GameFrame.
     */
    public GameFrame() {
        setTitle("Snake AI");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Switches the current panel.
     * @param panel the new panel
     */
    public void switchPanel(JPanel panel) {
        getContentPane().removeAll();
        add(panel);
        pack();
        revalidate();
        repaint();
        panel.requestFocus();
    }
}
