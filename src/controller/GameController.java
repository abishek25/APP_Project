package controller;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import model.Board;
import model.Game;
import model.Player;

/**
 * This class is used to handle events received from view
 *
 */
public class GameController implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	Game game;
	public int gameMode;
	String player1Name;
	String player2Name;
	boolean creator;
	
	/**
	 * The parameterized constructor.
	 * @param gameMode The game mode(Salva or Basic).
	 */
	public GameController(int gameMode, String player1Name) {
		this.gameMode = gameMode;
		this.player1Name = player1Name;
	}
	
	public GameController(int gameMode, String player1Name, String player2Name, Boolean creator) {
		this.gameMode = gameMode;
		this.player1Name = player1Name;
		this.player2Name = player2Name;
		this.creator = creator;
	}
	
	/**
	 * Function to create game
	 * @param player1Name The player 1 name
	 * @param board The game board
	 * @param playerShips ships
	 */
	public void createGame(String player1Name, Board board, String[] playerShips, Board player2board, String[] player2Ships) {
		if(this.player2Name == null) {
			game = new Game(this.player1Name, board, gameMode, playerShips);
		}
		else {
			game = new Game(this.player1Name, board, gameMode, playerShips, this.player2Name, player2board, player2Ships);
		}
	}
	
	public void createMultiplayerGame(Board board, String[] playerShips) {
		if(creator == true) {
			System.out.println("Game Created By: " + this.player1Name);
			game = new Game(player1Name, board, gameMode, playerShips, player2Name, true);
		}
		else {
			System.out.println("Game Joined By: " + this.player2Name);
			game = new Game(player2Name, board, gameMode, playerShips, player1Name, false);
		}
	}
	
	/**
	 * Function to return the game
	 * @return The game
	 */
	public Game getGame() {
		return game;
	}
	
	/**
	 * Function to get current player
	 * @return The current player
	 */
	public Player getCurrPlayer() {
		return game.getCurrPlayer();
	}
	
	public String multiplayerGetCurrPlayerName() {
		return game.multiplayerGetCurrPlayerName();
	}
	
	/**
	 * Function to process attack
	 * @param row The row co-ordinate
	 * @param col The column co-ordinate
	 * @return The result of attack
	 */
	public String processAttack(int row, int col) {
		return game.processAttack(row, col);
	}
	
	/**
	 * Function get player 1 score
	 * @return Player 1 score
	 */
	public int getP1Score() {
		return game.getPlayerOneResults();
	}
	
	/**
	 * Function get player 2 score
	 * @return Player 2 score
	 */
	public int getAIScore() {
		return game.getPlayerTwoResults();
	}
	
	public int getGameTypeMode() {
		return game.getGameTypeMode();
	}
}
