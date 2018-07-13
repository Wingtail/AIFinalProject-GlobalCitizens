package NEATImplementation;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import java.awt.BorderLayout;
import javax.swing.JPanel;

public class NetworkRecognition extends JComponent implements ActionListener{

	/**
	 * 
	 */
	public NetworkRecognition() {
		
	}
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		NetworkRecognition renderer = new NetworkRecognition();
		
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JTextArea textArea = new JTextArea();
		textArea.setRows(5);
		frame.getContentPane().add(textArea, BorderLayout.SOUTH);
		
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		panel.add(renderer);
		
		frame.pack();
		frame.setVisible(true);
	}

	/**
	 * Create the application.
	 */
	

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		initialize();
		
	}
}
