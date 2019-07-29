package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * This class is used to hold information about the game in progress
 *
 */
public class Game {
	
	public static int GAME_MODE_AI = 1;
	public static int GAME_MODE_NETWORK = 2;
	public static int GAME_MODE_INVALID = 3;
	
	public static int GAME_TYPE_REGULAR = 1;
	public static int GAME_TYPE_SALVA = 2;
	public static int TURN_TIMER_MAX = 5;
	
	public static Random rand = new Random();
	
	Player[] players;
	int mode;
	public static int gameMode;
	
	public static ArrayList<String> salvaAttackRes;
	
	int currPlayer;
	static boolean isFinished;
	static String winnerName;
	
	static int prevAIAttackResult;
	static int prevAIAttackRow;
	static int prevAIAttackCol;
	
	Integer playerTurnTimer;
	
	int playerOneScore;
	int playerTwoScore;
	
	public static String gameScores;
	
	/**
	 * This function checks positions passed for placement of ship
	 * @param positions of ship
	 * @return Success or failure
	 */
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
	
	/**
	 * This function generates random placement of ships for the AI engine
	 * @return The positions as string
	 */
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
	
	/**
	 * The consutructor to get information about the players playing the game
	 * @param playerOneName The player name
	 * @param player1Positions The player positions
	 * @param gameMode The game mode
	 * @param playerShips The player's ships
	 */
	public Game(String playerOneName, Board board, int gameMode, String[] playerShips) {
		players = new Player[2];
		players[0] = new Player(playerOneName, board, playerShips);
		players[1] = new Player("AI", randomShipPositions());
		mode = GAME_MODE_AI;
		this.gameMode = gameMode;
		currPlayer = 0;
		isFinished = false;
		salvaAttackRes = new ArrayList<String>();
		playerOneScore = 0;
		playerTwoScore = 0;
		playerTurnTimer = 0;
		timer.start();
	}
	
	/**
	 * Constructor to get information about two players
	 * @param playerOneName Player 1 name
	 * @param player1Positions Player 1 positions
	 * @param playerTwoName Player 2 name
	 * @param player2Positions Player 2 positions
	 */
	public Game(String playerOneName, String player1Positions, String playerTwoName, String player2Positions) {
		players = new Player[2];
		players[0] = new Player(playerOneName, player1Positions);
		players[1] = new Player(playerTwoName, player2Positions);
		mode = GAME_MODE_NETWORK;
		currPlayer = 0;
		salvaAttackRes = new ArrayList<String>();
		playerOneScore = 0;
		playerTwoScore = 0;
		playerTurnTimer = 0;
		timer.start();
	}
	
	/**
	 * Function to get game mode
	 * @return The game mode
	 */
	public int getGameMode() {
		return mode;
	}
	
	/**
	 * Function to get game board
	 * @return The game board
	 */
	public Board getGameBoard() {
		return players[currPlayer].getBoard();
	}
	
	/**
	 * Function to get ai board
	 * @return The game board of AI
	 */
	public Board getAiBoard() {
		if(mode == GAME_MODE_AI) {
			return players[1].getBoard();
		}
		return null;
	}
	
	/**
	 * Function to get current player
	 * @return The current player
	 */
	public Player getCurrPlayer() {
		return this.players[currPlayer];
	}
	
	/**
	 * Function to generate ai attack
	 */
	public void generateAiAttack() {
		ArrayList<String> possiblepairs = new ArrayList<String>();
		int i1 = 0;
		int j1 = 0;
		
		if(prevAIAttackResult == 1) {
			if(prevAIAttackCol < (Board.BOARD_COLS - 1)) {
				i1 = prevAIAttackRow;
				j1 = prevAIAttackCol + 1;
			}
			else if(prevAIAttackCol > 0) {
				i1 = prevAIAttackRow;
				j1 = prevAIAttackCol - 1;
			}
			else if(prevAIAttackRow < (Board.BOARD_ROWS - 1)) {
				i1 = prevAIAttackRow + 1;
				j1 = prevAIAttackCol;
			}
			else if(prevAIAttackRow > 0) {
				i1 = prevAIAttackRow - 1;
				j1 = prevAIAttackCol;
			}
			
			if(players[1].board.attack_grid[i1][j1] == Board.BOARD_EMPTY) {
				possiblepairs.add(String.valueOf(i1) + "," + String.valueOf(j1));
			}
		}
		
		if(possiblepairs.size() == 0) {
			for(int i = 0; i < Board.BOARD_ROWS; i++) {
				for(int j = 0; j < Board.BOARD_COLS; j++) {
					if(players[1].board.attack_grid[i][j] == Board.BOARD_EMPTY) {
						possiblepairs.add(String.valueOf(i) + "," + String.valueOf(j));
					}
				}
			}
		}
		
		Collections.shuffle(possiblepairs);
		int row = Integer.parseInt(possiblepairs.get(0).split(",")[0]);
		int col = Integer.parseInt(possiblepairs.get(0).split(",")[1]);
		String result = players[0].checkAttack(row, col);
		if(result.equals(Player.ATTACK_HIT)) {
			players[1].board.attack_grid[row][col] = Board.BOARD_HIT;
			prevAIAttackResult = 1;
			playerTwoScore++;
		}
		else if(result.equals(Player.ATTACK_MISS)){
			players[1].board.attack_grid[row][col] = Board.BOARD_MISS;
			prevAIAttackResult = -1;
		}
		else {
			//
		}
		
		prevAIAttackRow = row;
		prevAIAttackCol = col;
		
		System.out.println("AI Attack Result For (" + row + "," + col + "): " + result);
	}
	
