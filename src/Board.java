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
import java.util.Arrays;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;


public class Board extends JPanel implements ActionListener {


    private final int B_WIDTH  = 1300;
    private final int B_HEIGHT = 710;
    private final int DELAY    = 10;
    private int floor = (B_WIDTH/20);
    private int player_x = 10;
    private int player_y = 650;
    private int portal1_x = 1100;
    private int portal1_y = 410;
    private int portal2_x = 300;
    private int portal2_y = 100;
    private int apple_x = 200;
    private int apple_y = 100;

    private boolean[] enemy_chasing = new boolean[10];    
    private boolean[] enemy_idle_movements = new boolean[10];    
    private int[] enemy_x_pos = new int[10];
    private int[] enemy_y_pos = new int[10];
    private int   on_ground_enemy = 3; 

    private int[] walls_x_pos = new int[600];
    private int[] walls_y_pos = new int[600];
    private int wall_lenght = floor; 
    
    private int jumpCount = 0;
    private int floating_floor = 200;

    public boolean level_2 = false;
    private boolean justTeleported = false;
    private boolean leftDirection = false;
    private boolean rightDirection = false;
    private boolean upDirection = false;
    private boolean downDirection = false;
    private boolean isJumping = false; // Flag to track whether the player is jumping
    private double jumpSpeed = -5.0; // Initial jump speed (negative value for upward movement)
    private double gravity = 0.1; // Gravity value for downward movement
    private double verticalVelocity = 0.0; // Tracks the player's vertical velocity
            

    private int reset_player_x = player_x;
    private int reset_player_y = player_y;
    private int reset_enemys_num = on_ground_enemy;

    private boolean inGame = true;

    private Timer timer;
    private Image player;
    private Image wall; 
    private Image portal;
    private Image apple;
    private Image enemy;

    private long lastTime = System.nanoTime();
    private final long flickerInterval = 500 * 1000000;
    private boolean showPressSpaceMessage = true;


    String player_path = "Portal_game/src/new_game_resources/player.png";
    String walls_path  = "Portal_game/src/new_game_resources/wall.png";
    String portal_path = "Portal_game/src/new_game_resources/portal.png";
    String apple_path  = "Portal_game/src/new_game_resources/apple.png";
    String enemy_path  = "Portal_game/src/new_game_resources/enemy.jpeg";

    public Board() {
        
        initBoard();

    }


    
    private void initBoard() {

        addKeyListener(new TAdapter());
        setBackground(Color.black);
        setFocusable(true);

        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
        loadImages();
        initGame();
        
    }

    private void loadImages() {

        ImageIcon iia = new ImageIcon(player_path);
        player = iia.getImage();

        ImageIcon iiw = new ImageIcon(walls_path);
        wall = iiw.getImage();

        ImageIcon iip = new ImageIcon(portal_path);
        portal = iip.getImage();

        ImageIcon iiap = new ImageIcon(apple_path);
        apple = iiap.getImage();

        ImageIcon iien = new ImageIcon(enemy_path);
        enemy = iien.getImage();


    }
    private void reset(){
        player_x = reset_player_x;
        player_y = reset_player_y;
        on_ground_enemy = reset_enemys_num;
        initGame();
    }
    private void initGame() {
        for (int i = 0; i < on_ground_enemy; i++) {
            if(i%2==0){
                enemy_idle_movements[i] = true;
            }else{
                enemy_idle_movements[i] = false;
            }
        }
        

        for (int i = 0; i < wall_lenght; i++) {
            walls_x_pos[i] = i * 20;
            walls_y_pos[i] = B_HEIGHT - 20;
        }
    
        wall_lenght += 20;
        int m = 0;
        for (int i = floor; i < wall_lenght; i++) {
            walls_x_pos[i] = 900 + (20 * ++m);
            walls_y_pos[i] = 510;
        }
    
        int n = 0; 
        for (int i = wall_lenght; i < wall_lenght+10; i++) {
            walls_x_pos[i] = floating_floor + (20 * ++n);
            walls_y_pos[i] = 200;
        }
        wall_lenght += 10;

        initialize_enemys();
        timer = new Timer(DELAY,this);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        doDrawing(g);
    }

