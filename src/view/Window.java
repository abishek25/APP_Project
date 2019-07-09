package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import controller.GameController;
import model.Board;
import model.Game;

public class Window {
	
	GameController gameController;
	JButton[][] lblAttackGrid;
	JLabel[][] lblShipPlacementGrid;
	
	public JMenuItem createNewGameMenuItem() {
		JMenuItem newGame = new JMenuItem("New Game");
		
		newGame.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String player1 = JOptionPane.showInputDialog("Please input name for player 1: ");
				gameController = new GameController();
				gameController.createGame(player1);
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
	
	public void addBoard() {
		Board board = gameController.getGame().getGameBoard();
		int[][] attackGrid = board.getAttackGrid();
		boolean[][] shipPlacementGrid = board.getShipPlacementGrid();
		
		for(int i = 0; i < Board.BOARD_ROWS; i++) {
			for(int j = 0; j < Board.BOARD_COLS; j++) {
				
			}
		}
	}

	
	
	public void createBoardDisplay(JFrame f) {
		lblAttackGrid = new JButton[Board.BOARD_ROWS][Board.BOARD_COLS];
		lblShipPlacementGrid = new JLabel[Board.BOARD_ROWS][Board.BOARD_COLS];
		
		for(int i = 0; i < Board.BOARD_ROWS + 1; i++) {
			for(int j = 0; j < Board.BOARD_COLS + 1; j++) {
				if(i == Board.BOARD_ROWS || j == Board.BOARD_COLS) {
					JLabel lbl = new JLabel("");
					lbl.setBounds((60 * i), (60 * j), 200, 50);
					
					JLabel lbl2 = new JLabel("");
					lbl2.setBounds(600 + (60 * i), (60 * j), 200, 50);
					
					f.add(lbl);
					f.add(lbl2);
				}
				else {
					//JLabel lbl = new JLabel("-----");
					JButton btn = new JButton(("" + (char)(65 + i)) + (j + 1));
					lblAttackGrid[i][j] = btn;
					lblAttackGrid[i][j].setBounds(600 + (60 * i), (60 * j), 60, 60);
					//lblAttackGrid[i][j].setLocation(40 * i, 40 * j);
					lblAttackGrid[i][j].addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							System.out.println(e.getActionCommand());
						}
					});
					
					JLabel lbl2 = new JLabel("-----");
					lblShipPlacementGrid[i][j] = lbl2;
					lblShipPlacementGrid[i][j].setBounds((60 * i), (60 * j), 200, 50);
					
					f.add(lblAttackGrid[i][j]);
					f.add(lblShipPlacementGrid[i][j]);
				}
			}
		}
	}
	
	public void displayBoard() {
		
	}
	
	public void start() {
		JFrame.setDefaultLookAndFeelDecorated(true);
	    JFrame f = new JFrame("BattleShip");
	    f.setSize(1200,800);
	    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    f.setJMenuBar(createMenu());
	    createBoardDisplay(f);
	    f.setVisible(true);
	    
	}
	
	public static void main(String[] args) {
		Window win = new Window();
		win.start();
	}

}
