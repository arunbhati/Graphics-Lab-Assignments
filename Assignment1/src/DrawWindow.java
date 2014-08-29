import java.awt.Color; 
import java.awt.Graphics; 
import java.awt.Graphics2D; 
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class DrawWindow extends JPanel {
	static int xCordinate ;
	static int yCordinate ;
	private static BufferedImage canvas ;
	
	// Constructor to draw canvas of given width and height.
	public DrawWindow(int width,int height){
		canvas = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		fillCanvas(Color.WHITE);
	}
	
	// Function to draw white background and x and y axis.
	public void fillCanvas(Color c) {
		int color = c.getRGB();
	    for (int x = 0; x < canvas.getWidth(); x++) {
	    	for (int y = 0; y < canvas.getHeight(); y++) {
	    		canvas.setRGB(x, y, color);
	        }
	    }
	    drawAxes() ;
	    repaint();
	}
	
	// Overloaded function of JPanel.
	public void paintComponent(Graphics g) { 		
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.BLACK);
		g2d.drawImage(canvas, null, null);
	}
	
	// Draw pixel at cordinate (x,y) with color c.
	public void drawPixel(Color c, int x, int y) {
		int color = c.getRGB();
		canvas.setRGB(x+500,500-y, color);
		repaint();
	}
	 
	// Check whether a pixel is valid or not.
	public boolean isValidPixel(int x,int y){
		return ( (x >= -499) && (x < (canvas.getWidth()/2)) && (y >= -499) && (y < (canvas.getHeight()/2)) ) ;
	}
	 
	// Draw x and y axes on canvas.
	public void drawAxes(){
		Color color = Color.LIGHT_GRAY ;
		for(int i=0 ; i<500 ; i++){
			drawPixel(color,i,0) ;
			drawPixel(color,0,i) ;
			drawPixel(color,-i,0) ;
			drawPixel(color,0,-i) ;
		}
	}
}