    private void doDrawing(Graphics g) {
        
        if (inGame) {
            g.drawImage(apple, apple_x, apple_y,this);     
            if(on_ground_enemy!=0){
                for(int i = 0 ;i<on_ground_enemy;i++){
                    g.drawImage(enemy, enemy_x_pos[i], enemy_y_pos[i], this);
                }
            }

            for (int i = 0; i < wall_lenght; i++) {
                g.drawImage(wall, walls_x_pos[i], walls_y_pos[i],this);
            }

            g.drawImage(portal, portal2_x, portal2_y, this);
            g.drawImage(portal, portal1_x, portal1_y, this);
            g.drawImage(player, player_x, player_y, this);
            
            int p_x ;
            int p_y ;

            if (player_x>= portal1_x && player_x<= portal1_x+40 && player_y<=portal1_y+80 && player_y>=portal1_y) {
                p_x = portal2_x + (player_x - portal1_x);
                p_y = portal2_y + (player_y - portal1_y);
                g.drawImage(player,p_x ,p_y,this);
            }
            if (player_x>= portal2_x && player_x<= portal2_x+40 && player_y<=portal2_y+80 && player_y>=portal2_y) {
                p_x = portal1_x + (player_x - portal2_x);
                p_y = portal1_y + (player_y - portal2_y);
                g.drawImage(player,p_x ,p_y,this);
            }
            
            
            Toolkit.getDefaultToolkit().sync();  
            
            // Synchronizes the graphics state across different platforms to ensure smooth 
            // rendering
        }else{
            gameOver(g);
        }
    }
    private void move() {
        if (leftDirection) {
            player_x -= 1;
        }

        if (rightDirection) {
            player_x += 1;
        }

        if (upDirection) {
            player_y -= 1;
        }

        if (downDirection) {
            player_y += 1;
        }
        player_jump();
        if (player_y >= enemy_y_pos[0]-40) {
            move_enemy();    
        }
        idle_enemy();
        
    }

    private void idle_enemy(){
        for (int i = 0; i < on_ground_enemy; i++) {
            if (enemy_x_pos[i]<=0) enemy_idle_movements[i] = true; 
            else if(enemy_x_pos[i]>=B_WIDTH)enemy_idle_movements[i]= false;

            if(!enemy_chasing[i]){
                if (enemy_idle_movements[i]){
                    enemy_x_pos[i]++;
                }else{
                    enemy_x_pos[i]--;
                }
            } 
        }
    }

