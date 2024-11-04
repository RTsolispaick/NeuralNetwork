package ru.Maslov.neuralNetwork.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.Maslov.neuralNetwork.model.NeuralNetwork;
import ru.Maslov.neuralNetwork.generator.FigureDataGenerator;

import static ru.Maslov.neuralNetwork.Constants.EPOCHS;

@Configuration
public class Config {

    /**
     * Создает и обучает объект нейронной сети.
     *
     * @param figureDataGenerator генератор данных для обучения нейронной сети.
     * @return обученная нейронная сеть.
     */
    @Bean
    @Autowired
    public NeuralNetwork trainNeuralNetwork(FigureDataGenerator figureDataGenerator) {
        NeuralNetwork neuralNetwork = new NeuralNetwork();

        // Обучение нейронной сети с использованием тренировочного набора данных и заданного количества эпох.
        neuralNetwork.train(figureDataGenerator.getTrainDataSet(), EPOCHS);

        return neuralNetwork;
    }
}
