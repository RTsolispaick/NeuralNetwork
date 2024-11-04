package ru.Maslov.neuralNetwork.generator;

import ru.Maslov.neuralNetwork.model.FigureImage;

import java.util.List;

import static ru.Maslov.neuralNetwork.Constants.SIZE;

/**
 * Интерфейс FigureGenerator представляет собой генератор фигур для обучения и тестирования нейронной сети.
 * Обеспечивает методы для создания набора изображений фигур.
 */
public interface FigureGenerator {
    /**
     * Размер изображения (количество пикселей по одной стороне).
     * Константа используется для определения размерности изображений.
     */
    int SIZE_IMAGE = SIZE;

    /**
     * Генерирует список изображений фигур заданного количества.
     * Каждое изображение представляется объектом типа FigureImage, содержащим изображение и тип фигуры.
     *
     * @param countFigure количество генерируемых изображений фигур.
     * @return список объектов FigureImage, представляющих сгенерированные изображения фигур.
     */
    List<FigureImage> generate(Integer countFigure);
}
