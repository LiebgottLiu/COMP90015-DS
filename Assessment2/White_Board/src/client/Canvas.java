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
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.rmi.RemoteException;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import remote.CanvasServerInterface;
import server.util;


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

	private Color eraserColor = Color.white;
	private String canvasMessage = "Canvas server is down...";
	
	
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
				tryUpdate("start");
				
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
					shape = drawingbuttons(mode, shape);
					graphics.draw(shape);
					repaint();
				}
			}
		});
		

		//Listen the canvas actions.
		// draw the shap when the mouse is release
		// draw the shaoe one local client then send to server.
		addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				//as the location saves 
				endPt = e.getPoint();
				Shape shape = null;
				if(graphics != null) {
					shape = drawingWhenrelease(mode, shape);
					
					if (! mode.equals("text")) {
						try {
							graphics.draw(shape);
						}catch(NullPointerException e1) {
							//do nothing caused draw mode.
						}
					}
					
					repaint();
					
					// try to update users' canvas
					tryUpdate("end");
				}
			}
		});
		
		
	}


	//drawing the diffreent shape baed on different mode state.
	private Shape drawingWhenrelease(String state, Shape shape){
		switch(state){
			case "line":
				shape = Util.makeLine(shape, startPt, endPt);
				break;
			case "draw":
				shape = Util.makeLine(shape, startPt, endPt);
				break;
			case "rect":
				shape = Util.makeRect(shape, startPt, endPt);
				break;
			case "circle":
				shape = Util.makeCircle(shape, startPt, endPt);
				break;
			case "triangle":
				shape = Util.makeOval(shape, startPt, endPt);
				break;
			case "text":
				text = Util.getText();
				drawPreviousCanvas();
				graphics.setFont(new Font("TimesRoman", Font.PLAIN, 22));
				graphics.drawString(text, endPt.x, endPt.y);
				graphics.setStroke(new BasicStroke(1.0f));
				break;
		}
		return shape;
	}
	
	//drawing canvas when the mouse is clicked on the canvas
	private Shape drawingbuttons(String state, Shape shape){
		switch (state) {
			case "draw":
				shape = Util.makeLine(shape, startPt, endPt);
				startPt = endPt;
				tryUpdate("drawing");
				break;
			case "eraser":
				shape = Util.makeLine(shape, startPt, endPt);
				startPt = endPt;
				graphics.setPaint(eraserColor);
				graphics.setStroke(new BasicStroke(15.0f));
				tryUpdate("drawing");
				break;
			case "line":
				drawPreviousCanvas();
				shape = Util.makeLine(shape, startPt, endPt);
				break;
			case "rect":
				drawPreviousCanvas();
				shape = Util.makeRect(shape, startPt, endPt);
				break;
			case "circle":
				drawPreviousCanvas();
				shape = Util.makeCircle(shape, startPt, endPt);
				break;
			case "oval":
				drawPreviousCanvas();
				shape = Util.makeOval(shape, startPt, endPt);
				break;
			case "text":
				drawPreviousCanvas();
				graphics.setFont(new Font("TimesRoman", Font.PLAIN, 16));
				graphics.drawString("Enter text", endPt.x, endPt.y);
				shape = Util.makeText(shape, startPt);
				Stroke dashed = new BasicStroke(1, BasicStroke.CAP_BUTT, 
						BasicStroke.JOIN_BEVEL, 1, new float[] {3}, 0);
				graphics.setStroke(dashed);
				break;
		}
		return shape;
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
					util.errorMessage("Fail receiving imag!");
				}
			}
		}
		
		g.drawImage(image,0,0,null);
	}
	

	private void tryUpdate(String state){
		try {
			Util.sendMessage(state, 
			clientName, mode, eraserColor, endPt, "", server);
		} catch (Exception e) {
			Util.popupDialog(canvasMessage);
		}
	}
	
	// getter functions for basic informations
	public Color getCurrentColor() {
		return color;
	}
	
	//get current mode
	public String getCurrentMode() {
		return mode;
	}
	
	//get graphics
	public Graphics2D getGraohic() {
		return graphics;
	}
	
	//get canvas
	public BufferedImage getCanvas() {
		saveCanvas();
		return previousCanvas;
	}
	
	// reset the canvas page
	public void reset() {
		graphics.setPaint(eraserColor);
		graphics.fillRect(0, 0, 700, 350);
		graphics.setPaint(color);
		repaint();
	}
	
	//save the canvas page
	public void saveCanvas() {
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
	
	
	
}




	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

