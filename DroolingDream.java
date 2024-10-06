import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

class MenuPanel extends JPanel{

	JButton BtnPlay = new JButton("Play Game");
	JButton Btnhelp = new JButton("Game Help");
	JButton Btnexit = new JButton("Exit  Game");
	
	Image MainImage = new ImageIcon("resources/StartMenuBkg.jpg").getImage();  
	
	JPanel MenuPanel = new JPanel(); 
	
	//Main Menu Panel
	MenuPanel(){
		
		MenuPanel.setLayout(new BoxLayout(MenuPanel,BoxLayout.Y_AXIS)); 
		MenuPanel.setPreferredSize(new Dimension(100, 78));
		add(MenuPanel); 		

		BtnPlay.setBounds(60, 400, 100, 30);
		Btnhelp.setBounds(60, 500, 100, 30);
		Btnexit.setBounds(60, 600, 100, 30);

		MenuPanel.add(BtnPlay);
		MenuPanel.add(Btnhelp);
		MenuPanel.add(Btnexit);
				
		
		BtnPlay.addMouseListener(new MClick());
		Btnhelp.addMouseListener(new MClick());
		Btnexit.addMouseListener(new MClick());
		
		
	}
	//Defined class for key press
	class Keychecker extends KeyAdapter {

		@Override
		public void keyPressed(KeyEvent event) {
	
			//char ch = event.getKeyChar();
			int keycode = event.getKeyCode();        
			 
			System.out.println(keycode);
	
		}
	}
	//Defined class for mouse click
	class MClick extends MouseAdapter{ 
	
		public void mouseClicked(MouseEvent Mevent){
			if(Mevent.getSource()== BtnPlay){
				DroolingDream.CardLt.show(DroolingDream.JpMain, "GamePanel"); //show gamePanel when play is clicked
			}
			if(Mevent.getSource()== Btnhelp){
				DroolingDream.CardLt.show(DroolingDream.JpMain, "HelpPanel"); //show helpPanel when help is clicked
			}	
			if(Mevent.getSource()== Btnexit){
				System.exit(0);  //exit application when exit is clicked
			}
			
			
		}
	}
	
	//Paint component
	public void paintComponent(Graphics graph){
		super.paintComponent(graph);        
		Graphics2D g2d = (Graphics2D)graph; 
		setFocusable(true);		
		
		g2d.drawImage(MainImage, 0,0, null); 
		repaint();
	}
}//Menu Panel

//Help Panel
class HelpPanel extends JPanel{

	Image helpbkg = new ImageIcon("resources/HelpMenu.jpg").getImage(); 
	JButton BacktoMain = new JButton("Main Menu"); 
	
	HelpPanel(){
		setFocusable(true); //setting the focus
		add(BacktoMain);			//back to main menu
		
		BacktoMain.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent me){
						DroolingDream.CardLt.show(DroolingDream.JpMain, "MenuPanel"); // show menuPanel when back button is clicked
			}	
		  });		

	}//end constructor
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D)g;
		g2d.drawImage(helpbkg, 0,0, null); // draw help background
		repaint();
	}//end paintComponent
}//end Help panel


//Game Panel
class GamePanel extends JPanel{ 
	
	Image Gamebkg = new ImageIcon("resources/GameBkg.jpg").getImage();
	Image Astronaut  = new ImageIcon("resources/Astronaut.png").getImage();
	Image FoodPizza     = new ImageIcon("resources/Pizza.jpg").getImage();
	Image FoodHamburger = new ImageIcon("resources/Hamburger.jpg").getImage();
	Image FoodIcecream  = new ImageIcon("resources/Icecream.png").getImage();
	Image FoodPepsi     = new ImageIcon("resources/Pepsi.jpg").getImage();
	Image gameOverbkg= new ImageIcon("resources/EndGameBkg.png").getImage();
	Image Tempbkg; //temporary background
	
	//Image Astronaut's Coordinates
	int Astronautx, Astronauty;
	//Food Coordinates
	int Foodx,Foody; // 
	Random rand = new Random(); // for randomizing cooridate
	
	JLabel time;
	JLabel points;
	
	int pointsCount = 0;
	int timeleft = 59;
	int counter  = 0;
	
	boolean gameOver = false;
	int FoodRandomNum = 1;
	
