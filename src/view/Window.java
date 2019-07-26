package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.TransferHandler;

import controller.GameController;
import model.Board;
import model.Game;
import model.Player;

/**
 * The class is used to implement the user interface
 *
 */
public class Window {
	
	JFrame gameFrame; 
	GameController gameController;
	JButton[][] lblAttackGrid;
	JLabel[][] lblShipPlacementGrid;
	JLabel turnLabel;
	JLabel resultLabel;
	JLabel gameStatus;
	
	JLabel placeShip1;
	JLabel placeShip2;
	JLabel placeShip3;
	JLabel placeShip4;
	JLabel placeShip5;
	JLabel toMoveShip;
	
	/**
	 * Function to create the file's new game menu item
	 * @return The menu item
	 */
	public JMenuItem createNewGameMenuItem() {
		JMenuItem newGame = new JMenuItem("New Game");
		
		newGame.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String player1 = JOptionPane.showInputDialog("Please input name for player 1: ");
				
				/*
				String positions1 = JOptionPane.showInputDialog(""
						+ "Please input placement positions for\n"
						+ "Carrier(5),Battleship(4),Cruiser(3),Submarine(3),Destroyer(2) respectively\n"
						+ "Comma Separated Values\n"
						+ "Example: (A1,A5),(B2,B5),(C3,C5),(D4,D6),(E5,E6): ");
				
				while(Game.checkPositions(positions1) == false) {
					positions1 = JOptionPane.showInputDialog("Incorrect positions. Try again: ");
				}
				*/
				
				String positions1 = "";
				
				gameController = new GameController();
				gameController.createGame(player1, positions1);
				
				createBoardDisplay(gameFrame);
				
				turnLabel = new JLabel("Current Turn: " + gameController.getCurrPlayer().getName());
				turnLabel.setBounds(0, 600, 300, 50);
				gameFrame.add(turnLabel);
				
				resultLabel = new JLabel("");
				resultLabel.setBounds(0, 680, 300, 50);
				gameFrame.add(resultLabel);
				
				gameStatus = new JLabel("Game in progress");
				gameStatus.setBounds(0, 750, 300, 50);
				gameFrame.add(gameStatus);
				
				gameFrame.revalidate();
				
			}
		});
		
		return newGame;
	}
	
	/**
	 * Function to create file menu
	 * @return The File menu
	 */
	public JMenu createFileMenu() {
		JMenu file = new JMenu("File");
		file.add(createNewGameMenuItem());
		return file;
	}
	
	/**
	 * Function to create menu bar
	 * @return The menu bar
	 */
	public JMenuBar createMenu() {
		JMenuBar menuBar = new JMenuBar();
		menuBar.add(createFileMenu());
		return menuBar;
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
					lblShipPlacementGrid[i][j].setText("SHIP");
				}
				else if(shipPlacementGrid[i][j] == Board.PLACEMENT_BOARD_SHIP_HIT) {
					lblShipPlacementGrid[i][j].setText("*HIT");
				}
				else {
					lblShipPlacementGrid[i][j].setText("----");
				}
			}
		}
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

	/**
	 * Function to create the board display
	 * @param f The game frame
	 */
	public void createBoardDisplay(JFrame f) {
		lblAttackGrid = new JButton[Board.BOARD_ROWS][Board.BOARD_COLS];
		lblShipPlacementGrid = new JLabel[Board.BOARD_ROWS][Board.BOARD_COLS];
		
		for(int i = 0; i < Board.BOARD_ROWS + 1; i++) {
			for(int j = 0; j < Board.BOARD_COLS + 1; j++) {
				if(i == Board.BOARD_ROWS || j == Board.BOARD_COLS) {
					JLabel lbl = new JLabel("");
					lbl.setBounds((70 * i), (70 * j), 200, 50);
					
					JLabel lbl2 = new JLabel("");
					lbl2.setBounds(600 + (70 * i), (70 * j), 200, 50);
					
					f.add(lbl);
					f.add(lbl2);
				}
				else {
					JButton btn = new JButton(("" + (char)(65 + i)) + (j + 1));
					lblAttackGrid[i][j] = btn;
					lblAttackGrid[i][j].setBounds(800 + (70 * i), (70 * j), 70, 70);

					lblAttackGrid[i][j].addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							String cmd = e.getActionCommand();
							int row = cmd.charAt(0) - 65;
							int col = Integer.parseInt(cmd.substring(1)) - 1;
							String result = processCommand(row, col);
							if(result.equals(Player.ATTACK_HIT)) {
								lblAttackGrid[row][col].setText("*HIT");
							}
							else {
								lblAttackGrid[row][col].setText("MISS");
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
					
					JLabel lbl2 = new JLabel("-----");
					lblShipPlacementGrid[i][j] = lbl2;
					lblShipPlacementGrid[i][j].setBounds((70 * i), (70 * j), 200, 50);
					
					f.add(lblAttackGrid[i][j]);
					f.add(lblShipPlacementGrid[i][j]);
				}
			}
		}
		
		placeShip1 = new JLabel("SHIP#SHIP#SHIP#SHIP#SHIP");
		placeShip2 = new JLabel("SHIP#SHIP#SHIP#SHIP");
		placeShip3 = new JLabel("SHIP#SHIP#SHIP");
		placeShip4 = new JLabel("SHIP#SHIP#SHIP");
		placeShip5 = new JLabel("SHIP#SHIP");
		
		placeShip1.addMouseListener(lblDnd);
		placeShip2.addMouseListener(lblDnd);
		placeShip3.addMouseListener(lblDnd);
		placeShip4.addMouseListener(lblDnd);
		placeShip5.addMouseListener(lblDnd);
		
		placeShip1.setBounds(400, 670, 200, 10);
		placeShip2.setBounds(400, 690, 200, 10);
		placeShip3.setBounds(400, 710, 200, 10);
		placeShip4.setBounds(400, 730, 200, 10);
		placeShip5.setBounds(400, 750, 200, 10);
		
		f.add(placeShip1);
		f.add(placeShip2);
		f.add(placeShip3);
		f.add(placeShip4);
		f.add(placeShip5);
		
		refreshBoard();
	}

	/**
	 * Function to initialize java frame
	 */
	public void initMainFrame() {
		JFrame.setDefaultLookAndFeelDecorated(true);
	    gameFrame = new JFrame("BattleShip");
	    gameFrame.setLayout(null);
	    gameFrame.setSize(1600,900);
	    gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    gameFrame.setJMenuBar(createMenu());
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
	 * @param args The command line argumeents
	 */
	public static void main(String[] args) {
		Window win = new Window();
		win.start();
	}

	MouseListener lblDnd = new MouseListener() {
		@Override
		public void mouseReleased(MouseEvent e) {
			//toMoveShip.setBounds(e.getXOnScreen() - 5, e.getYOnScreen() - 60, 200, 10);
			
			int minx = 0;
			int miny = 0;
			
			int targetx = 0;
			int targety = 0;
			
			for(int i = 0; i < Board.BOARD_ROWS; i++) {
				for(int j = 0; j < Board.BOARD_COLS; j++) {
					JLabel tmpLbl = lblShipPlacementGrid[i][j];
					int diffx = Math.abs(tmpLbl.getX() - e.getXOnScreen());
					int diffy = Math.abs(tmpLbl.getY() - e.getYOnScreen() + 60);
					
					if(minx == 0 && miny == 0) {
						minx = diffx;
						miny = diffy;
						targetx = tmpLbl.getX();
						targety = tmpLbl.getY();
					}
					else if(minx > diffx || miny > diffy) {
						minx = diffx;
						miny = diffy;
						targetx = tmpLbl.getX();
						targety = tmpLbl.getY();
					}
				}
			}
			
			for(int i = 0; i < Board.BOARD_ROWS; i++) {
				for(int j = 0; j < Board.BOARD_COLS; j++) {
					
					JLabel tmpLbl = lblShipPlacementGrid[i][j];
					
					if(tmpLbl.getX() == targetx && tmpLbl.getY() == targety) {
						String[] numShips = toMoveShip.getText().split("#");
						if(numShips.length == 5) {
							if(i > (Board.BOARD_ROWS - 5)) {
								JOptionPane.showMessageDialog(null, "Not enough space to put the selected ship. Please try again");
							}
							else {
								boolean place = true;
								for(int k = i; k < (i + 5); k++) {
									if(lblShipPlacementGrid[k][j].getText().equals("SHIP")) {
										JOptionPane.showMessageDialog(null, "Area occupied by another ship. Please try again");
										place = false;
										break;
									}
								}
								if(place == true) {
									for(int k = i; k < (i + 5); k++) {
										lblShipPlacementGrid[k][j].setText("SHIP");
									}
								}
							}
						}
						else if(numShips.length == 4) {
							if(i > (Board.BOARD_ROWS - 4)) {
								JOptionPane.showMessageDialog(null, "Not enough space to put the selected ship. Please try again");
							}
							else {
								boolean place = true;
								for(int k = i; k < (i + 4); k++) {
									if(lblShipPlacementGrid[k][j].getText().equals("SHIP")) {
										JOptionPane.showMessageDialog(null, "Area occupied by another ship. Please try again");
										place = false;
										break;
									}
								}
								if(place == true) {
									for(int k = i; k < (i + 4); k++) {
										lblShipPlacementGrid[k][j].setText("SHIP");
									}
								}
							}
						}
						else if(numShips.length == 3) {
							if(i > (Board.BOARD_ROWS - 3)) {
								JOptionPane.showMessageDialog(null, "Not enough space to put the selected ship. Please try again");
							}
							else {
								boolean place = true;
								for(int k = i; k < (i + 3); k++) {
									if(lblShipPlacementGrid[k][j].getText().equals("SHIP")) {
										JOptionPane.showMessageDialog(null, "Area occupied by another ship. Please try again");
										place = false;
										break;
									}
								}
								if(place == true) {
									for(int k = i; k < (i + 3); k++) {
										lblShipPlacementGrid[k][j].setText("SHIP");
									}
								}
							}
						}
						else if(numShips.length == 2) {
							if(i > (Board.BOARD_ROWS - 2)) {
								JOptionPane.showMessageDialog(null, "Not enough space to put the selected ship. Please try again");
							}
							else {
								boolean place = true;
								for(int k = i; k < (i + 2); k++) {
									if(lblShipPlacementGrid[k][j].getText().equals("SHIP")) {
										JOptionPane.showMessageDialog(null, "Area occupied by another ship. Please try again");
										place = false;
										break;
									}
								}
								if(place == true) {
									for(int k = i; k < (i + 2); k++) {
										lblShipPlacementGrid[k][j].setText("SHIP");
									}
								}
							}
						}
					}
				}
			}
			
			toMoveShip.setBounds(targetx, targety, 200, 10);
			toMoveShip = null;
		}
		
		@Override
		public void mousePressed(MouseEvent e) {
			toMoveShip = (JLabel)e.getSource();
		}
		
		@Override public void mouseExited(MouseEvent e) {}
		
		@Override public void mouseEntered(MouseEvent e) {}
		
		@Override public void mouseClicked(MouseEvent e) {}
	};
	
	
}
