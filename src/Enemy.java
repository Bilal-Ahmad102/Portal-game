import java.awt.Image;
import java.util.Random;

import javax.swing.ImageIcon;


public class Enemy  {

    private int max_enemies = 500;
    private boolean[] enemy_chasing = new boolean[max_enemies];    
    private boolean[] enemy_idle_movements = new boolean[max_enemies];    
    private int[] enemy_x_pos = new int[max_enemies];
    private int[] enemy_y_pos = new int[max_enemies];
    private int   on_ground_enemy = 10; 

    private int reset_enemys_num = on_ground_enemy;

    private Image enemy;

    
    String enemy_path  = "Portal-game/src/new_game_resources/enemy.jpeg";
    public Enemy(int player_x,int B_WIDTH,int B_HEIGHT) {
        
        initBoard(player_x,B_WIDTH,B_HEIGHT);

    }


    
    private void initBoard(int player_x,int B_WIDTH,int B_HEIGHT) {
        loadImages();
        initGame(player_x,B_WIDTH,B_HEIGHT);
        
    }

    private void loadImages() {

        ImageIcon iien = new ImageIcon(enemy_path);
        enemy = iien.getImage();

    }

    public void reset(){
        on_ground_enemy = reset_enemys_num;
    }

    public Image get_enemy_Image(){
        return enemy;
    }
    public int[] get_enemy_x(){
        return enemy_x_pos;
    }
    public int[] get_enemy_y(){
        return enemy_y_pos;
    }
    public int get_on_ground_enemys(){
        return on_ground_enemy;
    }    
    public void initGame(int player_x,int B_WIDTH,int B_HEIGHT) {
        for (int i = 0; i < on_ground_enemy; i++) {
            if(i%2==0){
                enemy_idle_movements[i] = true;
            }else{
                enemy_idle_movements[i] = false;
            }
        }
        

        initialize_enemys( player_x,B_WIDTH,B_HEIGHT);
    }



    public void move(int player_x,int player_y,int B_WIDTH,int B_HEIGHT) {
        if (player_y >= enemy_y_pos[0]-40) {
            move_enemy(player_x,player_y,B_HEIGHT);    
        }
        idle_enemy(B_WIDTH);
        
    }

    private void idle_enemy(int B_WIDTH){
        for (int i = 0; i < on_ground_enemy; i++) {
            for (int j = 0; j < on_ground_enemy; j++) {
                if (enemy_x_pos[i]<0 || enemy_x_pos[j]<0) continue;
                
                int enemy_1_right = enemy_x_pos[i]+20;
                int enemy_2_right = enemy_x_pos[j]+20;

                if (enemy_1_right==enemy_x_pos[j]){
                    enemy_idle_movements[i] = false;
                    enemy_idle_movements[j] = true;
                }
                else if(enemy_x_pos[i]==enemy_2_right){
                    enemy_idle_movements[i]= true;
                    enemy_idle_movements[j] = false;
                }
            
            }
            if (enemy_x_pos[i]<=0) enemy_idle_movements[i] = true; 
            else if(enemy_x_pos[i]>=B_WIDTH-20)enemy_idle_movements[i]= false;

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
        enemy_x_pos[i] = -20;
        enemy_y_pos[i] = -20;
    
    }
    private void move_enemy(int player_x,int player_y,int B_HEIGHT) {
        // Set the movement speed of the enemies (adjustable value)
        int enemySpeed = 2;
        
        // Set the interaction radius within which enemies should move towards the player
        int interactionRadius = 100;
        
        // Iterate through each enemy on the ground
        for (int i = 0; i < on_ground_enemy; i++) {
            // Calculate the horizontal distance between the player and the enemy
            int distance = player_x - enemy_x_pos[i];
            
            if (player_y == B_HEIGHT - 40) {
                // Only chase player when they are on the ground
                if (Math.abs(distance) <= interactionRadius) {
                    // Move the enemy towards the player based on the distance
                    if (distance > 0) {
                        enemy_x_pos[i] += enemySpeed;
                        enemy_chasing[i] = true;
                    } else if (distance < 0) {
                        enemy_x_pos[i] -= enemySpeed;
                        enemy_chasing[i] = true;
                    }
                }
            }else{
                enemy_chasing[i] = false;
            }
        
        }
    }
    
    
    private void initialize_enemys(int player_x,int B_WIDTH,int B_HEIGHT) {
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
    private void kill(int player_x,int player_y){
        for (int i = 0; i < on_ground_enemy; i++) {
            if (player_x + 20 >= enemy_x_pos[i] && player_x <= enemy_x_pos[i] + 20  
                && ((player_y <= enemy_y_pos[i]-20) && player_y >= enemy_y_pos[i]-30 )) {
                // Collision detected between the player and the enemy
                    remove_enemy(i);
            }
        }
    }   
    public void checkCollision(int player_x,int player_y) {

        kill(player_x,player_y);

    }

}