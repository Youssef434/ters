package game;

import cards.Card;
import cards.CardType;
import players.Player;
import players.Team;
import services.GameRulesService;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Game {
  public List<Player> distribute(String[] names) {
    var cards = shuffleCards(generateCards()).toList();
    var gameRulesService = GameRulesService.create();
    return IntStream.range(0, 4)
        .mapToObj(i -> new Player(
            currentTeam(i),
            names[i],
            cards.subList(i * 10, Math.min((i + 1) * 10, cards.size())).stream().collect(Collectors.toUnmodifiableSet()),
            gameRulesService))
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

  private Team currentTeam(int index) {
    return index % 2 == 0 ? Team.A : Team.B;
  }
}
