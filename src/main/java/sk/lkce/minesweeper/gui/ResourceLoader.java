package sk.lkce.minesweeper.gui;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * A resource loader for loading all the game resources (images and sounds) from the files.
 * 
 * It is intended to be singleton. 
 * 
 * Before it can be used , {@link initialize()}
 * needs to be invoked. 
 * 
 * This ensures that all resources are loaded from the file system at the time controlled by the client.
 *
 */
public class ResourceLoader {

    private static final String PATH_MINEFIELD_NUMBERS = "numbers.png";
    private static final String PATH_DISPLAY_NUMBERS = "display.png";
    private static final String PATH_MINES = "mines.png";
    private static final String PATH_FLAG = "flag.png";
    private static final String PATH_QUESTION_MARK = "question_mark.png";
    private static final String PATH_FACES = "faces.png";
    private static final String PATH_APP_ICON = "icon.png";
    private static final String PATH_EXPLOSION_SOUND = "explosion.wav";
    private static final String PATH_TICK_SOUND = "tick.wav";
    private static final String PATH_WIN_SOUND = "win.wav";
    
    private static final int MINEFIELD_NUMBER_IMG_WIDTH = 20;
    private static final int DISPLAY_NUMBER_IMG_WIDTH = 26;
    private static final int MINE_IMG_WIDTH = 26;
    private static final int FLAG_IMG_WIDTH = 16;
    private static final int QUESTION_MARK_IMG_WIDTH = 12;
    private static final int FACE_IMG_WIDTH = 34;
    

    private Image[] minefieldNumbers, displayNumbers, faces;
    private Image imgMine, imgCrossedMine, imgFlag, imgQuestionMark;
    private Image appImage;
    
    private static ResourceLoader instance;
    private boolean initialized;

    /**
     * A type of sound resource.
     */
    public enum SoundResource {EXPLOSION, TICK, WIN}
    
    /**
     * A type of image resource.
     */
    public enum  ImageResource {
        MINE(MINE_IMG_WIDTH), 
        CROSSED_MINE(MINE_IMG_WIDTH),
        FLAG(FLAG_IMG_WIDTH), 
        QUESTION_MARK(QUESTION_MARK_IMG_WIDTH);
    	
        private final int width;
    	
        ImageResource(int width){
            this.width = width;
        }
    	
    };
    
    /**
     * A type of image resource which is actually a collection
     * of related pictures located in one file.
     */
    public enum  ImageSetResource {
        MINEFIELD_NUMBERS(MINEFIELD_NUMBER_IMG_WIDTH),
        DISPLAY_NUMBERS(DISPLAY_NUMBER_IMG_WIDTH),
        FACES(FACE_IMG_WIDTH);
    	
        private final int width;
    	
        ImageSetResource(int width){
            this.width = width;
        }
    
    };
    
    /**
     * Private constructor
     */
    private ResourceLoader(){}


    /**
     * Returns singleton instance.
     * @return singleton instance of resource loader
     */
    synchronized public static ResourceLoader getInstance(){
        if (instance == null)
            instance = new ResourceLoader();
        return instance;
    }
    
    
   /**
    * Returns an URL of a given sound resource. 
    * @param sound the sound resource which URL should be retrieved
    * @return the URL of the sound resource
    */
    public URL getSoundResourceUrl(SoundResource sound){

        String path;
        if (sound == SoundResource.EXPLOSION)
            path = PATH_EXPLOSION_SOUND;
        else if (sound == SoundResource.TICK)
            path = PATH_TICK_SOUND;
        else if (sound == SoundResource.WIN)
        	path = PATH_WIN_SOUND;
        else
            throw new AssertionError();
        	
        return getClass().getResource(path);
    }
    
    
    /**
     * Returns an image which represents the application icon.
     * @return the image to be used as application icon
     */
    public Image getApplicationIcon(){
        return appImage;
    }
    
