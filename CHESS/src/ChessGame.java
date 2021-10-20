import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.io.IOException;

public class ChessGame extends JFrame {

    public ChessGame() throws IOException {
        setTitle("CHESS");
        setPreferredSize(new Dimension(600, 400));
        ChessPanel gamePanel = new ChessPanel();
        ButtonPanel buttonPanel = new ButtonPanel(gamePanel);
        ScorePanel scorePanel = new ScorePanel(gamePanel);
        Container mainWindow = getContentPane();

        Border line = BorderFactory.createLineBorder(Color.black);
        gamePanel.setBorder(line);
        buttonPanel.setBorder(line);

        mainWindow.add(gamePanel, BorderLayout.CENTER);
        mainWindow.add(buttonPanel, BorderLayout.NORTH);
        mainWindow.add(scorePanel, BorderLayout.EAST);
    }

    public static void main(String[] args) throws IOException {

        ChessGame myApp = new ChessGame();
        myApp.setBounds(0,0,918,860);
        myApp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        myApp.setVisible(true);

    }

}
