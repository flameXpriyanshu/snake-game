import java.awt.*;
import java.awt.event.*;                        
import javax.swing.*;                            //jpanel UI//
import java.util.Random;

import javax.swing.JPanel;


public class GamePanel extends JPanel implements ActionListener{
	
	static final int SCREEN_WIDTH=600;
	static final int SCREEN_HEIGHT=600;
	static final int UNIT_SIZE=25;               //how big object we want in the game//
	static final int GAME_UNITS=(SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE;  // Cal.. no. of object can happen//
	static final int DELAY =85;                  //higher the delay no. slower will be the game//
	final int x[]=new int[GAME_UNITS];            
	final int y[]=new int[GAME_UNITS];           //contain snake body parts//
	int bodyParts=6;
	int applesEaten;
	int appleX;                                  //coordinate of x where apple located//
	int appleY;                                  //" " for y//
	char direction ='R';                         //moving toward right,  L..U..D//
	boolean running =false;
	Timer timer;
	Random random;
	
    GamePanel()                                  //working on layout of the screen//
    {
    	random=new Random();
    	this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));  
    	this.setBackground(Color.black);         //background//
    	this.setFocusable(true);
    	this.addKeyListener(new MyKeyAdapter());
    	startGame();
    }
	public void startGame(){
		newApple();
		running=true;
		timer=new Timer(DELAY,this);
		timer.start();
	}
	
	public void paintComponent(Graphics g){     //visible lining(UI coloring without interfering rest of it)//
	    super.paintComponent(g);
	    draw(g);
	}
	
	public void draw(Graphics g){
	  if(running){
		  
		/*for(int i=0;i<SCREEN_HEIGHT/UNIT_SIZE;i++)   //for draw line accross game panel to make sort of grid//
		{                                                         //x1,y1,x2,y2//
			g.drawLine(i*UNIT_SIZE,0,i*UNIT_SIZE,SCREEN_HEIGHT);  //for vertical lining//
			g.drawLine(0,i*UNIT_SIZE,SCREEN_WIDTH,i*UNIT_SIZE);   //for horizontal lining//
		}  */
		  
		  
		g.setColor(Color.red);                                //Apple Color
		g.fillOval(appleX,appleY, UNIT_SIZE+10,UNIT_SIZE+10);       //Apple Shape
		
		for(int i=0;i<bodyParts;i++)                          //Snake size and color
		{
			if(i==0)
			{
				g.setColor(Color.green);
				g.fillRect(x[i],y[i],UNIT_SIZE,UNIT_SIZE);
			}
			else
			{
				g.setColor(new Color(45,180,0));
				g.fillRect(x[i],y[i],UNIT_SIZE,UNIT_SIZE);
			}
		}
		g.setColor(Color.red);
		g.setFont(new Font("Ink Free",Font.BOLD,20));
		FontMetrics metrics=getFontMetrics(g.getFont());     //intance of fontmetrices
		g.drawString("Score: "+applesEaten,(SCREEN_WIDTH-metrics.stringWidth("Score: "+applesEaten))/1,g.getFont().getSize());
	  }
	  else
	  {
		  gameOver(g);
	  }
	}
	
	public void newApple(){
		appleX=random.nextInt((int)SCREEN_WIDTH/UNIT_SIZE)*UNIT_SIZE;
		appleY=random.nextInt((int)SCREEN_HEIGHT/UNIT_SIZE)*UNIT_SIZE;
	}
	
	public void move(){
		for(int i=bodyParts;i>0;i--)
		{
			x[i]=x[i-1];
			y[i]=y[i-1];
		}
		
		switch(direction) {
		case 'U':
			y[0]=y[0]-UNIT_SIZE;
			break;
		case 'D':
			y[0]=y[0]+UNIT_SIZE;
			break;
		case 'L':
			x[0]=x[0]-UNIT_SIZE;
			break;
		case 'R':
			x[0]=x[0]+UNIT_SIZE;
			break;
		}	
	}
	
	public void checkApple(){
		if((x[0]==appleX)&&(y[0]==appleY))
		{
			bodyParts++;
			applesEaten++;
			newApple();
		}
	}
	
	public void checkCollisions(){
		for(int i=bodyParts;i>0;i--)          
		{
			if((x[0]==x[i])&&(y[0]==y[i]))    //check if head collide with body
				running=false;
		 
			if(x[0]<0)                        //check if head touches left border
				running=false;                
			
			if(x[0]>SCREEN_WIDTH)             //check if head touches right border
				running=false;
			
			
			if(y[0]<0)                        //check if head touches top border
			     running=false;
			
			if(y[0]>SCREEN_HEIGHT)            //check if head touches last border
			     running=false;
			
			if(!running) 
				timer.stop();
	    }   
	}
	
	public void gameOver(Graphics g){
		g.setColor(Color.red);          //SCORE
		g.setFont(new Font("Ink Free",Font.BOLD,40));
		FontMetrics metrics1=getFontMetrics(g.getFont());     //intance of fontmetrices
		g.drawString("Score: "+applesEaten,(SCREEN_WIDTH-metrics1.stringWidth("Score: "+applesEaten))/2,g.getFont().getSize());
		
		g.setColor(Color.red);          //GAMEOVER
		g.setFont(new Font("Ink Free",Font.BOLD,75));
		FontMetrics metrics2=getFontMetrics(g.getFont());     //intance of fontmetrices
		g.drawString("Game Over",(SCREEN_WIDTH-metrics2.stringWidth("Game Over"))/2,SCREEN_HEIGHT/2); //will put in the centre of the screen
		
	}
	
	public void actionPerformed(ActionEvent e) {
		if(running)
		{
			move();
			checkApple();
			checkCollisions();
		}
		repaint();
	}
	
    public class MyKeyAdapter extends KeyAdapter{      //*interclass*//
    
    	public void keyPressed(KeyEvent e) {
    		switch(e.getKeyCode())
    		{
    		case KeyEvent.VK_LEFT:
    		if(direction!='R')
    			direction='L';
    		break;
    		
    		case KeyEvent.VK_RIGHT:
    		if(direction!='L')
    			direction='R';
    		break;
    		
    		case KeyEvent.VK_UP:
    		if(direction!='D')
    			direction='U';
    		break;
    		
    		case KeyEvent.VK_DOWN:
    		if(direction!='U')
    			direction='D';
    		break;
    		}
    	}
    }
}
