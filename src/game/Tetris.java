package game;

/*
CLASS: YourGameNameoids
DESCRIPTION: Extending Game, YourGameName is all in the paint method.
NOTE: This class is the metaphorical "main method" of your program,
      it is your control center.

*/
import java.awt.*;
import java.awt.event.*;

class Tetris extends Game {
    static int counter = 0;
    private Board board;
    private Tetrinome[] tetrinomes;
    private static final int TILE_SIZE = 30;

    public Tetris() {
        super("Tetris!", 800, 600);
        this.setFocusable(true);
        this.requestFocus();
        board = new Board(10, 20, 30, 800, 600);
        initializeTetrinomes();
    }

    private void initializeTetrinomes() {
        tetrinomes = new Tetrinome[3];
        tetrinomes[0] = Tetrinome.createTetrinome(TetrinomeType.I);
        tetrinomes[0].getSquares()[0].position = new Point(4 * TILE_SIZE, 10 * TILE_SIZE); // Position in the middle of the board
        tetrinomes[1] = Tetrinome.createTetrinome(TetrinomeType.O);
        tetrinomes[1].getSquares()[0].position = new Point(4 * TILE_SIZE, 5 * TILE_SIZE); // Position in the middle of the board
        tetrinomes[2] = Tetrinome.createTetrinome(TetrinomeType.T);
        tetrinomes[2].getSquares()[0].position = new Point(9.5 * TILE_SIZE, 8 * TILE_SIZE); // Position in the middle of the board
    }

    private void drawTetrinome(Graphics brush, Tetrinome tetrinome) {
        Polygon[] squares = tetrinome.getSquares();
        brush.setColor(Color.red);
        for (Polygon square : squares) {
            Point position = square.position;
            brush.fillRect((int) position.x, (int) position.y, TILE_SIZE, TILE_SIZE);
        }
    }

    public void paint(Graphics brush) {
        brush.setColor(Color.black);
        brush.fillRect(0, 0, width, height);

        // Draw the board
        board.draw(brush);

        // Draw the tetrinomes
        // for (Tetrinome tetrinome : tetrinomes) {
        //     drawTetrinome(brush, tetrinome);
        // }

        // sample code for printing message for debugging
        // counter is incremented and this message printed
        // each time the canvas is repainted
        counter++;
        brush.setColor(Color.white);
        brush.drawString("Counter is " + counter, 10, 10);
    }

    public static void main(String[] args) {
        Tetris a = new Tetris();
        a.repaint();
    }
}