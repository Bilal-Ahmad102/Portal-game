import java.awt.Image;
import javax.swing.ImageIcon;


public class Portals{


    private int portal1_x = 1100;
    private int portal1_y = 410;
    private int portal2_x = 300;
    private int portal2_y = 100;
    private Image portal;
    String portal_path = "Portal-game/src/new_game_resources/portal.png";
    public Portals() {
        loadImages();    
    }
    public int[] mirror_image(int player_x, int player_y) {
        int p_x;
        int p_y;
        
        // Check if player is within the bounds of portal1
        if (player_x >= portal1_x && player_x <= portal1_x + 40 && player_y <= portal1_y + 80 && player_y >= portal1_y) {
            p_x = portal2_x + (player_x - portal1_x);
            p_y = portal2_y + (player_y - portal1_y);
            int[] pos = new int[2];
            pos[0] = p_x; // Corrected index to 0
            pos[1] = p_y; // Corrected index to 1
            return pos;
        }
    
        // Check if player is within the bounds of portal2
        if (player_x >= portal2_x && player_x <= portal2_x + 40 && player_y <= portal2_y + 80 && player_y >= portal2_y) {
            p_x = portal1_x + (player_x - portal2_x);
            p_y = portal1_y + (player_y - portal2_y);
            int[] pos = new int[2];
            pos[0] = p_x; // Corrected index to 0
            pos[1] = p_y; // Corrected index to 1
            return pos;
        }
    
        // Return the original position if the player is not in any portal
        return new int[] { player_x, player_y };
    }
    
    private void loadImages() {
        ImageIcon iip = new ImageIcon(portal_path);
        portal = iip.getImage();
    }
    public Image get_portal_Image(){
        return portal;
    }
    public int[] get_portals_pos(){
        return new int[] {portal1_x,portal1_y,portal2_x,portal2_y};
    }
}