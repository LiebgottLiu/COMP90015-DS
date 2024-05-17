package client;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Point;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

import javax.swing.GroupLayout;
import javax.swing.JPanel;

import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import javax.swing.ImageIcon;
import javax.swing.Icon;

import remote.CanvasClientInterface;
import remote.CanvasMessageInterface;
import remote.CanvasServerInterface;
import server.util;

import java.io.ByteArrayInputStream;
import static javax.swing.GroupLayout.Alignment;
public class Client extends UnicastRemoteObject implements CanvasClientInterface {
	
	static CanvasServerInterface server;
	
	//access control
	private boolean isManager;
	private boolean havePermission;
	
	//UI pages
	private JFrame frame;
	private DefaultListModel<String> userList;
	private DefaultListModel<String> chatList;
	private JButton clearBtn, saveBtn, saveAsBtn, openBtn, closeButton;
	private JButton blackBtn, blueBtn, yellowBtn, redBtn, greenBtn, pinkBtn, purpleBtn, orangeBtn, grayBtn, limeBtn, magentaBtn, aoiBtn, skyBtn, cyanBtn, lightGrayBtn,bronBtn;

	private JButton drawBtn, lineBtn,rectBtn,circleBtn,ovalBtn,textBtn,eraserBtn,setSizeBtn;
	private JScrollPane msgArea;
	private JTextArea tellColor, displayColor;
	private JList<String> chat;
	private ArrayList<JButton> btnList;
	private Canvas canvasUI;
	
	private float size ;
	private String clientName;
	private String picName;
	private String picPath;
	private Hashtable<String, Point> startPoints = new Hashtable<String,Point>();
	
	private String ioMEssage = "There is an IO error, please double check";
	

	protected Client() throws RemoteException {
		this.userList = new DefaultListModel<>();
		this.isManager = false;
		this.havePermission = true;
		this.chatList = new DefaultListModel<>();
		this.btnList = new ArrayList<>();
	}

	// keep on listening on the client UI mouse actions
	ActionListener actionListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			
			//the chosen mode will be boxed with black border
			LineBorder empty = new LineBorder(new Color(238,238,238),2);
			LineBorder box = new LineBorder(Color.black,2);
			 
