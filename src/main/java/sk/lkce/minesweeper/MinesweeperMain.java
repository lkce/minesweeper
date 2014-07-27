package sk.lkce.minesweeper;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.DefaultButtonModel;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;
import javax.swing.UnsupportedLookAndFeelException;

import sk.lkce.minesweeper.gui.AboutDialog;
import sk.lkce.minesweeper.gui.GameController;
import sk.lkce.minesweeper.gui.GameOptions;
import sk.lkce.minesweeper.gui.GameOptions.Difficulty;
import sk.lkce.minesweeper.gui.GameView;
import sk.lkce.minesweeper.gui.ResourceLoader;
import sk.lkce.minesweeper.gui.ResourceLoadingException;
import sk.lkce.minesweeper.gui.SoundPlayer;

/**
 * Entry class of the application. Contains also inner action classes.
 */
public class MinesweeperMain {

	/**
	 * Menu actions enum.
	 */
	enum MenuAction {
		NEW_GAME("New game"), BEGINNER("Beginner"), INTERMEDIATE("Intermediate"), EXPERT(
				"Expert"), QUESTION_MARKS("Marks (?)"), SOUND("Sound"), EXIT(
				"Exit");

		private final String name;

		private MenuAction(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

	};

	private final String TITLE = "Minesweeper";
	private JMenuBar menuBar;
	private GameController gameController;
	private GameOptions options;
	private JFrame frame;
	private SoundPlayer soundPlayer;
	private Map<MenuAction, Action> actions = new HashMap<>();

	/**
	 * Default constructor.
	 */
	public MinesweeperMain() {

		// TODO review it

		try {
			String lookAndFeel = javax.swing.UIManager
					.getSystemLookAndFeelClassName();

			System.out.println(lookAndFeel);
			if (lookAndFeel.endsWith("MetalLookAndFeel")) // This might be Linux
															// so let's try GTK
															// look and feel.
				lookAndFeel = "com.sun.java.swing.plaf.gtk.GTKLookAndFeel";

			javax.swing.UIManager.setLookAndFeel(lookAndFeel);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			ResourceLoader.getInstance().initialize();
		} catch (ResourceLoadingException e) {
			e.printStackTrace();
		}

		createGame();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		new MinesweeperMain();
	}

	/**
	 * Constructs the fundamental game objects and game view.
	 */
	private void createGame() {

		options = new GameOptions();
		GameView gamePane = new GameView();
		soundPlayer = new SoundPlayer();
		try {
			soundPlayer.initialize();
		} catch (ResourceLoadingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		gameController = new GameController(options, gamePane, soundPlayer);

		actions = createActions();
		menuBar = createMenuBar();

		frame = new JFrame();
		frame.setTitle(TITLE);
		frame.setIconImage(ResourceLoader.getInstance().getApplicationIcon());
		frame.setResizable(false);
		frame.setJMenuBar(menuBar);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// window.setSize(WIDTH, HEIGHT);

		frame.add(gamePane);
		frame.pack();
	}

	/**
	 * Creates all menu actions.
	 * 
	 * @return menu action map
	 */
	private Map<MenuAction, Action> createActions() {
		Map<MenuAction, Action> result = new HashMap<>();

		NewGameAction newGame = new NewGameAction(MenuAction.NEW_GAME.getName());
		newGame.putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0));

		result.put(MenuAction.NEW_GAME, newGame);
		result.put(MenuAction.BEGINNER,
				new NewGameAction(MenuAction.BEGINNER.getName(),
						Difficulty.EASY));
		result.put(MenuAction.INTERMEDIATE, new NewGameAction(
				MenuAction.INTERMEDIATE.getName(), Difficulty.MEDIUM));
		result.put(MenuAction.EXPERT,
				new NewGameAction(MenuAction.EXPERT.getName(), Difficulty.HARD));
		result.put(MenuAction.QUESTION_MARKS, new ToggleQuestionMarksAction(
				MenuAction.QUESTION_MARKS.getName()));
		result.put(MenuAction.SOUND,
				new ToggleSoundAction(MenuAction.SOUND.getName()));
		result.put(MenuAction.EXIT,
				new ExitGameAction(MenuAction.EXIT.getName()));

		return result;
	}

