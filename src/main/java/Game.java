import cards.Card;
import cards.CardType;
import players.Player;
import players.Team;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Game {
  public static void main(String[] args) {
    System.out.println(new Game().distribute());
  }
  public List<Player> distribute() {
    var cards = shuffleCards(generateCards()).toList();
    return IntStream.range(0, 4)
        .mapToObj(i -> cards.subList(i * 10, Math.min((i + 1) * 10, cards.size())))
        .flatMap(e -> Arrays.stream(Team.values()).map(t -> new Player(t, e.stream().collect(Collectors.toUnmodifiableSet()))))
        .toList();
  }

  private Stream<Card> generateCards() {
    return IntStream.rangeClosed(1, 12)
        .unordered()
        .parallel()
        .filter(i -> i != 8 && i != 9)
        .boxed()
        .flatMap(number -> Arrays.stream(CardType.values()).parallel().map(type -> Card.of(number, type)));
  }

  private Stream<Card> shuffleCards(Stream<Card> cards) {
    var shuffledCards = cards.collect(Collectors.toList());
    Collections.shuffle(shuffledCards);
    return shuffledCards.stream();
  }
}
