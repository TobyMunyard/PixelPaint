import javax.swing.*;

/**
 * Represents the main frame of the paint application.
 * @author Toby Munyard
 */
public class PaintFrame extends JFrame{
    
    /**
     * Constructs a new PaintFrame and initializes its components.
     */
    PaintFrame(){
        PaintPanel paintPanel = new PaintPanel();
        this.add(paintPanel);
        this.setTitle("Paint");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }
}

