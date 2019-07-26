package model;

/**
 * This class is used to hold information about the ship
 *
 */
public class Ship {
	String name;
	boolean holes[];
	public String unfilledHoles[];
	
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
		this.unfilledHoles = new String[numHoles];
	}
}
