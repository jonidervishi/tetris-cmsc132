package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


/**
 * Represents a Tetrinome (Tetris block) in the game.
 * Handles the Tetrinome's shape, movement, rotation, and rendering.
 */
public class Tetrinome extends Polygon implements KeyListener{
    private Point[] points; // Points defining the shape of the Tetrinome
    private TetrinomeType type; // The type of the Tetrinome (I, O, T, etc.)
    private static final int TILE_SIZE = 30; // Size of each tile in pixels
    private Board board; // The game board the Tetrinome belongs to
    private boolean placed; // Flag to indicate if the Tetrinome is placed

    /**
     * Constructs a new Tetrinome instance.
     *
     * @param type     The type of the Tetrinome.
     * @param position The initial position of the Tetrinome.
     * @param board    The game board the Tetrinome belongs to.
     */
    public Tetrinome(TetrinomeType type, Point position, Board board) {
        super(getPointsForType(type), position, 0);
        this.points = getPointsForType(type);
        this.type = type;
        this.board = board;
        this.placed = false; // Initialize as not placed
        // this.position = position;
    }

    /**
     * Gets the points defining the shape of the Tetrinome based on its type.
     *
     * @param type The type of the Tetrinome.
     * @return An array of Points defining the Tetrinome's shape.
     */
    private static Point[] getPointsForType(TetrinomeType type) {
        Point[] points = new Point[]{new Point(0, 0), new Point(0, 1), new Point(1, 1), new Point(1, 0)};
        switch (type) {
            case I:
                points = new Point[]{new Point(0, 0), new Point(0, 1 * TILE_SIZE), new Point(0, 2 * TILE_SIZE), new Point(0, 3 * TILE_SIZE), new Point(0, 4 * TILE_SIZE), new Point(1 * TILE_SIZE, 4 * TILE_SIZE), new Point(1 * TILE_SIZE, 3 * TILE_SIZE), new Point(1 * TILE_SIZE, 2 * TILE_SIZE), new Point(1 * TILE_SIZE, 1 * TILE_SIZE), new Point(1 * TILE_SIZE, 0)};
                break;
            case O:
                points = new Point[]{new Point(0, 0), new Point(0, 1 * TILE_SIZE), new Point(0, 2 * TILE_SIZE), new Point(1 * TILE_SIZE, 2 * TILE_SIZE), new Point(2 * TILE_SIZE, 2 * TILE_SIZE), new Point(2 * TILE_SIZE, 1 * TILE_SIZE), new Point(2 * TILE_SIZE, 0), new Point(1 * TILE_SIZE, 0)};
                break;
            case T:
                points = new Point[]{new Point(0, 0), new Point(1 * TILE_SIZE, 0), new Point(2 * TILE_SIZE, 0), new Point(3 * TILE_SIZE, 0), new Point(3 * TILE_SIZE, 1 * TILE_SIZE), new Point(2 * TILE_SIZE, 1 * TILE_SIZE), new Point(2 * TILE_SIZE, 2 * TILE_SIZE), new Point(1 * TILE_SIZE, 2 * TILE_SIZE), new Point(1 * TILE_SIZE, 1 * TILE_SIZE), new Point(0, 1 * TILE_SIZE)};
                break;
            case S:
                points = new Point[]{new Point(1 * TILE_SIZE, 0), new Point(2 * TILE_SIZE, 0), new Point(3 * TILE_SIZE, 0), new Point(3 * TILE_SIZE, 1 * TILE_SIZE), new Point(2 * TILE_SIZE, 1 * TILE_SIZE), new Point(2 * TILE_SIZE, 2 * TILE_SIZE), new Point(1 * TILE_SIZE, 2 * TILE_SIZE), new Point(0, 2 * TILE_SIZE), new Point(0, 1 * TILE_SIZE), new Point(1 * TILE_SIZE, 1 * TILE_SIZE)};
                break;
            case Z:
                points = new Point[]{new Point(0, 0), new Point(1 * TILE_SIZE, 0), new Point(2 * TILE_SIZE, 0), new Point(2 * TILE_SIZE, 1 * TILE_SIZE), new Point(3 * TILE_SIZE, 1 * TILE_SIZE), new Point(3 * TILE_SIZE, 2 * TILE_SIZE), new Point(2 * TILE_SIZE, 2 * TILE_SIZE), new Point(1 * TILE_SIZE, 2 * TILE_SIZE), new Point(1 * TILE_SIZE, 1 * TILE_SIZE), new Point(0, 1 * TILE_SIZE)};
                break;
            case J:
                points = new Point[]{new Point(1 * TILE_SIZE, 0), new Point(2 * TILE_SIZE, 0), new Point(2 * TILE_SIZE, 1 * TILE_SIZE), new Point(2 * TILE_SIZE, 2 * TILE_SIZE), new Point(2 * TILE_SIZE, 3 * TILE_SIZE), new Point(1 * TILE_SIZE, 3 * TILE_SIZE), new Point(0, 3 * TILE_SIZE), new Point(0, 2 * TILE_SIZE), new Point(1 * TILE_SIZE, 2 * TILE_SIZE), new Point(1 * TILE_SIZE, 1 * TILE_SIZE)};
                break;
            case L:
                points = new Point[]{new Point(0, 0), new Point(0, 1 * TILE_SIZE), new Point(0, 2 * TILE_SIZE), new Point(0, 3 * TILE_SIZE), new Point(1 * TILE_SIZE, 3 * TILE_SIZE), new Point(2 * TILE_SIZE, 3 * TILE_SIZE), new Point(2 * TILE_SIZE, 2 * TILE_SIZE), new Point(1 * TILE_SIZE, 2 * TILE_SIZE), new Point(1 * TILE_SIZE, 1 * TILE_SIZE), new Point(1 * TILE_SIZE, 0)};
                break;
            default:
                throw new IllegalArgumentException("Unknown Tetrinome type: " + type);
        }
        return points;
    }

