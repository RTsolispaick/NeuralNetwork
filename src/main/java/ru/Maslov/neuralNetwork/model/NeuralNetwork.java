package ru.Maslov.neuralNetwork.model;

import java.util.List;
import java.util.Random;

/**
 * Класс NeuralNetwork представляет собой простую нейронную сеть с одним скрытым слоем.
 * Предназначен для распознавания и классификации входных данных (например, изображений фигур).
 */
public class NeuralNetwork {
    private static final int INPUT_SIZE = 49; // Размерность входного слоя
    private static final int HIDDEN_LAYER_SIZE = 20; // Размерность скрытого слоя
    private static final int OUTPUT_SIZE = 3; // Размерность выходного слоя

    private static final double MIN_ALPHA = 0.01; // Минимальное значение коэффициента обучения
    private static final double MAX_ALPHA = 0.3; // Максимальное значение коэффициента обучения
    private static final double MAX_ERR = 0.1; // Допустимая ошибка

    private final double[][] weightsInputHidden; // Весовые коэффициенты между входным и скрытым слоями
    private final double[][] weightsHiddenOutput; // Весовые коэффициенты между скрытым и выходным слоями

    private final Random random = new Random();

    /**
     * Конструктор создает экземпляр нейронной сети с инициализированными случайными весовыми коэффициентами.
     */
    public NeuralNetwork() {
        weightsInputHidden = randomMatrix(INPUT_SIZE, HIDDEN_LAYER_SIZE);
        weightsHiddenOutput = randomMatrix(HIDDEN_LAYER_SIZE, OUTPUT_SIZE);
    }

    /**
     * Обучает нейронную сеть на предоставленных данных.
     * @param trainingData Список обучающих данных типа FigureImage.
     * @param epochs Количество эпох для обучения.
     */
    public void train(List<FigureImage> trainingData, int epochs) {
        for (int epoch = 0; epoch < epochs; epoch++) {
            trainingData.forEach(figure -> {
                double[] input = flattenImage(figure.image());
                double[] target = oneHotEncoding(figure.figureType().ordinal(), OUTPUT_SIZE);

                double[] hiddenLayerOutputs = sigmoidVector(dot(input, weightsInputHidden));
                double[] outputLayerOutputs = sigmoidVector(dot(hiddenLayerOutputs, weightsHiddenOutput));

                double[] outputError = subtract(outputLayerOutputs, target);
                double[] hiddenLayerError = multiply(dotTrans(outputError, weightsHiddenOutput), sigmoidDerivative(hiddenLayerOutputs));

                double error = error(target, outputError);

                if (error > MAX_ERR) {
                    double learningRate = recalLearningRate(error);
                    updateWeights(input, hiddenLayerOutputs, hiddenLayerError, outputLayerOutputs, outputError, learningRate);
                }
            });
        }
    }

    /**
     * Предсказывает класс фигуры на основе предоставленного изображения.
     * @param figure Изображение типа FigureImage.
     * @return Тип фигуры типа FigureType.
     */
    public FigureType predict(FigureImage figure) {
        double[] input = flattenImage(figure.image());

        double[] hiddenLayerOutputs = sigmoidVector(dot(input, weightsInputHidden));
        double[] outputLayerOutputs = sigmoidVector(dot(hiddenLayerOutputs, weightsHiddenOutput));

        int predictedIndex = argMax(outputLayerOutputs);
        return FigureType.values()[predictedIndex];
    }

    /**
     * Пересчитывает коэффициент обучения на основе текущей ошибки.
     * @param error Текущая ошибка.
     * @return Новое значение коэффициента обучения.
     */
    private double recalLearningRate(double error) {
        double rel_e = 2 * error / OUTPUT_SIZE;
        return rel_e * (MAX_ALPHA - MIN_ALPHA) + MIN_ALPHA;
    }

    /**
     * Вычисляет среднюю ошибку между целевыми значениями и выходной ошибкой.
     * @param target Целевые значения.
     * @param outputError Ошибка выходного слоя.
     * @return Средняя ошибка.
     */
    private double error(double[] target, double[] outputError) {
        double e = 0;
        for (int i = 0; i < OUTPUT_SIZE; i++) {
            e += Math.abs(target[i] - outputError[i]);
        }
        return e / 2;
    }

    /**
     * Обновляет веса сети на основе ошибки и коэффициента обучения.
     * @param input Входные данные.
     * @param hiddenOutputs Выходные значения скрытого слоя.
     * @param hiddenError Ошибка скрытого слоя.
     * @param outputOutputs Выходные значения выходного слоя.
     * @param outputError Ошибка выходного слоя.
     * @param learningRate Коэффициент обучения.
     */
    private void updateWeights(double[] input, double[] hiddenOutputs, double[] hiddenError, double[] outputOutputs, double[] outputError, double learningRate) {
        double[] primeSigmoidOutputOutput = sigmoidDerivative(outputOutputs);
        for (int i = 0; i < OUTPUT_SIZE; i++) {
            for (int j = 0; j < HIDDEN_LAYER_SIZE; j++) {
                weightsHiddenOutput[j][i] -= learningRate * outputError[i] * primeSigmoidOutputOutput[i] * hiddenOutputs[j];
            }
        }

        double[] primeSigmoidHiddenOutput = sigmoidDerivative(hiddenOutputs);
        for (int i = 0; i < HIDDEN_LAYER_SIZE; i++) {
            for (int j = 0; j < INPUT_SIZE; j++) {
                weightsInputHidden[j][i] -= learningRate * hiddenError[i] * primeSigmoidHiddenOutput[i] * input[j];
            }
        }
    }

