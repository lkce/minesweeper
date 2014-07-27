package sk.lkce.minesweeper.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * The game model. Contains the game logic and maintains and changes state of the
 * fields (also called cells)  in the mine-field grid.  <br><br>
 * 
 * The mine-field is two dimensional collection of individual fields/cells  some of which contain mines.  
 * 
 */
public class MineField {
    	
    private int columnCount;
    private int rowCount;
    private int mineCount;
    private int cellCount;
    private Map<Coordinate,Cell> cells;
    private List<Cell> mines = new ArrayList<Cell>();
    private boolean mineHit, gameWon;
    private List<Coordinate> newlyRevealedCells = new ArrayList<Coordinate>();
    private GameInfo gameInfo;
    private int flagsLeft;
    private int coveredCells;
    private List<Coordinate> mineCoordinates = new ArrayList<>();
    
    /**
     * Constructs a new mine-field instance. The coordinates of mines are not calculated
     * when constructing the object but {@link #putMines(Coordinate)} needs to be invoked to put
     * mine-field object to game-ready state.
     * 
     * @param columnCount number of columns of the mine-field
     * @param rowCount number of rows  of the mine-field
     * @param mineCount number of mines the mine-field should have
     */
    public MineField(int columnCount, int rowCount, int mineCount){
        this.columnCount =columnCount;
        this.rowCount = rowCount;
        this.mineCount = mineCount;
    	
        cellCount = columnCount * rowCount;
        cells = new HashMap<Coordinate,Cell>(cellCount);
    	
        flagsLeft = mineCount;
    	
        coveredCells = cellCount;
        
        for (int row = 0; row < rowCount; row++) {
            for (int col = 0; col < columnCount; col++) {
                Cell cell = new Cell(col, row);
                cells.put(cell.getCoordinate(), cell);
            }
        }
    }
    
    
    /**
     * Calculates and sets the mines of the mine-field based on randomly generated coordinates and guarantees
     * that the cell for a given coordinate will not contain mine.
     * 
     * This method needs to be invoked before the game can start.<br><br>
     * The purpose of excluding of the mines "creation" from the construction phase of the mine-field
     * is to  give the presentation layer a possibility to guarantee that the first unrevelaed field contains
     * no mine.
     * 
     * @param ignoreCoordinate a coordinate which is guaranteed to contain no mine
     */
    public void putMines(Coordinate ignoreCoordinate){
        Set<Coordinate> coordinates = calculateMineCoordinates(ignoreCoordinate);
        mineCoordinates.addAll(coordinates);
        
        
        for (Coordinate coordinate : mineCoordinates){
            Cell cell = cells.get(coordinate);
            mines.add(cell);
            cell.setHasMine(true);
        }

        // Count the number of mine-carrying neighbours for each cell
        for (Cell cell : cells.values())
            countNeighbouringMines(cell);
        
    }

    /**
     * Returns  a cell information object for the given row and column index.
     * @param x column index
     * @param y row index
     * @return cell information object
     */
    public CellInfo getCellInfo(int x, int y){
        return getCell(x,y).getCellInfo();
    }
    
    /**
     * Returns number of columns of this mine-field
     * @return number of columns
     */
    public int getColumnCount(){
        return columnCount;
    }
    
    /**
     * Returns number of rows of this mine-field
     * @return number of rows
     */
    public int getRowCount(){
        return rowCount;
    }
    
    /**
     * Returns <code>true</code> if the last time the cell was revealed
     * it contained mine.
     * @return <code>true</code> if the mine was hit when revealing the cell
     */
    public boolean wasMineHit(){
        return mineHit;
    }
    
    
    /**
     * Returns <code>true</code> of all the cells without mine have been revealed.
     * @return <code>true</code> if the game is considered to be won
     */
    public boolean isGameWon(){
        return gameWon;
    }
    
    
    /**
     * Returns number of flags left. The flags can be used to mark the cells which are expected
     * to contain mines and their number corresponds to the number of mines.       
     * 
     * @return the number of flags left
     */
    public int getLeftFlagsCount(){
        return flagsLeft;
    }
    
