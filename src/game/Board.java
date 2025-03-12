package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
    private TetrinomeController controller; // Tetrinome controller

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
        this.grid = new int[height][width + 1]; // Initialize the grid with an extra column

        // Calculate offsets to center the board
        this.offsetX = ((gameWidth - width * cellSize) / 2 / cellSize) * cellSize;
        this.offsetY = ((gameHeight - height * cellSize) / 2 / cellSize) * cellSize;
    }

    /**
     * Inner class for managing Tetrinomes and handling key events.
     */
    public class TetrinomeController implements KeyListener {
        private List<Tetrinome> tetrinomeBag; // Bag of Tetrinomes for random generation
        private Tetrinome activeTetrinome;    // Currently active Tetrinome

        public TetrinomeController() {
            this.tetrinomeBag = new ArrayList<>();
            this.activeTetrinome = null;
            refillBag(); // Fill the bag with Tetrinomes
            spawnNextTetrinome(); // Spawn the first Tetrinome
        }

        /**
         * Refills the bag with one of each Tetrinome type.
         */
        private void refillBag() {
            tetrinomeBag.clear();
            for (TetrinomeType type : TetrinomeType.values()) {
                tetrinomeBag.add(new Tetrinome(type, new Point(12 * cellSize, 0), Board.this));
            }
            Collections.shuffle(tetrinomeBag); // Shuffle the bag for randomness
        }

        /**
         * Spawns the next Tetrinome from the bag.
         */
        private void spawnNextTetrinome() {
            if (tetrinomeBag.isEmpty()) {
                refillBag(); // Refill the bag if empty
            }
            activeTetrinome = tetrinomeBag.remove(0); // Get the next Tetrinome
            activeTetrinome.setPlaced(false); // Ensure it's not marked as placed
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (activeTetrinome == null || activeTetrinome.isPlaced()) {
                return; // Ignore input if no active Tetrinome or it's already placed
            }

            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    activeTetrinome.move(true, false, false, false);
                    break;
                case KeyEvent.VK_RIGHT:
                    activeTetrinome.move(false, true, false, false);
                    break;
                case KeyEvent.VK_DOWN:
                    activeTetrinome.move(false, false, true, false);
                    break;
                case KeyEvent.VK_UP:
                    activeTetrinome.move(false, false, false, true);
                    break;
            }

            // Check if the active Tetrinome is placed
            if (activeTetrinome.isPlaced()) {
                addTetrinomeToGrid(activeTetrinome); // Add the Tetrinome to the grid
                spawnNextTetrinome(); // Spawn a new Tetrinome
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {}

        @Override
        public void keyTyped(KeyEvent e) {}

        /**
         * Gets the currently active Tetrinome.
         *
         * @return The active Tetrinome.
         */
        public Tetrinome getActiveTetrinome() {
            return activeTetrinome;
        }
    }

    /**
     * Gets the TetrinomeController for this board.
     *
     * @return The TetrinomeController.
     */
    public TetrinomeController getController() {
        return controller;
    }

    /**
     * Draws the board using the provided Graphics object.
     *
     * @param brush The Graphics object used for rendering.
     */
    public void draw(Graphics brush) {
        // Draw the grid lines, excluding the extra column
        brush.setColor(Color.DARK_GRAY);
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                int x = offsetX + col * cellSize;
                int y = offsetY + row * cellSize;
                brush.drawRect(x, y, cellSize, cellSize);
            }
        }

        // Draw the occupied cells, excluding the extra column
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
     * Checks if a Tetrinome collides with the board or other Tetrinomes.
     *
     * @param t The Tetrinome to check for collisions.
     * @return True if a collision is detected, false otherwise.
     */
    public boolean isCollision(Tetrinome t) {
        Point[] points = t.getPoints();
        for (Point point : points) {
            int col = (int) Math.floor((point.x - offsetX) / cellSize);
            int row = (int) Math.floor((point.y - offsetY) / cellSize);

            // Check if the point is outside the board boundaries
            if (col < 0 || col >= width + 1 || row < 0 || row >= height) {
                return true;
            }

            // Check if the point collides with an occupied cell
            if (grid[row][col] != 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * Places the Tetrinome on the board if it touches the bottom collision points.
     *
     * @param t The Tetrinome to place on the board.
     */
    public void placeTetrinome(Tetrinome t) {
        Point[] points = t.getPoints();
        for (Point point : points) {
            int col = (int) Math.floor((point.x - offsetX) / cellSize);
            int row = (int) Math.floor((point.y - offsetY) / cellSize);

            // Check if the point is at the bottom of the board
            if (row >= height - 1) {
                addTetrinomeToGrid(t);
                t.setPlaced(true); // Mark the Tetrinome as placed
                return;
            }

            // Check if the point collides with an occupied cell below
            if (grid[row + 1][col] != 0) {
                addTetrinomeToGrid(t);
                t.setPlaced(true); // Mark the Tetrinome as placed
                return;
            }
        }
    }

    /**
     * Adds collision points on the bottom row and sides of the board.
     *
     * @return An array of Points representing the collision points.
     */
    public Point[] getCollisionPoints() {
        Point[] collisionPoints = new Point[width + 1 + 2 * height]; // Bottom row + left and right sides

        // Bottom row
        for (int col = 0; col < width + 1; col++) {
            collisionPoints[col] = new Point(offsetX + col * cellSize, offsetY + height * cellSize);
        }

        // Left and right sides
        for (int row = 0; row < height; row++) {
            collisionPoints[width + 1 + row] = new Point(offsetX, offsetY + row * cellSize); // Left side
            collisionPoints[width + 1 + height + row] = new Point(offsetX + width * cellSize, offsetY + row * cellSize); // Right side
        }

        return collisionPoints;
    }

    /**
     * Updates the grid with the occupied cells of the Tetrinome.
     *
     * @param t The Tetrinome to add to the grid.
     */
    public void addTetrinomeToGrid(Tetrinome t) {
        int[][] occupiedCells = getOccupiedCells(t);
        for (int[] cell : occupiedCells) {
            int row = cell[0];
            int col = cell[1];
            grid[row][col] = 1; // Mark the cell as occupied
        }
    }

    /**
     * Gets the indexes of the cells occupied by the Tetrinome.
     *
     * @param t The Tetrinome to check.
     * @return A 2D array of the occupied cells' indexes.
     */
    private int[][] getOccupiedCells(Tetrinome t) {
        Point[] points = t.getPoints();
        int[][] occupiedCells = new int[points.length][2];
        for (int i = 0; i < points.length; i++) {
            int col = (int) Math.floor((points[i].x - offsetX) / cellSize);
            int row = (int) Math.floor((points[i].y - offsetY) / cellSize);
            occupiedCells[i][0] = row;
            occupiedCells[i][1] = col;
        }
    
        // Step 1: Find the highest row value for each column
        java.util.Map<Integer, Integer> maxRowPerColumn = new java.util.HashMap<>();
        for (int[] cell : occupiedCells) {
            int col = cell[1];
            int row = cell[0];
            maxRowPerColumn.put(col, Math.max(maxRowPerColumn.getOrDefault(col, Integer.MIN_VALUE), row));
        }
    
        // Step 2: Find the highest column value for each row
        java.util.Map<Integer, Integer> maxColPerRow = new java.util.HashMap<>();
        for (int[] cell : occupiedCells) {
            int row = cell[0];
            int col = cell[1];
            maxColPerRow.put(row, Math.max(maxColPerRow.getOrDefault(row, Integer.MIN_VALUE), col));
        }
    
        // Step 3: Filter out rows that have the highest row value for their column
        // OR columns that have the highest column value for their row
        java.util.List<int[]> filteredCells = new java.util.ArrayList<>();
        for (int[] cell : occupiedCells) {
            int row = cell[0];
            int col = cell[1];
            boolean isMaxRowForCol = (row == maxRowPerColumn.get(col));
            boolean isMaxColForRow = (col == maxColPerRow.get(row));
            if (!isMaxRowForCol && !isMaxColForRow) {
                filteredCells.add(cell);
            }
        }
    
        // Step 4: Handle exceptions for specific block types and rotations
        double rotation = t.getRotation();
        switch (t.getType()) {
            case O:
                // For the O block, ensure the bottom-right cell is included
                int maxRow = Collections.max(maxRowPerColumn.values());
                int maxCol = Collections.max(maxColPerRow.values());
                filteredCells.add(new int[]{maxRow, maxCol});
                break;
    
            case Z:
                if (rotation == 90 || rotation == 270) {
                    // For the Z block at 90° or 270°, remove the bottom-right cell
                    int maxRowZ = Collections.max(maxRowPerColumn.values());
                    int maxColZ = Collections.max(maxColPerRow.values());
                    filteredCells.removeIf(cell -> cell[0] == maxRowZ && cell[1] == maxColZ);
                }
                break;
    
            case T:
                if (rotation == 0 || rotation == 270) {
                    // For the T block at 0° or 270°, remove the bottom-right cell
                    int maxRowT = Collections.max(maxRowPerColumn.values());
                    int maxColT = Collections.max(maxColPerRow.values());
                    filteredCells.removeIf(cell -> cell[0] == maxRowT && cell[1] == maxColT);
                }
                break;
    
            default:
                throw new IllegalArgumentException("Unknown Tetrinome type: " + t.getType());
        }
    
        // Step 5: Convert the filtered list back to a 2D array
        int[][] result = new int[filteredCells.size()][2];
        for (int i = 0; i < filteredCells.size(); i++) {
            result[i] = filteredCells.get(i);
        }
    
        return result;
    }   
}