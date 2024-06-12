import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Board extends JPanel implements ActionListener {

    private final int B_WIDTH  = 1300;
    private final int B_HEIGHT = 710;
    private final int DELAY    = 10;

    public boolean restart = false;
    private boolean inGame = true;
    private int score = 0; // Add score variable

    private Timer timer;
    private Image backgroundImage;

    private long lastTime = System.nanoTime();
    private final long flickerInterval = 500 * 1000000;
    private boolean showPressSpaceMessage = true;

    Player p;
    Enemy e;
    Walls w;
    Portals pt;
    Apple a;

    String backgroundImage_path = "Portal-game/src/new_game_resources/bg.jpeg";

    public Board() {
        initBoard();
    }

    private void initBoard() {
        p = new Player();
        e = new Enemy(p.get_player_x(), B_WIDTH, B_HEIGHT);
        w = new Walls();
        pt = new Portals();
        a = new Apple();

        addKeyListener(new TAdapter());
        setBackground(Color.black);
        setFocusable(true);
        
        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
        loadImages();
        initGame();
    }

    private void loadImages() {
        ImageIcon iibg = new ImageIcon(backgroundImage_path);
        backgroundImage = iibg.getImage();
    }

    private void reset() {
        p.set_player_pos();
        e.reset();
        score = 0; // Reset score
        initGame();
    }

    private void initGame() {
        e.initGame(p.get_player_x(), B_WIDTH, B_HEIGHT);        
        w.initGame();
        timer = new Timer(DELAY, this);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, this);
        doDrawing(g);
    }

    private void doDrawing(Graphics g) {
        if (inGame) {
            g.drawImage(a.getAppleImage(), a.get_apple_x(), a.get_apple_y(), this);

            if (e.get_on_ground_enemys() != 0) {
                for (int i = 0; i < e.get_on_ground_enemys(); i++) {
                    g.drawImage(e.get_enemy_Image(), e.get_enemy_x()[i], e.get_enemy_y()[i], this);
                }
            }

            for (int i = 0; i < w.wall_lenght(); i++) {
                g.drawImage(w.get_wall_Image(), w.get_wall_x()[i], w.get_walls_y()[i], this);
            }

            int[] ps = pt.get_portals_pos();
            g.drawImage(pt.get_portal_Image(), ps[2], ps[3], this);
            g.drawImage(pt.get_portal_Image(), ps[0], ps[1], this);
            g.drawImage(p.get_player_Image(), p.get_player_x(), p.get_player_y(), this);
            
            int p_x;
            int p_y;
            int[] pt_pos = pt.mirror_image(p.get_player_x(), p.get_player_y());
            p_x = pt_pos[0]; p_y = pt_pos[1];

            if (p_x != p.get_player_x()) {
                g.drawImage(p.get_player_Image(), p_x, p_y, this);
            }

            // Draw the scoreboard
            drawScoreboard(g);
            
            Toolkit.getDefaultToolkit().sync();
        } else {
            gameOver(g);
        }
    }

    private void drawScoreboard(Graphics g) {
        g.setColor(new Color(255, 0, 0, 200)); // Red color with transparency
        g.setFont(new Font("Helvetica", Font.BOLD, 30));
        String scoreText = "Score: " + score;
        FontMetrics metrics = getFontMetrics(g.getFont());
        int x = (B_WIDTH - metrics.stringWidth(scoreText)) / 2;
        int y = metrics.getHeight() + 20;
        g.drawString(scoreText, x, y);
    }

    private void move() {
        p.move(w.get_wall_x(), w.get_walls_y(), w.wall_lenght());
        e.move(p.get_player_x(), p.get_player_y(), B_WIDTH, B_HEIGHT);
    }

    private void checkCollision() {
        int[] pt_pos = pt.get_portals_pos();
        int p1_x = pt_pos[0];
        int p1_y = pt_pos[1];
        int p2_x = pt_pos[2];
        int p2_y = pt_pos[3];
        p.checkCollision(p1_x, p1_y, p2_x, p2_y);

        int newPoints = a.apple_check(p.get_player_x(), p.get_player_y(), w.get_wall_x(), w.get_walls_y(), w.wall_lenght());
        score += newPoints; // Update score

        e.checkCollision(p.get_player_x(), p.get_player_y());
        if (p.enemy_collision(e.get_enemy_x(), e.get_enemy_y(), e.get_on_ground_enemys())) {
            inGame = false;
            timer.stop();
        }
        if (!inGame) {
            timer.stop();
        }
    }

    private void gameOver(Graphics g) {
        String gameOverMsg = "Game Over";
        String pressSpaceMsg = "Press Space to Restart";
        Font smallFont = new Font("Vibes", Font.BOLD, 30);
        Font pressSpaceFont = new Font("Vibes", Font.PLAIN, 20);
        FontMetrics metrics = getFontMetrics(smallFont);

        g.setColor(Color.red);
        g.setFont(smallFont);
        g.drawString(gameOverMsg, (B_WIDTH - metrics.stringWidth(gameOverMsg)) / 2, B_HEIGHT / 2);

        long now = System.nanoTime();
        long elapsed = now - lastTime;

        if (elapsed > flickerInterval) {
            showPressSpaceMessage = !showPressSpaceMessage;
            lastTime = now;
        }

        if (showPressSpaceMessage) {
            g.setFont(pressSpaceFont);
            FontMetrics pressSpaceMetrics = getFontMetrics(pressSpaceFont);
            g.drawString(pressSpaceMsg, (B_WIDTH - pressSpaceMetrics.stringWidth(pressSpaceMsg)) / 2,
                    (B_HEIGHT / 2) + pressSpaceMetrics.getHeight() + 10);
        }

        repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (inGame) {
            if (restart) {
                inGame = false;
            }
            checkCollision();
            move();
        }
        repaint();
    }

    private class TAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();

            if (key == KeyEvent.VK_LEFT) {
                p.moves_left();
            } else if (key == KeyEvent.VK_RIGHT) {
                p.moves_right();
            } else if (key == KeyEvent.VK_SPACE) {
                if (!inGame) {
                    reset();
                    inGame = true;
                }
                p.space_jump();
            } else if (key == KeyEvent.VK_ESCAPE) {
                restart = true;
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            int key = e.getKeyCode();

            switch (key) {
                case KeyEvent.VK_LEFT:
                    p.set_left();
                    break;
                case KeyEvent.VK_RIGHT:
                    p.set_right();
                    break;
                case KeyEvent.VK_SPACE:
                    p.set_isjumping(false);
                    break;
                case KeyEvent.VK_ESCAPE:
                    restart = false;
                    break;
                default:
                    break;
            }
        }
    }
}
