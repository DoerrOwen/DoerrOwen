import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class Board {

    private Tile[][] spaces = new Tile[8][8];
    private Tile pieceTile;
    private Tile moveTile;
    private ArrayList<Tile> checkingMoves;

    private boolean TURN; //true = white's turn, false = black's turn
    private boolean saveTurn;

    //CONSTRUCTORS//----------------------------------------------------------------------------------------------------

    public Board() throws IOException {
        placePieces();
        pieceTile = null;
        moveTile = null;
        checkingMoves = new ArrayList<>();
        TURN = true;
    }

    public Board(Board flip) {
        flipBoard(flip);
        pieceTile = null;
        moveTile = null;
        TURN = !flip.getTurn();
    }
    
    //GETTERS/SETTERS//-------------------------------------------------------------------------------------------------

    public boolean getSaveTurn() {
        return saveTurn;
    }

    public Tile getKingTile(boolean turn) {
        for(Tile[] row : spaces)
            for(Tile tile : row) {
                if(tile.getPiece() != null && tile.getPiece().getType() == "King" &&
                   tile.getPiece().isWhite() == turn)
                    return tile;
            }
        return null;
    }

    public Tile[][] getSpaces() {
        return spaces;
    }

    public boolean tileInRange(int r, int c) {
        if(r < 0 || r > 7 || c < 0 || c > 7)
            return false;
        else
            return true;
    }

    public boolean getTurn() {
        return TURN;
    }

    public Tile getTile(int r, int c) {
        if(r < 0 || r > 7 || c < 0 || c > 7)
            return null;
        else
            return spaces[r][c];
    }

    public Tile getPieceTile() {
        return pieceTile;
    }

    public Tile getMoveTile() {
        return moveTile;
    }

    private Piece getNewRook(boolean turn) throws IOException {
        if(turn)
            return new Piece("Rook", true, "pieceImages/rook_white.png",5);
        else
            return new Piece("Rook", false, "pieceImages/rook_black.png",5);
    }

    private Piece getNewQueen(boolean turn) throws IOException{
        if(turn)
            return new Piece("Queen", true, "pieceImages/queen_white.png",9);
        else
            return new Piece("Queen", false, "pieceImages/queen_black.png",9);
    }

    //GRAPHICS & BOARD-BUILDING METHODS//-------------------------------------------------------------------------------

    private void placeWhitePieces() throws IOException {
        spaces[7][0] = new Tile(new Piece("Rook", true, "pieceImages/rook_white.png",5), 7, 0);
        spaces[7][1] = new Tile(new Piece("Knight", true, "pieceImages/knight_white.png",3), 7, 1);
        spaces[7][2] = new Tile(new Piece("Bishop", true, "pieceImages/bishop_white.png",3), 7, 2);
        spaces[7][3] = new Tile(new Piece("Queen", true, "pieceImages/queen_white.png",9), 7, 3);
        spaces[7][4] = new Tile(new Piece("King", true, "pieceImages/king_white.png",0), 7, 4);
        spaces[7][5] = new Tile(new Piece("Bishop", true,"pieceImages/bishop_white.png",3), 7, 5);
        spaces[7][6] = new Tile(new Piece("Knight", true, "pieceImages/knight_white.png",3), 7, 6);
        spaces[7][7] = new Tile(new Piece("Rook", true, "pieceImages/rook_white.png",5), 7, 7);

        for(int c = 0; c < 8; c++) {
            spaces[6][c] = new Tile(new Piece("Pawn", true, "pieceImages/pawn_white.png", 1), 6, c);
        }
    }

    private void placeBlackPieces() throws IOException {
        for(int c = 0; c < 8; c++) {
            spaces[1][c] = new Tile(new Piece("Pawn", false, "pieceImages/pawn_black.png",1), 1, c);
        }

        spaces[0][0] = new Tile(new Piece("Rook", false, "pieceImages/rook_black.png",5), 0, 0);
        spaces[0][1] = new Tile(new Piece("Knight", false, "pieceImages/knight_black.png",3), 0, 1);
        spaces[0][2] = new Tile(new Piece("Bishop", false, "pieceImages/bishop_black.png",3), 0, 2);
        spaces[0][3] = new Tile(new Piece("Queen", false, "pieceImages/queen_black.png",9), 0, 3);
        spaces[0][4] = new Tile(new Piece("King", false, "pieceImages/king_black.png",0), 0, 4);
        spaces[0][5] = new Tile(new Piece("Bishop", false, "pieceImages/bishop_black.png",3), 0, 5);
        spaces[0][6] = new Tile(new Piece("Knight", false, "pieceImages/knight_black.png",3), 0, 6);
        spaces[0][7] = new Tile(new Piece("Rook", false, "pieceImages/rook_black.png",5), 0, 7);
    }

    public void placePieces() throws IOException {
        //Images from https://commons.wikimedia.org/wiki/Category:PNG_chess_pieces/Standard_transparent
        placeWhitePieces();
        placeBlackPieces();
        //Empty Spaces
        for(int r = 2; r < 6; r++) {
            for(int c = 0; c < 8; c++) {
                spaces[r][c] = new Tile(null, r, c);
            }
        }
    }

    public void paintPieces(Graphics g) {
        for(int r = 0; r < spaces.length; r++)
            for(int c = 0; c < spaces[r].length; c++) {
                Tile temp = spaces[r][c];
                int x = c*100;
                int y = r*100;

                if(temp.getPiece() != null) {
                    g.drawImage(temp.getPiece().getPieceImage(), x, y, 100, 100, null);
                }
                drawMoveOval(g, r, c, x, y);
            }

    }

    private void drawMoveOval(Graphics g, int r, int c, int x, int y) {
        if(spaces[r][c].isMovable()) {

            if(spaces[r][c].getPiece() != null)
                g.setColor(new Color(186, 67, 67, 150));
            else
                g.setColor(new Color(97, 91, 91, 150));

            g.fillOval(x+35, y+35, 30,30);

        }
    }

    //MOVE CALCULATION//------------------------------------------------------------------------------------------------

    public void calculateMovable(Tile tile, boolean turn, boolean normalCalculation) {
        switch(tile.getPiece().getType()) {
            case "Pawn":
                calculatePawnMoves(tile, turn);
                break;
            case "Rook":
                calculateRookMoves(tile, turn);
                break;
            case "Knight":
                calculateKnightMoves(tile, turn);
                break;
            case "Bishop":
                calculateBishopMoves(tile, turn);
            case "Queen":
                calculateQueenMoves(tile, turn);
                break;
            case "King":
                calculateKingMoves(tile, turn);
                break;
        }
        checkCheck(turn, tile, normalCalculation);
    }

    private void calculateBishopMoves(Tile tile, boolean turn) {
        int r = tile.getRow();
        int c = tile.getColumn();

        boolean upLeft = true;
        boolean downLeft = true;
        boolean upRight = true;
        boolean downRight = true;
        int row = r;
        int col = c;

        calculateMoveLoop(tile, upRight, turn, row, col, -1, 1); //UP
        calculateMoveLoop(tile, downLeft, turn, row, col, 1, -1); //DOWN
        calculateMoveLoop(tile, downRight, turn, row, col, 1, 1); //RIGHT
        calculateMoveLoop(tile, upLeft, turn, row, col, -1, -1); //LEFT
    }

    private void calculateKnightMoves(Tile tile, boolean turn) {
        int r = tile.getRow();
        int c = tile.getColumn();

        //(r-2,c-1)(r-2,c+1)(r-1,c-2)(r-1,c+2)
        //(r+2,c-1)(r+2,c+1)(r+1,c-2)(r+1,c+2)
        int[][] shifts = new int[][] { {-2, -1},{-2, 1},{-1, -2},{-1, 2},{2, -1},{2, 1},{1, -2},{1, 2} };
        for(int i = 0; i < shifts.length; i++) {
            int row = r + shifts[i][0];
            int col = c + shifts[i][1];
            calculateSpecificRange(row, col, turn);
        }
    }

    private void calculateRookMoves(Tile tile, boolean turn) {
        int r = tile.getRow();
        int c = tile.getColumn();

        boolean up = true;
        boolean down = true;
        boolean right = true;
        boolean left = true;
        int row = r;
        int col = c;

        calculateMoveLoop(tile, up, turn, row, col, -1, 0); //UP
        calculateMoveLoop(tile, down, turn, row, col, 1, 0); //DOWN
        calculateMoveLoop(tile, right, turn, row, col, 0, 1); //RIGHT
        calculateMoveLoop(tile, left, turn, row, col, 0, -1); //LEFT
    }

    private void calculatePawnMoves(Tile tile, boolean turn) {
        int r = tile.getRow();
        int c = tile.getColumn();

        if(tileInRange(r-1, c) && spaces[r-1][c].getPiece() == null)
            spaces[r-1][c].setMovable(true);
        if(tileInRange(r-2, c) && spaces[r-2][c].getPiece() == null && tile.getPiece().hasMoved() == false &&
                spaces[r-1][c].isMovable())
            spaces[r-2][c].setMovable(true);
        if(tileInRange(r-1, c-1) && spaces[r-1][c-1].getPiece() != null) {
            if(spaces[r-1][c-1].getPiece().isWhite() != turn) {
                spaces[r-1][c-1].setMovable(true);
            }
        }
        if(tileInRange(r-1, c+1) && spaces[r-1][c+1].getPiece() != null) {
            if(spaces[r-1][c+1].getPiece().isWhite() != turn) {
                spaces[r-1][c+1].setMovable(true);
            }
        }
    }

    private void calculateQueenMoves(Tile tile, boolean turn) {
        calculateRookMoves(tile, turn);
        calculateBishopMoves(tile, turn);
    }

    private void calculateKingMoves(Tile tile, boolean turn) {

        int r = tile.getRow();
        int c = tile.getColumn();

        //(-1,0)(-1,-1)(1,0)(1,1)(0,1)(0,-1)(-1,1)(1,-1)
        int[][] shifts = new int[][] { {-1, 0},{-1, -1},{1, 0},{1, 1},{0, 1},{0, -1},{-1, 1},{1, -1} };
        for(int i = 0; i < shifts.length; i++) {
            int row = r + shifts[i][0];
            int col = c + shifts[i][1];
            calculateSpecificRange(row, col, turn);
        }
        if(tile.getPiece().getKingInCheck() == false)
            calculateCastle(tile, turn);
    }

    private void calculateMoveLoop(Tile tile, boolean check, boolean turn, int row, int col, int rowShift, int colShift) {
        int tempRow = row;
        int tempCol = col;
        while(check) {
            row = row+rowShift;
            col = col+colShift;
            check = moveLoopCheck(turn, row, col);
        }
        row = tempRow;
        col = tempCol;
        removeBishopStraits(tile, row, col);
    }

    private boolean moveLoopCheck(boolean turn, int row, int col) {
        if(tileInRange(row, col) == false) {
            return false;
        }
        else if(tileInRange(row,col)) {

            if(spaces[row][col].getPiece() == null) {
                spaces[row][col].setMovable(true);
            }
            else if(spaces[row][col].getPiece() != null && spaces[row][col].getPiece().isWhite() != turn) {
                spaces[row][col].setMovable(true);
                return false;
            }
            else if(spaces[row][col].getPiece() != null && spaces[row][col].getPiece().isWhite() == turn) {
                return false;
            }
        }
        return true;
    }

    private void removeBishopStraits(Tile tile, int row, int col) {
        if(tile.getPiece().getType() == "Bishop") {
            for(int r = 0; r < spaces.length; r++)
                for(int c = 0; c < spaces[r].length; c++) {
                    if((r == row || c == col) && spaces[r][c].isMovable())
                        spaces[r][c].setMovable(false);
                }
        }
    }

    private void calculateSpecificRange(int row, int col, boolean turn) {
        if(tileInRange(row, col)) {
            if((spaces[row][col].getPiece() != null && spaces[row][col].getPiece().isWhite() != turn)) {
                spaces[row][col].setMovable(true);
            }
            else if(spaces[row][col].getPiece() == null) {
                spaces[row][col].setMovable(true);
            }
        }
    }

    //---------------------//Castling//------------------------------------//

    private void calculateCastle(Tile kingTile, boolean turn) {
        ArrayList<Tile> rooks = getSpecificTiles("Rook", turn);
        Tile rightRook = null;
        Tile leftRook = null;
        Tile[] leftTiles;
        Tile[] rightTiles;

        for(Tile rook : rooks) {
            if(rook.getPiece().hasMoved() == false) {
                if(rook.getColumn() == 0)
                    leftRook = rook;
                if(rook.getColumn() == 7)
                    rightRook = rook;
            }
        }

        if(turn) {
            leftTiles = new Tile[] {spaces[7][1], spaces[7][2], spaces[7][3]};
            rightTiles = new Tile[] {spaces[7][5], spaces[7][6]};
            setCastle(leftRook, kingTile, leftTiles, 2);
            setCastle(rightRook, kingTile, rightTiles, 6);
        }
        else {
            leftTiles = new Tile[] {spaces[7][1], spaces[7][2]};
            rightTiles = new Tile[] {spaces[7][4], spaces[7][5], spaces[7][6]};
            setCastle(leftRook, kingTile, leftTiles, 1);
            setCastle(rightRook, kingTile, rightTiles, 5);
        }
    }

    private int countBlockingPieces(Tile[] checkTiles) {
        int answer = 0;
        for(Tile space : checkTiles) {
            if(!(space.getPiece() == null))
                answer++;
        }
        return answer;
    }

    private void setCastle(Tile rookTile, Tile kingTile, Tile[] checkTiles, int col) {
        if(countBlockingPieces(checkTiles) == 0 && kingTile.getPiece().hasMoved() == false && rookTile != null) {
            spaces[7][col].setCastleTile(true);
            spaces[7][col].setMovable(true);
        }
    }

    public void checkCheck(boolean turn, Tile tile, boolean normalCalculation) {
        Tile kingTile = getKingTile(!turn);
        if(kingTile.isMovable()) {
            if(normalCalculation) {
                kingTile.setMovable(false);
            }
            kingTile.getPiece().setKingInCheck(true);
        }
        else {
            kingTile.getPiece().setKingInCheck(false);
        }
    }

    //Checkmate Calculation//-------------------------------------------------------------------------------------------

    public void calculateCheckingMoves(Tile moveTile) {
        resetSelections();
        checkingMoves = new ArrayList<>();
        calculateMovable(moveTile, TURN, true);
        checkingMoves = getCurrentMovable();
        removeUnwantedCheckingMoves();
        checkingMoves.add(moveTile);
    }

    private void removeUnwantedCheckingMoves() {
        resetSelections();
        calculateQueenMoves(getKingTile(TURN), TURN);
        ArrayList<Tile> kingRange = getCurrentMovable();
        ArrayList<Tile> temp = checkingMoves;
        checkingMoves = new ArrayList<>();
        for(Tile tile : temp) {
            if(kingRange.contains(tile))
                checkingMoves.add(tile);
        }
    }

    private boolean checkmateOne() {
        //checks to see if the king can move to a safe space
        calculateMovable(getKingTile(TURN), TURN, true);
        for(Tile bad : badKingSpaces())
            bad.setMovable(false);
        for(Tile kingMove : getCurrentMovable()) {
            if(!(checkingMoves.contains(kingMove))) {
                resetSelections();
                return false;
            }
        }
        resetSelections();
        return true;
    }

    private ArrayList<Tile> badKingSpaces() {
        ArrayList<Tile> answer = new ArrayList<>();
        for(Tile kingMove : getCurrentMovable()) {
            if(checkingMoves.contains(kingMove) && kingMove.getRow() > getKingTile(TURN).getRow()) {
                if(tileInRange(kingMove.getRow()-2, kingMove.getColumn()))
                    answer.add(getTile(kingMove.getRow()-2, kingMove.getColumn()));
            }
        }
        return answer;
    }

    private boolean checkmateTwo() {
        //checks to see if any piece can block the path to the king or kill the piece attacking it
        ArrayList<Tile> allMoves = getAllMovable(false);
        for(Tile check : checkingMoves) {
            if(allMoves.contains(check))
                return false;
        }
        return true;
    }

    public boolean checkmate() {
        return (checkmateOne() && checkmateTwo());
    }

    //Saving/Loading Games//--------------------------------------------------------------------------------------------

    public void saveBoard(int ws, int bs, Board save) {
        PrintWriter boardFile = null;
        try {
            boardFile = new PrintWriter(new File("board.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        for(Tile[] row : save.getSpaces()) {
            for(Tile tile : row) {
                if(tile.getPiece()!=null) {
                    printPieceDetails(boardFile, tile.getPiece());
                }
                else
                    boardFile.print("---- ");
            }
            boardFile.println();
        }
        boardFile.println(save.getTurn());
        saveTurn = save.getTurn();
        boardFile.println(ws);
        boardFile.println(bs);
        boardFile.close();
    }

    private void printPieceDetails(PrintWriter boardFile, Piece piece) {
        boardFile.print(piece.getType().substring(0,2)); //First two letters of the piece name
        boardFile.print(piece.getColorChar()); // 'W' or 'B' (white or black)
        boardFile.print(piece.getMovedChar() + " "); // 'T' or 'F' (moved or not)
    }

    public void loadBoard(ChessPanel scoreKeep) {
        Scanner boardLoader = null;
        try {
            boardLoader = new Scanner(new File("board.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String[][] boardRead = new String[8][8];
        readBoard(boardRead, boardLoader, scoreKeep);
        placeLoadedBoard(boardRead);
        scoreKeep.repaint();
        resetSelections();
    }

    private void readBoard(String[][] boardRead, Scanner boardLoader, ChessPanel scoreKeep) {
        for(int r=0; r<8; r++) {
            boardRead[r] = boardLoader.nextLine().split(" ");
        }
        if(boardLoader.nextLine().equals("false")) {
            TURN = false;
        }
        else {
            TURN = true;
        }
        int ws = Integer.parseInt(boardLoader.nextLine());
        int bs = Integer.parseInt(boardLoader.nextLine());
        scoreKeep.setScore(true, ws);
        scoreKeep.setScore(false, bs);
    }

    private void placeLoadedBoard(String[][] boardRead) {
        for(int r=0; r<boardRead.length; r++) {
            for(int c=0; c<boardRead[r].length; c++) {
                if(!(boardRead[r][c].equals("----"))) {
                    String type = getLoadedType(boardRead[r][c].substring(0,2));
                    boolean color = (boardRead[r][c].charAt(2) == 'W');
                    boolean moved = (boardRead[r][c].charAt(3) == 'T');
                    try {
                        spaces[r][c].setPiece(new Piece(type, color, getImagePath(color,type), getPieceValue(type)));
                        spaces[r][c].getPiece().setHasMoved(moved);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    spaces[r][c].setPiece(null);
                }
            }
        }

    }

    public String getLoadedType(String t) {
        switch (t) {
            case "Ro": return "Rook";
            case "Kn": return "Knight";
            case "Bi": return "Bishop";
            case "Qu": return "Queen";
            case "Ki": return "King";
            case "Pa": return "Pawn";
            default: return null;
        }
    }

    public String getImagePath(boolean color, String type) {
        String answer = "pieceImages/";
        answer += (type.toLowerCase() + "_");
        if(color)
            answer+="white";
        else
            answer+="black";
        answer+=".png";
        return answer;
    }

    public int getPieceValue(String type) {
        switch (type) {
            case "Rook": return 5;
            case "Knight": return 3;
            case "Bishop": return 3;
            case "Queen": return 9;
            case "King": return 0;
            case "Pawn": return 1;
            default: return 0;
        }
    }

    //Moves & Other Methods//-------------------------------------------------------------------------------------------

    public void resetSelections() { //resets all movable/castled tiles
        for(int r = 0; r < spaces.length; r++)
            for(int c = 0; c < spaces[r].length; c++) {
                spaces[r][c].setMovable(false);
                spaces[r][c].setCastleTile(false);
            }
    }

    public Tile getClicked(MouseEvent e, boolean turn) { //gets the last clicked tile and sets the piece/move tiles
        for(int r = 0; r < spaces.length; r++)
            for(int c = 0; c < spaces[r].length; c++) {

                int x;
                int y;

                if(turn) {
                    x=c;
                    y=r;
                }
                else {
                    x = Math.abs(c-7);
                    y = Math.abs(r-7);
                }

                if((e.getX() > spaces[r][c].getLocationX() && e.getX() < spaces[r][c].getLocationX()+100) &&
                   (e.getY() > spaces[r][c].getLocationY() && e.getY() < spaces[r][c].getLocationY()+100)) {

                    if(spaces[x][y].getPiece() != null && spaces[x][y].getPiece().isWhite() == turn) {
                        pieceTile = spaces[x][y];
                        resetSelections();
                    }
                    else if(spaces[x][y].isMovable()) {
                        moveTile = spaces[x][y];
                    }
                    return spaces[x][y];

                }
            }
        return spaces[0][0];
    }

    public void makeMove(Tile pieceTile, Tile moveTile) { //makes a move from pieceTile to moveTile
        if(moveTile.isCastleTile()) {
            executeCastleTurn(TURN, moveTile);
        }
        else {
            makeNormalMove(pieceTile, moveTile);
        }
    }

    private void makeNormalMove(Tile pieceTile, Tile moveTile) { //makes a non-castle move
        moveTile.setPiece(pieceTile.getPiece());
        pieceTile.setPiece(null);
        resetSelections();
        calculateMovable(moveTile, TURN, true);
        resetSelections();
    }

    public void executeCastleTurn(boolean turn, Tile moveTile) { //configures the castle based on the turn and direction
        if(turn) {
            if(moveTile.getColumn() == 2) {
                executeCastle(0, 3);
            }
            else if(moveTile.getColumn() == 6) {
                executeCastle(7, 5);
            }
        }
        else {
            if(moveTile.getColumn() == 1) {
                executeCastle(0, 2);
            }
            else if(moveTile.getColumn() == 5) {
                executeCastle(7, 4);
            }
        }
    }

    private void executeCastle(int rookLocation, int castleCol) { //makes a castling move
        resetSelections();
        moveTile.setPiece(pieceTile.getPiece());
        pieceTile.setPiece(null);
        spaces[7][castleCol].setPiece(spaces[7][rookLocation].getPiece());
        spaces[7][rookLocation].setPiece(null);

        resetSelections();
        calculateMovable(moveTile, TURN, true);
        calculateMovable(spaces[7][castleCol], TURN, true);
        resetSelections();
    }

    public void resetRooks() throws IOException { //resets the rook pieces if a castle is illegal
        Tile[] bottom = spaces[0];
        for(int i = 1; i < spaces.length-1; i++) {
            if(bottom[i].getPiece()!=null && bottom[i].getPiece().getType().equals("Rook") && bottom[i].getPiece().hasMoved() == false) {
                bottom[i].setPiece(null);
                if(i >= 4) {
                    spaces[0][7].setPiece(getNewRook(!TURN));
                    bottom[i].setPiece(null);
                }
                else {
                    spaces[0][0].setPiece(getNewRook(!TURN));
                    bottom[i].setPiece(null);
                }
            }
        }
    }

    public void promotePawn(Tile tile) throws IOException { //promotes a pawn if it reaches the end of the board
        if(tile.getPiece()!=null && tile.getPiece().getType().equals("Pawn")) {
            if(tile.getRow() == 7) {
                tile.setPiece(getNewQueen(!TURN));
                tile.getPiece().setHasMoved(true);
            }
        }
    }

    public ArrayList<Tile> getCurrentMovable() { //gets all tiles that are currently movable
        ArrayList<Tile> answer = new ArrayList<>();
        for(Tile[] row : spaces)
            for(Tile tile : row) {
                if(tile.isMovable())
                    answer.add(tile);
            }
        return answer;
    }

    public ArrayList<Tile> getAllMovable(boolean useKingMoves) { //gets ALL of the possible moves for the current turn
        ArrayList<Tile> answer = new ArrayList<>();
        for(Tile[] row : spaces)
            for(Tile tile : row) {
                if(tile.getPiece() != null && tile.getPiece().isWhite() == getTurn()) {
                    if(useKingMoves || (useKingMoves == false && !(tile.getPiece().getType().equals("King"))))
                        calculateMovable(tile, getTurn(), false);

                    for(Tile[] row2 : spaces)
                        for(Tile tile2 : row2) {
                            if(tile2.isMovable()) {
                                answer.add(tile2);
                            }
                        }

                    resetSelections();
                }
            }
        return answer;
    }

    public ArrayList<Tile> getSpecificTiles(String type, boolean color) { //gets tiles with the specified type and color
        ArrayList<Tile> answer = new ArrayList<>();
        for(Tile[] row : spaces)
            for(Tile tile : row) {
                if(tile.getPiece() != null && tile.getPiece().getType() == type && tile.getPiece().isWhite() == color) {
                    answer.add(tile);
                }
            }
        return answer;
    }

    public void flipBoard(Board flip) { //flips the board (used in overloaded constructor)
        Tile[][] spaces = flip.getSpaces();
        for(int r = 0; r < spaces.length; r++) {
            for(int c = 0; c < spaces[r].length; c++) {
                this.spaces[r][c] = spaces[Math.abs(r-7)][Math.abs(c-7)];
                this.spaces[r][c].setRow(r);
                this.spaces[r][c].setColumn(c);
            }
        }
    }

    //TEST METHODS//----------------------------------------------------------------------------------------------------

    public void printTile(Tile tile) {
        String name = "";
        if(tile.getPiece() == null) {
            name = "There is no piece at ";
        }
        else {
            name = tile.getPiece().getType() + " at ";
        }
        System.out.println(name + tile.getRow() +","+ tile.getColumn());
    }

    public void printMoveInfo(Tile pieceTile, Tile moveTile) {
        System.out.println("Moving " + pieceTile.getPiece().getType() + " at " + pieceTile.getRow()+","
                +pieceTile.getColumn() + " to " +moveTile.getRow()+","+moveTile.getColumn());
    }

    public ArrayList<Tile> getCurrentImages(boolean turn) {
        ArrayList<Tile> answer = new ArrayList<>();
        for(Tile[] row : spaces)
            for(Tile tile : row) {
                if(tile.getPiece()!=null && tile.getPiece().isWhite() == turn)
                    answer.add(tile);
            }
        return answer;
    }

}