package sk.lkce.minesweeper.model;



/**
 *  Value object representing the convenient combination of a row and column index in the grid.
 *
 */
public class Coordinate {
    public final int x;
    public final int y;
    
    public Coordinate(int x, int y){
        this.x =x;
        this.y =y;
    }
    
    
    /**
     * Determines whether or a given object is equals to this coordinate.
     *  <br>
     * An object is equal to this coordinate if: <br>
     * <ul>
     * <li>is instance of {@link Coordinate}</li>
     * <li>its column and row indexes are equal
     *  to this coordinate's column and row indexes</li>
     * </ul>
     *  
     * 
     * @return <code>true</code> if a given object is considered to be equal with this coordinate
     */
    @Override
    public boolean equals(Object o){
        if (o instanceof Coordinate == false)
            return false;
        Coordinate c = (Coordinate) o;
    	
        return (x == c.x && y == c.y);
    }
    
    /*
 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode(){
        return y*11+y*7;
    }
    
    
    /**
     * Custom string representation of coordinate.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString(){
        return "[" + x + "," + y + "]";
    }
    
}
