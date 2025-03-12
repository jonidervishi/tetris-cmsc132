package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Tetrinome extends Polygon implements KeyListener{
    private Point[] points;
    private TetrinomeType type;
    private static final int TILE_SIZE = 30;
    private Board board;
    private boolean placed; // Flag to indicate if the Tetrinome is placed

    public Tetrinome(TetrinomeType type, Point position, Board board) {
        super(getPointsForType(type), position, 0);
        this.points = getPointsForType(type);
        this.type = type;
        this.board = board;
        this.placed = false; // Initialize as not placed
    }

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

    public double getRotation(){
        return this.rotation;
    }

    public TetrinomeType getType() {
        return this.type;
    }

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

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}
    
    public void setPlaced(boolean placed) {
        this.placed = placed;
    }
    public boolean isPlaced() {
        return placed;
    }
}
