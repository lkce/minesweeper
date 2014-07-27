package sk.lkce.minesweeper.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.Timer;

import sk.lkce.minesweeper.model.CellInfo;
import sk.lkce.minesweeper.model.Coordinate;
import sk.lkce.minesweeper.model.MineField;

/**
 * A controller of the game logic. A mediator between {@link MineField}
 * and {@link GameView}. Some presentation logic is also part of the game view.
 *
 */
public class GameController implements CellViewObserver{

    private MineField field; 
    private GameView gamePane;
    private boolean timerOn;
    private int secondsPassed;
    private Timer timer;
    private GameOptions options;
    private SoundPlayer soundPlayer;
    private static final int TIMER_INTERVAL = 1000;
    private boolean minesSet;

    /**
     * Constructs a controller.
     * @param options game options
     * @param gameView game view
     * @param soundPlayer sound player
     */
    public GameController(GameOptions options, GameView gameView, SoundPlayer soundPlayer){
        this.field = new MineField(options.getColumCount(),options.getRowCount(), options.getMineCount());
        this.gamePane = gameView;
        this.options = options;
        this.soundPlayer = soundPlayer;
    	
        gameView.addFaceButtonListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                startNewGame();
        	}
        	
        });
        
        startNewGame();
    }
    
    /**
     * Starts a new game. The whole game state including timer and flag counter is reset.
     */
    public void startNewGame(){
        stopTimer(); //Stop the timer in case it runs from previous game.
        secondsPassed = 0;
        minesSet = false;
        timerOn = false;
        field = new MineField(options.getColumCount(),options.getRowCount(), options.getMineCount());
        gamePane.newGame(field.getGameInfo(), this);
        gamePane.setFlagDisplayNumber(field.getLeftFlagsCount());
        gamePane.setTimeDisplayNumber(0);
    }
    
    /**
     * Undertakes the necessary action after the game has ended based on whether
     * the game was won or lost.
     * @param won <code>true</code> if game was won, <code>false</code> if lost
     */
    private void gameOver(boolean won){
        stopTimer();
        if (options.isSound())
            if (won)
                soundPlayer.playWinSound();
            else
                soundPlayer.playExplosionSound();
        gamePane.gameOver(won);
    }
    
    /**
     * Starts the game timer.
     */
    private void startTimer(){
        	 
        ActionListener taskPerformer = new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                gamePane.setTimeDisplayNumber(++secondsPassed);
                if (options.isSound())
                    soundPlayer.playTickSound();
        	}
        };
        timer = new Timer(TIMER_INTERVAL, taskPerformer);
        timer.start();
        gamePane.setTimeDisplayNumber(++secondsPassed); //Start from value 1.
    }
    
    /**
     * Stops the game timer,
     */
    private void stopTimer(){
        if (timer != null)
            timer.stop();
        timerOn = false;
    }


    /**
     * Sets the flag for a cell with a given coordinate and
     * updates flag counter display.
     */
    private void setFlag(Coordinate coordinate){
        int flagCount = field.setFlag(coordinate, true);
        gamePane.setFlagDisplayNumber(flagCount);
    }

    
    /**
     * Removes the flag from a cell with a given coordinate and
     * updates flag counter display.
     */
    private void removeFlag(Coordinate coordinate){
        int flagCount = field.setFlag(coordinate, false);
        gamePane.setFlagDisplayNumber(flagCount);
    }

    @Override
    public void leftButtonActivated(Coordinate coordinate) {
    	
        if (field.getCellInfo(coordinate.x, coordinate.y).hasFlag()) //Ignore if the cell has flag.
            return;
    	
        if (!timerOn){
            startTimer();
            timerOn = true;
        }
        
        if (!minesSet){
            field.putMines(coordinate);
            minesSet = true;
        }
        
        List<Coordinate> newlyRevealedCells = field.revealCell(coordinate);
        gamePane.updateMineField(newlyRevealedCells);
        if (field.wasMineHit())
            gameOver(false);
        else if (field.isGameWon())
            gameOver(true);
        
        //System.out.println(field.debugImg());
    }
    
    @Override
    public void rightButtonActivated(Coordinate coordinate) {
            CellInfo info = field.getCellInfo(coordinate.x, coordinate.y);
        	
            if (info.hasFlag()){ //If has flag, remove flag and add question mark.
                assert !info.hasQuestionMark();
                removeFlag(coordinate);
                if (options.hasQuestionMarks())
                    field.setQuestionMark(coordinate, true);
            }else if (info.hasQuestionMark()){ //Has question mark, remove question mark (flag shout not be there!).
                assert !info.hasFlag();
                field.setQuestionMark(coordinate, false);
            }else{ //info.hasFlag() == false && info.hasQuestionMark() == false
                assert !(info.hasQuestionMark() || info.hasFlag());
        		
                if (field.getLeftFlagsCount()  == 0) //Ignore if we cannot set more flags.
                    return;
                setFlag(coordinate);
        	}
        	
            gamePane.updateMineField(coordinate);
    }
}