	/**
	 * Function to update game status
	 * @param board The new game status after attack
	 */
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
	
	/**
	 * Function to process attack
	 * @param row The input row
	 * @param col The input column
	 * @return The result of attack
	 */
	public String processAttack(int row, int col) {
		String result = "";
		if(getGameMode() == GAME_MODE_AI) {
			if(this.gameMode == Game.GAME_TYPE_REGULAR) {
				result = players[1].checkAttack(row, col);
				System.out.println("Player Attack Result For (" + row + "," + col + "): " + result);
				if(result.equals(Player.ATTACK_HIT)) {
					players[0].board.attack_grid[row][col] = Board.BOARD_HIT;
					synchronized (playerTurnTimer) {
						if(playerTurnTimer <= TURN_TIMER_MAX) {
							playerOneScore++;
						}
					}
					
				}
				else {
					players[0].board.attack_grid[row][col] = Board.BOARD_MISS;
					synchronized (playerTurnTimer) {
						if(playerTurnTimer > TURN_TIMER_MAX) {
							playerOneScore--;
						}
					}
				}
				updateGameStatus(players[1].board);
				gameScores = "Scores: 1) " + players[0].name + "(" + playerOneScore + ")" + " 2) AI (" + playerTwoScore + ")";
				if(isFinished == false) {
					generateAiAttack();
					synchronized (playerTurnTimer) {
						playerTurnTimer = 0;
					}
					updateGameStatus(players[0].board);
					gameScores = "Scores: 1) " + players[0].name + "(" + playerOneScore + ")" + " 2) AI (" + playerTwoScore + ")";
					if(isFinished == true) {
						winnerName = players[1].getName();
					}
				}
				else {
					winnerName = players[0].getName();
				}
			}
			else if(Game.gameMode == Game.GAME_TYPE_SALVA) {
				result = players[1].checkAttack(row, col);
				if(result.equals(Player.ATTACK_HIT)) {
					players[0].board.attack_grid[row][col] = Board.BOARD_HIT;
					synchronized (playerTurnTimer) {
						if(playerTurnTimer <= TURN_TIMER_MAX) {
							playerOneScore++;
						}
						synchronized (playerTurnTimer) {
							playerTurnTimer = 0;
						}
					}
				}
				else {
					players[0].board.attack_grid[row][col] = Board.BOARD_MISS;
					synchronized (playerTurnTimer) {
						if(playerTurnTimer > TURN_TIMER_MAX) {
							playerOneScore--;
						}
						synchronized (playerTurnTimer) {
							playerTurnTimer = 0;
						}
					}
				}
				
				salvaAttackRes.add(row + "#" + col + "#" + result.substring(11));
				if(salvaAttackRes.size() == players[0].numShipsAlive) {
					String results = "";
					
					for(int i = 0; i < players[0].numShipsAlive; i++) {
						results += salvaAttackRes.get(i) + " ";
					}
					salvaAttackRes.clear();
					updateGameStatus(players[1].board);
					gameScores = "Scores: 1) " + players[0].name + "(" + playerOneScore + ")" + " 2) AI (" + playerTwoScore + ")";
					
					if(isFinished == false) {
						for(int i = 0; i < players[1].numShipsAlive; i++) {
							generateAiAttack();
						}
						synchronized (playerTurnTimer) {
							playerTurnTimer = 0;
						}
						updateGameStatus(players[0].board);
						gameScores = "Scores: 1) " + players[0].name + "(" + playerOneScore + ")" + " 2) AI (" + playerTwoScore + ")";
						if(isFinished == true) {
							winnerName = players[1].getName();
						}
					}
					else {
						winnerName = players[0].getName();
					}
					
					return results;
				}
				else {
					return "Turn in process";
				}
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
	
	/**
	 * Function to get winner
	 * @return The winner name
	 */
	public static String getWinner() {
		return winnerName;
	}
	
	/**
	 * Function to get game status
	 * @return Game status
	 */
	public static boolean checkIfGameWon() {
		return isFinished;
	}
	
	/**
	 * The thread to keep track of turn time
	 */
	public Thread timer = new Thread() {
		public void run() {
			while(true) {
				try {
					Thread.sleep(1000);
					synchronized (playerTurnTimer) {
						playerTurnTimer++;
						System.out.println("Timer: " + playerTurnTimer);
						if(playerTurnTimer > Game.TURN_TIMER_MAX) {
							System.out.println("Timer Expired");
						}
					}
				}
				catch(Exception e) {
					
				}
			}
		}
	};
	
	/**
	 * Function to get player one score
	 * @return Player one scores
	 */
	public int getPlayerOneResults() {
		return playerOneScore;
	}
	
	/**
	 * Function to get player two score
	 * @return Player two scores
	 */
	public int getPlayerTwoResults() {
		return playerTwoScore;
	}
}
