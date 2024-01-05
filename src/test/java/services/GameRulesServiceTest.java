package services;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import players.Player;
import players.Team;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameRulesServiceTest {
  private static GameRulesService gameRulesService;

  @BeforeAll
  public static void init() {
    gameRulesService = GameRulesService.create();
  }

  @Test
  public void testPlayerWithEmptyHand() {
    var player = Player.of(Team.A, "", Set.of());

    var playableCards = gameRulesService.playableCards(player, null);

    assertEquals(Set.of(), playableCards);
  }
}
