package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Board {
    private int[][] grid; // 2D array to store the state of the grid
    private int width;    // Width of the board in cells
    private int height;   // Height of the board in cells
    private int cellSize; // Size of each cell in pixels
    private int offsetX;  // Horizontal offset to center the board
    private int offsetY;  // Vertical offset to center the board
    private TetrinomeController controller; // Tetrinome controller
    private Tetris tetris; // Reference to Tetris for GameOver access

    // Constructor
    public Board(int width, int height, int cellSize, int gameWidth, int gameHeight, Tetris tetris) {
        this.width = width;
        this.height = height;
        this.cellSize = cellSize;
        this.grid = new int[height][width]; // Initialize the grid

        // Calculate offsets to center the board
        this.offsetX = (gameWidth - width * cellSize) / 2;
        this.offsetY = (gameHeight - height * cellSize) / 2;
        this.tetris = tetris; // Store the reference to Tetris
        this.controller = new TetrinomeController(); // Initialize the controller
    }

    // Draws the grid and occupied cells
    public void draw(Graphics brush) {
        // Draw grid lines
        brush.setColor(Color.DARK_GRAY);
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                int x = offsetX + col * cellSize;
                int y = offsetY + row * cellSize;
                brush.drawRect(x, y, cellSize, cellSize);
            }
        }

        // Draw occupied cells
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                if (grid[row][col] != 0) {
                    int x = offsetX + col * cellSize;
                    int y = offsetY + row * cellSize;
                    brush.setColor(Color.WHITE);
                    brush.fillRect(x, y, cellSize, cellSize);
                }
            }
        }
    }

    public boolean isCollision(Tetrinome t) {
        Point[] points = t.getPoints(); // Get the Tetrinome's current points
        for (Point point : points) {
            int col = (int) Math.floor((point.x - offsetX) / cellSize); // Map to grid column
            int row = (int) Math.floor((point.y - offsetY) / cellSize); // Map to grid row

            // Check for out-of-bounds collision
            if (col < 0 || col >= width || row < 0 || row >= height) {
                return true; // Collision detected
            }

            // Check for collision with occupied cells
            if (grid[row][col] != 0) {
                return true; // Collision detected
            }
        }
        return false; // No collision detected
    }

    // Places the Tetrinome on the grid
    public void placeTetrinome(Tetrinome t) {
        Point[] points = t.getPoints();
        for (Point point : points) {
            int col = (int) Math.floor((point.x - offsetX) / cellSize);
            int row = (int) Math.floor((point.y - offsetY) / cellSize);

            // Mark the grid cell as occupied
            if (row >= 0 && row < height && col >= 0 && col < width) {
                grid[row][col] = 1;
            }
        }
        t.setPlaced(true); // Marks the Tetrinome as placed
        clearLines(); // Checks for and clear completed lines
    }

    // Clears completed lines from the grid
    public void clearLines() {
        for (int row = 0; row < height; row++) {
            boolean isComplete = true;
            for (int col = 0; col < width; col++) {
                if (grid[row][col] == 0) {
                    isComplete = false;
                    break;
                }
            }

            if (isComplete) {
                // Shifts all rows above down by one
                for (int r = row; r > 0; r--) {
                    System.arraycopy(grid[r - 1], 0, grid[r], 0, width);
                }
                // Clears the top row
                for (int col = 0; col < width; col++) {
                    grid[0][col] = 0;
                }
            }
        }
    }

    // Inner class for managing Tetrinomes and handling key events
    public class TetrinomeController implements KeyListener {
        private List<Tetrinome> tetrinomeBag;
        private Tetrinome activeTetrinome;

        public TetrinomeController() {
            this.tetrinomeBag = new ArrayList<>();
            refillBag(); // Fill the bag
            spawnNextTetrinome();
        }

        private void refillBag() {
            tetrinomeBag.clear();
            for (TetrinomeType type : TetrinomeType.values()) {
                tetrinomeBag.add(new Tetrinome(type, new Point(4 * cellSize, 0), Board.this));
            }
            Collections.shuffle(tetrinomeBag); // Shuffle the bag
        }

        private void spawnNextTetrinome() {
            if (tetrinomeBag.isEmpty()) {
                refillBag(); // Refresh the Tetrinome bag if empty
            }
            Tetrinome nextTetrinome = tetrinomeBag.remove(0); // Get the next piece

            // Check if Game Over condition is met
            if (tetris.getGameOver().isActive() || isCollision(nextTetrinome)) { 
                // Trigger Game Over in Tetris
                tetris.getGameOver().trigger();
                return; // Stop spawning the piece
            }

            activeTetrinome = nextTetrinome; // Activate the piece if no collision
            activeTetrinome.setPlaced(false); // Ensure it is not marked as placed
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (activeTetrinome == null || activeTetrinome.isPlaced() || tetris.getGameOver().isActive()) return;

            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    activeTetrinome.move(t -> {
                        t.position.x -= cellSize;
                        if (isCollision(t)) t.position.x += cellSize; // Undo move if collision occurs
                    });
                    break;
                case KeyEvent.VK_RIGHT:
                    activeTetrinome.move(t -> {
                        t.position.x += cellSize;
                        if (isCollision(t)) t.position.x -= cellSize; // Undo move if collision occurs
                    });
                    break;
                case KeyEvent.VK_DOWN:
                    activeTetrinome.move(t -> {
                        t.position.y += cellSize;
                        if (isCollision(t)) {
                            t.position.y -= cellSize; // Undo move if collision occurs
                            placeTetrinome(t); // Place the Tetrinome if it hits the bottom
                        }
                    });
                    break;
                case KeyEvent.VK_UP:
                    activeTetrinome.move(t -> {
                        t.rotation += 90;
                        if (isCollision(t)) t.rotation -= 90; // Undo rotation if collision occurs
                    });
                    break;
            }

            if (activeTetrinome.isPlaced()) {
                placeTetrinome(activeTetrinome);
                spawnNextTetrinome();
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {}

        @Override
        public void keyTyped(KeyEvent e) {}

        public Tetrinome getActiveTetrinome() {
            return activeTetrinome;
        }
    }

    public TetrinomeController getController() {
        return controller;
    }
}
