package game;

public class Tetrinome {
    private Polygon[] squares;
    private TetrinomeType type;
    private static final int TILE_SIZE = 30;

    public Tetrinome(Polygon[] squares, TetrinomeType type) {
        this.squares = squares;
        this.type = type;
    }

    public static Tetrinome createTetrinome(TetrinomeType type) {
        Polygon[] squares;
        switch (type) {
            case I:
                squares = new Polygon[]{
                    new Polygon(new Point[]{new Point(0, 0), new Point(TILE_SIZE, 0), new Point(TILE_SIZE, TILE_SIZE), new Point(0, TILE_SIZE)}, new Point(0, 0), 0),
                    new Polygon(new Point[]{new Point(TILE_SIZE, 0), new Point(2 * TILE_SIZE, 0), new Point(2 * TILE_SIZE, TILE_SIZE), new Point(TILE_SIZE, TILE_SIZE)}, new Point(0, 0), 0),
                    new Polygon(new Point[]{new Point(2 * TILE_SIZE, 0), new Point(3 * TILE_SIZE, 0), new Point(3 * TILE_SIZE, TILE_SIZE), new Point(2 * TILE_SIZE, TILE_SIZE)}, new Point(0, 0), 0),
                    new Polygon(new Point[]{new Point(3 * TILE_SIZE, 0), new Point(4 * TILE_SIZE, 0), new Point(4 * TILE_SIZE, TILE_SIZE), new Point(3 * TILE_SIZE, TILE_SIZE)}, new Point(0, 0), 0)
                };
                break;
            case O:
                squares = new Polygon[]{
                    new Polygon(new Point[]{new Point(0, 0), new Point(TILE_SIZE, 0), new Point(TILE_SIZE, TILE_SIZE), new Point(0, TILE_SIZE)}, new Point(0, 0), 0),
                    new Polygon(new Point[]{new Point(TILE_SIZE, 0), new Point(2 * TILE_SIZE, 0), new Point(2 * TILE_SIZE, TILE_SIZE), new Point(TILE_SIZE, TILE_SIZE)}, new Point(0, 0), 0),
                    new Polygon(new Point[]{new Point(0, TILE_SIZE), new Point(TILE_SIZE, TILE_SIZE), new Point(TILE_SIZE, 2 * TILE_SIZE), new Point(0, 2 * TILE_SIZE)}, new Point(0, 0), 0),
                    new Polygon(new Point[]{new Point(TILE_SIZE, TILE_SIZE), new Point(2 * TILE_SIZE, TILE_SIZE), new Point(2 * TILE_SIZE, 2 * TILE_SIZE), new Point(TILE_SIZE, 2 * TILE_SIZE)}, new Point(0, 0), 0)
                };
                break;
            case T:
                squares = new Polygon[]{
                    new Polygon(new Point[]{new Point(0, 0), new Point(TILE_SIZE, 0), new Point(TILE_SIZE, TILE_SIZE), new Point(0, TILE_SIZE)}, new Point(0, 0), 0),
                    new Polygon(new Point[]{new Point(TILE_SIZE, 0), new Point(2 * TILE_SIZE, 0), new Point(2 * TILE_SIZE, TILE_SIZE), new Point(TILE_SIZE, TILE_SIZE)}, new Point(0, 0), 0),
                    new Polygon(new Point[]{new Point(2 * TILE_SIZE, 0), new Point(3 * TILE_SIZE, 0), new Point(3 * TILE_SIZE, TILE_SIZE), new Point(2 * TILE_SIZE, TILE_SIZE)}, new Point(0, 0), 0),
                    new Polygon(new Point[]{new Point(TILE_SIZE, TILE_SIZE), new Point(2 * TILE_SIZE, TILE_SIZE), new Point(2 * TILE_SIZE, 2 * TILE_SIZE), new Point(TILE_SIZE, 2 * TILE_SIZE)}, new Point(0, 0), 0)
                };
                break;
            case S:
                squares = new Polygon[]{
                    new Polygon(new Point[]{new Point(TILE_SIZE, 0), new Point(2 * TILE_SIZE, 0), new Point(2 * TILE_SIZE, TILE_SIZE), new Point(TILE_SIZE, TILE_SIZE)}, new Point(0, 0), 0),
                    new Polygon(new Point[]{new Point(2 * TILE_SIZE, 0), new Point(3 * TILE_SIZE, 0), new Point(3 * TILE_SIZE, TILE_SIZE), new Point(2 * TILE_SIZE, TILE_SIZE)}, new Point(0, 0), 0),
                    new Polygon(new Point[]{new Point(0, TILE_SIZE), new Point(TILE_SIZE, TILE_SIZE), new Point(TILE_SIZE, 2 * TILE_SIZE), new Point(0, 2 * TILE_SIZE)}, new Point(0, 0), 0),
                    new Polygon(new Point[]{new Point(TILE_SIZE, TILE_SIZE), new Point(2 * TILE_SIZE, TILE_SIZE), new Point(2 * TILE_SIZE, 2 * TILE_SIZE), new Point(TILE_SIZE, 2 * TILE_SIZE)}, new Point(0, 0), 0)
                };
                break;
            case Z:
                squares = new Polygon[]{
                    new Polygon(new Point[]{new Point(0, 0), new Point(TILE_SIZE, 0), new Point(TILE_SIZE, TILE_SIZE), new Point(0, TILE_SIZE)}, new Point(0, 0), 0),
                    new Polygon(new Point[]{new Point(TILE_SIZE, 0), new Point(2 * TILE_SIZE, 0), new Point(2 * TILE_SIZE, TILE_SIZE), new Point(TILE_SIZE, TILE_SIZE)}, new Point(0, 0), 0),
                    new Polygon(new Point[]{new Point(TILE_SIZE, TILE_SIZE), new Point(2 * TILE_SIZE, TILE_SIZE), new Point(2 * TILE_SIZE, 2 * TILE_SIZE), new Point(TILE_SIZE, 2 * TILE_SIZE)}, new Point(0, 0), 0),
                    new Polygon(new Point[]{new Point(2 * TILE_SIZE, TILE_SIZE), new Point(3 * TILE_SIZE, TILE_SIZE), new Point(3 * TILE_SIZE, 2 * TILE_SIZE), new Point(2 * TILE_SIZE, 2 * TILE_SIZE)}, new Point(0, 0), 0)
                };
                break;
            case J:
                squares = new Polygon[]{
                    new Polygon(new Point[]{new Point(0, 0), new Point(TILE_SIZE, 0), new Point(TILE_SIZE, TILE_SIZE), new Point(0, TILE_SIZE)}, new Point(0, 0), 0),
                    new Polygon(new Point[]{new Point(0, TILE_SIZE), new Point(TILE_SIZE, TILE_SIZE), new Point(TILE_SIZE, 2 * TILE_SIZE), new Point(0, 2 * TILE_SIZE)}, new Point(0, 0), 0),
                    new Polygon(new Point[]{new Point(TILE_SIZE, TILE_SIZE), new Point(2 * TILE_SIZE, TILE_SIZE), new Point(2 * TILE_SIZE, 2 * TILE_SIZE), new Point(TILE_SIZE, 2 * TILE_SIZE)}, new Point(0, 0), 0),
                    new Polygon(new Point[]{new Point(2 * TILE_SIZE, TILE_SIZE), new Point(3 * TILE_SIZE, TILE_SIZE), new Point(3 * TILE_SIZE, 2 * TILE_SIZE), new Point(2 * TILE_SIZE, 2 * TILE_SIZE)}, new Point(0, 0), 0)
                };
                break;
            case L:
                squares = new Polygon[]{
                    new Polygon(new Point[]{new Point(2 * TILE_SIZE, 0), new Point(3 * TILE_SIZE, 0), new Point(3 * TILE_SIZE, TILE_SIZE), new Point(2 * TILE_SIZE, TILE_SIZE)}, new Point(0, 0), 0),
                    new Polygon(new Point[]{new Point(0, TILE_SIZE), new Point(TILE_SIZE, TILE_SIZE), new Point(TILE_SIZE, 2 * TILE_SIZE), new Point(0, 2 * TILE_SIZE)}, new Point(0, 0), 0),
                    new Polygon(new Point[]{new Point(TILE_SIZE, TILE_SIZE), new Point(2 * TILE_SIZE, TILE_SIZE), new Point(2 * TILE_SIZE, 2 * TILE_SIZE), new Point(TILE_SIZE, 2 * TILE_SIZE)}, new Point(0, 0), 0),
                    new Polygon(new Point[]{new Point(2 * TILE_SIZE, TILE_SIZE), new Point(3 * TILE_SIZE, TILE_SIZE), new Point(3 * TILE_SIZE, 2 * TILE_SIZE), new Point(2 * TILE_SIZE, 2 * TILE_SIZE)}, new Point(0, 0), 0)
                };
                break;
            default:
                throw new IllegalArgumentException("Unknown Tetrinome type: " + type);
        }
        return new Tetrinome(squares, type);
    }

    public Polygon[] getSquares() {
        return squares;
    }

    public TetrinomeType getType() {
        return type;
    }
}
