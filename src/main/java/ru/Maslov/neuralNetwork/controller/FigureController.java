package ru.Maslov.neuralNetwork.controller;

import lombok.Getter;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.Maslov.neuralNetwork.model.FigureImage;
import ru.Maslov.neuralNetwork.model.FigureType;
import ru.Maslov.neuralNetwork.model.NeuralNetwork;
import ru.Maslov.neuralNetwork.generator.FigureDataGenerator;

import java.util.List;

/**
 * Контроллер FigureController предоставляет эндпоинты для обработки запросов, связанных с
 * изображениями фигур и работой с нейронной сетью для предсказания типа фигуры.
 */
@Controller
public class FigureController {
    private final NeuralNetwork neuralNetwork;
    private final FigureDataGenerator figureDataGenerator;

    /**
     * Конструктор контроллера FigureController.
     *
     * @param neuralNetwork объект нейронной сети для предсказаний.
     * @param figureDataGenerator генератор данных для фигур.
     */
    @Autowired
    public FigureController(NeuralNetwork neuralNetwork, FigureDataGenerator figureDataGenerator) {
        this.neuralNetwork = neuralNetwork;
        this.figureDataGenerator = figureDataGenerator;
    }

    /**
     * Обрабатывает GET-запрос по корневому пути "/" и возвращает представление "figure".
     *
     * @return имя представления "figure".
     */
    @GetMapping("/")
    public String figure() {
        return "figure";
    }

    /**
     * Обрабатывает GET-запрос по пути "/valid" для получения набора изображений фигур
     * и передачи данных в модель для отображения отчета.
     *
     * @param model объект Model для передачи данных в представление.
     * @return имя представления "report".
     */
    @GetMapping("/valid")
    public String getFigureImages(Model model) {
        List<FigureImage> valid = figureDataGenerator.getValidDataSet();
        model.addAttribute("report", new Report(valid));
        return "report";
    }

    /**
     * Обрабатывает POST-запрос по корневому пути "/" для предсказания типа фигуры
     * на основе переданного изображения фигуры.
     *
     * @param figureImage объект FigureImage с данными изображения фигуры.
     * @return объект ResponseEntity с результатом предсказания.
     */
    @PostMapping("/")
    public ResponseEntity<?> figurePost(@RequestBody FigureImage figureImage) {
        String result = neuralNetwork.predict(figureImage).toString();
        return ResponseEntity.ok().body(new PredictionResponse(result));
    }

    /**
     * Класс Report используется для создания отчета по набору изображений фигур,
     * включая общее количество изображений, количество правильных и неправильных предсказаний,
     * а также процент правильных предсказаний.
     */
    @Getter
    private class Report {
        private final Integer countAll;
        private final Double procentTrue;
        private Integer countTrue = 0;
        private Integer countFalse = 0;

        /**
         * Конструктор класса Report.
         *
         * @param figureImages список изображений фигур для генерации отчета.
         */
        public Report(List<FigureImage> figureImages) {
            countAll = figureImages.size();

            for (FigureImage figureImage : figureImages) {
                FigureType predict = neuralNetwork.predict(figureImage);

                if (predict.equals(figureImage.figureType()))
                    countTrue++;
                else {
                    countFalse++;
                }
            }

            procentTrue = countTrue / (double) countAll * 100;
        }
    }

    /**
     * Класс PredictionResponse представляет ответ на предсказание типа фигуры.
     */
    private record PredictionResponse(String result) {}
}
