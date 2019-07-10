package controller;

import model.Game;
import model.Player;

/**
 * This class is used to handle events received from view
 *
 */
public class GameController {
	
	Game game;
	
	/**
	 * Function to create game
	 * @param player1Name The player 1 name
	 * @param positions1 The player 1 positions
	 */
	public void createGame(String player1Name, String positions1) {
		game = new Game(player1Name, positions1);
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