	/**
	 * Creates game view menu bar
	 * 
	 * @return create menu bar
	 */
	@SuppressWarnings("serial")
	private JMenuBar createMenuBar() {
		JMenuBar resultMenuBar = new JMenuBar();
		JMenu gameMenu = new JMenu("Game");

		gameMenu.add(actions.get(MenuAction.NEW_GAME));
		gameMenu.addSeparator();

		ButtonGroup group = new ButtonGroup();

		JRadioButtonMenuItem beginner = new JRadioButtonMenuItem(
				actions.get(MenuAction.BEGINNER));
		group.add(beginner);
		gameMenu.add(beginner);

		JRadioButtonMenuItem intermediate = new JRadioButtonMenuItem(
				actions.get(MenuAction.INTERMEDIATE));
		group.add(intermediate);
		gameMenu.add(intermediate);

		JRadioButtonMenuItem expert = new JRadioButtonMenuItem(
				actions.get(MenuAction.EXPERT));
		group.add(expert);
		gameMenu.add(expert);

		// Based on the set difficulty, set the selected item in menu.
		if (options.getDifficulty() == Difficulty.EASY)
			beginner.setSelected(true);
		else if (options.getDifficulty() == Difficulty.MEDIUM)
			intermediate.setSelected(true);
		else if (options.getDifficulty() == Difficulty.HARD)
			expert.setSelected(true);

		gameMenu.addSeparator();

		JCheckBoxMenuItem marks = new JCheckBoxMenuItem(
				actions.get(MenuAction.QUESTION_MARKS));
		marks.setModel(new DefaultButtonModel() {
			@Override
			public boolean isSelected() {
				return options.hasQuestionMarks();
			}
		});
		// marks.setSelected(options.hasQuestionMarks());
		gameMenu.add(marks);

		JCheckBoxMenuItem sound = new JCheckBoxMenuItem(
				actions.get(MenuAction.SOUND));
		sound.setModel(new DefaultButtonModel() {
			@Override
			public boolean isSelected() {
				return options.isSound();
			}
		});
		gameMenu.add(sound);

		gameMenu.addSeparator();

		gameMenu.add(actions.get(MenuAction.EXIT));

		resultMenuBar.add(gameMenu);
		JMenu helpMenu = new JMenu("Help");
		helpMenu.add(new ShowAboutAction());

		resultMenuBar.add(helpMenu);

		return resultMenuBar;
	}

	/**
	 * An action which starts a new game.
	 */
	@SuppressWarnings("serial")
	private class NewGameAction extends AbstractAction {

		Difficulty difficulty;

		public NewGameAction(String name) {
			super(name);
		}

		public NewGameAction(String name, Difficulty difficulty) {
			this(name);
			this.difficulty = difficulty;
		}

		@Override
		public void actionPerformed(ActionEvent e) {

			if (difficulty != null) {
				options.setDifficulty(difficulty);
			}

			gameController.startNewGame();
			frame.pack();
		}

	}

	/**
	 * An action which either turns off or on the question mark feature of the
	 * game based on whether this feature is on or off.
	 */
	@SuppressWarnings("serial")
	private class ToggleQuestionMarksAction extends AbstractAction {

		public ToggleQuestionMarksAction(String name) {
			super(name);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			options.setQuestionMarks(!options.hasQuestionMarks());
		}
	}

	/**
	 * An action which either turns on or off the game sound based whether the
	 * sound is off or on.
	 */
	@SuppressWarnings("serial")
	private class ToggleSoundAction extends AbstractAction {

		public ToggleSoundAction(String name) {
			super(name);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			options.setSound(!options.isSound());
		}
	}

	/**
	 * An action which terminates the execution of the application.
	 */
	@SuppressWarnings("serial")
	private class ExitGameAction extends AbstractAction {

		public ExitGameAction(String name) {
			super(name);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println("Bye bye!");
			System.exit(0);
		}
	}

	/**
	 * An action which shows the 'about' pop-up window with informations about
	 * the application and author.
	 */
	@SuppressWarnings("serial")
	private class ShowAboutAction extends AbstractAction {

		public ShowAboutAction() {
			super("About");
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			JDialog dialog = new AboutDialog(frame);
			dialog.setVisible(true);
		}
	}
}
