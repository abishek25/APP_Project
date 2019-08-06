import static org.junit.jupiter.api.Assertions.*;

import javax.swing.JButton;
import javax.swing.JLabel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import controller.GameController;
import model.Board;
import model.Game;
import model.Player;
import model.Ship;
import view.Window;

/**
 * It tests the code for integrity
 *
 */
class BattleShipTests {
	
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
		for(int j = 0; j<0; j++) {
			assertEquals("SHIP", win.lblShipPlacementGrid[i][j]);
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
		for(int j = 0; j<0; j++) {
			assertEquals("SHIP", win.lblShipPlacementGrid[j][i]);
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
		win.gameController.createGame("Tesst", win.board, win.playerShips);
		String positions = win.gameController.getGame().randomShipPositions();
		assertTrue(positions.split(",").length == 10);
	}
	
	/**
	 * Test the AI Attack
	 */
	@Test
	void testAIAttack() {
		win.gameController.createGame("Tesst", win.board, win.playerShips);
		String result = win.gameController.getGame().generateAiAttack();
		assertTrue(result.split("#").length == 3);
	}
	
	/**
	 * Test startup player turn
	 */
	@Test
	void testStartUpPlayerTurn() {
		win.gameController.createGame("Tesst", win.board, win.playerShips);
		String playerName = win.gameController.getGame().getCurrPlayer().getName();
		assertEquals(playerName, "Tesst");
	}
	
	/**
	 * Test startup player scores
	 */
	@Test
	void testStartUpPlayerScores() {
		win.gameController.createGame("Tesst", win.board, win.playerShips);
		assertEquals(win.gameController.getGame().getPlayerOneResults(), 0);
		assertEquals(win.gameController.getGame().getPlayerTwoResults(), 0);
	}
	
	/**
	 * Test startup game mode
	 */
	@Test
	void testStartUpGameMode() {
		win.gameController.createGame("Tesst", win.board, win.playerShips);
		assertEquals(win.gameController.getGame().getGameMode(), Game.GAME_MODE_AI);
	}
	
	/**
	 * Test startup players
	 */
	@Test
	void testStartUpPlayers() {
		win.gameController.createGame("Tesst", win.board, win.playerShips);
		assertEquals(win.gameController.getGame().players.length, 2);
	}
	
	/**
	 * Test startup Ship Placement Grid Creation
	 */
	@Test
	void testStartUpBoardCreationOne() {
		Board b = new Board();
		for(int i = 0; i < Board.BOARD_ROWS; i++) {
			for(int j = 0; j < Board.BOARD_COLS; j++) {
				assertEquals(b.ship_placement_grid[i][j], Board.BOARD_EMPTY);
			}
		}
	}
	
	/**
	 * Test Startup Attack Grid Creation
	 */
	@Test
	void testStartUpBoardCreationTwo() {
		Board b = new Board();
		for(int i = 0; i < Board.BOARD_ROWS; i++) {
			for(int j = 0; j < Board.BOARD_COLS; j++) {
				assertEquals(b.getAttackGrid()[i][j], Board.BOARD_EMPTY);
			}
		}
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
	 * Test startup UI Creation File Menu
	 */
	@Test
	void testStartupFileMenuCreation() {
		Window win = new Window();
		assertNotNull(win.createFileMenu());
	}
	
	/**
	 * Test startup UI Creation Menu Bar
	 */
	@Test
	void testStartUpMenuBarCreation() {
		Window win = new Window();
		assertNotNull(win.createMenu());
	}
	
	/**
	 * Test Startup Player Ship creation
	 */
	@Test
	void testPlayerShipCreation() {
		Window win = new Window();
		win.initMainFrame();
		assertEquals(win.playerShips.length, 5);
	}
	
	/**
	 * Test game winner
	 */
	@Test
	void testGameWinner() {
		win.gameController.createGame("Tesst", win.board, win.playerShips);
		for(int i = 0; i < Board.BOARD_ROWS; i++) {
			for(int j = 0; j < Board.BOARD_COLS; j++) {
				win.gameController.getGame().processAttack(i, j);
				if(Game.isFinished == true) {
					break;
				}
			}
		}
		assertNotNull(win.gameController.getGame().getWinner());
	}
	
	/**
	 * Test game attack changing ship placement grid
	 */
	@Test
	void testGameAttackOne() {
		win.gameController.createGame("Tesst", win.board, win.playerShips);
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
		win.gameController.createGame("Tesst", win.board, win.playerShips);
		win.gameController.getGame().processRegularAttackHit(0,0, win.gameController.getGame().getGameBoard().getAttackGrid());
		assertEquals(win.gameController.getGame().getGameBoard().getAttackGrid()[0][0], Board.BOARD_HIT);
	}
	
	/**
	 * Test game attack reset player turn timer
	 */
	@Test
	void testGameAttackThree() {
		win.gameController.createGame("Tesst", win.board, win.playerShips);
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
		win.gameController.createGame("Tesst", win.board, win.playerShips);
		win.gameController.getGame().processRegularAttackHit(0,0, win.gameController.getGame().getGameBoard().getAttackGrid());
		assertEquals(win.gameController.getGame().getPlayerOneResults(), 1);
	}
	
	/**
	 * Test game attack player score decrement
	 */
	@Test
	void testGameAttackFive() {
		win.gameController.createGame("Tesst", win.board, win.playerShips);
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
		win.gameController.createGame("Tesst", win.board, win.playerShips);
		win.gameController.getGame().processRegularAttackMiss(0,0, win.gameController.getGame().getGameBoard().getAttackGrid());
		assertEquals(win.gameController.getGame().getGameBoard().getAttackGrid()[0][0], Board.BOARD_MISS);
	}
	
	/**
	 * Test game attack changing ship placement grid for miss
	 */
	@Test
	void testGameAttackSeven() {
		win.gameController.createGame("Tesst", win.board, win.playerShips);
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
