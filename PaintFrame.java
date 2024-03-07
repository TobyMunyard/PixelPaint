import javax.swing.*;

public class PaintFrame extends JFrame{
    
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
