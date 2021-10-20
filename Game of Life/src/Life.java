import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class Life {

    private static char[][] LIFE = new char[20][20];
    private static Cell[][] CELLS = new Cell[20][20];
    private static boolean willContinue = true;

    public static void main(String[] args) throws FileNotFoundException {

        initializeCells(CELLS);

        Scanner coordinates = new Scanner(new File("coordinates.txt")); //gets coordinates (row, column)
        while(coordinates.hasNextLine()) {                                       //and births initial cells there.
            String[] coordRow = coordinates.nextLine().split(",");
            int rowCoord = Integer.parseInt(coordRow[0]);
            int colCoord = Integer.parseInt(coordRow[1]);
            birthCell(CELLS, rowCoord-1, colCoord-1);
        }

        Scanner kb = new Scanner(System.in);
        System.out.println("Input Max Generations: ");
        int maxGenerations = kb.nextInt();

        int totalGenerations = 0;
        while(willContinue) {
            System.out.println(totalGenerations);
            totalGenerations++;
            UpdateLife(CELLS, LIFE);

            if(checkAllDead(LIFE)) {
                System.out.println("All Cells Died!");
                System.out.println("Total Generations: " + totalGenerations);
                willContinue = false;
            }
            else if(totalGenerations == maxGenerations) {
                System.out.println("You survived over " + maxGenerations + " generations.");
                willContinue = false;
            }
        }


    }

    private static void UpdateLife(Cell[][] cells, char[][] life) {
        //birth a cell if it has exactly three living neighbors
        //kill a cell if it has one neighbor or less
        //kill a cell if it has four or more neighbors
        //keep the cell if it has two or three neighbors
        //if all cells are dead, set willcontinue to false

        calculateNeighbors(cells, true);
        calculateNeighbors(cells, false);
        setChars(cells, life);
        printArray(life);

        for(int r = 0; r < cells.length; r++) {
            for(int c = 0; c < cells[r].length; c++) {

                if(cells[r][c].isLiving() == false && cells[r][c].getNeighbors() == 3) {
                    birthCell(cells, r, c);
                    setChars(cells, life);
                }
                else if(cells[r][c].isLiving() && (cells[r][c].getNeighbors() <= 1 || cells[r][c].getNeighbors() >= 4)) {
                    killCell(cells, r, c);
                    setChars(cells, life);
                }
            }
        }
        resetAllNeighbors(cells);
    }

    private static void printArray(char[][] print) { //prints the 2d char array
        for (char[] row : print) {
            for(char space : row) {
                System.out.print(space + " ");
            }
            System.out.println();
        }
    }
    private static void printNeighborTest(int[][] print) { //test method to print 2d int neighbor count arrays
        for (int[] row : print) {
            System.out.println(Arrays.toString(row));
        }
    }

    private static char[][] setChars(Cell[][] cells, char[][] life) { //sets array of chars that make the game
        for(int r = 0; r < cells.length; r++)
            for(int c = 0; c < cells[r].length; c++) {
                if(cells[r][c].isLiving())
                    life[r][c] = '■';
                else
                    life[r][c] = '.';
            }
        return life;
    }

    private static void initializeCells(Cell[][] cells) { //initializes 2d cell array
        for(int r = 0; r < cells.length; r++) {
            for(int c = 0; c < cells[r].length; c++) {
                cells[r][c] = new Cell();
                cells[r][c].setLiving(false);
            }
        }
    }

    private static void birthCell(Cell[][] cells, int row, int col) { //births a new cell
        cells[row][col].setLiving(true);
    }

    private static void killCell(Cell[][] cells, int row, int col) { //kills a living cell
        cells[row][col].setLiving(false);
    }

    private static void calculateNeighbors(Cell[][] cells, boolean living) { //calculates neighbors for living or dead cells
        //int[][] answer = new int[20][20];

        for(int r = 0; r < cells.length; r++) {
            for(int c = 0; c < cells[r].length; c++) {

                if(cells[r][c].isLiving() == living) {

                    int startR;
                    int startC;
                    int endR;
                    int endC;

                    switch(r) {
                        case(0):
                            startR = 0;
                            endR = r+1;
                            break;
                        case(19):
                            startR = r-1;
                            endR = r;
                            break;
                        default:
                            startR = r-1;
                            endR = r+1;
                    }
                    switch(c) {
                        case(0):
                            startC = 0;
                            endC = c+1;
                            break;
                        case(19):
                            startC = c-1;
                            endC = c;
                            break;
                        default:
                            startC = c-1;
                            endC = c+1;
                    }

                    for(int checkRow = startR; checkRow <= endR; checkRow++)
                        for(int checkCol = startC; checkCol <= endC; checkCol++) {
                            if(!(checkRow == r && checkCol == c))
                                if(cells[checkRow][checkCol].isLiving())
                                    cells[r][c].setNeighbors(cells[r][c].getNeighbors() + 1);
                        }
                }

                //answer[r][c] = cells[r][c].getNeighbors();

            }
        }

        //return answer;

    }

    private static void resetAllNeighbors(Cell[][] cells) { //resets neighbor counts after an update
        for(int r = 0; r < cells.length; r++)
            for(int c = 0; c < cells[r].length; c++) {
                cells[r][c].setNeighbors(0);
            }
    }

    private static boolean checkAllDead(char[][] life) { //checks if all cells are dead
        for(int r = 0; r < life.length; r++) {
            for(int c = 0; c < life[r].length; c++) {
                if(life[r][c] == '■')
                    return false;
            }
        }
        return true;
    }

}
