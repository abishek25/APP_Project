package model;

import java.util.Random;

public class Game {
	
	public static int GAME_MODE_AI = 1;
	public static int GAME_MODE_NETWORK = 2;
	public static int GAME_MODE_INVALID = 3;
	public static Random rand = new Random();
	
	Player[] players;
	int mode;
	int currPlayer;
	
	public static boolean checkPositions(String positions) {
		return true;
	}
	
	public String randomShipPositions() {
		String aiPositions = "(B1,B5),(D2,D5),(C3,C5),(F4,F6),(G2,G3)";
		return aiPositions;
	}
	
	public Game(String playerOneName, String player1Positions) {
		players = new Player[2];
		players[0] = new Player(playerOneName, player1Positions);
		players[1] = new Player("AI", randomShipPositions());
		mode = GAME_MODE_AI;
		currPlayer = 0;
	}
	
	public Game(String playerOneName, String player1Positions, String playerTwoName, String player2Positions) {
		players = new Player[2];
		players[0] = new Player(playerOneName, player1Positions);
		players[1] = new Player(playerTwoName, player2Positions);
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
	
	public Player getCurrPlayer() {
		return this.players[currPlayer];
	}
	
	public String processAttack(int row, int col) {
		return "";
	}
}
