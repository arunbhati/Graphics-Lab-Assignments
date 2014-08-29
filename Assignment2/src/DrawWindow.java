import java.awt.Color; 
import java.awt.Graphics; 
import java.awt.Graphics2D; 
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.Stack;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class DrawWindow extends JPanel {
	static int xCordinate ;
	static int yCordinate ;
	public static BufferedImage canvas ;
	
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
	    //drawAxes() ;
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
		Color color = Color.WHITE ;
		for(int i=0 ; i<500 ; i++){
			drawPixel(color,i,0) ;
			drawPixel(color,0,i) ;
			drawPixel(color,-i,0) ;
			drawPixel(color,0,-i) ;
		}
	}
	
	public void fillRegion(Color c,int x,int y){
		System.out.println(x + " " + y ) ;
		if(isValidPixel(x,y)){
			FillDFS(c,x,y) ;
		}
	}
	
	void FillDFS(Color c,int x,int y){
		
		Stack<java.awt.Point> stk = new Stack<java.awt.Point>() ;
		java.awt.Point pnt = new java.awt.Point() ;
		java.awt.Point pnt1 ;
 		pnt.setLocation(x, y) ;
 		if(isValidPixel(x,y)){
 			stk.push(pnt) ;
 		}
 		while(!stk.isEmpty()){
 			pnt1 = stk.pop() ;
 			x = pnt1.x ;
 			y = pnt1.y ;
 			drawPixel(c,x,y) ;
 			if(isValidPixel(x,y-1) && canvas.getRGB(x+500, 500-y+1) == Color.WHITE.getRGB()){
 				pnt = new java.awt.Point(x,y-1) ;
 				stk.push(pnt) ;
 			}
 			if(isValidPixel(x-1,y) && canvas.getRGB(500+x-1, 500-y) == Color.WHITE.getRGB()){
 				pnt = new java.awt.Point(x-1,y) ;
 				stk.push(pnt) ;
 			}
 			if(isValidPixel(x,y+1) && canvas.getRGB(500+x,500-y-1) == Color.WHITE.getRGB()){
 				pnt = new java.awt.Point(x,y+1) ;
 				stk.push(pnt) ;
 			}
 			if(isValidPixel(x+1,y) && canvas.getRGB(500+x+1, 500-y) == Color.WHITE.getRGB()){
 				pnt = new java.awt.Point(x+1,y) ;
 				stk.push(pnt) ;
 			}
 		}
		
		return ;
	}
}
