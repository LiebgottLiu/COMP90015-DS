//Student Name: Zhuoyang Liu
//Student ID: 917183

package Server;

import java.awt.EventQueue;
import java.awt.Font;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;

public class Server_UI extends Thread {

	private JFrame frame;
	static JTextArea clientInfoTextArea;
	static JLabel lblConnectedClients;
	private int connectedClientsCount = 0;

	public void run() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Server_UI window = new Server_UI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Server_UI() {
		initialize();
	}

	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 589, 407);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JLabel lblNewLabel = new JLabel("Dict Server");
		lblNewLabel.setFont(new Font("Arial", Font.PLAIN, 29));
		lblNewLabel.setForeground(UIManager.getColor("menuText"));
		lblNewLabel.setBounds(10, 10, 417, 49);
		frame.getContentPane().add(lblNewLabel);

		lblConnectedClients = new JLabel("Connected Clients: 0");
		lblConnectedClients.setFont(new Font("Arial", Font.PLAIN, 18));
		lblConnectedClients.setBounds(10, 70, 200, 25);
		frame.getContentPane().add(lblConnectedClients);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 105, 553, 230);
		frame.getContentPane().add(scrollPane);

		clientInfoTextArea = new JTextArea();
		clientInfoTextArea.setEditable(false);
		scrollPane.setViewportView(clientInfoTextArea);
	}

	public void updateClientInfo(String clientID, String requestType) {
		String currentText = clientInfoTextArea.getText();
		String newText = "Client ID: " + clientID + " | Request Type: " + requestType + "\n";
		clientInfoTextArea.setText(currentText + newText);
	}

	public synchronized void incrementConnectedClients() {
		connectedClientsCount++;
		lblConnectedClients.setText("Connected Clients: " + connectedClientsCount);
	}

	public synchronized void decrementConnectedClients() {
		connectedClientsCount--;
		lblConnectedClients.setText("Connected Clients: " + connectedClientsCount);
	}
}

