import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.BoxLayout;
import java.awt.Choice;
import java.awt.Button;
import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;
import org.eclipse.wb.swing.FocusTraversalOnArray;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Label;

public class Dic_Client {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Dic_Client window = new Dic_Client();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Dic_Client() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setForeground(SystemColor.windowText);
		frame.getContentPane().setFont(new Font("SimSun", Font.PLAIN, 12));
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.X_AXIS));
		
		Choice choice = new Choice();
		choice.setBackground(new Color(255, 255, 255));
		frame.getContentPane().add(choice);
		
		Button button = new Button("Send Request");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		button.setForeground(SystemColor.inactiveCaptionText);
		button.setFont(new Font("Century", Font.ITALIC, 13));
		button.setActionCommand("Send");
		frame.getContentPane().add(button);
		frame.getContentPane().setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{choice, button}));
	}
}