    private void remove_enemy(int i){
        for (int j = i; j < on_ground_enemy; j++) {
            enemy_x_pos[i] = enemy_x_pos[i+1];
            enemy_y_pos[i] = enemy_y_pos[i+1];
        }
        on_ground_enemy--;
    }
    private void player_jump(){
        for(int i=0;i<wall_lenght;i++) {
            int floorX = walls_x_pos[i];
            int floorY = walls_y_pos[i];
    
            // Check if the player is on the floating_floor floor
            if (player_x >= floorX - 20 && player_x <= floorX + 20 && player_y >= floorY - 20 && player_y <= floorY) {
                // Player is on the floating_floor floor
                player_y = floorY - 20; // Adjust the player_y position
                jumpCount = 0;
                if(!isJumping)verticalVelocity = 0; // Stop vertical movement
            }
            if(player_x >= floorX-20 && player_x <= floorX + 20 && player_y <= floorY + 20 && player_y >= floorY){
                player_y = floorY + 20;
                jumpCount = 1;
            }
        }
        // Jump logic
        if (isJumping) {
            // Apply upward velocity
            player_y += verticalVelocity;
            // Gradually decrease upward velocity (gravity effect)
            verticalVelocity += gravity;
            
            // Check if the jump has peaked
            if (verticalVelocity >= 0) {
                isJumping = false; // Stop jumping once upward movement ends
            }
        } else {
            // Apply gravity when not jumping
            if (player_y < B_HEIGHT - 40) {
                verticalVelocity += gravity;
                player_y += verticalVelocity;
            }
        }
        // Reset the jump count if the player lands back on the ground
        if (player_y >= B_HEIGHT - 40) {
            jumpCount = 0; // Reset jump count
            verticalVelocity = 0.0; // Reset vertical velocity
        }

    }private void move_enemy() {
        // Set the movement speed of the enemies (adjustable value)
        int enemySpeed = 2;
        
        // Set the interaction radius within which enemies should move towards the player
        int interactionRadius = 100;
        
        // Iterate through each enemy on the ground
        for (int i = 0; i < on_ground_enemy; i++) {
            // Calculate the horizontal distance between the player and the enemy
            int distance = player_x - enemy_x_pos[i];
            
            // Check if the enemy is within the interaction radius of the player
            if (Math.abs(distance) <= interactionRadius) {
                // Move the enemy towards the player based on the distance
                if (distance > 0) {
                    // Player is to the right of the enemy; move enemy right
                    enemy_x_pos[i] += enemySpeed;
                    enemy_chasing[i] = true;
                } else if (distance < 0) {
                    // Player is to the left of the enemy; move enemy left
                    enemy_x_pos[i] -= enemySpeed;
                    enemy_chasing[i] = true;
                }
                
                // Ensure the enemy stays within the game boundaries (B_WIDTH)
                if (enemy_x_pos[i] < 0) {
                    enemy_x_pos[i] = 0;
                } else if (enemy_x_pos[i] > B_WIDTH - 20) {
                    enemy_x_pos[i] = B_WIDTH - 20;
                }
            }else{
                enemy_chasing[i] = false;
            }
        
        }
    }
    
    
    private void initialize_enemys() {
        // Random number generator for enemy movement direction
        Random random = new Random();
        int rangeSize = B_WIDTH-20 - 20 + 1; // int rangeSize = max - min + 1;
        // Loop through each enemy to manage its behavior
        for (int i = 0; i < on_ground_enemy; i++) {
            // Randomly decide the enemy's initial position
            int x_position = random.nextInt(rangeSize) + 20; //random.nextInt(rangeSize) + min;

            while (Math.abs(x_position-player_x) < 150) {
                x_position = random.nextInt(rangeSize) + 20; //random.nextInt(rangeSize) + min;

            }
    
            // Update the enemy's x position array based on the chosen intial position
            enemy_x_pos[i] = x_position;
            enemy_y_pos[i] = B_HEIGHT-40;
    

        }
    }
    private void kill(){
        for (int i = 0; i < on_ground_enemy; i++) {
            if (player_x + 20 >= enemy_x_pos[i] && player_x <= enemy_x_pos[i] + 20  
                && ((player_y <= enemy_y_pos[i]-20) && player_y >= enemy_y_pos[i]-30 )) {
                // Collision detected between the player and the enemy
                    remove_enemy(i);
            }
        }
    }        
    private void checkCollision() {
        if (player_y >= B_HEIGHT - 20) {
            // Reset the player's position if they hit the ground
            player_y = B_HEIGHT - 20;
            verticalVelocity = 0.0; // Reset vertical velocity
        }

        if (player_y >= B_HEIGHT) {
            player_y = B_HEIGHT;
        }

        if (player_y < 0) {
            player_y = 0;
        }

        if (player_x >= B_WIDTH) {
            player_x = 0;
        }
        if (player_x < 0) {
            player_x = B_WIDTH;
        }
        
        teleport();
        checkTeleportReset();
        kill();
        for (int i = 0; i < on_ground_enemy; i++) {
            // Check for collision between the enemy and the player
            int enemyWidth  = 20; // Assuming the enemy is 20 units wide
            int enemyHeight = 20; // Assuming the enemy is 20 units tall
            
            if (player_x + 20 >= enemy_x_pos[i] && player_x <= enemy_x_pos[i] + enemyWidth &&
                player_y + 20 >= enemy_y_pos[i] && player_y <= enemy_y_pos[i] + enemyHeight) {
                // Collision detected between the player and the enemy
                inGame = false; // End the game or apply damage as desired
                timer.stop();
                 // Stop the game timer
                break; // Exit the loop as the game is over
            }
        }
        if (!inGame) {
            timer.stop();
        }
    }
    
