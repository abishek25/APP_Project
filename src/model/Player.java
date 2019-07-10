package model;


public class Player {
	
	public static String ATTACK_HIT = "Attack was success";
	public static String ATTACK_MISS = "Attack was missed";
	
	String name;
	Ship[] ships;
	Board board;
	
	public void initShips() {
		ships = new Ship[5];
		ships[0] = new Ship("Carrier", 5);
		ships[1] = new Ship("Battleship", 4);
		ships[2] = new Ship("Cruiser", 3);
		ships[3] = new Ship("Submarine", 3);
		ships[4] = new Ship("Destroyer", 2);
	}
	
	public void putShipOnBoard(String start, String end) {
		int row = start.charAt(0) - 65;
		int col = Integer.parseInt(start.substring(1)) - 1;
		
		int row2 = end.charAt(0) - 65;
		int col2 = Integer.parseInt(end.substring(1)) - 1;
		
		if(row == row2) {
			for(int i = col; i <= col2; i++) {
				board.ship_placement_grid[row][i] = Board.PLACEMENT_BOARD_SHIP;
			}
		}
		else if(col == col2) {
			for(int i = col; i <= col2; i++) {
				board.ship_placement_grid[row][i] = Board.PLACEMENT_BOARD_SHIP;
			}
		}
	}
	
	public Player(String name, String positions) {
		this.board = new Board();
		this.name = name;
		initShips();

		positions = positions.replaceAll(" ", "");
		String[] pos = positions.split("\\(");
		for(int i = 0; i < pos.length; i++) {
			String[] info = pos[i].split(",");
			if(info.length < 2) {
				continue;
			}
			info[1] = info[1].substring(0, info[1].length() - 1);

			putShipOnBoard(info[0], info[1]);
		}
	}
	
	public String getName() {
		return name;
	}
	
	public Board getBoard() {
		return board;
	}
	
	public String checkAttack(int row, int col) {
		try {
			if(this.board.ship_placement_grid[row][col] == Board.PLACEMENT_BOARD_SHIP) {
				board.handleShipAttack(row, col);
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