    /**
     * Преобразует двумерный список изображений в одномерный массив.
     * @param image Изображение в виде двумерного списка логических значений.
     * @return Одномерный массив входных данных.
     */
    private double[] flattenImage(List<List<Boolean>> image) {
        double[] flatImage = new double[INPUT_SIZE];
        int index = 0;
        for (List<Boolean> row : image) {
            for (Boolean cell : row) {
                flatImage[index++] = cell ? 1.0 : 0.0;
            }
        }
        return flatImage;
    }

    /**
     * Вычисляет сигмоидное значение.
     * @param x Входное значение.
     * @return Результат функции сигмоиды.
     */
    private double sigmoid(double x) {
        return 1.0 / (1.0 + Math.exp(-x));
    }

    /**
     * Вычисляет производную сигмоиды.
     * @param x Входное значение.
     * @return Производная сигмоиды.
     */
    private double sigmoidPrime(double x) {
        return sigmoid(x) * (1 - sigmoid(x));
    }

    /**
     * Применяет сигмоидную функцию ко всем элементам массива.
     * @param x Входной массив.
     * @return Массив с примененной функцией сигмоиды.
     */
    private double[] sigmoidVector(double[] x) {
        double[] result = new double[x.length];
        for (int i = 0; i < x.length; i++) {
            result[i] = sigmoid(x[i]);
        }
        return result;
    }

    /**
     * Применяет производную сигмоиды ко всем элементам массива.
     * @param x Входной массив.
     * @return Массив с примененной производной сигмоиды.
     */
    private double[] sigmoidDerivative(double[] x) {
        double[] result = new double[x.length];
        for (int i = 0; i < x.length; i++) {
            result[i] = sigmoidPrime(x[i]);
        }
        return result;
    }

    /**
     * Вычисляет произведение вектора и матрицы.
     * @param vector Входной вектор.
     * @param matrix Входная матрица.
     * @return Результирующий вектор.
     */
    private double[] dot(double[] vector, double[][] matrix) {
        double[] result = new double[matrix[0].length];
        for (int i = 0; i < matrix[0].length; i++) {
            for (int j = 0; j < vector.length; j++) {
                result[i] += vector[j] * matrix[j][i];
            }
        }
        return result;
    }

    /**
     * Вычисляет произведение вектора и транспонированной матрицы.
     * @param vector Входной вектор.
     * @param matrix Входная матрица.
     * @return Результирующий вектор.
     */
    private double[] dotTrans(double[] vector, double[][] matrix) {
        double[] result = new double[matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < vector.length; j++) {
                result[i] += vector[j] * matrix[i][j];
            }
        }
        return result;
    }

    /**
     * Перемножает два массива поэлементно.
     * @param a Первый массив.
     * @param b Второй массив.
     * @return Результирующий массив.
     */
    private double[] multiply(double[] a, double[] b) {
        double[] result = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            result[i] = a[i] * b[i];
        }
        return result;
    }

    /**
     * Вычитает один массив из другого поэлементно.
     * @param a Первый массив.
     * @param b Второй массив.
     * @return Результирующий массив.
     */
    private double[] subtract(double[] a, double[] b) {
        double[] result = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            result[i] = a[i] - b[i];
        }
        return result;
    }

    /**
     * Генерирует случайную матрицу с весовыми коэффициентами.
     * @param rows Количество строк.
     * @param cols Количество столбцов.
     * @return Случайная матрица.
     */
    private double[][] randomMatrix(int rows, int cols) {
        double[][] matrix = new double[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix[i][j] = random.nextDouble() - 0.5; // Диапазон значений от -0.5 до 0.5
            }
        }
        return matrix;
    }

    /**
     * Преобразует целочисленный индекс в вектор с одноразовой кодировкой.
     * @param index Индекс целевого значения.
     * @param size Размерность вектора.
     * @return Вектор с одноразовой кодировкой.
     */
    private double[] oneHotEncoding(int index, int size) {
        double[] vector = new double[size];
        vector[index] = 1.0;
        return vector;
    }

    /**
     * Находит индекс максимального элемента в массиве.
     * @param array Входной массив.
     * @return Индекс максимального элемента.
     */
    private int argMax(double[] array) {
        int maxIndex = 0;
        for (int i = 1; i < array.length; i++) {
            if (array[i] > array[maxIndex]) {
                maxIndex = i;
            }
        }
        return maxIndex;
    }
}