    /**
     * Reveals a cell with a given coordinate, verifies the winning conditions and reveals 
     * the list of the revealed cells. In some cases, however, also other cells are automatically
     * revealed based on whether there are any mines in adjacent cells or whether the cell itself contains mine.
     * <br>
     * The behavior is following:
     * 
     * <ul>
     *  <li> <b>Cell does not contain mine AND there is at least one mine-cell in the neighborhood </b> - only the cell is revealed
     *  and contain information of how adjacent cells contain mines. </li>
     *  <li> <b>Cell does not contain mine AND there is no mine-cell in the neighborhood </b> - the cell is revealed and all its
     *  neighborhoods are revealed recursively </li>
     *  <li> <b>Cell contains mine </b> - the cell is revealed and all the
     *  other cells with mine are revealed as well</li>
     * </ul>
     *
     * The list of the coordinates of the revealed cells is returned by the method.
     * 
     * For victory conditions see {{@link #isGameWon()} and {{@link #wasMineHit()} .
     * 
     * @param coordinate a coordinate of the cell to be revealed
     * 
     * @return the list of coordinates of the revealed cells
     * @throws IllegalArgumentException if the coordinate values are not within the bounds of the mine-field size
     * @throws IllegalStateException if an attempt is made to reveal flagged cell
     * @see #isGameWon() 
     * @see #wasMineHit()
     */
    public List<Coordinate> revealCell(Coordinate coordinate){
    
        if (coordinate.x < 0 || coordinate.x >= columnCount)
            throw new IllegalArgumentException("Coordinate.x is not within bounds 0 - " + (columnCount-1));
        if (coordinate.y < 0 || coordinate.y >= rowCount)
            throw new IllegalArgumentException("Coordinate.y is not within bounds 0 - " + (rowCount-1));
    	
        Cell cell = cells.get(coordinate);
    	
        if (cell.hasFlag())
            throw new IllegalStateException("Cannot reveal flagged cell");

        
        if (cell.hasMine()){
            cell.mineWasHit();
            //Reveal all mines
            for (Cell c : mines)
                c.reveal();
        	
            mineHit = true; 
            return Collections.unmodifiableList(mineCoordinates);
        }
    	
        //Make call to internal overloaded revealCell method. 
        revealCell(cell);
        verifyIfWon();
        
        if(gameWon) //If game won, also update the mine cell as we set the flags there.
            newlyRevealedCells.addAll(mineCoordinates);
        
        return Collections.unmodifiableList(newlyRevealedCells);
    }
    
    
    /**
     * Sets to or removes flag from a cell at a given coordinate.
     * 
     * @param coordinate coordinate of the cell
     * @param isFlagged <code>true</code> if the flag should be added, <code>false</code> if the flag should be removed
     * @return the number of flags left
     * 
     * @throws IllegalStateException if there are no flags left are if an attempt is made to remove flag 
     * from a cell which is not flagged
     * @see #getLeftFlagsCount()
     */
    public int setFlag(Coordinate coordinate, boolean isFlagged){
    	
        if (isFlagged){
            if (flagsLeft == 0)
                throw new IllegalStateException("There cannot be more flags then mines");
            flagsLeft--;
        }else{
            if (flagsLeft == mineCount)
                throw new IllegalStateException("Cannot remove flag. There should be no flag in the field.");
            flagsLeft++;
        }
    	
        cells.get(coordinate).setHasFlag(isFlagged);
    	
        return flagsLeft;
    }
    
    
    /**
     * Sets to or removes question mark from a cell with a given coordinate.
     * @param coordinate coordinate of the cell
     * @param hasQuestionmark <code>true</code> if question mark should be added, <code>false</code> if it should be removed
     */
    public void setQuestionMark(Coordinate coordinate, boolean hasQuestionmark){
        cells.get(coordinate).setQuestionMark(hasQuestionmark);
    }
    
    /**
     *  Returns cell for given column and row indexes.
     */
    private Cell getCell(int x, int y){
        Coordinate coor = new Coordinate(x,y);
        return cells.get(coor);
    }
    
    /**
     * Generates randomly and returns coordinates of cell where the mine will be places. 
     * The set of mine coordinates will not contain the provided ignore-coordinate. 
     */
    private Set<Coordinate> calculateMineCoordinates(Coordinate ignoreCoordinate){
        
        Set<Coordinate> coordinates = new HashSet<Coordinate>(mineCount);
        Random random = new Random();
        for (int i = 0; i < mineCount;i++){
            int index = random.nextInt(cellCount);
            Coordinate co = indextToCoordinate(index);
            //If the coordinate has been already picked or should be ignored, add
            //one more iteration.
            if (co.equals(ignoreCoordinate) || 
                    !coordinates.add(co)){
                i--;
            }
        }
        
        return coordinates;
    }
    
