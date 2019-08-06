package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import controller.GameController;
import model.Board;
import model.Game;

public class Menu {
	
	Window win;
	String SAVE_GAME_PATH = "F:\\savedgame\\";

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
				String player2 = "TestPlayer2";
				
				boolean multiplayer = false;
				
				if(Window.DEV_TEST != 1) {
					String isMulti = JOptionPane.showInputDialog("Please input game type: (1 - Single Player, Anything else for Multiplayer)");
					if(isMulti.equals("1")) {
						multiplayer = false;
					}
					else {
						multiplayer = true;
					}
				}
				
				if(Window.DEV_TEST != 1) {
					if(multiplayer == false) {
						player1 = JOptionPane.showInputDialog("Please input name for player 1: ");
					}
					else {
						player1 = JOptionPane.showInputDialog("Please input name for player: ");
					}
				}
				
				String strGameMode = "hj";
				if(Window.DEV_TEST != 1) {
					strGameMode = JOptionPane.showInputDialog("Please input game mode: (1 - Salva, Anything else for Regular)");
				}
				int gameMode = Game.GAME_TYPE_REGULAR;
				
				if(strGameMode.equals("1")) {
					gameMode = Game.GAME_TYPE_SALVA;
				}
				
				if(multiplayer == false) {
					win.gameController = new GameController(gameMode, player1);
				}
				else {
					
				}
				
				if(multiplayer == false) {
					win.createBoardDisplay(win.gameFrame);
					win.tmpPlayerName = player1;
					win.gameFrame.revalidate();
				}
			}
		});
		
		return newGame;
	}

	public JMenuItem createSaveGameMenuItem() {
		JMenuItem saveGame = new JMenuItem("Save Game");
		saveGame.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveGame();
			}
		});
		return saveGame;
	}
	
	public JMenuItem createLoadGameMenuItem() {
		JMenuItem loadGame = new JMenuItem("Load Game");
		loadGame.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				loadGame();
			}
		});
		return loadGame;
	}

	public void saveGame() {
		FileOutputStream fout = null;
		ObjectOutputStream oos = null;
		
		try {
			fout = new FileOutputStream(SAVE_GAME_PATH + "game");
			oos = new ObjectOutputStream(fout);
			oos.writeObject(win);
			win.gameFrame.setVisible(false);
			System.out.println("Game Saved");
			System.exit(0);
		}
		catch(Exception e) {
			e.printStackTrace();
			System.out.println("Game could not be saved");
		}
	}
	
	public void loadGame() {
		try {
			ObjectInputStream iis = new ObjectInputStream(new FileInputStream(SAVE_GAME_PATH + "game"));
			win = (Window) iis.readObject();
			System.out.println("Game Loaded");
			iis.close();
			
			if(win.isMultiplayer == false) {
				win.gameFrame.setVisible(true);
				//win.gameFrame.revalidate();
			}
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * Function to create file menu
	 * @return The File menu
	 */
	public JMenu createFileMenu() {
		JMenu file = new JMenu("File");
		file.add(createNewGameMenuItem());
		file.add(createSaveGameMenuItem());
		file.add(createLoadGameMenuItem());
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
	 * This function generates the java frame.
	 */
	public void initMainFrame() {
		JFrame.setDefaultLookAndFeelDecorated(true);
		win.gameFrame = new JFrame("BattleShip");
		win.gameFrame.setLayout(null);
		win.gameFrame.setSize(1600,900);
		win.gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		win.gameFrame.setJMenuBar(createMenu());
		win.gameFrame.setVisible(true);
		win.playerShips = new String[5];
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
		Menu men = new Menu();
		men.win = new Window();
		men.win.isMultiplayer = false;
		men.start();
	}
	
}
