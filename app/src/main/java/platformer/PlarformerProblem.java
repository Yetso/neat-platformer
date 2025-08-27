// package platformer;
//
// import java.io.BufferedReader;
// import java.io.FileReader;
// import java.io.IOException;
// import java.util.ArrayList;
// import java.util.List;
//
// import neat.*;
//
// public class PlarformerProblem implements Problem {
//     private int nTicks;
//     private int width;
//     private int height;
//     private Coordinate start;
//     private Coordinate goal;
//     private List<Coordinate> obstacles;
//     private static final double[][] INPUTS = { { 0, 0 }, { 0, 1 }, { 1, 0 }, { 1, 1 } };
//     // private static final double[] OUTPUTS = { 0, 1, 1, 0 };
//
//     public PlarformerProblem(String filePath) throws IOException {
//         this.obstacles = new ArrayList<>();
//         int index = 0;
//         String line;
//         BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));
//         while ((line = bufferedReader.readLine()) != null) {
//             String[] numbers = line.split("\\s+");
//             switch (index) {
//                 case 0:
//                     this.nTicks = Integer.parseInt(numbers[0]);
//                     break;
//                 case 1:
//                     this.width = Integer.parseInt(numbers[0]);
//                     this.height = Integer.parseInt(numbers[1]);
//                     break;
//                 case 2:
//                     this.start = new Coordinate(Integer.parseInt(numbers[0]), Integer.parseInt(numbers[1]));
//                     break;
//                 case 3:
//                     this.goal = new Coordinate(Integer.parseInt(numbers[0]), Integer.parseInt(numbers[1]));
//                     break;
//                 default:
//                     this.obstacles.add(new Coordinate(Integer.parseInt(numbers[0]), Integer.parseInt(numbers[1])));
//                     break;
//             }
//             index++;
//         }
//         bufferedReader.close();
//     }
//     @Override
//     public int getInputCount() {
//         return 2;
//     }
//
//     @Override
//     public int getOutputCount() {
//         return 1;
//     }
//
//     @Override
//     public int getNumberTest() {
//         return INPUTS.length;
//     }
//
//     @Override
//     public double[][] getInputs() {
//         return INPUTS;
//     }
//
//     @Override
//     public double evaluateFitness(Genome genome) {
//         double maxDistance = Math.sqrt(width * width + height * height);
//         Coordinate indiPosition = calculatePosition(genome);
//
//         double distance = Math.sqrt(
//                 Math.pow(goal.getX() - indiPosition.getX(), 2) + (Math.pow(goal.getY() - indiPosition.getY(), 2)));
//
//         genome.setFitness(1 / (1 + distance));
//         return genome.getFitness();
//     }
//
//
//     public void printLevel() {
//         for (int y = height - 1; y > -1; y--) {
//             for (int x = 0; x < width; x++) {
//                 Coordinate c = new Coordinate(x, y);
//                 if (c.equals(start)) {
//                     System.out.print("S "); // start
//                 } else if (c.equals(goal)) {
//                     System.out.print("G "); // goal
//                 } else if (obstacles.contains(c)) {
//                     System.out.print("󰝤 "); // obstacle
//                 } else {
//                     System.out.print(" "); // vide
//                 }
//             }
//             System.out.println();
//         }
//     }
//
//     // public void printLevel(Individual individual) {
//     //     Coordinate coord = calculatePosition(individual);
//     //     for (int y = height - 1; y > -1; y--) {
//     //         for (int x = 0; x < width; x++) {
//     //             Coordinate c = new Coordinate(x, y);
//     //             if (coord.equals(c)) {
//     //                 System.out.print("☻ "); // creaturePosition
//     //             } else if (c.equals(start)) {
//     //                 System.out.print("S "); // start
//     //             } else if (c.equals(goal)) {
//     //                 System.out.print("G "); // goal
//     //             } else if (obstacles.contains(c)) {
//     //                 System.out.print("󰝤 "); // obstacle
//     //             } else {
//     //                 System.out.print(" "); // vide
//     //             }
//     //         }
//     //         System.out.println();
//     //     }
//     // }
//
//     public double calculateFitness(Individual individual) {
//         double maxDistance = Math.sqrt(width * width + height * height);
//         Coordinate indiPosition = calculatePosition(individual);
//
//         double distance = Math.sqrt(
//                 Math.pow(goal.getX() - indiPosition.getX(), 2) + (Math.pow(goal.getY() - indiPosition.getY(), 2)));
//
//         individual.setFitness(1 / (1 + distance));
//         return individual.getFitness();
//
//     }
//
//     public Coordinate calculatePosition(Genome genome) {
//         Coordinate indiPosition = new Coordinate(0, 0);
//         for (Move move : genome.getMoves()) {
//             indiPosition = move(indiPosition, move);
//         }
//         return indiPosition;
//     }
//
//     public Coordinate move(Coordinate start, Move move) {
//         int dx = 0, dy = 0;
//
//         switch (move) {
//             case LEFT -> {
//                 dx = -1; dy = 0;
//             }
//             case UP_LEFT -> {
//                 dx = -1; dy = +1;
//             }
//             case UP -> {
//                 dx = 0; dy = +1;
//             }
//             case UP_RIGHT -> {
//                 dx = +1; dy = +1;
//             }
//             case RIGHT -> {
//                 dx = +1; dy = 0;
//             }
//             case DOWN_RIGHT -> {
//                 dx = +1; dy = -1;
//             }
//             case DOWN -> {
//                 dx = 0; dy = -1;
//             }
//             case DOWN_LEFT -> {
//                 dx = -1; dy = -1;
//             }
//             default -> throw new IllegalArgumentException("Unexpected value: " + move);
//         }
//         Coordinate wantTo = new Coordinate(start.getX() + dx, start.getY() + dy);
//         if (!isFree(wantTo))
//             return start;
//
//         return verifyGround(wantTo, dx);
//     }
//
//     // public Coordinate move(Coordinate start, Move move) {
//     // Coordinate end;
//     // switch (move) {
//     // case LEFT -> end = moveLeft(start);
//     // case UP_LEFT -> end = moveUpLeft(start);
//     // case UP -> end = moveUp(start);
//     // case UP_RIGHT -> end = moveUpRight(start);
//     // case RIGHT -> end = moveRight(start);
//     // case DOWN_RIGHT -> end = moveDownRight(start);
//     // case DOWN -> end = moveDown(start);
//     // case DOWN_LEFT -> end = moveDownLeft(start);
//     // default -> throw new IllegalArgumentException("Unexpected value: " + move);
//     // }
//     // return end;
//     // }
//     //
//     // private Coordinate moveLeft(Coordinate start) {
//     //     Coordinate wantTo = new Coordinate(start.getX() - 1, start.getY());
//     //     if (!isFree(wantTo))
//     //         return start;
//     //
//     //     return verifyGround(wantTo, -1);
//     // }
//     //
//     // private Coordinate moveUpLeft(Coordinate start) {
//     //     Coordinate wantTo = new Coordinate(start.getX() - 1, start.getY() + 1);
//     //     if (!isFree(wantTo))
//     //         return start;
//     //
//     //     return verifyGround(wantTo, -1);
//     // }
//     //
//     // private Coordinate moveUp(Coordinate start) {
//     //     Coordinate wantTo = new Coordinate(start.getX(), start.getY() + 1);
//     //     if (!isFree(wantTo))
//     //         return start;
//     //
//     //     return verifyGround(wantTo, 0);
//     // }
//     //
//     // private Coordinate moveUpRight(Coordinate start) {
//     //     Coordinate wantTo = new Coordinate(start.getX() + 1, start.getY() + 1);
//     //     if (!isFree(wantTo))
//     //         return start;
//     //
//     //     return verifyGround(wantTo, +1);
//     // }
//     //
//     // private Coordinate moveRight(Coordinate start) {
//     //     Coordinate wantTo = new Coordinate(start.getX() + 1, start.getY());
//     //     if (!isFree(wantTo))
//     //         return start;
//     //
//     //     return verifyGround(wantTo, +1);
//     // }
//     //
//     // private Coordinate moveDownRight(Coordinate start) {
//     //     Coordinate wantTo = new Coordinate(start.getX() + 1, start.getY() - 1);
//     //     if (!isFree(wantTo))
//     //         return start;
//     //
//     //     return verifyGround(wantTo, +1);
//     // }
//     //
//     // private Coordinate moveDown(Coordinate start) {
//     //     Coordinate wantTo = new Coordinate(start.getX(), start.getY() - 1);
//     //     if (!isFree(wantTo))
//     //         return start;
//     //
//     //     return verifyGround(wantTo, 0);
//     // }
//     //
//     // private Coordinate moveDownLeft(Coordinate start) {
//     //     Coordinate wantTo = new Coordinate(start.getX() - 1, start.getY() - 1);
//     //     if (!isFree(wantTo))
//     //         return start;
//     //
//     //     return verifyGround(wantTo, -1);
//     // }
//     //
//     private Coordinate verifyGround(Coordinate start, int xAxis) {
//         Coordinate wantTo = new Coordinate(start.getX(), start.getY());
//
//         while (isFree(wantTo.getX(), wantTo.getY() - 1)) {
//             if (xAxis != 0 && isFree(wantTo.getX() + xAxis, wantTo.getY() - 1)) {
//                 wantTo.changeX(xAxis);
//             }
//             wantTo.changeY(-1);
//         }
//         return wantTo;
//     }
//
//     /**
//      * Function to verify if the given coordinate is inside the play grid
//      * 
//      * @param coordinate
//      * @return True if the given coordinate is possible and free <br>
//      *         False if there is a obstacle or outside the grid
//      */
//     private boolean isFree(Coordinate coordinate) {
//         if (coordinate.getX() < 0 || coordinate.getX() >= width)
//             return false;
//         if (coordinate.getY() < 0 || coordinate.getY() >= height)
//             return false;
//         return !obstacles.contains(coordinate);
//     }
//
//     /**
//      * Function to verify if the given coordinate is inside the play grid
//      * 
//      * @param x
//      * @param y
//      * @return True if the given coordinate is possible and free <br>
//      *         False if there is a obstacle or outside the grid
//      */
//     private boolean isFree(int x, int y) {
//         return isFree(new Coordinate(x, y));
//     }
//
// }
