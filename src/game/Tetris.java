package game;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.Timer;

/**
 * The main class for the Tetris game.
 * Extends the Game class and serves as the control center for the game.
 * Handles the game board, Tetrinome management, and rendering.
 */
class Tetris extends Game {
    static int counter = 0; // Counter for debugging purposes, incremented each time the canvas is repainted.
    private Tetrinome[] tetrinomes; // Array of Tetrinomes used in the game.
    private static final int TILE_SIZE = 30; // The size of each tile in pixels.
    private static final int CENTER_X = 400; // The X-coordinate of the center of the game window.
    private static final int CENTER_Y = 300; // The Y-coordinate of the center of the game window.
    private static final int BOARD_WIDTH = 10; // The width of the game board in cells.
    private static final int BOARD_HEIGHT = 20; // The height of the game board in cells.
    private Board board = new Board(BOARD_WIDTH, BOARD_HEIGHT, TILE_SIZE, 800, 600); // The game board instance for managing the grid.
    private TetrinomeController controller; // The controller for managing Tetrinomes and handling key events.

    /**
     * Constructs a new Tetris game instance.
     * Initializes the game board and controller, and sets up key listeners.
     */
    public Tetris() {
        super("Tetris!", 800, 600);
        this.setFocusable(true);
        this.requestFocus();

        // Initialize the board and controller
        this.board = new Board(10, 20, 30, 800, 600);
        this.controller = new TetrinomeController(this.board);

        // Add the controller as a key listener
        this.addKeyListener(controller);
    }

    /**
     * Inner class for managing Tetrinomes and handling key events.
     * Implements the KeyListener and GameTimer interfaces.
     */
    public class TetrinomeController implements KeyListener, GameTimer {
        private List<Tetrinome> tetrinomeBag; // Bag of Tetrinomes for random generation.
        private Tetrinome activeTetrinome; // The currently active Tetrinome.
        private Board board; // The game board instance for managing the grid.
        private TimerHandler timerHandler; // Inner class instance for managing the timer.
        private int dropInterval = 1000; // Interval for dropping Tetrinomes (in milliseconds).

        /**
         * Constructs a new TetrinomeController instance.
         *
         * @param board The game board to manage.
         */
        public TetrinomeController(Board board) {
            this.tetrinomeBag = new ArrayList<>();
            this.activeTetrinome = null;
            this.board = board;
            refillBag(); // Fill the bag with Tetrinomes
            spawnNextTetrinome(); // Spawn the first Tetrinome

            // Initialize the timer handler
            this.timerHandler = new TimerHandler();
            this.timerHandler.startTimer();
        }

        /**
         * Refills the bag with one of each Tetrinome type and shuffles it.
         */
        private void refillBag() {
            this.tetrinomeBag.clear();
            for (TetrinomeType type : TetrinomeType.values()) {
                this.tetrinomeBag.add(new Tetrinome(type, new Point(12 * TILE_SIZE, 0), board));
            }
            Collections.shuffle(tetrinomeBag); // Shuffle the bag for randomness
        }

        /**
         * Spawns the next Tetrinome from the bag.
         * Refills the bag if it is empty.
         */
        private void spawnNextTetrinome() {
            if (this.tetrinomeBag.isEmpty()) {
                refillBag(); // Refill the bag if empty
            }
            activeTetrinome = tetrinomeBag.remove(0); // Get the next Tetrinome
            activeTetrinome.setPlaced(false); // Ensure it's not marked as placed
        }

        /**
         * Handles key press events to move or rotate the active Tetrinome.
         *
         * @param e The KeyEvent triggered by the key press.
         */
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
                this.board.addTetrinomeToGrid(activeTetrinome); // Add the Tetrinome to the grid
                spawnNextTetrinome(); // Spawn a new Tetrinome
            }
        }

        /**
         * Handles key release events (not used).
         *
         * @param e The KeyEvent triggered by the key release.
         */
        @Override
        public void keyReleased(KeyEvent e) {}

        /**
         * Handles key typed events (not used).
         *
         * @param e The KeyEvent triggered by the key typed.
         */
        @Override
        public void keyTyped(KeyEvent e) {}

        /**
         * Gets the currently active Tetrinome.
         *
         * @return The active Tetrinome.
         */
        public Tetrinome getActiveTetrinome() {
            return this.activeTetrinome;
        }

        // GameTimer interface methods

        /**
         * Starts the game timer.
         */
        @Override
        public void startTimer() {
            this.timerHandler.startTimer();
        }

        /**
         * Stops the game timer.
         */
        @Override
        public void stopTimer() {
            this.timerHandler.stopTimer();
        }

        /**
         * Updates the timer interval.
         *
         * @param newInterval The new interval in milliseconds.
         */
        @Override
        public void updateInterval(int newInterval) {
            this.timerHandler.updateInterval(newInterval);
        }

        /**
         * Inner class for managing the timer logic.
         */
        private class TimerHandler {
            /** Timer for managing the automatic downward movement of Tetrinomes. */
            private Timer timer;

            /**
             * Constructs a new TimerHandler instance.
             */
            public TimerHandler() {
                this.timer = new Timer(dropInterval, e -> {
                    if (activeTetrinome != null && !activeTetrinome.isPlaced()) {
                        activeTetrinome.move(false, false, true, false); // Move the Tetrinome down
                    }

                    // Check if the active Tetrinome is placed
                    if (activeTetrinome != null && activeTetrinome.isPlaced()) {
                        board.addTetrinomeToGrid(activeTetrinome); // Add the Tetrinome to the grid
                        spawnNextTetrinome(); // Spawn a new Tetrinome

                        // Gradually speed up the game
                        if (dropInterval > 200) { // Set a minimum interval limit
                            dropInterval -= 50; // Decrease the interval
                            updateInterval(dropInterval); // Update the timer interval
                        }
                    }
                });
            }

            /**
             * Starts the timer.
             */
            public void startTimer() {
                if (!timer.isRunning()) {
                    timer.start();
                }
            }

            /**
             * Stops the timer.
             */
            public void stopTimer() {
                if (timer.isRunning()) {
                    timer.stop();
                }
            }

            /**
             * Updates the timer interval.
             *
             * @param newInterval The new interval in milliseconds.
             */
            public void updateInterval(int newInterval) {
                timer.setDelay(newInterval); // Update the timer delay
            }
        }
    }

    /**
     * Paints the game screen, including the board and the active Tetrinome.
     *
     * @param brush The Graphics object used for rendering.
     */
    public void paint(Graphics brush) {
        brush.setColor(Color.black);
        brush.fillRect(0, 0, width, height);

        // Draw the board and active Tetrinome
        board.draw(brush);
        Tetrinome activeTetrinome = controller.getActiveTetrinome();
        if (activeTetrinome != null) {
            activeTetrinome.paint(brush);
        }

        // Debugging message
        counter++;
        brush.setColor(Color.white);
        brush.drawString("Counter is " + counter, 10, 10);
    }

    /**
     * The main method to start the Tetris game.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        Tetris a = new Tetris();
        a.repaint();
    }
}