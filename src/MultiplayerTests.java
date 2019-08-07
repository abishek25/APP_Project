import static org.junit.jupiter.api.Assertions.*;

import javax.swing.JButton;
import javax.swing.JLabel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import controller.GameController;
import model.Board;
import model.Game;
import view.Menu;
import view.Window;

class MultiplayerTests {

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
		
		win.gameController = new GameController(1, "TestPlayer", "TestPlayer2", true);
		win.board = new Board();
		win.playerShips = new String[5];
		Window.DEV_TEST = 5;
	}

	
	@Test
	public void testOwnPort() {
		win.gameController.createMultiplayerGame(win.board, win.playerShips);
		assertEquals(win.gameController.getOwnPort(), Menu.PORT_CREATOR);
	}
	
	@Test
	public void testOwnAddress() {
		win.gameController.createMultiplayerGame(win.board, win.playerShips);
		assertEquals(win.gameController.getOwnAddr(), Menu.ADDR_CREATOR);
	}
	
	@Test
	public void testOtherPlayerPort() {
		win.gameController.createMultiplayerGame(win.board, win.playerShips);
		assertEquals(win.gameController.getOtherPlayerPort(), Menu.PORT_JOINER);
	}
	
	@Test
	public void testOtherPlayerAddr() {
		win.gameController.createMultiplayerGame(win.board, win.playerShips);
		assertEquals(win.gameController.getOtherPlayerAddr(), Menu.ADDR_JOINER);
	}
	
	@Test
	public void testOpponentTurn() {
		win.gameController.createMultiplayerGame(win.board, win.playerShips);
		assertFalse(win.gameController.checkIfOpponentTurn());
	}
	
	@Test
	public void testMultiplayerPlayerTurn() {
		win.gameController.createMultiplayerGame(win.board, win.playerShips);
		assertEquals(win.gameController.multiplayerGetCurrPlayerName(), "TestPlayer");
	}
	
	@Test
	public void testPlayerShipsAlive() {
		win.gameController.createMultiplayerGame(win.board, win.playerShips);
		assertEquals(win.gameController.getGame().players[0].numShipsAlive, 5);
	}
	
	/**
	 * Test startup game mode for multiplayer
	 */
	@Test
	void testMultiplayerStartUpGameMode() {
		win.gameController.createMultiplayerGame(win.board, win.playerShips);
		assertEquals(win.gameController.getGame().getGameMode(), Game.GAME_MODE_NETWORK);
	}
	
	/**
	 * Test startup players for multiplayer
	 */
	@Test
	void testStartUpPlayers() {
		win.gameController.createMultiplayerGame(win.board, win.playerShips);
		assertEquals(win.gameController.getGame().players.length, 1);
	}
	
	/**
	 * Test startup player scores for multiplayer
	 */
	@Test
	void testMultiplayerStartUpPlayerScores() {
		win.gameController.createMultiplayerGame(win.board, win.playerShips);
		assertEquals(win.gameController.getGame().getPlayerOneResults(), 0);
	}
}
