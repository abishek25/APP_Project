package model;

import java.io.Serializable;

/**
 * This class is used to hold information about the ship
 *
 */
public class Ship implements Serializable {
	private static final long serialVersionUID = 1L;
	String name;
	public boolean holes[];
	public String unfilledHoles[];
	
	/**
	 * Function to initialize the ship holes
	 * @param numHoles The number of holes
	 */
	public void initHoles(int numHoles) {
		this.holes = new boolean[numHoles];
		for(int i = 0; i < numHoles; i++) {
			this.holes[i] = false;
		}
	}
	
	/**
	 * Function to check if ship sunk
	 * @return true if all holes are not filled else false
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
	 * @return false if ship already sunken else true
	 */
	public boolean shipHit() {
		if(isShipSunk() == true) {
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
	 * Parameterized constructor to set ship information
	 * @param name The ship name
	 * @param numHoles The ship number of holes
	 */
	public Ship(String name, int numHoles) {
		this.name = name;
		this.initHoles(numHoles);
		this.unfilledHoles = new String[numHoles];
	}
}
