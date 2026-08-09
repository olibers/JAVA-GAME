import javax.swing.JFrame;

public class App {

    public static void main(String[] args) {

        JFrame window = new JFrame();

        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("RPG");

        GamePanel gamePanel = new GamePanel();

        window.add(gamePanel);
        window.pack();

        window.setLocationRelativeTo(null);
        window.setVisible(true);

        // Ensures the window grabs keyboard input
        gamePanel.requestFocusInWindow();

        gamePanel.setupGame();
        gamePanel.startGameThread();
    }
