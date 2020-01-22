import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;  
import java.io.*;
import java.awt.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.lang.*;

class ToolBar extends JPanel{

	class Tools extends JPanel{
		ButtonGroup  group = new ButtonGroup();
		JRadioButton Btns[];
		Tools(int col,int selIndex,String actions[]){
				setMaximumSize(new Dimension(112, 168));
				setLayout(new GridLayout(0,col));

				Btns=new JRadioButton[actions.length];
				ActionListener listener = new ActionListener() {
				    @Override
				    public void actionPerformed(ActionEvent e) {
				        String s = ((JRadioButton)e.getSource()).getText();
						notifyCanv(s);
				    }
				};
				for(int i = 0; i < actions.length; i++) {
					try{
						JRadioButton btn =makeRB(actions[i]);
						if(i==selIndex)btn.setSelected(true);
					   btn.setText(actions[i]);
					   btn.addActionListener(listener);
					   group.add(btn);
					   add(btn);

					   Btns[i]=btn;
		    		}catch(IOException e){}

				}
		}
		JRadioButton makeRB(String s) throws IOException{
			final BufferedImage img=ImageIO.read(getClass().getResource("image3/"+s+".png"));;

		    return new JRadioButton(){
			    public void paintComponent(Graphics g) {
			    	super.paintComponent(g);
			    	// BufferedImage img=null;
				    // try{
				    // 	img=ImageIO.read(getClass().getResource("image3/"+s+".png"));;
				    // }catch(IOException e){}
			    	g.setColor(Color.WHITE);
			    	if(this.isSelected()==true){
			    		g.setColor(Color.decode("#dadada"));
			    	}else if(this.getModel().isRollover()==true){
			    		g.setColor(Color.decode("#ededed"));
			    	}
			    	g.fillRect(0, 0, getSize().width , getSize().height );


				    g.drawImage( img,8,8,null);
				}	
			};
		}
		void notifyCanv(String s){
			if(canv.curShape!=null && canv.action.equals("Move") && !s.equals("Move")){
    			canv.curShape.selected=false;
    			canv.repaint();
    		}
    		canv.action=s;
		}
		void setSelected(String s){
			for(JRadioButton btn: Btns){
				System.out.println(btn.getText()+" "+s);
				if(btn.getText().equals(s)){
					btn.setSelected(true);
				}
			}
		}
	}
	class Colors extends Tools{
		Colors(int col,int selIndex,String actions[]){
			super(col,selIndex,actions);
		}
		JRadioButton makeRB(String s){
			JRadioButton btn =new JRadioButton(){
				@Override
			    public void paintComponent(Graphics g) {
			    	// System.out.println(getSize().width+" "+getSize().height);
			    	Color color=Color.decode(this.getText());
			    	super.paintComponent(g);
			    	g.setColor(color);
			        g.fillRect(3, 3, getSize().width - 6, getSize().height - 6);
			        // System.out.println(getSize().width+" "+getSize().height);
			        if(this.isSelected() || this.getModel().isRollover()){
			        	Graphics2D g2= (Graphics2D) g;
						g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
						g2.setStroke(new BasicStroke(5));
			        	if(this.isSelected() ){
			        		g.setColor(color.darker().darker());
			        	}else{
			        		g.setColor(color.darker());
			        	}
				        g.drawRect(5, 5, getSize().width - 10, getSize().height - 10);
			    	}
				}
			};
			btn.addMouseListener(new MouseAdapter() {
	           @Override
	           public void mousePressed(MouseEvent e) {
	              if (SwingUtilities.isRightMouseButton(e)) {
	                 System.out.println("Right Button Pressed");
	                 Color color=Color.decode(btn.getText());
				        Color background = JColorChooser.showDialog(
				          (JFrame) SwingUtilities.getRoot(e.getComponent()), "JColorChooser Sample", color);
				        if (background != null) {
				          color=background;
				          String hex = "#"+Integer.toHexString(color.getRGB()).substring(2);
				          btn.setText(hex);
				          if(btn.isSelected()==true){
				          	// canv.c=hex;
				          	canv.color=color;
				          	System.out.println("hi");
				          }
				      }
	              }
	           }
	        });
	        return btn;
		}
		void notifyCanv(String s){
			// canv.c=s;
			canv.color=Color.decode(s);
			if(canv.action.equals("Move")&&canv.curShape!=null){
				canv.curShape.color=Color.decode(s);
				canv.repaint();
			}
		}
	}
	class Lines extends Tools{
		Lines(int col,int selIndex,String actions[]){
			super(col,selIndex,actions);
		}
		JRadioButton makeRB(String s){
			return new JRadioButton(){
				public void paintComponent(Graphics g) {
					// System.out.println(getSize().width+" "+getSize().height);
			    	super.paintComponent(g);
			    	g.setColor(Color.WHITE);
			    	if(this.isSelected()==true){
			    		g.setColor(Color.decode("#dadada"));
			    	}else if(this.getModel().isRollover()==true){
			    		g.setColor(Color.decode("#ededed"));
			    	}
			    	g.fillRect(0, 0, getSize().width , getSize().height );

			    	Graphics2D g2= (Graphics2D) g;
			    	g2.setStroke(new BasicStroke(Integer.parseInt(s)));
			    	g.setColor(Color.BLACK);
			    	g.drawLine(10,28,102,28);
			    	
				}
			};
		}
		void notifyCanv(String s){
			canv.thickness=Integer.parseInt(s);
			if(canv.action.equals("Move")&&canv.curShape!=null){
				canv.curShape.thickness=Integer.parseInt(s);
				canv.repaint();
			}
		}
	}
	Canvas canv;
	Tools t=new Tools(2,2,new String[]{"Move","Erase","Rect","Oval","Line","Fill"});
	Colors c=new Colors(2,0,new String[]{"#f4511e","#ffb300","#7cb342","#00897b","#1e88e5","#3949ab"});
	Lines l=new Lines(1,0,new String[]{"1","3","5"});
	ToolBar(Canvas canv){
		this.canv=canv;
		setLayout (new BoxLayout (this, BoxLayout.Y_AXIS));
		add(t);
		add(c);
		add(l);
	}
	void matchStyleToShape(Color color,int width){
		c.setSelected("#"+Integer.toHexString(color.getRGB()).substring(2));
		l.setSelected(Integer.toString(width));
	}
}


