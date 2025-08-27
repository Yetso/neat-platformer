package neat;

import platformer.Coordinate;

public interface Problem {
    int getInputCount();

    int getOutputCount();

    double[] getInputs(Coordinate coordinate);

    double evaluateFitness(Genome genome);
}

