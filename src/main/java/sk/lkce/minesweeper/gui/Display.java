package sk.lkce.minesweeper.gui;

import java.awt.GridLayout;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import sk.lkce.minesweeper.gui.ResourceLoader.ImageSetResource;

/**
 * A Swing display  which shows a 3 digit number.
 * The display copies the Windows XP Minesweeper display look. 
 * @see Display
 * @see FaceButton
 */
@SuppressWarnings("serial")
public class Display extends JPanel{

    private static final int DIGIT_COUNT = 3;
    private JLabel[] labels = new JLabel[DIGIT_COUNT];
    private Icon[] numbers = ResourceLoader.getInstance().createIconSet(ImageSetResource.DISPLAY_NUMBERS,13);
    
    /**
     * Constructs a new display.
     */
    public Display(){
        GridLayout layout = new GridLayout(1,DIGIT_COUNT);
        setLayout(layout);
        for (int i = 0; i < labels.length; i++) {
            labels[i] = new JLabel();
            labels[i].setIcon(numbers[0]);
            add(labels[i]);
        }

        setNumber(0);
    }
    
    /**
     * Sets this display to start showing a given number value.
     * @param number number to be displayed
     */
    public void setNumber(int number){
    	
        int max  = (int)Math.pow(10, DIGIT_COUNT)-1;
        if (number < 0 && number > max)
            throw new IllegalArgumentException("The number needs to be in range <0, " + max + ">");
    	
        int[] digits = getDigitArray(number);
    	
        removeAll();
        for (int i = 0; i < digits.length; i++) {
            //labels[i].setIcon(numbers[digits[i]]);
            //Instead seting an icon just create & add new JLabel. As setting
            //an icon invokes repaint and the digits in display might not change at once but one after another.
            //TODO look at better solutions (other than custom painting).
            add(new JLabel(numbers[digits[i]]));
        }
    	
        revalidate();
        repaint();
    }
   
    /**
     * Converts an integer to an integer array representing the number's digits.
     */
    private int[] getDigitArray(int number){
        int[] digits = new int[DIGIT_COUNT];
    	
        int leftover = number;
        for (int i = 0; i < DIGIT_COUNT; i++){
            int result = leftover % 10;
            leftover = (leftover - result)/10;
            digits[DIGIT_COUNT -1 -i] = result;
        	
            if (leftover == 0)
                break;
        }
        assert leftover == 0;
    	
        return digits;
    }
}
