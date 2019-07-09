package model;

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
	
	public Player(String name) {
		this.name = name;
		initShips();
		this.board = new Board();
	}
	
	public Board getBoard() {
		return board;
	}
}
