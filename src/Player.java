import java.awt.Image;
import java.io.IOException;
import java.io.Serializable;
import javax.swing.ImageIcon;

public class Player implements Serializable {

    private static final long serialVersionUID = 1L;

    private final int B_WIDTH = 1300;
    private final int B_HEIGHT = 710;
    private int player_x = 10;
    private int player_y = 650;
    private int jumpCount = 0;

    public boolean restart = false;
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
    private boolean save_game;

    private transient Image player;

    String player_path = "Portal-game/src/new_game_resources/player.png";

    public Player() {
        initBoard();
    }

    private void initBoard() {
        loadImages();
    }

    private void loadImages() {
        ImageIcon iia = new ImageIcon(player_path);
        player = iia.getImage();
    }

    public void reset() {
        player_x = reset_player_x;
        player_y = reset_player_y;
    }

    public Image get_player_Image() {
        if (player == null) {
            loadImages(); // Reinitialize player image if it is null after deserialization
        }
        return player;
    }
    public void save_game(boolean c){
        this.save_game = c;
    }
    public boolean do_save_game(){
        return this.save_game;
    }


    public void set_player_pos() {
        player_x = reset_player_x;
        player_y = reset_player_y;
    }

    public int get_player_x() {
        return player_x;
    }

    public int get_player_y() {
        return player_y;
    }

    public void moves_left() {
        if (!rightDirection) {
            leftDirection = true;
            upDirection = false;
            downDirection = false;
        }
    }

    public void moves_right() {
        if (!leftDirection) {
            rightDirection = true;
            upDirection = false;
            downDirection = false;
        }
    }

    public void space_jump() {
        if (jumpCount < 1) {
            isJumping = true;
            verticalVelocity = jumpSpeed; // Set the initial upward speed
            jumpCount++; // Increment the jump count
        }
    }

    public void set_left() {
        leftDirection = false;
    }

    public void set_right() {
        rightDirection = false;
    }

    public void set_isjumping(boolean c) {
        isJumping = c;
    }

    public void move(int[] walls_x_pos, int[] walls_y_pos, int wall_length) {
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
        player_jump(walls_x_pos, walls_y_pos, wall_length);
    }

    public void player_jump(int[] walls_x_pos, int[] walls_y_pos, int wall_length) {
        for (int i = 0; i < wall_length; i++) {
            int floorX = walls_x_pos[i];
            int floorY = walls_y_pos[i];

            // Check if the player is on the floating_floor floor
            if (player_x >= floorX - 20 && player_x <= floorX + 20 && player_y >= floorY - 20 && player_y <= floorY) {
                // Player is on the floating_floor floor
                player_y = floorY - 20; // Adjust the player_y position
                jumpCount = 0;
                if (!isJumping) verticalVelocity = 0; // Stop vertical movement
            }
            if (player_x >= floorX - 20 && player_x <= floorX + 20 && player_y <= floorY + 20 && player_y >= floorY) {
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
    }

    public void checkCollision(int portal1_x, int portal1_y, int portal2_x, int portal2_y) {
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

        teleport(portal1_x, portal1_y, portal2_x, portal2_y);
        checkTeleportReset(portal1_x, portal1_y, portal2_x, portal2_y);
    }

    public boolean enemy_collision(int[] enemy_x_pos, int[] enemy_y_pos, int on_ground_enemy) {
        for (int i = 0; i < on_ground_enemy; i++) {
            // Check for collision between the enemy and the player
            int enemyWidth = 20; // Assuming the enemy is 20 units wide
            int enemyHeight = 20; // Assuming the enemy is 20 units tall

            if (player_x + 20 >= enemy_x_pos[i] && player_x <= enemy_x_pos[i] + enemyWidth &&
                    player_y + 20 >= enemy_y_pos[i] && player_y <= enemy_y_pos[i] + enemyHeight) {
                // Collision detected between the player and the enemy
                return true;
            }
        }
        return false;
    }

    public void teleport(int portal1_x, int portal1_y, int portal2_x, int portal2_y) {
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

    public void checkTeleportReset(int portal1_x, int portal1_y, int portal2_x, int portal2_y) {
        // Reset the justTeleported flag if the player is outside of the portal areas
        if (!(player_x >= portal1_x && player_x <= portal1_x + 60 && player_y >= portal1_y && player_y <= portal1_y + 100) &&
                !(player_x >= portal2_x && player_x <= portal2_x + 60 && player_y >= portal2_y && player_y <= portal2_y + 100)) {
            justTeleported = false;
        }
    }

    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        loadImages(); // Reinitialize transient fields
    }
}
