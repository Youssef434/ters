package players;

import cards.Card;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static cards.CardType.*;
import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {
  @Test
  public void testPlayIsSuccessful() {
    var player = Player.of(Team.A, "", Stream.of(Card.of(10, BASTOS), Card.of(2, OROS)).collect(Collectors.toSet()));

    assertEquals(Card.of(2, OROS), player.play(2, OROS));
    assertEquals(Set.of(Card.of(10, BASTOS)), player.cards());
  }

  @Test
  public void testPlayIsInvalid() {
    var player = Player.of(Team.A, "", Stream.of(Card.of(10, BASTOS), Card.of(2, OROS)).collect(Collectors.toSet()));

    assertThrows(IllegalArgumentException.class, () -> player.play(22, OROS));
  }
}
