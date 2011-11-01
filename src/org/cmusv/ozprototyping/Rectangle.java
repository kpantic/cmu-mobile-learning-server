package org.cmusv.ozprototyping;

public class Rectangle {
	public double x;
	public double y;
	public double width;
	public double height;
	public String action;
	
	public Rectangle(double x, double y, double w, double h, String a){
		this.x = x;
		this.y = y;
		this.width = w;
		this.height = h;
		this.action = a;
	}
	
	public boolean isIn(float pointX, float pointY){
		return ((pointX >= x && pointX <= x + width)
				&&(pointY >= y && pointY <= y + height)); 
	}
}
