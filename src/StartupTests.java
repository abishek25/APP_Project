import static org.junit.jupiter.api.Assertions.*;

import javax.swing.JButton;
import javax.swing.JLabel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import controller.GameController;
import model.Board;
import model.Game;
import model.Ship;
import view.Menu;
import view.Window;

class StartupTests {

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
	 * Test startup player turn
	 */
	@Test
	void testStartUpPlayerTurn() {
		win.gameController.createGame("Tesst", win.board, win.playerShips, null, null);
		String playerName = win.gameController.getGame().getCurrPlayer().getName();
		System.out.println(playerName);
		assertEquals(playerName, "TestPlayer");
	}
	
	/**
	 * Test startup player scores
	 */
	@Test
	void testStartUpPlayerScores() {
		win.gameController.createGame("Tesst", win.board, win.playerShips, null, null);
		assertEquals(win.gameController.getGame().getPlayerOneResults(), 0);
		assertEquals(win.gameController.getGame().getPlayerTwoResults(), 0);
	}
	
	/**
	 * Test startup game mode
	 */
	@Test
	void testStartUpGameMode() {
		win.gameController.createGame("Tesst", win.board, win.playerShips, null, null);
		System.out.println(win.gameController.getGame().getGameMode());
		assertEquals(win.gameController.getGame().getGameMode(), Game.GAME_MODE_AI);
	}
	
	/**
	 * Test startup players
	 */
	@Test
	void testStartUpPlayers() {
		win.gameController.createGame("Tesst", win.board, win.playerShips, null, null);
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
	 * Test startup UI Creation File Menu
	 */
	@Test
	void testStartupFileMenuCreation() {
		Menu win = new Menu();
		assertNotNull(win.createFileMenu());
	}
	
	/**
	 * Test startup UI Creation Menu Bar
	 */
	@Test
	void testStartUpMenuBarCreation() {
		Menu win = new Menu();
		assertNotNull(win.createMenu());
	}
	
	@Test
	void testShipHoles() {
		Ship s = new Ship("testship", 4);
		for(int i = 0; i < 4; i++) {
			assertFalse(s.holes[i]);
		}
	}
}
