package ru.Maslov.neuralNetwork.generator;

import org.springframework.stereotype.Component;
import ru.Maslov.neuralNetwork.model.FigureImage;
import ru.Maslov.neuralNetwork.model.FigureType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class SquareGenerator implements FigureGenerator {
    private static final Random RANDOM = new Random();

    @Override
    public List<FigureImage> generate(Integer countFigure) {
        double center = (SIZE_IMAGE - 1) / 2.0;
        int destOutBound = (int) Math.ceil(center - (int) Math.ceil((SIZE_IMAGE - center) / 2));

        List<FigureImage> figures = new ArrayList<>();

        for (int i = 0; i < countFigure; i++) {
            List<List<Boolean>> image = new ArrayList<>();

            for (int row = 0; row < SIZE_IMAGE; row++) {
                List<Boolean> line = new ArrayList<>();
                for (int col = 0; col < SIZE_IMAGE; col++) {
                    boolean isEdge =
                            (row == destOutBound && destOutBound <= col && col <= SIZE_IMAGE - destOutBound - 1) ||
                            (col == destOutBound && destOutBound <= row && row <= SIZE_IMAGE - destOutBound - 1) ||
                            (row == SIZE_IMAGE - destOutBound - 1 && destOutBound <= col && col <= SIZE_IMAGE - destOutBound - 1) ||
                            (col == SIZE_IMAGE - destOutBound - 1 && destOutBound <= row && row <= SIZE_IMAGE - destOutBound - 1);
                    boolean hasNoise = RANDOM.nextDouble() < 0.05;

                    if (isEdge && !hasNoise) {
                        line.add(true);
                    } else if (hasNoise) {
                        if (!isEdge)
                            line.add(true);
                        else
                            line.add(false);
                    } else {
                        line.add(false);
                    }
                }
                image.add(line);
            }

            figures.add(new FigureImage(image, FigureType.SQUARE));
        }

        return figures;
    }
}
