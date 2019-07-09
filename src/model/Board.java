package model;

public class Board {
	
	public static int BOARD_EMPTY = 0;
	public static int BOARD_MISS = 1;
	public static int BOARD_HIT = 2;
	
	public static int BOARD_ROWS = 11;
	public static int BOARD_COLS = 9;
	
	int attack_grid[][];
	boolean ship_placement_grid[][];
	
	public void init_ship_placement() {
		this.ship_placement_grid = new boolean[BOARD_ROWS][BOARD_COLS];
		for(int i = 0; i < BOARD_ROWS; i++) {
			for(int j = 0; j < BOARD_COLS; j++) {
				this.ship_placement_grid[i][j] = false;
			}
		}
	}
	
	public void init_attack_grid() {
		this.attack_grid = new int[BOARD_ROWS][BOARD_COLS];
		for(int i = 0; i < BOARD_ROWS; i++) {
			for(int j = 0; j < BOARD_COLS; j++) {
				this.attack_grid[i][j] = BOARD_EMPTY;
			}
		}
	}
	
	public Board() {
		init_ship_placement();
		init_attack_grid();
	}
	
	public int[][] getAttackGrid() {
		return attack_grid;
	}
	
	public boolean[][] getShipPlacementGrid() {
		return ship_placement_grid;
	}
}
