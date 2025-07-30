import gui.GameController;

import javax.swing.*;

/**
 * The main class to start the application.
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(GameController::new);
    }
}