			//init the function buttons 
			buttonFunctions(e.getSource());
			//the chosen color will be boxed with black border
			drawButtons(e,empty,box);

			

			
			//select the color buttons 
			if (e.getSource() == blackBtn || e.getSource() == blueBtn || e.getSource() == yellowBtn || 
    			e.getSource() == redBtn || e.getSource() == greenBtn || e.getSource() == pinkBtn || 
    			e.getSource() == purpleBtn || e.getSource() == orangeBtn || e.getSource() == grayBtn || 
    			e.getSource() == limeBtn || e.getSource() == magentaBtn || e.getSource() == aoiBtn || 
    			e.getSource() == skyBtn || e.getSource() == cyanBtn || e.getSource() == lightGrayBtn ||
				e.getSource() == bronBtn) {
    				displayColor.setBackground(canvasUI.getCurrentColor());
				}
		}

		//select the function buttons
		private void drawButtons(ActionEvent e, LineBorder empty, LineBorder box) {
			if(e.getSource() == drawBtn){
				canvasUI.draw();
				for(JButton button : btnList) {
					if(button == drawBtn) {
						button.setBorder(box);
					}else {
						button.setBorder(empty);
					}
				}
			}else if (e.getSource() == lineBtn) {
				canvasUI.line();
				for(JButton button : btnList ) {
					if(button == lineBtn) {
						button.setBorder(box);
					}else {
						button.setBorder(empty);
					}
				}
			}else if (e.getSource() == rectBtn) {
				canvasUI.rect();
				for(JButton button : btnList ) {
					if(button == rectBtn) {
						button.setBorder(box);
					}else {
						button.setBorder(empty);
					}
				}
			}else if (e.getSource() == circleBtn) {
				canvasUI.circle();
				for(JButton button : btnList ) {
					if(button == circleBtn) {
						button.setBorder(box);
					}else {
						button.setBorder(empty);
					}
				}
			}else if (e.getSource() == ovalBtn) {
				canvasUI.oval();
				for(JButton button : btnList ) {
					if(button == ovalBtn) {
						button.setBorder(box);
					}else {
						button.setBorder(empty);
					}
				}
			}else if (e.getSource() == textBtn) {
				canvasUI.text();
				for(JButton button : btnList ) {
					if(button == textBtn) {
						button.setBorder(box);
					}else {
						button.setBorder(empty);
					}
				}
			}else if (e.getSource() == eraserBtn) {
				canvasUI.eraser();
				for(JButton button : btnList ) {
					if(button == eraserBtn) {
						button.setBorder(box);
					}else {
						button.setBorder(empty);
					}
				}
			}else if(e.getSource() == setSizeBtn){
				
				String input = "";
				boolean validName = false;

				while (!validName) {
					input = JOptionPane.showInputDialog("Please enter pen size here (from 1-20):");
					try {
						
						if (Float.parseFloat(input) < 1 || Float.parseFloat(input) > 20) {
							JOptionPane.showMessageDialog(null, "Please enter a valid pen size between 1 and 20.");
						} else {
							validName = true;
							// Append 'f' to convert float to string
							size = Float.parseFloat(input+ "f") ;
							JOptionPane.showMessageDialog(null, size);
						}
					} catch (NumberFormatException ex) {
						JOptionPane.showMessageDialog(null, "Invalid input. Please enter a valid number.");
					}
				}

			}
		}
	};
		
	//buttonfunction facteds;
	private void buttonFunctions(Object event){
		functionButtons(event);
		colorButtons(event);
	}

	// color button selections , set the color to canvas
	private void colorButtons(Object event) {
		if (event == blackBtn) {
			canvasUI.black();
		} else if (event == blueBtn) {
			canvasUI.blue();
		} else if (event == yellowBtn) {
			canvasUI.yellow();
		} else if (event == redBtn) {
			canvasUI.red();
		} else if (event == greenBtn) {
			canvasUI.green();
		} else if (event == pinkBtn) {
			canvasUI.pink();
		} else if (event == purpleBtn) {
			canvasUI.purple();
		} else if (event == orangeBtn) {
			canvasUI.orange();
		} else if (event == grayBtn) {
			canvasUI.gray();
		} else if (event == limeBtn) {
			canvasUI.lime();
		} else if (event == magentaBtn) {
			canvasUI.magenta();
		} else if (event == aoiBtn) {
			canvasUI.aoi();
		} else if (event == skyBtn) {
			canvasUI.sky();
		} else if (event == cyanBtn) {
			canvasUI.cyan();
		} else if (event == lightGrayBtn) {
			canvasUI.lightGray();
		}else if (event == bronBtn) {
			canvasUI.brown();
		}
	}



	// select the manager function buttons
	private void functionButtons(Object event){
	    // the buttons on the right side of the screen
		if(event == clearBtn){
			clearLog();
		}else if(event == openBtn){
		    openLog();
		}else if(event == saveBtn){
		    saveLog();
		}else if(event == saveAsBtn){
		    saveAsLog();
		}else if(event == closeButton){
			closeLog();
		}
	}

	//new board logic
	private void clearLog(){
	    int choice = JOptionPane.showConfirmDialog(frame, "Do you want to creat a new page?", 
		"New Page Confirmation", JOptionPane.YES_NO_OPTION);
		if (choice == JOptionPane.YES_OPTION) {
			// User clicked "Yes"
			canvasUI.reset();
		}
		if(isManager) {
			managerUpdate();
		}
	}

	//update the manager canvas to user
	private void managerUpdate(){
		try {
			server.refreshCanvas();
		} catch (RemoteException e1) {
			Util.popupDialog("Canvas server is down, please save and exit!");
		}
	}

	//close button logic
	private void closeLog(){
		if (JOptionPane.showConfirmDialog(frame,
								"Do you want to close the Canvas", "Close Paint Board?", 
								JOptionPane.YES_NO_OPTION, 
								JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
							try {
								server.removeAll();
								updateUserList(server.getClientList());
							}catch(RemoteException e) {
								Util.popupDialog("Canvas server is down, please save and exit.");
								
							} catch (IOException e) {
								Util.popupDialog(ioMEssage);
							}finally {
								System.exit(0);
							}
								
						}
	    
	}

	//save As logic
	private void saveAsLog(){
		try {
			saveAs();
		}catch(IOException e1) {
			util.errorMessage(ioMEssage);
		}
	}

	//save image logic
	private void saveLog(){
	    try {
			save();
		}catch(IOException e1) {
			util.errorMessage(ioMEssage);
		}
	}

	//openlocal image logic
	private void openLog(){
		try {
			open();
		}catch(IOException e1) {
			util.errorMessage(ioMEssage);
		}
	}


	// open loacl iamges 
	private void open() throws IOException{
		FileDialog opendialog = new FileDialog(frame, "open an iamge", FileDialog.LOAD);
		opendialog.setVisible(true);
		if (opendialog.getFile() != null) {
			this.picPath = opendialog.getDirectory();
			this.picName = opendialog.getFile();
			BufferedImage image = ImageIO.read(new File(picPath + picName));
			canvasUI.drawImage(image);
			
			ByteArrayOutputStream imageArray = new ByteArrayOutputStream();
			ImageIO.write(image, "png", imageArray);
			server.sedOpenedImage(imageArray.toByteArray());
		}
	}
	
	//saveAs local images
	private void saveAs() throws IOException{
		FileDialog saveAsdialog = new FileDialog(frame, "save image", FileDialog.SAVE);
		saveAsdialog.setVisible(true);
		if(saveAsdialog.getFile() != null) {
			this.picPath = saveAsdialog.getDirectory();
			this.picName = saveAsdialog.getFile();
			ImageIO.write(canvasUI.getCanvas(), "png", new File(picPath + picName));
		}
	}

	//save to local
	private void save()throws IOException{
		if(picName == null) {
			saveAs() ;
		}else {
			ImageIO.write(canvasUI.getCanvas(), "png", new File(picPath + picName));
		}
	}

	
	// verify the client name are unick
	private static void UnickNameCheck(CanvasClientInterface client, CanvasServerInterface server2) throws HeadlessException, RemoteException {
		boolean validName = false;
		String client_name = "";
		while(!validName) {
			client_name = JOptionPane.showInputDialog("Please type your name here");
			// if user do not enter a name it will ask user to input one 
			if(client_name.equals("")) {
				JOptionPane.showMessageDialog(null, "Please Enter your name");
			}else {
				validName = true;
			}
			for(CanvasClientInterface c : server.getClientList()) {
				if(client_name.equals(c.getName()) || c.getName().equals("*" + client_name)) {
					validName = false;
					JOptionPane.showMessageDialog(null, "The name is taken, please Enter a new name!");
				}
			}
		}
		client.setName(client_name);
	}

	private void setColorButtons(){
		//set the color button
		blackBtn = new JButton();
		blackBtn.setBackground(Color.black);
		blackBtn.setBorderPainted(false);
		blackBtn.setOpaque(true);
		blackBtn.addActionListener(actionListener);
		
		blueBtn = new JButton();
		blueBtn.setBackground(Color.blue);
		blueBtn.setBorderPainted(false);
		blueBtn.setOpaque(true);
		blueBtn.addActionListener(actionListener);
		
		yellowBtn = new JButton();
		yellowBtn.setBackground(Color.yellow);
		yellowBtn.setBorderPainted(false);
		yellowBtn.setOpaque(true);
		yellowBtn.addActionListener(actionListener);
		
		redBtn = new JButton();
		redBtn.setBackground(Color.red);
		redBtn.setBorderPainted(false);
		redBtn.setOpaque(true);
		redBtn.addActionListener(actionListener);
		
		greenBtn = new JButton();
		greenBtn.setBackground(Color.green);
		greenBtn.setBorderPainted(false);
		greenBtn.setOpaque(true);
		greenBtn.addActionListener(actionListener);
		
		pinkBtn = new JButton();
		pinkBtn.setBackground(new Color(255,153,204)); // Pink
		pinkBtn.setBorderPainted(false);
		pinkBtn.setOpaque(true);
		pinkBtn.addActionListener(actionListener);
		
		purpleBtn = new JButton();
		purpleBtn.setBackground(new Color(102,0,204)); // Purple
		purpleBtn.setBorderPainted(false);
		purpleBtn.setOpaque(true);
		purpleBtn.addActionListener(actionListener);
		
		orangeBtn = new JButton();
		orangeBtn.setBackground(Color.orange);
		orangeBtn.setBorderPainted(false);
		orangeBtn.setOpaque(true);
		orangeBtn.addActionListener(actionListener);
		
		grayBtn = new JButton();
		grayBtn.setBackground(Color.gray);
		grayBtn.setBorderPainted(false);
		grayBtn.setOpaque(true);
		grayBtn.addActionListener(actionListener);
		
		limeBtn = new JButton();
		limeBtn.setBackground(new Color(102,102,0)); // Lime
		limeBtn.setBorderPainted(false);
		limeBtn.setOpaque(true);
		limeBtn.addActionListener(actionListener);
		
		magentaBtn = new JButton();
		magentaBtn.setBackground(Color.magenta);
		magentaBtn.setBorderPainted(false);
		magentaBtn.setOpaque(true);
		magentaBtn.addActionListener(actionListener);
		
		aoiBtn = new JButton();
		aoiBtn.setBackground(new Color(0,102,102)); // AOI
		aoiBtn.setBorderPainted(false);
		aoiBtn.setOpaque(true);
		aoiBtn.addActionListener(actionListener);

		bronBtn = new JButton();
		bronBtn.setBackground(new Color(102,51,0)); // Brown
		bronBtn.setBorderPainted(false);
		bronBtn.setOpaque(true);
		bronBtn.addActionListener(actionListener);
		
		skyBtn = new JButton();
		skyBtn.setBackground(new Color(0,128,255)); // Sky
		skyBtn.setBorderPainted(false);
		skyBtn.setOpaque(true);
		skyBtn.addActionListener(actionListener);
		
		cyanBtn = new JButton();
		cyanBtn.setBackground(Color.cyan);
		cyanBtn.setBorderPainted(false);
		cyanBtn.setOpaque(true);
		cyanBtn.addActionListener(actionListener);
		
		lightGrayBtn = new JButton();
		lightGrayBtn.setBackground(Color.LIGHT_GRAY);
		lightGrayBtn.setBorderPainted(false);
		lightGrayBtn.setOpaque(true);
		lightGrayBtn.addActionListener(actionListener);
	}
	
	private void settingIcons() {
		// Set line border for buttons
		LineBorder border = new LineBorder(Color.black, 2);
	
		// Create buttons
		createDrawButton(border);
		border = new LineBorder(new Color(238,238,238),2);
		createLineButton(border);
		createRectButton(border);
		createCircleButton(border);
		createOvalButton(border);
		createTextButton(border);
		createEraserButton(border);
		setPaintSizeButton(border);
	}
	
	private void createDrawButton(LineBorder border) {
		Icon icon = new ImageIcon(getClass().getResource("/icon/pencil.png"));
		JButton button = createButton(icon, "free draw", border);
		drawBtn = button;
	}
	
	private void createLineButton(LineBorder border) {
		Icon icon = new ImageIcon(getClass().getResource("/icon/nodes.png"));
		JButton button = createButton(icon, "Draw line", border);
		lineBtn = button;
	}
	
	private void createRectButton(LineBorder border) {
		Icon icon = new ImageIcon(getClass().getResource("/icon/rectangular-shape-outline.png"));
		JButton button = createButton(icon, "Draw rectangle", border);
		rectBtn = button;
	}
	
	private void createCircleButton(LineBorder border) {
		Icon icon = new ImageIcon(getClass().getResource("/icon/oval.png"));
		JButton button = createButton(icon, "Draw circle", border);
		circleBtn = button;
	}
	
	private void createOvalButton(LineBorder border) {
		Icon icon = new ImageIcon(getClass().getResource("/icon/aoval.png"));
		JButton button = createButton(icon, "Draw oval", border);
		ovalBtn = button;
	}
	
	private void createTextButton(LineBorder border) {
		Icon icon = new ImageIcon(getClass().getResource("/icon/text.png"));
		JButton button = createButton(icon, "Draw text", border);
		textBtn = button;
	}
	
	private void createEraserButton(LineBorder border) {
		Icon icon = new ImageIcon(getClass().getResource("/icon/eraser.png"));
		JButton button = createButton(icon, "Eraser", border);
		eraserBtn = button;
	}

	private void setPaintSizeButton(LineBorder border){
		Icon icon = new ImageIcon(getClass().getResource("/icon/ruler.png"));
		JButton button = createButton(icon, "Set size", border);
		setSizeBtn = button;
	}
	
	private JButton createButton(Icon icon, String toolTipText, LineBorder border) {
		// Get the image from the original icon
		Image originalImage = ((ImageIcon) icon).getImage();
	
		// Scale the image to the desired dimensions
		Image scaledImage = originalImage.getScaledInstance(25, 25, Image.SCALE_SMOOTH);
	
		// Create a new ImageIcon with the scaled image
		Icon scaledIcon = new ImageIcon(scaledImage);
	
		// Create the button with the scaled icon
		JButton button = new JButton(scaledIcon);
		button.setToolTipText(toolTipText);
		button.setBorder(border);
		button.addActionListener(actionListener);
	
		// Add the button to the list
		btnList.add(button);
	
		return button;
	}


	//init the canvas board 
	@Override
	public void drawBoard(CanvasServerInterface server) throws RemoteException{
		frame = new JFrame(clientName + "'s WhiteBoard");
		Container content = frame.getContentPane();
		
		canvasUI = new Canvas(clientName, isManager , server);
		
		setColorButtons();
		settingIcons();
		

		managerBUtton();
		
		
		if (isManager == false) {
			clearBtn.setVisible(false);
			saveBtn.setVisible(false);
			saveAsBtn.setVisible(false);
			openBtn.setVisible(false);
			closeButton.setVisible(false);
		}
		
		// showing the user list 
		JList<String> list = new JList<>(userList);
		JScrollPane currUsers = new JScrollPane(list);
		currUsers.setMinimumSize(new Dimension(100,150));
		if(!isManager) {
			currUsers.setMinimumSize(new Dimension(100,290));
		}
		
		if(isManager) {
			// remove user
			kickoutUser(list);
		}
		
		//chat box 
		chat = new JList<>(chatList);
		msgArea = new JScrollPane(chat);
		msgArea.setMinimumSize(new Dimension(100,100));
		JTextField msgText = new JTextField();
		JButton sendBtn = setChatbox(msgText);
		
		 setGUILayout(content,currUsers,msgArea,sendBtn,msgText);
		
		//clientUI layout
		

			frame.setMinimumSize(new Dimension(900, 650));
			frame.setLocationRelativeTo(null);
			frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			frame.setVisible(true);
			
			managerCloseUI();		
	} 



	//if the manager close the windows
	private void managerCloseUI(){
		
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				if(isManager) {
					if(JOptionPane.showConfirmDialog(frame, 
							"You are the manager? Are you sure you want to close the app?"
							,"Close App?", 
							JOptionPane.YES_NO_OPTION,
							JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
						try {
							server.removeAll();
						}catch(IOException e) {
							util.errorMessage(ioMEssage);
						}finally {
							System.exit(0);
						}
					}
				} else {
					if (JOptionPane.showConfirmDialog(frame,
							"Are you shure you want to quit?", "Close Paint Board?", 
							JOptionPane.YES_NO_OPTION, 
							JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
						try {
							server.removeMe(clientName);
							updateUserList(server.getClientList());
						}catch(RemoteException e) {
							Util.popupDialog("Canvas server is down, please save and exit.");
						}finally {
							System.exit(0);
						}
							
					}
				}
			}
		});		
	}

	// if manager choose to kit some user
	private void kickoutUser(JList<String> list){
		list.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				@SuppressWarnings("unchecked")
				JList<String> list =(JList<String>)evt.getSource();
				if(evt.getClickCount() == 2) {
					int index = list.locationToIndex(evt.getPoint());
					String selectedName = list.getModel().getElementAt(index);
					try {
						if(!getName().equals(selectedName)) {
							int dialogResult = JOptionPane.showConfirmDialog(frame, "Do you want to remove " + selectedName,
									"Warning",JOptionPane.YES_NO_OPTION);
							if(dialogResult == JOptionPane.YES_OPTION) {
								try {
									server.deleteClient(selectedName);
									updateUserList(server.getClientList());
								}catch(IOException e) {
									util.errorMessage(ioMEssage);
								}
							}
						}
					}catch(HeadlessException e) {
						util.errorMessage(ioMEssage);
					}catch(RemoteException e) {
						util.errorMessage(ioMEssage);
					}
				}
			}
		});
	}

	// send message to server button
	private JButton setChatbox(JTextField msgText){
		JButton sendBtn = new JButton("Send");
		sendBtn.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				if(!msgText.getText().equals("")) {
					try {
						server.addChat(clientName + ": "+ msgText.getText());
						
						SwingUtilities.invokeLater(() -> {
							JScrollBar vertical = msgArea.getVerticalScrollBar();
							vertical.setValue(vertical.getMaximum());
					});
					}catch(RemoteException e1) {
						JOptionPane.showMessageDialog(null, "WhiteBoard server lost connection, Please Sava and Exit.");
					}
					msgText.setText("");
				}
			}
		});
		return sendBtn;
	}

	private void setGUILayout(Container content,JScrollPane currUsers, JScrollPane msgArea2, JButton sendBtn, JTextField msgText){
		GroupLayout layout = new GroupLayout(content);
		content.setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		
		layout.setHorizontalGroup(layout.createSequentialGroup()
			    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER) // Use GroupLayout.Alignment.CENTER
			        .addComponent(drawBtn)
			        .addComponent(lineBtn)
			        .addComponent(rectBtn)
			        .addComponent(circleBtn)
			        .addComponent(ovalBtn)
			        .addComponent(textBtn)
			        .addComponent(eraserBtn)
					.addComponent(setSizeBtn)
			    )
			    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER) // Use GroupLayout.Alignment.CENTER
			        .addComponent(canvasUI)
			        .addComponent(msgArea)
			        .addGroup(layout.createSequentialGroup()
			            .addComponent(msgText)
			            .addComponent(sendBtn)
			        )
			    )
			    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
			    		.addComponent(clearBtn)
			    		.addComponent(openBtn)
						.addComponent(closeButton)
						
			    		.addComponent(saveBtn)
			    		.addComponent(saveAsBtn)
			    		.addComponent(currUsers)
			    		.addComponent(tellColor)
			    		.addComponent(displayColor)
					
			        .addGroup(layout.createSequentialGroup()
			            .addComponent(blackBtn)
						.addComponent(orangeBtn)
						.addComponent(grayBtn)
						.addComponent(magentaBtn)
			        )
			        .addGroup(layout.createSequentialGroup()
						.addComponent(blueBtn)
						.addComponent(yellowBtn)
						.addComponent(pinkBtn)
						.addComponent(aoiBtn)
			        )
			        .addGroup(layout.createSequentialGroup()
			            .addComponent(redBtn)
						.addComponent(limeBtn)
						.addComponent(skyBtn)
						.addComponent(cyanBtn)
			        )
			        .addGroup(layout.createSequentialGroup()
			            .addComponent(greenBtn)
						.addComponent(lightGrayBtn)
						.addComponent(purpleBtn)
						.addComponent(bronBtn)
			        )
			    		)
			);
		
		layout.setVerticalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addGroup(layout.createSequentialGroup()
								.addComponent(drawBtn)
						        .addComponent(lineBtn)
						        .addComponent(rectBtn)
						        .addComponent(circleBtn)
						        .addComponent(ovalBtn)
						        .addComponent(textBtn)
						        .addComponent(eraserBtn)
								.addComponent(setSizeBtn)
								)
						.addComponent(canvasUI)
						.addGroup(layout.createSequentialGroup()
								.addComponent(clearBtn)
					    		.addComponent(openBtn)
								.addComponent(closeButton)
					    		.addComponent(saveBtn)
					    		.addComponent(saveAsBtn)
					    		.addComponent(currUsers)
					    		.addComponent(tellColor)
					    		.addComponent(displayColor)
								.addGap(20, 20, 20)
								.addGroup(layout.createSequentialGroup()
								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
									.addGroup(layout.createSequentialGroup()
										.addComponent(blackBtn)
										.addComponent(blueBtn)
										.addComponent(redBtn)
										.addComponent(greenBtn)
										)
									.addGroup(layout.createSequentialGroup()
										.addComponent(orangeBtn)
										.addComponent(yellowBtn)
										.addComponent(limeBtn)
										.addComponent(lightGrayBtn)
										)
									.addGroup(layout.createSequentialGroup()
										.addComponent(grayBtn)
										.addComponent(pinkBtn)
										.addComponent(skyBtn)
										.addComponent(purpleBtn)
										)
									.addGroup(layout.createSequentialGroup()
										.addComponent(magentaBtn)
										.addComponent(aoiBtn)
										.addComponent(cyanBtn)
										.addComponent(bronBtn)
										)
									)
										
								)
					    		)
						
						)
						.addGroup(layout.createSequentialGroup()
								.addComponent(msgArea)
								.addGroup(layout.createParallelGroup()
										.addComponent(msgText)
										.addComponent(sendBtn)
										)
								)
				
						
				);
				layout.linkSize(SwingConstants.HORIZONTAL, clearBtn,saveBtn,saveAsBtn,openBtn,closeButton);
	}
	

	// init the manager avaliable buttons such as save open
	public void managerBUtton(){
		clearBtn = new JButton("New Board");
		clearBtn.setToolTipText("Create a new board");
		clearBtn.addActionListener(actionListener);
		
		saveBtn = new JButton("Save Image");
		saveBtn.setToolTipText("Save as iamge file");
		saveBtn.addActionListener(actionListener);
		
		saveAsBtn = new JButton("Save as");
		saveAsBtn.setToolTipText("Save iamge as file");
		saveAsBtn.addActionListener(actionListener);
		
		openBtn = new JButton("Open Local Image");
		openBtn.setToolTipText("Open an iamge file");
		openBtn.addActionListener(actionListener);

		closeButton = new JButton("Close Canvas");
		closeButton.setToolTipText("Close the canvas");
		closeButton.addActionListener(actionListener);
		
		// tell color
		tellColor = new JTextArea("The current color is:");
		tellColor.setBackground(new Color(238,238,238));
		displayColor = new JTextArea("");
		displayColor.setBackground(Color.black);
	}
	

	// update for all user list 
	public void updateUserList(Set<CanvasClientInterface> list) {
		this.userList.removeAllElements();
		for (CanvasClientInterface c: list) {
			try {
				userList.addElement(c.getName());
			}catch(RemoteException e) {
				System.out.print("Server is down");
			}
		}
	}
		
	// get names of all users
	@Override
	public String getName() throws RemoteException {
		return this.clientName;
	}

	// set the canvas name 
	@Override
	public void setName(String client_name) throws RemoteException {
		this.clientName = client_name;
		return;
	}

	//assign the client as manager 
	@Override
	public void assignManager() {
		this.isManager = true;
		
	}

	//get the manager states
	@Override
	public boolean getManager() {
		return this.isManager;
	}

	//new user ask join permission
	@Override
	public boolean askPermission(String name)  {
		if (JOptionPane.showConfirmDialog(frame, 
				name + " wants to join. Do you approve?", "Grant permission",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
			return true;
		}else {
			return false;
		}
		
	}

	@Override
	public boolean getPermission() {
		
		return this.havePermission;
	}
	
	@Override
	public void setPermission(boolean flag) {
		this.havePermission = flag;
		
	}


	//send update to all users 
	@Override
	public void syncCanvas(CanvasMessageInterface message)  throws RemoteException{
		if(message.getName().compareTo(clientName) == 0) {
			return;
		}
		Shape shape = null;
		
		if(message.getState().equals("start")) {
			startPoints.put(message.getName(), message.getpoint());
			return;
		}
		
		//start from the start point of client
		Point startPt =(Point)startPoints.get(message.getName());
		
		//set canvas stroke color
		canvasUI.getGraohic().setPaint(message.getcolor());
		
		if(message.getState().equals("drawing")) {
			// String input = JOptionPane.showInputDialog("Please enter the mode:");
			// size = Float.parseFloat(input+"f");
			canvasUI.getGraohic().setStroke(new BasicStroke(1.0f));
			if(message.getmode().equals("eraser")) {
				canvasUI.getGraohic().setStroke(new BasicStroke(15.0f));
			}
			shape = Util.makeLine(shape, startPt, message.getpoint());
			startPoints.put(message.getName(), message.getpoint());
			canvasUI.getGraohic().draw(shape);
			canvasUI.repaint();
			return;
		}
		
		// the mouse is released so we draw from start point to the break point
		if( message.getState().equals("end")) {
			canvasUI.getGraohic().setStroke(new BasicStroke(1.0f));
			if(message.getmode().equals("draw") || message.getmode().equals("line")) {
				shape = Util.makeLine(shape, startPt, message.getpoint());
			}else if(message.getmode().equals("eraser")) {
				canvasUI.getGraohic().setStroke(new BasicStroke(15.0f));
			}else if(message.getmode().equals("rect")) {
				shape = Util.makeRect(shape,startPt,message.getpoint());
			}else if(message.getmode().equals("circle")) {
				shape = Util.makeCircle(shape,startPt,message.getpoint());
			}else if(message.getmode().equals("oval")) {
				shape = Util.makeOval(shape,startPt,message.getpoint());
			}else if(message.getmode().equals("text")) {
				canvasUI.getGraohic().setFont(new Font("TimeRoman", Font.PLAIN,16));
				canvasUI.getGraohic().drawString(message.gettext(), message.getpoint().x, message.getpoint().y);
			}
			
			//draw shape if in shape mode:
			if(!message.getmode().equals("text")){
				try {
					canvasUI.getGraohic().draw(shape);
				}catch(Exception e) {
					System.err.println("Here is an error during drawing");
				}
			}
			
			canvasUI.repaint();
			
			startPoints.remove(message.getName());
			return;
		}
		return;
		
	}

	//get the canvas from the server
	@Override
	public void refreshCanvas() {
		if(this.isManager == false) {
			this.canvasUI.reset();
		}
		
	}

	//close the UI for user
	@Override
	public void closeUI() throws IOException {
		if(!this.havePermission) {
			Thread t = new Thread(new Runnable() {
				public void run() {
					JOptionPane.showMessageDialog(null, "Sorry You are not allowed to acce the whitboard..." , 
							"Warning", JOptionPane.WARNING_MESSAGE);
					System.exit(0);
				}
			});
			t.start();
			return;
		}
		
		//if kicked out 
		Thread t = new Thread(new Runnable() {
			public void run() {
				JOptionPane.showMessageDialog(frame, "The manager has queit. \n or you have been removed\n"
						+ "Your application will be closed",
						"Error", JOptionPane.ERROR_MESSAGE);
				System.exit(0);
			}
		}
		);
		t.start();
		
	}

	//add cirremt chat to servers 
	@Override
	public void addChat(String text) throws RemoteException {
		this.chatList.addElement(text);
		
	}

	//sending the image to server 
	@Override
	public byte[] sendImage() throws IOException {
		ByteArrayOutputStream imageArray = new ByteArrayOutputStream();
		ImageIO.write(this.canvasUI.getCanvas(), "png", imageArray);
		return imageArray.toByteArray();
	}
	
	// draw current 
	@Override
	public void drawOpenedImage(byte[] rawImage) throws IOException {
		BufferedImage image = ImageIO.read(new ByteArrayInputStream(rawImage));
		this.canvasUI.drawImage(image);
		
	}
	
	//main functions
	public static void main(String[] args) throws NotBoundException, NotBoundException, IOException {
		//set the connection informations
		String hostName = "";
		String portNumber = "";
		String serverName = "WhiteboardServer";

		if(args.length < 2) {
			Util.popupDialog("Not enough argments! Should be <server address> <server port");
			System.exit(0);
		}

		hostName = args[0]; // Assuming the first argument is the server address
		int inputPort = Util.parsePort(args[1]); // Assuming the second argument is the server port
		portNumber = Integer.toString(inputPort);
		String serverAddress = "//" + hostName+":" +  portNumber + "/" + serverName;

		try {
			server = (CanvasServerInterface)Naming.lookup(serverAddress);
		} catch (Exception e) {
			// TODO: handle exception
			Util.popupDialog("Cannot connect to the server please check again");
			System.exit(0);
		}
		
		
		
		CanvasClientInterface client = new Client();
		
		// verify the client name are unick
		UnickNameCheck(client,server);

		try {
			server.register(client);
		}catch(Exception e) {
			
		}
		
		//lunch the canvas GUI
		client.drawBoard(server);
		
		
		
		if(!client.getPermission()) {
			server.deleteClient(client.getName());
		}
		
	}

	



}