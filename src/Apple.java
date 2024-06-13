import java.awt.Image;
import java.io.IOException;
import java.io.Serializable;
import java.util.Random;

import javax.swing.ImageIcon;


public class Apple implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private final int B_WIDTH = 1300;
    private final int B_HEIGHT = 710;
    private int apple_x = 200;
    private int apple_y = 100;
    private transient Image apple;
    private String apple_path = "Portal-game/src/new_game_resources/apple.png";

    public Apple() {
        initBoard();
    }

    private void initBoard() {
        loadImages();
    }

    private void loadImages() {
        ImageIcon iiap = new ImageIcon(apple_path);
        apple = iiap.getImage();
    }

    public Image getAppleImage() {
        if (apple == null) {
            loadImages(); // Reinitialize apple image if it is null after deserialization
        }
        return apple;
    }

    public int get_apple_x() {
        return apple_x;
    }

    public int get_apple_y() {
        return apple_y;
    }

    public int apple_check(int player_x, int player_y, int[] walls_x_pos, int[] walls_y_pos, int wall_length) {
        if (apple_x <= player_x + 20 && apple_x + 10 >= player_x && apple_y <= player_y + 20 && apple_y + 10 >= player_y) {
            Random r = new Random();
            int rangeSize_x = B_WIDTH - 40;
            int rangeSize_y = B_HEIGHT - 40;

            apple_x = r.nextInt(rangeSize_x) + 20;
            apple_y = r.nextInt(rangeSize_y) + 20;

            boolean validPosition;
            do {
                validPosition = true;
                for (int i = 0; i < wall_length; i++) {
                    if (Math.abs(walls_x_pos[i] - apple_x) <= 40 && Math.abs(walls_y_pos[i] - apple_y) <= 40) {
                        apple_x = r.nextInt(rangeSize_x) + 20;
                        apple_y = r.nextInt(rangeSize_y) + 20;
                        validPosition = false;
                        break;
                    }
                }
            } while (!validPosition);

            return 1;
        }
        return 0;
    }

    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        loadImages(); // Reinitialize transient fields
    }
}