    /**
     * Gets the transformed points of the Tetrinome after applying rotation and translation.
     *
     * @return An array of transformed Points.
     */
    @Override
    public Point[] getPoints() {
        Point[] transformedPoints = new Point[points.length];
        double angle = Math.toRadians(rotation);
        double sin = Math.sin(angle);
        double cos = Math.cos(angle);

        // Calculate the center of the tetrinome
        Point center = findCenter();

        for (int i = 0; i < points.length; i++) {
            int x = (int)(points[i].x - center.x);
            int y = (int)(points[i].y - center.y);

            // Apply rotation around the center
            int rotatedX = (int) Math.round(x * cos - y * sin);
            int rotatedY = (int) Math.round(x * sin + y * cos);

            // Apply translation
            int translatedX = (int)(rotatedX + center.x + position.x);
            int translatedY = (int)(rotatedY + center.y + position.y);

            // Snap to grid
            int snappedX = (int) Math.round((double) translatedX / TILE_SIZE) * TILE_SIZE;
            int snappedY = (int) Math.round((double) translatedY / TILE_SIZE) * TILE_SIZE;

            transformedPoints[i] = new Point(snappedX, snappedY);
        }

        return transformedPoints;
    }

    /**
     * Finds the center point of the Tetrinome.
     *
     * @return The center Point of the Tetrinome.
     */
    private Point findCenter() {
        int sumX = 0;
        int sumY = 0;
        for (Point point : points) {
            sumX += point.x;
            sumY += point.y;
        }
        int centerX = sumX / points.length;
        int centerY = sumY / points.length;
        return new Point(centerX, centerY);
    }

