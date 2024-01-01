package services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import players.Player;

import java.util.List;
import java.util.Set;

public class CardsServiceTest {
  private static List<Player> players;
  @BeforeAll
  public static void init() {
    players = CardsService.create().distribute(new String[] {"P1", "P2", "P3", "P4"});
  }

  @Test
  public void testIsDistributedCardsSizeEqualTo40() {
    long overallNumberOfCards = players.stream()
        .map(Player::cards)
        .mapToInt(Set::size)
        .sum();
    Assertions.assertEquals(40, overallNumberOfCards);
  }

  @Test
  public void testNumberOfPlayersEqualTo4() {
    Assertions.assertEquals(4, players.size());
  }
}
