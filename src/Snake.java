
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Snake extends JPanel implements ActionListener {

    private final int B_WIDTH = 1300;
    private final int B_HEIGHT = 710;
    private final int DOT_SIZE = 10;
    private final int ALL_DOTS = 900;
    public final int DELAY = 7;

    public final int x[] = new int[ALL_DOTS];
    public final int y[] = new int[ALL_DOTS];

    public int dots;

    private boolean leftDirection = false;
    private boolean rightDirection = false;
    private boolean upDirection = false;
    private boolean downDirection = true;



    private Timer timer;
    private Image ball;
    private Image head;

    String dot_path = "Portal-game\\src\\new_game_resources\\dot.png";
    String head_path = "Portal-game\\src\\new_game_resources\\head.png";

    public Snake() {
        initBoard();
    }

    private void initBoard() {
        loadImages();
        initGame();
    }
    
    public void setTimer(Timer t) {
        this.timer = t;
    }

    private void loadImages() {
        ImageIcon iid = new ImageIcon(dot_path);
        ball = iid.getImage();

        ImageIcon iih = new ImageIcon(head_path);
        head = iih.getImage();
    }

    public void initGame() {
        dots = 3;
        for (int z = 0; z < dots; z++) {
            x[z] = 150 - z * 10;
            y[z] = 400;
        }
        

        timer = new Timer(DELAY, this);
        timer.start();
        
    }


    public void doDrawing(Graphics g) {
        
        for (int z = 0; z < dots; z++) {
            if (z == 0) {
                g.drawImage(head, x[z], y[z], this);
            } else {
                g.drawImage(ball, x[z], y[z], this);
            }
        }
            // Synchronizes the graphics state across different platforms to ensure smooth rendering
        
    }

    public void move() {
        for (int z = dots; z > 0; z--) {
            x[z] = x[(z - 1)];
            y[z] = y[(z - 1)];
        }
        if (leftDirection) {
            x[0] -= DOT_SIZE;
        }
        if (rightDirection) {
            x[0] += DOT_SIZE;
        }
        if (upDirection) {
            y[0] -= DOT_SIZE;
        }
        if (downDirection) {
            y[0] += DOT_SIZE;
        }
    }

    public boolean checkCollision() {
        for (int z = dots; z > 0; z--) {
            if ((z > 4) && (x[0] == x[z]) && (y[0] == y[z])) {
                return true;
            }
        }
        if (y[0] >= B_HEIGHT) {
            y[0] = 0;
        }
        if (y[0] < 0) {
            y[0] = B_HEIGHT;
        }
        if (x[0] >= B_WIDTH) {
            x[0] = 0;
        }
        if (x[0] < 0) {
            x[0] = B_WIDTH;
        }
        return false;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        
        // checkCollision();
            
        
        // repaint();
    }
    public void moves_left() {
        leftDirection = true;
        upDirection = false;
        downDirection = false;
    }

    public void moves_Right() {
        rightDirection = true;
        upDirection = false;
        downDirection = false;
    }

    public void moves_Up() {
        upDirection = true;
        rightDirection = false;
        leftDirection = false;
    }

    public void moves_Down() {
        downDirection = true;
        rightDirection = false;
        leftDirection = false;
    }

    public boolean get_left() {
        return leftDirection;
    }

    public boolean get_right() {
        return rightDirection;
    }

    public boolean get_up() {
        return upDirection;
    }

    public boolean get_down() {
        return downDirection;
    }


}