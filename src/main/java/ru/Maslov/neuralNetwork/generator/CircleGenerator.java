package ru.Maslov.neuralNetwork.generator;

import org.springframework.stereotype.Component;
import ru.Maslov.neuralNetwork.model.FigureImage;
import ru.Maslov.neuralNetwork.model.FigureType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class CircleGenerator implements FigureGenerator {
    private static final Random RANDOM = new Random();

    @Override
    public List<FigureImage> generate(Integer countFigure) {
        double centerYCircle = (SIZE_IMAGE - 1) / 2.0;
        double centerXCircle = (SIZE_IMAGE - 1) / 2.0;
        int radiusCircle = (int) Math.ceil((SIZE_IMAGE - centerXCircle) / 2);

        List<FigureImage> images = new ArrayList<>();

        for (int i = 0; i < countFigure; i++) {
            List<List<Boolean>> circle = new ArrayList<>();
            for (int y = 0; y < SIZE_IMAGE; y++) {
                List<Boolean> line = new ArrayList<>();
                for (int x = 0; x < SIZE_IMAGE; x++) {
                    double distance = Math.sqrt(Math.pow(x - centerXCircle, 2) + Math.pow(y - centerYCircle, 2));
                    boolean isEdge = Math.round(distance) == radiusCircle;
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
                circle.add(line);
            }
            images.add(new FigureImage(circle, FigureType.CIRCLE));
        }

        return images;
    }
}
