package controller;

import model.Game;
import model.Player;

public class GameController {
	
	Game game;
	
	public void createGame(String player1Name, String positions1) {
		game = new Game(player1Name, positions1);
	}
	
	public Game getGame() {
		return game;
	}
	
	public Player getCurrPlayer() {
		return game.getCurrPlayer();
	}
	
	public String processAttack(int row, int col) {
		return game.processAttack(row, col);
	}
}
