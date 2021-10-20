import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;

public class ChessPanel extends JPanel implements MouseListener, ActionListener {

    private Point[][] points = new Point[8][8];
    private String lastMove;
    private Board board;

    private ArrayList<Image> whitePieces;
    private ArrayList<Image> blackPieces;
    private int whiteScore;
    private int blackScore;

    private boolean checkmate;

    public ChessPanel() throws IOException {
        addMouseListener(this);
        setSize(800, 800);
        checkmate = false;
        board = new Board();
        lastMove = "";

        for(int r = 0; r < 8; r++) {
            for(int c = 0; c < 8; c++) {
                points[r][c] = new Point(r*100, c*100);
            }
        }
        whitePieces = new ArrayList<>();
        blackPieces = new ArrayList<>();
        whiteScore = 0;
        blackScore = 0;
    }

    public ArrayList<Image> getTakenImages(boolean turn) {
        if(turn)
            return whitePieces;
        else
            return blackPieces;
    }

    public boolean getCheckmate() {
        return checkmate;
    }

    public int getScore(boolean turn) {
        if(turn)
            return whiteScore;
        else
            return blackScore;
    }

    public void setScore(boolean turn, int set) {
        if(turn)
            whiteScore=set;
        else
            blackScore=set;
    }

    public String getMateTurn() {
        if(board.getTurn())
            return "BLACK";
        else
            return "WHITE";
    }

    public void save() {
        board.saveBoard(whiteScore, blackScore, board);
    }

    public void load() {
        if(board.getSaveTurn()!=board.getTurn())
            board = new Board(board);
        board.loadBoard(this);
    }

    private void updateScore(boolean turn, Piece piece) {
        if(turn) {
            whiteScore+=piece.getPieceValue();
            whitePieces.add(piece.getPieceImage());
        }
        else {
            blackScore+=piece.getPieceValue();
            blackPieces.add(piece.getPieceImage());
        }
    }

    public void paintComponent(Graphics g) {
        for (int r = 0; r < 8; r++) {
            for(int c = 0; c < 8; c++) {
                int x = r*100;
                int y = c*100;

                setBoardColors(g, r, c);

                points[r][c] = new Point(x, y);
                g.fillRect(points[r][c].x, points[r][c].y, 100, 100);

            }
        }
        board.paintPieces(g);
    }

    public void setBoardColors(Graphics g, int r, int c) {
        if ((r % 2) == (c % 2))
            g.setColor(new Color(195, 231, 212, 255));
        else
            g.setColor(new Color(21, 182, 158));
        if(board.getTile(c,r).getPiece() != null && board.getTile(c,r).getPiece().getKingInCheck())
            g.setColor(new Color(255, 0, 0));
    }

    public void resetBoard() throws IOException {
        System.out.println("Resetting Board!");
        lastMove = "";
        checkmate = false;
        board = new Board();
        repaint();

        whitePieces = new ArrayList<>();
        blackPieces = new ArrayList<>();
        whiteScore = 0;
        blackScore = 0;
    }

    public void setLastMove(Tile pieceTile, Tile moveTile) {
        char[] letters = {'A','B','C','D','E','F','G','H'};
        char[] numbers = {'8','7','6','5','4','3','2','1'};
        char number;
        char letter;
        String color;
        if(pieceTile.getPiece().isWhite()) {
            color = "White";
            letter = letters[moveTile.getColumn()];
            number = numbers[moveTile.getRow()];
        }
        else {
            color = "Black";
            letter = letters[Math.abs(moveTile.getColumn()-7)];
            number = numbers[Math.abs(moveTile.getRow()-7)];
        }
        String move = letter + "" + number;
        lastMove = pieceTile.getPiece().getType() + " to " + move +" ("+color+")";
    }

    public String getLastMove() {
        return lastMove;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

        Board temp = board; //get the board at the beginning of the attempted move.

        Tile lastClickedTile = board.getClicked(e, board.getTurn()); //get the last clicked tile
        if(lastClickedTile.getPiece() != null && lastClickedTile.getPiece().isWhite() == board.getTurn()) {
            board.calculateMovable(board.getPieceTile(), board.getTurn(), true); //if there is a piece and the piece is the same color as
        }                                                                                       //the turn, show all movable spaces
        if(board.getMoveTile() != null && board.getPieceTile() != null) { //if player clicks a movable space...

            Tile pieceTile = board.getPieceTile();
            Tile moveTile = board.getMoveTile();
            setLastMove(pieceTile, moveTile); //set the banner at the top with the move made
            Piece movePiece = null;
            if (moveTile.getPiece() != null) {
                movePiece = moveTile.getPiece(); //get the piece of the movetile, if there is one
            }

            boolean moveWasCastle = moveTile.isCastleTile(); //checks if the move was a castle
            boolean pieceHadMoved = pieceTile.getPiece().hasMoved(); //checks if the piece has moved before
            boolean kingWasChecked = board.getKingTile(board.getTurn()).getPiece().getKingInCheck(); //checks if the king was checked before the attempted move

            board.makeMove(pieceTile, moveTile); //make the move
            board = new Board(board); //flip the board
            ArrayList<Tile> resultingMoves = board.getAllMovable(true); //get all of the next possible moves
            Tile king = board.getKingTile(!board.getTurn());
            ArrayList<Tile> rooks = board.getSpecificTiles("Rook", !board.getTurn());
            for (Tile tile : resultingMoves) {

                if (tile.getRow() == king.getRow() && tile.getColumn() == king.getColumn()
                    || (rooks.contains(tile) && moveWasCastle)) { //if an opposing move lands on the king or the move was a castle THROUGH a check...
                    lastMove = "MOVE ENDANGERS KING!"; //warn the player that their last move is illegal

                    board.makeMove(moveTile, pieceTile); //redo the move
                    moveTile.setPiece(movePiece); // put the piece back if it was taken
                    if (moveWasCastle) {
                        try {
                            board.resetRooks();     //if the move was a castle, reset the position of the moved rook
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    }

                    board = new Board(board); //flip the board back
                    if (kingWasChecked)
                        board.getKingTile(board.getTurn()).getPiece().setKingInCheck(true); //keep the king in check if it was already
                    else
                        board.getKingTile(board.getTurn()).getPiece().setKingInCheck(false); //remove any unwanted checks
                    repaint();
                    return; //prevent the board from flipping again, prevent pieces who technically haven't moved being classified as "moved."
                }
            }

            if (pieceHadMoved == false) {
                moveTile.getPiece().setHasMoved(true); //flag moved pieces as "moved"
            }
            try {
                board.promotePawn(moveTile); //promote the pawn to a queen if it has reached the end of the board
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            if (movePiece != null) {
                updateScore(!board.getTurn(), movePiece); //update the score
            }


            board = new Board(temp); //flip the board back
            if (board.getKingTile(board.getTurn()).getPiece().getKingInCheck()) { //if the king is in check...
                board.calculateCheckingMoves(moveTile);
                if (board.checkmate()) { //check to see if there is a checkmate
                    System.out.println("CHECKMATE!");
                    checkmate = true;
                    lastMove = "CHECKMATE! " + getMateTurn() + " WINS!";
                }
            }
            repaint();

        }
        repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

}
