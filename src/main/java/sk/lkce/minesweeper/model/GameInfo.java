package sk.lkce.minesweeper.model;

/**
 *  Read-only collection of basic information about the mine-field intended to be used 
 *  in view layer.
 */
public interface GameInfo {

    /**
     * Returns the number of  rows of the mine-field
     * @return the number of  rows of the mine-field
     */
	int getRowCount();

    /**
     * Returns the number of  columns of the mine-field
     * @return the number of  columns of the mine-field
     */
	int getColumnCount();
	
	
	/**
	 * Returns a cell information object for a given column and row coordinate
	 * @param x column coordinate
	 * @param y row coordinate
	 * @return cell information object
	 */
	CellInfo getCellInfo(int x, int y);
}
