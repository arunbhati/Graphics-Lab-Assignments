import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;
import javax.swing.JFrame;
import javax.swing.SortOrder;

public class Ellipse {
	private double[] constants ;
	private JFrame jFrame ; 
	private DrawWindow jPanel ;
	private double[][] cofficents ;
	private ArrayList< ArrayList<Point> >	boundaryPointsList ;
	private int currentRegion ;

	
	// Constructor of SplineCurve Class that will initialize other classes.
	public Ellipse(int frameWidth,int frameHeight){
		jFrame = new JFrame("Ellipse Curve"); 
		jFrame.setSize(frameWidth, frameHeight); 
		jPanel = new  DrawWindow(frameWidth,frameHeight) ;	
		jFrame.add(jPanel); 
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		boundaryPointsList = new ArrayList< ArrayList<Point>>() ;
		boundaryPointsList.ensureCapacity(frameHeight) ;
		for(int i=0;i<frameHeight;i++){
			boundaryPointsList.add(new ArrayList<Point>()) ;
		}
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
		
		x = xCord ;
		y = yCord ;
		if(jPanel.isValidPixel(x+(int)constants[0],y+(int)constants[1])){
			Color c = Color.BLUE ;
			System.out.println(x + " " + y +  " " + (int)constants[0] + " " + (int)constants[1]) ;
			addPointInList(x,y,(int)constants[0],(int)constants[1]) ;
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
				addPointInList(x,y,(int)constants[0],(int)constants[1]) ;
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
				addPointInList(x,y,(int)constants[0],(int)constants[1]) ;
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
		
		ellipse.currentRegion = 0 ;
		ellipse.fillConstants() ;
		ellipse.drawPoints() ;
		
		ellipse.currentRegion = 1 ;
		ellipse.fillConstants() ;
		ellipse.drawPoints() ;
		
		ellipse.calculateRegion() ;
		
		// Draw Spline Curve
		ellipse.showEllipse();
	
		for(int i=0 ; i<ellipse.boundaryPointsList.size();i++){
			Collections.sort(ellipse.boundaryPointsList.get(i),new PointComparator());
			for (Point pt : ellipse.boundaryPointsList.get(i)) {
				System.out.print(pt.x + " ");
			}
			System.out.println() ;
		}
		/*for(int i=0;i<ellipse.boundaryPointsList.size();i++){
			for(int j=0;j<ellipse.boundaryPointsList.get(i).size();j++){
				System.out.print(ellipse.boundaryPointsList.get(i).get(j)+"  ") ;
			}
		}*/
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
	
	void fillConstants(){
		for(int i=0;i<4;i++){
			constants[i] = cofficents[this.currentRegion][i] ;
		}
	}
	
	void calculateRegion(){
		
		for(int i=0;i<boundaryPointsList.size();i++){
			int ln = boundaryPointsList.get(i).size() ;
			
			if(ln==0){
				continue ;
			}
			
			int minxCord , maxxCord ;
			boolean isSameRegion = true  ;
			
			minxCord = maxxCord = boundaryPointsList.get(i).get(0).x ;
			for(int j=1;j<boundaryPointsList.get(i).size();j++){
				if(boundaryPointsList.get(i).get(j).regionNumber!=boundaryPointsList.get(i).get(j-1).regionNumber){
					isSameRegion = false ;
					break ;
				}
				minxCord = Math.min(minxCord, boundaryPointsList.get(i).get(j).x) ;
				maxxCord = Math.max(maxxCord, boundaryPointsList.get(i).get(j).x) ;
			}
			
			if(isSameRegion){
				if(boundaryPointsList.get(i).get(0).regionNumber==0){
					fillLine(minxCord,maxxCord,boundaryPointsList.get(i).get(0).y,Color.ORANGE) ;	
				}else{
					fillLine(minxCord,maxxCord,boundaryPointsList.get(i).get(0).y,Color.GREEN) ;
				}
			}else if(ln==1){
				if(boundaryPointsList.get(i).get(0).regionNumber==1){
					jPanel.drawPixel(Color.GREEN, boundaryPointsList.get(i).get(0).x, boundaryPointsList.get(i).get(0).y ) ;
				}else{
					jPanel.drawPixel(Color.ORANGE, boundaryPointsList.get(i).get(0).x, boundaryPointsList.get(i).get(0).y ) ;
				}
			}else if(ln==2){
				for(int j=0;j<2;j++){
					if(boundaryPointsList.get(i).get(j).regionNumber==1){
						jPanel.drawPixel(Color.GREEN, boundaryPointsList.get(i).get(j).x, boundaryPointsList.get(i).get(j).y ) ;
					}else{
						jPanel.drawPixel(Color.ORANGE, boundaryPointsList.get(i).get(j).x, boundaryPointsList.get(i).get(j).y ) ;
					}
				}
			}else if(ln==3){
				
			}else if(ln==4){
				Collections.sort(boundaryPointsList.get(i),new PointComparator()) ;
				if(boundaryPointsList.get(i).get(0).regionNumber == boundaryPointsList.get(i).get(1).regionNumber ){
					if(boundaryPointsList.get(i).get(0).regionNumber==0){
						fillLine(boundaryPointsList.get(i).get(0).x,boundaryPointsList.get(i).get(1).x,boundaryPointsList.get(i).get(0).y,Color.ORANGE) ;	
					}else{
						fillLine(boundaryPointsList.get(i).get(0).x,boundaryPointsList.get(i).get(1).x,boundaryPointsList.get(i).get(0).y,Color.GREEN) ;
					}
					
					if(boundaryPointsList.get(i).get(2).regionNumber==0){
						fillLine(boundaryPointsList.get(i).get(2).x,boundaryPointsList.get(i).get(3).x,boundaryPointsList.get(i).get(2).y,Color.ORANGE) ;	
					}else{
						fillLine(boundaryPointsList.get(i).get(2).x,boundaryPointsList.get(i).get(3).x,boundaryPointsList.get(i).get(2).y,Color.GREEN) ;
					}
				}else if(boundaryPointsList.get(i).get(2).regionNumber == boundaryPointsList.get(i).get(1).regionNumber){
					
					fillLine(boundaryPointsList.get(i).get(1).x,boundaryPointsList.get(i).get(2).x,boundaryPointsList.get(i).get(2).y,Color.RED) ;
					if(boundaryPointsList.get(i).get(0).regionNumber==0){
						fillLine(boundaryPointsList.get(i).get(0).x,boundaryPointsList.get(i).get(1).x,boundaryPointsList.get(i).get(0).y,Color.ORANGE) ;	
						fillLine(boundaryPointsList.get(i).get(2).x,boundaryPointsList.get(i).get(3).x,boundaryPointsList.get(i).get(0).y,Color.ORANGE) ;	
						
					}else{
						fillLine(boundaryPointsList.get(i).get(0).x,boundaryPointsList.get(i).get(1).x,boundaryPointsList.get(i).get(0).y,Color.GREEN) ;
						fillLine(boundaryPointsList.get(i).get(2).x,boundaryPointsList.get(i).get(3).x,boundaryPointsList.get(i).get(0).y,Color.GREEN) ;	
						
					}
					
				}else{
					fillLine(boundaryPointsList.get(i).get(1).x,boundaryPointsList.get(i).get(2).x,boundaryPointsList.get(i).get(2).y,Color.RED) ;
					if(boundaryPointsList.get(i).get(0).regionNumber==0){
						fillLine(boundaryPointsList.get(i).get(0).x,boundaryPointsList.get(i).get(1).x,boundaryPointsList.get(i).get(0).y,Color.ORANGE) ;	
					}else{
						fillLine(boundaryPointsList.get(i).get(0).x,boundaryPointsList.get(i).get(1).x,boundaryPointsList.get(i).get(0).y,Color.GREEN) ;
					}
					if(boundaryPointsList.get(i).get(3).regionNumber==0){
						fillLine(boundaryPointsList.get(i).get(2).x,boundaryPointsList.get(i).get(3).x,boundaryPointsList.get(i).get(0).y,Color.ORANGE) ;	
					}else{
						fillLine(boundaryPointsList.get(i).get(2).x,boundaryPointsList.get(i).get(3).x,boundaryPointsList.get(i).get(0).y,Color.GREEN) ;
					}
				}
			}
		}
		
		
		
		/*if(cofficents[0][0]<cofficents[1][0]){
			
		}else if(cofficents[0][0]<cofficents[1][0]){
			
		}else if(cofficents[0][1]<cofficents[1][1]){
			
		}else if(cofficents[0][1]>cofficents[1][1]){
			
		}else{
			if(cofficents[0][2]!=cofficents[1][2]){
				jPanel.fillRegion(Color.GREEN,(int)cofficents[0][0],(int)cofficents[0][1]) ;
				jPanel.fillRegion(Color.BLACK,((int)cofficents[0][0]+ (int)Math.max(cofficents[0][2], cofficents[1][2]) - 1),(int)cofficents[0][1]) ;
			}else{
				
			}
		}*/
	}
	
	private void fillLine(int xFirst,int xLast,int y,Color c){
		for(int i=xFirst;i<=xLast;i++){
			jPanel.drawPixel(c, i, y);
		}
	}
	
	private void addPointInList(int x,int y,int xc,int yc){
		Point p ;
		int k = 500 ;
		
		p = new Point(x+xc,y+yc,currentRegion) ;
		boundaryPointsList.get(y+yc+k).add(p) ;
		p = new Point(x+xc,-y+yc,currentRegion) ;
		boundaryPointsList.get(-y+yc+k).add(p) ;
		p = new Point(-x+xc,-y+yc,currentRegion) ;
		boundaryPointsList.get(-y+yc+k).add(p) ;
		p = new Point(-x+xc,y+yc,currentRegion) ;
		boundaryPointsList.get(y+yc+k).add(p) ;
		return ;
	}
}

/* Point class corresponding to each point on curve. it will contain information about cordinates and
 * value of sign of d(x)/d(u) or d(y)/d(u) that are represented by isPositivex and isPositivey.
 */
class Point{
	int x ;
	int y ;
	int regionNumber ;
	
	Point(int x,int y,int n){
		this.x = x ;
		this.y = y ;
		this.regionNumber = n ;
	}
	
	// Print information related to point.
	public String toString(){	
		return "region = " + regionNumber + " xpos = " + x + "ypos = " + y ;
	}
}

class PointComparator implements Comparator<Point> {
	@Override
	public int compare(Point pt1, Point pt2) {
		if (pt1.x > pt2.x) {
			return 1;
		} else if (pt1.x < pt2.x) {
			return -1;
		}
		return 0;
	}
}

