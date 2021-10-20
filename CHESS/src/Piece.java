import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Piece {

    private String type;
    private boolean isWhite;
    private Image pieceImage;
    private int pieceValue;

    private boolean hasMoved;
    private boolean kingInCheck;

    public Piece(String type, boolean isWhite, String imagePath, int pieceValue) throws IOException {
        this.type = type;
        this.isWhite = isWhite;
        setPieceImage(imagePath);
        kingInCheck = false;
        this.pieceValue = pieceValue;
    }

    public int getPieceValue() {
        return pieceValue;
    }

    public boolean getKingInCheck() {
        return kingInCheck;
    }

    public void setKingInCheck(boolean set) {
        kingInCheck = set;
    }

    public boolean hasMoved() {
        return hasMoved;
    }

    public void setHasMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;
    }

    public char getColorChar() {
        if(isWhite)
            return 'W';
        else
            return 'B';
    }

    public char getMovedChar() {
        if(hasMoved)
            return 'T';
        else
            return 'F';
    }

    public void setPieceImage(String imagePath) throws IOException {
        pieceImage = ImageIO.read(new File(imagePath));
    }

    public Image getPieceImage() {
        return pieceImage;
    }

    public void setType(String set) {
        type = set;
    }

    public String getType() {
        return type;
    }

    public boolean isWhite() {
        return isWhite;
    }

    public void setWhite(boolean white) {
        isWhite = white;
    }

}
