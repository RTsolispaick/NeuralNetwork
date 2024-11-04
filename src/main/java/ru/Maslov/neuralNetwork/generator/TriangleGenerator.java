package ru.Maslov.neuralNetwork.generator;

import org.springframework.stereotype.Component;
import ru.Maslov.neuralNetwork.model.FigureImage;
import ru.Maslov.neuralNetwork.model.FigureType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class TriangleGenerator implements FigureGenerator {
    private static final Random RANDOM = new Random();

    @Override
    public List<FigureImage> generate(Integer countFigure) {
        List<FigureImage> images = new ArrayList<>();

        for (int i = 0; i < countFigure; i++) {
            int stepsBaseWidth = (SIZE_IMAGE + 1) / 2;
            double center = (SIZE_IMAGE - 1) / 2.0;
            int destOutBound = (int) Math.ceil(center - (int) Math.ceil((SIZE_IMAGE - center) / 2));

            List<List<Boolean>> triangle = new ArrayList<>();

            for (int row = 0; row < SIZE_IMAGE; row++) {
                List<Boolean> line = new ArrayList<>();
                for (int col = 0; col < SIZE_IMAGE; col++) {
                    boolean hasNoise = RANDOM.nextDouble() < 0.05;
                    if (row >= destOutBound && row <= SIZE_IMAGE - destOutBound - 1) {
                        int triangleBaseWidth = ((row - destOutBound) * stepsBaseWidth) / SIZE_IMAGE * 2 + 1;
                        int triangleStartCol = (SIZE_IMAGE - triangleBaseWidth) / 2;
                        boolean isEdge = col == triangleStartCol || col == triangleStartCol + triangleBaseWidth - 1
                                || (row == SIZE_IMAGE - destOutBound - 1 && col >= destOutBound && col < SIZE_IMAGE - destOutBound - 1);

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
                    } else {
                        if (hasNoise)
                            line.add(true);
                        else
                            line.add(false);
                    }
                }
                triangle.add(line);
            }

            images.add(new FigureImage(triangle, FigureType.TRIANGLE));
        }

        return images;
    }
}
