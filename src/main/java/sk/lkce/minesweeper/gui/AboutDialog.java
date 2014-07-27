package sk.lkce.minesweeper.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * A modal dialog which shows the information about the application and the
 * author.
 */
@SuppressWarnings("serial")
public class AboutDialog extends JDialog {

	private final static int HEIGHT = 160;
	private final static int WIDTH = 250;

	public AboutDialog(Frame frame) {
		super(frame, true);
		setup();
		setLocationRelativeTo(frame);
		setTitle("About");
		this.setSize(WIDTH, HEIGHT);

	}

	/**
	 * Creates and adds all the dialog components. 
	 */
	private void setup() {
		JLabel label = new JLabel();
		label.setText("<html>This is a Java Swing <br>"
				+ "implementation of Microsoft's <br>classic Windows XP Minesweeper."
				+ "<br<i>Version 1.0<i/>" +
				"</html>");
		JButton button = new JButton("Ok");
		button.setPreferredSize(new Dimension(50, 20));
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				AboutDialog.this.dispose();
			}

		});

		JPanel panel = new JPanel();

		JPanel buttonPanel = new JPanel();
		buttonPanel.add(button);

		panel.add(label);

		add(panel);
		add(buttonPanel, BorderLayout.SOUTH);
	}
	
}
