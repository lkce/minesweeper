package sk.lkce.minesweeper.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;

import sk.lkce.minesweeper.gui.FaceButton.Face;
import sk.lkce.minesweeper.model.Coordinate;
import sk.lkce.minesweeper.model.GameInfo;

/**
 * A game view. Contains also some presentation logic. It should be the only interface for interacting
 * with view objects. The whole game view consists of displays, button, and game grid with cell views.
 *
 */
@SuppressWarnings("serial")
public class GameView extends JPanel{

    /* Definition of look and style */
    static final Color DARK_COLOR = new Color(128, 128, 128);
    static final Color MAIN_COLOR = new Color(192, 192, 192);
    static final Color LIGHT_COLOR = Color.white;
    private static final Border OUTSIDE_BORDER_A = BorderFactory.createMatteBorder(3, 3, 0, 0, LIGHT_COLOR);
    private static final Border OUTSIDE_BORDER_B = BorderFactory.createMatteBorder(1, 1, 0, 0, MAIN_COLOR);
    private static final Border OUTSIDE_BORDER = BorderFactory.createCompoundBorder(OUTSIDE_BORDER_A, OUTSIDE_BORDER_B);
    private static final Border INSIDE_BORDER = BorderFactory.createLineBorder(MAIN_COLOR,5);
    private static final Border BORDER = BorderFactory.createCompoundBorder(OUTSIDE_BORDER, INSIDE_BORDER);
    
    private DisplayPane displayPane;
    private MineFieldGrid mineFieldGrid;
    
   /** 
    * Constructs a game view. 
    */
    public GameView() {
        super(new BorderLayout());
        setBackground(MAIN_COLOR);
        setBorder(BORDER);
        
        mineFieldGrid = new MineFieldGrid(this);
        add(mineFieldGrid);
        
        displayPane = new DisplayPane();
    
        add(displayPane, BorderLayout.NORTH);
    }
   
    /**
     * Sets the game view to a 'new-game' state. 
     * @param gameInfo information about the new game
     * @param controller game conroller
     */
    public void newGame(GameInfo gameInfo, GameController controller){
        mineFieldGrid.removeAll();
        mineFieldGrid.newGame(gameInfo, controller);
        displayPane.getFaceButton().reset();
        repaint();
    }
    
    /**
     * Updates cells views for cells with given coordinates.
     * @param coordinates cell coordinates
     */
    public void updateMineField(List<Coordinate> coordinates){
        mineFieldGrid.update(coordinates);
    }
    
    /**
     * Updates a cell view for a cell with a given coordinate.
     * @param coordinate cell coordinate
     */
    public void updateMineField(Coordinate coordinate){
        mineFieldGrid.update(coordinate);
    }
    
    
    /**
     * Sets the flag display to display given value.
     * @param number the number of flags to be displayed
     */
    public void setFlagDisplayNumber(int number){
        displayPane.getFlagDisplay().setNumber(number);
    }
    
    /**
     * Sets the time display to display given time.
     * @param number the seconds passed
     */
    public void setTimeDisplayNumber(int number){
        displayPane.getTimeDisplay().setNumber(number);
    }
    
    /**
     * Sets the face type for the game button
     * @param face face type which should be displayed
     */
    public void setFace(Face face){
        displayPane.getFaceButton().setFace(face);
    }
   
    /**
     * Adds listener/observer for the face button.
     * @param listener face button listener
     */
    public void addFaceButtonListener(ActionListener listener){
        displayPane.getFaceButton().addActionListener(listener);
    }
   
    /**
     * Updates the view to reflect 'gave-over' state and based on
     * whether the game was won or lost.
     * @param won <code>true</code> if the game was lost, <code>false</code> if otherwise
     */
    public void gameOver(boolean won){
        Face face = won? Face.VICTORIOUS : Face.DEAD;
        displayPane.getFaceButton().setFace(face);
        mineFieldGrid.gameOver(won);
    }
}
