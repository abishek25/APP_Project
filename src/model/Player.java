package model;

/**
 * This class is used to hold information about the player
 *
 */
public class Player {
	
	public static String ATTACK_HIT = "Attack was success";
	public static String ATTACK_MISS = "Attack was missed";
	
	String name;
	public Ship[] ships;
	Board board;
	int numShipsAlive;
	
	/**
	 * Function to initialize the ships
	 */
	public void initShips() {
		ships = new Ship[5];
		ships[0] = new Ship("Carrier", 5);
		ships[1] = new Ship("Battleship", 4);
		ships[2] = new Ship("Cruiser", 3);
		ships[3] = new Ship("Submarine", 3);
		ships[4] = new Ship("Destroyer", 2);
	}
	
	/**
	 * Place the ships on the placement board
	 * @param start The start position of ship
	 * @param end The end position of ship
	 * @param shipCtr The ship counter
	 */
	public void putShipOnBoard(String start, String end, int shipCtr) {
		int row = start.charAt(0) - 65;
		int col = Integer.parseInt(start.substring(1)) - 1;
		
		int row2 = end.charAt(0) - 65;
		int col2 = Integer.parseInt(end.substring(1)) - 1;
		
		if(row == row2) {
			int ctr = 0;
			for(int i = col; i <= col2; i++) {
				board.ship_placement_grid[row][i] = Board.PLACEMENT_BOARD_SHIP;
				ships[shipCtr].unfilledHoles[ctr++] = row + "" + i + ",";
			}
		}
		else if(col == col2) {
			for(int i = row; i <= row2; i++) {
				board.ship_placement_grid[i][col] = Board.PLACEMENT_BOARD_SHIP;
				ships[shipCtr].unfilledHoles[i] = i + "" + col + ",";
			}
		}
	}
	
	/**
	 * Constructor to get information about player
	 * @param name The player name
	 * @param positions The player positions
	 */
	public Player(String name, String positions) {
		this.board = new Board();
		this.name = name;
		initShips();

		positions = positions.replaceAll(" ", "");
		String[] pos = positions.split("\\(");
		int k = 0;
		for(int i = 0; i < pos.length; i++) {
			String[] info = pos[i].split(",");
			if(info.length < 2) {
				continue;
			}
			info[1] = info[1].substring(0, info[1].length() - 1);

			putShipOnBoard(info[0], info[1], k);
			k++;
		}
		numShipsAlive = 5;
	}
	
	/**
	 * Parameterized constructor with ships
	 * @param name Name of player
	 * @param board Game board
	 * @param playerShips Player's ships
	 */
	public Player(String name, Board board, String[] playerShips) {
		this.board = board;
		this.name = name;
		
		ships = new Ship[5];
		ships[0] = new Ship("Carrier", 5);
		ships[1] = new Ship("Battleship", 4);
		ships[2] = new Ship("Cruiser", 3);
		ships[3] = new Ship("Submarine", 3);
		ships[4] = new Ship("Destroyer", 2);
		
		for(int i = 0; i < 5; i++) {
			if(playerShips[i] == null) {
				break;
			}
			String[] pos = playerShips[i].split(",");
			for(int j = 0; j < ships[i].unfilledHoles.length; j++) {
				ships[i].unfilledHoles[j] = pos[j];
			}
		}
		
		numShipsAlive = 5;
	}
	
	/**
	 * Function to get player name
	 * @return Player name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Function to get game baord
	 * @return The game board
	 */
	public Board getBoard() {
		return board;
	}
	
	/**
	 * Function to check whether the ship has sunken or not.
	 * @param row Ship row co-ordinate
	 * @param col Ship column co-ordinate
	 */
	public void checkShips(int row, int col) {
		
		for(int i = 0; i < ships.length; i++) {
			for(int j = 0; j < ships[i].unfilledHoles.length; j++) {
				if(ships[i].unfilledHoles[j] == null) {
					continue;
				}
				if(ships[i].unfilledHoles[j].equals(row + "" + col)) {
					ships[i].unfilledHoles[j] = null;
				}
			}
		}
		
		this.numShipsAlive = 5;
		for(int i = 0; i < ships.length; i++) {
			boolean sunk = true;
			for(int j = 0; j < ships[i].unfilledHoles.length; j++) {
				if(ships[i].unfilledHoles[j] != null) {
					sunk = false;
				}
			}
			if(sunk == true) {
				numShipsAlive--;
			}
		}
		System.out.println("Ships Alive: " + numShipsAlive);
	}
	
	/**
	 * The function checks whether the attack is a hit or miss and updates the board accordingly.
	 * @param row The row co-ordinate of the attack
	 * @param col The column co-ordinate of the attack.
	 * @return The attack result
	 */
	public String checkAttack(int row, int col) {
		try {
			if(this.board.ship_placement_grid[row][col] == Board.PLACEMENT_BOARD_SHIP) {
				board.handleShipAttack(row, col);
				checkShips(row, col);
				return ATTACK_HIT;
			}
			else {
				return ATTACK_MISS;
			}
		}
		catch(Exception e) {
			System.out.println(row + ", " + col);
			e.printStackTrace();
		}
		return "FAILED";
	}
}
