package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import controller.GameController;
import model.Board;
import model.Game;
import model.Player;

public class Window {
	
	JFrame gameFrame; 
	GameController gameController;
	JButton[][] lblAttackGrid;
	JLabel[][] lblShipPlacementGrid;
	JLabel turnLabel;
	
	public JMenuItem createNewGameMenuItem() {
		JMenuItem newGame = new JMenuItem("New Game");
		
		newGame.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String player1 = JOptionPane.showInputDialog("Please input name for player 1: ");
				String positions1 = JOptionPane.showInputDialog(""
						+ "Please input placement positions for\n"
						+ "Carrier(5),Battleship(4),Cruiser(3),Submarine(3),Destroyer(2) respectively\n"
						+ "Comma Separated Values\n"
						+ "Example: (A1,A5),(B2,B5),(C3,C5),(D4,D6),(E5,E6): ");
				
				positions1 = "(A1,A5),(B2,B5),(C3,C5),(D4,D6),(E5,E6)";
				
				while(Game.checkPositions(positions1) == false) {
					positions1 = JOptionPane.showInputDialog("Incorrect positions. Try again: ");
				}
				
				gameController = new GameController();
				gameController.createGame(player1, positions1);
				
				createBoardDisplay(gameFrame);
				
				turnLabel = new JLabel("Current Turn: " + gameController.getCurrPlayer().getName());
				turnLabel.setBounds(0, 600, 300, 50);
				gameFrame.add(turnLabel);
				
				gameFrame.revalidate();
				
			}
		});
		
		return newGame;
	}
	
	public JMenu createFileMenu() {
		JMenu file = new JMenu("File");
		file.add(createNewGameMenuItem());
		return file;
	}
	
	public JMenuBar createMenu() {
		JMenuBar menuBar = new JMenuBar();
		menuBar.add(createFileMenu());
		return menuBar;
	}
	
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
	
	public String processCommand(int row, int col) {
		String result = gameController.processAttack(row, col);
		refreshBoard();
		return result;
	}

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
					//JLabel lbl = new JLabel("-----");
					JButton btn = new JButton(("" + (char)(65 + i)) + (j + 1));
					lblAttackGrid[i][j] = btn;
					lblAttackGrid[i][j].setBounds(600 + (70 * i), (70 * j), 70, 70);
					//lblAttackGrid[i][j].setLocation(40 * i, 40 * j);
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
		
		refreshBoard();
	}

	public void initMainFrame() {
		JFrame.setDefaultLookAndFeelDecorated(true);
	    gameFrame = new JFrame("BattleShip");
	    gameFrame.setLayout(null);
	    gameFrame.setSize(1200,800);
	    gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    gameFrame.setJMenuBar(createMenu());
	    gameFrame.setVisible(true);
	}
	
	public void start() {
		initMainFrame();
	}
	
	public static void main(String[] args) {
		Window win = new Window();
		win.start();
	}

}
