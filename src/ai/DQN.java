package ai;

import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.learning.config.Adam;
import org.nd4j.linalg.lossfunctions.LossFunctions;

/**
 * Defines the Deep Q-Network (DQN).
 */
public class DQN {
    private static final int INPUT_NEURONS = 26; // 8 directions * 3 features + 2 food direction
    private static final int OUTPUT_NEURONS = 4; // 4 actions
    private static final double LEARNING_RATE = 0.001;

    /**
     * Creates a new DQN.
     * @return the DQN
     */
    public static MultiLayerNetwork createDQN() {
        MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
                .seed(123)
                .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
                .updater(new Adam(LEARNING_RATE))
                .weightInit(WeightInit.XAVIER)
                .list()
                .layer(0, new DenseLayer.Builder().nIn(INPUT_NEURONS).nOut(128).activation(Activation.RELU).build())
                .layer(1, new DenseLayer.Builder().nIn(128).nOut(64).activation(Activation.RELU).build())
                .layer(2, new OutputLayer.Builder(LossFunctions.LossFunction.MSE).nIn(64).nOut(OUTPUT_NEURONS).activation(Activation.IDENTITY).build())
                .build();

        MultiLayerNetwork model = new MultiLayerNetwork(conf);
        model.init();
        return model;
    }
}
