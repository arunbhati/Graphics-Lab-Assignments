import java.awt.Color;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.JFrame;

public class Ellipse {
	private double[] constants ;
	private JFrame jFrame ; 
	private DrawWindow jPanel ;
	private double[][] cofficents ;
	
	// Constructor of SplineCurve Class that will initialize other classes.
	public Ellipse(int frameWidth,int frameHeight){
		jFrame = new JFrame("Ellipse Curve"); 
		jFrame.setSize(frameWidth, frameHeight); 
		jPanel = new  DrawWindow(frameWidth,frameHeight) ;	
		jFrame.add(jPanel); 
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	// Print spine curve
	public void showEllipse(){
		jFrame.setVisible(true);
	}
	
	// Get input from user.
	private void getInput(){
		Scanner input = new Scanner(System.in) ;
		constants = new double[4] ;
		cofficents = new double[2][4] ;
		
		System.out.println("Enter cx cy rx ry for ellipse 1") ;
		for(int i=0;i<4;i++)
			cofficents[0][i] = constants[i] = input.nextDouble();
		
		System.out.println("Enter cx cy rx ry for ellipse 2") ;
		for(int i=0;i<4;i++)
			cofficents[1][i] = constants[i] = input.nextDouble();
		
		return ;
	}
	
	// It will return whether value of derivative d(y)/d(u) or d(x)/d(u) is positive or not.
	private int getCurrentRegion(double x, double y){
		if((2*constants[3]*constants[3]*x) >= (2*constants[2]*constants[2]*y))
			return 2 ;
		else
			return 1 ;
	}
	
	// This function will draw points from each interval and in every interval it will follow Mid-Point algorithm.
	public void drawPoints(){
		int xCord, yCord , x , y ;
		double p , rySquare = Math.pow(constants[3], 2) , rxSquare = Math.pow(constants[2], 2);
		p  = constants[3] * constants[3]  - constants[2] * constants[2]	* constants[3] + (1 / 4.0) * constants[2] * constants[2] ;  
		xCord = 0 ;
		yCord = (int)constants[3] ;
		
		if(jPanel.isValidPixel((int)constants[0],(int)constants[1])){
			Color c = Color.BLUE ;
		//	jPanel.drawPixel(c,(int)constants[0],(int)constants[1]) ;
		}
		
		x = xCord ;
		y = yCord ;
		if(jPanel.isValidPixel(x+(int)constants[0],y+(int)constants[1])){
			Color c = Color.BLUE ;
			jPanel.drawPixel(c, x+(int)constants[0], y+(int)constants[1]) ;
			jPanel.drawPixel(c, x+(int)constants[0], -y+(int)constants[1]) ;
			jPanel.drawPixel(c, -x+(int)constants[0],-y+(int)constants[1]) ;
			jPanel.drawPixel(c, -x+(int)constants[0], y+(int)constants[1]) ;
		}
		
		
		while(getCurrentRegion(xCord,yCord)==1){
			if(p < 0){
				xCord++ ;
				p = p + 2* rySquare * xCord + rySquare ;
			}else{
				xCord++ ;
				yCord-- ;
				p = p + 2* rySquare * xCord - 2 * rxSquare * yCord + rySquare ;
			}
			x = xCord ;
			y = yCord ;
			if(jPanel.isValidPixel(x+(int)constants[0],y+(int)constants[1])){
				Color c = Color.BLUE ;
				jPanel.drawPixel(c, x+(int)constants[0], y+(int)constants[1]) ;
				jPanel.drawPixel(c, x+(int)constants[0], -y+(int)constants[1]) ;
				jPanel.drawPixel(c, -x+(int)constants[0],-y+(int)constants[1]) ;
				jPanel.drawPixel(c, -x+(int)constants[0], y+(int)constants[1]) ;
			}
		}
		
		p = rySquare * Math.pow((xCord+0.5),2) + rxSquare * Math.pow((yCord-1),2) - rxSquare * rySquare ;
		while(yCord >= 0){
			if(p > 0){
				yCord-- ;
				p = p - 2 * rxSquare * yCord + rxSquare ;
			}else{
				yCord-- ;
				xCord++ ;
				p = p + 2 * rySquare * xCord - 2 * rxSquare * yCord + rxSquare ; 
			}
			x = xCord ;
			y = yCord ;
			if(jPanel.isValidPixel(x+(int)constants[0],y+(int)constants[1])){
				Color c = Color.BLUE ;
				jPanel.drawPixel(c, x+(int)constants[0], y+(int)constants[1]) ;
				jPanel.drawPixel(c, x+(int)constants[0], -y+(int)constants[1]) ;
				jPanel.drawPixel(c, -x+(int)constants[0],-y+(int)constants[1]) ;
				jPanel.drawPixel(c, -x+(int)constants[0], y+(int)constants[1]) ;
			}
		}
		
		return ;
	}
	
	// The first function that will be called 
	public static void main(String [ ] args){
		
		// Create Instance of SplineCurve Class
		Ellipse ellipse = new Ellipse(1000,1000) ;
	
		// Get inputs
		ellipse.getInput() ;
		
		ellipse.fillConstants(0) ;
		ellipse.drawPoints() ;
		ellipse.fillConstants(1) ;
		ellipse.drawPoints() ;
	//	ellipse.jPanel.drawPixel(Color.BLUE,0,100) ;
	//	ellipse.jPanel.drawPixel(Color.BLUE,0,-100) ;
	//	System.out.println(ellipse.jPanel.canvas.getRGB(0+500, 500-100)) ;
		ellipse.calculateRegion() ;
		// Draw Spline Curve
		ellipse.showEllipse();
	}
	
	// Function for debugging purpose 
	@SuppressWarnings("unused")
	private void printArray(double[] Arry,int n){
		for(int i=0;i<n;i++){
			System.out.print(Arry[i] + " ") ;
		}
		return ;
	}
	
	// Function for debugging purpose
	@SuppressWarnings("unused")
	private void printArray(Object[] Arry,int n){
		for(int i=0;i<n;i++){
			System.out.print(Arry[i].toString() + " ") ;
		}
		return ;
	}
	
	void fillConstants(int index){
		for(int i=0;i<4;i++){
			constants[i] = cofficents[index][i] ;
		}
	}
	
	void calculateRegion(){
		if(cofficents[0][0]<cofficents[1][0]){
			
		}else if(cofficents[0][0]<cofficents[1][0]){
			
		}else if(cofficents[0][1]<cofficents[1][1]){
			
		}else if(cofficents[0][1]>cofficents[1][1]){
			
		}else{
			if(cofficents[0][2]!=cofficents[1][2]){
				jPanel.fillRegion(Color.GREEN,(int)cofficents[0][0],(int)cofficents[0][1]) ;
				jPanel.fillRegion(Color.BLACK,((int)cofficents[0][0]+ (int)Math.max(cofficents[0][2], cofficents[1][2]) - 1),(int)cofficents[0][1]) ;
			}else{
				
			}
		}
	}
}

/* Point class corresponding to each point on curve. it will contain information about cordinates and
 * value of sign of d(x)/d(u) or d(y)/d(u) that are represented by isPositivex and isPositivey.
 */
class Point{
	int dimention ;
	double[] cordinate ;
	int isPositivex ;
	int isPositivey ;
	
	// Constructor for 1-D point.
	Point(double d){
		isPositivex = 1 ;
		isPositivey = 1 ;
		dimention = 1 ;
		cordinate = new double[1] ;
		cordinate[0] = d ;
	}
	
	// Constructor for N-D point where n > 1.
	Point(int d,double[] list){
		isPositivex = 1 ;
		isPositivey = 1 ;
		dimention = d ;
		cordinate = new double[d] ;
		for(int i=0 ; i<dimention ; i++){
			cordinate[i] = list[i] ;
		}
	}
	
	// Print information related to point.
	public String toString(){	
		return cordinate[0] + "xpos = " + isPositivex + "ypos = " + isPositivey ;
	}
}