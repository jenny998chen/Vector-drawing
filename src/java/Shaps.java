import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;  
import java.io.*;
import java.awt.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.lang.*;

//new item dq.push(top layer)
//print backwards
//switch arround x1 x2 y1 y2 to allow draing form bottom to top
abstract class myShape  implements Serializable{
	int xStart,yStart,x1,y1,x2,y2,w,h;
	int border;
	Color color;
	int thickness;
	boolean selected=false;
	int delX, delY;
	myShape(int x1,int y1,int thickness,Color c){
		this.xStart=x1;
		this.yStart=y1;
		this.thickness=thickness;
		this.color=c;
	}
	void setCoords(int x,int y){
		if(x<xStart){
			this.x1=x;
			this.x2=xStart;
		}else{
			this.x1=xStart;
			this.x2=x;	
		}
		this.w=this.x2-this.x1;

		if(y<yStart){
			this.y1=y;
			this.y2=yStart;
		}else{
			this.y1=yStart;
			this.y2=y;	
		}
		this.h=this.y2-this.y1;
	}
	void calcDelta(int x2,int y2){
		this.delX=x2 - this.x1;
		this.delY=y2 - this.y1;
	}
	void updateLocation(int x2,int y2){
		this.x1=x2 -delX;
		this.y1=y2 -delY;
		this.x2=this.x1 +w;
		this.y2=this.y1 +h;
	}

	abstract void drawShape(Graphics2D g);
	abstract boolean contains(int x,int y);
}
class Rect extends myShape{
	// int w,h;
	
	Rect(int x1,int y1,int thickness,Color c){
		super(x1,y1,thickness,c);
	}
	void drawShape(Graphics2D g){
		g.setStroke(new BasicStroke(thickness));

		if(selected){
			int s=thickness/2;
			g.setColor(new Color(0, 0, 0, 150));
        	g.fillRect(x1 + 5 -s, y1 +5 -s, w+thickness, h+thickness);
		}

		g.setColor(color);
	    g.fillRect(x1,y1,w,h);
		g.setColor(Color.BLACK);
        g.drawRect(x1,y1,w,h);
	}
	
	boolean contains(int x,int y){
		return (x >= x1 && y >= y1 && x <= x2 && y <= y2);
	}
}
class Oval extends myShape{
	// int w,h;
	Oval(int x1,int y1,int thickness,Color c){
		super(x1,y1,thickness,c);
	}
	void drawShape(Graphics2D g){
		g.setStroke(new BasicStroke(thickness));
		if(selected){
			int s=thickness/2;
			g.setColor(new Color(0, 0, 0, 150));
        	g.fillOval(x1 + 5-s , y1 +5-s, w+thickness, h+thickness);
		}
		g.setColor(color);
        g.fillOval(x1,y1,w,h);
		g.setColor(Color.BLACK);
        g.drawOval(x1,y1,w,h);

	}

	boolean contains(int x,int y){
		int a=w/2;
		int b=h/2;
		// System.out.println(s.x1 +" "+s.y1 +" "+s.x1 +" "+s.y2 );
		return Math.pow(((double)(x-x1-a)/a),2) + Math.pow(((double)(y-y1-b)/b),2) <= 1;
	}
}
class Line extends myShape{
	// int xEnd,yEnd;
	Line(int x1,int y1,int thickness,Color c){
		super(x1,y1,thickness,c);
		this.x1=x1;
		this.y1=y1;
	}
	void setCoords(int x,int y){
		this.x2=x;
		this.y2=y;
		w=x2-x1;
		h=y2-y1;
	}

	void drawShape(Graphics2D g){
		if(selected){
			int s=thickness/2;

			g.setStroke(new BasicStroke(5+s));
			g.setColor(new Color(0, 0, 0, 130));

			g.drawLine(x1 +2+s , y1 + 2+s , x2 +2+s , y2 +2+s);
		}
		g.setStroke(new BasicStroke(thickness));
		g.setColor(Color.BLACK);
		g.drawLine(x1 , y1 , x2 , y2 );
	}
	

	boolean contains(int x,int y){
		// int right,left,top,bottom;
		int left =Math.min(x1,x2);
		int right =Math.max(x1,x2);
		int top =Math.min(y1,y2);
		int bottom =Math.max(y1,y2);
		return (x >= left && y >= top && x <= right && y <= bottom) &&
		(Math.abs((x - x1) * h - (y - y1) * w) / Math.sqrt(w * w + h * h) <= 5);
	}
}
//add static gobal variable for 
