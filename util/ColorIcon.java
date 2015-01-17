package util;
import javax.swing.*;
import java.awt.*;
/**
 *
 * This class produces the colored rectangles as icons for the Legend
 */

public class ColorIcon implements Icon
{
    private static int HEIGHT = 10;
    private static int WIDTH = 10;

    private Color color;

    public ColorIcon(Color color)
    {
        this.color = color;
    }

    public int getIconHeight()
    {
        return HEIGHT;
    }

    public int getIconWidth()
    {
        return WIDTH;
    }

    public void paintIcon(Component c, Graphics g, int x, int y)
    {
        g.setColor(color);
        g.fillRect(x, y, WIDTH - 1, HEIGHT - 1);

        g.setColor(Color.black);
        g.drawRect(x, y, WIDTH - 1, HEIGHT - 1);
    }
}