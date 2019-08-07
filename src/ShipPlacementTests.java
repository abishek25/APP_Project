import static org.junit.jupiter.api.Assertions.*;

import javax.swing.JButton;
import javax.swing.JLabel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import controller.GameController;
import model.Board;
import view.Window;

class ShipPlacementTests {

	Window win;
	
	@BeforeEach 
	public void setup() {
		win = new Window();
		win.lblAttackGrid = new JButton[Board.BOARD_ROWS][Board.BOARD_COLS];
		win.lblShipPlacementGrid = new JLabel[Board.BOARD_ROWS][Board.BOARD_COLS];
		
		for(int i = 0; i < Board.BOARD_ROWS; i++) {
			for(int j = 0; j < Board.BOARD_COLS; j++) {
				win.lblAttackGrid[i][j] = new JButton("");
				win.lblShipPlacementGrid[i][j] = new JLabel("-----");
			}
		}
		
		win.gameController = new GameController(1, "TestPlayer");
		win.board = new Board();
		win.playerShips = new String[5];
		Window.DEV_TEST = 5;
	}
	
	/**
	 * Test one to vertical ship placement
	 */
	@Test
	void testShipPlacementVertical() {
		String shipText = "SHIP#SHIP#SHIP#SHIP#SHIP";
		win.processShipPlacementVertically(0, 0, shipText);
		
		int i = 0;
		for(int j = 0; j < 5; j++) {
			assertEquals("SHIP", win.lblShipPlacementGrid[i][j].getText());
		}
	}
	
	/**
	 * Test one to check horizontal ship placement
	 */
	@Test
	void testShipPlacementHorizontal() {
		String shipText = "SHIP#SHIP#SHIP#SHIP#SHIP";
		win.processShipPlacementHorizontally(0, 0, shipText);
		
		int i = 0;
		for(int j = 0; j < 5; j++) {
			assertEquals("SHIP", win.lblShipPlacementGrid[j][i].getText());
		}
	}
	
	/**
	 * Test the placement of ship one on other
	 */
	@Test
	void testShipPlacementConstraintOne() {
		String shipText = "SHIP#SHIP#SHIP#SHIP#SHIP";
		win.processShipPlacementHorizontally(0, 0, shipText);
		Boolean result = win.processShipPlacementHorizontally(0, 0, shipText);
		assertEquals(result, false);
	}
	
	/**
	 * Test the placement of ship in same row
	 */
	@Test
	void testShipPlacementConstraintTwo() {
		String shipText = "SHIP#SHIP#SHIP#SHIP#SHIP";
		win.processShipPlacementHorizontally(0, 0, shipText);
		shipText = "SHIP#SHIP#SHIP#SHIP";
		Boolean result = win.processShipPlacementHorizontally(5, 0, shipText);
		assertEquals(result, false);
	}
	
	/**
	 * Test the placement of ship outside board
	 */
	@Test
	void testShipPlacementConstraintThree() {
		String shipText = "SHIP#SHIP#SHIP#SHIP#SHIP";
		Boolean result = win.processShipPlacementHorizontally(9, 0, shipText);
		assertEquals(result, false);
	}
	
	/**
	 * Test the AI Ship Placement
	 */
	@Test
	void testAIShipPlacement() {
		win.gameController.createGame("Tesst", win.board, win.playerShips, null, null);
		String positions = win.gameController.getGame().randomShipPositions();
		assertTrue(positions.split(",").length == 10);
	}

}
