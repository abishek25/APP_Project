package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import controller.GameController;
import model.Board;
import model.Game;

public class Menu {
	
	public static int PORT_CREATOR = 4444;
	public static int PORT_JOINER = 2222;
	
	public static String ADDR_CREATOR = "localhost";
	public static String ADDR_JOINER = "localhost";
	
	public Window win;
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
				
				boolean multiplayer = true;
				win.isMultiplayer = multiplayer;
				
				if(Window.DEV_TEST != 1) {
					String isMulti = JOptionPane.showInputDialog("Please input game type: (1 - Single Player, Anything else for Multiplayer)");
					if(isMulti.equals("1")) {
						multiplayer = false;
						win.isMultiplayer = false;
					}
					else {
						multiplayer = true;
						win.isMultiplayer = true;
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
				
				boolean gameCreating = false;
				boolean gameJoining = false;
				
				//if(Window.DEV_TEST != 1) {
				if(multiplayer == true) {
					String createJoinStr = JOptionPane.showInputDialog("Are you creating game or joining? (1 for Creation, Anything else for Joining)");
					if(createJoinStr.equals("1")) {
						gameCreating = true;
					}
					else {
						gameJoining = true;
					}
				}
				//}
				
				String strGameMode = "hj";
				int gameMode = -1;
				
				if(multiplayer == false || (multiplayer == true && gameCreating == true)) {
					if(Window.DEV_TEST != 1) {
						strGameMode = JOptionPane.showInputDialog("Please input game mode: (1 - Salva, Anything else for Regular)");
					}
					gameMode = Game.GAME_TYPE_REGULAR;
					
					if(strGameMode.equals("1")) {
						gameMode = Game.GAME_TYPE_SALVA;
					}
				}
				
				if(multiplayer == false) {
					win.gameController = new GameController(gameMode, player1);
				}
				else {
					if(gameCreating == true) {
						String other_player_name = multiplayerCreateGame(player1, gameMode);
						win.gameController = new GameController(gameMode, player1, other_player_name, true);
						win.createBoardDisplay(win.gameFrame);
						win.tmpPlayerName = player1;
						win.gameFrame.revalidate();
					}
					else if(gameJoining == true) {
						if(Window.DEV_TEST != 1) {
							String other_player_name = multiplayerJoinGame(player1);
							win.gameController = new GameController(Integer.parseInt(other_player_name.split("_")[1]), 
									other_player_name.split("_")[0], player1, false);
							win.createBoardDisplay(win.gameFrame);
							win.tmpPlayerName = player1;
							win.gameFrame.revalidate();
						}
						else {
							String other_player_name = multiplayerJoinGame(player2);
							win.gameController = new GameController(Integer.parseInt(other_player_name.split("_")[1]), 
									other_player_name.split("_")[0], player2, false);
							
							win.createBoardDisplay(win.gameFrame);
							win.tmpPlayerName = player1;
							win.gameFrame.revalidate();
						}
					}
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

	public String multiplayerCreateGame(String playerName, int gameMode) {
		System.out.println("Game created at socket 4444. Waiting for players");
		
		try {
			DatagramSocket socket = new DatagramSocket(PORT_CREATOR);
			byte[] buffer = new byte[65536];
			
			DatagramPacket incoming = new DatagramPacket(buffer, buffer.length);
			socket.receive(incoming);
			byte[] data = incoming.getData();
			String recd_info = new String(data, 0, incoming.getLength());
			
			buffer = new byte[65536];
			String to_send = playerName + "_" + gameMode;
			buffer = to_send.getBytes();
			DatagramPacket dp = new DatagramPacket(buffer, buffer.length , 
					incoming.getAddress(), incoming.getPort());
			socket.send(dp);
			
			socket.close();
			return recd_info;
			
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String multiplayerJoinGame(String playername) {
		try {
			DatagramSocket socket = new DatagramSocket(PORT_JOINER);
			byte[] buffer = new byte[65536];
			String to_send = playername;
			buffer = to_send.getBytes();
			DatagramPacket dp = new DatagramPacket(buffer, buffer.length , 
					InetAddress.getByName(ADDR_CREATOR), PORT_CREATOR);
			socket.send(dp);
			
			buffer = new byte[65536];
			DatagramPacket incoming = new DatagramPacket(buffer, buffer.length);
			socket.receive(incoming);
			byte[] data = incoming.getData();
			String recd_info = new String(data, 0, incoming.getLength());
			
			socket.close();
			return recd_info;
		}
		catch (SocketException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
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
