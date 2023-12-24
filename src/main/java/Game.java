import cards.Card;
import cards.CardType;
import players.Player;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Game {
  public static void main(String[] args) {
    System.out.println(new Game().generateCards().collect(Collectors.toUnmodifiableSet()));
  }
  public List<Player> distribute() {
    return null;
  }

  private Stream<Card> generateCards() {
    return IntStream.rangeClosed(1, 12)
        .boxed()
        .flatMap(number -> Arrays.stream(CardType.values()).map(type -> Card.of(number, type)));
  }
}
