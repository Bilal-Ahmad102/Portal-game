// import java.awt.Font;
// import java.awt.FontMetrics;
// import java.awt.Color;
// import java.awt.Dimension;
// import java.awt.Graphics;
// import java.awt.Image;
// import java.awt.Toolkit;
// import java.awt.event.ActionEvent;
// import java.awt.event.ActionListener;
// import java.awt.event.KeyAdapter;
// import java.awt.event.KeyEvent;
// import java.util.Random;
// import javax.swing.ImageIcon;
// import javax.swing.JPanel;
// import javax.swing.Timer;

// public class test extends JPanel implements ActionListener {

//     private final int B_WIDTH = 1300;
//     private final int B_HEIGHT = 710;
//     private final int DELAY = 10;
//     private int floor = (B_WIDTH / 20);
//     private Player player;
//     private int portal1_x = 1100;
//     private int portal1_y = 410;
//     private int portal2_x = 300;
//     private int portal2_y = 100;
//     private int apple_x = 200;
//     private int apple_y = 100;

//     private int max_enemies = 500;
//     private boolean[] enemy_chasing = new boolean[max_enemies];    
//     private boolean[] enemy_idle_movements = new boolean[max_enemies];    
//     private int[] enemy_x_pos = new int[max_enemies];
//     private int[] enemy_y_pos = new int[max_enemies];
//     private int on_ground_enemy = 10; 

//     private int[][] walls_pos = new int[600][2];
//     private int wall_lenght = floor;

//     private int floating_floor = 200;

//     public boolean restart = false;
//     private boolean justTeleported = false;

//     private int reset_player_x = 10;
//     private int reset_player_y = 650;
//     private int reset_enemys_num = on_ground_enemy;

//     private boolean inGame = true;

//     private Timer timer;
//     private Image wall;
//     private Image portal;
//     private Image apple;
//     private Image enemy;
//     private Image backgroundImage;

//     private long lastTime = System.nanoTime();
//     private final long flickerInterval = 500 * 1000000;
//     private boolean showPressSpaceMessage = true;

//     String player_path = "Portal-game/src/new_game_resources/player.png";
//     String walls_path = "Portal-game/src/new_game_resources/wall.png";
//     String portal_path = "Portal-game/src/new_game_resources/portal.png";
//     String apple_path = "Portal-game/src/new_game_resources/apple.png";
//     String enemy_path = "Portal-game/src/new_game_resources/enemy.jpeg";
//     String backgroundImage_path = "Portal-game/src/new_game_resources/bg.jpeg";

//     public test() {
//         initBoard();
//     }

//     private void initBoard() {
//         addKeyListener(new TAdapter());
//         setBackground(Color.black);
//         setFocusable(true);
//         setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
//         loadImages();
//         player = new Player( player_path, B_WIDTH, B_HEIGHT);
//         initGame();
//     }

//     private void loadImages() {
//         ImageIcon iiw = new ImageIcon(walls_path);
//         wall = iiw.getImage();

//         ImageIcon iip = new ImageIcon(portal_path);
//         portal = iip.getImage();

//         ImageIcon iiap = new ImageIcon(apple_path);
//         apple = iiap.getImage();

//         ImageIcon iien = new ImageIcon(enemy_path);
//         enemy = iien.getImage();

//         ImageIcon iibg = new ImageIcon(backgroundImage_path);
//         backgroundImage = iibg.getImage();
//     }

//     private void reset() {
//         player = new Player(reset_player_x, reset_player_y, player_path, B_WIDTH, B_HEIGHT);
//         on_ground_enemy = reset_enemys_num;
//         initGame();
//     }

//     private void initGame() {
//         for (int i = 0; i < on_ground_enemy; i++) {
//             if (i % 2 == 0) {
//                 enemy_idle_movements[i] = true;
//             } else {
//                 enemy_idle_movements[i] = false;
//             }
//         }

//         for (int i = 0; i < wall_lenght; i++) {
//             walls_pos[i][0] = i * 20;
//             walls_pos[i][1] = B_HEIGHT - 20;
//         }

//         wall_lenght += 20;
//         int m = 0;
//         for (int i = floor; i < wall_lenght; i++) {
//             walls_pos[i][0] = 900 + (20 * ++m);
//             walls_pos[i][1] = 510;
//         }

//         int n = 0;
//         for (int i = wall_lenght; i < wall_lenght + 10; i++) {
//             walls_pos[i][0] = floating_floor + (20 * ++n);
//             walls_pos[i][1] = 200;
//         }
//         wall_lenght += 10;

//         int l = 0;
//         for (int i = wall_lenght; i < wall_lenght + 10; i++) {
//             walls_pos[i][0] = 800 + (20 * ++l);
//             walls_pos[i][1] = 220;
//         }
//         wall_lenght += 10;

//         initialize_enemys();
//         timer = new Timer(DELAY, this);
//         timer.start();
//     }

//     @Override
//     public void paintComponent(Graphics g) {
//         super.paintComponent(g);
//         g.drawImage(backgroundImage, 0, 0, this);
//         doDrawing(g);
//     }

//     private void doDrawing(Graphics g) {
//         if (inGame) {
//             g.drawImage(apple, apple_x, apple_y, this);

//             if (on_ground_enemy != 0) {
//                 for (int i = 0; i < on_ground_enemy; i++) {
//                     g.drawImage(enemy, enemy_x_pos[i], enemy_y_pos[i], this);
//                 }
//             }

