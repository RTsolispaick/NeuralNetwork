package ru.Maslov.neuralNetwork.generator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.Maslov.neuralNetwork.model.FigureImage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static ru.Maslov.neuralNetwork.Constants.COUNT_FIGURE_IN_TEST;
import static ru.Maslov.neuralNetwork.Constants.COUNT_FIGURE_IN_TRAIN;

/**
 * Класс FigureDataGenerator используется для генерации набора данных фигур
 * с использованием нескольких генераторов фигур. Он предоставляет методы для
 * создания наборов данных для обучения и тестирования нейронной сети.
 */
@Component
public class FigureDataGenerator {
    private final List<FigureGenerator> figureGenerators;

    /**
     * Конструктор класса FigureDataGenerator. Получает список генераторов фигур.
     *
     * @param figureGenerators список генераторов фигур для создания данных.
     */
    @Autowired
    public FigureDataGenerator(List<FigureGenerator> figureGenerators) {
        this.figureGenerators = figureGenerators;
    }

    /**
     * Создает и возвращает набор данных для тренировки.
     *
     * @return список объектов FigureImage, представляющих набор данных для тестирования.
     */
    public List<FigureImage> getTrainDataSet() {
        return generateDataSet(COUNT_FIGURE_IN_TRAIN);
    }

    /**
     * Создает и возвращает набор данных для валидации.
     *
     * @return список объектов FigureImage, представляющих набор данных для валидации.
     */
    public List<FigureImage> getValidDataSet() {
        return generateDataSet(COUNT_FIGURE_IN_TEST);
    }

    /**
     * Генерирует набор данных заданного размера с использованием всех доступных генераторов фигур.
     * Использует асинхронную обработку для параллельного выполнения генерации.
     *
     * @param countFigure количество фигур для генерации.
     * @return список объектов FigureImage, представляющих сгенерированные данные.
     */
    private List<FigureImage> generateDataSet(int countFigure) {
        List<CompletableFuture<List<FigureImage>>> futures = figureGenerators.stream()
                .map(generator -> CompletableFuture.supplyAsync(() -> generator.generate(countFigure)))
                .toList();

        List<FigureImage> allGeneratedFigures = new ArrayList<>(futures.stream()
                .map(CompletableFuture::join)
                .flatMap(List::stream)
                .toList());

        Collections.shuffle(allGeneratedFigures);

        return allGeneratedFigures;
    }
}
