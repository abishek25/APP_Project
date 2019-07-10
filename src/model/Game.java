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
	
	public static Random rand = new Random();
	
	Player[] players;
	int mode;
	int currPlayer;
	static boolean isFinished;
	static String winnerName;
	
	/**
	 * This function checks positions passed for placement of ship
	 * @param positions of ship
	 * @return Success or failure
	 */
	public static boolean checkPositions(String positions) {
		positions = positions.replaceAll(" ", "");
		String[] pos = positions.split("\\(");
		ArrayList<String> visitedPos = new ArrayList<>();   //Stores the co-ordinates of the ships which are placed
		ArrayList<String> occupiedPos = new ArrayList<>();
		
		if(positions.split(",").length != 10) {
			JOptionPane.showMessageDialog(new JFrame(), "All the co-ordinates not mentioned");
			return false;
		}
			
		for(int i = 0; i < pos.length; i++) {
			String[] info = pos[i].split(",");
			if(info.length < 2) {
				continue;
			}
			info[1] = info[1].substring(0, info[1].length() - 1);
			
			/**
			 * Checks whether are placed inside the grid or not
			 */
			if((info[0].charAt(0) < 'A' || info[0].charAt(0) > 'K') || (info[0].charAt(1) < '1' || info[0].charAt(1) > '9')){
				if((info[1].charAt(0) < 'A' || info[1].charAt(0) > 'K') || (info[1].charAt(1) < '1' || info[1].charAt(1) > '9')) {
					return false;
				}
				return false;
			}
			int startPos=0, endPos = 0;
			if(info[0].charAt(0)==info[1].charAt(0)) {
				startPos = Character.getNumericValue(info[0].charAt(1));
				endPos = Character.getNumericValue(info[1].charAt(1));
			}
			else {
				startPos = info[0].charAt(0) - 64;
				endPos = info[1].charAt(0) - 64;
			}
			
			int lengthShip=-1;
			switch(i) {
			case 1:
				lengthShip = 4;
				break;
			case 2:
				lengthShip = 3;
				break;
			case 3:
				lengthShip = 2;
				break;
			case 4:
				lengthShip = 2;
				break;
			case 5:
				lengthShip = 1;
				break;
			}
			
			/**
			 * Checks whether length of ships of the input positions matches or not
			 */
			if(endPos - startPos != lengthShip) {
				JOptionPane.showMessageDialog(new JFrame(), "Position for length of ships does not match");
				return false;
			}
			
			/**
			 * Checks whether ships are overlapping or not
			 */
			if(!visitedPos.isEmpty()) {
				if(visitedPos.contains(info[0]) || visitedPos.contains(info[1])) { 
					JOptionPane.showMessageDialog(new JFrame(), "Ships overlapping each other");
					return false;
				}
			}
			
			/**
			 * Adding all the co-ordinates in which ships are placed for checking overlapping conditions
			 */
			if(info[0].charAt(0)==info[1].charAt(0)) {
				int tempPos = Character.getNumericValue(info[0].charAt(1));
				int stopPos = Character.getNumericValue(info[1].charAt(1));
				while(tempPos<=stopPos) {
					String temp = info[0].substring(0, 1);
					temp += tempPos;
					visitedPos.add(temp);
					tempPos++;
				}	
			}
			else {
				int tempPos = info[0].charAt(0);
				int stopPos = info[1].charAt(0);
				while(tempPos<=stopPos) {
					String temp = "";
					char c = (char)tempPos;
					temp += c;
					temp += info[0].substring(1);
					visitedPos.add(temp);
					tempPos++;
				}	
			}
			
			/**
			 * Checks whether the ships are placed beside each other or not
			 */
			if(!visitedPos.isEmpty()) {
				for(int j=0;j<visitedPos.size();j++) {
					String placedShip = visitedPos.get(j);
					if(info[0].charAt(0)==info[1].charAt(0)) {
						int placedPos = placedShip.charAt(0);
						int currPos = info[0].charAt(0);
						if(currPos==placedPos + 1 || currPos == placedPos - 1) {
							JOptionPane.showMessageDialog(new JFrame(), "Ships cannot be beside each other");
							return false;
						}
					}
					else {
						int placedPos = Character.getNumericValue(placedShip.charAt(1));
						int currPos = Character.getNumericValue(info[1].charAt(1));
						if(currPos == placedPos + 1 || currPos == placedPos - 1) {
							JOptionPane.showMessageDialog(new JFrame(), "Ships cannot be beside each other");
							return false;
						}
					}
				}
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
	 */
	public Game(String playerOneName, String player1Positions) {
		players = new Player[2];
		players[0] = new Player(playerOneName, player1Positions);
		players[1] = new Player("AI", randomShipPositions());
		mode = GAME_MODE_AI;
		currPlayer = 0;
		isFinished = false;
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
}
