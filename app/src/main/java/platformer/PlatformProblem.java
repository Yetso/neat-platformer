package platformer;

import lombok.Data;
import neat.Genome;
import neat.Problem;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Data
public class PlatformProblem implements Problem {
    private int nTicks;
    private int width;
    private int height;
    private Coordinate start;
    private Coordinate goal;
    private final List<Coordinate> obstacles;

    public PlatformProblem(String filePath) throws IOException {
        this.obstacles = new ArrayList<>();
        int index = 0;
        String line;
        BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));
        while ((line = bufferedReader.readLine()) != null) {
            String[] numbers = line.split("\\s+");
            switch (index) {
                case 0:
                    this.nTicks = Integer.parseInt(numbers[0]);
                    break;
                case 1:
                    this.width = Integer.parseInt(numbers[0]);
                    this.height = Integer.parseInt(numbers[1]);
                    break;
                case 2:
                    this.start = new Coordinate(Integer.parseInt(numbers[0]), Integer.parseInt(numbers[1]));
                    break;
                case 3:
                    this.goal = new Coordinate(Integer.parseInt(numbers[0]), Integer.parseInt(numbers[1]));
                    break;
                default:
                    this.obstacles.add(new Coordinate(Integer.parseInt(numbers[0]), Integer.parseInt(numbers[1])));
                    break;
            }
            index++;
        }
        bufferedReader.close();
    }

    @Override
    public int getInputCount() {
        return 10;
    }

    @Override
    public int getOutputCount() {
        return 8;
    }

    @Override
    public double[] getInputs(Coordinate coordinate) {
        double[] input = new double[10];

        int x = coordinate.getX();
        int y = coordinate.getY();

        // Directions around creature
        int[][] directions = {
                { 0, -1 }, // up
                { 0, 1 }, // down
                { -1, 0 }, // left
                { 1, 0 }, // right
                { -1, -1 }, // up-left
                { 1, -1 }, // up-right
                { -1, 1 }, // down-left
                { 1, 1 } // down-right
        };

        // 8 close cases, 1 if obstacles 0 else
        for (int i = 0; i < 8; i++) {
            int nx = x + directions[i][0];
            int ny = y + directions[i][1];

            input[i] = (isFree(nx, ny)) ? 0.0 : 1.0;
        }

        int dx = goal.getX() - x;
        int dy = goal.getY() - y;

        input[8] = dx / (double) width;
        input[9] = dy / (double) height;

        return input;
    }

    @Override
    public double evaluateFitness(Genome genome) {
        Coordinate coordinate = calculatePostion(genome);

        double distance = Math.sqrt(
                Math.pow(goal.getX() - coordinate.getX(), 2)
                        + (Math.pow(goal.getY() - coordinate.getY(), 2)));

        genome.setFitness(1 / (1 + distance));
        return 1 / (1 + distance);
    }

    public Coordinate calculatePostion(Genome genome) {
        return calculatePostion(genome, false);
    }

    public Coordinate calculatePostion(Genome genome, boolean print) {
        Coordinate coordinate = new Coordinate(start);

        if (print) printLevel(coordinate);
        for (int i = 0; i < nTicks; i++) {
            int moveIndex = 0;
            double[] output = genome.evaluate(getInputs(coordinate));
            for (int j = 1 ; j < output.length ; j++) {
                if (output[j] > output[moveIndex]) moveIndex = j;
            }
            Move move = Move.values()[moveIndex];
            coordinate = move(coordinate, move);
            if (print) printLevel(coordinate);
        }
        return coordinate;
    }

    public void printLevel() {
        for (int y = height - 1; y > -1; y--) {
            for (int x = 0; x < width; x++) {
                Coordinate c = new Coordinate(x, y);
                if (c.equals(start)) {
                    System.out.print("S "); // start
                } else if (c.equals(goal)) {
                    System.out.print("G "); // goal
                } else if (obstacles.contains(c)) {
                    System.out.print("󰝤 "); // obstacle
                } else {
                    System.out.print(" "); // vide
                }
            }
            System.out.println();
        }
    }
    public void printLevel(Coordinate coordinate) {
        for (int y = height - 1; y > -1; y--) {
            for (int x = 0; x < width; x++) {
                Coordinate c = new Coordinate(x, y);
                if (c.equals(coordinate)) {
                    System.out.print("☻ "); // creature
                } else if (c.equals(start)) {
                    System.out.print("S "); // start
                } else if (c.equals(goal)) {
                    System.out.print("G "); // goal
                } else if (obstacles.contains(c)) {
                    System.out.print("󰝤 "); // obstacle
                } else {
                    System.out.print(" "); // vide
                }
            }
            System.out.println();
        }
    }

    // public Coordinate calculatePosition(Genome individual) {
    // Coordinate indiPosition = new Coordinate(0, 0);
    // for (Move move : individual.getMoves()) {
    // indiPosition = move(indiPosition, move);
    // }
    // return indiPosition;
    // }

    public Coordinate move(Coordinate start, Move move) {
        int dx = 0, dy = 0;

        switch (move) {
            case LEFT -> {
                dx = -1;
                dy = 0;
            }
            case UP_LEFT -> {
                dx = -1;
                dy = +1;
            }
            case UP -> {
                dx = 0;
                dy = +1;
            }
            case UP_RIGHT -> {
                dx = +1;
                dy = +1;
            }
            case RIGHT -> {
                dx = +1;
                dy = 0;
            }
            case DOWN_RIGHT -> {
                dx = +1;
                dy = -1;
            }
            case DOWN -> {
                dx = 0;
                dy = -1;
            }
            case DOWN_LEFT -> {
                dx = -1;
                dy = -1;
            }
            default -> throw new IllegalArgumentException("Unexpected value: " + move);
        }
        Coordinate wantTo = new Coordinate(start.getX() + dx, start.getY() + dy);
        if (!isFree(wantTo))
            return start;

        return verifyGround(wantTo, dx);
    }

    private Coordinate verifyGround(Coordinate start, int xAxis) {
        Coordinate wantTo = new Coordinate(start.getX(), start.getY());

        while (isFree(wantTo.getX(), wantTo.getY() - 1)) {
            if (xAxis != 0 && isFree(wantTo.getX() + xAxis, wantTo.getY() - 1)) {
                wantTo.changeX(xAxis);
            }
            wantTo.changeY(-1);
        }
        return wantTo;
    }

    /**
     * Function to verify if the given coordinate is inside the play grid
     * 
     * @param coordinate
     * @return True if the given coordinate is possible and free <br>
     *         False if there is a obstacle or outside the grid
     */
    private boolean isFree(Coordinate coordinate) {
        if (coordinate.getX() < 0 || coordinate.getX() >= width)
            return false;
        if (coordinate.getY() < 0 || coordinate.getY() >= height)
            return false;
        return !obstacles.contains(coordinate);
    }

    /**
     * Function to verify if the given coordinate is inside the play grid
     * 
     * @param x
     * @param y
     * @return True if the given coordinate is possible and free <br>
     *         False if there is a obstacle or outside the grid
     */
    private boolean isFree(int x, int y) {
        return isFree(new Coordinate(x, y));
    }

}
