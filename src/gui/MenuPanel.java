package gui;

import javax.swing.*;
import java.awt.*;

/**
 * The panel for the main menu.
 */
public class MenuPanel extends JPanel {
    private final JButton humanModeButton;
    private final JButton aiTrainingModeButton;
    private final JButton aiAutonomousModeButton;
    private final JButton showChartButton;

    /**
     * Constructs a new MenuPanel.
     * @param controller the game controller
     */
    public MenuPanel(GameController controller) {
        setLayout(new GridLayout(3, 1, 10, 10));
        setBorder(BorderFactory.createEmptyBorder(100, 100, 100, 100));
        setBackground(Color.BLACK);

        setLayout(new GridLayout(4, 1, 10, 10));
        humanModeButton = new JButton("Human Mode");
        aiTrainingModeButton = new JButton("AI Training Mode");
        aiAutonomousModeButton = new JButton("AI Autonomous Mode");
        showChartButton = new JButton("Show Training Chart");


        configureButton(humanModeButton);
        configureButton(aiTrainingModeButton);
        configureButton(aiAutonomousModeButton);
        configureButton(showChartButton);

        humanModeButton.addActionListener(e -> controller.startHumanMode());
        aiTrainingModeButton.addActionListener(e -> controller.startAiTrainingMode());
        aiAutonomousModeButton.addActionListener(e -> controller.startAiAutonomousMode());
        showChartButton.addActionListener(e -> controller.showTrainingChart());

        add(humanModeButton);
        add(aiTrainingModeButton);
        add(aiAutonomousModeButton);
        add(showChartButton);
    }

    private void configureButton(JButton button) {
        button.setFont(new Font("Arial", Font.BOLD, 20));
        button.setFocusable(false);
    }
}
