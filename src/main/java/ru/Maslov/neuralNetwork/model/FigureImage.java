package ru.Maslov.neuralNetwork.model;

import java.util.List;

/**
 * Класс FigureImage представляет изображение фигуры, состоящее из двух компонентов:
 * - матрицы пикселей, представленная в виде списка списков логических значений, где
 *   true обозначает заполненный пиксель, а false — пустой пиксель.
 * - тип фигуры, представленным объектом {@link FigureType}.
 *
 * @param image      матрица пикселей фигуры
 * @param figureType тип фигуры
 */
public record FigureImage(List<List<Boolean>> image, FigureType figureType) {}
