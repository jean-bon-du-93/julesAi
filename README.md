# Snake AI Project

This project is a complete Snake game in Java with an AI component that learns to play using Reinforcement Learning (Q-learning).

## Features

- **Human Player Mode**: Play the classic Snake game using keyboard controls (W, A, S, D).
- **AI Training Mode**: Train the AI agent in the background. The AI's progress is saved periodically and can be resumed later.
- **AI Autonomous Mode**: Watch the trained AI play the game on its own.
- **Graphical User Interface**: A simple and clear GUI built with Swing.
- **Statistics and Charting**: View the AI's training progress with a score chart.

## Project Structure

- `src/`: Contains the source code.
  - `ai/`: AI-related classes (Agent, Q-learning, Trainer).
  - `game/`: Core game logic (Game, Snake, Food).
  - `gui/`: GUI components (GameFrame, GamePanel, Menu).
  - `utils/`: Utility classes (Stats, ChartGenerator).
- `saves/`: Directory for saved AI models.
- `test/`: Unit tests for the project.

## Building and Running the Project

This project uses Apache Ant for building.

### Dependencies

The required libraries (JFreeChart, JUnit) should be placed in a `lib` directory at the root of the project.

1.  **JFreeChart**: Download from the [official website](https://www.jfree.org/jfreechart/). You will need `jfreechart-1.5.3.jar` and `jcommon-1.0.23.jar`.
2.  **JUnit 5**: Download `junit-platform-console-standalone-1.8.2.jar` from the [JUnit website](https://junit.org/junit5/).

### Building with Ant

Once you have Ant installed and the dependencies in the `lib` folder, you can use the following commands from the root of the project:

- **`ant`** or **`ant dist`**: Compiles the project and creates an executable JAR file in the `dist` directory.
- **`ant clean`**: Deletes the `build` and `dist` directories.

### Execution

There are two ways to run the application:

1.  **With GUI**:

    ```bash
    java -cp "lib/*:out" Main
    ```

2.  **Headless Training**: To run the training process without the GUI for maximum speed:

    ```bash
    java -cp "lib/*:out" ai.HeadlessTrainer
    ```

### Running Tests

1.  Compile the test files:

    ```bash
    javac -d out/test -cp "lib/*:out:src" $(find test -name "*.java")
    ```

2.  Run the tests from the command line:

    ```bash
    java -jar lib/junit-platform-console-standalone-x.x.x.jar --class-path "out:out/test" --scan-class-path
    ```

## How to Use

1.  **Start the application**: Run the `Main` class.
2.  **Main Menu**:
    - **Human Mode**: Play the game yourself.
    - **AI Training Mode**: Start the AI training process. This will run in the background and may take a long time. The console will show the progress.
    - **AI Autonomous Mode**: Watch the AI play. This requires a saved model, so you must run the training mode first.
    - **Show Training Chart**: After a training session, click this button to see a chart of the AI's score progression.

## Future Optimizations

- **State Representation**: The current state representation for the AI is simple. A more complex representation (e.g., using a wider view of the snake's surroundings) could lead to better performance.
- **Hyperparameter Tuning**: The learning rate, discount factor, and exploration rate in the `Agent` class can be tuned to improve the learning process.
- **Performance**: For a very large number of training episodes, the simulation could be run without the GUI thread sleeping, which would speed up training significantly.
- **Deployment**: The project could be packaged into an executable JAR for easier distribution.
- **GPU Acceleration**: For more complex models (like a neural network instead of a Q-table), using a library like Deeplearning4j could allow for GPU acceleration.
