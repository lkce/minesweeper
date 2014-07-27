package sk.lkce.minesweeper.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import sk.lkce.minesweeper.gui.ResourceLoader.ImageResource;
import sk.lkce.minesweeper.gui.ResourceLoader.ImageSetResource;
import sk.lkce.minesweeper.model.CellInfo;
import sk.lkce.minesweeper.model.Coordinate;

/**
 * A Swing component view for a mine-field cell in style of Windows XP Minesweeper.
 * The view can have several states
 * like default, pressed, revealed etc. The cell view also contains
 * a presentation logic of how to respond to various mouse events.
 * The view is notified about mouse events from the parent component 
 * which it is part of (as this  sometimes require synchronize 
 * behavior of multiple cell view) and therefore no regular Swing mouse listeners are 
 * added to this component.
 * 
 * <br><br> 
 * All information about cell is accessed via {@link CellInfo}.
 *
 */
@SuppressWarnings("serial")
public class CellView extends JPanel {

    private enum ButtonAction {LEFT, RIGHT};
    
    public static final int HEIGHT = 16;
    public static final int WIDTH = HEIGHT;
    private static final int BORDER_WIDTH = 2;
    public static final Dimension dimension = new Dimension(WIDTH, HEIGHT);
    private static final Color colorBackground = GameView.MAIN_COLOR;
    private static final Color colorHitMine = Color.red;
    private static final Color colorBorder = GameView.DARK_COLOR;
    
    private static final Border border = new MineSweeperBorder(BORDER_WIDTH, GameView.LIGHT_COLOR, colorBorder);
    private static final Border borderRevealed = BorderFactory.createMatteBorder(1, 1, 0, 0, colorBorder);
    private static final Icon[] numberIcons = ResourceLoader.getInstance().createIconSet(ImageSetResource.MINEFIELD_NUMBERS, 10);
    private static final Icon mineIcon = ResourceLoader.getInstance().createIcon(ImageResource.MINE, 13);
    private static final Icon crossedMineIcon = ResourceLoader.getInstance().createIcon(ImageResource.CROSSED_MINE, 13);
    private static final Icon questionMarkIcon = ResourceLoader.getInstance().createIcon(ImageResource.QUESTION_MARK, 6);
    private static final Icon flagIcon = ResourceLoader.getInstance().createIcon(ImageResource.FLAG, 8);

    private static final Border labelPressedBorder = BorderFactory.createEmptyBorder(1, 1, 0, 0);
    
    private boolean isRevealed;
    private JLabel label;
    private CellInfo cellInfo;
    private Set<CellViewObserver> listeners = new HashSet<CellViewObserver>();
    private boolean isPressed;
    
    /**
     * Constructs a cell view based on a given cell information.
     * @param cellInfo
     */
    public CellView(final CellInfo  cellInfo){
        super(new BorderLayout());
        setBackground(colorBackground);
        setSize(WIDTH, HEIGHT);
        this.cellInfo = cellInfo;
    	
        label = new JLabel();
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);
        setBorder(border);
        add(label);
    }
    
    
    /**
     * Sets this view to pressed state for a given mouse button.
     * Based on the mouse button and the view's state an event
     * might be fired to registered {@link CellViewObserver} objects.
     * @param button mouse button type
     */
    void mousePressed(int button){
        if (isRevealed) 
            return;
    	
        if (button == MouseEvent.BUTTON3)
            fireButtonActivated(ButtonAction.RIGHT); //Activate flag/question mark right after right  button was pressed.
        else{
            if (!cellInfo.hasFlag()){
                setPressed(); //Just show pressed look. Revealing the cell is activated upon the release of the left mouse button.
        	}
        }
    }
    
    /**
     * Invoked when the right mouse button has been released on top of this
     * cell view (simply a click event). It is assumed that the view is in pressed state when this method is invoked.
     *
     * If the cell view is not revealed or does not contain flag, the left mouse button
     * activate action is fired to all registered @link CellViewObserver} objects.
     * 
     */
    void mouseReleased(){
        if (isRevealed || cellInfo.hasFlag())
            return;
        assert isPressed;
    	
        fireButtonActivated(ButtonAction.LEFT);
        label.setBorder(null);
    }
    
    /**
     * Cancels the pressed state of this cell view. This method should be used
     * in a situation when the cell view has been brought to the pressed state
     * and this state needs to be undone, effectively bringing the view to the
     * default state.
     */
    void unpressMouse(){
        if (isRevealed)
            return;
        setUnpressed();
    }
   
    /**
     * Sets pressed look.
     */
    private void setPressed(){
        isPressed= true;
        setBorder(borderRevealed);
        label.setBorder(labelPressedBorder);
        repaint();
    }
    
    /**
     * Sets 'unpressed' look.
     */
    private void setUnpressed(){
        isPressed = false;
        setBorder(border);
        repaint(); // Paint back to normal.
    }
    
   /**
    * Adds specified observer to receive the cell view
    * events from this cell view.
    * @param listener listener to be added
    */
    public void addListener(CellViewObserver listener){
        listeners.add(listener);
    }
    
    /**
     * Instructs this cell view to update its look according
     * to the underlying cell information and it's current state.
     * This method should be invoked when the cell backing this cell view
     * has changed its state.
     */
    public void updateLook(){
        if (cellInfo.isRevealed()){
            isRevealed = true;
            setRevealedLook();
            return;
        }
    	
        Icon icon = null;
    	
        if (cellInfo.hasQuestionMark())
            icon = questionMarkIcon;
        else if (cellInfo.hasFlag())
            icon = flagIcon;
    	
        label.setIcon(icon);
    }
   
    /**
     * Returns coordinate of this cell view's cell.
     * @return cell's coordinate
     */
    public Coordinate getCoordinate(){
        return cellInfo.getCoordinate();
    }
    
    /**
     * Sets the reveal look.
     */
    private void setRevealedLook(){
        Icon icon = null;
        setBorder(borderRevealed);
        if (cellInfo.hasMine()){
            icon = mineIcon;
            if (cellInfo.wasMineHit())
                setBackground(colorHitMine);
            else if (cellInfo.hasFlag())
                icon = crossedMineIcon;
        }
        else{
            int count = cellInfo.getsetNearbyMinesCount();
            if (count > 0)
                icon = numberIcons[count-1];
        }
    	
        label.setIcon(icon);
    }
    
   /**
    * Fires button activated event to all observers
    * for a given button type. 
    */
    private void fireButtonActivated(ButtonAction button){
        if (button == ButtonAction.LEFT)
            for (CellViewObserver listener : listeners)
                listener.leftButtonActivated(cellInfo.getCoordinate());
        else
            for (CellViewObserver listener : listeners)
                listener.rightButtonActivated(cellInfo.getCoordinate());
    }
    
    @Override
    public Dimension getPreferredSize(){
        return dimension;
    }	
}
