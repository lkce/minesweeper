package sk.lkce.minesweeper.model;

/*
 * A collection of informations about the mine-field cell.
 * Provides read-only access and can be used in the presentation layer without
 * threat of modifying the game state.
 * 
 */
public interface CellInfo {

    /**
     * Returns coordinate of the cell.
     * @return cell coordinate
     */
    Coordinate getCoordinate();
    
    /**
     * Determines whether the cell is revealed.
     * @return <code>true</code> if the cell is revealed
     */
    boolean isRevealed();
    
    /**
     * Determines whether the cell has mine.
     * @return <code>true</code> if the cell has mine 
     */
    boolean hasMine();

    /**
     * Determines whether the cell has flag.
     * @return <code>true</code> if the cell has flag 
     */
    boolean hasFlag();
    
    /**
     * Returns the number of cell adjacent to the cell which
     * have mine.
     * @return number of neighboring cells with mine
     */
    int getsetNearbyMinesCount();
    
    /**
     * Determines if the mine was found when the cell was revealed
     * @return <code>true</code> if the cell contains mine which was hit
     */
    boolean wasMineHit();
    
    
    /**
     * Determines if the cell has question mark.
     * @return <code>true</code> if the cell has question mark
     */
    boolean hasQuestionMark();
}
