package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.Rectangle;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import controller.GameController;
//import javafx.scene.shape.Rectangle;
import model.Board;
import model.Game;
import model.Player;
import model.Ship;

/**
 * The class is used to implement the user interface
 */
public class Window extends JPanel implements MouseListener, MouseMotionListener {
	
	static JFrame gameFrame;
	static JFrame mainFrame;
	static JButton basicGame;
	static JButton salvaGame;
	static JLabel label;
	
	static GameController gameController;
	static JButton[][] lblAttackGrid;
	static JButton[][] lblShipPlacementGrid;
	
	static JLabel turnLabel;
	static JLabel resultLabel;
	static JLabel gameStatus;
	
	private static Ship[] ships = new Ship[5];
	private static Rectangle[] rect = new Rectangle[5];
	public boolean[] onState = new boolean[5];
	
//	public Window() {
//		// TODO Auto-generated constructor stub
//		addMouseListener(this);
//		addMouseMotionListener(this);
//		
//		
//	}
	/**
	 * Function to create the file's new game menu item
	 * @return The menu item
	 */
	public void createNewGameMenuItem() {
		
		basicGame.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String player1 = JOptionPane.showInputDialog("Please input name for player 1: ");
				if(player1.isEmpty())
					player1 = "Human";
				System.out.println("calling create board");
				createBoardDisplay(gameFrame);		
				//gameFrame.getContentPane().add(new Window());
				gameFrame.revalidate();
				
			}
		});
	}
	
	/**
	 * Function to refresh the board display
	 */
	public void refreshBoard() {
		Board board = gameController.getGame().getGameBoard();
		int[][] shipPlacementGrid = board.getShipPlacementGrid();
		
		for(int i = 0; i < Board.BOARD_ROWS; i++) {
			for(int j = 0; j < Board.BOARD_COLS; j++) {
				if(shipPlacementGrid[i][j] == Board.PLACEMENT_BOARD_SHIP) {
					lblShipPlacementGrid[i][j].setBackground(Color.GRAY);      //ship placed color
				}
				else if(shipPlacementGrid[i][j] == Board.PLACEMENT_BOARD_SHIP_HIT) {
					lblShipPlacementGrid[i][j].setBackground(Color.ORANGE);    //hit
				}
//				else {
//					lblShipPlacementGrid[i][j].setBackground(Color.WHITE);     //nothing
//				}
			}
		}
		repaint();
	}
	
	/**
	 * Function to process the attack command
	 * @param row The row to attack
	 * @param col The column to attack
	 * @return The attack result
	 */
	public String processCommand(int row, int col) {
		String result = gameController.processAttack(row, col);
		refreshBoard();
		return result;
	}

	 public void paintComponent(Graphics g) {
		super.paintComponent(g);
		System.out.println("ïn paint");
		//createBoardDisplay(gameFrame);
		 
		for(int j = 0;j<5;j++) {
			 if(onState[j]) {
				 g.setColor(Color.GREEN);
			 }
			 else 
				 g.setColor(Color.GRAY);
			// Draws background
				g.fillRect(rect[j].x,rect[j].y,rect[j].width,rect[j].height);	
		}
		
	 } 
	
	/**
	 * Function to create the board display
	 * @param f The game frame
	 */
	public void createBoardDisplay(JFrame f) {
		System.out.println("create Board display");
		lblAttackGrid = new JButton[Board.BOARD_ROWS][Board.BOARD_COLS];
		lblShipPlacementGrid = new JButton[Board.BOARD_ROWS][Board.BOARD_COLS];

		f = new JFrame();
		f.setBounds(0, 0, 1210, 700);
		

		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		for(int i = 0; i < Board.BOARD_ROWS + 1; i++) {
			for(int j = 0; j < Board.BOARD_COLS + 1; j++) {
				if(i == Board.BOARD_ROWS || j == Board.BOARD_COLS) {
					JLabel lbl = new JLabel("");
					lbl.setBounds((40 * i), (40 * j), 35, 35);
					
					JLabel lbl2 = new JLabel("");
					lbl2.setBounds(600 + (40 * i), (40 * j), 35, 35);
					
					f.add(lbl);
					f.add(lbl2);
				}
				else {
					JButton btn = new JButton(("" + (char)(65 + i)) + (j + 1));
					//btn.setFont(new Font("Arial", Font.PLAIN, 6));
					btn.setForeground(Color.BLACK);
					lblAttackGrid[i][j] = btn;
					lblAttackGrid[i][j].setBounds(700 + (40 * i), (40 * j), 40, 40);
					lblAttackGrid[i][j].setBackground(Color.BLACK);       //board bg color

					lblAttackGrid[i][j].addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							String cmd = e.getActionCommand();
							int row = cmd.charAt(0) - 65;
							int col = Integer.parseInt(cmd.substring(1)) - 1;
							String result = processCommand(row, col);
							if(result.equals(Player.ATTACK_HIT)) {
								lblAttackGrid[row][col].setBackground(Color.ORANGE);     //hit
							}
							else {
								lblAttackGrid[row][col].setBackground(Color.WHITE);      //miss
							}
							
							lblAttackGrid[row][col].setEnabled(false);
							result = "Prev Turn Result: " + result;
							resultLabel.setText(result);
							if(Game.checkIfGameWon() == true) {
								gameStatus.setText("Game Over. Winner is " + Game.getWinner());
								JOptionPane.showMessageDialog(null, Game.getWinner() + " won the game.", 
										"Game Over", JOptionPane.INFORMATION_MESSAGE);
								System.exit(0);
							}
						}
					});
					//("" + (char)(65 + i)) + (j + 1)
					JButton btn1 = new JButton(("" + (char)(65 + i)) + (j + 1));
					lblShipPlacementGrid[i][j] = btn1;
					btn1.setForeground(Color.BLACK);
					lblShipPlacementGrid[i][j].setBounds((40 * i), (40 * j), 40, 40);
					lblShipPlacementGrid[i][j].setBackground(Color.BLACK);
//					lblShipPlacementGrid[i][j] = lbl2;
//					lblShipPlacementGrid[i][j].setBounds((40 * i), (40 * j), 200, 50);
					
					f.add(lblAttackGrid[i][j]);
					f.add(lblShipPlacementGrid[i][j]);
					f.remove(label);
					f.remove(basicGame);
					f.remove(salvaGame);
				}
			}
		}
		
		turnLabel = new JLabel("Current Turn: " );
		turnLabel.setBounds(0, 400, 250, 10);
		f.add(turnLabel);
		
		resultLabel = new JLabel("");
		resultLabel.setBounds(0, 420, 250, 15);
		f.add(resultLabel);
		
		gameStatus = new JLabel("Game in progress");
		gameStatus.setBounds(0, 440, 250, 15);
		f.add(gameStatus);
		
		for(int i=0;i<5;i++) {
			ships[i] = new Ship();
			ships[i].addPosition(400, 50+40*i);
			if(i<2) {
				ships[i].setLength(i+2);
			}
			else {
				ships[i].setLength(i+1);
			}
		}
		
		
		for(int i=0;i<5;i++) {
			if(ships[i].isHorizontal) {
				rect[i] = new Rectangle(460, (40 * i), 40 * ships[i].getLength(), 35);
			}
			else {
				rect[i] = new Rectangle(40 * i, (40 * i), 40, 40 * ships[i].getLength());
			}
			onState[i] = false;
		}
		
		f.getContentPane().add(new Window());
