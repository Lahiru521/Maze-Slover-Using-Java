import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Stack;

public class MazeSolver1 {
    private char[][] maze; // 2D array representing the maze
    private int rows; // Number of rows in the maze
    private int cols; // Number of columns in the maze
    private boolean[][] correctPositions; // To keep track of correct positions (cells forming the correct path)

    // Constructor to initialize the MazeSolver1 object with a given maze
    public MazeSolver1(char[][] maze) {
        this.maze = maze;
        this.rows = maze.length;
        this.cols = maze[0].length;
        this.correctPositions = new boolean[rows][cols];
    }

    // Method to solve the maze and find the path from 'S' (start) to 'E' (end)
    public void solveMaze() {
        Stack<int[]> stack = new Stack<>(); // Stack to keep track of cells during exploration
        boolean[][] visited = new boolean[rows][cols]; // 2D array to mark visited cells
        int[] start = findStart(); // Find the starting position ('S') in the maze
        int[] goal = null; // To store the coordinates of the goal position ('E')

        if (start == null) {
            System.out.println("No start position 'S' found.");
            return;
        }

        stack.push(start); // Push the starting position onto the stack

        while (!stack.isEmpty()) {
            int[] current = stack.pop(); // Pop a cell from the stack
            int row = current[0]; // Get the row index of the current cell
            int col = current[1]; // Get the column index of the current cell

            if (visited[row][col]) {
                continue; // Skip already visited cells
            }

            visited[row][col] = true; // Mark the current cell as visited

            if (maze[row][col] == 'E') {
                System.out.println("Path found!"); // Path from 'S' to 'E' is found
                goal = new int[]{row, col}; // Store the goal coordinates
                break;
            }

            // Explore neighboring cells (left, right, up, and down)
            exploreNeighboringCells(stack, visited, row, col - 1); // Left
            exploreNeighboringCells(stack, visited, row, col + 1); // Right
            exploreNeighboringCells(stack, visited, row - 1, col); // Up
            exploreNeighboringCells(stack, visited, row + 1, col); // Down
        }

        if (goal != null) {
            markPathWithStar(goal[0], goal[1]); // Mark the correct path with '*' symbols
            printMazeWithWitness(); // Print the maze with the witness path
        } else {
            System.out.println("No path found."); // No path from 'S' to 'E' is found
        }
    }

    // Method to explore neighboring cells and push valid unvisited cells into the stack
    private void exploreNeighboringCells(Stack<int[]> stack, boolean[][] visited, int row, int col) {
        if (isValidCell(row, col) && !visited[row][col] && maze[row][col] != '#') {
            stack.push(new int[]{row, col}); // Push the cell coordinates onto the stack
            correctPositions[row][col] = true; // Store correct positions during exploration
        }
    }

    // Method to check if a given cell is within the maze boundaries
    private boolean isValidCell(int row, int col) {
        return row >= 0 && row < rows && col >= 0 && col < cols;
    }

    // Method to find the starting position ('S') in the maze
    private int[] findStart() {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (maze[row][col] == 'S') {
                    return new int[]{row, col}; // Return the starting position coordinates
                }
            }
        }
        return null; // Return null if no 'S' is found in the maze
    }

    // Method to mark the correct path with '*' symbols from the goal to the start position ('S')
    private void markPathWithStar(int row, int col) {
        int[] previous;
        while (maze[row][col] != 'S') { // Until we reach the starting position
            maze[row][col] = '*'; // Mark the cell as part of the correct path
            previous = findPreviousVisited(row, col); // Find the previous visited cell in the path
            if (previous == null) {
                break;
            }
            row = previous[0]; // Move to the previous cell in the path
            col = previous[1];
        }
    }

    // Method to find the previous visited cell in the path during backtracking
    private int[] findPreviousVisited(int row, int col) {
        if (isValidCell(row - 1, col) && maze[row - 1][col] == '*') {
            return new int[]{row - 1, col};
        } else if (isValidCell(row + 1, col) && maze[row + 1][col] == '*') {
            return new int[]{row + 1, col};
        } else if (isValidCell(row, col - 1) && maze[row][col - 1] == '*') {
            return new int[]{row, col - 1};
        } else if (isValidCell(row, col + 1) && maze[row][col + 1] == '*') {
            return new int[]{row, col + 1};
        }
        return null;
    }

    // Method to print the maze with the correct path marked as '*' and unvisited cells as '.'
    private void printMazeWithWitness() {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (maze[row][col] == 'S') {
                    System.out.print('S'); // Start position
                } else if (maze[row][col] == 'E') {
                    System.out.print('E'); // End position
                } else if (correctPositions[row][col]) {
                    System.out.print('*'); // Correct path
                } else {
                    System.out.print('.'); // Unvisited cell
                }
            }
            System.out.println();
        }
    }

    // Main method to run the maze solver

    public static void main(String[] args) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("Maze1.txt"));

            String line;
            int row = 0;
            int cols = 0;
            char[][] maze = new char[10][10]; // Assuming maximum maze size is 10x10

            while ((line = reader.readLine()) != null) {
                cols = line.length();
                for (int col = 0; col < cols; col++) {
                    maze[row][col] = line.charAt(col); // Fill in the maze array from the input file
                }
                row++;
            }

            // Trim the maze array to the actual size
            char[][] trimmedMaze = new char[row][cols];
            for (int i = 0; i < row; i++) {
                trimmedMaze[i] = maze[i];
            }

            MazeSolver1 solver = new MazeSolver1(trimmedMaze); // Create a maze solver object
            solver.solveMaze(); // Solve and print the maze with the correct path

            reader.close(); // Close the file reader
        } catch (IOException e) {
            e.printStackTrace(); // Print any IO exception that occurs during file reading
        }

    }
}