package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Game {
	
	public static int GAME_MODE_AI = 1;
	public static int GAME_MODE_NETWORK = 2;
	public static int GAME_MODE_INVALID = 3;
	public static Random rand = new Random();
	
	Player[] players;
	int mode;
	int currPlayer;
	static boolean isFinished;
	static String winnerName;
	
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
		isFinished = false;
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
	
	public void generateAiAttack() {
		ArrayList<String> possiblepairs = new ArrayList<String>();
		
		for(int i = 0; i < Board.BOARD_ROWS; i++) {
			for(int j = 0; j < Board.BOARD_COLS; j++) {
				if(players[1].board.attack_grid[i][j] == Board.BOARD_EMPTY) {
					possiblepairs.add(String.valueOf(i) + "," + String.valueOf(j));
				}
			}
		}
		
		Collections.shuffle(possiblepairs);
		int row = Integer.parseInt(possiblepairs.get(0).split(",")[0]);
		int col = Integer.parseInt(possiblepairs.get(0).split(",")[1]);
		String result = players[0].checkAttack(row, col);
		if(result.equals(Player.ATTACK_HIT)) {
			players[1].board.attack_grid[row][col] = Board.BOARD_HIT;
		}
		else if(result.equals(Player.ATTACK_MISS)){
			players[1].board.attack_grid[row][col] = Board.BOARD_MISS;
		}
		else {
			//
		}
		System.out.println("AI Attack Result For (" + row + "," + col + "): " + result);
	}
	
	public void updateGameStatus(Board board) {
		boolean rt = false;
		int[][] grid = board.getShipPlacementGrid();
		for(int i = 0; i < Board.BOARD_ROWS; i++) {
			for(int j = 0; j < Board.BOARD_COLS; j++) {
				if(grid[i][j] == Board.PLACEMENT_BOARD_SHIP) {
					rt = true;
					break;
				}
			}
		}
		if(rt == false) {
			isFinished = true;
		}
	}
	
	public String processAttack(int row, int col) {
		String result = "";
		if(getGameMode() == GAME_MODE_AI) {
			result = players[1].checkAttack(row, col);
			System.out.println("Player Attack Result For (" + row + "," + col + "): " + result);
			if(result.equals(Player.ATTACK_HIT)) {
				players[0].board.attack_grid[row][col] = Board.BOARD_HIT;
			}
			else {
				players[0].board.attack_grid[row][col] = Board.BOARD_MISS;
			}
			updateGameStatus(players[1].board);
			if(isFinished == false) {
				generateAiAttack();
				updateGameStatus(players[0].board);
				if(isFinished == true) {
					winnerName = players[1].getName();
				}
			}
			else {
				winnerName = players[0].getName();
			}
		}
		else {
			if(currPlayer == 0) {
				result = players[1].checkAttack(row, col);
				currPlayer = 1;
			}
			else if(currPlayer == 1) {
				result = players[0].checkAttack(row, col);
				currPlayer = 0;
			}
		}
		
		return result;
	}
	
	public static String getWinner() {
		return winnerName;
	}
	
	public static boolean checkIfGameWon() {
		return isFinished;
	}
}
