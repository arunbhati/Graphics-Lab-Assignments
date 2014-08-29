import java.util.ArrayList;

public class MathUtility {
	
	// Return roots of quadratic equation.
	static ArrayList<Double> realRootOfQuadraticEq(double a,double b,double c){
		double d = ((b * b) - (4 * a * c )) ;
		ArrayList<Double> list = new ArrayList<Double>() ;
		if(a==0){
			if(b!=0){
				list.add(-(c/b)) ;
			}
			return list ;
		}		
		if(d>0){			
			double x1 = (-b + Math.sqrt(d)) / (2 * a) ;
			double x2 = (-b - Math.sqrt(d)) / (2 * a) ;
			list.add(new Double(x2)) ;
			list.add(new Double(x1)) ;
		}else if(d==0){
			list.add(new Double(((-b)/(2*a)))) ;
		}
		return list;
	}
	
	// Filter list on basis of minimum and maximum value.
	static ArrayList<Double> filter(ArrayList<Double> list,double low,double high){
		for(int i=0 ; i<list.size() ; i++){
			if((list.get(i) > high) || (list.get(i) < low)){
				list.remove(i) ;
				i-- ;
			}
		}
		return list ;
	}
	
	// Get value of cubic equation (a * x^3 + b * x^2 + c * x + d) on input x.
	static double getValueOfEquation(double[] list,double x){
		return list[0] + list[1] * x + list[2] * x * x + list[3] * x * x * x ; 
	}
	
	// Get roots of equation a * x^3 + b * x^2 + c * x + d.
	static ArrayList<Double> getCubicRoot(double a, double b, double c, double d){
		ArrayList<Double> roots = new ArrayList<Double>() ;
		if(a==0){
			return realRootOfQuadraticEq(b,c,d) ;
		}
		double f = (((3*c)/a)-((b*b)/(a*a))) / 3 ;
		double g = (((2*b*b*b)/(a*a*a)) - ((9*b*c)/(a*a)) + ((27*d)/(a))) / (27) ;
		double h = ((g*g)/4)+((f*f*f)/(27)) ;
		double answer ;
		if(h>0){
			double r = (-1 * (g/2)) + Math.sqrt(h) ;
			double s ;
			if(r < 0){
				s= -Math.pow(-r,0.33) ;
			}else{
				s = Math.pow(r,0.33) ;
			}
			double t = (-1 * (g/2)) - Math.sqrt(h) ;
			double u ;
			if(t<0){
				u = -Math.pow(-t, 0.33) ;
			}else{
				u = Math.pow(t,0.33) ;
			}
			answer = (s + u) - (b / (3*a)) ;
			roots.add(answer) ;
		}else if(f==0 && h==0 && g==0){
			if((d/a)<0){
				answer = Math.pow(-(d/a), 0.33) ;
			}else{
				answer = Math.pow((d/a),0.33) * -1 ;
			}
			
			roots.add(answer) ;
		}else{
			double i = Math.sqrt(((g*g)/4)-h) ;
			double j ;
			if(i<0){
				j = -Math.pow(-i,0.33) ;
			}else{
				j = Math.pow(i,0.33) ;
 			}
			double k = Math.acos(-1*(g/(2*i))) ;
			double l = j * -1 ;
			double m = Math.cos(k/3) ;
			double n = Math.sqrt(3) * Math.sin(k/3) ;
			double p = (b/(3*a)) * (-1) ;
			answer = 2 * j * Math.cos(k/3)-(b/(3*a)) ;
			roots.add(answer) ;
			answer = l * (m + n) + p ;
			roots.add(answer) ;
			answer = l * (m - n) + p ;
			roots.add(answer) ;
		}
		return roots ;
	}
}
