package client;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.HeadlessException;
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
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import javax.swing.ImageIcon;
import javax.swing.Icon;

import remote.CanvasClientInterface;
import remote.CanvasMessageInterface;
import remote.CanvasServerInterface;
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
	private JButton clearBtn, saveBtn, saveAsBtn, openBtn,blackBtn,blueBtn,yelloBtn,redBtn,greenBtn;
	private JButton drawBtn, lineBtn,rectBtn,circleBtn,ovalBtn,textBtn,eraserBtn;
	private JScrollPane msgArea;
	private JTextArea tellColor, displayColor;
	private JList<String> chat;
	private ArrayList<JButton> btnList;
	private Canvas canvasUI;
	
	private String clientName;
	private String picName;
	private String picPath;
	private Hashtable<String, Point> startPoints = new Hashtable<String,Point>();
	

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
			 
			if(e.getSource() == clearBtn) {
				int choice = JOptionPane.showConfirmDialog(frame, "Do you want to set a new page?", "New Page Confirmation", JOptionPane.YES_NO_OPTION);
				if (choice == JOptionPane.YES_OPTION) {
		            // User clicked "Yes"
		            canvasUI.reset();
		        }
				if(isManager) {
					try {
					    server.refreshCanvas();
					} catch (RemoteException e1) {
					    JOptionPane.showMessageDialog(null, "Canvas server is down, please save and exit!");
					}
	            }
			}else if (e.getSource() == openBtn) {
				try {
					open();
				}catch(IOException e1) {
					System.err.println("There is an IO error, please double check");
				}
			}else if (e.getSource() == saveBtn) {
				try {
					save();
				}catch(IOException e1) {
					System.err.println("There is an IO error, please double check");
				}
			}else if (e.getSource() == saveAsBtn) {
				try {
					saveAs();
				}catch(IOException e1) {
					System.err.println("There is an IO error, please double check");
				}
			}
			// select colors 
			else if (e.getSource() == blackBtn) {
				canvasUI.black();
			}else if (e.getSource() == blueBtn) {
				canvasUI.blue();
			}else if (e.getSource() == yelloBtn) {
				canvasUI.yellow();
			}else if (e.getSource() == redBtn) {
				canvasUI.red();
			}else if (e.getSource() == greenBtn) {
				canvasUI.green();
			}
			
			// drawing shapes
			else if (e.getSource() == drawBtn) {
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
			}
			
			if (e.getSource() == blackBtn || e.getSource() == blueBtn || e.getSource() == yelloBtn || e.getSource() == redBtn) {
				displayColor.setBackground(canvasUI.getCurrentColor());
			}
		}
	};
	
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
	
	private void saveAs() throws IOException{
		FileDialog saveAsdialog = new FileDialog(frame, "save image", FileDialog.SAVE);
		saveAsdialog.setVisible(true);
		if(saveAsdialog.getFile() != null) {
			this.picPath = saveAsdialog.getDirectory();
			this.picName = saveAsdialog.getFile();
			ImageIO.write(canvasUI.getCanvas(), "png", new File(picPath + picName));
		}
	}
	
	private void save()throws IOException{
		if(picName == null) {
			JOptionPane.showMessageDialog(null, "Plase SaveAs first");
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
	
	@Override
	public void drawBoard(CanvasServerInterface server) {
		frame = new JFrame(clientName + "'s WhiteBoard");
		Container content = frame.getContentPane();
		
		canvasUI = new Canvas(clientName, isManager , server);
		
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
		
		
		LineBorder border = new LineBorder(Color.black,2);
		
		//free draw
		Icon icon = new ImageIcon("src");
		drawBtn = new JButton(icon);
		drawBtn.setToolTipText("free draw");
		drawBtn.setBorder(border);
		drawBtn.addActionListener(actionListener);
		
		//draw line
		border = new LineBorder(new Color(238,238,238),2);
		icon = new ImageIcon("src");
		lineBtn = new JButton(icon);
		lineBtn.setToolTipText("Draw line");
		lineBtn.setBorder(border);
		lineBtn.addActionListener(actionListener);
		
		// draw rectange
		icon = new ImageIcon("src");
		rectBtn = new JButton(icon);
		rectBtn.setToolTipText("Draw rectangle");
		rectBtn.setBorder(border);
		rectBtn.addActionListener(actionListener);
		
		// draw circle
		icon = new ImageIcon("src");
		circleBtn = new JButton(icon);
		circleBtn.setToolTipText("Draw circle");
		circleBtn.setBorder(border);
		circleBtn.addActionListener(actionListener);
		
		//draw oval
		icon = new ImageIcon("src");
		ovalBtn = new JButton(icon);
		ovalBtn.setToolTipText("Draw oval");
		ovalBtn.setBorder(border);
		ovalBtn.addActionListener(actionListener);
		
		//set text
		icon = new ImageIcon("src");
		textBtn = new JButton(icon);
		textBtn.setToolTipText("Draw text");
		textBtn.setBorder(border);
		textBtn.addActionListener(actionListener);
		
		//set eraser
		icon = new ImageIcon("src");
		eraserBtn = new JButton(icon);
		eraserBtn.setToolTipText("eraser");
		eraserBtn.setBorder(border);
		eraserBtn.addActionListener(actionListener);
		
		
		btnList.add(drawBtn);
		btnList.add(lineBtn);
		btnList.add(rectBtn);
		btnList.add(circleBtn);
		btnList.add(ovalBtn);
		btnList.add(textBtn);
		btnList.add(eraserBtn);
		
		
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
		
		// tell color
		tellColor = new JTextArea("The current color is:");
		tellColor.setBackground(new Color(238,238,238));
		displayColor = new JTextArea("");
		displayColor.setBackground(Color.black);
		
		if (isManager == false) {
			clearBtn.setVisible(false);
			saveBtn.setVisible(false);
			saveAsBtn.setVisible(false);
			openBtn.setVisible(false);
		}
		
		// showing the user list 
		JList<String> list = new JList<>(userList);
		JScrollPane currUsers = new JScrollPane(list);
		currUsers.setMinimumSize(new Dimension(100,150));
		if(!isManager) {
			currUsers.setMinimumSize(new Dimension(100,290));
		}else {
			// remove user
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
										System.err.println("There is an io error");
									}
								}
							}
						}catch(HeadlessException e) {
							System.err.println("There is an Headless error");
						}catch(RemoteException e) {
							System.err.println("There is an IO error");
						}
					}
				}
			});
		}
		
		//chat box 
		chat = new JList<>(chatList);
		msgArea = new JScrollPane(chat);
		msgArea.setMinimumSize(new Dimension(100,100));
		JTextField msgText = new JTextField();
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
		
		
		//clientUI layout
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
			    )
			    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER) // Use GroupLayout.Alignment.CENTER
			        .addComponent(canvasUI)
			        .addComponent(msgArea)
			        .addGroup(layout.createSequentialGroup()
			            .addComponent(msgText)
			            .addComponent(sendBtn)
			        )
			        .addGroup(layout.createSequentialGroup()
			            .addComponent(blackBtn)
			        )
			        .addGroup(layout.createSequentialGroup()
			            .addComponent(blueBtn)
			        )
			        .addGroup(layout.createSequentialGroup()
			            .addComponent(redBtn)
			        )
			        .addGroup(layout.createSequentialGroup()
			            .addComponent(greenBtn)
			        )
			    )
			    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
			    		.addComponent(clearBtn)
			    		.addComponent(openBtn)
			    		.addComponent(saveBtn)
			    		.addComponent(saveAsBtn)
			    		.addComponent(currUsers)
			    		.addComponent(tellColor)
			    		.addComponent(displayColor)
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
								)
						.addComponent(canvasUI)
						.addGroup(layout.createSequentialGroup()
								.addComponent(clearBtn)
					    		.addComponent(openBtn)
					    		.addComponent(saveBtn)
					    		.addComponent(saveAsBtn)
					    		.addComponent(currUsers)
					    		.addComponent(tellColor)
					    		.addComponent(displayColor)
					    		)
						)
				.addGroup(layout.createSequentialGroup()
						.addComponent(msgArea)
						.addGroup(layout.createParallelGroup()
								.addComponent(msgText)
								.addComponent(sendBtn)
								)
						)
				.addGroup(layout.createSequentialGroup()
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(blackBtn)
								)
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(blueBtn)
								)
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(redBtn)
								)
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(greenBtn)
								)
						)
				);
			layout.linkSize(SwingConstants.HORIZONTAL, clearBtn,saveBtn,saveAsBtn,openBtn);
			
			frame.setMinimumSize(new Dimension(820, 600));
			frame.setLocationRelativeTo(null);
			frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			frame.setVisible(true);
			
			//if the manager close the windows
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
								System.err.println("There are an IO error");
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
								JOptionPane.showMessageDialog(null, "Canvas server is down, please save and exit.");
							}finally {
								System.exit(0);
							}
								
						}
					}
				}
			});		
	} 
	
	
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
	
	public Shape makeLine(Shape shape, Point start, Point end) {
		shape = new Line2D.Double(start.x,start.y,end.x,end.y);
		return shape;
	}

	
	// draw rectangle
		public Shape makeRect(Shape shape, Point start, Point end) {
			int x = Math.min(start.x, end.x);
			int y = Math.min(start.y, end.y);
			int width = Math.abs(start.x - end.x);
			int height = Math.abs(start.y - end.y);
			shape = new Rectangle2D.Double(x,y,width,height);
			return shape;
		}
		
		// draw circle
		public Shape makeCircle(Shape shape, Point start, Point end) {
			int x = Math.min(start.x, end.x);
			int y = Math.min(start.y, end.y);
			int width = Math.abs(start.x - end.x);
			int height = Math.abs(start.y - end.y);
			shape = new Ellipse2D.Double(x,y,Math.max(width, height), Math.max(width, height));
			return shape;
		}
		
		// draw Oval
		public Shape makeOval(Shape shape, Point start, Point end) {
			int x = Math.min(start.x, end.x);
			int y = Math.min(start.y, end.y);
			int width = Math.abs(start.x - end.x);
			int height = Math.abs(start.y - end.y);
			shape = new Ellipse2D.Double(x,y,width,height);
			return shape;
		}
		
		// draw text
		public Shape makeText(Shape shape, Point start) {
			int x = start.x -5;
			int y = start.y -20;
			int width = 130;
			int height = 25;
			shape = new RoundRectangle2D.Double(x,y,width,height, 15,15);
			return shape;
			
		}
		
		
	@Override
	public String getName() throws RemoteException {
		return this.clientName;
	}

	@Override
	public void setName(String client_name) throws RemoteException {
		this.clientName = client_name;
		
	}

	@Override
	public void assignManager() throws RemoteException {
		this.isManager = true;
		
	}

	@Override
	public boolean getManager() throws RemoteException {
		return this.isManager;
	}

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
	public boolean getPermission() throws RemoteException {
		
		return this.havePermission;
	}
	
	@Override
	public void setPermission(boolean permission) {
		this.havePermission = permission;
		
	}


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
			canvasUI.getGraohic().setStroke(new BasicStroke(1.0f));
			if(message.getmode().equals("eraser")) {
				canvasUI.getGraohic().setStroke(new BasicStroke(15.0f));
			}
			shape = makeLine(shape, startPt, message.getpoint());
			startPoints.put(message.getName(), message.getpoint());
			canvasUI.getGraohic().draw(shape);
			canvasUI.repaint();
			return;
		}
		
		// the mouse is released so we draw from start point to the break point
		if( message.getState().equals("end")) {
			canvasUI.getGraohic().setStroke(new BasicStroke(1.0f));
			if(message.getmode().equals("draw") || message.getmode().equals("line")) {
				shape = makeLine(shape, startPt, message.getpoint());
			}else if(message.getmode().equals("eraser")) {
				canvasUI.getGraohic().setStroke(new BasicStroke(15.0f));
			}else if(message.getmode().equals("rect")) {
				shape = makeRect(shape,startPt,message.getpoint());
			}else if(message.getmode().equals("circle")) {
				shape = makeCircle(shape,startPt,message.getpoint());
			}else if(message.getmode().equals("oval")) {
				shape = makeOval(shape,startPt,message.getpoint());
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

	@Override
	public void refreshCanvas() throws RemoteException {
		if(this.isManager == false) {
			this.canvasUI.reset();
		}
		
	}

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

	@Override
	public void addChat(String text) throws RemoteException {
		this.chatList.addElement(text);
		
	}

	@Override
	public byte[] sendImage() throws IOException {
		ByteArrayOutputStream imageArray = new ByteArrayOutputStream();
		ImageIO.write(this.canvasUI.getCanvas(), "png", imageArray);
		return imageArray.toByteArray();
	}

	@Override
	public void drawOpenedImage(byte[] rawImage) throws IOException {
		BufferedImage image = ImageIO.read(new ByteArrayInputStream(rawImage));
		this.canvasUI.drawImage(image);
		
	}
	
	//main functions
	public static void main(String args[]) throws NotBoundException, IOException {
		//set the connection informations
		String hostName = "localhost";
		String portNumber = "8080";
		String serverName = "WhiteboardServer";
		String serverAddress = "//" + hostName+":" + portNumber + "/" + serverName;
		
		server = (CanvasServerInterface)Naming.lookup(serverAddress);
		
		CanvasClientInterface client = new Client();
		
		// verify the client name are unick
//		UnickNameCheck(client,server);
		
		
		boolean validName = false;
		String client_Name = "";
		while(!validName) {
			client_Name = JOptionPane.showInputDialog("Please type your name here");
			if(client_Name.equals("")) {
				JOptionPane.showMessageDialog(null, "Please enter a name!");
			}else {
				validName = true;
			}
			
			for(CanvasClientInterface c : server.getClientList()) {
				if(client_Name.equals(c.getName()) || c.getName().equals("*"+ client_Name)) {
					validName =false;
					JOptionPane.showMessageDialog(null, "The name are already been token, please try differernt one");
					
				}
			}
		}
		
		client.setName(client_Name);

		
		try {
			// only register if client have permission
//			if(!client.getPermission()) {
//				server.register(client);
//			}	
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
