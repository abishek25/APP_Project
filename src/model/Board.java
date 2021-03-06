package model;

import java.io.Serializable;

/**
 * This class is the model for boards
 * Ship placement and Attack Board.
 */
public class Board implements Serializable {

	private static final long serialVersionUID = 1L;
	public static int BOARD_EMPTY = 0;
	public static int BOARD_MISS = 1;
	public static int BOARD_HIT = 2;
	
	public static int PLACEMENT_BOARD_EMPTY = 0;
	public static int PLACEMENT_BOARD_SHIP = 1;
	public static int PLACEMENT_BOARD_SHIP_HIT = 2;
	
	public static int BOARD_ROWS = 11;
	public static int BOARD_COLS = 9;
	
	int attack_grid[][];
	public int ship_placement_grid[][];
	
	/**
	 * The function to initialize the board of player.
	 */
	public void init_ship_placement() {
		this.ship_placement_grid = new int[BOARD_ROWS][BOARD_COLS];
		for(int i = 0; i < BOARD_ROWS; i++) {
			for(int j = 0; j < BOARD_COLS; j++) {
				this.ship_placement_grid[i][j] = PLACEMENT_BOARD_EMPTY;
			}
		}
	}
	
	/**
	 * The function to initialize the board of the AI.
	 */
	public void init_attack_grid() {
		this.attack_grid = new int[BOARD_ROWS][BOARD_COLS];
		for(int i = 0; i < BOARD_ROWS; i++) {
			for(int j = 0; j < BOARD_COLS; j++) {
				this.attack_grid[i][j] = BOARD_EMPTY;
			}
		}
	}
	
	/**
	 * The constructor for the class
	 */
	public Board() {
		init_ship_placement();
		init_attack_grid();
	}
	
	/**
	 * The function returns the attack grid
	 * @return The attack grid
	 */
	public int[][] getAttackGrid() {
		return attack_grid;
	}
	
	/**
	 * The function to return ship placement grid
	 * @return The ship placement grid
	 */
	public int[][] getShipPlacementGrid() {
		return ship_placement_grid;
	}
	
	/**
	 * The function to handle the attack on ship
	 * @param row The row co-ordinate
	 * @param col The column co-ordinate
	 */
	public void handleShipAttack(int row, int col) {
		this.ship_placement_grid[row][col] = PLACEMENT_BOARD_SHIP_HIT;
	}
}
