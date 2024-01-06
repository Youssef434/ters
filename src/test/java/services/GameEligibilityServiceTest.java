package services;

import cards.Card;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import players.Player;
import players.Team;

import java.util.List;
import java.util.Set;

import static cards.CardType.*;
import static org.junit.jupiter.api.Assertions.*;

public class GameEligibilityServiceTest {
  private static GameEligibilityService gameEligibilityService;

  @BeforeAll
  public static void init() {
    gameEligibilityService = GameEligibilityService.create();
  }

  @Test
  public void testPlayerHavingMoreThan6CardsOfTheSameType() {
    var players = List.of(Player.of(Team.A, "", Set.of(
        Card.of(1, BASTOS),
        Card.of(2, BASTOS),
        Card.of(3, BASTOS),
        Card.of(12, BASTOS),
        Card.of(11, BASTOS),
        Card.of(10, BASTOS),
        Card.of(4, BASTOS),
        Card.of(3, OROS),
        Card.of(1, COPAS),
        Card.of(1, ESPADAS))));

     boolean isEligible = gameEligibilityService.isEligible(players);

    assertFalse(isEligible);
  }

  @Test
  public void testPlayerHavingLessThanFourThirdsInHisHand() {
    var players = List.of(Player.of(Team.A, "", Set.of(
        Card.of(5, BASTOS),
        Card.of(6, BASTOS),
        Card.of(7, BASTOS),
        Card.of(12, BASTOS),
        Card.of(11, BASTOS),
        Card.of(10, BASTOS),
        Card.of(7, COPAS),
        Card.of(4, OROS),
        Card.of(4, COPAS),
        Card.of(4, ESPADAS))));

    boolean isEligible = gameEligibilityService.isEligible(players);

    assertFalse(isEligible);
  }

  @Test
  public void testGameIsEligible() {
    var players = List.of(Player.of(Team.A, "", Set.of(
        Card.of(1, BASTOS),
        Card.of(2, BASTOS),
        Card.of(3, BASTOS),
        Card.of(12, BASTOS),
        Card.of(11, BASTOS),
        Card.of(10, BASTOS),
        Card.of(2, COPAS),
        Card.of(3, OROS),
        Card.of(1, COPAS),
        Card.of(1, ESPADAS))));

    boolean isEligible = gameEligibilityService.isEligible(players);

    assertTrue(isEligible);
  }
}
