import static org.junit.jupiter.api.Assertions.*;

import javax.swing.JButton;
import javax.swing.JLabel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import controller.GameController;
import model.Board;
import view.Window;

/**
 * This class tests for attack related cases
 *
 */
class AttackTests {

	Window win;

	/**
	 * Setup function to run before reach test case
	 */
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
	 * Test the AI Attack
	 */
	@Test
	void testAIAttack() {
		win.gameController.createGame("Tesst", win.board, win.playerShips, null, null);
		String result = win.gameController.getGame().generateAiAttack();
		assertTrue(result.split("#").length == 3);
	}
	
	/**
	 * Test Attack on ship
	 */
	@Test
	void testShipAttack() {
		Board b = new Board();
		b.handleShipAttack(0, 0);
		assertEquals(b.ship_placement_grid[0][0], Board.PLACEMENT_BOARD_SHIP_HIT);
	}
	
	/**
	 * Test game attack changing ship placement grid
	 */
	@Test
	void testGameAttackOne() {
		win.gameController.createGame("Tesst", win.board, win.playerShips, null, null);
		boolean attackSuccess = false;
		
		for(int i = 0; i < Board.BOARD_ROWS; i++) {
			for(int j = 0; j < Board.BOARD_COLS; j++) {
				win.gameController.getGame().processAttack(i, j);
				if(win.gameController.getGame().getAiBoard().ship_placement_grid[i][j] == Board.PLACEMENT_BOARD_SHIP_HIT) {
					attackSuccess = true;
				}
			}
		}
		
		assertTrue(attackSuccess);
	}
	
	/**
	 * Test game attack changing attack grid
	 */
	@Test
	void testGameAttackTwo() {
		win.gameController.createGame("Tesst", win.board, win.playerShips, null, null);
		win.gameController.getGame().processRegularAttackHit(0,0, win.gameController.getGame().getGameBoard().getAttackGrid());
		assertEquals(win.gameController.getGame().getGameBoard().getAttackGrid()[0][0], Board.BOARD_HIT);
	}
	
	/**
	 * Test game attack reset player turn timer
	 */
	@Test
	void testGameAttackThree() {
		win.gameController.createGame("Tesst", win.board, win.playerShips, null, null);
		win.gameController.getGame().processRegularAttackHit(0,0, win.gameController.getGame().getGameBoard().getAttackGrid());
		synchronized (win.gameController.getGame().playerTurnTimer) {
			assertTrue(win.gameController.getGame().playerTurnTimer < 2);
		}
	}
	
	/**
	 * Test game attack player score increment
	 */
	@Test
	void testGameAttackFour() {
		win.gameController.createGame("Tesst", win.board, win.playerShips, null, null);
		win.gameController.getGame().processRegularAttackHit(0,0, win.gameController.getGame().getGameBoard().getAttackGrid());
		assertEquals(win.gameController.getGame().getPlayerOneResults(), 1);
	}
	
	/**
	 * Test game attack player score decrement
	 */
	@Test
	void testGameAttackFive() {
		win.gameController.createGame("Tesst", win.board, win.playerShips, null, null);
		synchronized (win.gameController.getGame().playerTurnTimer) {
			win.gameController.getGame().playerTurnTimer = 7;
		}
		
		win.gameController.getGame().processRegularAttackMiss(0,0, win.gameController.getGame().getGameBoard().getAttackGrid());
		assertEquals(win.gameController.getGame().getPlayerOneResults(), -1);
	}
	
	/**
	 * Test game attack changing attack grid MISS
	 */
	@Test
	void testGameAttackSix() {
		win.gameController.createGame("Tesst", win.board, win.playerShips, null, null);
		win.gameController.getGame().processRegularAttackMiss(0,0, win.gameController.getGame().getGameBoard().getAttackGrid());
		assertEquals(win.gameController.getGame().getGameBoard().getAttackGrid()[0][0], Board.BOARD_MISS);
	}
	
	/**
	 * Test game attack changing ship placement grid for miss
	 */
	@Test
	void testGameAttackSeven() {
		win.gameController.createGame("Tesst", win.board, win.playerShips, null, null);
		boolean missSuccess = false;
		
		for(int i = 0; i < Board.BOARD_ROWS; i++) {
			for(int j = 0; j < Board.BOARD_COLS; j++) {
				String result = win.gameController.getGame().processAttack(i, j);
				if(result.contains("miss")) {
					missSuccess = true;
				}
			}
		}
		
		assertTrue(missSuccess);
	}

}
