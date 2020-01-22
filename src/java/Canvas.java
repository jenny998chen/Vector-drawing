import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;  
import java.io.*;
import java.awt.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.lang.*;

//add static gobal variable for 

public class Canvas extends JPanel{
	 Deque<myShape> shapes = new ArrayDeque<myShape>(); 

	boolean dragged=false;
	boolean init=false;
	// String c ="#F4511E";
	Color color=Color.decode("#f4511e");
	int thickness=1;
	String action="Rect";
	int x1,y1,x2,y2,w,h;
	myShape curShape;

	ToolBar tb;
	void setToolBarReference(ToolBar tb){
		this.tb=tb;
	}
	Canvas(){
		addMouseListener(new MouseAdapter(){
			public void mousePressed(MouseEvent e){
				init=true;
				x1=e.getX();
				y1=e.getY();
				switch(action){
					case "Rect":
						curShape =new Rect(x1,y1,thickness,color);
						break;
					case "Oval":
						curShape =new Oval(x1,y1,thickness,color);
						break;
					case "Line":
						curShape =new Line(x1,y1,thickness,color);
						break;
					case "Move":
						System.out.println(shapes.size());
						System.out.println(x1 +" "+y1);
						for(myShape s: shapes){
							// System.out.println(s.x1 +" "+s.y1 +" "+s.x2 +" "+s.y2 );
							if(s.contains(x1,y1)){						
								shapes.remove(s);
								shapes.push(s);
								if(curShape!=null)curShape.selected=false;
								curShape=s;
								curShape.selected=true;
								s.calcDelta(x1,y1);
								
								tb.matchStyleToShape(curShape.color,curShape.thickness);
								color=curShape.color;
								thickness=curShape.thickness;

								repaint();
								return;
							}
						}
						if(curShape!=null){
							curShape.selected=false;
							curShape=null;
							repaint();
						}
						break;
				}
				System.out.println("Pressed");
			}
			public void mouseReleased(MouseEvent e){
				if(dragged){
					System.out.println("Released");
					shapes.push(curShape);
					dragged=false;
					curShape=null;
				}else if(action.equals("Move")){
					// shapes.push(curShape);
					
				}else{
					switch(action){
					case "Fill":
						for(myShape s: shapes){
							// System.out.println(s.x1 +" "+s.y1 +" "+s.x2 +" "+s.y2 );
							if(s.contains(x1,y1)){
								System.out.println("true");
								s.color=color;
								// s.filled=true;
								shapes.remove(s);
								shapes.push(s);
								repaint();

								break;
							}
						}
						break;
					case "Erase":
						for(myShape s: shapes){
							if(s.contains(x1,y1)){
								shapes.remove(s);
								repaint();
								break;
							}
						}
						break;
					}
				}
			}
		});
		addMouseMotionListener(new MouseAdapter(){
			@Override
			public void mouseDragged(MouseEvent e) {
				x2=e.getX();
				y2=e.getY();
				if(action.equals("Rect")||action.equals("Oval")||action.equals("Line")){
					curShape.setCoords(x2,y2);
					repaint();
					dragged=true;
				}else if(action.equals("Move")&&curShape!=null){
					// System.out.println("Dragged");
					curShape.updateLocation(x2,y2);
					repaint();
				}
			}
		});

	}

	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		// System.out.println("paintComponent");
		Graphics2D g2= (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		// if(!init){
		// 	return;
		// }
		Iterator it = shapes.descendingIterator();
		while(it.hasNext()){
			myShape s=(myShape)it.next();
			g2.setStroke(new BasicStroke(s.thickness));
		    s.drawShape(g2);
		}	
		if(dragged){
			g2.setStroke(new BasicStroke(curShape.thickness));
			curShape.drawShape(g2);
		}
	}
}