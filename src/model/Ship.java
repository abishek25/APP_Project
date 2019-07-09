package model;

public class Ship {
	String name;
	boolean holes[];
	
	public void initHoles(int numHoles) {
		this.holes = new boolean[numHoles];
		for(int i = 0; i < numHoles; i++) {
			this.holes[i] = false;
		}
	}
	
	public boolean isShipSunk() {
		for(int i = 0; i < holes.length; i++) {
			if(holes[i] == false) {
				return false;
			}
		}
		return true;
	}
	
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
	
	public Ship(String name, int numHoles) {
		this.name = name;
		this.initHoles(numHoles);
	}
}
