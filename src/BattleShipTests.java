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
import view.Menu;
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
	 * Test game winner
	 */
	@Test
	void testGameWinner() {
		win.gameController.createGame("Tesst", win.board, win.playerShips, null, null);
		boolean winner = false;
		for(int i = 0; i < Board.BOARD_ROWS; i++) {
			for(int j = 0; j < Board.BOARD_COLS; j++) {
				win.gameController.getGame().processAttack(i, j);
				if(Game.isFinished == true) {
					winner = true;
					break;
				}
			}
		}
		if(winner == true)
			assertNotNull(Game.getWinner());
		else
			assertNull(Game.getWinner());
	}
	
	
}
