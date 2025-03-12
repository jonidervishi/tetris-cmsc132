package game;

import java.awt.Color;
import java.awt.Graphics;

/**
 * Represents the Tetris game board.
 */
public class Board {
    private int[][] grid; // 2D array to store the state of the grid
    private int width;    // Width of the board in cells
    private int height;   // Height of the board in cells
    private int cellSize; // Size of each cell in pixels
    private int offsetX;  // Horizontal offset to center the board
    private int offsetY;  // Vertical offset to center the board

    /**
     * Constructs a new Board instance.
     *
     * @param width    The width of the board in cells.
     * @param height   The height of the board in cells.
     * @param cellSize The size of each cell in pixels.
     * @param gameWidth The width of the game window.
     * @param gameHeight The height of the game window.
     */
    public Board(int width, int height, int cellSize, int gameWidth, int gameHeight) {
        this.width = width;
        this.height = height;
        this.cellSize = cellSize;
        this.grid = new int[height][width]; // Initialize the grid

        // Calculate offsets to center the board
        this.offsetX = (gameWidth - width * cellSize) / 2;
        this.offsetY = (gameHeight - height * cellSize) / 2;
    }

    /**
     * Draws the board using the provided Graphics object.
     *
     * @param brush The Graphics object used for rendering.
     */
    public void draw(Graphics brush) {
        // Draw the grid without grid lines
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                int x = offsetX + col * cellSize;
                int y = offsetY + row * cellSize;

                // Draw a cell if it's occupied
                if (grid[row][col] != 0) {
                    brush.setColor(Color.WHITE);
                    brush.fillRect(x, y, cellSize, cellSize);
                }
            }
        }
    }

    /**
     * Clears completed lines on the board.
     */
    public void clearLines() {
        // Logic to clear completed lines
    }

    /**
     * Checks if a Tetromino collides with the board or other Tetrominoes.
     *
     * @param t The Tetromino to check for collisions.
     * @return True if a collision is detected, false otherwise.
     */
    // public boolean isCollision(Tetromino t) {
    //     // Logic to check for collisions
    //     return false;
    // }

    /**
     * Adds collision points on the bottom row and sides of the board.
     *
     * @return An array of Points representing the collision points.
     */
    public Point[] getCollisionPoints() {
        Point[] collisionPoints = new Point[width + 2 * height]; // Bottom row + left and right sides

        // Bottom row
        for (int col = 0; col < width; col++) {
            collisionPoints[col] = new Point(offsetX + col * cellSize, offsetY + height * cellSize);
        }

        // Left and right sides
        for (int row = 0; row < height; row++) {
            collisionPoints[width + row] = new Point(offsetX, offsetY + row * cellSize); // Left side
            collisionPoints[width + height + row] = new Point(offsetX + width * cellSize, offsetY + row * cellSize); // Right side
        }

        return collisionPoints;
    }
}