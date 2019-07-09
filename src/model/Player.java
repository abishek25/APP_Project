package model;

import java.util.StringTokenizer;

public class Player {
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
				board.ship_placement_grid[row][i] = true;
			}
		}
		else if(col == col2) {
			for(int i = col; i <= col2; i++) {
				board.ship_placement_grid[row][i] = true;
			}
		}
	}
	
	public Player(String name, String positions) {
		this.board = new Board();
		this.name = name;
		initShips();
		System.out.println(positions);
		positions = positions.replaceAll(" ", "");
		String[] pos = positions.split("\\(");
		for(int i = 0; i < pos.length; i++) {
			String[] info = pos[i].split(",");
			if(info.length < 2) {
				continue;
			}
			info[1] = info[1].substring(0, info[1].length() - 1);
			System.out.println(info[0] + " : " + info[1]);
			putShipOnBoard(info[0], info[1]);
		}
		
	}
	
	public String getName() {
		return name;
	}
	
	public Board getBoard() {
		return board;
	}
}
