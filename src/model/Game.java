package model;

public class Game {
	
	public static int GAME_MODE_AI = 1;
	public static int GAME_MODE_NETWORK = 2;
	public static int GAME_MODE_INVALID = 3;
	
	Player[] players;
	int mode;
	int currPlayer;
	
	public Game(String playerOneName) {
		players = new Player[2];
		players[0] = new Player(playerOneName);
		players[1] = new Player("AI");
		mode = GAME_MODE_AI;
		currPlayer = 0;
	}
	
	public Game(String playerOneName, String playerTwoName) {
		players = new Player[2];
		players[0] = new Player(playerOneName);
		players[1] = new Player(playerTwoName);
		mode = GAME_MODE_NETWORK;
		currPlayer = 0;
	}
	
	public int getGameMode() {
		return mode;
	}
	
	public Board getGameBoard() {
		return players[currPlayer].getBoard();
	}
	
	public Board getAiBoard() {
		if(mode == GAME_MODE_AI) {
			return players[1].getBoard();
		}
		return null;
	}
}
