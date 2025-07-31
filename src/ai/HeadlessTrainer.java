package ai;

/**
 * A command-line tool for training the AI without a GUI.
 */
public class HeadlessTrainer {
    public static void main(String[] args) {
        System.out.println("Starting headless training...");
        Trainer trainer = new Trainer();
        trainer.startTraining();
        System.out.println("Headless training finished.");
    }
}
