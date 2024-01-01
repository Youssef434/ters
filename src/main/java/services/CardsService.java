package services;

import cards.Card;
import cards.CardType;
import players.Player;
import players.Team;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public interface CardsService {
  List<Player> distribute(String[] names);

  static CardsService create() {
    return new CardsServiceImpl();
  }

  final class CardsServiceImpl implements CardsService {
    private CardsServiceImpl() {}

    public List<Player> distribute(String[] names) {
      var cards = shuffle(generate()).toList();
      return IntStream.range(0, 4)
          .mapToObj(i -> Player.of(
              currentTeam(i),
              names[i],
              cards.subList(i * 10, Math.min((i + 1) * 10, cards.size()))
                  .stream()
                  .sorted()
                  .collect(Collectors.toCollection(LinkedHashSet::new))))
          .toList();
    }

    private Stream<Card> generate() {
      return IntStream.rangeClosed(1, 12)
          .unordered()
          .parallel()
          .filter(i -> i != 8 && i != 9)
          .boxed()
          .flatMap(number -> Arrays.stream(CardType.values()).parallel().map(type -> Card.of(number, type)));
    }

    private Stream<Card> shuffle(Stream<Card> cards) {
      var shuffledCards = cards.collect(Collectors.toList());
      Collections.shuffle(shuffledCards);
      return shuffledCards.stream();
    }

    private Team currentTeam(int index) {
      return index % 2 == 0 ? Team.A : Team.B;
    }
  }
}
