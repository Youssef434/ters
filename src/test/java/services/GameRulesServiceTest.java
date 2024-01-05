package services;

import cards.Card;
import cards.CardType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import players.Player;
import players.Team;

import java.util.Set;

import static cards.CardType.BASTOS;
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

  @Test
  public void testPlayerWithAllCardsHavingDominantType() {
    var playerCards = Set.of(Card.of(10, BASTOS), Card.of(11, BASTOS), Card.of(4, BASTOS));
    var player = Player.of(Team.A, "", playerCards);

    var playableCards = gameRulesService.playableCards(player, BASTOS);

    assertEquals(playerCards, playableCards);
  }
}
