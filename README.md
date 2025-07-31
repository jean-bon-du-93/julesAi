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

## Compilation and Execution

### Dependencies

This project requires the following libraries:

- **JFreeChart**: For generating the statistics chart.
- **JUnit 5**: For running the unit tests.

You will need to download the JAR files for these libraries and include them in your classpath.

1.  **JFreeChart**: Download from the [official website](https://www.jfree.org/jfreechart/) or a Maven repository. You will need `jfreechart-x.x.x.jar` and `jcommon-x.x.x.jar`.
2.  **JUnit 5**: Download `junit-platform-console-standalone-x.x.x.jar` from the [JUnit website](https://junit.org/junit5/).

### Compilation

1.  Place the downloaded JAR files in a `lib` directory at the root of the project.

2.  Compile the source code from the root directory of the project:

    ```bash
    javac -d out -cp "lib/*:src" $(find src -name "*.java")
    ```

### Execution

1.  Run the main application:

    ```bash
    java -cp "lib/*:out" Main
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
