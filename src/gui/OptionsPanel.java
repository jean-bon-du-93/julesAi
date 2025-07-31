package gui;

import utils.Config;

import javax.swing.*;
import java.awt.*;

/**
 * The panel for game options.
 */
public class OptionsPanel extends JPanel {
    private final JSlider speedSlider;
    private final JSlider sizeSlider;

    public OptionsPanel(GameController controller) {
        setLayout(new GridLayout(3, 2, 10, 10));
        setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        setBackground(Color.BLACK);

        // Speed Slider
        add(new JLabel("Game Speed:", JLabel.CENTER)).setForeground(Color.WHITE);
        speedSlider = new JSlider(JSlider.HORIZONTAL, 50, 200, Config.GAME_SPEED);
        speedSlider.setMajorTickSpacing(50);
        speedSlider.setPaintTicks(true);
        speedSlider.setPaintLabels(true);
        add(speedSlider);

        // Size Slider
        add(new JLabel("Grid Size:", JLabel.CENTER)).setForeground(Color.WHITE);
        sizeSlider = new JSlider(JSlider.HORIZONTAL, 10, 40, Config.GRID_WIDTH);
        sizeSlider.setMajorTickSpacing(10);
        sizeSlider.setPaintTicks(true);
        sizeSlider.setPaintLabels(true);
        add(sizeSlider);

        // Buttons
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            Config.GAME_SPEED = speedSlider.getValue();
            Config.GRID_WIDTH = sizeSlider.getValue();
            Config.GRID_HEIGHT = sizeSlider.getValue();
            controller.showMenu();
        });
        add(saveButton);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> controller.showMenu());
        add(backButton);
    }
}
