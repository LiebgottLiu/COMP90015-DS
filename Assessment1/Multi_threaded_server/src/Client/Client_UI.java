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
import javax.swing.JOptionPane;
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
import javax.swing.JTextPane;

public class Client_UI extends Thread{

	private static final long seriaVersionUID = 1L;
	private JFrame frame = new JFrame();
	private JTextField txtEnterWorld;
	private JTextField textField;
	static JTextArea textDictionary;
	static JEditorPane editorPane;

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
		textField.setToolTipText("World");
		textField.setBounds(10, 59, 114, 21);
		frame.getContentPane().add(textField);
		textField.setColumns(10);

		

		editorPane = new JEditorPane();
		editorPane.setToolTipText("Means");
		editorPane.setEditable(false);
		editorPane.setBounds(10, 109, 138, 49);
		frame.getContentPane().add(editorPane);

		JComboBox comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"Add", "Delet", "Search", "Update"}));
		comboBox.setToolTipText("typebar");
		comboBox.setBounds(205, 26, 84, 32);
		frame.getContentPane().add(comboBox);

		JLabel lblNewLabel = new JLabel("Dictionary");
		lblNewLabel.setForeground(UIManager.getColor("Button.foreground"));
		lblNewLabel.setFont(new Font("Candara", Font.BOLD | Font.ITALIC, 24));
		lblNewLabel.setBounds(10, 10, 114, 39);
		frame.getContentPane().add(lblNewLabel);



		JLabel lblNewLabel_1 = new JLabel("Enter World");
		lblNewLabel_1.setBounds(10, 43, 76, 15);
		frame.getContentPane().add(lblNewLabel_1);

		JLabel lblNewLabel_2 = new JLabel("World Meaning");
		lblNewLabel_2.setBounds(10, 89, 98, 15);
		frame.getContentPane().add(lblNewLabel_2);

		JLabel lblNewLabel_3 = new JLabel("New Meaning(op)");
		lblNewLabel_3.setBounds(10, 168, 114, 15);
		frame.getContentPane().add(lblNewLabel_3);
		
		JTextArea textArea = new JTextArea();
		textArea.setToolTipText("inpu_means");
		textArea.setBounds(10, 182, 138, 69);
		frame.getContentPane().add(textArea);
		
		textDictionary = new JTextArea();
		textDictionary.setToolTipText("Server_text");
		textDictionary.setEditable(false);
		textDictionary.setBounds(158, 95, 266, 156);
		frame.getContentPane().add(textDictionary);
		
		JLabel lblNewLabel_4 = new JLabel("Server message");
		lblNewLabel_4.setBounds(158, 67, 131, 21);
		frame.getContentPane().add(lblNewLabel_4);
		
		JButton btnNewButton_1 = new JButton("Send Request");
		btnNewButton_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				String selectedOption = (String) comboBox.getSelectedItem();
				String word = textField.getText();
				String means = textArea.getText();
				if((selectedOption == "Update" ||selectedOption == "Add") && (means == "" || means == null)) {
					JOptionPane.showMessageDialog(frame, "Please enter the meaning of the word.", "Missing Meaning", JOptionPane.WARNING_MESSAGE);
		            return;
				}

//				System.out.println(selectedOption);
				textDictionary.setText(null);
				editorPane.setText(null);
				textArea.setText(null);
				String request = "";
				
				if((word.equals("") || means.equals("")) &&(selectedOption == "Update" ||selectedOption == "Add") ) {
					JOptionPane.showMessageDialog(btnNewButton_1 ,"Missing either word or meanning ");
					return;
				}
				if((word.equals("")) && (selectedOption == "Delet" ||selectedOption == "Search") ) {
					JOptionPane.showMessageDialog(btnNewButton_1 ,"Please Enter the word ");
					return;
				}
				
				switch (selectedOption){
				case "Add":
					request = "Add" + ":" + word + "/" + means +"\n";
					break;
				case "Delet":
					request = "Delet" + ":" + word + "/" + means +"\n";
					break;
				case "Search":
					request = "Search" + ":" + word + "/" + means +"\n";
					break;
				case "Update":
					request = "Update" + ":" + word + "/" + means +"\n";
					break;
				}
				System.out.println(request);

				try {
					Client.writer.write(request);
					Client.writer.flush();
				}catch(Exception e1) {
					e1.printStackTrace();
				}
				
			}
		});
		btnNewButton_1.setBounds(299, 26, 125, 32);
		frame.getContentPane().add(btnNewButton_1);
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
