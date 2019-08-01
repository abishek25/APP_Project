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
 * This class represents the view of the game.
 */
public class Window {
	
	JFrame gameFrame; 
	public GameController gameController;
	public JButton[][] lblAttackGrid;
	public JLabel[][] lblShipPlacementGrid;
	JLabel turnLabel;
	JLabel resultLabel;
	JLabel scores;
	JLabel gameStatus;
	
	JLabel placeShip1;
	JLabel placeShip2;
	JLabel placeShip3;
	JLabel placeShip4;
	JLabel placeShip5;
	JLabel toMoveShip;
	
	int numShipsPlaced = 0;
	
	String tmpPlayerName;
	public Board board;
	public String[] playerShips;
	
	public static int DEV_TEST = 0;
	
	/**
	 * Function to create the file's new game menu item.
	 * @return newGame The menu item.
	 */
	public JMenuItem createNewGameMenuItem() {
		JMenuItem newGame = new JMenuItem("New Game");
		
		newGame.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String player1 = "TestPlayer";
				if(DEV_TEST != 1) {
					player1 = JOptionPane.showInputDialog("Please input name for player 1: ");
				}
				String strGameMode = "1";
				if(DEV_TEST != 1) {
					strGameMode = JOptionPane.showInputDialog("Please input game mode: (1 - Salva, Anything else for Regular)");
				}
				int gameMode = Game.GAME_TYPE_REGULAR;
				
				if(strGameMode.equals("1")) {
					gameMode = Game.GAME_TYPE_SALVA;
				}
				
				gameController = new GameController(gameMode);
				createBoardDisplay(gameFrame);
				tmpPlayerName = player1;
				gameFrame.revalidate();
			}
		});
		
		return newGame;
	}
	
	/**
	 * Function creates the game with placed ship and updates the board.
	 * @param board The game board
	 */
	public void createGameAndPlayer(Board board) {
		if(gameFrame == null) {
			return;
		}
		
		gameController.createGame(tmpPlayerName, board, playerShips);
		
		turnLabel = new JLabel("Current Turn: " + gameController.getCurrPlayer().getName());
		turnLabel.setBounds(0, 600, 300, 50);
		gameFrame.add(turnLabel);
		
		resultLabel = new JLabel("");
		resultLabel.setBounds(0, 680, 900, 50);
		gameFrame.add(resultLabel);
		
		scores = new JLabel("");
		scores.setBounds(0, 710, 900, 50);
		gameFrame.add(scores);
		
		gameStatus = new JLabel("Game in progress");
		gameStatus.setBounds(0, 750, 300, 50);
		gameFrame.add(gameStatus);
		
		gameFrame.revalidate();
		refreshBoard();
		JOptionPane.showMessageDialog(null, "Game Begins");
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
	 * Function to update the board display after any movement/attack.
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
	 * Function to process the attack command.
	 * @param row The row co-ordinate
	 * @param col The column co-ordinate
	 * @return The attack result
	 */
	public String processCommand(int row, int col) {
		String result = gameController.processAttack(row, col);
		refreshBoard();
		return result;
	}

	/**
	 * The function to generate and display the board.
	 * @param f The game frame
	 */
	public void createBoardDisplay(JFrame f) {
		board = new Board();
		
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
							System.out.println("Window received result: " + result);
							if(Game.gameMode == Game.GAME_TYPE_REGULAR) {
								if(result.equals(Player.ATTACK_HIT)) {
									lblAttackGrid[row][col].setText("*HIT");
								}
								else {
									lblAttackGrid[row][col].setText("MISS");
								}
							}
							else {
								if(result.equals("Turn in process")) {
									lblAttackGrid[row][col].setText("WAIT");
								}
								else {
									String resuts[] = result.split(" ");
									String salvaResult = "";
									
									for(int i = 0; i < resuts.length; i++) {
										String info[] = resuts[i].split("#");
										int trow = Integer.parseInt(info[0]);
										int tcol = Integer.parseInt(info[1]);
										if(info[2].equals("missed")) {
											lblAttackGrid[trow][tcol].setText("MISS");
											salvaResult += "missed ";
										}
										else {
											lblAttackGrid[trow][tcol].setText("*HIT");
											salvaResult += "success ";
										}
									}
									
									result = salvaResult;
								}
							}
							
							lblAttackGrid[row][col].setEnabled(false);
							result = "Prev Turn Result: " + result;
							resultLabel.setText(result);
							
							scores.setText(Game.gameScores);
							
							if(Game.checkIfGameWon() == true) {
								gameStatus.setText("Game Over. Winner is " + Game.getWinner());
								JOptionPane.showMessageDialog(null, 
										Game.getWinner() + " won the game. Scores: " + 
												tmpPlayerName + "(" + gameController.getP1Score() + ")" +
												" AND AI (" + gameController.getAIScore() + ")", 
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
		
		placeShip1.setBounds(1100, 670, 200, 10);
		placeShip2.setBounds(1100, 690, 200, 10);
		placeShip3.setBounds(1100, 710, 200, 10);
		placeShip4.setBounds(1100, 730, 200, 10);
		placeShip5.setBounds(1100, 750, 200, 10);
		
		f.add(placeShip1);
		f.add(placeShip2);
		f.add(placeShip3);
		f.add(placeShip4);
		f.add(placeShip5);
		
		if(DEV_TEST == 1) {
			autoShipPlacementForTesting();
		}
	}

	/**
	 * This function generates the java frame.
	 */
	public void initMainFrame() {
		JFrame.setDefaultLookAndFeelDecorated(true);
	    gameFrame = new JFrame("BattleShip");
	    gameFrame.setLayout(null);
	    gameFrame.setSize(1600,900);
	    gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    gameFrame.setJMenuBar(createMenu());
	    gameFrame.setVisible(true);
	    playerShips = new String[5];
	}
	
	/**
	 * Function to start everything
	 */
	public void start() {
		initMainFrame();
	}
	
	/**
	 * Function to check whether ship is placed inside the board or not. 
	 * @param rowNum The row co-ordinate from which ship will be placed
	 * @param maxRows Maximum number of rows of the board
	 * @param shipSpace The length of the ship
	 * @return false if outside the board size else true
	 */
	public boolean checkShipPlacementSpace(int rowNum, int maxRows, int shipSpace) {
		if(rowNum > (maxRows - shipSpace)) {
			return false;
		}
		return true;
	}
	
	/**
	 * Function checks whether there is already a ship placed on that row co-ordinates.
	 * @param i The row co-ordinate
	 * @param j The column co-ordinate
	 * @param shipSpace The length of the ship
	 * @param lblShipPlacementGrid The player board
	 * @return true if occupied else false.
	 */
	public boolean checkIfRowAreaOccupiedByAnotherShip(int i, int j, int shipSpace, JLabel[][] lblShipPlacementGrid) {
		for(int k = i; k < (i + shipSpace); k++) {
			if(lblShipPlacementGrid[k][j].getText().equals("SHIP")) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Function checks whether there is already a ship placed on that column co-ordinates.
	 * @param i The row co-ordinate
	 * @param j The column co-ordinate
	 * @param shipSpace The length of the ship
	 * @param lblShipPlacementGrid The player board
	 * @return true if occupied else false.
	 */
	public boolean checkIfColAreaOccupiedByAnotherShip(int i, int j, int shipSpace, JLabel[][] lblShipPlacementGrid) {
		for(int k = j; k < (j + shipSpace); k++) {
			if(lblShipPlacementGrid[i][k].getText().equals("SHIP")) {
				return true;
			}
		}
		return false;
	}
	
	
	public boolean checkIfShipInEntireRow(int j, int maxRows, JLabel[][] lblShipPlacementGrid) {
		for(int k = 0; k < maxRows; k++) {
			if(lblShipPlacementGrid[k][j].getText().equals("SHIP")) {
				return true;
			}
		}
		return false;
	}
	
	public boolean checkIfShipInEntireCol(int i, int maxCols, JLabel[][] lblShipPlacementGrid) {
		for(int k = 0; k < maxCols; k++) {
			if(lblShipPlacementGrid[i][k].getText().equals("SHIP")) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * This function places the ship on the board according to the length of the ship.
	 * @param shipSpace The length of the ship
	 * @param shipPos String containing the co-ordinates
	 */
	public void fillPlayerShip(int shipSpace, String shipPos) {
		if(shipSpace == 5) {
			playerShips[0] = shipPos;
		}
		else if(shipSpace == 4) {
			playerShips[1] = shipPos;
		}
		else if(shipSpace == 3) {
			if(playerShips[2] == null || playerShips[2].length() < 2) {
				playerShips[2] = shipPos;
			}
			else {
				playerShips[3] = shipPos;
			}
		}
		else if(shipSpace == 2) {
			playerShips[4] = shipPos;
		}
	}
	
	/**
	 * This function checks if there is a ship in next column or not.
	 * @param i Row co-ordinate
	 * @param j Column co-ordinate
	 * @param shipSpace The length of the ship
	 * @param lblShipPlacementGrid The board on which ship needs to be placed
	 * @return
	 */
	public boolean checkVerticalParallel(int i, int j, int shipSpace, JLabel[][] lblShipPlacementGrid) {
		if(i > 0) {
			int tmpi = i - 1;
			for(int k = j; k < (j + shipSpace); k++) {
				if(lblShipPlacementGrid[tmpi][k].getText().equals("SHIP")) {
					return false;
				}
			}
		}
		if(i < (Board.BOARD_COLS - 1)) {
			int tmpi = i + 1;
			for(int k = j; k < (j + shipSpace); k++) {
				if(lblShipPlacementGrid[tmpi][k].getText().equals("SHIP")) {
					return false;
				}
			}
		}
		
		return true;
	}
	
	/**
	 * This function checks if there is a ship in next row or not.
	 * @param i Row co-ordinate
	 * @param j Column co-ordinate
	 * @param shipSpace The length of the ship
	 * @param lblShipPlacementGrid The board on which ship needs to be placed
	 * @return
	 */
	public boolean checkHorizontalParallel(int i, int j, int shipSpace, JLabel[][] lblShipPlacementGrid) {
		if(j > 0) {
			int tmpi = j - 1;
			for(int k = i; k < (i + shipSpace); k++) {
				if(lblShipPlacementGrid[k][tmpi].getText().equals("SHIP")) {
					return false;
				}
			}
		}
		if(j < (Board.BOARD_COLS - 1)) {
			int tmpi = j + 1;
			for(int k = i; k < (i + shipSpace); k++) {
				if(lblShipPlacementGrid[k][tmpi].getText().equals("SHIP")) {
					return false;
				}
			}
		}
		
		return true;
	}
	

	/**
	 * The function to place ship horizontally.
	 * @param i The row co-ordinate
	 * @param j The column co-ordinate
	 * @param shipText The length of the ship
	 * @return true if constraints satisfies else false.
	 */
	public boolean processShipPlacementHorizontally(int i, int j, String shipText) {
		String[] numShips = shipText.split("#");
		
		if(checkShipPlacementSpace(i, Board.BOARD_ROWS, numShips.length) == false) {
			if(DEV_TEST != 5)
				JOptionPane.showMessageDialog(null, "Not enough space to put the selected ship. Please try again");
			return false;
		}
		
		if(checkIfRowAreaOccupiedByAnotherShip(i, j, numShips.length, lblShipPlacementGrid) == true) {
			if(DEV_TEST != 5)
				JOptionPane.showMessageDialog(null, "Area occupied by another ship. Please try again");
			return false;
		}
		
		if(checkIfShipInEntireRow(j, Board.BOARD_ROWS, lblShipPlacementGrid) == true) {
			if(DEV_TEST != 5)
				JOptionPane.showMessageDialog(null, "There is already a ship in this row. Please try again");
			return false;
		}
		
		if(checkHorizontalParallel(i, j, numShips.length, lblShipPlacementGrid) == false) {
			if(DEV_TEST != 5)
				JOptionPane.showMessageDialog(null, "There is already a ship besides this ship");
			return false;
		}
		
		String shipPos = "";
		
		for(int k = i; k < (i + numShips.length); k++) {
			lblShipPlacementGrid[k][j].setText("SHIP");
			board.ship_placement_grid[k][j] = Board.PLACEMENT_BOARD_SHIP;
			shipPos += k + "" + j + ",";
		}
		
		fillPlayerShip(numShips.length, shipPos);
		numShipsPlaced++;
		return true;
	}
	
	/**
	 * The function to place ship vertically.
	 * @param i The row co-ordinate
	 * @param j The column co-ordinate
	 * @param shipText The length of the ship
	 * @return true if constraints are satisfies else false.
	 */
	public boolean processShipPlacementVertically(int i, int j, String shipText) {
		String[] numShips = shipText.split("#");
		
		if(checkShipPlacementSpace(j, Board.BOARD_COLS, numShips.length) == false) {
			if(DEV_TEST != 5)
				JOptionPane.showMessageDialog(null, "Not enough space to put the selected ship. Please try again");
			System.out.println(DEV_TEST);
			return false;
		}
		
		if(checkIfColAreaOccupiedByAnotherShip(i, j, numShips.length, lblShipPlacementGrid) == true) {
			if(DEV_TEST != 5)
				JOptionPane.showMessageDialog(null, "Area occupied by another ship. Please try again");
			return false;
		}
		
		if(checkIfShipInEntireCol(i, Board.BOARD_COLS, lblShipPlacementGrid) == true) {
			if(DEV_TEST != 5)
				JOptionPane.showMessageDialog(null, "There is already a ship in this col. Please try again");
			return false;
		}
		
		if(checkVerticalParallel(i, j, numShips.length, lblShipPlacementGrid) == false) {
			if(DEV_TEST != 5)
				JOptionPane.showMessageDialog(null, "There is already a ship besides this ship");
			return false;
		}
		
		String shipPos = "";
		
		for(int k = j; k < (j + numShips.length); k++) {
			lblShipPlacementGrid[i][k].setText("SHIP");
			board.ship_placement_grid[i][k] = Board.PLACEMENT_BOARD_SHIP;
			shipPos += i + "" + k + ",";
		}
		
		fillPlayerShip(numShips.length, shipPos);
		numShipsPlaced++;
		return true;
	}
	
	/**
	 * The main function called by OS
	 * @param args The command line argumeents
	 */
	public static void main(String[] args) {
		Window win = new Window();
		win.start();
	}
	
	/**
	 * The function places the ships for testing function.
	 */
	public void autoShipPlacementForTesting() {
		String shipText = "SHIP#SHIP#SHIP#SHIP#SHIP";
		processShipPlacementVertically(0, 0, shipText);
		
		shipText = "SHIP#SHIP#SHIP#SHIP";
		processShipPlacementVertically(1, 1, shipText);
		
		shipText = "SHIP#SHIP#SHIP";
		processShipPlacementVertically(2, 2, shipText);
		
		shipText = "SHIP#SHIP#SHIP";
		processShipPlacementVertically(3, 3, shipText);
		
		shipText = "SHIP#SHIP";
		processShipPlacementVertically(4, 4, shipText);
		
		createGameAndPlayer(board);
	}

	/**
	 * Listeners for the ship(JLabel).
	 */
	MouseListener lblDnd = new MouseListener() {
		@Override
		public void mouseReleased(MouseEvent e) {
			
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
						String horizontalVertical = JOptionPane.showInputDialog("Enter 1 to place horizontally, anything else to place vertically");
						Boolean shipPlacementResult = false;
						if(horizontalVertical.equals("1")) {
							shipPlacementResult = processShipPlacementHorizontally(i, j, toMoveShip.getText());
						}
						else {
							shipPlacementResult = processShipPlacementVertically(i, j, toMoveShip.getText());
						}
						if(shipPlacementResult == true) {
							toMoveShip.setText("");
							toMoveShip.disable();
						}
					}
				}
			}
			
			toMoveShip = null;

			if(numShipsPlaced == 5) {
				System.out.println("All Ships placed. Game starts now");
				createGameAndPlayer(board);
			}
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
