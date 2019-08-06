package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * This class is used to hold information about the game in progress.
 */
public class Game implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public static int GAME_MODE_AI = 1;
	public static int GAME_MODE_NETWORK = 2;
	public static int GAME_MODE_INVALID = 3;
	
	public static int GAME_TYPE_REGULAR = 1;
	public static int GAME_TYPE_SALVA = 2;
	public static int TURN_TIMER_MAX = 5;
	
	public static Random rand = new Random();
	
	public Player[] players;
	int mode;
	public int gameMode;
	
	public static ArrayList<String> salvaAttackRes;
	
	int currPlayer;
	public static boolean isFinished;
	static String winnerName;
	
	int prevAIAttackResult;
	int prevAIAttackRow;
	int prevAIAttackCol;
	ArrayList<String> possiblePairs;
	
	public Integer playerTurnTimer;
	
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
		ArrayList<Integer> selected_rows = new ArrayList<Integer>();
		
		for(int i = 0; i < (Board.BOARD_ROWS); i++) {
			rows.add(i);
		}
		
		int[] shipSizes = {4,3,2,2,1};
		String aiPositions = "";
		for(int i = 0; i < shipSizes.length; i++) {
			if(rows.size() == 0) {
				
			}
			Collections.shuffle(rows);
			int row = rows.remove(0);
			selected_rows.add(row);
			for(int k = 0; k < rows.size(); k++) {
				int check_row = row + 1;
				int check_row_2 = row - 1;
				if(rows.contains(check_row)) {
					rows.remove(new Integer(check_row));
					k--;
				}
				
				if(rows.contains(check_row_2)) {
					rows.remove(new Integer(check_row_2));
					k--;
				}
				if(k < -1) {
					break;
				}
			}
			int col = rand.nextInt(Board.BOARD_COLS - shipSizes[i]) + 1;
			aiPositions = aiPositions + "(" + ((char)(row + 65)) + col + "," + ((char)(row + 65)) + (col + shipSizes[i]) + "),";
			System.out.println(aiPositions);
		}
		aiPositions = aiPositions.substring(0, aiPositions.length() - 1);
		
		return aiPositions;
	}
	
	/**
	 * The constructor to set/initialize information about the game and also start the timer
	 * @param playerOneName The player name
	 * @param board The game board
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
		possiblePairs = new ArrayList<String>();
		playerOneScore = 0;
		playerTwoScore = 0;
		playerTurnTimer = 0;
		timer.start();
	}
	
	/**
	 * Constructor to set/initialize information about two players
	 * @param playerOneName Player 1 name
	 * @param player1Positions Player 1 positions
	 * @param playerTwoName Player 2 name
	 * @param player2Positions Player 2 positions
	 */
	public Game(String playerOneName, Board board, int gameMode, String[] playerShips, String playerTwoName, Board board2, String[] player2Ships) {
		players = new Player[2];
		players[0] = new Player(playerOneName, board, playerShips);
		players[1] = new Player(playerTwoName, board2, player2Ships);
		mode = GAME_MODE_NETWORK;
		this.gameMode = gameMode;
		currPlayer = 0;
		isFinished = false;
		salvaAttackRes = new ArrayList<String>();
		possiblePairs = new ArrayList<String>();
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
	 * Function to get AI board
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
	 * Function generates attack of AI and also  updates the board according to the success or failure of the attack.
	 * @return result The attack result(hit or miss)
	 */
	public String generateAiAttack() {
		int i1 = 0;
		int j1 = 0;
		
		if(prevAIAttackResult == 1) {
			possiblePairs = new ArrayList<String>();
			if(prevAIAttackCol < (Board.BOARD_COLS - 1)) {
				i1 = prevAIAttackRow;
				j1 = prevAIAttackCol + 1;
				if(players[1].board.attack_grid[i1][j1] == Board.BOARD_EMPTY) {
					possiblePairs.add(String.valueOf(i1) + "," + String.valueOf(j1));
				}
			}
			if(prevAIAttackCol > 0) {
				i1 = prevAIAttackRow;
				j1 = prevAIAttackCol - 1;
				if(players[1].board.attack_grid[i1][j1] == Board.BOARD_EMPTY) {
					possiblePairs.add(String.valueOf(i1) + "," + String.valueOf(j1));
				}
			}
			if(prevAIAttackRow < (Board.BOARD_ROWS - 1)) {
				i1 = prevAIAttackRow + 1;
				j1 = prevAIAttackCol;
				if(players[1].board.attack_grid[i1][j1] == Board.BOARD_EMPTY) {
					possiblePairs.add(String.valueOf(i1) + "," + String.valueOf(j1));
				}
			}
			if(prevAIAttackRow > 0) {
				i1 = prevAIAttackRow - 1;
				j1 = prevAIAttackCol;
				if(players[1].board.attack_grid[i1][j1] == Board.BOARD_EMPTY) {
					possiblePairs.add(String.valueOf(i1) + "," + String.valueOf(j1));
				}
			}
		}
		
		if(possiblePairs.size() == 0) {
			for(int i = 0; i < Board.BOARD_ROWS; i++) {
				for(int j = 0; j < Board.BOARD_COLS; j++) {
					if(players[1].board.attack_grid[i][j] == Board.BOARD_EMPTY) {
						possiblePairs.add(String.valueOf(i) + "," + String.valueOf(j));
					}
				}
			}
		}
		
		Collections.shuffle(possiblePairs);
		int row = Integer.parseInt(possiblePairs.get(0).split(",")[0]);
		int col = Integer.parseInt(possiblePairs.get(0).split(",")[1]);
		possiblePairs.remove(0);
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
		return row + "#" + col + "#" + result;
	}
	
	/**
	 * Function to check whether there is any ship left on the board.
	 * @param board The board which needs to be checked
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
		
		gameScores = "Scores: 1) " + players[0].name + "(" + playerOneScore + ")" + " 2) AI (" + playerTwoScore + ")";
		
		if(rt == false) {
			isFinished = true;
		}
	}
	
	/**
	 * The function to handle the successful attack immediately for basic game alongwith score.
	 * @param row The row co-ordinate
	 * @param col The column co-ordinate
	 * @param attackGrid Board on which attack result needs to be updated
	 */
	public void processRegularAttackHit(int row, int col, int[][] attackGrid) {
		attackGrid[row][col] = Board.BOARD_HIT;
		
		synchronized (playerTurnTimer) {
			if(playerTurnTimer <= TURN_TIMER_MAX) {
				playerOneScore++;
			}
		}
	}
	
	/**
	 * The function to handle the unsuccessful attack immediately for basic game alongwith score.
	 * @param row The row co-ordinate
	 * @param col The column co-ordinate
	 * @param attackGrid Board on which attack result needs to be updated
	 */
	public void processRegularAttackMiss(int row, int col, int[][] attackGrid) {
		attackGrid[row][col] = Board.BOARD_MISS;
		
		synchronized (playerTurnTimer) {
			if(playerTurnTimer > TURN_TIMER_MAX) {
				playerOneScore--;
			}
		}
	}
	
	/**
	 * The function to process the result of the attack.
	 * @param row The row co-ordinate
	 * @param col The column co-ordinate
	 * @param result The result of the attack
	 */
	public void processRegularAttackResult(int row, int col, String result) {
		if(result.equals(Player.ATTACK_HIT)) {
			processRegularAttackHit(row, col, players[0].board.attack_grid);
		}
		else {
			processRegularAttackMiss(row, col, players[0].board.attack_grid);
		}
	}
	
	/**
	 * The function to reset the timer of the player after attack.
	 */
	public void resetPlayerTimer() {
		synchronized (playerTurnTimer) {
			playerTurnTimer = 0;
		}
	}
	
	/**
	 * Function to check attack for basic game.
	 * @param row The row co-ordinate of the attack
	 * @param col The column co-ordinate of the attack
	 * @return result The result whether it is hit or miss
	 */
	public String regularAttack(int row, int col) {
		String result = players[1].checkAttack(row, col);
		System.out.println("Player Attack Result For (" + row + "," + col + "): " + result);
		processRegularAttackResult(row, col, result);
		updateGameStatus(players[1].board);

		if(isFinished == false) {
			generateAiAttack();
			resetPlayerTimer();
			updateGameStatus(players[0].board);
			
			if(isFinished == true) {
				winnerName = players[1].getName();
			}
		}
		else {
			winnerName = players[0].getName();
		}
		
		return result;
	}
	
	/**
	 * The function to handle the successful attack immediately for salva game alongwith score.
	 * @param row The row co-ordinate
	 * @param col The column co-ordinate
	 * @param attackGrid Board on which attack result needs to be updated
	 */
	public void processSalvaAttackHit(int row, int col, int[][] attackGrid) {
		attackGrid[row][col] = Board.BOARD_HIT;
		synchronized (playerTurnTimer) {
			if(playerTurnTimer <= TURN_TIMER_MAX) {
				playerOneScore++;
			}
			playerTurnTimer = 0;
		}
	}
	
	/**
	 * The function to handle the unsuccessful attack immediately for salva game alongwith score.
	 * @param row The row co-ordinate
	 * @param col The column co-ordinate
	 * @param attackGrid Board on which attack result needs to be updated
	 */
	public void processSalvaAttackMiss(int row, int col, int[][] attackGrid) {
		attackGrid[row][col] = Board.BOARD_MISS;
		synchronized (playerTurnTimer) {
			if(playerTurnTimer > TURN_TIMER_MAX) {
				playerOneScore--;
			}
			playerTurnTimer = 0;
		}
	}
	
	/**
	 * Function to check attack for Salva variation.
	 * @param row The row co-ordinate of the attack
	 * @param col The column co-ordinate of the attack
	 * @return result The result whether it is hit or miss
	 */
	public String salvaAttack(int row, int col) {
		String result = players[1].checkAttack(row, col);
		if(result.equals(Player.ATTACK_HIT)) {
			processSalvaAttackHit(row, col, players[0].board.attack_grid);
		}
		else {
			processSalvaAttackMiss(row, col, players[0].board.attack_grid);
		}
		salvaAttackRes.add(row + "#" + col + "#" + result.substring(11));
		
		if(salvaAttackRes.size() == players[0].numShipsAlive) {
			String results = "";
			
			for(int i = 0; i < players[0].numShipsAlive; i++) {
				results += salvaAttackRes.get(i) + " ";
			}
			
			salvaAttackRes.clear();
			updateGameStatus(players[1].board);
			
			if(isFinished == false) {
				for(int i = 0; i < players[1].numShipsAlive; i++) {
					generateAiAttack();
				}
				resetPlayerTimer();
				updateGameStatus(players[0].board);

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
	
	/**
	 * Function to processes the attack based on game mode.
	 * @param row The input row
	 * @param col The input column
	 * @return The result of attack
	 */
	public String processAttack(int row, int col) {
		String result = "";
		if(getGameMode() == GAME_MODE_AI) {
			if(this.gameMode == Game.GAME_TYPE_REGULAR) {
				result = regularAttack(row, col);
			}
			else if(this.gameMode == Game.GAME_TYPE_SALVA) {
				result = salvaAttack(row, col);
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
	 * Function to check whether the game is finished or not.
	 * @return isFinished Current status of the game.
	 */
	public static boolean checkIfGameWon() {
		return isFinished;
	}
	
	/**
	 * The thread to keep track of turn time
	 */
	public transient Thread timer = new Thread() {
		public void run() {
			while(true) {
				try {
					Thread.sleep(1000);
					synchronized (playerTurnTimer) {
						playerTurnTimer++;
						if(playerTurnTimer > Game.TURN_TIMER_MAX) {
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
	
	public int getGameTypeMode() {
		return gameMode;
	}
}