//             g.drawImage(portal, portal1_x, portal1_y, this);
//             g.drawImage(portal, portal2_x, portal2_y, this);

//             for (int i = 0; i < wall_lenght; i++) {
//                 g.drawImage(wall, walls_pos[i][0], walls_pos[i][1], this);
//             }

//             g.drawImage(player.getImage(), player.getX(), player.getY(), this);

//             Toolkit.getDefaultToolkit().sync();

//             if (!inGame) {
//                 gameOver(g);
//             }
//         }
//     }

//     private void gameOver(Graphics g) {
//         String msg = "Game Over";
//         Font small = new Font("Helvetica", Font.BOLD, 14);
//         FontMetrics metr = getFontMetrics(small);

//         g.setColor(Color.white);
//         g.setFont(small);
//         g.drawString(msg, (B_WIDTH - metr.stringWidth(msg)) / 2, B_HEIGHT / 2);
//     }

//     @Override
//     public void actionPerformed(ActionEvent e) {
//         inGame();

//         player.move();
//         // player.jump(walls_x_pos, wall_lenght);
//         update_enemys();

//         checkCollision();
//         checkTeleport();
//         checkApple();
//         checkFlicker();
//         repaint();
//     }

//     private void inGame() {
//         if (!inGame) {
//             timer.stop();
//         }
//     }

//     private void initialize_enemys() {
//         Random random = new Random();
//         for (int i = 0; i < on_ground_enemy; i++) {
//             enemy_x_pos[i] = random.nextInt(B_WIDTH - 100) + 50;
//             enemy_y_pos[i] = B_HEIGHT - 40;
//         }
//     }

//     private void update_enemys() {
//         for (int i = 0; i < on_ground_enemy; i++) {
//             if (enemy_chasing[i]) {
//                 if (player.getX() < enemy_x_pos[i]) {
//                     enemy_x_pos[i]--;
//                 } else if (player.getX() > enemy_x_pos[i]) {
//                     enemy_x_pos[i]++;
//                 }
//             } else {
//                 if (enemy_idle_movements[i]) {
//                     enemy_x_pos[i]++;
//                     if (enemy_x_pos[i] >= B_WIDTH) {
//                         enemy_x_pos[i] = 0;
//                     }
//                 } else {
//                     enemy_x_pos[i]--;
//                     if (enemy_x_pos[i] < 0) {
//                         enemy_x_pos[i] = B_WIDTH;
//                     }
//                 }
//             }
//         }
//     }

//     private void checkCollision() {
//         for (int i = 0; i < on_ground_enemy; i++) {
//             if (Math.abs(player.getX() - enemy_x_pos[i]) < 20 && Math.abs(player.getY() - enemy_y_pos[i]) < 20) {
//                 inGame = false;
//             }
//         }
//     }

//     private void checkTeleport() {
//         if (!justTeleported && Math.abs(player.getX() - portal1_x) < 20 && Math.abs(player.getY() - portal1_y) < 20) {
//             player.setX(portal2_x);
//             player.setY(portal2_y);
//             justTeleported = true;
//         } else if (!justTeleported && Math.abs(player.getX() - portal2_x) < 20 && Math.abs(player.getY() - portal2_y) < 20) {
//             player.setX(portal1_x);
//             player.setY(portal1_y);
//             justTeleported = true;
//         } else if (justTeleported && Math.abs(player.getX() - portal1_x) >= 20 && Math.abs(player.getY() - portal1_y) >= 20 && Math.abs(player.getX() - portal2_x) >= 20 && Math.abs(player.getY() - portal2_y) >= 20) {
//             justTeleported = false;
//         }
//     }

//     private void checkApple() {
//         if (Math.abs(player.getX() - apple_x) < 20 && Math.abs(player.getY() - apple_y) < 20) {
//             reset();
//         }
//     }

//     private void checkFlicker() {
//         long currentTime = System.nanoTime();
//         if (currentTime - lastTime >= flickerInterval) {
//             showPressSpaceMessage = !showPressSpaceMessage;
//             lastTime = currentTime;
//         }
//     }

//     private class TAdapter extends KeyAdapter {
//         @Override
//         public void keyReleased(KeyEvent e) {
//             int key = e.getKeyCode();

//             if (key == KeyEvent.VK_LEFT) {
//                 player.setLeftDirection(false);
//             }

//             if (key == KeyEvent.VK_RIGHT) {
//                 player.setRightDirection(false);
//             }

//             if (key == KeyEvent.VK_UP) {
//                 player.setUpDirection(false);
//             }

//             if (key == KeyEvent.VK_DOWN) {
//                 player.setDownDirection(false);
//             }
//         }

//         @Override
//         public void keyPressed(KeyEvent e) {
//             int key = e.getKeyCode();

//             if (key == KeyEvent.VK_LEFT) {
//                 player.setLeftDirection(true);
//             }

//             if (key == KeyEvent.VK_RIGHT) {
//                 player.setRightDirection(true);
//             }

//             if (key == KeyEvent.VK_UP) {
//                 player.setUpDirection(true);
//             }

//             if (key == KeyEvent.VK_DOWN) {
//                 player.setDownDirection(true);
//             }

//             if (key == KeyEvent.VK_SPACE) {
//                 player.startJump();
//             }

//             if (key == KeyEvent.VK_R) {
//                 reset();
//             }
//         }
//     }
// }
