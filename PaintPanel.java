import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.*;

public class PaintPanel extends JPanel implements ActionListener {

    // data fields
    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static final int DELAY = 75;
    static final int SCREEN_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
    final int[] x = new int[SCREEN_UNITS];
    final int[] y = new int[SCREEN_UNITS];
    int mouseX, mouseY;
    boolean userClicked = false;
    boolean bucketToolEnabled = false;
    boolean showLines = true;
    ArrayList<Rectangle> paintedStuff = new ArrayList<Rectangle>();
    Timer timer;
    Color color = Color.BLACK;

    PaintPanel() {
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT + 35));
        this.setBackground(Color.white);
        this.setFocusable(true);
        this.addMouseListener(new MyMouseAdapter());
        JPanel buttonPanel = createButtonPanel();
        this.setLayout(new BorderLayout());
        this.add(buttonPanel, BorderLayout.SOUTH);
        startPaint();
    }

    public JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel();
        JButton colourButton = new JButton("Change Colour");
        JButton saveButton = new JButton("Save as Image");
        JButton bucketButton = new JButton("Bucket Tool");
        JButton showLinesButton = new JButton("Show Lines");
        buttonPanel.add(colourButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(bucketButton);
        buttonPanel.add(showLinesButton);
        colourButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color newColor = JColorChooser.showDialog(buttonPanel, "Choose Color", Color.RED);
                if (newColor != null) {
                    color = newColor;
                }
            }
        });
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveImage(SCREEN_WIDTH, SCREEN_HEIGHT);
            }
        });
        bucketButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (bucketToolEnabled) {
                    bucketToolEnabled = false;
                } else {
                    bucketToolEnabled = true;
                }
                repaint();
            }
        });
        showLinesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (showLines) {
                    showLines = false;
                } else {
                    showLines = true;
                }
            }
        });
        return buttonPanel;
    }

    public void startPaint() {
        timer = new Timer(DELAY, this);
        timer.start();
    }

    /**
     * Calls the draw method that draws the content of the screen.
     * 
     * @param Graphics object to draw.
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    /**
     * Draws lines on screen based on UNIT_SIZE, allows user to draw
     * colors of their choice inside the units.
     * 
     * @param g Graphics object to draw.
     */
    public void draw(Graphics g) {
        for (Rectangle rect : paintedStuff) {
            g.setColor(rect.getColor());
            g.fillRect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
        }
        if (showLines) {
            g.setColor(Color.GRAY);
            for (int i = 0; i < SCREEN_WIDTH; i += UNIT_SIZE) {
                g.drawLine(i, 0, i, SCREEN_HEIGHT);
            }
            for (int j = 0; j < SCREEN_HEIGHT; j += UNIT_SIZE) {
                g.drawLine(0, j, SCREEN_WIDTH, j);
            }
        }
    }

    /**
     * Runs methods needed to make paint run, repaints the screen
     * to reflect what is happening.
     * 
     * @param e An ActionEvent to indicate something is happening.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    public void saveImage(int width, int height) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();
        paint(g2d);
        g2d.dispose();
        BufferedImage croppedImage = cropImage(image, 0, 40, image.getWidth(), image.getHeight() - 40);

        // Save the image to a file
        try {
            File output = new File("panel_image.png");
            ImageIO.write(croppedImage, "png", output);
            System.out.println("Image saved successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static BufferedImage cropImage(BufferedImage originalImage, int x, int y, int width, int height) {
        // Ensure crop area is within bounds
        if (x < 0 || y < 0 || width <= 0 || height <= 0 || x + width > originalImage.getWidth()
                || y + height > originalImage.getHeight()) {
            throw new IllegalArgumentException("Invalid crop dimensions");
        }

        // Create a new BufferedImage with the cropped dimensions
        BufferedImage croppedImage = new BufferedImage(width, height, originalImage.getType());

        // Copy the cropped portion of the original image to the new BufferedImage
        Graphics2D g2d = croppedImage.createGraphics();
        g2d.drawImage(originalImage.getSubimage(x, y, width, height), 0, 0, null);
        g2d.dispose();

        return croppedImage;
    }

    public void paintBackground(Color color) {
        super.setBackground(color);
    }

    public class MyMouseAdapter extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            mouseX = e.getX();
            mouseY = e.getY();
            // Calculate the closest unit
            int closestX = (mouseX / UNIT_SIZE) * UNIT_SIZE;
            int closestY = (mouseY / UNIT_SIZE) * UNIT_SIZE;
            if (!bucketToolEnabled) {
                Rectangle rect = new Rectangle(closestX, closestY, UNIT_SIZE, UNIT_SIZE, color);
                paintedStuff.add(rect);
            } else {
                paintBackground(color);
            }
            repaint();
        }

    }

}