//		addMouseListener(this);
//		addMouseMotionListener(this);
		f.setVisible(true);
		f.revalidate();
		f.repaint();
//		refreshBoard();
	}

	/**
	 * Function to initialize java frame
	 */
	public void initMainFrame() {
		JFrame.setDefaultLookAndFeelDecorated(true);
	    gameFrame = new JFrame("BattleShip");
	    gameFrame.setLayout(null);
	    gameFrame.setSize(1350,600);
	    gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    //gameFrame.setJMenuBar(createMenu());
	    
	    label = new JLabel("BATTLESHIP WAR");
	    label.setFont(new Font("ARIAL",Font.BOLD, 50));
	    label.setBounds(470, 50, 500, 50);
	    label.setForeground(Color.DARK_GRAY);
	    gameFrame.add(label);
	    
	    basicGame = new JButton("BASIC GAME");
	    basicGame.setFont(new Font("ARIAL",Font.BOLD, 25));
	    basicGame.setBounds(400, 250, 200, 30);
	    gameFrame.add(basicGame);
	    
	    salvaGame = new JButton("ADVANCED GAME");
	    salvaGame.setFont(new Font("ARIAL",Font.BOLD, 25));
	    salvaGame.setBounds(650, 250, 300, 30);
	    gameFrame.add(salvaGame);
	    
	    //w = new Window();
	    
	    createNewGameMenuItem();
	    //repaint();
	    gameFrame.setVisible(true);
	}
	
	/**
	 * Function to start everything
	 */
	public void start() {
		initMainFrame();
	}
	
	/**
	 * The main function called by OS
	 * @param args The command line arguments
	 */
	public static void main(String[] args) {
		Window win = new Window();
		win.start();
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		System.out.println("Mouse dragged");
		// TODO Auto-generated method stub
		int x, y;

		x = e.getX();
		y = e.getY();

		// Allows the user to move their ships
//		if (turn == id) {
			for (int i = 0; i < 5; i++) {
				if (onState[i]) {
					rect[i].x = x;
					rect[i].y = y;
//					ships[i].x = x;
//					ships[i].y = y;
				}
			}
			repaint();
//		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		System.out.println("mouse moved");
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println("mouse entered");
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println("Mouse entered");
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println("Mouse exited");
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		System.out.println("Mouse pressed...");
		int x, y;

		x = e.getX();
		y = e.getY();

		// Sets the clickedOnState of a ship true when it is pressed
//		if (turn == id) {
			for (int i = 0; i < 5; i++) {
				if (rect[i].contains(x, y)) {
					onState[i] = true;
				}

//			}
		}

		repaint();
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
