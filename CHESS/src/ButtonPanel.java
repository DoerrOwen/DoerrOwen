import javax.swing.*;
import javax.swing.plaf.basic.BasicOptionPaneUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class ButtonPanel extends JPanel {

    private JButton resetButton;
    private JButton saveButton;
    private JButton loadButton;
    private ChessPanel chessPanel;

    public ButtonPanel(ChessPanel chessPanel) {

        this.chessPanel = chessPanel;
        setSize(800, 50);

        resetButton = new JButton("Reset Board");
        resetButton.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent e) {
                try {
                    chessPanel.resetBoard();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }

        });
        saveButton = new JButton("Save Board");
        saveButton.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent e) {
                chessPanel.save();
            }

        });
        loadButton = new JButton("Load Board");
        loadButton.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent e) {
                chessPanel.load();
            }

        });

        add(resetButton);
        add(saveButton);
        add(loadButton);
    }

    public void paintComponent(Graphics g) {
        String str = chessPanel.getLastMove();
        Font font = new Font("Verdana", Font.BOLD, 13);
        g.setFont(font);
        if(chessPanel.getCheckmate())
            str = "CHECKMATE! " + chessPanel.getMateTurn() + " WINS!";
        if(str == "MOVE ENDANGERS KING!" || chessPanel.getCheckmate()) {
            g.setColor(Color.red);
            g.drawString(str, 10, 25);
        }
        else {
            g.setColor(Color.DARK_GRAY);
            g.drawString("LAST MOVE: " + str, 10, 25);
        }
        repaint();
    }


}