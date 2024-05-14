package client;


import java.awt.BasicStroke;
import java.awt.Stroke;
import java.io.ByteArrayInputStream;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.rmi.RemoteException;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JOptionPane;

import remote.CanvasServerInterface;
import client.Util;


public class Canvas extends JComponent {

	private static final long serialVersionUID = 1L;
	private String clientName;
	private boolean isManager;
	private Point startPt, endPt;
	private Color color;
	private String mode;
	private String text;
	
	private BufferedImage image;
	private BufferedImage previousCanvas;
	private Graphics2D graphics;
	
	private CanvasServerInterface server;
	
	
	public Canvas(String name, boolean isManager, CanvasServerInterface RemoteInterface) {
		// inite the information in the constructions
		this.server = RemoteInterface;
		this.clientName = name;
		this.isManager = isManager;
		this.color = Color.black;
		this.mode = "draw";
		this.text = "";
		
		setDoubleBuffered(false);
		
		// keep listen the mouse actions 
		// for mouse pressed
		//if click store the start location and send to server
		addMouseListener(new MouseAdapter(){
			public void mousePressed(MouseEvent e) {
				startPt = e.getPoint();
				saveCanvas();
				try {
					sendMessage("start",clientName,
							mode,color,startPt,text);
				}catch(Exception e1) {
					
					Util.popupDialog("Canvas server is down...");
				}
				
			}
		});
		
		//Listen the canvas actions. 
		// for mouse dragged actions 
		//draw the shape on local client then send to server
		addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) {
				endPt = e.getPoint();
				Shape shape = null;
				if(graphics != null) {
					graphics.setPaint(color);
					graphics.setStroke(new BasicStroke(1.0f));
					
					//改写。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。
					if(mode.equals("draw")) {
						shape = makeLine(shape, startPt, endPt);
						startPt = endPt;
						try {
							MessageWrapper message = new MessageWrapper("drawing", 
									clientName, mode, color, endPt, "");
							server.broadCastCancas(message);
						}catch(Exception e1) {
							JOptionPane.showMessageDialog(null, "Canvas server is down...");
						}
					}else if (mode.equals("eraser")) {
						Color eraserColor = Color.white;
						shape = makeLine(shape, startPt, endPt);
						startPt = endPt;
						graphics.setPaint(eraserColor);
						graphics.setStroke(new BasicStroke(15.0f));
						try {
							MessageWrapper message = new MessageWrapper("drawing", 
									clientName, mode, eraserColor, endPt, "");
							server.broadCastCancas(message);
						}catch(Exception e1) {
							JOptionPane.showMessageDialog(null, "Canvas server is down...");
						}
					}else if(mode.equals("line")) {
						drawPreviousCanvas();
						shape = makeLine(shape,startPt, endPt);
					}else if(mode.equals("rect")) {
						drawPreviousCanvas();
						shape = makeRect(shape,startPt, endPt);
					}else if(mode.equals("circle")) {
						drawPreviousCanvas();
						shape = makeCircle(shape,startPt, endPt);
					}else if(mode.equals("oval")) {
						drawPreviousCanvas();
						shape = makeOval(shape,startPt, endPt);
					}else if(mode.equals("text")) {
						drawPreviousCanvas();
						graphics.setFont(new Font("TimesRoman", Font.PLAIN, 16));
						graphics.drawString("Enter text", endPt.x, endPt.y);
						shape = makeText(shape, startPt);
						Stroke dashed = new BasicStroke(1, BasicStroke.CAP_BUTT, 
								BasicStroke.JOIN_BEVEL, 1, new float[] {3}, 0);
						graphics.setStroke(dashed);
					}
					graphics.draw(shape);
					repaint();
				}
			}
		});
		

		// draw the shap when the mouse is release
		addMouseListener(new MouseAdapter() {
			
			public void mouseReleased(MouseEvent e) {
				//as the location saves 
				endPt = e.getPoint();
				Shape shape = null;
				if(graphics != null) {
					if (mode.equals("line")){
						shape = makeLine(shape, startPt, endPt);
					}else if (mode.equals("draw")){
						shape = makeLine(shape, startPt, endPt);
					}else if (mode.equals("rect")){
						shape = makeRect(shape, startPt, endPt);
					}else if(mode.equals("circle")){
						shape = makeCircle(shape, startPt, endPt);
					}else if (mode.equals ("triangle")){
						shape = makeOval(shape, startPt, endPt);
					}else if(mode.equals("text")){
						text = JOptionPane.showInputDialog("Adding text here");
						if(text == null) 
							text = "";
						drawPreviousCanvas();
						graphics.setFont(new Font("TimesRoman", Font.PLAIN, 22));
						graphics.drawString(text, endPt.x, endPt.y);
						graphics.setStroke(new BasicStroke(1.0f));
					}
					
					if (! mode.equals("text")) {
						try {
							graphics.draw(shape);
						}catch(NullPointerException e1) {
							//do nothing caused draw mode.
						}
					}
					
					repaint();
					
					try {
						MessageWrapper message = new MessageWrapper("end", clientName, 
								mode, color, endPt, text);
						server.broadCastCancas(message);
					}catch(RemoteException e2){
						JOptionPane.showMessageDialog(null, "Canvas server is down...");
					}
					
						
				}
			}
		});
		
		
	}
	
	private void drawing(String state){
		
	}

	//painting the shape on the canvas
	protected void paintComponent(Graphics g) {
		if(image == null) {
			if(isManager) {
				image = new BufferedImage(700,350,BufferedImage.TYPE_INT_RGB);
				graphics = (Graphics2D) image.getGraphics();
				graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
						RenderingHints.VALUE_ANTIALIAS_ON);
				reset();
			}else {
				try {
					byte[] rawImage = server.sendImage();
					// Cannot read the array length because "buf" is null
					image = ImageIO.read(new ByteArrayInputStream(rawImage));
					graphics = (Graphics2D) image.getGraphics();
					graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
							RenderingHints.VALUE_ANTIALIAS_ON);
					graphics.setPaint(color);
					
				}catch(IOException e) {
					System.err.println("Fail receiving imag!");
				}
			}
		}
		
		g.drawImage(image,0,0,null);
	}
	
	
	// getter functions for basic informations
	public Color getCurrentColor() {
		return color;
	}
	
	public String getCurrentMode() {
		return mode;
	}
	
	public Graphics2D getGraohic() {
		return graphics;
	}
	
	public BufferedImage getCanvas() {
		saveCanvas();
		return previousCanvas;
	}
	
	// reset the canvas page
	public void reset() {
		graphics.setPaint(Color.white);
		graphics.fillRect(0, 0, 700, 350);
		graphics.setPaint(color);
		repaint();
	}
	
	//save the canvas page
	public void saveCanvas() {
		//22222222222222222222222222222222222222222222222222222
		//Cannot invoke "java.awt.image.BufferedImage.getColorModel()"
		//because "this.image" is null
		ColorModel cm = image.getColorModel();
		WritableRaster raster = image.copyData(null);
		previousCanvas = new BufferedImage(cm,raster,false,null);
	}
	
	// cover the current canvas with previous canvas states
	public void drawPreviousCanvas() {
		drawImage(previousCanvas);
	}
	
	public void drawImage(BufferedImage img) {
		graphics.drawImage(img, null, 0, 0);
		repaint();
	}
	
	
	//  different colors 
	//可优化
	public void brown() {
		this.color = new Color(153,76,0);
		graphics.setPaint(color);
	}
	
	public void red() {
		this.color = Color.red;
		graphics.setPaint(color);
	}
	
	public void black() {
		this.color = Color.black;
		graphics.setPaint(color);
	}
	
	public void green() {
		this.color = Color.green;
		graphics.setPaint(color);
	}
	
	public void pink() {
		this.color = new Color(255,153,204);
		graphics.setPaint(color);
	}
	
	public void purple() {
		this.color = new Color(102,0,204);
		graphics.setPaint(color);
	}
	
	public void blue() {
		this.color = Color.blue;
		graphics.setPaint(color);
	}
	
	public void orange() {
		this.color = Color.orange;
		graphics.setPaint(color);
	}
	
	public void gray() {
		this.color = Color.gray;
		graphics.setPaint(color);
	}

	public void lime() {
		this.color = new Color(102,102,0);
		graphics.setPaint(color);
	}
	
	public void magenta() {
		this.color = Color.magenta;
		graphics.setPaint(color);
	}
	
	public void aoi() {
		this.color = new Color(0,102,102);
		graphics.setPaint(color);
	}
	
	public void sky() {
		this.color = new Color(0,128,255);
		graphics.setPaint(color);
	}
	
	public void yellow() {
		this.color = Color.yellow;
		graphics.setPaint(color);
	}
	
	public void cyan() {
		this.color = Color.cyan;
		graphics.setPaint(color);
	}
	
	public void lightGray() {
		this.color = Color.LIGHT_GRAY;
		graphics.setPaint(color);
	}
	
	
	//set different modes
	public void draw() {
		mode = "draw";
	}

	public void line() {
		mode = "line";
	}
	
	public void rect() {
		mode = "rect";
	}
	
	public void circle() {
		mode = "circle";
	}
	
	public void oval() {
		mode = "oval";
	}
	
	public void triangle() {
		mode = "triangle";
	}
	
	public void text() {
		mode = "text";
	}
	
	public void eraser() {
		mode = "eraser";
	}
	
	
	// draw different shapes
	// draw line 
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
}




	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