	GamePanel(){
		
		setLayout(null);
		setFocusable(true);
		Tempbkg = Gamebkg;	

		System.out.println("test#10");
		System.out.println(FoodRandomNum);
		
		Astronautx = 450; Astronauty = 500;
		Foodx = (int)rand.nextInt(1000);
		Foody = 0;
		
	    time = new JLabel("Time: 59");
		time.setBounds(20, 10, 50, 20); //setting the time label on screen
	    
	    
	    points = new JLabel("Points: 0");
		points.setBounds(100,10,100,20);

		/*adding both components in jpanel*/
		add(time);
		add(points);
		
		addKeyListener(new KeyAdapter(){
			public void keyPressed(KeyEvent ke){
				
				if((ke.getKeyCode() == ke.VK_LEFT || ke.getKeyCode() == ke.VK_A) & Astronautx>10){
					Astronautx-=50;
					repaint(); // redraw at new position
				}
				if((ke.getKeyCode() == ke.VK_RIGHT || ke.getKeyCode() == ke.VK_D) & Astronautx<1000){
					Astronautx+=50; // redraw at new position
					repaint();
				}
			}//end keypressed
		});	
	}//end constructor
	
	void FallFoods(){
		if(Foody >=650){
			Foody = 0;
			Foodx = rand.nextInt(1000);

			if (FoodRandomNum >=1 && FoodRandomNum < 4 ) {
				FoodRandomNum ++;
			}
			else if (FoodRandomNum > 4){
				FoodRandomNum = 1;
			};
		}
		else
		Foody++;
	}
	
	void updateTime(){
		counter++;
		if(counter == 100) //we count to 60 and then dec timeleft by 1 for slowing speed
		{
		   timeleft--;  //dec time left after 60 counts
		   counter = 0; //reset counter
		}
		time.setText("Time:"+timeleft);
	}
	
	void detectCollision(){
		Rectangle AstronautRect = new Rectangle(Astronautx,Astronauty,100,65); //making a rectangle on Astronaut
		Rectangle FoodRect    = new Rectangle(Foodx,Foody,45,67); //making a rectangle on Food
		
		if(FoodRect.intersects(AstronautRect)){
			pointsCount+=5; // give 5 points on each catch
			points.setText("Points:"+pointsCount); // set the count
			Foody = 0; // for next food
			Foodx = rand.nextInt(1000); // again randomizing x axis of food
		}
	}//end collision detection
	
	void checkGameOver(){
		if(timeleft <= 0)
		{
			JLabel yourScore = new JLabel("Your SCORE :" + pointsCount);
			Tempbkg = gameOverbkg;
			yourScore.setBounds(400, 400, 200, 100);
			gameOver = true;
			yourScore.setForeground(Color.red);
			yourScore.setFont(new Font("Serif", Font.PLAIN, 14));			
			add(yourScore);

		}
	}

	public void paintComponent(Graphics g){	

		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D)g;
		g2d.drawImage(Tempbkg,0,0,null);

		checkGameOver();
		
		if(gameOver == false){
			setFocusable(true);
			grabFocus();
			updateTime();
			
			FallFoods();
			detectCollision();
		    
			System.out.println("test#20");
			System.out.println(FoodRandomNum);

			switch (FoodRandomNum) {
				case 1:
					g2d.drawImage(FoodHamburger, Foodx, Foody,null);
					break;
				case 2:
					g2d.drawImage(FoodIcecream, Foodx, Foody,null);
					break;
				case 3:
					g2d.drawImage(FoodPepsi, Foodx, Foody,null);
					break;
				case 4:
					g2d.drawImage(FoodPizza, Foodx, Foody,null);
					break;
				default:
					break;
			};


			g2d.drawImage(Astronaut, Astronautx, Astronauty, null); 
		}
		else if (gameOver == true){			
			//Delay 5 seoncds and back to main menu
			
			try {
				Thread.sleep(5000);
			  } catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			  }
			DroolingDream.CardLt.show(DroolingDream.JpMain, "MenuPanel"); // show menuPanel when game over
			
		};
		
		repaint();	
	}//end paintComponent
}//end game panel

public class DroolingDream extends JFrame{
	
	static MenuPanel MuPanel = new MenuPanel();
	static HelpPanel HpPanel = new HelpPanel();
	static GamePanel GmPanel = new GamePanel();
	
	static CardLayout CardLt = new CardLayout();
	static JPanel JpMain = new JPanel(); // to contain the panels as cards
	
	DroolingDream(){
		
		JpMain.setLayout(CardLt);// setting the layout to cardlayout
		JpMain.add(MuPanel, "MenuPanel");
		JpMain.add(HpPanel, "HelpPanel");
		JpMain.add(GmPanel, "GamePanel");
		CardLt.show(JpMain, "MenuPanel");
		add(JpMain); //adding the panel with cardlayout in JFrame
		
		setTitle("Drooling Dream");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1024, 650); //frame size
		setVisible(true);   //frame visibility	
		setResizable(false);
		setLocationRelativeTo(null);
	}//end constructor
	
	public static void main(String args[]){
		new DroolingDream();//Init
	}//end main
}//end class

