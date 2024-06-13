import java.awt.Dimension;
import java.awt.EventQueue;
import java.io.*;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Portal extends JFrame {

    private static final long serialVersionUID = 1L;
    private static final String SAVE_FILE_NAME = "game_save.ser";
    private final int B_WIDTH = 1300;
    private final int B_HEIGHT = 710;
    private Player player;
    private Enemy enemy;
    private Portals portals;
    private Walls walls;
    private Apple apple;
    private Score score;

    private boolean do_save_game; // Flag to determine if game should be saved

    public Portal() {
        loadGame(); // Load game state when the game starts
        initUI();
    }

    private void initGameComponents() {
        if (player == null) {
            player = new Player();
        }
        if (apple == null) {
            apple = new Apple();
        }
        if (enemy == null) {
            enemy = new Enemy(player.get_player_x(), B_WIDTH, B_HEIGHT);
        }
        if (score == null) {
            score = new Score();
        }

        portals = new Portals();
        walls = new Walls();

    }

    private void initUI() {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(640, 480));
        getContentPane().add(panel);

        JButton singlePlayerButton = new JButton("Single Player");
        singlePlayerButton.addActionListener(e -> {
            initGameComponents(); // Initialize game components if starting a new game
            add(new Board(player, enemy, walls, portals, apple,score));
            getContentPane().remove(panel);
            pack();
            startGameLoop(); // Start the game loop
        });

        panel.add(singlePlayerButton);

        setTitle("Portal");
        setSize(600, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // Start the game loop where game state is updated
    private void startGameLoop() {
        new Thread(() -> {
            while (true) {
                // Perform game updates here

                // Example: Check if save is needed based on player's action or game condition
                do_save_game = player.do_save_game();
                if (do_save_game) {
                    saveGame(); // Save game state
                    System.out.println("Game saved.");
                    // Reset save flag after saving
                    player.save_game(false);
                }

                try {
                    Thread.sleep(1000); // Adjust delay as per your game loop speed
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    // Save game state to a file
    public void saveGame() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SAVE_FILE_NAME))) {
            oos.writeObject(player);
            oos.writeObject(enemy);
            oos.writeObject(apple);
            oos.writeObject(score);
            System.out.println("Game saved successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load game state from a file
    public void loadGame() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(SAVE_FILE_NAME))) {
            player = (Player) ois.readObject();
            enemy = (Enemy) ois.readObject();
            apple = (Apple) ois.readObject();
            score = (Score) ois.readObject();
            System.out.println("Game loaded successfully.");
        } catch (FileNotFoundException e) {
            System.out.println("No previous save file found. Starting new game.");
            initGameComponents(); // Initialize new game components
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            JFrame ex = new Portal();
            ex.setVisible(true);
        });
    }
}
