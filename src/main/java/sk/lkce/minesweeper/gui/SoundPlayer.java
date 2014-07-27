package sk.lkce.minesweeper.gui;

import java.io.IOException;
import java.net.URL;

import javax.media.Manager;
import javax.media.NoPlayerException;
import javax.media.Player;
import javax.media.Time;

import sk.lkce.minesweeper.gui.ResourceLoader.SoundResource;

/**
 * A sound player for the application. Provides
 * only interface for playing sounds. Before the normal usage
 * the {@link #initialize()} must be invoked to lead the
 * sound resources.
 */
public class SoundPlayer {

    private Player explosionPlayer, tickPlayer, winPlayer;
    private URL urlExplosion, urlTick, urlWin;
    private boolean isInitialized;

    /*
     * Constructs a new sound player.
     */
    public SoundPlayer() {
        urlExplosion = ResourceLoader.getInstance().getSoundResourceUrl(
                SoundResource.EXPLOSION);
        urlTick = ResourceLoader.getInstance().getSoundResourceUrl(
                SoundResource.TICK);
        urlWin = ResourceLoader.getInstance().getSoundResourceUrl(
                SoundResource.WIN);

    }

    /**
     * Initialises this sound player with the necessary resources.
     * This method should be called only once. All the other methods
     * which play sounds can be called only afterwards.
     * 
     * @throws ResourceLoadingException  if a problem occurs when loading
     * the sound resources
     * 
     */
    public void initialize() throws ResourceLoadingException {
        try {

            JarDataSource jdsExplosion = new JarDataSource(urlExplosion);
            jdsExplosion.connect();
            JarDataSource jdsTick = new JarDataSource(urlTick);
            jdsTick.connect();
            JarDataSource jdsWin = new JarDataSource(urlWin);
            jdsWin.connect();

            explosionPlayer = Manager.createPlayer(jdsExplosion);
            explosionPlayer.realize();
            tickPlayer = Manager.createPlayer(jdsTick);
            tickPlayer.realize();
            winPlayer = Manager.createPlayer(jdsWin);
            winPlayer.realize();

        } catch (NoPlayerException e) {
            throw new ResourceLoadingException(
                    "Could not initialize the sound player", e);
        } catch (IOException e) {
            throw new ResourceLoadingException(
                    "Could not initialize the sound player", e);
        }catch(Exception e){
            throw new ResourceLoadingException(
                    "Could not initialize the sound player", e);
        }

        isInitialized = true;
    }

    /**
     * Ensures that this sound player is initialised. If this is not the 
     * case an exception is thrown.
     * @throws IllegalStateException if the sound player has not been initialised
     */
    private void ensureInitialized() {
        if (!isInitialized)
            throw new IllegalStateException("The player is not initialized");
    }

    /**
     * Determines if this sound player has been initialised.
     * @return <code>true</code> if the sound player is initialised
     */
    public boolean isInitialized() {
        return isInitialized;
    }

    /**
     * Plays the mine explosion sound.
     * @throws IllegalStateException if the sound player has not been initialised
     */
    public void playExplosionSound() {
        ensureInitialized();
        explosionPlayer.setMediaTime(new Time(0));
        explosionPlayer.start();
    }

    /**
     * Plays the clock tick sound. 
     * @throws IllegalStateException if the sound player has not been initialised
     */
    public void playTickSound() {
        ensureInitialized();
        tickPlayer.setMediaTime(new Time(0));
        tickPlayer.start();
    }

    /**
     * Plays the 'game won' sound. 
     * @throws IllegalStateException if the sound player has not been initialised
     */
    public void playWinSound() {
        ensureInitialized();
        winPlayer.setMediaTime(new Time(0));
        winPlayer.start();
    }
}
