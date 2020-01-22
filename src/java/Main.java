import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;  
import java.io.*;
import java.awt.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.lang.*;
public class Main extends JFrame{
	
	JPanel p=new JPanel();
	JPanel btnPane=new JPanel();
	// JPanel btnPane=new JPanel(new GridLayout(0,1));
	JButton clear;
	JButton save;
	JButton load;
	Color selectColor =Color.decode("#ededed");
	public static void main(String[] args){
		new Main();
	}
	public static JButton myButton(String text){
		JButton b = new JButton(text);
		b.setPreferredSize(new Dimension(100, 26));
		b.setFocusPainted(false);
		b.setBorderPainted(false);
		return b;
	}
	
	public Main(){
		UIManager.put("RadioButton.font", new Font("sans-serif", Font.PLAIN, 8));
		UIManager.put("RadioButton.background", Color.WHITE);
		UIManager.put("Button.foreground", Color.WHITE);
		UIManager.put("Button.background", Color.decode("#549eff"));
		UIManager.put("Button.select", Color.decode("#6292d3"));
		// UIManager.put("Panel.background", Color.decode("#549eff"));

		
		
		//ToolBar(Canvas canv, int col,int selIndex,String actions[],int type)
		// ToolBar t=new ToolBar(canv, 2,2,0,new String[]{"Move","Erase","Rect","Oval","Line","Fill"});
		// ToolBar c=new ToolBar(canv, 2,0,1,new String[]{"#F4511E","#FFB300","#7CB342","#00897B","#1E88E5","#3949AB"});
		// ToolBar l=new ToolBar(canv, 1,0,2,new String[]{"1","3","5"});
		Canvas canv = new Canvas();
		ToolBar p=new ToolBar(canv);
		canv.setToolBarReference(p);
		
		clear=myButton("New");
		save=myButton("Save");
		load=myButton("Load");

		
		// btnPane.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY));
		// p.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.BLACK));

		setSize(800,580);
		setResizable(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setMinimumSize(new Dimension(580, 580));

		p.setLayout (new BoxLayout (p, BoxLayout.Y_AXIS)); 
		btnPane.setBackground(selectColor);
		// p.setBackground(selectColor);
		canv.setBackground(Color.WHITE);

		btnPane.add(clear);
		btnPane.add(save);
		btnPane.add(load);
		

		// p.add(t);
		// p.add(c);
		// p.add(l);

		add(btnPane,BorderLayout.NORTH);
		add(p,BorderLayout.WEST);
		add(canv,BorderLayout.CENTER);
		setVisible(true);
		clear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                canv.shapes.clear();
                canv.repaint();
            }
        });
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	try{
            		//.getParent()
            		JFileChooser fileChooser = new JFileChooser(new File(System.getProperty("user.dir")));
					if (fileChooser.showSaveDialog(canv) == JFileChooser.APPROVE_OPTION) {
					 	 File file = fileChooser.getSelectedFile();
					 	 FileOutputStream fileOut = new FileOutputStream(file);
					  	ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
			            objectOut.writeObject(canv.shapes);
			            objectOut.close();
					  // save to file
					}
	            }catch(IOException ex){
	            	ex.printStackTrace();
				}

            }
        });
        load.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	try{
            		JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir"));
					if (fileChooser.showOpenDialog(canv) == JFileChooser.APPROVE_OPTION) {
						File file = fileChooser.getSelectedFile();
						FileInputStream fi = new FileInputStream(file);
						ObjectInputStream oi = new ObjectInputStream(fi);
						canv.shapes = (Deque<myShape>) oi.readObject();
						oi.close();
						canv.init=true;
						canv.repaint();
					}
				}catch(IOException|ClassNotFoundException ex){
					ex.printStackTrace();
				}


            }
        });
        canv.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "pressed");
        canv.getActionMap().put("pressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("esc");
                if(canv.curShape!=null){
                    canv.curShape.selected=false;
                    canv.curShape=null;
		        	canv.repaint();
	        	}
            }
        });
	}
}
