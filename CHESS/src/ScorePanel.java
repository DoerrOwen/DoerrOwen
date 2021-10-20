import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

public class ScorePanel extends JPanel {

    private ChessPanel chessPanel;
    private JButton quit;

    public ScorePanel(ChessPanel chessPanel) {
        this.chessPanel = chessPanel;

        quit = new JButton("    QUIT    ");
        quit.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        add(quit);
        setSize(250, 800);
    }



    public void paintComponent(Graphics g) {
        setBackground(new Color(173, 164, 164));
        Font font = new Font("Verdana", Font.BOLD, 50);
        g.setFont(font);
        g.drawString("■", 73, 70);
        g.setColor(Color.WHITE);
        g.drawString("■", 13, 70);

        ArrayList<Image> whitePieces = chessPanel.getTakenImages(true);
        ArrayList<Image> blackPieces = chessPanel.getTakenImages(false);
        printPieces(g, whitePieces, 16, chessPanel.getScore(true));
        printPieces(g, blackPieces, 77, chessPanel.getScore(false));
        repaint();
    }

    public void printPieces(Graphics g, ArrayList<Image> pieces, int x, int score) {
        for(int i = 0; i < pieces.size(); i++) {
            int n = i*20;
            g.drawImage(pieces.get(i), x, 70+n, 20,  20, null);
        }
        printScore(g, pieces.size(), x, score);
    }

    public void printScore(Graphics g, int pieceSize, int x, int score) {
        int finalPos = (pieceSize*20)+80;
        String str = Integer.toString(score);
        Font font = new Font("Verdana", Font.BOLD, 12);
        g.setFont(font);
        g.setColor(Color.DARK_GRAY);
        g.drawString(str, x+5, finalPos);
    }

}