    /**
     * Paints the Tetrinome on the screen.
     *
     * @param brush The Graphics object used for rendering.
     */
    public void paint(Graphics brush) {
        if (placed) {
            return; // Do not draw if the Tetrinome is placed
        }

        brush.setColor(this.getColor());

        Point[] transformedPoints = this.getPoints();
        int[] xCoords = new int[transformedPoints.length];
        int[] yCoords = new int[transformedPoints.length];
        for (int i = 0; i < transformedPoints.length; i++) {
            xCoords[i] = (int) transformedPoints[i].x;
            yCoords[i] = (int) transformedPoints[i].y;
        }
        brush.fillPolygon(xCoords, yCoords, transformedPoints.length);
    }

    /**
     * Moves the Tetrinome in the specified direction or rotates it.
     *
     * @param left   Whether to move left.
     * @param right  Whether to move right.
     * @param down   Whether to move down.
     * @param rotate Whether to rotate the Tetrinome.
     */
    public void move(boolean left, boolean right, boolean down, boolean rotate) {
        if (placed) {
            return; // Do not move if the Tetrinome is placed
        }

        if (left) {
            this.position.x -= TILE_SIZE;
            if (board.isCollision(this)) {
                this.position.x += TILE_SIZE; // Undo move if collision detected
            }
        }
        if (right) {
            this.position.x += TILE_SIZE;
            if (board.isCollision(this)) {
                this.position.x -= TILE_SIZE; // Undo move if collision detected
            }
        }
        if (down) {
            this.position.y += TILE_SIZE;
            if (board.isCollision(this)) {
                this.position.y -= TILE_SIZE; // Undo move if collision detected
                board.placeTetrinome(this); // Place the Tetrinome if it touches the bottom
            }
        }
        if (rotate) {
            this.rotation += 90;
            if (board.isCollision(this)) {
                this.rotation -= 90; // Undo rotation if collision detected
            }
        }
    }

    /**
     * Gets the rotation angle of the Tetrinome.
     *
     * @return The rotation angle in degrees.
     */
    public double getRotation(){
        return this.rotation;
    }

    /**
     * Gets the type of the Tetrinome.
     *
     * @return The TetrinomeType of the Tetrinome.
     */
    public TetrinomeType getType() {
        return this.type;
    }

    /**
     * Gets the origin point of the Tetrinome based on its type.
     *
     * @param type The type of the Tetrinome.
     * @return The origin Point of the Tetrinome.
     */
    public static Point getOrigin(TetrinomeType type) {
        switch (type) {
            case I, O, T, Z, L:
                return new Point(0, 0);
            case S, J:
                return new Point(1 * TILE_SIZE, 0);
            default:
                throw new IllegalArgumentException("Unknown Tetrinome type: " + type);
        }
    }

    /**
     * Gets the color of the Tetrinome based on its type.
     *
     * @return The Color of the Tetrinome.
     */
    public Color getColor() {
        switch (this.type) {
            case I:
                return Color.CYAN;
            case O:
                return Color.YELLOW;
            case T:
                return Color.MAGENTA;
            case S:
                return Color.GREEN;
            case Z:
                return Color.RED;
            case J:
                return Color.BLUE;
            case L:
                return Color.ORANGE;
            default:
                throw new IllegalArgumentException("Unknown Tetrinome type: " + this.type);
        }
    }

    /**
     * Handles key press events to move or rotate the Tetrinome.
     *
     * @param e The KeyEvent triggered by the key press.
     */
    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                move(true, false, false, false);
                break;
            case KeyEvent.VK_RIGHT:
                move(false, true, false, false);
                break;
            case KeyEvent.VK_DOWN:
                move(false, false, true, false);
                break;
            case KeyEvent.VK_UP:
                move(false, false, false, true);
                break;
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
     * Sets whether the Tetrinome is placed on the board.
     *
     * @param placed True if the Tetrinome is placed, false otherwise.
     */
    public void setPlaced(boolean placed) {
        this.placed = placed;
    }

    /**
     * Checks if the Tetrinome is placed on the board.
     *
     * @return True if the Tetrinome is placed, false otherwise.
     */
    public boolean isPlaced() {
        return placed;
    }
}
