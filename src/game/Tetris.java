
package game;

/*
CLASS: YourGameNameoids
DESCRIPTION: Extending Game, YourGameName is all in the paint method.
NOTE: This class is the metaphorical "main method" of your program,
      it is your control center.

*/
import java.awt.*;
import java.awt.event.*;
import java.awt.image.TileObserver;

import game.Board.TetrinomeController;

class Tetris extends Game {
    static int counter = 0;
    private Tetrinome[] tetrinomes;
    private static final int TILE_SIZE = 30;
	private static final int CENTER_X = 400;
	private static final int CENTER_Y = 300;
	private static final int BOARD_WIDTH = 10;
	private static final int BOARD_HEIGHT = 20;
	private Board board = new Board(BOARD_WIDTH, BOARD_HEIGHT, TILE_SIZE, 800, 600, this);
	private GameOver gameOver;
	private TetrinomeController controller;
	
	private Tetrinome o = new Tetrinome(TetrinomeType.O, new Point(11 * TILE_SIZE, 0), board);
	private Tetrinome i = new Tetrinome(TetrinomeType.I, new Point(13 * TILE_SIZE, 0), board);
	private Tetrinome t = new Tetrinome(TetrinomeType.T, new Point(8 * TILE_SIZE, 4 * TILE_SIZE), board);
	private Tetrinome s = new Tetrinome(TetrinomeType.S, new Point(10 * TILE_SIZE, 6 * TILE_SIZE), board);
	private Tetrinome z = new Tetrinome(TetrinomeType.Z, new Point(8 * TILE_SIZE, 8 * TILE_SIZE), board);
	private Tetrinome l = new Tetrinome(TetrinomeType.L, new Point(10 * TILE_SIZE, 10 * TILE_SIZE), board);
	private Tetrinome j = new Tetrinome(TetrinomeType.J, new Point(12 * TILE_SIZE, 12 * TILE_SIZE), board);
    public Tetris() {
        super("Tetris!", 800, 600);
        this.setFocusable(true);
        this.requestFocus();

        // Initialize the board and controller
        board = new Board(10, 20, 30, 800, 600, this);
        controller = board.getController();

        // Add the controller as a key listener
        this.addKeyListener(controller);
    }
    public void paint(Graphics brush) {
        brush.setColor(Color.black);
        brush.fillRect(0, 0, width, height);

        // Handle Game Over
        if (gameOver.isActive()) {
            // Render "Game Over" text directly in the paint method
            brush.setColor(Color.red);
            brush.setFont(new Font("Arial", Font.BOLD, 50));
            brush.drawString("GAME OVER", CENTER_X - 150, CENTER_Y);
            brush.setFont(new Font("Arial", Font.PLAIN, 20));
            brush.drawString("Press R to Restart or Q to Quit", CENTER_X - 150, CENTER_Y + 50);
            return; // Stop painting the game
        }

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

    // Inner GameOver class
    public class GameOver {
        private boolean active = false; // Tracks game-over state

        // Sets the game-over state to active
        public void trigger() {
            active = true;
        }

        // Checks whether the game is over
        // @return True if the game is over, false otherwise
        public boolean isActive() {
            return active;
        }

        //Resets the game-over state
        public void reset() {
            active = false;
        }
    }

    // Provides access to the GameOver inner class
    // @return The GameOver instance
    public GameOver getGameOver() {
        return gameOver;
    }
    public static void main(String[] args) {
        Tetris a = new Tetris();
        a.repaint();
    }
}
