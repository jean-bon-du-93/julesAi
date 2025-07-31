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
    private final JButton optionsButton;

    /**
     * Constructs a new MenuPanel.
     * @param controller the game controller
     */
    public MenuPanel(GameController controller) {
        setLayout(new GridLayout(5, 1, 10, 10));
        setBorder(BorderFactory.createEmptyBorder(100, 100, 100, 100));
        setBackground(Color.BLACK);

        humanModeButton = new JButton("Human Mode");
        aiTrainingModeButton = new JButton("AI Training Mode");
        aiAutonomousModeButton = new JButton("AI Autonomous Mode");
        showChartButton = new JButton("Show Training Chart");
        optionsButton = new JButton("Options");


        configureButton(humanModeButton);
        configureButton(aiTrainingModeButton);
        configureButton(aiAutonomousModeButton);
        configureButton(showChartButton);
        configureButton(optionsButton);

        humanModeButton.addActionListener(e -> controller.startHumanMode());
        aiTrainingModeButton.addActionListener(e -> controller.startAiTrainingMode());
        aiAutonomousModeButton.addActionListener(e -> controller.startAiAutonomousMode());
        showChartButton.addActionListener(e -> controller.showTrainingChart());
        optionsButton.addActionListener(e -> controller.showOptions());

        add(humanModeButton);
        add(aiTrainingModeButton);
        add(aiAutonomousModeButton);
        add(showChartButton);
        add(optionsButton);
    }

    private void configureButton(JButton button) {
        button.setFont(new Font("Arial", Font.BOLD, 20));
        button.setFocusable(false);
    }
}
