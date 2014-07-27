package sk.lkce.minesweeper.gui;

/**
 * An exception which is thrown when
 * a game resource could not be loaded.
 */
@SuppressWarnings("serial")
public class ResourceLoadingException extends Exception {

    /**
     * Constructs a resource loading exception with a detail message
     * and a cause. 
     * @param msg the detail message
     * @param cause the cause
     */
    public ResourceLoadingException(String msg, Throwable cause){
        super(msg,cause);
    }
    
}
