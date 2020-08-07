package Game;

import javax.swing.*;

public class GameFrame extends JFrame {
    public static final int WIDTH=500;
    public static final int HEIGHT=650;

    public GameFrame(){
        setSize(WIDTH,HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("飞机大战");
        setLocationRelativeTo(null);
        add(new GamePanel(this));
        setVisible(true);
    }
}
