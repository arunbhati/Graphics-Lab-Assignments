import java.awt.Color;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.JFrame;

public class SplineCurve {
	private double[][] constants ;
	private JFrame jFrame ; 
	private DrawWindow jPanel ;
	private ArrayList<Point> interval ;
	
	// Constructor of SplineCurve Class that will initialize other classes.
	public SplineCurve(int frameWidth,int frameHeight){
		jFrame = new JFrame("Spline Curve"); 
		jFrame.setSize(frameWidth, frameHeight); 
		jPanel = new  DrawWindow(frameWidth,frameHeight) ;	
		jFrame.add(jPanel); 
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	// Print spine curve
	public void showSplineCurve(){
		jFrame.setVisible(true);
	}
	
	// Get input from user.
	private void getInput(){
		Scanner input = new Scanner(System.in) ;
		constants = new double[2][4] ;
		System.out.println("Enter a0 a1 a2 a3") ;
		for(int i=0;i<4;i++)
	        constants[0][i] = input.nextDouble();
		System.out.println("Enter b0 b1 b2 b3") ;
		for(int i=0;i<4;i++)
	        constants[1][i] = input.nextDouble();
		return ;
	}
	
	/* Spline-Curve will be divided into set of intervals. according to value of u these interval will be divided.
	 * In particular interval p value of d(y)/d(u) and value of d(x)/d(u) will remain constant.
	 * And Mid-Point algorithm will be applied on individual intervals.
	 * */
	private void getInterval(){
		ArrayList<Double> rootsOfxbyu = MathUtility.realRootOfQuadraticEq(
				3 * constants[0][3], 
				2 * constants[0][2], 
				1 * constants[0][1]) ;
		
		ArrayList<Double> rootsOfybyu = MathUtility.realRootOfQuadraticEq(
				3 * constants[1][3], 
				2 * constants[1][2], 
				1 * constants[1][1]) ;
		
		rootsOfxbyu = MathUtility.filter(rootsOfxbyu,0,1) ;
		rootsOfybyu = MathUtility.filter(rootsOfybyu,0,1) ;
		
		int index1 = 0, index2 = 0 , fstln = rootsOfxbyu.size() , sndln = rootsOfybyu.size();
		
		
		interval = new ArrayList<Point>() ;
		interval.add(new Point(0)) ;
		interval.get(0).isPositivex = getSignOfDerivative(0,0) ;
		interval.get(0).isPositivey = getSignOfDerivative(1,0) ;
		
		if(fstln > 0 && sndln > 0){
			if(rootsOfxbyu.get(0)==interval.get(0).cordinate[0]){
				index1++ ;
			}
			if(rootsOfybyu.get(0)==interval.get(0).cordinate[0]){
				index2++ ;
			}
		}else if(fstln > 0){
			if(rootsOfxbyu.get(0)==interval.get(0).cordinate[0]){
				index1++ ;
			}
		}else if(sndln > 0){
			if(rootsOfybyu.get(0)==interval.get(0).cordinate[0]){
				index2++ ;
			}
		}else{
			interval.add(new Point(1)) ;
			return ;			
		}
		
		while((index1 < fstln) || (index2 < sndln)){
			if((index1 < fstln) && (index2 < sndln)){
				if(rootsOfxbyu.get(index1) < rootsOfybyu.get(index2)){
					interval.add(new Point(rootsOfxbyu.get(index1))) ;
					interval.get(interval.size()-1).isPositivex = (interval.get(interval.size()-2).isPositivex + 1 ) % 2 ;
					interval.get(interval.size()-1).isPositivey = interval.get(interval.size()-2).isPositivey ;
					index1++ ;
				}else if(rootsOfxbyu.get(index1) > rootsOfybyu.get(index2)){
					interval.add(new Point(rootsOfybyu.get(index2))) ;
					interval.get(interval.size()-1).isPositivey = (interval.get(interval.size()-2).isPositivey + 1 ) % 2 ;
					interval.get(interval.size()-1).isPositivex = interval.get(interval.size()-2).isPositivex ;
					index2++ ;
				}else{
					interval.add(new Point(rootsOfxbyu.get(index1))) ;
					interval.get(interval.size()-1).isPositivex = (interval.get(interval.size()-2).isPositivex + 1 ) % 2 ;
					interval.get(interval.size()-1).isPositivey = (interval.get(interval.size()-2).isPositivey + 1 ) % 2 ;
					index1++ ;
					index2++ ;
				}
			}else if((index1 < fstln)){
				interval.add(new Point(rootsOfxbyu.get(index1))) ;
				interval.get(interval.size()-1).isPositivex = (interval.get(interval.size()-2).isPositivex + 1 ) % 2 ;
				interval.get(interval.size()-1).isPositivey = interval.get(interval.size()-2).isPositivey ;
				index1++ ;
			}else{
				interval.add(new Point(rootsOfybyu.get(index2))) ;
				interval.get(interval.size()-1).isPositivey = (interval.get(interval.size()-2).isPositivey + 1 ) % 2 ;
				interval.get(interval.size()-1).isPositivex = interval.get(interval.size()-2).isPositivex ;
				index2++ ;
			}
		}
		
		if(interval.get(interval.size()-1)!=(new Point(1))){
			interval.add(new Point(1)) ;
		}
		return ;
	}
	
	// It will return whether value of derivative d(y)/d(u) or d(x)/d(u) is positive or not.
	private int getSignOfDerivative(int index,double u){
		if(getValueOfDerivative(index,u) >= 0)
			return 1 ;
		else
			return 0 ;
	}
	
	// It will return d(y)/d(u) or d(x)/d(u) value according to input parameter.
	private double getValueOfDerivative(int index,double u){
		return 3 * constants[index][3] * u * u + 2 * constants[index][2] * u + constants[index][1] ;
	}
	
	// This function will draw points from each interval and in every interval it will follow Mid-Point algorithm.
	public void drawPoints(){
		int xCord, yCord ;
		double uValue, uValueLimit, tempxCord, tempyCord ;
		ArrayList<Double> realRootlist ;
		
		for(int i=0 ; i<interval.size()-1 ;i++){
			uValue = interval.get(i).cordinate[0] ;
			uValueLimit = interval.get(i+1).cordinate[0] ;
			xCord = (int) Math.ceil(MathUtility.getValueOfEquation(constants[0],uValue)) ;  
			yCord = (int) Math.ceil(MathUtility.getValueOfEquation(constants[1],uValue)) ;
			if(jPanel.isValidPixel(xCord,yCord)){
				jPanel.drawPixel(Color.BLACK, xCord, yCord) ;
			}
			while(uValue < uValueLimit){
				double yByu = getValueOfDerivative(1,uValue) ;
				double xByu = getValueOfDerivative(0,uValue) ;
				
				if(Math.abs(yByu/xByu)>=1){
					
					if(interval.get(i).isPositivey == 1){
						yCord++ ;
						realRootlist = MathUtility.getCubicRoot(constants[1][3],constants[1][2],constants[1][1],(constants[1][0]-yCord)) ;
					}else{
						yCord-- ;
						realRootlist = MathUtility.getCubicRoot(constants[1][3],constants[1][2],constants[1][1],(constants[1][0]-yCord)) ;
					}
					
					tempxCord = xCord ;
					double minim = Math.abs((tempxCord-MathUtility.getValueOfEquation(constants[0],realRootlist.get(0)))) ;
					uValue = realRootlist.get(0) ;
					
					for(int j=1;j<realRootlist.size();j++){
						if( Math.abs((tempxCord-MathUtility.getValueOfEquation(constants[0],realRootlist.get(j)))) < minim){
							uValue = realRootlist.get(j) ;
							minim = Math.abs((tempxCord-MathUtility.getValueOfEquation(constants[0],realRootlist.get(j)))) ;
						}
					}

					double xVal = MathUtility.getValueOfEquation(constants[0],uValue) ;
					
					if(xVal > xCord + 0.5)
							xCord++;
					else if(xVal < xCord -0.5)
							xCord--;
					
				
				}else{
					if(interval.get(i).isPositivex == 1){
						xCord++ ;
						realRootlist = MathUtility.getCubicRoot(constants[0][3],constants[0][2],constants[0][1],(constants[0][0]-xCord)) ;
					}else{
						xCord-- ;
						realRootlist = MathUtility.getCubicRoot(constants[0][3],constants[0][2],constants[0][1],(constants[0][0]-xCord)) ;
					}
										
					tempyCord = yCord ;
					
					double minim = Math.abs((tempyCord-MathUtility.getValueOfEquation(constants[1],realRootlist.get(0)))) ;
					uValue = realRootlist.get(0) ;
					
					for(int j=1;j<realRootlist.size();j++){
						if( Math.abs((tempyCord-MathUtility.getValueOfEquation(constants[1],realRootlist.get(j)))) < minim){
							uValue = realRootlist.get(j) ;
							minim = Math.abs((tempyCord-MathUtility.getValueOfEquation(constants[1],realRootlist.get(j)))) ;
						}
					}

					double yVal = MathUtility.getValueOfEquation(constants[1],uValue) ;
					
					if(yVal > (yCord + 0.5) ){
						yCord++ ;
					}else if(yVal < (yCord - 0.5)){
						yCord-- ;
					}
				}
				
				if(jPanel.isValidPixel(xCord,yCord)){
					jPanel.drawPixel(Color.BLACK, xCord, yCord) ;
				}	
			}	
		}
	}
	
	// The first function that will be called 
	public static void main(String [ ] args){
		
		// Create Instance of SplineCurve Class
		SplineCurve splineCurve = new SplineCurve(1000,1000) ;
	
		while(true){
			// Get inputs
			splineCurve.getInput() ;
		
			splineCurve.getInterval() ;
			splineCurve.drawPoints() ;
				
			// Draw Spline Curve
			splineCurve.showSplineCurve();
		}
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