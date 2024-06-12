
import java.awt.Image;
import javax.swing.ImageIcon;


public class Walls {


    private final int B_WIDTH  = 1300;
    private final int B_HEIGHT = 710;
    private int floor = (B_WIDTH/20);

    private int[] walls_x_pos = new int[600];
    private int[] walls_y_pos = new int[600];
    private int wall_lenght = floor; 
    
    private int floating_floor = 200;

    private Image wall; 


    String walls_path  = "Portal-game/src/new_game_resources/wall.png";
    public Walls() {
        
        initBoard();

    }


    
    private void initBoard() {
        loadImages();
        initGame();
        
    }

    private void loadImages() {

        
        ImageIcon iiw = new ImageIcon(walls_path);
        wall = iiw.getImage();
    }
    
    public void initGame() {

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

        int l = 0; 
        for (int i = wall_lenght; i < wall_lenght+10; i++) {
            walls_x_pos[i] = 800 + (20 * ++l);
            walls_y_pos[i] = 220;
        }
        wall_lenght += 10;
        
    }
    public int[] get_wall_x(){
        return walls_x_pos;
    }
    public int[] get_walls_y(){
        return walls_y_pos;
    }
    public Image get_wall_Image(){
        return wall;
    }
    public int wall_lenght(){
        return wall_lenght;
    }

}