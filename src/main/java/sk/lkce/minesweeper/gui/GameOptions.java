package sk.lkce.minesweeper.gui;

/**
 * A data holder object which stored the current game settings.
 */
public class GameOptions {

	/**
	 * A difficulty level of the minesweeper game. Contains definition of the
	 * field size and the number of mines.
	 */
	public enum Difficulty {
		EASY(9, 9, 10), MEDIUM(16, 16, 40), HARD(30, 16, 99);

		private final int rowCount, columnCount, mineCount;

		Difficulty(int columnCount, int rowCount, int mineCount) {
			this.rowCount = rowCount;
			this.columnCount = columnCount;
			this.mineCount = mineCount;
		}

		public int getRowCount() {
			return rowCount;
		}

		public int getColumnCount() {
			return columnCount;
		}

		public int getMineCount() {
			return mineCount;
		}

	};

	private boolean questionMarks = true;
	private int rowCount;
	private int columnCount;
	private int mineCount;
	private Difficulty difficulty;
	private boolean sound;

	/**
	 * Creates a game options object with the game difficulty set to medium.
	 */
	public GameOptions() {
		setDifficulty(Difficulty.MEDIUM);
	}

	/**
	 * Sets a game difficulty in this game-options.
	 * @param difficulty the difficulty of the game
	 */
	public void setDifficulty(Difficulty difficulty) {
		this.difficulty = difficulty;
		setColumnCount(difficulty.getColumnCount());
		setRowCount(difficulty.getRowCount());
		setMineCount(difficulty.getMineCount());
	}

	/**
	 * Returns the game difficulty stored in these game options.
	 */
	public Difficulty getDifficulty() {
		return difficulty;
	}
	
	/**
	 * Returns the value of 'question marks' flag stored in these game options.
	 * @return <code>true</code> if the question marks are enabled
	 */
	public boolean hasQuestionMarks() {
		return questionMarks;
	}
	
	/**
	 * Sets the 'question marks' flag of these game options to a given value.
	 * @param questionMarks
	 */
	public void setQuestionMarks(boolean questionMarks) {
		this.questionMarks = questionMarks;
	}

	/**
	 * Returns the set number of rows of the mine-field grid. 
	 * @return the number of rows of mine-field grid.
	 */
	public int getRowCount() {
		return rowCount;
	}

	/**
	 * Sets the number of rows of the mine-field grid to a given value.
	 * @param rowCount the number of rows to be set
	 */
	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}

	/**
	 * Returns the set number of columns of the mine-field grid. 
	 * @return the number of columns of the mine-field grid.
	 */
	public int getColumCount() {
		return columnCount;
	}

	/**
	 * Sets the number of columns of the mine-field grid to a given value.
	 * @param rowCount the number of the columns to be set
	 */
	public void setColumnCount(int columnCount) {
		this.columnCount = columnCount;
	}

	/**
	 * Returns the set number of mines in the mine-field grid. 
	 * @return the number of mines in the mine-field grid.
	 */
	public int getMineCount() {
		return mineCount;
	}

	/**
	 * Sets the number of mines in the mine-field grid to a given value.
	 * @param rowCount the number in the mines to be set
	 */
	public void setMineCount(int mineCount) {
		this.mineCount = mineCount;
	}

	/**
	 * Determines if the sound is turned on in these game options.
	 * @return <code>true</code> if the sound is turned on, <code>false</code> if it is not
	 */
	public boolean isSound() {
		return sound;
	}

	/** Sets the value of sound-on flag in these game options.
	 * @param sound <code>true</code> if the game sounds should be played, <code>false</code> if otherwise
	 */
	public void setSound(boolean sound) {
		this.sound = sound;
	}

}
