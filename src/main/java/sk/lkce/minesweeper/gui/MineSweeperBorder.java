package sk.lkce.minesweeper.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.border.Border;

/**
 * A custom implementation of border used in the game view components which
 * copy the Windows XP Minesweeper look. The border has raised border look
 * and has given width and one color used for top and left side and a second
 * color for bottom and right sides.
 */
public class MineSweeperBorder implements Border{

    private int borderWidth;
    private Color topLeftColor,bottomRightColor;

    
    /**
     * Constructs a mine sweeper border with given two colours and width. 
     * @param width the width of the border
     * @param topLeft the colour of the top and left side of the border
     * @param bottomRight the colour of the bottom and right side of the border
     */
    public MineSweeperBorder(int width, Color topLeft, Color bottomRight){
        borderWidth = width;
        topLeftColor = topLeft;
        bottomRightColor = bottomRight;
    }
    
    
    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width,
            int height) {
    	
        g.setColor(topLeftColor);
        for (int i = 0; i < borderWidth;i ++){
            g.drawLine(x, y + i, x + width  - 1 - 1 - i, y + i); //Horizontal line on top
            g.drawLine(x + i, y, x + i, y + height - 1 - 1 -i); //Vertical line on the left
        }
    	
    	
        g.setColor(bottomRightColor);
        for (int i = 0; i < borderWidth; i++){
            g.drawLine(x + 1 + i, y + height -1 - i, x + width - 1, y + height -  1 - i); //Horizontal line at the bottom
            g.drawLine(x + width - 1 - i, y + 1 + i, x + width -1 - i, y + height - 1); //Vertical line on the right
        }
    	
    }

    @Override
    public Insets getBorderInsets(Component c) {
        //Insets is obviously mutable :/. Better create a new object every-time.
        return new Insets(borderWidth,borderWidth,borderWidth,borderWidth);
    }

    @Override
    public boolean isBorderOpaque() {
        return false;
    }

}
