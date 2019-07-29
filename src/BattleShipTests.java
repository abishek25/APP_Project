import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import controller.GameController;
import model.Board;
import model.Game;
import model.Player;
import model.Ship;
import view.Window;

class BattleShipTests {

	@Test
	void test1() {
		Ship ship = new Ship("test", 4);
		ship.initHoles(4);
		for(int i = 0; i < 4; i++) {
			assertFalse(ship.holes[i]);
		}
	}
	
	@Test
	void test2() {
		Ship ship = new Ship("test", 4);
		ship.initHoles(4);
		assertFalse(ship.isShipSunk());
	}
	
	@Test
	void test3() {
		Ship ship = new Ship("test", 4);
		ship.initHoles(4);
		assertTrue(ship.shipHit());
	}
	
	@Test
	void test4() {
		Ship ship = new Ship("test", 4);
		ship.initHoles(4);
		ship.shipHit();
		assertTrue(ship.holes[0]);
	}
	
	@Test
	void test5() {
		Player p = new Player("playerone", "");
		assertEquals(p.ships.length, 5);
	}
	
	@Test
	void test6() {
		Player p = new Player("playerone", "");
		assertEquals(p.getName(), "playerone");
	}
	
	@Test
	void test7() {
		Player p = new Player("playerone", "");
		assertNotNull(p.getBoard());
	}
	
	@Test
	void test8() {
		Game g = new Game("p1", "", "p2", "");
		assertEquals(g.getGameMode(), 2);
	}
	
	@Test
	void test9() {
		Game g = new Game("p1", "", "p2", "");
		assertNotNull(g.getGameBoard());
	}
	
	@Test
	void test10() {
		Game g = new Game("p1", "", "p2", "");
		assertEquals(g.getCurrPlayer().getName(), "p1");
	}
	
	@Test
	void test11() {
		Game g = new Game("p1", "", "p2", "");
		assertEquals(Game.checkIfGameWon(), false);
	}
	
	@Test
	void test12() {
		Game g = new Game("p1", "", "p2", "");
		assertEquals(g.getPlayerTwoResults(), 0);
	}
	
	@Test
	void test13() {
		Game g = new Game("p1", "", "p2", "");
		assertEquals(g.getPlayerOneResults(), 0);
	}
	
	@Test
	void test14() {
		Board b = new Board();
		assertEquals(b.ship_placement_grid[0][0], Board.BOARD_EMPTY);
	}
	
	@Test
	void test15() {
		Board b = new Board();
		assertEquals(b.getAttackGrid()[0][0], Board.BOARD_EMPTY);
	}
	
	@Test
	void test16() {
		Board b = new Board();
		b.handleShipAttack(0, 0);
		assertEquals(b.ship_placement_grid[0][0], Board.PLACEMENT_BOARD_SHIP_HIT);
	}
	
	@Test
	void test17() {
		GameController gc = new GameController(1);
		assertEquals(gc.gameMode, 1);
	}
	
	@Test
	void test18() {
		GameController gc = new GameController(1);
		assertNull(gc.getGame());
	}
	
	@Test
	void test19() {
		String s[] = {"1,1,1,1,1,1","1,1,1,1,1,1","1,1,1,1,1,1","1,1,1,1,1,1","1,1,1,1,1,1"};
		GameController gc = new GameController(1);
		gc.createGame("p1", new Board(), s);
		assertEquals(gc.getP1Score(), 0);
	}
	
	@Test
	void test20() {
		String s[] = {"1,1,1,1,1,1","1,1,1,1,1,1","1,1,1,1,1,1","1,1,1,1,1,1","1,1,1,1,1,1"};
		GameController gc = new GameController(1);
		gc.createGame("p1", new Board(), s);
		assertEquals(gc.getAIScore(), 0);
	}
	
	@Test
	void test21() {
		String s[] = {"1,1,1,1,1,1","1,1,1,1,1,1","1,1,1,1,1,1","1,1,1,1,1,1","1,1,1,1,1,1"};
		GameController gc = new GameController(1);
		gc.createGame("p1", new Board(), s);
		assertEquals(gc.getCurrPlayer().getName(), "p1");
	}
	
	@Test
	void test22() {
		String s[] = {"1,1,1,1,1,1","1,1,1,1,1,1","1,1,1,1,1,1","1,1,1,1,1,1","1,1,1,1,1,1"};
		GameController gc = new GameController(1);
		gc.createGame("p1", new Board(), s);
		assertNotNull(gc.getGame());
	}
	
	@Test
	void test23() {
		Window win = new Window();
		assertNotNull(win.createFileMenu());
	}
	
	@Test
	void test24() {
		Window win = new Window();
		assertNotNull(win.createMenu());
	}
	
	@Test
	void test25() {
		Window win = new Window();
		win.initMainFrame();
		assertEquals(win.playerShips.length, 5);
	}
}
