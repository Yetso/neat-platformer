import java.io.IOException;

import neat.*;
import platformer.*;

public class App {

    public static void main(String[] args) {
        PlatformProblem problem;
        try {
            problem = new PlatformProblem("./src/main/resources/level1.txt");
        } catch (IOException e) {
            throw new RuntimeException("Error loading level : ", e);
        }

        NeatAlgorithm neatAlgorithm = new NeatAlgorithm(problem);
        neatAlgorithm.CreateGenomes();
        Genome solution = neatAlgorithm.Train();

        System.out.println("Final solution:");
        double score = solution.getFitness();
        System.out.println("score = " + score);

        problem.evaluateFitness(solution);
        problem.printLevel(problem.calculatePostion(solution, true)); 
    }
}
