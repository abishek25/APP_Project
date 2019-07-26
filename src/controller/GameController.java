package controller;

import model.Board;
import model.Game;
import model.Player;

/**
 * This class is used to handle events received from view
 *
 */
public class GameController {
	
	Game game;
	int gameMode;
	
	/**
	 * The constructor to assign game mode
	 * @param gameMode The game mode
	 */
	public GameController(int gameMode) {
		this.gameMode = gameMode;
	}
	
	/**
	 * Function to create game
	 * @param player1Name The player 1 name
	 * @param positions1 The player 1 positions
	 * @param player1 ships
	 */
	public void createGame(String player1Name, Board board, String[] playerShips) {
		game = new Game(player1Name, board, gameMode, playerShips);
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
	
	/**
	 * Function to process attack
	 * @param row The row for attack
	 * @param col The column for attack
	 * @return The result of attack
	 */
	public String processAttack(int row, int col) {
		return game.processAttack(row, col);
	}
}