    private void teleport() {
        // Check if the player has just teleported; if true, do not teleport again
        if (justTeleported) {
            return;
        }
    
        boolean isportling = false;
        boolean isportling1 = false;
    
        if (player_x >= portal1_x && player_x <= portal1_x + 60 && player_y >= portal1_y && player_y <= portal1_y + 100) {
            isportling = true;
        }
    
        if (player_x >= portal2_x && player_x <= portal2_x + 60 && player_y >= portal2_y && player_y <= portal2_y + 100) {
            isportling1 = true;
        }
    
        // Teleport player from portal1 to portal2
        if (isportling && player_x >= portal1_x && player_x <= portal1_x + 60) {
            player_x = portal2_x + (player_x - portal1_x);
            player_y = portal2_y + (player_y - portal1_y);
            justTeleported = true;
        
        }
    
        // Teleport player from portal2 to portal1
        if (isportling1 && player_x >= portal2_x && player_x <= portal2_x + 60) {
            player_x = portal1_x + (player_x - portal2_x);
            player_y = portal1_y + (player_y - portal2_y);
            justTeleported = true;
        }
    }
    
    private void checkTeleportReset() {
        // Reset the justTeleported flag if the player is outside of the portal areas
        if (!(player_x >= portal1_x && player_x <= portal1_x + 60 && player_y >= portal1_y && player_y <= portal1_y + 100) &&
            !(player_x >= portal2_x && player_x <= portal2_x + 60 && player_y >= portal2_y && player_y <= portal2_y + 100)) {
            justTeleported = false;
        }
    }
    

    private void gameOver(Graphics g) {
        String gameOverMsg = "Game Over";
        String pressSpaceMsg = "Press Space to Restart";
        Font smallFont = new Font("Vibes", Font.BOLD, 30);
        Font pressSpaceFont = new Font("Vibes", Font.PLAIN, 20); // Adjust the font size as needed
        FontMetrics metrics = getFontMetrics(smallFont);
    
        // Draw the "Game Over" message
        g.setColor(Color.red);
        g.setFont(smallFont);
        g.drawString(gameOverMsg, (B_WIDTH - metrics.stringWidth(gameOverMsg)) / 2, B_HEIGHT / 2);
    
        // Calculate time elapsed since last flicker change
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
                    (B_HEIGHT / 2) + pressSpaceMetrics.getHeight() + 10); // Adjust the position as needed
        }
    
        repaint(); // Trigger repaint to update the display

    }
    
    @Override
    public void actionPerformed(ActionEvent e) {

        if (inGame) {
            checkCollision();

            move();

        }

        repaint();
    }

    private class TAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
    
            // Set the directions based on holding a key
            if (key == KeyEvent.VK_LEFT) {
                if (!rightDirection) {
                    leftDirection = true;
                    upDirection = false;
                    downDirection = false;
                }
            } else if (key == KeyEvent.VK_RIGHT) {
                if (!leftDirection) {
                    rightDirection = true;
                    upDirection = false;
                    downDirection = false;
                }
            } else if (key == KeyEvent.VK_UP) {
                if (!downDirection) {
                    upDirection = true;
                    rightDirection = false;
                    leftDirection = false;
                }
            } else if (key == KeyEvent.VK_DOWN) {
                if (!upDirection) {
                    downDirection = true;
                    rightDirection = false;
                    leftDirection = false;
                }
            } else if (key == KeyEvent.VK_SPACE) {
                if (!inGame) {
                    reset();
                    inGame=true;
                }            
                if (jumpCount < 1) {
                    isJumping = true;
                    verticalVelocity = jumpSpeed; // Set the initial upward speed
                    jumpCount++; // Increment the jump count
                }
        }
    }
        @Override
        public void keyReleased(KeyEvent e) {
            int key = e.getKeyCode();
        
            // Check the key code of the released key and update the corresponding flag
            switch (key) {
                case KeyEvent.VK_LEFT:
                    // Reset left direction flag
                    leftDirection = false;
                    break;
                case KeyEvent.VK_RIGHT:
                    // Reset right direction flag
                    rightDirection = false;
                    break;
                case KeyEvent.VK_UP:
                    // Reset up direction flag
                    upDirection = false;
                    break;
                case KeyEvent.VK_DOWN:
                    // Reset down direction flag
                    downDirection = false;
                    break;
                case KeyEvent.VK_SPACE:
                    // Reset jump flag
                    isJumping = false;
                    break;
                default:
                    // Handle other key releases if necessary
                    break;
            }
        }
            }
}
