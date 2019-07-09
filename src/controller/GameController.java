package controller;

import model.Game;
import model.Player;

public class GameController {
	
	Game game;
	
	public void createGame(String player1Name) {
		game = new Game(player1Name);
	}
	
	public Game getGame() {
		return game;
	}
}
