import java.util.ArrayList;

public class Tile {

    private Piece piece;
    private int r;
    private int c;
    private boolean isMovable;
    private boolean isCastleTile;

    private int locationX;
    private int locationY;

    public Tile(Piece piece, int r, int c) {
        this.piece = piece;
        setRow(r);
        setColumn(c);
        isMovable = false;

        locationX = r*100;
        locationY = c*100;
    }

    public boolean isCastleTile() {
        return isCastleTile;
    }

    public void setCastleTile(boolean set) {
        isCastleTile = set;
    }

    public int getLocationX() {
        return locationX;
    }

    public int getLocationY() {
        return locationY;
    }

    public boolean isMovable() {
        return isMovable;
    }

    public void setMovable(boolean movable) {
        isMovable = movable;
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece set) {
        piece = set;
    }

    public int getRow() {
        return r;
    }

    public void setRow(int r) {
        this.r = r;
    }

    public int getColumn() {
        return c;
    }

    public void setColumn(int c) {
        this.c = c;
    }

    ////////

}
