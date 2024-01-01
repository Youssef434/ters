package services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import players.Player;

import java.util.List;
import java.util.Set;

public class CardsServiceTest {
  private static List<Player> distributedCards;
  @BeforeAll
  public static void init() {
    distributedCards = CardsService.create().distribute(new String[] {"P1", "P2", "P3", "P4"});
  }

  @Test
  public void testIsDistributedCardsSizeEqualTo40() {
    long overallNumberOfCards = distributedCards.stream()
        .map(Player::cards)
        .mapToInt(Set::size)
        .sum();
    Assertions.assertEquals(40, overallNumberOfCards);
  }
}
