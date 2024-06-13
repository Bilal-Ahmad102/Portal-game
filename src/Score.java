import java.awt.*;
import java.io.Serializable;

public class Score implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private final int B_WIDTH = 1300;
    private int score = 0; // Add score variable

    public void increaseScore(int points) {
        score += points;
    }

    public int get_score(){
        return score;
    }

    public void reset() {
        score = 0; // Reset score
    }

    public void drawScoreboard(Graphics g) {
        g.setColor(new Color(255, 0, 0, 200)); // Red color with transparency
        g.setFont(new Font("Helvetica", Font.BOLD, 30));
        String scoreText = "Score: " + score;
        FontMetrics metrics = g.getFontMetrics();
        int x = (B_WIDTH - metrics.stringWidth(scoreText)) / 2;
        int y = metrics.getHeight() + 20;
        g.drawString(scoreText, x, y);
    }
}
