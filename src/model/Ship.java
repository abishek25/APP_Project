package model;

/**
 * This class is used to hold information about the ship
 *
 */
public class Ship {
	String name;
	boolean holes[];
	int length;
	public boolean isHorizontal;
	
	int x; int y;
	
	/**
	 * Function to initialize the ship holes
	 * @param numHoles
	 */
	public void initHoles(int numHoles) {
		this.holes = new boolean[numHoles];
		for(int i = 0; i < numHoles; i++) {
			this.holes[i] = false;
		}
	}
	
	/**
	 * Function to check if ship sunk
	 * @return The success or failure
	 */
	public boolean isShipSunk() {
		for(int i = 0; i < holes.length; i++) {
			if(holes[i] == false) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Function to hit the ship
	 * @return The success or failure
	 */
	public boolean shipHit() {
		if(isShipSunk() == false) {
			return false;
		}
		for(int i = 0; i < holes.length; i++) {
			if(holes[i] == false) {
				holes[i] = true;
			}
		}
		return true;
	}
	
	/**
	 * Constructor to get ship information
	 * @param name The ship name
	 * @param numHoles The ship number of holes
	 */
	public Ship(String name, int numHoles) {
		this.name = name;
		this.initHoles(numHoles);
	}
	
	public Ship() {
		this.x = 0;
		this.y = 0;
		isHorizontal = true;	
	}
	
	public void setLength(int l) {
		this.length = l;
	}
	
	public int getLength() {
		return this.length;
	}
	
	public void addPosition(int x,int y) {
		this.x = x;
		this.y = y;
	}
}
