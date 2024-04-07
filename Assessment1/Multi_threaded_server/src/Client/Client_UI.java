package Client;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JScrollBar;
import javax.swing.JProgressBar;
import javax.swing.JEditorPane;
import javax.swing.JToolBar;
import javax.swing.JMenuBar;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JToggleButton;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.JTextArea;
import java.awt.event.ActionEvent;

public class Client_UI extends Thread{
	
	private static final long seriaVersionUID = 1L;
	private JFrame frame = new JFrame();
	private JTextField txtEnterWorld;
	private JTextField textField;

	/**
	 * Launch the application.
	 */
	public void run() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Client_UI window = new Client_UI();
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
	public Client_UI() {
		frame.getContentPane().setLayout(null);
		
		JButton btnNewButton = new JButton("Send Request");
		btnNewButton.setBounds(316, 0, 118, 48);
		
		frame.getContentPane().add(btnNewButton);
		
		JEditorPane dtrpnEnterWorldMeaning = new JEditorPane();
		dtrpnEnterWorldMeaning.setText("Enter world Meaning");
		dtrpnEnterWorldMeaning.setBounds(32, 129, 131, 107);
		frame.getContentPane().add(dtrpnEnterWorldMeaning);
		
		JComboBox comboBox = new JComboBox();
		comboBox.setFont(new Font("Century Schoolbook", Font.PLAIN, 12));
		comboBox.setToolTipText("Select one\r\n");
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"Adding", "Update", "Delet", "Searching"}));
		comboBox.setBounds(228, -1, 88, 48);
		frame.getContentPane().add(comboBox);
		
		txtEnterWorld = new JTextField();
		txtEnterWorld.setText("Enter world");
		txtEnterWorld.setBounds(32, 87, 131, 30);
		frame.getContentPane().add(txtEnterWorld);
		txtEnterWorld.setColumns(10);
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		textField = new JTextField();
		textField.setBounds(33, 88, 98, 21);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		JEditorPane editorPane = new JEditorPane();
		editorPane.setBounds(33, 132, 98, 69);
		frame.getContentPane().add(editorPane);
		
		JComboBox comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"Add", "Delet", "Search", "Update"}));
		comboBox.setToolTipText("typebar");
		comboBox.setBounds(254, 26, 67, 23);
		frame.getContentPane().add(comboBox);
		
		JLabel lblNewLabel = new JLabel("Dictionary");
		lblNewLabel.setForeground(UIManager.getColor("Button.foreground"));
		lblNewLabel.setFont(new Font("Candara", Font.BOLD | Font.ITALIC, 24));
		lblNewLabel.setBounds(10, 10, 114, 39);
		frame.getContentPane().add(lblNewLabel);
		
		JButton btnNewButton_1 = new JButton("New button");
		btnNewButton_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String selectedOption = (String) comboBox.getSelectedItem();
		        System.out.println(selectedOption);
			}
		});
		btnNewButton_1.setBounds(331, 26, 93, 23);
		frame.getContentPane().add(btnNewButton_1);
		
		JTextArea textArea = new JTextArea();
		textArea.setBounds(228, 145, 93, 56);
		frame.getContentPane().add(textArea);
		
		JLabel lblNewLabel_1 = new JLabel("New label");
		lblNewLabel_1.setBounds(33, 74, 54, 15);
		frame.getContentPane().add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("New label");
		lblNewLabel_2.setBounds(33, 119, 54, 15);
		frame.getContentPane().add(lblNewLabel_2);
	}
	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}
}
