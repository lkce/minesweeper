package sk.lkce.minesweeper.model;


/**
 * A cell in the mine-field.
 * 
 * @see MineField
 */
class Cell {
    
	private Coordinate coordinate;
	private boolean mine;
	private boolean flagged;
	private boolean questionMark;
	private boolean revealed;
	private boolean wasMineHit;
	private int nearbyMinesCount = -1;
	
	private CellInfo info;
	
	/**
	 * Constructs the cell for a given column and row indes
	 * @param column column index
	 * @param row row index
	 */
	Cell(int column, int row){
		coordinate = new Coordinate(column,row);
	}
	
	/**
	 * Determines of this cell has mine,
	 * @return <code>true</code> if cell has mine
	 */
	boolean hasMine(){
		return mine;
	}
	
	/**
	 * Sets or remove mine from this cell.
	 * @param b if <code>true</code> mine is set, else mine is removed
	 */
	void setHasMine(boolean b){
		mine = b;
	}
	
	/**
	 * Determines if this cell is revealed.
	 * @return <code>true</code> if the cell is revealed
	 */
	boolean isRevealed(){
		return revealed;
	}
	
	/**
	 * Reveals the cell.
	 * @return <code>true</code> if this cell contains mine
	 */
	boolean reveal(){
		assert !revealed : "The cell has been already unconvered";
		revealed = true;
		return mine;
	}
	
	/**
	 * Determines if the cell has flag.
	 * @return <code>true</code> if the cell has flag
	 */
	boolean hasFlag(){
		return flagged;
	}
	
	/**
	 * Sets or removes flag from the cell.
	 * @param b <code>true</code> if the flag should be set, <code>false</code> if removed
	 */
	void setHasFlag(boolean b){
		flagged = b;
		
	}
	
	/**
	 * Returns the coordinate of this cell.
	 * @return coordinate of the cell
	 */
	Coordinate getCoordinate(){
		return coordinate;
	}
	
    /**
     * Returns the number of cell adjacent to this cell which
     * have mine.
     * @return number of neighboring cells with mine
     */
	int getNearbyMinesCount() {
		return nearbyMinesCount;
	}
	
	
	/**
     * Sets the number of cell adjacent to this cell which
     * have mine.
	 * @param nearbyMinesCount number of nearby mines
	 */
	void setNearbyMinesCount(int nearbyMinesCount) {
		this.nearbyMinesCount = nearbyMinesCount;
	}
	
	
	/**
	 * Sets this cell as the one where the mine was revealed
	 */
	void mineWasHit(){
		wasMineHit = true;
	}
	
	/**
     * Determines if this cell has question mark.
     * @return <code>true</code> if the cell has question mark
     */
	boolean hasQuestionMark() {
		return questionMark;
	}

	/**
	 * Sets or removes question mark from this cell.
	 * @param hasQuestionMark <code>true</code> if question mark should be set, <code>false</code> if removed 
	 */
	void setQuestionMark(boolean hasQuestionMark) {
		this.questionMark = hasQuestionMark;
	}

	@Override
	public String toString(){
		String mineStr = mine?"mine!":"";
		String flaggedStr = flagged?"flagged":"";
		return Cell.class.getSimpleName() +  " [" + 
				+ coordinate.x + ", " + coordinate.y +"] " + mineStr + ", " + flaggedStr; 
	}
	
	
	/**
	 * Returns view representation of this cell.
	 * @return cell info object bound to this cell
	 */
	CellInfo getCellInfo(){
		
		if (info == null)
			info = new CellInfo(){
	
				@Override
				public Coordinate getCoordinate() {
					return Cell.this.getCoordinate();
				}
	
				@Override
				public boolean isRevealed() {
					return Cell.this.isRevealed();
				}
	
				@Override
				public boolean hasMine() {
					return Cell.this.hasMine();
				}
	
				@Override
				public boolean hasFlag() {
					return Cell.this.hasFlag();
				}
	
				@Override
				public int getsetNearbyMinesCount() {
					return Cell.this.getNearbyMinesCount();
				}
				
				@Override
				public boolean wasMineHit() {
					return wasMineHit;
				}
				
				@Override
				public boolean hasQuestionMark() {
					return questionMark;
				}
				
		};
		
		return info;
	}
	
	
}
