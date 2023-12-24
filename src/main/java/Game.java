import cards.Card;
import cards.CardType;
import players.Player;

import java.util.Arrays;
import java.util.Collections;
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
        .unordered()
        .parallel()
        .filter(i -> i != 8 && i != 9)
        .boxed()
        .flatMap(number -> Arrays.stream(CardType.values()).parallel().map(type -> Card.of(number, type)));
  }
  private List<Card> shuffleCards(Stream<Card> cards) {
    var shuffledCards = cards.collect(Collectors.toList());
    Collections.shuffle(shuffledCards);
    return shuffledCards.stream().toList();
  }
}
