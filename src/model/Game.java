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
		int corr_size[] = {4,3,2,2,1};
		ArrayList<String> prevPositions = new ArrayList<String>();
		positions = positions.replaceAll(" ", "");
		String[] pos = positions.split("\\(");
		if(pos.length < 6) {
			return false;
		}
		for(int i = 1; i < pos.length; i++) {
			String[] info = pos[i].split(",");
			if(info.length < 2) {
				continue;
			}
			info[1] = info[1].substring(0, info[1].length() - 1);

			int row = info[0].charAt(0) - 65;
			int col = Integer.parseInt(info[0].substring(1)) - 1;
			
			int row2 = info[1].charAt(0) - 65;
			int col2 = Integer.parseInt(info[1].substring(1)) - 1;
			
			if(row < 0 || row2 < 0 || row > Board.BOARD_ROWS || row2 > Board.BOARD_ROWS ||
					col < 0 || col2 < 0 || col > Board.BOARD_COLS || col > Board.BOARD_COLS) {
				return false;
			}
			else if(row == row2 && (col2 - col) == corr_size[i - 1]) {
				for(int j = col; j <= col2; j++) {
					String toAdd = String.valueOf(Character.valueOf((char)(row + 65))) + j;
					if(prevPositions.contains(toAdd) == false) {
						prevPositions.add(toAdd);
					}
					else {
						return false;
					}
				}
			}
			else if(col == col2 && (row2 - row) == corr_size[i - 1]) {
				for(int j = row; j <= row2; j++) {
					String toAdd = String.valueOf(Character.valueOf((char)(j + 65))) + col;
					if(prevPositions.contains(toAdd) == false) {
						prevPositions.add(toAdd);
					}
					else {
						return false;
					}
				}
			}
			else {
				return false;
			}
		}
		
		return true;
	}
	
	public String randomShipPositions() {
		ArrayList<Integer> rows = new ArrayList<Integer>();
		for(int i = 0; i < (Board.BOARD_ROWS - 5); i++) {
			rows.add(i);
		}
		
		int[] shipSizes = {4,3,2,2,1};
		String aiPositions = "";
		for(int i = 0; i < shipSizes.length; i++) {
			Collections.shuffle(rows);
			int row = rows.remove(0);
			int col = rand.nextInt(Board.BOARD_COLS - shipSizes[i]) + 1;
			aiPositions = aiPositions + "(" + ((char)(row + 65)) + col + "," + ((char)(row + 65)) + (col + shipSizes[i]) + "),";
		}
		aiPositions = aiPositions.substring(0, aiPositions.length() - 1);
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
