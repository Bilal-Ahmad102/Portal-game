import java.awt.Dimension;
import java.awt.EventQueue;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class Portal extends JFrame {

    public Portal() {
        
        initUI();
    }
    private void initUI() {

        JPanel panel = new JPanel();
        panel.setPreferredSize( new Dimension( 640, 480 ) );
        getContentPane().add(panel);
        
        
    
        JButton singlePlayerButton = new JButton("Single Player");
        singlePlayerButton.addActionListener(e -> {
            add(new Board());
            getContentPane().remove(panel);            
            pack();   
        });
        
        panel.add(singlePlayerButton);

        setTitle("Portal");
        setSize(600,600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    

    public static void main(String[] args) {
        
        EventQueue.invokeLater(() -> {
            JFrame ex = new Portal();
            ex.setVisible(true);
        });
    }
}