    /**
     * Initialises the resource manager.
     * Loads all the resources. This method must be invoked before any
     * resource retrieval method is called.
     * @throws ResourceLoadingException when there was a problem during loading a resource
     */
    public void initialize() throws ResourceLoadingException {
    	
        try {
            minefieldNumbers = loadImagesFromFile(PATH_MINEFIELD_NUMBERS, MINEFIELD_NUMBER_IMG_WIDTH,8);
            imgFlag = loadImg(PATH_FLAG);
            imgQuestionMark = loadImg(PATH_QUESTION_MARK);
        	
            Image[] mines = loadImagesFromFile(PATH_MINES, MINE_IMG_WIDTH, 2);
            imgMine = mines[0];
            imgCrossedMine = mines[1];
        	
            displayNumbers = loadImagesFromFile(PATH_DISPLAY_NUMBERS, DISPLAY_NUMBER_IMG_WIDTH, 11);
            faces = loadImagesFromFile(PATH_FACES, FACE_IMG_WIDTH, 4);
        	
            appImage = loadImg(PATH_APP_ICON);
        	
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
            throw new ResourceLoadingException("Icons could not be loaded", e);
        }
    	
        initialized = true;
    }

    
    /**
     * Returns an icons array for mine-field numbers for the given icon width. Numbers range from 1 to 8. The icon for number <code>n</code>
     * is in the returned array on position <code>n - 1</code>.  The original image width is {@link #MINEFIELD_NUMBER_IMG_WIDTH} and the
     * image is scaled based on the proportion of the given width to this original width.<br> 
     * 
     * @param width the width of the icons
     * @return the newly created icon array from the mine field number images
     */
    public Icon[] createIconSet(ImageSetResource type, int width){
        checkIfInitialized();
    	
        Image[] images;
    	
        if (type == ImageSetResource.DISPLAY_NUMBERS)
            images = displayNumbers;
        else if (type == ImageSetResource.FACES)
            images = faces;
        else if (type == ImageSetResource.MINEFIELD_NUMBERS)
            images = minefieldNumbers;
        else
            throw new AssertionError("No images defined for " + type);
    	
        Icon[] icons = new Icon[images.length]; 
        //Calculate scale factor based on the original and given width ratio. 
        float  scaleFactor = ((float) width )/ type.width; 
    	
        for (int i = 0; i < images.length; i++) 
            icons[i] = getScaledImageIcon(images[i], scaleFactor);
    	
    	
        return icons; 
    }
    

    /**
     * Creates and returns an icon from a given image resource with the given width.
     * @param type the type of the image resource
     * @param width the desired width of the icon
     * @return a new image icon with a given image
     */
    public Icon createIcon(ImageResource type, int width){
        checkIfInitialized();
    
        Image img;
    	
        if (type == ImageResource.MINE)
            img = imgMine;
        else if (type == ImageResource.CROSSED_MINE)
            img = imgCrossedMine;
        else if (type == ImageResource.FLAG)
            img = imgFlag;
        else if (type == ImageResource.QUESTION_MARK)
            img = imgQuestionMark;
        else
            throw new AssertionError("No image defined for " + type);

        //Calculate scale factor based on the original and given width ratio. 
        float  scaleFactor = ((float) width )/ type.width; 
        return getScaledImageIcon(img, scaleFactor); 
    }
    

    /**
     * Creates a new image icon from an image which is before scaled
     * according to the specified scale factor.
     */
    private Icon getScaledImageIcon(Image originalImg, float scaleFactor){
        return new ImageIcon(getScaledImage(originalImg,scaleFactor));
    }
    
    /**
     * Return a new instance of image scaled according to the specified scale factor.
     */
    private Image getScaledImage(Image originalImg, float scaleFactor){
        int h = Math.round(scaleFactor * originalImg.getHeight(null));
        int w = Math.round(scaleFactor  * originalImg.getWidth(null));
        Image newImg = originalImg.getScaledInstance(w, h, BufferedImage.SCALE_SMOOTH);
    	
        return newImg;
    }
    
    
    /**
     * Check if this resource loader is in initialised state and throws
     * an exception if it is not.
     * @throw {@link IllegalStateException} if the manager is not initialised
     */
    private void checkIfInitialized(){
        if (!initialized)
            throw new IllegalStateException("The resource manager has not been initialized yet.");
    }
    
    /**
     * Creates an image array from a image file. The sub-images with the same height as 
     * the original image and each with the same width will be created by
     * retrieving the part of the original image using the logic:<br><br>
     * 
     *  <i>n-th</i> image = view port of the parent image on  <i>n</i> * <b>width</b> - <i>(n + 1)</i> * <b>width</b>
     *  X coordinates (the height of the sub-image stays the same as the height of the original image)
     * 
     * @param imgPath path to the file containing the image
     * @param width the width of the sub-images
     * @param count the number of sub-image to be made
     * @return
     * @throws URISyntaxException
     * @throws IOException
     */
    private Image[] loadImagesFromFile(String imgPath, int width, int count) throws URISyntaxException, IOException{
        URL url = getClass().getResource(imgPath);
        BufferedImage img = ImageIO.read(url);
    	
        Image[] result = new BufferedImage[count];
    	
        int height = img.getHeight();
    	
        for (int i = 0; i < count; i++) 
            result[i]=  img.getSubimage(i * width , 
                    0, width, height);
    	
    	
        return result;
    }
    
    /**
     * Loads the image from the resource file on the specified path.
     * @throws URISyntaxException
     * @throws IOException
     */
    private Image loadImg(String path) throws URISyntaxException, IOException{
        URL url = getClass().getResource(path);
        return ImageIO.read(url);
    }
    
}