    /**
     * Helper method which transform an index to coordinate. 
     */
    private Coordinate indextToCoordinate(int i){
        int x = i  % columnCount;
        int y = (i - x) / columnCount ;
        
        return new Coordinate(x,y);
    }
    
    
    /**
     * Generates and return the string representation of the mine-field which shows
     * the cells and their states.
     * 
     * @return string representation of the state of this mine-field
     */
    public String debugImg(){
        StringBuilder sb = new StringBuilder();
    	
        for (int h = 0; h < getRowCount(); h++){
            for (int w = 0; w < getColumnCount(); w++){
                Cell c = getCell(w, h);
        		
                String s;
                if (c.hasFlag())
                    s ="F";
                else if (c.hasMine())
                    s = "X";
                else{
                    if (!c.isRevealed())
                        s = ".";
                    else
                        s = c.getNearbyMinesCount() +"";
            	}
        		
                sb.append(s);
                sb.append(" ");
        	}
            sb.append("\n");
        }
        		
    	
        return sb.toString();
    }
    
    /**
     * Makes check if the conditions are met for the game to be declared as won.
     */
    private void verifyIfWon(){
        if (coveredCells > mines.size())
            return;
        
        gameWon = true;
        //Make the flag set on all mines.
        for (Cell cell : mines)
            if (!cell.hasFlag())
                cell.setHasFlag(true);
    }
    
    /**
     * Counts and set the number of mines in the nearby (neighboring) cells for a given cell.
     */
    private void countNeighbouringMines(Cell cell){
        if (cell.hasMine()) //Makes no sense to count it for a mine cell.
            return;
    	
        int mineCount = 0;
        for (int y = -1; y < 2; y++)
            for (int x = -1 ; x < 2; x++){
                if (y == 0 && x == 0) //These are the 'cell' coordinates.
                    continue;
                Cell neighbour = getCellNeighbour(cell, x, y);
                if (neighbour != null && neighbour.hasMine())
                    mineCount++;
        	}
    	
        cell.setNearbyMinesCount(mineCount);
    }
    	
    /**
     * Reveals a cell and also recursively its neighbors if the cell has no mine in neighborhood.
     * Cell is revealed only if condition <b>{@link Cell#hasMine()} == <code>true</code> </b> holds true. 
     * If the cell does not have any mine-carrying neighbors the reveal operation is spread recursively to
     * all of its neighbors eventually stopping at the cells near the mine or at the side of the mine-field.
     */
    private void revealCell(Cell cell){
    	
        //Ignore if it has been already uncovered (also prevents stackoverflow cycle) or
        //if has flag.
        if (cell.isRevealed() || cell.hasFlag()) 
            return;
    	
        cell.reveal();
        cell.setQuestionMark(false); //Ensure the question mark is not present after being uncovered.
        newlyRevealedCells.add(cell.getCoordinate()); //Add to the list of revealed cells.
    	coveredCells--; //Decrement the total number of covered cells
    	
        if (cell.getNearbyMinesCount() > 0) //Contains mine in neighborhood - do not uncover the neighbors.
            return;
    	
        //Uncover all neighbors as the cell has no mine in the neighborhood
        for (int y = -1; y < 2; y++)
            for (int x = -1 ; x < 2; x++){
                if (y == 0 && x == 0) //These are the 'cell' coordinates - no need to check yourself.
                    continue;
                Cell neighbour = getCellNeighbour(cell, x, y);
                if (neighbour == null) //No neighbor - must be the margin position.
                    continue;
                revealCell(neighbour);
        	}
    }
    
    /**
     * Returns a cell's neighbor for given row and column offset.
     */
    private Cell getCellNeighbour(Cell cell,int offsetX, int offsetY ){
        Coordinate homeCoor = cell.getCoordinate();
        Coordinate neighbourCoor = new Coordinate(homeCoor.x + offsetX,
                                        homeCoor.y +  offsetY);
        return cells.get(neighbourCoor);
    }
    
    
    /**
     * Returns read-only view of the mine-field. Intended to be used in the view beyond
     * controller to reduce the coupling and possibility of views interacting with game model
     * directly.
     * 
     * @return game information object
     */
    public GameInfo getGameInfo(){
        if (gameInfo == null) //Initiate lazily
            gameInfo = new GameInfo(){

                @Override
                public int getRowCount() {
                    return MineField.this.getRowCount();
            	}

                @Override
                public int getColumnCount() {
                    return MineField.this.getColumnCount();
            	}

                @Override
                public CellInfo getCellInfo(int x, int y) {
                    return MineField.this.getCellInfo(x, y);
            	}
        };

                return gameInfo;
    }
    
}
