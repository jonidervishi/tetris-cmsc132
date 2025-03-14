package game;

import java.awt.Color;
import java.awt.Graphics;

public class Tetrinome extends Polygon {
    private Point[] points;
    private TetrinomeType type;
    private static final int TILE_SIZE = 30;
    private Board board;
    private boolean placed; // Flag to indicate if the Tetrinome is placed

    // Constructor
    public Tetrinome(TetrinomeType type, Point position, Board board) {
        super(getPointsForType(type), position, 0);
        this.points = getPointsForType(type);
        this.type = type;
        this.board = board;
        this.placed = false; // Initialize as not placed
    }

    // Determines the points based on the Tetrinome type
    private static Point[] getPointsForType(TetrinomeType type) {
        switch (type) {
            case I:
                return new Point[]{new Point(0, 0), new Point(0, 1 * TILE_SIZE), new Point(0, 2 * TILE_SIZE), new Point(0, 3 * TILE_SIZE)};
            case O:
                return new Point[]{new Point(0, 0), new Point(0, 1 * TILE_SIZE), new Point(1 * TILE_SIZE, 1 * TILE_SIZE), new Point(1 * TILE_SIZE, 0)};
            case T:
                return new Point[]{new Point(0, 0), new Point(-1 * TILE_SIZE, 0), new Point(1 * TILE_SIZE, 0), new Point(0, -1 * TILE_SIZE)};
            case S:
                return new Point[]{new Point(0, 0), new Point(0, -1 * TILE_SIZE), new Point(-1 * TILE_SIZE, 0), new Point(-1 * TILE_SIZE, 1 * TILE_SIZE)};
            case Z:
                return new Point[]{new Point(0, 0), new Point(0, -1 * TILE_SIZE), new Point(1 * TILE_SIZE, 0), new Point(1 * TILE_SIZE, 1 * TILE_SIZE)};
            case J:
                return new Point[]{new Point(0, 0), new Point(0, -1 * TILE_SIZE), new Point(0, -2 * TILE_SIZE), new Point(-1 * TILE_SIZE, -2 * TILE_SIZE)};
            case L:
                return new Point[]{new Point(0, 0), new Point(0, -1 * TILE_SIZE), new Point(0, -2 * TILE_SIZE), new Point(1 * TILE_SIZE, -2 * TILE_SIZE)};
            default:
                return new Point[]{};
        }
    }

    // Returns the transformed points after applying rotation and position
    @Override
    public Point[] getPoints() {
        Point[] transformedPoints = new Point[points.length];
        double angle = Math.toRadians(rotation);
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        for (int i = 0; i < points.length; i++) {
            double newX = points[i].x * cos - points[i].y * sin + position.x;
            double newY = points[i].x * sin + points[i].y * cos + position.y;
            transformedPoints[i] = new Point(newX, newY);
        }
        return transformedPoints;
    }

    // Paints the Tetrinome
    public void paint(Graphics brush) {
        if (placed) return;

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

    // New move method using Behavior interface and lambda expressions
    public void move(Behavior behavior) {
        behavior.apply(this); // Apply the lambda-defined behavior to this Tetrinome
    }

    // Getter for Tetrinome type
    public TetrinomeType getType() {
        return type;
    }

    // Getter and Setter for the placed flag
    public boolean isPlaced() {
        return placed;
    }

    public void setPlaced(boolean placed) {
        this.placed = placed;
    }

    // Gets the color of the Tetrinome
    public Color getColor() {
        switch (this.type) {
            case I: return Color.CYAN;
            case O: return Color.YELLOW;
            case T: return Color.MAGENTA;
            case S: return Color.GREEN;
            case Z: return Color.RED;
            case J: return Color.BLUE;
            case L: return Color.ORANGE;
            default: return Color.WHITE;
        }
    }
}
